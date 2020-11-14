package fr.uge.ifsCars;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.rpc.ServiceException;

import fr.uge.database.DataBase;
import fr.uge.database.DataBaseServiceLocator;
import fr.uge.database.DataBaseSoapBindingStub;

public class Garage extends UnicastRemoteObject implements IGarage {
	private DataBase db;
	private final Map<Long, Queue<Tenant>> rentalsQueues; // Key : vehicle ID ; Value : La file des employés en attente pour le véhicule ou en cours de location (1er element de la file)
	
	public Garage() throws RemoteException, ServiceException {
		super();
		
		this.db = new DataBaseServiceLocator().getDataBase();
		((DataBaseSoapBindingStub) this.db).setMaintainSession(true);
		
		this.rentalsQueues = new ConcurrentHashMap<>();
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
			
			// On parcours la file d'attente pour vérifier si `tenant` n'est pas déjà dans la file voir déjà en location avec ce véhicule.
			// On vérifie également que les remoteObject dans cette file existe toujours (non déconnecté du serveur), sinon on les retire de la file.
			var it = queue.iterator();
			int i = 0;
			boolean endRent = false;
			while (it.hasNext()) {
				try {
					var currentTenantId = it.next().getId();
					if (currentTenantId == tenant.getId()) {
						throw new IllegalArgumentException("L'employé " + currentTenantId + " loue déjà ou est dans la file d'attente pour louer ce véhicule");
					}
				} catch (RemoteException re) {
					if (i == 0) {
						// La référence avec le locataire courant du véhicule est perdu, on va appeler endRent() plutôt que de le retirer ici
						// pour pouvoir notifier le prochain locataire dans la file qu'il est le nouveau locataire
						System.err.println("La référence avec l'actuel locataire du véhicule " + vehicleId + " est perdu, on met fin à sa location");
						endRent = true;
					} else {
						System.err.println("La référence avec le " + (i+1) + "ème locataire de la file du véhicule " + vehicleId + " est perdu, on le retire de la file");
						it.remove();
					}
				}
				
				i++;
			}
			
			queue.add(tenant);
			registerRental(tenant, vehicleId);
			
			if (endRent) {
				endRent(vehicleId);
			}
			
			return false;
		} else {
			// Le véhicule est disponible, `tenant` en devient le locataire.
			var queue = new ConcurrentLinkedQueue<Tenant>();
			queue.add(tenant);
			registerRental(tenant, vehicleId);
			rentalsQueues.put(vehicleId, queue);
			return true;
		}
	}
	
	@Override
	public void endRent(long vehicleId) throws SQLException, IllegalArgumentException {
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
	
	/**
	 * Enregistre la location dans la base de données
	 * 
	 * @param tenant L'employé louant le véhicule
	 * @param vehicleId L'identifiant du véhicule loué
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 * @throws RemoteException
	 */
	private void registerRental(Tenant tenant, long vehicleId) throws SQLException, RemoteException {
	    SimpleDateFormat format = new SimpleDateFormat(db.getDateFormat());
	    Date now = new Date();
	    String strDate = format.format(now);
		
		db.registerRental(strDate, tenant.getId(), vehicleId);
	}
}
