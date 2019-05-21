package net.etfbl.garage.controller.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import net.etfbl.garage.enumeration.VehicleType;
import net.etfbl.garage.model.vehicle.Car;
import net.etfbl.garage.model.vehicle.Motorcycle;
import net.etfbl.garage.model.vehicle.Van;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.utils.GarageUtils;
import net.etfbl.garage.utils.Messages;
import net.etfbl.garage.view.admin.AddVehicleForm;
import net.etfbl.garage.view.admin.AdministratorPane;
import net.etfbl.garage.view.admin.VehicleTableView;

/*
 * Klikom na dugme "Dodaj" koje se nalazi na formi za unso podataka o vozilu, na osnovu unesenih podataka
 * kreira se novo vozilo i dodaje u tabelu
 */

public class SubmitInsertController implements EventHandler<ActionEvent>{
	AdministratorPane activePane;
	Vehicle vehicle;


	@Override
	public void handle(ActionEvent arg) {
		Messages messages = Messages.getInstance();
		activePane = AdministratorController.getInstance().getActivePane();

		//Ne smije postojati nepopunjeno polje
		if(existEmptyField()) {
			 Alert alert = new Alert(AlertType.ERROR);
			 alert.setTitle(messages.getErrorMessage());
			 alert.setContentText(messages.getEmtyFiledMessages());
			 alert.showAndWait();
		}
		else {		
			String name = activePane.getAddVehicleForm().getTFieldName().getText();
			Integer chasissNumber = Integer.parseInt(activePane.getAddVehicleForm().getTFieldChassisNumber().getText());
			Integer engineNumber = Integer.parseInt(activePane.getAddVehicleForm().getTFieldEngineNumber().getText());
			String picturePath = "file:" + activePane.getAddVehicleForm().getTFieldPicture().getText();

			String registrationNumber = activePane.getAddVehicleForm().getTFieldRegistrationNumber().getText().toUpperCase();
		
			if(activePane.getVehicleCBox().getValue().equals(VehicleType.AUTOMOBIL.getType())) {
				Integer doorNumber = Integer.parseInt(activePane.getAddVehicleForm().getTFieldDoorNumber().getText());
				vehicle = new Car(name, chasissNumber, engineNumber, picturePath, registrationNumber, doorNumber);
			
			}
			else if(activePane.getVehicleCBox().getValue().equals(VehicleType.KOMBI.getType())) {
				Integer capacity = Integer.parseInt(activePane.getAddVehicleForm().getTFieldCapacity().getText());
				vehicle = new Van(name, chasissNumber, engineNumber, picturePath, registrationNumber, capacity);
			}
			else {
				vehicle = new Motorcycle(name, chasissNumber, engineNumber, picturePath, registrationNumber);
			}
			boolean existRegistration = existVehicle(registrationNumber);
			
			//registracija mora biti oblika SCC-S-CCC
			if(!GarageUtils.getInstance().isCorrectRegistrationNumber(registrationNumber)) {
				Alert alert = new Alert(AlertType.ERROR);
				 alert.setTitle(messages.getErrorMessage());
				 alert.setContentText("Pogresan format registracijskog broja\r\nFormat treba biti:SBB-S-BBB");
				 alert.showAndWait();
			}
			//Ne smije postojati vozilo sa tom registracijom
			else if(existRegistration) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle(messages.getErrorMessage());
				alert.setContentText(messages.getExistRegistrationNumberMessages());
				alert.showAndWait();
			}
			else {
				/*
				 * Poslije odavanja vozila ni jedan red u tabeli nece biti selektovan dugmad za
				 * izmjenu i brisanje moraju biti disable
				 */
				activePane.getChangeVehicleButton().setDisable(true);
				activePane.getRemoveVehicleButton().setDisable(true);
				activePane.getVehicleTableView().addVehicle(vehicle);
				
				if(activePane.getVehicleTableView().getItems().size() >= 28) {
					activePane.getVehicleCBox().setDisable(true);
					activePane.getAddVehicleButton().setDisable(true);
				}
				
				activePane.getAddVehicleForm().close();
				
			}
		}
		
	}
	
	public boolean existEmptyField() {
		AddVehicleForm vehicleForm = activePane.getAddVehicleForm();
		boolean eName = vehicleForm.getTFieldName().getText().equals("");
		boolean eChassisNumber = vehicleForm.getTFieldChassisNumber().getText().equals("");
		boolean eEngineNumber = vehicleForm.getTFieldEngineNumber().getText().equals("");
		boolean ePicture =  vehicleForm.getTFieldPicture().getText().equals("");
		boolean eRegNumber = vehicleForm.getTFieldRegistrationNumber().getText().equals("");
		boolean eDoorNumber = (vehicle instanceof Car) &&  vehicleForm.getTFieldDoorNumber().getText().equals("");
		boolean eCapacity = (vehicle instanceof Van) && vehicleForm.getTFieldCapacity().getText().equals("");
		
		return(eName || eChassisNumber || eEngineNumber|| ePicture || eRegNumber || eDoorNumber || eCapacity );
	}
	
	public boolean existVehicle(String registrationNumber) {
		AdministratorController adminController = AdministratorController.getInstance();
		
		for(int i = 0; i < adminController.getPlatformsNumber(); ++ i) {
			AdministratorPane adminPane = adminController.getElement(i);
			VehicleTableView vehicleTableView = adminPane.getVehicleTableView();
			if(vehicleTableView.getVehicleMap().containsKey(registrationNumber)){
				return true;
			}
		}		
		return false;		
	}
	

}
