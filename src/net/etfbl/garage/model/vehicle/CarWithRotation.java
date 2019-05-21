package net.etfbl.garage.model.vehicle;

import net.etfbl.garage.model.vehicle.interfaces.Rotation;

public class CarWithRotation extends Car implements Rotation{
	private static final long serialVersionUID = 1L;
	protected transient boolean rotation = false;
	
	public CarWithRotation() {
		super();
	}
	
	public CarWithRotation(Car car) {
		super(car);
		if(car instanceof CarWithRotation) {
			this.rotation = ((CarWithRotation)car).rotation;
		}
	}
	
	public void setRotation(boolean rotation) {
		this.rotation = rotation;
	}

	@Override
	public void TurnOnRotation() {
		rotation = true;
	}

	@Override
	public void TurnOffRotattion() {
		rotation = false;
	}

	@Override
	public boolean isRotationTurnOn() {
		return rotation;
	}
	

}
