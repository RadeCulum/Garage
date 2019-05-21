package net.etfbl.garage.model.vehicle;

import net.etfbl.garage.model.vehicle.interfaces.Rotation;

public class VanWithRotation extends Van implements Rotation {
	private static final long serialVersionUID = 1L;
	protected transient boolean rotation;
	
	public VanWithRotation() {
		super();
	}
	
	public VanWithRotation(Van van) {
		super(van);
		if(van instanceof VanWithRotation) {
			this.rotation = ((VanWithRotation)van).rotation;
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
