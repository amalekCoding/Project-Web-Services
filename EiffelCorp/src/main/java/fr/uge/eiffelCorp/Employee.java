package fr.uge.eiffelCorp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fr.uge.ifsCars.Tenant;

public class Employee extends UnicastRemoteObject implements Tenant {
	private final long id;
	private static final Map<Long, Employee> instances = new ConcurrentHashMap<Long, Employee>();
	
	public static Employee getEmployee(long id) throws RemoteException {
		if (!instances.containsKey(id)) {
			instances.put(id, new Employee(id));
		}
		
		return instances.get(id);
	}
	
	private Employee(long id) throws RemoteException {
		super();
		this.id = id;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public void notifyRented(long vehicleId) {
		System.out.println("Le client " + id + " est maintenant locataire du véhicule " + vehicleId);
	}
}
