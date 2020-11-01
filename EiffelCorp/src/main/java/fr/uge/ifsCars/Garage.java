package fr.uge.ifsCars;

import java.sql.SQLException;

import fr.uge.database.DataBase;

public class Garage {
	private final DataBase db;
	
	public Garage() {
		this.db = DataBase.getDatabase();
	}
	
	/**
	 * Tente de louer un véhicule au client, si le véhicule est disponible.
	 * S'il ne l'est pas, le client est mis en file d'attente et notifié lorsque
	 * le véhicule s'est libéré.
	 * 
	 * @param clientId L'identifiant du client
	 * @param vehicleId L'identifiant du véhicule
	 * @return True si la voiture a pu être loué, et False si le client est mis en liste d'attente.
	 * @throws SQLException 
	 * @throws IllegalArgumentException Si l'identifiant du client ou du véhicule est incorrect (n'existe pas dans la base).
	 */
	public boolean rent(long clientId, long vehicleId) throws SQLException, IllegalArgumentException {
		if (!db.clientExists(clientId)) {
			throw new IllegalArgumentException("Erreur lors de la location d'un véhicule : Le client " + clientId + " n'existe pas dans la base !");
		}
		
		if (!db.vehicleExists(vehicleId)) {
			throw new IllegalArgumentException("Erreur lors de la location d'un véhicule : Le véhicule \" + vehicleId + \" n'existe pas dans la base !");
		}
		
		return false;
	}
	
	/**
	 * Note un véhicule que le client a loué.
	 * 
	 * @param clientId L'identifiant du client
	 * @param vehicleId L'identifiant du véhicule
	 * @param vehicleGrade Note du véhicule
	 * @param conditionGrade Note de l'état du véhicule
	 * @return False si un des arguments est incorrect (client ou vehicule non trouvé, ou note invalide (non comprise entre 0 et 10)).
	 * @throws SQLException 
	 * @throws IllegalArgumentException Si l'identifiant du client ou du véhicule est incorrect (n'existe pas dans la base), ou si les notes
	 * 									ne sont pas comprises entre 0 et 10 inclus.
	 */
	public boolean grade(long clientId, long vehicleId, int vehicleGrade, int conditionGrade) throws SQLException {
		if (!db.clientExists(clientId)) {
			throw new IllegalArgumentException("Erreur lors de la location d'un véhicule : Le client " + clientId + " n'existe pas dans la base !");
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
		
		db.addGrade(clientId, vehicleId, vehicleGrade, conditionGrade);
		return true;
	}
}
