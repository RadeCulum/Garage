package net.etfbl.garage.controller.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.view.admin.AdministratorPane;
import net.etfbl.garage.view.admin.VehicleTableView;

/*
 * Brise iz tabele selektovano vozilo
 */

public class RemoveVehicleController implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent e) {
		AdministratorPane activePane = AdministratorController.getInstance().getActivePane();
		VehicleTableView tableView =activePane.getVehicleTableView();
		Vehicle selectedItem = tableView.getSelectionModel().getSelectedItem();
		tableView.getVehicleMap().remove(selectedItem.getRegistrationNumber());
		tableView.setVehicleMap(tableView.getVehicleMap());
		if(tableView.getItems().size() == 27) {
			activePane.getVehicleCBox().setDisable(false);
			if(!activePane.getVehicleCBox().getSelectionModel().isEmpty()) {
				activePane.getAddVehicleButton().setDisable(false);
			}
		}
		activePane.getRemoveVehicleButton().setDisable(true);
		activePane.getChangeVehicleButton().setDisable(true);
	}

}
