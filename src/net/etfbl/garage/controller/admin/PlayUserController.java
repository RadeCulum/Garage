package net.etfbl.garage.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import net.etfbl.garage.controller.VehicleWriter;
import net.etfbl.garage.controller.user.UserController;
import net.etfbl.garage.model.vehicle.Vehicle;
/*
 * Klikom na dugme sa ikonicom Play prelazi se na korisnicki mod
 */
public class PlayUserController implements EventHandler<ActionEvent>{
	

	@Override
	public void handle(ActionEvent arg0) {
		AdministratorController admincontroller= AdministratorController.getInstance();
		List<List<Vehicle>> allVehicle = new ArrayList<>();
		
		//Konvertujem Mapu u Array listu jer VehicleWriter prima Array listu
		for(int i = 0; i < admincontroller.getPlatformsNumber(); ++i) {
			Map<String, Vehicle> vehicleMap = AdministratorController.getInstance().getElement(i).
					getVehicleTableView().getVehicleMap();
			allVehicle.add(new ArrayList<>(vehicleMap.values()));
			
		}
		
		new VehicleWriter().write(allVehicle);
		UserController.getInstance().showForm();
	}

}
