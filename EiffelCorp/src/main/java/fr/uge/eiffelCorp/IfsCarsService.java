package fr.uge.eiffelCorp;

public class IfsCarsService {
	
	public IfsCarsService() {
		
	}
	
	public int getPrice(long vehicleId) {
		return (int) vehicleId;
	}
	
	public boolean isAvailable(long vehicleId) {
		return true;
	}
}
