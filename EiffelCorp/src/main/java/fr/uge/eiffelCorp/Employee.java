package fr.uge.eiffelCorp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import fr.uge.ifsCars.Tenant;

public class Employee extends UnicastRemoteObject implements Tenant {
	private final long id;
	
	public Employee(long id) throws RemoteException {
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
