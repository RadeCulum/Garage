package net.etfbl.garage.enumeration;

public enum VehicleType {
	AUTOMOBIL("Automobil"), KOMBI("Kombi"), MOTOCIKL("Motocikl");
	private final String type;
	
	private VehicleType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
}
