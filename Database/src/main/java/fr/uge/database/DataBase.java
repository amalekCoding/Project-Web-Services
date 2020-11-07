package fr.uge.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class DataBase {
	// Informations de connexion
	private final static String SERVER_URL = "localhost/EiffelCorp";
	private final static String USER = "postgres";
	private final static String PASSWORD = "postgres";
	
	// Nom des tables
	private final static String EMPLOYEES_TABLE = "public.\"Employees\"";
	private final static String VEHICLES_TABLE = "public.\"Vehicles\"";
	private final static String GRADES_TABLE = "public.\"Grades\"";
	private final static String CLIENTS_TABLE = "public.\"Clients\"";
	
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
	
	private void silentlyClose() {
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
	 * @param employeeId The client ID
	 * @return True if the client exists, False otherwise
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
	 * @param employeeId L'identifiant de l'employé donnant la note
	 * @param vehicleId L'identifiant du véhicule à noter
	 * @param vehicleGrade La note du véhicule
	 * @param conditionGrade La note sur l'état du véhicule
	 * @throws SQLException
	 */
	public void addGrade(long employeeId, long vehicleId, int vehicleGrade, int conditionGrade) throws SQLException {
		var query = String.format("INSERT INTO " + GRADES_TABLE + " VALUES (%d, %d, %d, %d);", employeeId, vehicleId, vehicleGrade, conditionGrade);
		executeUpdate(query);
	}
	
	/**
	 * Renvoie le prix en euros du véhicule donné en paramètre.
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return Le prix du véhicule
	 * @throws SQLException
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base
	 */
	public int getVehiclePrice(long vehicleId) throws SQLException, IllegalArgumentException {
		var query = String.format("SELECT price FROM " + VEHICLES_TABLE + " WHERE id=%d;", vehicleId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			if (!result.next()) {
				throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'existe pas dans la base !");
			}
			
			return result.getInt("price");
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
	public int getClientBankBalance(long clientId) throws SQLException, IllegalArgumentException {
		var query = String.format("SELECT bank_balance FROM " + CLIENTS_TABLE + " WHERE id=%d;", clientId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			if (!result.next()) {
				throw new IllegalArgumentException("Le client " + clientId + " n'existe pas dans la base !");
			}
			
			return result.getInt("bank_balance");
		}
		
		throw new IllegalStateException();
	}
	
	/**
	 * Débite le client du montant donné en paramètre
	 * 
	 * @param clientId L'identifiant du client
	 * @param amount Le montant à débiter
	 * @throws SQLException 
	 * @throws IllegalArgumentException Si l'identifiant du client n'est pas renseigné dans la base, ou si le montant à débiter est négatif
	 */
	public void debiteClient(long clientId, int amount) throws IllegalArgumentException, SQLException {
		if (amount < 0) {
			throw new IllegalArgumentException("La somme à débiter doit être positive");
		}
		
		int currentAmount = getClientBankBalance(clientId);
		String query = String.format("UPDATE " + CLIENTS_TABLE + " SET bank_balance = %d WHERE id = %d;", currentAmount - amount, clientId);
		executeUpdate(query);
	}
	
	/**
	 * Récupère le nombre de fois auquel le véhicule a été loué par un employé
	 * 
	 * @param vehicleId L'identifiant du véhicule
	 * @return Le nombre de fois auquel le véhicule a été loué
	 * @throws SQLException 
	 * @throws IllegalArgumentException Si l'identifiant du véhicule n'est pas renseigné dans la base
	 */
	public int getRentalsNumber(long vehicleId) throws SQLException, IllegalArgumentException {
		var query = String.format("SELECT nb_rented FROM " + VEHICLES_TABLE + " WHERE id=%d;", vehicleId);
		
		var result = executeQuery(query);
		if (!Objects.isNull(result)) {
			if (!result.next()) {
				throw new IllegalArgumentException("Le véhicule " + vehicleId + " n'existe pas dans la base !");
			}
			
			return result.getInt("nb_rented");
		}
		
		throw new IllegalStateException();
	}
}
