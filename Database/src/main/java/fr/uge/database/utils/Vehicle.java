package fr.uge.database.utils;

import java.io.Serializable;
import java.util.Date;

public class Vehicle implements Serializable {
	private static final long serialVersionUID = 3243347348028062389L;
	public final long id;
	public final String brand, model;
	public final double buyingPrice, rentalPrice;
	public final float generalGrade, conditionGrade;
	/**
	 * Selon le contexte, correspond à la date d'achat ou de location du véhicule, ou null si hors contexte.
	 */
	public final Date date;
	
	public Vehicle(long id, String brand, String model, double buyingPrice, double rentalPrice, float generalGrade, float conditionGrade, Date date) {
		this.id = id;
		this.brand = brand;
		this.model = model;
		this.buyingPrice = buyingPrice;
		this.rentalPrice = rentalPrice;
		this.generalGrade = generalGrade;
		this.conditionGrade = conditionGrade;
		this.date = date;
	}
	
	public Vehicle() {
		this.id = -1;
		this.brand = null;
		this.model = null;
		this.buyingPrice = -1;
		this.rentalPrice = -1;
		this.generalGrade = -1;
		this.conditionGrade = -1;
		this.date = null;
	}
}
