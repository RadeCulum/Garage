package net.etfbl.garage.model.vehicle;

public class Van extends Vehicle{
	private static final long serialVersionUID = 1L;
	protected int capacity;
	
	public Van() {
		super();
	}
	
	public Van(String name, int chasissNumber, int engineNumber,String picutre, String registrationNumber, int capacity) {
		super(name, chasissNumber, engineNumber, picutre, registrationNumber);
		this.capacity = capacity;
	}

	public Van(Vehicle vehicle) {
		super(vehicle);
		if(vehicle instanceof Van) {
			this.capacity  = ((Van)vehicle).capacity;
		}
	}
	
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
