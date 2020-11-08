package fr.uge.currency;

public class CurrencyConverter {
	public double convertEuroTo(String currency, double amount) {
		switch (currency) {
			case "EUR": return amount;
			case "USD": return amount * 1.19;
			case "GBP": return amount * 0.90;
			case "JPY": return amount * 122.82;
			default: throw new IllegalArgumentException("Conversion vers la devise " + currency + " non implémentée");
		}
	}
}
