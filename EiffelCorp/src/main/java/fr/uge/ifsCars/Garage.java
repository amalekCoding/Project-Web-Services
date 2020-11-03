package fr.uge.ifsCars;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import fr.uge.database.DataBase;

public class Garage extends UnicastRemoteObject implements IGarage {
	private final DataBase db;
	private final Map<Long, Queue<Tenant>> rentalsQueues; // Key : vehicle ID ; Value : La file des clients en attente pour le v�hicule ou en cours de location (1er element de la file)
	
	public Garage() throws RemoteException {
		super();
		this.db = DataBase.getDatabase();
		this.rentalsQueues = new HashMap<>();
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
			for (Tenant t : queue) {
				if (t.getId() == tenant.getId()) {
					throw new IllegalArgumentException("Le client " + tenant.getId() + " loue déjà ou est dans la file d'attente pour louer ce véhicule");
				}
			}
			
			queue.add(tenant);
			return false;
		} else {
			// Le véhicule est disponible, clientId en devient le locataire.
			var queue = new LinkedList<Tenant>();
			queue.add(tenant);
			rentalsQueues.put(vehicleId, queue);
			return true;
		}
	}
	
	@Override
	public void endRent(long vehicleId) throws SQLException, IllegalArgumentException, RemoteException {
		if (!rentalsQueues.containsKey(vehicleId)) {
			throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'est pas en cours de location");
		}
		
		var queue = rentalsQueues.get(vehicleId);
		queue.poll(); // Dernier locataire
		if (queue.isEmpty()) {
			rentalsQueues.remove(vehicleId);
		} else {
			var nextTenant = queue.peek();
			nextTenant.notifyRented(vehicleId);
		}
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
