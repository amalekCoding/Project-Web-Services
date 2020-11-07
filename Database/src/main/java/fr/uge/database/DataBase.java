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
	 * Determines if a client exists in the database.
	 * 
	 * @param clientId The client ID
	 * @return True if the client exists, False otherwise
	 * @throws SQLException 
	 */
	public boolean clientExists(long clientId) throws SQLException {
		var query = String.format("SELECT COUNT(*) FROM " + EMPLOYEES_TABLE + " WHERE id=%d;", clientId);
		
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
	 * @param clientId L'identifiant du client donnant la note
	 * @param vehicleId L'identifiant du véhicule à noter
	 * @param vehicleGrade La note du véhicule
	 * @param conditionGrade La note sur l'état du véhicule
	 * @throws SQLException
	 */
	public void addGrade(long clientId, long vehicleId, int vehicleGrade, int conditionGrade) throws SQLException {
		var query = String.format("INSERT INTO " + GRADES_TABLE + " VALUES (%d, %d, %d, %d)", clientId, vehicleId, vehicleGrade, conditionGrade);
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
}
