package fr.uge.ifsCars;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.rpc.ServiceException;

import fr.uge.database.DataBase;
import fr.uge.database.DataBaseServiceLocator;
import fr.uge.database.DataBaseSoapBindingStub;
import fr.uge.objects.Vehicle;
import fr.uge.utils.Serialization;

public class Garage extends UnicastRemoteObject implements IGarage {
	private DataBase db;
	/**
	 * Key : vehicle ID <br/>
	 * Value : La file des employés en attente pour le véhicule ou en cours de location (1er element de la file)
	 */
	private final Map<Long, Queue<Tenant>> rentalsQueues;
	
	public Garage() throws RemoteException, ServiceException {
		super();
		
		this.db = new DataBaseServiceLocator().getDataBase();
		((DataBaseSoapBindingStub) this.db).setMaintainSession(true);
		
		this.rentalsQueues = new ConcurrentHashMap<>();
	}
	
	/**
	 * Retire de la file d'attente tous les demandeurs de location déconnectés.
	 * Si l'actuel locataire du véhicule est déconnecté, la méthode renvoie True et il n'est pas retiré.
	 * 
	 * @param vehicleId L'identifiant du véhicule dont il faut nettoyer la file d'attente
	 * @return True si le locataire actuel est deconnecté, False sinon
	 */
	private boolean removeDisconnectedTenants(long vehicleId) {
		if (!rentalsQueues.containsKey(vehicleId)) {
			throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'est pas en cours de location");
		}
		
		var queue = rentalsQueues.get(vehicleId);
		var it = queue.iterator();
		int i = 0;
		boolean currentTenantDisconnected = false;
		while (it.hasNext()) {
			try {
				it.next().getId();
			} catch (RemoteException re) {
				if (i == 0) {
					// La référence avec le locataire courant du véhicule est perdu, on va appeler endRent() plutôt que de le retirer ici
					// pour pouvoir notifier le prochain locataire dans la file qu'il est le nouveau locataire
					System.err.println("La référence avec l'actuel locataire du véhicule " + vehicleId + " est perdu, on met fin à sa location");
					currentTenantDisconnected = true;
				} else {
					System.err.println("La référence avec le " + (i+1) + "ème locataire de la file du véhicule " + vehicleId + " est perdu, on le retire de la file");
					it.remove();
				}
			}
			
			i++;
		}
		
		return currentTenantDisconnected;
	}
	
	/**
	 * Détermine si l'employé donné en paramètre loue déjà le véhicule donné en paramètre.
	 * (Ne vérifie pas s'il est dans l'éventuelle file d'attente).
	 * 
	 * @param tenant L'employé dont il faut vérifier s'il loue le véhicule
	 * @param vehicleId L'identifiant du véhicule
	 * @return True si l'employé est en train de louer le véhicule, False sinon
	 * @throws RemoteException Si le locataire du véhicule est déconnecté
	 */
	private boolean isRenting(Tenant tenant, long vehicleId) throws RemoteException {
		if (!rentalsQueues.containsKey(vehicleId)) {
			throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'est pas en cours de location");
		}
		
		var queue = rentalsQueues.get(vehicleId);
		return queue.peek().getId() == tenant.getId();
	}
	
	/**
	 * Détermine si l'employé donné en paramètre est dans l'éventuelle file d'attente du véhicule donné en paramètre.
	 * 
	 * @param tenant L'employé dont il faut vérifier s'il se situe dans la file d'attente du véhicule
	 * @param vehicleId L'identifiant du véhicule
	 * @return True si l'employé dans la file d'attente véhicule, False sinon
	 * @throws RemoteException Si un des employés dans la file d'attente est déconnecté
	 */
	private boolean isInQueue(Tenant tenant, long vehicleId) throws RemoteException {
		if (!rentalsQueues.containsKey(vehicleId)) {
			throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'est pas en cours de location");
		}
		
		var queue = rentalsQueues.get(vehicleId);
		int i = 0;
		
		for (var otherTenant : queue) {
			if (i != 0 && otherTenant.getId() == tenant.getId()) {
				return true;
			}
			i++;
		}
		
		return false;
	}
	
