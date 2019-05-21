package net.etfbl.garage.model.vehicle;

import net.etfbl.garage.model.vehicle.interfaces.Police;

public class PoliceCar extends CarWithRotation implements Police{
	private static final long serialVersionUID = 1L;

	public PoliceCar() {
		super();
	}
	
	public PoliceCar(CarWithRotation car) {
		super(car);
		this.rotation = car.rotation;
	}
}
