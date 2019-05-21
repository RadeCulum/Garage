package net.etfbl.garage.model.vehicle;

import net.etfbl.garage.model.vehicle.interfaces.FireFighter;

public class FirefighterVan extends VanWithRotation implements FireFighter{
	private static final long serialVersionUID = 1L;
	
	public FirefighterVan() {
		super();
	}
	
	public FirefighterVan(VanWithRotation van) {
		super(van);
		this.rotation = van.rotation;
	}

}
