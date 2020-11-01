package fr.uge.ifsCars;

import java.sql.SQLException;

import fr.uge.database.DataBase;

public class Garage {
	private final DataBase db;
	
	public Garage() {
		this.db = DataBase.getDatabase();
	}
	
	/**
	 * Tente de louer un v�hicule au client, si le v�hicule est disponible.
	 * S'il ne l'est pas, le client est mis en file d'attente et notifi� lorsque
	 * le v�hicule s'est lib�r�.
	 * 
	 * @param clientId L'identifiant du client
	 * @param vehicleId L'identifiant du v�hicule
	 * @return True si la voiture a pu �tre lou�, et False si le client est mis en liste d'attente.
	 * @throws SQLException 
	 * @throws IllegalArgumentException Si l'identifiant du client ou du v�hicule est incorrect (n'existe pas dans la base).
	 */
	public boolean rent(long clientId, long vehicleId) throws SQLException, IllegalArgumentException {
		if (!db.clientExists(clientId)) {
			throw new IllegalArgumentException("Erreur lors de la location d'un v�hicule : Le client " + clientId + " n'existe pas dans la base !");
		}
		
		if (!db.vehicleExists(vehicleId)) {
			throw new IllegalArgumentException("Erreur lors de la location d'un v�hicule : Le v�hicule \" + vehicleId + \" n'existe pas dans la base !");
		}
		
		return false;
	}
	
	/**
	 * Note un v�hicule que le client a lou�.
	 * 
	 * @param clientId L'identifiant du client
	 * @param vehicleId L'identifiant du v�hicule
	 * @param vehicleGrade Note du v�hicule
	 * @param conditionGrade Note de l'�tat du v�hicule
	 * @return False si un des arguments est incorrect (client ou vehicule non trouv�, ou note invalide (non comprise entre 0 et 10)).
	 * @throws SQLException 
	 * @throws IllegalArgumentException Si l'identifiant du client ou du v�hicule est incorrect (n'existe pas dans la base), ou si les notes
	 * 									ne sont pas comprises entre 0 et 10 inclus.
	 */
	public boolean grade(long clientId, long vehicleId, int vehicleGrade, int conditionGrade) throws SQLException {
		if (!db.clientExists(clientId)) {
			throw new IllegalArgumentException("Erreur lors de la location d'un v�hicule : Le client " + clientId + " n'existe pas dans la base !");
		}
		
		if (!db.vehicleExists(vehicleId)) {
			throw new IllegalArgumentException("Erreur lors de la location d'un v�hicule : Le v�hicule \" + vehicleId + \" n'existe pas dans la base !");
		}
		
		if (vehicleGrade < 0 || vehicleGrade > 10) {
			throw new IllegalArgumentException("La note du v�hicule doit �tre comprise entre 0 et 10 inclus");
		}
		
		if (conditionGrade < 0 || conditionGrade > 10) {
			throw new IllegalArgumentException("La note de l'�tat du v�hicule doit �tre comprise entre 0 et 10 inclus");
		}
		
		db.addGrade(clientId, vehicleId, vehicleGrade, conditionGrade);
		return true;
	}
}
