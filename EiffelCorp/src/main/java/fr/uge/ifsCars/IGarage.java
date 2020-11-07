package fr.uge.ifsCars;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface IGarage extends Remote {
	/**
	 * Tente de louer un véhicule au client, si le véhicule est disponible.
	 * S'il ne l'est pas, le client est mis en file d'attente et notifié lorsque
	 * le vévéhicule s'est libéré.
	 * 
	 * @param tenant Le client locataire
	 * @param vehicleId L'identifiant du véhicule
	 * @return True si la voiture a pu être loué, et False si le client est mis en liste d'attente.
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 * @throws IllegalArgumentException Si l'identifiant du client ou du véhicule est incorrect (n'existe pas dans la base),
	 * 									ou si le client est déjà en cours de location pour ce véhicule.
	 * @throws RemoteException
	 */
	boolean rent(Tenant tenant, long vehicleId) throws SQLException, IllegalArgumentException, RemoteException;
	
	/**
	 * Met fin à l'actuelle location du véhicule, et enchaine avec le client suivant sur la liste d'attente, en le notifiant.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 * @throws IllegalArgumentException Si le véhicule donné n'est pas en cours de location.
	 * @throws RemoteException
	 */
	void endRent(long vehicleId) throws SQLException, IllegalArgumentException, RemoteException;
	
	/**
	 * Détermine si le véhicule est en cours de location.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return True si le véhicule est en cours de location, False sinon
	 * @throws RemoteException
	 */
	boolean isRented(long vehicleId) throws RemoteException;
	
	/**
	 * Note un véhicule que le client a loué.
	 * 
	 * @param tenant Le client locataire
	 * @param vehicleId L'identifiant du véhicule
	 * @param vehicleGrade Note du véhicule (entre 0 et 10 inclus)
	 * @param conditionGrade Note de l'état du véhicule (entre 0 et 10 inclus)
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 * @throws IllegalArgumentException Si l'identifiant du client ou du véhicule est incorrect (n'existe pas dans la base), ou si les notes
	 * 									ne sont pas comprises entre 0 et 10 inclus.
	 * @throws RemoteException
	 */
	void grade(Tenant tenant, long vehicleId, int vehicleGrade, int conditionGrade) throws SQLException, IllegalArgumentException, RemoteException;
}
