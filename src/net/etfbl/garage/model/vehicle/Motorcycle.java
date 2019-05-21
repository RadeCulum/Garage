package net.etfbl.garage.model.vehicle;

public class Motorcycle extends Vehicle{
	private static final long serialVersionUID = 1L;

	public Motorcycle() {
		super();
	}
	
	public Motorcycle(String name, int chasissNumber, int engineNumber,String picutre, String registrationNumber) {
		super(name, chasissNumber, engineNumber, picutre, registrationNumber);
	}
	
	public Motorcycle(Vehicle vehicle) {
		super(vehicle);
	}

}
