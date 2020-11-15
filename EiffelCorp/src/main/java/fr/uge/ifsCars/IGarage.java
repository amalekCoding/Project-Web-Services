package fr.uge.ifsCars;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface IGarage extends Remote {
	/**
	 * Tente de louer un véhicule à l'employé, si le véhicule est disponible.
	 * S'il ne l'est pas, l'employé est mis en file d'attente et notifié lorsque
	 * le véhicule s'est libéré.
	 * 
	 * @param tenant L'employé souhaitant louer le véhicule
	 * @param vehicleId L'identifiant du véhicule
	 * @return True si la voiture a pu être loué, et False si l'employé est mis en liste d'attente.
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 * @throws IllegalArgumentException Si l'identifiant de l'employé ou du véhicule est incorrect (n'existe pas dans la base),
	 * 									ou si l'employé est déjà en cours de location pour ce véhicule.
	 * @throws RemoteException
	 */
	boolean rent(Tenant tenant, long vehicleId) throws SQLException, IllegalArgumentException, RemoteException;
	
	/**
	 * Met fin à l'actuelle location du véhicule, et enchaine avec l'employé suivant sur la liste d'attente, en le notifiant.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @throws IllegalArgumentException Si le véhicule donné n'est pas en cours de location.
	 * @throws RemoteException
	 */
	void endRent(long vehicleId) throws IllegalArgumentException, RemoteException;
	
	/**
	 * Retire de la file d'attente un employé pour le véhicule donné.
	 * 
	 * @param tenant L'employé à retirer de la file
	 * @param vehicleId L'identifiant du véhicule
	 * @throws IllegalArgumentException Si le véhicule n'est pas en cours de location ou si l'employé ne se trouve pas dans la file d'attente de ce véhicule
	 * @throws RemoteException 
	 */
	void removeFromQueue(Tenant tenant, long vehicleId) throws IllegalArgumentException, RemoteException;
	
	/**
	 * Détermine si le véhicule est en cours de location.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return True si le véhicule est en cours de location, False sinon
	 * @throws RemoteException
	 */
	boolean isRented(long vehicleId) throws RemoteException;
	
	/**
	 * Note un véhicule que l'employé a loué.
	 * 
	 * @param rentalDate La date de début de la location, au format spécifié par le résultat de `DataBase.getDateFormat()`
	 * @param tenant L'employé ayant loué le véhicule
	 * @param vehicleId L'identifiant du véhicule
	 * @param vehicleGrade Note du véhicule (entre 0 et 10 inclus)
	 * @param conditionGrade Note de l'état du véhicule (entre 0 et 10 inclus)
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 * @throws IllegalArgumentException Si l'identifiant de l'employé ou du véhicule est incorrect (n'existe pas dans la base),
	 * 									si les notes ne sont pas comprises entre 0 et 10 inclus,
	 * 									si le format de la date est correcte,
	 * 									ou si l'employé n'a pas loué le véhicule à la date donnée.
	 * @throws RemoteException
	 */
	void grade(String rentalDate, Tenant tenant, long vehicleId, int vehicleGrade, int conditionGrade) throws SQLException, IllegalArgumentException, RemoteException;
	
	/**
	 * Récupère toutes les caractéristiques d'un véhicule depuis la base de données et renvoie l'ensemble sous forme d'un objet Vehicle.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return L'objet Vehicle correspondant
	 * @throws RemoteException
	 * @throws IllegalArgumentException Si le véhicule n'existe pas dans la base de données
	 */
	Vehicle getVehicle(long vehicleId) throws RemoteException, IllegalArgumentException;
}
