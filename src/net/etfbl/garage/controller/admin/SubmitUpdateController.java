package net.etfbl.garage.controller.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import net.etfbl.garage.model.vehicle.Car;
import net.etfbl.garage.model.vehicle.Motorcycle;
import net.etfbl.garage.model.vehicle.Van;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.utils.Messages;
import net.etfbl.garage.view.admin.AddVehicleForm;
import net.etfbl.garage.view.admin.AdministratorPane;
import net.etfbl.garage.view.admin.VehicleTableView;

/*
 * Na formi za izmjenu podataka o vozilu klikom na dugme "Potvrdi", cuvaju se sve korektne promjene
 * podataka o vozilu
 */

public class SubmitUpdateController implements EventHandler<ActionEvent>{
	AdministratorPane activePane;
	Vehicle selectedItem;
	Vehicle newVehicle;

	@Override
	public void handle(ActionEvent arg0) {
		Messages messages = Messages.getInstance();
		activePane = AdministratorController.getInstance().getActivePane();
		selectedItem = activePane.getVehicleTableView().getSelectionModel().getSelectedItem();
		
		String name = activePane.getAddVehicleForm().getTFieldName().getText();
		Integer chasissNumber = Integer.parseInt(activePane.getAddVehicleForm().getTFieldChassisNumber().getText());
		Integer engineNumber = Integer.parseInt(activePane.getAddVehicleForm().getTFieldEngineNumber().getText());
		String picturePath = "file:" + activePane.getAddVehicleForm().getTFieldPicture().getText();
		String registrationNumber = activePane.getAddVehicleForm().getTFieldRegistrationNumber().getText().toUpperCase();
		
		boolean existRegistration = existVehicle(registrationNumber);
		boolean sameRegistration = selectedItem.getRegistrationNumber().equals(registrationNumber);
		
		//Sva polja moraju biti popunjena
		if(existEmptyField()) {
			 Alert alert = new Alert(AlertType.ERROR);
			 alert.setTitle(messages.getErrorMessage());
			 alert.setContentText(messages.getEmtyFiledMessages());
			 alert.showAndWait();
		}else if(existRegistration && !sameRegistration) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(messages.getErrorMessage());
			alert.setContentText(messages.getExistRegistrationNumberMessages());
			alert.showAndWait();
		}		
		else {
			if(selectedItem instanceof Car) {
			Integer doorNumber = Integer.parseInt(activePane.getAddVehicleForm().getTFieldDoorNumber().getText());
			
			((Car) selectedItem).setDoorNumber(doorNumber);
			}
			else if(selectedItem instanceof Van) {
				Integer capacity = Integer.parseInt(activePane.getAddVehicleForm().getTFieldCapacity().getText());
				newVehicle = new Van(name, chasissNumber, engineNumber, picturePath, registrationNumber, capacity);
			}
			else {
				newVehicle = new Motorcycle(name, chasissNumber, engineNumber, picturePath, registrationNumber);
			}
			
			selectedItem.setName(name);
			selectedItem.setChasisNumber(chasissNumber);
			selectedItem.setEngineNumber(engineNumber);
			selectedItem.setPicture(picturePath);
			selectedItem.setRegistrationNumber(registrationNumber);

//			activePane.getRemoveVehicleButton().fire();
//			activePane.getVehicleTableView().addVehicle(newVehicle);
			activePane.getVehicleTableView().refresh();
			activePane.getAddVehicleForm().close();
		}
	}
		
	
	/*
	 * Provjerava se da li postoji prazno polje na formi za unos podataka o novom vozilu
	 * koja je vezana za trenutno aktivni UserPane
	 */
	public boolean existEmptyField() {
		AddVehicleForm vehicleForm = activePane.getAddVehicleForm();
		boolean eName = vehicleForm.getTFieldName().getText().equals("");
		boolean eChassisNumber = vehicleForm.getTFieldChassisNumber().getText().equals("");
		boolean eEngineNumber = vehicleForm.getTFieldEngineNumber().getText().equals("");
		boolean ePicture =  vehicleForm.getTFieldPicture().getText().equals("");
		boolean eRegNumber = vehicleForm.getTFieldRegistrationNumber().getText().equals("");
		boolean eDoorNumber = (selectedItem instanceof Car) &&  vehicleForm.getTFieldDoorNumber().getText().equals("");
		boolean eCapacity = (selectedItem instanceof Van) && vehicleForm.getTFieldCapacity().getText().equals("");
		
		return(eName || eChassisNumber || eEngineNumber|| ePicture || eRegNumber || eDoorNumber || eCapacity );
	}
	
	//Provjerava da li postoji vozilo sa datom registracijom
	public boolean existVehicle(String registrationNumber) {
		AdministratorController adminController= AdministratorController.getInstance();
		
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
