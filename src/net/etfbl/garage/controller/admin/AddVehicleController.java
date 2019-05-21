package net.etfbl.garage.controller.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import net.etfbl.garage.enumeration.VehicleType;
import net.etfbl.garage.view.admin.AddVehicleForm;

/*
 * Kada se klikne na dugme "Dodaj vozilo", otvara se forma za unos podataka o vozilu
 * u zavisnosti od tipa vozila odabranog u ComboBoxu
 */

public class AddVehicleController implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent arg0) {
		AdministratorController.getInstance().getActivePane().setAddVehicleForm();
		AddVehicleForm addVehicleForm = AdministratorController.getInstance().getActivePane().getAddVehicleForm();
		String vehicleType = AdministratorController.getInstance().getActivePane().getVehicleCBox().getValue();	

		if(vehicleType.equals(VehicleType.AUTOMOBIL.getType())) {
			addVehicleForm.addCarField();
		}
		else if(vehicleType.equals(VehicleType.KOMBI.getType())){
			addVehicleForm.addVanFiled();
		}
		
		addVehicleForm.getbuttonChoosePicture().setOnAction(new ChooseVehicleForInsertController());
		addVehicleForm.addButtonAdd();
		addVehicleForm.initModality(Modality.APPLICATION_MODAL);
		addVehicleForm.showAndWait();
		addVehicleForm.close();
	}

}
