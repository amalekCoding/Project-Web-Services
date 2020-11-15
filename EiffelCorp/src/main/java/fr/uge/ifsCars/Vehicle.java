package fr.uge.ifsCars;

import java.io.Serializable;

public class Vehicle implements Serializable {
	private static final long serialVersionUID = 3243347348028062389L;
	public final long id;
	public final String brand, model;
	public final double buyingPrice, rentalPrice;
	public final float generalGrade, conditionGrade;
	
	public Vehicle(long id, String brand, String model, double buyingPrice, double rentalPrice, float generalGrade, float conditionGrade) {
		this.id = id;
		this.brand = brand;
		this.model = model;
		this.buyingPrice = buyingPrice;
		this.rentalPrice = rentalPrice;
		this.generalGrade = generalGrade;
		this.conditionGrade = conditionGrade;
	}
}
