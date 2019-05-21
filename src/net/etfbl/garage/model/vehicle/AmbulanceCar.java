package net.etfbl.garage.model.vehicle;

import net.etfbl.garage.model.vehicle.interfaces.Ambulance;

public class AmbulanceCar extends CarWithRotation implements Ambulance{
	private static final long serialVersionUID = 1L;
	
	public AmbulanceCar() {
		super();
	}
	
	public AmbulanceCar(CarWithRotation car) {
		super(car);
		this.rotation =  car.rotation;
	}
	
}
