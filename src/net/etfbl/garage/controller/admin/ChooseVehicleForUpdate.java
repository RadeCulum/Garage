package net.etfbl.garage.controller.admin;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import net.etfbl.garage.model.vehicle.Car;
import net.etfbl.garage.model.vehicle.Motorcycle;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.utils.GarageProperties;
import net.etfbl.garage.utils.GarageUtils;
import net.etfbl.garage.view.admin.AdministratorPane;

/*
 * Na formi za izmjenu podataka za selektovano vozilo, klikom na dugme "Choose" se otvara
 * folder sa slikama vozila odgovarajuceg tipa
 */

public class ChooseVehicleForUpdate implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent e) {
		GarageProperties properties = GarageProperties.getInstance();
		AdministratorPane activePane = AdministratorController.getInstance().getActivePane();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(properties.getChoosePictureTitle());
		String directoryString = properties.getVehicleFolderName();
		Vehicle selectedVehicle = activePane.getVehicleTableView().getSelectionModel().getSelectedItem();
		
		if(selectedVehicle instanceof Motorcycle) {
			directoryString += properties.getFileSeparator() + properties.getMotorcycleFolderName();
		}else if(selectedVehicle instanceof Car) {
			directoryString += properties.getFileSeparator() + properties.getCarFolderName();
		}else {
			directoryString += properties.getFileSeparator() + properties.getVanFolderName();
		}
		
		File directory = new File(directoryString);
		fileChooser.setInitialDirectory(directory);
		File file = fileChooser.showOpenDialog(activePane.getAddVehicleForm().getScene().getWindow());	
		if(file != null) {
			String fileName = directoryString + properties.getFileSeparator() + file.getName();
			activePane.getAddVehicleForm().getTFieldPicture().setText(fileName);
			//Ako je polje za naziv vozila prazno, popunjava se nazivom odabrane slike
			if(activePane.getAddVehicleForm().getTFieldName().getText().equals("")) {
				activePane.getAddVehicleForm().getTFieldName().setText(GarageUtils.getInstance().URLToFileName(fileName));
			}
		}
	}
}
