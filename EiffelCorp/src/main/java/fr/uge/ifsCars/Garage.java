package fr.uge.ifsCars;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.uge.database.DataBase;

public class Garage extends UnicastRemoteObject implements IGarage {
	private final DataBase db;
	private final Map<Long, Queue<Tenant>> rentalsQueues; // Key : vehicle ID ; Value : La file des clients en attente pour le véhicule ou en cours de location (1er element de la file)
	
	public Garage() throws RemoteException {
		super();
		this.db = DataBase.getDatabase();
		this.rentalsQueues = new ConcurrentHashMap<>();
	}
	
	@Override
	public boolean rent(Tenant tenant, long vehicleId) throws SQLException, IllegalArgumentException, RemoteException {
		if (!db.clientExists(tenant.getId())) {
			throw new IllegalArgumentException("Erreur lors de la location d'un véhicule : Le client " + tenant.getId() + " n'existe pas dans la base !");
		}
		
		if (!db.vehicleExists(vehicleId)) {
			throw new IllegalArgumentException("Erreur lors de la location d'un véhicule : Le véhicule " + vehicleId + " n'existe pas dans la base !");
		}
		
		if (rentalsQueues.containsKey(vehicleId)) {
			// Le véhicule est déjà en cours de location par un autre client, on met clientId dans la file d'attente.
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
						throw new IllegalArgumentException("Le client " + currentTenantId + " loue déjà ou est dans la file d'attente pour louer ce véhicule");
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
			
			if (endRent) {
				endRent(vehicleId);
			}
			
			return false;
		} else {
			// Le véhicule est disponible, clientId en devient le locataire.
			var queue = new ConcurrentLinkedQueue<Tenant>();
			queue.add(tenant);
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
	public void grade(Tenant tenant, long vehicleId, int vehicleGrade, int conditionGrade) throws SQLException, IllegalArgumentException, RemoteException {
		if (!db.clientExists(tenant.getId())) {
			throw new IllegalArgumentException("Erreur lors de la location d'un véhicule : Le client " + tenant.getId() + " n'existe pas dans la base !");
		}
		
		if (!db.vehicleExists(vehicleId)) {
			throw new IllegalArgumentException("Erreur lors de la location d'un véhicule : Le véhicule \" + vehicleId + \" n'existe pas dans la base !");
		}
		
		if (vehicleGrade < 0 || vehicleGrade > 10) {
			throw new IllegalArgumentException("La note du véhicule doit être comprise entre 0 et 10 inclus");
		}
		
		if (conditionGrade < 0 || conditionGrade > 10) {
			throw new IllegalArgumentException("La note de l'état du véhicule doit être comprise entre 0 et 10 inclus");
		}
		
		// TODO : Vérifier que le client ait le droit de noter le véhicule
		
		db.addGrade(tenant.getId(), vehicleId, vehicleGrade, conditionGrade);
	}
}
