package net.etfbl.garage.controller.admin;

import java.util.ArrayList;

import javafx.scene.Scene;
import net.etfbl.garage.controller.VehicleReader;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.model.vehicle.VehicleMapWrapper;
import net.etfbl.garage.utils.GarageProperties;
import net.etfbl.garage.view.MainStage;
import net.etfbl.garage.view.admin.AdministratorPane;

public class AdministratorController {
	private static AdministratorController administratorControler = null;
	
	private ArrayList<AdministratorPane> administratorPaneList = null;
	
	
	private AdministratorController() {
		setAdministratorPanes();
	}
	
	public void showForm() {
		MainStage.getInstance().setScene(administratorPaneList.get(0).getScene());
	}
	
	private void setAdministratorPanes() {
		administratorPaneList = new ArrayList<>();
		
		ArrayList<ArrayList<Vehicle>> allVehicle = new ArrayList<>();
		allVehicle = new VehicleReader().getAllVehicle();
		
		for(int i = 0; i < getPlatformsNumber(); ++i) {
			AdministratorPane adminPane = new AdministratorPane();
			if(!allVehicle.isEmpty()) {
				VehicleMapWrapper vehicleMap = new VehicleMapWrapper();
				for(Vehicle vehicle : allVehicle.get(i)) {
					vehicleMap.vehicleMap.put(vehicle.getRegistrationNumber(), vehicle);
				}
				
				adminPane.getVehicleTableView().setVehicleMap(vehicleMap.vehicleMap);
				if(vehicleMap.vehicleMap.size() >= 28) {
					adminPane.getVehicleCBox().setDisable(true);
					adminPane.getAddVehicleButton().setDisable(true);
				}
			}
			adminPane.getPlatformNumberTField().setText(String.valueOf(getPlatformsNumber()));
			administratorPaneList.add(adminPane);
		}	
	}

	public static AdministratorController getInstance() {
		if(administratorControler == null) {
			administratorControler = new AdministratorController();
		}
		return administratorControler;
	}
	
	public int getPlatformsNumber() {
		return GarageProperties.getInstance().getPlatformsNumber();
	}
	
	//Vraca index Scene odgovarajuceg AdministratoPane-a
		public int getIndexOfScene(Scene adminScene) {
			for(AdministratorPane x : administratorPaneList) {
				if(x.getScene() == adminScene) {
					return administratorPaneList.indexOf(x);
				}
			}
			return -1;
		}
	//Vraca indeks Scene aktivne platforme, tj. one koju trenutno koristi MainStage
	public int getIndexOfActivePane() {
		return AdministratorController.getInstance().getIndexOfScene(MainStage.getInstance().getScene());
	}
	//Vraca aktivni AdministratorPane
	public AdministratorPane getActivePane() {
		return getElement(AdministratorController.getInstance().getIndexOfActivePane());
	}
	public AdministratorPane getElement(int index) {
		if(index < getPlatformsNumber()) {
			return administratorPaneList.get(index);
		}
		return null;
	}
	public void setActivePane(int pos) {
		MainStage.getInstance().setScene(AdministratorController.getInstance().getElement(pos).getScene());
	}
	public void updatePlatformNuberCB() {
		administratorControler.getActivePane().getPlatformNumberCBox().setValue(administratorControler.getIndexOfActivePane() + 1);
	}
}
