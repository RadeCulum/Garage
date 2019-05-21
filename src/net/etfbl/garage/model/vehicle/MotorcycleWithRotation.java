package net.etfbl.garage.model.vehicle;

import net.etfbl.garage.model.vehicle.interfaces.Rotation;

public class MotorcycleWithRotation extends Motorcycle implements Rotation{
	private static final long serialVersionUID = 1L;
	protected transient boolean rotation = false;

	public MotorcycleWithRotation() {
		super();
	}
	
	public MotorcycleWithRotation(Motorcycle motorcycle) {
		super(motorcycle);
		if(motorcycle instanceof MotorcycleWithRotation) {
			this.rotation = ((MotorcycleWithRotation)motorcycle).rotation;
		}
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
	
	public void setRotation(boolean rotation) {
		this.rotation = rotation;
	}
	
}