	@Override
	public boolean rent(Tenant tenant, long vehicleId) throws SQLException, IllegalArgumentException, RemoteException {
		if (!db.employeeExists(tenant.getId())) {
			throw new IllegalArgumentException("Erreur lors de la location d'un véhicule : L'employé " + tenant.getId() + " n'existe pas dans la base !");
		}
		
		if (!db.vehicleExists(vehicleId)) {
			throw new IllegalArgumentException("Erreur lors de la location d'un véhicule : Le véhicule " + vehicleId + " n'existe pas dans la base !");
		}
		
		if (rentalsQueues.containsKey(vehicleId)) {
			// Le véhicule est déjà en cours de location par un autre employé, on met clientId dans la file d'attente.
			var queue = rentalsQueues.get(vehicleId);
			
			// On vérifie que les remoteObject dans cette file existent toujours (non déconnecté du serveur), sinon on les retire de la file. (Nettoyage)
			if (removeDisconnectedTenants(vehicleId)) {
				endRent(vehicleId);
			}
			
			// On parcours la file d'attente pour vérifier si `tenant` n'est pas déjà dans la file voir déjà en location avec ce véhicule.
			if (isRenting(tenant, vehicleId) || isInQueue(tenant, vehicleId)) {
				throw new IllegalArgumentException("L'employé " + tenant.getId() + " loue déjà ou est dans la file d'attente pour louer ce véhicule");
			}
			
			queue.add(tenant);
			registerRental(tenant, vehicleId);
			
			return false;
		} else {
			// Le véhicule est disponible, `tenant` en devient le locataire.
			var queue = new ConcurrentLinkedQueue<Tenant>();
			queue.add(tenant);
			rentalsQueues.put(vehicleId, queue);
			registerRental(tenant, vehicleId);
			return true;
		}
	}
	
	@Override
	public void endRent(long vehicleId) throws IllegalArgumentException {
		if (!rentalsQueues.containsKey(vehicleId)) {
			throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'est pas en cours de location");
		}
		
		var queue = rentalsQueues.get(vehicleId);
		queue.poll(); // Locataire sortant
		
		if (queue.isEmpty()) {
			rentalsQueues.remove(vehicleId);
		} else {
			var nextTenant = queue.peek();
			try {
				nextTenant.notifyRented(vehicleId);
			} catch (RemoteException e) {
				System.err.println("Le nouveau locataire du véhicule " + vehicleId + " n'est plus référencé, on passe au suivant dans la file ...");
				try {
					endRent(vehicleId);
				} catch (IllegalArgumentException iae) {
					// La file était constituée de locataire(s) déconnecté(s), la file est maintenant libérée et le véhicule disponible
				}
			}
		}
	}
	
	@Override
	public void removeFromQueue(Tenant tenant, long vehicleId) throws IllegalArgumentException, RemoteException {
		if (!rentalsQueues.containsKey(vehicleId)) {
			throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'est pas en cours de location");
		}
		
		// On vérifie que les remoteObject dans cette file existent toujours (non déconnecté du serveur), sinon on les retire de la file. (Nettoyage)
		if (removeDisconnectedTenants(vehicleId)) {
			endRent(vehicleId);
		}
		
		if (!isInQueue(tenant, vehicleId)) {
			throw new IllegalArgumentException("L'employé " + tenant.getId() + " n'est pas dans file d'attente pour le véhicule " + vehicleId);
		}
		
		var queue = rentalsQueues.get(vehicleId);
		var it = queue.iterator();
		int i = 0;
		while (it.hasNext()) {
			var currentTenantId = it.next().getId();
			if (i != 0 && currentTenantId == tenant.getId()) {
				it.remove();
			}
			i++;
		}
	}
	
	@Override
	public boolean isRented(long vehicleId) throws RemoteException {
		return rentalsQueues.containsKey(vehicleId);
	}
	
