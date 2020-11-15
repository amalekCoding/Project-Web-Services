package fr.uge.eiffelCorp;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.currencysystem.webservices.currencyserver.CurncsrvReturnRate;
import com.currencysystem.webservices.currencyserver.CurrencyServerLocator;
import com.currencysystem.webservices.currencyserver.CurrencyServerSoap;

import fr.uge.banque.BanqueService;
import fr.uge.banque.BanqueServiceServiceLocator;
import fr.uge.banque.BanqueServiceSoapBindingStub;
import fr.uge.database.DataBase;
import fr.uge.database.DataBaseServiceLocator;
import fr.uge.database.DataBaseSoapBindingStub;
import fr.uge.ifsCars.IGarage;

public class IfsCarsService {
	private final DataBase db;
	private final BanqueService bank;
	private final CurrencyServerSoap currencyConverter;
	private IGarage garage;
	
	private final List<Long> basket;
	
	public IfsCarsService() throws ServiceException {
		this.db = new DataBaseServiceLocator().getDataBase();
		((DataBaseSoapBindingStub) this.db).setMaintainSession(true);
		
		this.bank = new BanqueServiceServiceLocator().getBanqueService();
		((BanqueServiceSoapBindingStub) this.bank).setMaintainSession(true);
		
		this.currencyConverter = new CurrencyServerLocator().getCurrencyServerSoap();
		
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
	 * @param currency La devise du prix
	 * @return Le prix du véhicule
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base, ou si la devise donné n'est pas disponible
	 * @throws RemoteException Si la connexion avec la base de donnée, ou le service de conversion de devise a été interrompue
	 */
	public double getPrice(long vehicleId, String currency) throws IllegalArgumentException, SQLException, RemoteException {
		double price = db.getVehiclePrice(vehicleId);
		
		return (double) currencyConverter.convert("", "EUR", currency, price, false, "", CurncsrvReturnRate.curncsrvReturnRateNumber, "", "");
	}
	
	/**
	 * Détermine si le véhicule est disponible pour l'achat.
	 * Un véhicule est disponible pour l'achat s'il n'est pas en cours de location et s'il a préalablement été loué
	 * par un employé de EiffelCorp.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return True si le véhicule est disponible pour l'achat, False sinon.
	 * @throws RemoteException Si la connexion avec le serveur de IfsCars, ou avec la base de données est indisponible
	 */
	public boolean isAvailable(long vehicleId) throws RemoteException {
		return !garage.isRented(vehicleId) && db.getRentalsNumber(vehicleId) > 0;
	}
	
	/**
	 * Ajoute un véhicule au panier courant.
	 * 
	 * @param vehicleId L'identifiant du véhicule.
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base
	 * @throws RemoteException Si la connexion avec la base de donnée a été interrompue
	 */
	public void addToBasket(long vehicleId) throws SQLException, IllegalArgumentException, RemoteException {
		if (!db.vehicleExists(vehicleId)) {
			throw new IllegalArgumentException("Erreur lors de l'ajout d'un véhicule au panier : Le véhicule " + vehicleId + " n'existe pas dans la base !");
		}
		
		basket.add(vehicleId);
	}
	
	/**
	 * Achète les véhicules placés dans le panier.
	 * Vide le panier si l'achat a réussis (le client avait les fonds suffisants).
	 * 
	 * @param clientId L'identifiant du client
	 * @return True si l'achat a réussis (c'est à dire si le client avait les fonds suffisants), False sinon.
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 * @throws RemoteException Si la connexion avec la base de donnée ou la banque a été interrompue
	 */
	public boolean purchase(long clientId) throws SQLException, RemoteException {
		int totalPrice = 0;
		
		for (Long vehicleId : basket) {
			try {
				totalPrice += getPrice(vehicleId, "EUR");
			} catch (IllegalArgumentException e) {
				throw new IllegalStateException("Panier invalide : Le véhicule " + vehicleId + " n'existe pas dans la base");
			}
		}
		
		return bank.makePurchase(clientId, totalPrice);
	}
	
	/**
	 * Récupère la liste des identifiants de tous les véhicules (achetable ou louable seulement).
	 * 
	 * @return Un tableau contenant les identifiants des véhicules de la base
	 * @throws RemoteException
	 */
	public long[] getVehiclesList() throws RemoteException {
		return db.getVehiclesId();
	}
}
