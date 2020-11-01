package fr.uge.ifsCars;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface IGarage extends Remote {
	/**
	 * Tente de louer un v�hicule au client, si le v�hicule est disponible.
	 * S'il ne l'est pas, le client est mis en file d'attente et notifi� lorsque
	 * le v�hicule s'est lib�r�.
	 * 
	 * @param tenant Le client locataire
	 * @param vehicleId L'identifiant du v�hicule
	 * @return True si la voiture a pu �tre lou�, et False si le client est mis en liste d'attente.
	 * @throws SQLException 
	 * @throws IllegalArgumentException Si l'identifiant du client ou du v�hicule est incorrect (n'existe pas dans la base),
	 * 									ou si le client est d�j� en cours de location pour ce v�hicule.
	 */
	boolean rent(Tenant tenant, long vehicleId) throws SQLException, IllegalArgumentException, RemoteException;
	
	/**
	 * Met fin � l'actuelle location du v�hicule, et enchaine avec le client suivant sur la liste d'attente, en le notifiant.
	 * 
	 * @param vehicleId L'identifiant du v�hicule
	 * @throws SQLException
	 * @throws IllegalArgumentException Si le v�hicule donn� n'est pas en cours de location.
	 */
	void endRent(long vehicleId) throws SQLException, IllegalArgumentException, RemoteException;
	
	/**
	 * Note un v�hicule que le client a lou�.
	 * 
	 * @param tenant Le client locataire
	 * @param vehicleId L'identifiant du v�hicule
	 * @param vehicleGrade Note du v�hicule
	 * @param conditionGrade Note de l'�tat du v�hicule
	 * @throws SQLException 
	 * @throws IllegalArgumentException Si l'identifiant du client ou du v�hicule est incorrect (n'existe pas dans la base), ou si les notes
	 * 									ne sont pas comprises entre 0 et 10 inclus.
	 */
	void grade(Tenant tenant, long vehicleId, int vehicleGrade, int conditionGrade) throws SQLException, IllegalArgumentException, RemoteException;
}