	@Override
	public void grade(String rentalDate, Tenant tenant, long vehicleId, int vehicleGrade, int conditionGrade) throws SQLException, IllegalArgumentException, RemoteException {
		if (!db.employeeExists(tenant.getId())) {
			throw new IllegalArgumentException("Erreur lors de l'ajout de note d'un véhicule : L'employé " + tenant.getId() + " n'existe pas dans la base !");
		}
		
		if (!db.vehicleExists(vehicleId)) {
			throw new IllegalArgumentException("Erreur lors de l'ajout de note d'un véhicule : Le véhicule " + vehicleId + " n'existe pas dans la base !");
		}
		
		if (vehicleGrade < 0 || vehicleGrade > 10) {
			throw new IllegalArgumentException("La note du véhicule doit être comprise entre 0 et 10 inclus");
		}
		
		if (conditionGrade < 0 || conditionGrade > 10) {
			throw new IllegalArgumentException("La note de l'état du véhicule doit être comprise entre 0 et 10 inclus");
		}
		
		if (!db.hasRentedVehicle(tenant.getId(), vehicleId)) {
			throw new IllegalArgumentException("Impossible de noter le véhicule " + vehicleId + " : L'employé " + tenant.getId() + " ne l'a pas loué");
		}
		
		db.addGrade(rentalDate, tenant.getId(), vehicleId, vehicleGrade, conditionGrade);
	}
	
	@Override
	public Vehicle getVehicle(long vehicleId) throws RemoteException, IllegalArgumentException {
		if (!db.vehicleExists(vehicleId)) {
			throw new IllegalArgumentException("Erreur : Le véhicule " + vehicleId + " n'existe pas dans la base !");
		}
		
		try {
			return (Vehicle) Serialization.deserialize(db.getVehicle(vehicleId));
		} catch (ClassNotFoundException | IOException e) {
			return null;
		}
	}
	
	@Override
	public Vehicle[] getRentingVehicles(Tenant tenant) throws RemoteException {
		var vehicles = new ArrayList<Vehicle>();
		
		for (var vehicleId : rentalsQueues.keySet()) {
			// On vérifie que les remoteObject dans cette file existent toujours (non déconnecté du serveur), sinon on les retire de la file. (Nettoyage)
			if (removeDisconnectedTenants(vehicleId)) {
				endRent(vehicleId);
			}
			
			if (rentalsQueues.get(vehicleId).peek().getId() == tenant.getId()) {
				vehicles.add(getVehicle(vehicleId));
			}
		}
		
		var array = new Vehicle[vehicles.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = vehicles.get(i);
		}
		
		return array;
	}
	
	/**
	 * Enregistre la location dans la base de données.
	 * 
	 * @param tenant L'employé louant le véhicule
	 * @param vehicleId L'identifiant du véhicule loué
	 * @throws SQLException Si la connexion avec la base de données a été interrompue
	 * @throws RemoteException
	 */
	private void registerRental(Tenant tenant, long vehicleId) throws SQLException, RemoteException {
	    SimpleDateFormat format = new SimpleDateFormat(db.getDateFormat());
	    Date now = new Date();
	    String strDate = format.format(now);
		
		db.registerRental(strDate, tenant.getId(), vehicleId);
	}
	
	@Override
	public Vehicle[] getVehiclesList() throws SQLException, RemoteException {
		var SerializedVehicles = db.getAllVehicles();
		var vehicles = new Vehicle[SerializedVehicles.length];
		
		for (int i = 0; i < SerializedVehicles.length; i++) {
			try {
				vehicles[i] = (Vehicle) Serialization.deserialize(SerializedVehicles[i]);
			} catch (ClassNotFoundException | IOException e) {
				vehicles[i] = null;
			}
		}
		
		return vehicles;
	}
	
	@Override
	public Vehicle[] getRentedVehicles(Tenant tenant) throws SQLException, RemoteException {
		var SerializedVehicles = db.getRentedVehicles(tenant.getId());
		var vehicles = new Vehicle[SerializedVehicles.length];
		
		for (int i = 0; i < SerializedVehicles.length; i++) {
			try {
				vehicles[i] = (Vehicle) Serialization.deserialize(SerializedVehicles[i]);
			} catch (ClassNotFoundException | IOException e) {
				vehicles[i] = null;
			}
		}
		
		return vehicles;
	}
	
	@Override
    public Vehicle[] getPendingsVehicles(Tenant tenant) throws RemoteException {
        var vehicles = new ArrayList<Vehicle>();

        for (var vehicleId : rentalsQueues.keySet()) {
            if (isInQueue(tenant, vehicleId)) {
                vehicles.add(getVehicle(vehicleId));
            }
        }

        var array = new Vehicle[vehicles.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = vehicles.get(i);
        }

        return array;
    }
}
