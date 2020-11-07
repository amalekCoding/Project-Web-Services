package fr.uge.ifsCars;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Tenant extends Remote {
	/**
	 * Récupère l'identifiant du locataire
	 * 
	 * @return L'identifiant du locataire
	 * @throws RemoteException
	 */
	long getId() throws RemoteException;
	
	/**
	 * Action à effectuer lorsque le futur locataire sort de la file d'attente et devient locataire du véhicule.
	 * 
	 * @param vehicleId Identifiant du véhicule nouvellement loué
	 * @throws RemoteException
	 */
	void notifyRented(long vehicleId) throws RemoteException;
}
