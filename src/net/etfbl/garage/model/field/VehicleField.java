package net.etfbl.garage.model.field;

import net.etfbl.garage.model.vehicle.Vehicle;

public class VehicleField extends GarageField{
	private static final long serialVersionUID = 1L;
	
	Vehicle vehicle;
	
	public VehicleField() {
		super();
	}
	
	public VehicleField(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public Vehicle getVehicle() {
		return this.vehicle;
	}


}
