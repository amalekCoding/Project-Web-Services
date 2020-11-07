package fr.uge.eiffelCorp;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.uge.database.DataBase;
import fr.uge.ifsCars.IGarage;

public class IfsCarsService {
	private final DataBase db;
	private IGarage garage;
	
	private final List<Long> basket;
	
	public IfsCarsService() {
		this.db = DataBase.getDatabase();
		
		try {
			this.garage = (IGarage) Naming.lookup("Garage");
		} catch (Exception e) {
			System.err.println("Erreur : Le serveur RMI définissant \"Garage\" n'est pas accessible");
		}
		
		this.basket = new ArrayList<Long>();
	}
	
	/**
	 * Renvoie le prix du véhicule donné en paramètre.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return Le prix du véhicule
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base
	 */
	public int getPrice(long vehicleId) throws IllegalArgumentException, SQLException {
		int price = db.getVehiclePrice(vehicleId);
		
		// TODO : Faire la conversion (probablement créer nous meme le web service de change)
		
		return price;
	}
	
	/**
	 * Détermine si le véhicule est disponible pour l'achat.
	 * Un véhicule est disponible pour l'achat s'il n'est pas en cours de location et s'il a préalablement été loué
	 * par un employé de EiffelCorp.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return True si le véhicule est disponible pour l'achat, False sinon.
	 * @throws RemoteException Si la connexion avec le serveur de IfsCars est indisponible
	 */
	public boolean isAvailable(long vehicleId) throws RemoteException {
		return !garage.isRented(vehicleId);
		// TODO : Vérifier également que le véhicule a déjà été loué.
	}
	
	/**
	 * Ajoute un véhicule au panier courant.
	 * 
	 * @param vehicleId L'identifiant du véhicule.
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base
	 */
	public void addToBasket(long vehicleId) throws SQLException, IllegalArgumentException {
		if (!db.vehicleExists(vehicleId)) {
			throw new IllegalArgumentException("Erreur lors de l'ajout d'un véhicule au panier : Le véhicule " + vehicleId + " n'existe pas dans la base !");
		}
		
		basket.add(vehicleId);
	}
	
	/**
	 * Achète les véhicules placés dans le panier.
	 * Vide le panier si l'achat a réussis (le client avait les fonds suffisants).
	 * 
	 * @return True si l'achat a réussis (c'est à dire si le client avait les fonds suffisants), False sinon.
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 */
	public boolean purchase() throws SQLException {
		int totalPrice = 0;
		
		for (Long vehicleId : basket) {
			try {
				totalPrice += getPrice(vehicleId);
			} catch (IllegalArgumentException e) {
				throw new IllegalStateException("Panier invalide : Le véhicule " + vehicleId + " n'existe pas dans la base");
			}
		}
		
		// TODO Vérifier auprès du web service Banque que le client a les fonds suffisants
		return false;
	}
}
