package fr.uge.ifsCars;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Tenant extends Remote {
	long getId() throws RemoteException;
	
	/**
	 * Action � effectuer lorsque le client sort de la file d'attente et devient locataire du v�hicule.
	 * 
	 * @param vehicleId Identifiant du v�hicule nouvellement lou� par le client
	 * @throws RemoteException
	 */
	void notifyRented(long vehicleId) throws RemoteException;
}
