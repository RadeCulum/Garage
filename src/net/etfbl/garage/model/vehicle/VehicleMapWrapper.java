package net.etfbl.garage.model.vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * Wrapper-ska klasa koje je serializable
 * Okruzivace Map<registracijskiBroj, voziloSaTomRegistracijom>
 */
public class VehicleMapWrapper implements Serializable{
	private static final long serialVersionUID = 1L;
	public final Map<String, Vehicle> vehicleMap;
	
	public VehicleMapWrapper() {
		super();
		vehicleMap = new HashMap<>();
	}
	public VehicleMapWrapper(Map<String, Vehicle> vehicleList) {
		this.vehicleMap = vehicleList;
	}
	
	public ArrayList<Vehicle> toArrayList(){
		return new ArrayList<>(vehicleMap.values());
	}
}
