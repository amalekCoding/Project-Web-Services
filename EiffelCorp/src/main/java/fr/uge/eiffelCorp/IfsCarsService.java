package fr.uge.eiffelCorp;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

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
import fr.uge.objects.Vehicle;
import fr.uge.utils.Serialization;

public class IfsCarsService {
	private final DataBase db;
	private final BanqueService bank;
	private final CurrencyServerSoap currencyConverter;
	private IGarage garage;
	
	/**
	 * Identifiants des véhicules dans le panier du client.
	 */
	private final ConcurrentLinkedQueue<Long> basket;
	private long clientId;
	
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
		
		this.basket = new ConcurrentLinkedQueue<Long>();
		this.clientId = -1;
	}
	
	/**
	 * Défini l'identifiant du client associé à cet objet.
	 * 
	 * @param clientId L'identifiant du client
	 * @throws RemoteException
	 */
	public void setId(long clientId) throws RemoteException {
		if (this.clientId != -1) {
			throw new IllegalStateException("L'identifiant ne peut pas être redéfini plusieurs fois");
		}
		
		if (!db.clientExists(clientId)) {
			throw new IllegalArgumentException("Le client " + clientId + " n'existe pas dans la base de données");
		}
		
		this.clientId = clientId;
	}
	
	/**
	 * Renvoie le prix d'achat du véhicule donné en paramètre.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @param currency La devise du prix
	 * @return Le prix d'achat du véhicule
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base, ou si la devise donné n'est pas disponible
	 * @throws RemoteException Si la connexion avec la base de donnée, ou le service de conversion de devise a été interrompue
	 */
	public double getBuyingPrice(long vehicleId, String currency) throws IllegalArgumentException, SQLException, RemoteException {
		double price = db.getVehicleBuyingPrice(vehicleId);
		
		return (double) currencyConverter.convert("", "EUR", currency, price, false, "", CurncsrvReturnRate.curncsrvReturnRateNumber, "", "");
	}
	
	/**
	 * Renvoie le prix de location du véhicule donné en paramètre.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @param currency La devise du prix
	 * @return Le prix de location du véhicule
	 * @throws SQLException Si la connexion avec la base de donnée a été interrompue
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base, ou si la devise donné n'est pas disponible
	 * @throws RemoteException Si la connexion avec la base de donnée, ou le service de conversion de devise a été interrompue
	 */
	public double getRentalPrice(long vehicleId, String currency) throws IllegalArgumentException, SQLException, RemoteException {
		double price = db.getVehicleRentalPrice(vehicleId);
		
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
	 * @return True si l'achat a réussis (c'est à dire si le client avait les fonds suffisants), False sinon.
	 * @throws SQLException Si la connexion avec la base de données a été interrompue
	 * @throws RemoteException Si la connexion avec la base de données ou la banque a été interrompue
	 */
	public boolean purchase() throws SQLException, RemoteException {
		int totalPrice = 0;
		
		if (clientId == -1) {
			throw new IllegalStateException("L'identifiant du client n'a pas été renseigné. Exectuez d'abord la méthode setId().");
		}
		
		for (Long vehicleId : basket) {
			try {
				totalPrice += getBuyingPrice(vehicleId, "EUR");
			} catch (IllegalArgumentException e) {
				throw new IllegalStateException("Panier invalide : Le véhicule " + vehicleId + " n'existe pas dans la base");
			}
		}
		
		if (bank.makePurchase(clientId, totalPrice)) {
			for (Long vehicleId : basket) {
				registerPurchase(clientId, vehicleId);
			}
			basket.clear();
			
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Récupère la liste des véhicule achetés par le client.
	 * 
	 * @return Un tableau contenant les véhicules achetés.
	 * @throws RemoteException
	 */
	public Vehicle[] getPurchasedVehicles() throws RemoteException {
		if (clientId == -1) {
			throw new IllegalStateException("L'identifiant du client n'a pas été renseigné. Exectuez d'abord la méthode setId().");
		}
		
		var SerializedVehicles = db.getPurchasedVehicles(clientId);
		if (Objects.isNull(SerializedVehicles)) {
			return new Vehicle[0];
		}
		
		var vehicles = new Vehicle[SerializedVehicles.length];
		
		for (int i = 0; i < SerializedVehicles.length; i++) {
			try {
				vehicles[i] = (Vehicle) Serialization.deserialize(SerializedVehicles[i]);
			} catch (ClassNotFoundException | IOException e) {
				vehicles[i] = null;
			}
		}
		
		return vehicles;
	}
	
	/**
	 * Renvoie la liste des véhicules placés dans le panier.
	 * 
	 * @return La liste des véhicules placés dans le panier
	 * @throws IOException 
	 */
	public Vehicle[] getBasket() throws IOException {
		Vehicle[] vehicles = new Vehicle[basket.size()];
		
		var iterator = basket.iterator();
		for (int i = 0; iterator.hasNext(); i++) {
			vehicles[i] = garage.getVehicle(iterator.next());
		}
		
		return vehicles;
	}
	
	/**
	 * Enregistre l'achat dans la base de données.
	 * 
	 * @param clientId Le client achetant le véhicule
	 * @param vehicleId L'identifiant du véhicule acheté
	 * @throws SQLException 
	 * @throws RemoteException Si la connexion avec la base de données a été interrompue
	 */
	private void registerPurchase(long clientId, long vehicleId) throws SQLException, RemoteException {
	    SimpleDateFormat format = new SimpleDateFormat(db.getDateFormat());
	    Date now = new Date();
	    String strDate = format.format(now);
		
		db.registerPurchase(strDate, clientId, vehicleId);
	}
	
	/**
	 * Vend un véhicule acheté par le client, et le crédite du même montant que le prix d'achat du véhicule.
	 * 
	 * @param vehicleId L'identifiant du véhicule à vendre
	 * @throws RemoteException Si la connexion avec la base de données ou la banque a été interrompue
	 * @throws SQLException
	 * @throws IllegalArgumentException Si le client n'a pas acheté le véhicule qu'il souhaite vendre
	 */
	public void sellVehicle(long vehicleId) throws RemoteException, SQLException, IllegalArgumentException {
		if (!db.hasPurchasedVehicle(clientId, vehicleId)) {
			throw new IllegalArgumentException("Le client " + clientId + " ne possède pas le véhicule " + vehicleId);
		}
		
		db.removePuchasedVehicle(clientId, vehicleId);
		bank.credit(clientId, db.getVehicleBuyingPrice(vehicleId));
	}
}
