package net.etfbl.garage.model.vehicle;

public class Car extends Vehicle{
	private static final long serialVersionUID = 1L;
	protected Integer doorNumber;
	
	public Car() {
		super();
	}
	
	public Car(String name, Integer chasissNumber, Integer engineNumber, String picutre, String registrationNumber, Integer doorNmber) {
		super(name, chasissNumber, engineNumber, picutre, registrationNumber);
		this.doorNumber = doorNmber;
	}
	
	public Car(Vehicle vehicle) {
		super(vehicle);
		if(vehicle instanceof Car) {
			this.doorNumber = ((Car)vehicle).doorNumber;
		}
	}


	public Integer getDoorNumber() {
		return doorNumber;
	}

	public void setDoorNumber(Integer doorNumber) {
		this.doorNumber = doorNumber;
	}
	
	
	
}
