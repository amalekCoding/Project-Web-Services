package fr.uge.banque;

import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.xml.rpc.ServiceException;

import fr.uge.database.DataBase;
import fr.uge.database.DataBaseServiceLocator;
import fr.uge.database.DataBaseSoapBindingStub;

public class BanqueService {
	private final DataBase db;
	
	public BanqueService() throws ServiceException {
		this.db = new DataBaseServiceLocator().getDataBase();
		((DataBaseSoapBindingStub) this.db).setMaintainSession(true);
	}
	
	/**
	 * Effectue un achat : Débite le client s'il a les fonds nécessaires, sinon ne fait rien.
	 * 
	 * @param clientId L'identifiant du client à débiter
	 * @param amount Le montant à débiter
	 * @return True si l'achat a réussis, False sinon (le client n'avait pas les fonds nécessaires)
	 * @throws RemoteException Si la connexion avec la base de données est interrompue
	 * @throws SQLException 
	 * @throws IllegalArgumentException Si le client n'existe pas dans la base ou si le montant est négatif
	 */
	public boolean makePurchase(long clientId, double amount) throws RemoteException, SQLException, IllegalArgumentException {
		if (amount < 0) {
			throw new IllegalArgumentException("Le montant de l'achat doit être positif");
		}
		
		if (db.getClientBankBalance(clientId) < amount) {
			return false;
		}
		
		db.debiteClient(clientId, amount);
		return true;
	}
	
	/**
	 * Crédite le client du montant donné en paramètre.
	 * 
	 * @param clientId L'identifiant du client
	 * @param amount Le montant à créditer
	 * @throws RemoteException Si la connexion avec la base de données est interrompue
	 * @throws SQLException 
	 * @throws IllegalArgumentException Si le client n'existe pas dans la base ou si le montant est négatif
	 */
	public void credit(long clientId, double amount) throws RemoteException, SQLException, IllegalArgumentException {
		if (amount < 0) {
			throw new IllegalArgumentException("Le montant à créditer doit être positif");
		}
		
		db.creditClient(clientId, amount);
	}
}
