package net.etfbl.garage.controller.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import net.etfbl.garage.model.vehicle.Car;
import net.etfbl.garage.model.vehicle.Van;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.view.admin.AddVehicleForm;
import net.etfbl.garage.view.admin.AdministratorPane;
import net.etfbl.garage.view.admin.VehicleTableView;

/*
 * Kada se klikne na dugme "Izmjeni vozilo" otvara forma popunjena trenutnim podacima
 * selektvanog vozila i omogucena je izmjena trenutnih podataka
 */

public class ChangeVehicleController implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent arg0) {
		AdministratorPane adminPane = AdministratorController.getInstance().getActivePane();
		VehicleTableView tableView = adminPane.getVehicleTableView();
		Vehicle selectedItem = tableView.getSelectionModel().getSelectedItem();
		/*
		 * Samo se kreira nova forma u AdminPejnu kako bi se mogla povezati sa njim
		 * Omogucava da se zna koji je tip vozila odabran u ComboBox-u
		 */
		adminPane.setAddVehicleForm();
		AddVehicleForm addVehicleForm = adminPane.getAddVehicleForm();
		
		addVehicleForm.getTFieldName().setText(selectedItem.getName());
		addVehicleForm.getTFieldChassisNumber().setText(String.valueOf(selectedItem.getChassisNumber()));
		addVehicleForm.getTFieldEngineNumber().setText(String.valueOf(selectedItem.getEngineNumber()));
		addVehicleForm.getTFieldRegistrationNumber().setText(selectedItem.getRegistrationNumber());
		addVehicleForm.getTFieldPicture().setText(selectedItem.getImagePath().substring(5));
		
		if(selectedItem instanceof Car) {
			addVehicleForm.addCarField();
			addVehicleForm.getTFieldDoorNumber().setText(String.valueOf(((Car) selectedItem).getDoorNumber()));
		}else if(selectedItem instanceof Van) {
			addVehicleForm.addVanFiled();
			addVehicleForm.getTFieldCapacity().setText(String.valueOf(((Van) selectedItem).getCapacity()));
		}
		
		addVehicleForm.getbuttonChoosePicture().setOnAction(new ChooseVehicleForUpdate());
		addVehicleForm.addButtonUpdate();
		
		
		addVehicleForm.initModality(Modality.APPLICATION_MODAL);
		addVehicleForm.showAndWait();
		addVehicleForm.close();
	}

}
