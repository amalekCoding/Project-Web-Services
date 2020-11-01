package fr.uge.ifsCars;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Tenant extends Remote {
	long getId() throws RemoteException;
	
	/**
	 * Action à effectuer lorsque le client sort de la file d'attente et devient locataire du véhicule.
	 * 
	 * @param vehicleId Identifiant du véhicule nouvellement loué par le client
	 * @throws RemoteException
	 */
	void notifyRented(long vehicleId) throws RemoteException;
}
