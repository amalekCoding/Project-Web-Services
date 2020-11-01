package fr.uge.ifsCars;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {

	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(1099);
			IGarage garage = new Garage();
			Naming.rebind("Garage", garage);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

}
