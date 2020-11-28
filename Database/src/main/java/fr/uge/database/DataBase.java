package fr.uge.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

import fr.uge.database.utils.Serialization;
import fr.uge.database.utils.Utils;
import fr.uge.objects.Vehicle;

import static fr.uge.database.utils.Utils.DATE_FORMAT;

public class DataBase {
	// Informations de connexion
	private final static String SERVER_URL = "localhost/EiffelCorp";
	private final static String USER = "postgres";
	private final static String PASSWORD = "postgres";
	
	// Nom des tables
	private final static String EMPLOYEES_TABLE = "public.\"Employees\"";
	private final static String VEHICLES_TABLE = "public.\"Vehicles\"";
	private final static String RENTALS_TABLE = "public.\"Rentals\"";
	private final static String PURCHASES_TABLE = "public.\"Purchases\"";
	private final static String CLIENTS_TABLE = "public.\"Clients\"";
	
	// Constantes
	private final static char SEP_CAR = ':';
	
	private Connection pgConnection;

	public DataBase() {
		this.pgConnection = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			pgConnection = DriverManager.getConnection("jdbc:postgresql://" + SERVER_URL, USER, PASSWORD);
		} catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println("Driver postgresql non trouvé\n" + e.getMessage());
		}
		
        if (pgConnection != null) {
            System.out.println("Connected to the database !");
        } else {
            System.err.println("Failed to make connection with database !");
        }
	}
	
	public void silentlyClose() {
		try {
			pgConnection.close();
			pgConnection = null;
		} catch (SQLException e) {
			System.err.println("Erreur lors de la fermeture de la base de données");
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	}
	
	private ResultSet executeQuery(String query) throws SQLException {
		Statement preparedStatement = pgConnection.createStatement();
		return preparedStatement.executeQuery(query);
	}
	
	private void executeUpdate(String query) throws SQLException {
		Statement preparedStatement = pgConnection.createStatement();
		preparedStatement.executeUpdate(query);
	}
	
	/**
	 * Determines if a employee exists in the database.
	 * 
	 * @param employeeId The employee ID
	 * @return True if the employee exists, False otherwise
	 * @throws SQLException 
	 */
	public boolean employeeExists(long employeeId) throws SQLException {
		var query = String.format("SELECT COUNT(*) FROM " + EMPLOYEES_TABLE + " WHERE id=%d;", employeeId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result) && result.next()) {
			return result.getInt("count") > 0;
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Determines if a client exists in the database.
	 * 
	 * @param clientId The client ID
	 * @return True if the client exists, False otherwise
	 * @throws SQLException 
	 */
	public boolean clientExists(long clientId) throws SQLException {
		var query = String.format("SELECT COUNT(*) FROM " + CLIENTS_TABLE + " WHERE id=%d;", clientId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result) && result.next()) {
			return result.getInt("count") > 0;
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Determines if a vehicle exists in the database.
	 * 
	 * @param vehicleId The vehicle ID
	 * @return True if the vehicle exists, False otherwise
	 * @throws SQLException 
	 */
	public boolean vehicleExists(long vehicleId) throws SQLException {
		var query = String.format("SELECT COUNT(*) FROM " + VEHICLES_TABLE + " WHERE id=%d;", vehicleId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result) && result.next()) {
			return result.getInt("count") > 0;
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Ajoute une note au véhicule par le client.
	 * 
	 * @param date La date de début de location du véhicule, au format DATE_FORMAT
	 * @param employeeId L'identifiant de l'employé donnant la note
	 * @param vehicleId L'identifiant du véhicule à noter
	 * @param vehicleGrade La note du véhicule
	 * @param conditionGrade La note sur l'état du véhicule
	 * @throws SQLException
	 * @throws IllegalArgumentException Si la date donnée n'est pas au bon format
	 */
	public void addGrade(String date, long employeeId, long vehicleId, int vehicleGrade, int conditionGrade) throws SQLException, IllegalArgumentException {
		if (!Utils.isValidDate(date)) {
			throw new IllegalArgumentException("La date doit être au format " + DATE_FORMAT);
		}
		
		var query = String.format("UPDATE " + RENTALS_TABLE
								+ " SET vehicle_grade = %d, condition_grade = %d"
								+ "WHERE date = %s AND vehicle_id = %d AND employee_id = %d;", vehicleGrade, conditionGrade, date, vehicleId, employeeId);
		executeUpdate(query);
	}
	
	/**
	 * Enregistre dans la base de données le début d'une nouvelle location.
	 * 
	 * @param date La date de début de location du véhicule, au format DATE_FORMAT
	 * @param employeeId L'identifiant de l'employé louant le véhicule
	 * @param vehicleId L'identifiant du véhicule à louer
	 * @throws SQLException
	 * @throws IllegalArgumentException Si la date donnée n'est pas au bon format
	 */
	public void registerRental(String date, long employeeId, long vehicleId) throws SQLException, IllegalArgumentException {
		if (!Utils.isValidDate(date)) {
			throw new IllegalArgumentException("La date doit être au format " + DATE_FORMAT);
		}
		
		var query = String.format("INSERT INTO " + RENTALS_TABLE + " (date, vehicle_id, employee_id) VALUES ('%s', %d, %d);", date, vehicleId, employeeId);
		executeUpdate(query);
	}
	
	/**
	 * Enregistre dans la base de données l'achat d'un véhicule.
	 * 
	 * @param date La date de l'achat du véhicule, au format DATE_FORMAT
	 * @param clientId L'identifiant du client ayant acheté le véhicule
	 * @param vehicleId L'identifiant du véhicule acheté
	 * @throws SQLException
	 * @throws IllegalArgumentException Si la date donnée n'est pas au bon format
	 */
	public void registerPurchase(String date, long clientId, long vehicleId) throws SQLException, IllegalArgumentException {
		if (!Utils.isValidDate(date)) {
			throw new IllegalArgumentException("La date doit être au format " + DATE_FORMAT);
		}
		
		var query = String.format("INSERT INTO " + PURCHASES_TABLE + " (date, vehicle_id, client_id) VALUES ('%s', %d, %d);", date, vehicleId, clientId);
		executeUpdate(query);
	}
	
	/**
	 * Renvoie le prix d'achat en euros du véhicule donné en paramètre.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return Le prix d'achat du véhicule
	 * @throws SQLException
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base
	 */
	public double getVehicleBuyingPrice(long vehicleId) throws SQLException, IllegalArgumentException {
		var query = String.format("SELECT buying_price FROM " + VEHICLES_TABLE + " WHERE id=%d;", vehicleId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			if (!result.next()) {
				throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'existe pas dans la base !");
			}
			
			return result.getDouble("buying_price");
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Renvoie le prix de location en euros du véhicule donné en paramètre.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return Le prix de location du véhicule
	 * @throws SQLException
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base
	 */
	public double getVehicleRentalPrice(long vehicleId) throws SQLException, IllegalArgumentException {
		var query = String.format("SELECT rental_price FROM " + VEHICLES_TABLE + " WHERE id=%d;", vehicleId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			if (!result.next()) {
				throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'existe pas dans la base !");
			}
			
			return result.getDouble("rental_price");
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Renvoie la marque du véhicule donné en paramètre.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return La marque du véhicule
	 * @throws SQLException
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base
	 */
	public String getVehicleBrand(long vehicleId) throws SQLException, IllegalArgumentException {
		var query = String.format("SELECT brand FROM " + VEHICLES_TABLE + " WHERE id=%d;", vehicleId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			if (!result.next()) {
				throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'existe pas dans la base !");
			}
			
			return result.getString("brand");
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Renvoie le modèle du véhicule donné en paramètre.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return Le modèle du véhicule
	 * @throws SQLException
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base
	 */
	public String getVehicleModel(long vehicleId) throws SQLException, IllegalArgumentException {
		var query = String.format("SELECT model FROM " + VEHICLES_TABLE + " WHERE id=%d;", vehicleId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			if (!result.next()) {
				throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'existe pas dans la base !");
			}
			
			return result.getString("model");
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Renvoie la note moyenne générale du véhicule donné en paramètre.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return La note moyenne générale du véhicule
	 * @throws SQLException
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base
	 */
	private float getVehicleGeneralGrade(long vehicleId) throws SQLException, IllegalArgumentException {
		var query = String.format("SELECT AVG(vehicle_grade) AS note FROM " + RENTALS_TABLE + " WHERE vehicle_id=%d;", vehicleId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			if (!result.next()) {
				throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'a jamais été noté");
			}
			
			return result.getFloat("note");
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Renvoie la note moyenne de l'état du véhicule donné en paramètre.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return La note moyenne de l'état du véhicule
	 * @throws SQLException
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base
	 */
	private float getVehicleConditionGrade(long vehicleId) throws SQLException, IllegalArgumentException {
		var query = String.format("SELECT AVG(condition_grade) AS note FROM " + RENTALS_TABLE + " WHERE vehicle_id=%d;", vehicleId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			if (!result.next()) {
				throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'a jamais été noté");
			}
			
			return result.getFloat("note");
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Récupère le solde bancaire du client donné en paramètre.
	 * 
	 * @param clientId L'identifiant du client
	 * @return Le solde bancaire du client
	 * @throws SQLException
	 * @throws IllegalArgumentException Si l'identifiant du client n'est pas renseigné dans la base
	 */
	public double getClientBankBalance(long clientId) throws SQLException, IllegalArgumentException {
		var query = String.format("SELECT bank_balance FROM " + CLIENTS_TABLE + " WHERE id=%d;", clientId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			if (!result.next()) {
				throw new IllegalArgumentException("Le client " + clientId + " n'existe pas dans la base !");
			}
			
			return result.getDouble("bank_balance");
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Débite le client du montant donné en paramètre.
	 * 
	 * @param clientId L'identifiant du client
	 * @param amount Le montant à débiter
	 * @throws SQLException 
	 * @throws IllegalArgumentException Si l'identifiant du client n'est pas renseigné dans la base, ou si le montant à débiter est négatif
	 */
	public void debiteClient(long clientId, double amount) throws IllegalArgumentException, SQLException {
		if (amount < 0) {
			throw new IllegalArgumentException("La somme à débiter doit être positive");
		}
		
		double currentAmount = getClientBankBalance(clientId);
		var newAmount = String.valueOf(currentAmount - amount).replace(',', '.'); // Remplacement des virgules par des points pour l'injecter avec jdbc
		String query = String.format("UPDATE " + CLIENTS_TABLE + " SET bank_balance = %s WHERE id = %d;", newAmount, clientId);
		executeUpdate(query);
	}
	
	/**
	 * Crédite le client du montant donné en paramètre.
	 * 
	 * @param clientId L'identifiant du client
	 * @param amount Le montant à créditer
	 * @throws SQLException 
	 * @throws IllegalArgumentException Si l'identifiant du client n'est pas renseigné dans la base, ou si le montant à créditer est négatif
	 */
	public void creditClient(long clientId, double amount) throws IllegalArgumentException, SQLException {
		if (amount < 0) {
			throw new IllegalArgumentException("La somme à créditer doit être positive");
		}
		
		double currentAmount = getClientBankBalance(clientId);
		var newAmount = String.valueOf(currentAmount + amount).replace(',', '.'); // Remplacement des virgules par des points pour l'injecter avec jdbc
		String query = String.format("UPDATE " + CLIENTS_TABLE + " SET bank_balance = %s WHERE id = %d;", newAmount, clientId);
		executeUpdate(query);
	}
	
	/**
	 * Récupère le nombre de fois auquel le véhicule a été loué par un employé.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return Le nombre de fois auquel le véhicule a été loué
	 * @throws SQLException
	 */
	public int getRentalsNumber(long vehicleId) throws SQLException {
		var query = String.format("SELECT COUNT(*) FROM " + RENTALS_TABLE + " WHERE vehicle_id=%d;", vehicleId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result) && result.next()) {
			return result.getInt("count");
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Détermine si l'employé a loué le véhicule donné en paramètre
	 * 
	 * @param employeeId L'identifiant de l'employé
	 * @param vehicleId L'identifiant du véhicule
	 * @return True si l'employé a loué le véhicule, False sinon.
	 * @throws SQLException
	 */
	public boolean hasRentedVehicle(long employeeId, long vehicleId) throws SQLException {
		var query = String.format("SELECT COUNT(*) FROM " + RENTALS_TABLE + " WHERE employee_id=%d AND vehicle_id=%d;", employeeId, vehicleId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result) && result.next()) {
			return result.getInt("count") > 0;
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Détermine si le client a acheté le véhicule donné en paramètre
	 * 
	 * @param clientId L'identifiant du client
	 * @param vehicleId L'identifiant du véhicule
	 * @return True si le client a acheté le véhicule, False sinon.
	 * @throws SQLException
	 */
	public boolean hasPurchasedVehicle(long clientId, long vehicleId) throws SQLException {
		var query = String.format("SELECT COUNT(*) FROM " + PURCHASES_TABLE + " WHERE client_id=%d AND vehicle_id=%d;", clientId, vehicleId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result) && result.next()) {
			return result.getInt("count") > 0;
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Retire de la table l'achat d'un véhicule par un client.<br/>
	 * Si le client a acheté plusieurs fois le même véhicule, seul 1 est retiré.
	 * 
	 * @param clientId L'identifiant du client
	 * @param vehicleId L'identifiant du véhicule
	 * @throws SQLException
	 */
	public void removePuchasedVehicle(long clientId, long vehicleId) throws SQLException {
		var query = String.format("DELETE TOP 1 FROM " + PURCHASES_TABLE + " WHERE client_id=%d AND vehicle_id=%d;", clientId, vehicleId);
		executeUpdate(query);
	}
	
	/**
	 * Récupère toutes les caractéristiques d'un véhicule depuis la base de données et renvoie l'ensemble sous forme d'un objet Vehicle sérialisé.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return L'objet Véhicule sérialisé associé à l'identifiant
	 * @throws SQLException
	 */
	public String getVehicle(long vehicleId) throws SQLException {
		var query = String.format("SELECT id, buying_price, rental_price, brand, model FROM " + VEHICLES_TABLE + " WHERE id=%d;", vehicleId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result) && result.next()) {
			var id = result.getLong("id");
			var buyingPrice = result.getDouble("buying_price");
			var rentalPrice = result.getDouble("rental_price");
			var brand = result.getString("brand");
			var model = result.getString("model");
			float avgGeneralGrade, avgConditionGrade;
			try {
				avgGeneralGrade = getVehicleGeneralGrade(id);
				avgConditionGrade = getVehicleConditionGrade(id);	
			} catch (IllegalArgumentException e) { // Si le véhicule n'a jamais été noté
				avgGeneralGrade = avgConditionGrade = -1;
			}
			
			try {
				return Serialization.serialize(new Vehicle(id, brand, model, buyingPrice, rentalPrice, avgGeneralGrade, avgConditionGrade, null));
			} catch (IOException e) {
				return null;
			}
		}
		
		return null;
	}
	
	/**
	 * Récupère la liste de tous les véhicules de la base sous forme sérialisé.
	 * 
	 * @return Un tableau contenant les véhicules sérialisés de la base
	 * @throws SQLException
	 */
	public String[] getAllVehicles() throws SQLException {
		var query = String.format("SELECT id, buying_price, rental_price, brand, model FROM " + VEHICLES_TABLE + ";");
		var lst = new ArrayList<Vehicle>();
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			while (result.next()) {
				var id = result.getLong("id");
				var buyingPrice = result.getDouble("buying_price");
				var rentalPrice = result.getDouble("rental_price");
				var brand = result.getString("brand");
				var model = result.getString("model");
				float avgGeneralGrade, avgConditionGrade;
				try {
					avgGeneralGrade = getVehicleGeneralGrade(id);
					avgConditionGrade = getVehicleConditionGrade(id);	
				} catch (IllegalArgumentException e) { // Si le véhicule n'a jamais été noté
					avgGeneralGrade = avgConditionGrade = -1;
				}
				
				lst.add(new Vehicle(id, brand, model, buyingPrice, rentalPrice, avgGeneralGrade, avgConditionGrade, null));
			}
		}
		
		var array = new String[lst.size()];
		for (int i = 0; i < array.length; i++) {
			try {
				array[i] = Serialization.serialize(lst.get(i));
			} catch (IOException e) {
				array[i] = null;
			}
		}
		
		return array;
	}
	
	/**
	 * Renvoie le format de date utilisé pour les timestamp postgreSQL.
	 * 
	 * @return Le format d'une date
	 */
	public String getDateFormat() {
		return DATE_FORMAT;
	}
	
	/**
	 * Récupère la liste des véhicules loués sérialisés par l'employé dont l'identifiant est donné.
	 * 
	 * @param employeeId L'identifiant de l'employé
	 * @return Un tableau contenant les véhicules loués sérialisés par l'employé
	 * @throws SQLException
	 */
	public String[] getRentedVehicles(long employeeId) throws SQLException {
		var query = String.format("SELECT id, buying_price, rental_price, brand, model, date"
				+ "FROM " + VEHICLES_TABLE + ", " + RENTALS_TABLE + " "
				+ "WHERE " + VEHICLES_TABLE + ".id = " + RENTALS_TABLE + ".vehicle_id AND employee_id=%d;", employeeId);
		var lst = new ArrayList<Vehicle>();
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			while (result.next()) {
				var id = result.getLong("id");
				var buyingPrice = result.getDouble("buying_price");
				var rentalPrice = result.getDouble("rental_price");
				var brand = result.getString("brand");
				var model = result.getString("model");
				var date = result.getTimestamp("date");
				float avgGeneralGrade, avgConditionGrade;
				try {
					avgGeneralGrade = getVehicleGeneralGrade(id);
					avgConditionGrade = getVehicleConditionGrade(id);
				} catch (IllegalArgumentException e) { // Si le véhicule n'a jamais été noté
					avgGeneralGrade = avgConditionGrade = -1;
				}
				
				lst.add(new Vehicle(id, brand, model, buyingPrice, rentalPrice, avgGeneralGrade, avgConditionGrade, date));
			}
		}
		
		var array = new String[lst.size()];
		for (int i = 0; i < array.length; i++) {
			try {
				array[i] = Serialization.serialize(lst.get(i));
			} catch (IOException e) {
				array[i] = null;
			}
		}
		
		return array;
	}
	
	/**
	 * Récupère la liste des véhicules achetés sérialisés par le client dont l'identifiant est donné.
	 * 
	 * @return Un tableau contenant les véhicules achetés sérialisés par le client
	 * @throws SQLException
	 */
	public String[] getPurchasedVehicles(long clientId) throws SQLException {
		var query = String.format("SELECT id, buying_price, rental_price, brand, model, date" + " "
				+ "FROM " + VEHICLES_TABLE + ", " + PURCHASES_TABLE + " "
				+ "WHERE " + VEHICLES_TABLE + ".id = " + PURCHASES_TABLE + ".vehicle_id AND client_id=%d;", clientId);
		var lst = new ArrayList<Vehicle>();
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			while (result.next()) {
				var id = result.getLong("id");
				var buyingPrice = result.getDouble("buying_price");
				var rentalPrice = result.getDouble("rental_price");
				var brand = result.getString("brand");
				var model = result.getString("model");
				var date = result.getTimestamp("date");
				float avgGeneralGrade, avgConditionGrade;
				try {
					avgGeneralGrade = getVehicleGeneralGrade(id);
					avgConditionGrade = getVehicleConditionGrade(id);
				} catch (IllegalArgumentException e) { // Si le véhicule n'a jamais été noté
					avgGeneralGrade = avgConditionGrade = -1;
				}
				
				lst.add(new Vehicle(id, brand, model, buyingPrice, rentalPrice, avgGeneralGrade, avgConditionGrade, date));
			}
		}
		
		var array = new String[lst.size()];
		for (int i = 0; i < array.length; i++) {
			try {
				array[i] = Serialization.serialize(lst.get(i));
			} catch (IOException e) {
				array[i] = null;
			}
		}
		
		return array;
	}
	
	/**
	 * Vérifie l'authentification d'un employé, selon ses identifiants, et renvoie ses informations
	 * si l'authentification a réussis sous la forme "id:firstname:lastname".
	 * 
	 * @param login Le login de l'employé
	 * @param password Le mot de passe de l'employé
	 * @return La String contenant les informations de l'employé ou null si l'authentification a échouée
	 * @throws SQLException
	 */
	public String authenticateEmployee(String login, String password) throws SQLException {
		var query = String.format("SELECT id, firstname, lastname FROM " + EMPLOYEES_TABLE + " WHERE login='%s' AND password='%s';", login, password);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			if (result.next()) {
				var id = result.getString("id");
				var firstname = result.getString("firstname");
				var lastname = result.getString("lastname");
				
				return id + SEP_CAR + firstname + SEP_CAR + lastname;
			} else {
				return null;
			}
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Vérifie l'authentification d'un client, selon ses identifiants, et renvoie ses informations
	 * si l'authentification a réussis sous la forme "id:firstname:lastname".
	 * 
	 * @param login Le login du client
	 * @param password Le mot de passe de l'employé
	 * @return La String contenant les informations du client ou null si l'authentification a échouée
	 * @throws SQLException
	 */
	public String authenticateClient(String login, String password) throws SQLException {
		var query = String.format("SELECT id, firstname, lastname FROM " + CLIENTS_TABLE + " WHERE login='%s' AND password='%s';", login, password);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			if (result.next()) {
				var id = result.getString("id");
				var firstname = result.getString("firstname");
				var lastname = result.getString("lastname");
				
				return id + SEP_CAR + firstname + SEP_CAR + lastname;
			} else {
				return null;
			}
		}
		
		throw new IllegalStateException();
	}
}
