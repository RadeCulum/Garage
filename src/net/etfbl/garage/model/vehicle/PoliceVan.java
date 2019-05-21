package net.etfbl.garage.model.vehicle;

import net.etfbl.garage.model.vehicle.interfaces.Police;

public class PoliceVan extends VanWithRotation implements Police{
	private static final long serialVersionUID = 1L;
	
	public PoliceVan() {
		super();
	}
	
	public PoliceVan(VanWithRotation van) {
		super(van);
		this.rotation = van.rotation;
	}
}
