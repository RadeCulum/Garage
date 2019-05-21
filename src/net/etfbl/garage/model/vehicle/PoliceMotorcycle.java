package net.etfbl.garage.model.vehicle;

import net.etfbl.garage.model.vehicle.interfaces.Police;

public class PoliceMotorcycle extends MotorcycleWithRotation implements Police{
	private static final long serialVersionUID = 1L;
	
	public PoliceMotorcycle() {
		super();
	}
	
	public PoliceMotorcycle(MotorcycleWithRotation motorcycle) {
		super(motorcycle);
		this.rotation = motorcycle.rotation;
	}

}
