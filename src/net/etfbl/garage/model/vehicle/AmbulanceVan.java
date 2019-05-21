package net.etfbl.garage.model.vehicle;

import net.etfbl.garage.model.vehicle.interfaces.Ambulance;

public class AmbulanceVan extends VanWithRotation implements Ambulance{
	private static final long serialVersionUID = 1L;
	
	public AmbulanceVan() {
		super();
	}
	
	public AmbulanceVan(VanWithRotation van) {
		super(van);
		this.rotation = van.rotation;
	}

}
