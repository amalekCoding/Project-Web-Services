package fr.uge.eiffelCorp;

public class Market {
	
	public Market() {
		
	}
	
	public int getPrice(long vehicleId) {
		return (int) vehicleId;
	}
	
	public boolean isAvailable(long vehicleId) {
		return true;
	}
}
