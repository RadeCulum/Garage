package net.etfbl.garage.view.admin;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import javafx.scene.input.ScrollEvent;
import net.etfbl.garage.model.vehicle.Vehicle;

public class VehicleTableView extends TableView<Vehicle>{
	private Map<String, Vehicle> vehicleMap; //Ovu mapu koristim za manipulaziju sa vozilima
//	private ObservableList<Vehicle> obsevablList; //Ova lista sluzi samo za upis u tabelu
	
	public VehicleTableView() {
		super();
		//Pomaze da ne dodje od havarije kad se pojavi scroll
		 this.addEventFilter(ScrollEvent.ANY, scrollEvent -> {
	            this.refresh();
	        });
		vehicleMap = new HashMap<>();
		super.setItems(FXCollections.observableArrayList(vehicleMap.values()));
	}
	
	
	public void addVehicle(Vehicle vehicle) {
		vehicleMap.put(vehicle.getRegistrationNumber(), vehicle);
		super.setItems(FXCollections.observableArrayList(vehicleMap.values()));
	}
	
	public void setVehicleMap(Map<String, Vehicle> vehicleMap) {
		this.vehicleMap = vehicleMap;
		super.setItems(FXCollections.observableArrayList(vehicleMap.values()));
	}
	
	public Map<String, Vehicle> getVehicleMap(){
		return vehicleMap;
	}
	
}
