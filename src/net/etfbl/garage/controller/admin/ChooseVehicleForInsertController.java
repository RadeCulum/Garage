package net.etfbl.garage.controller.admin;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import net.etfbl.garage.enumeration.VehicleType;
import net.etfbl.garage.utils.GarageProperties;
import net.etfbl.garage.utils.GarageUtils;
import net.etfbl.garage.view.admin.AdministratorPane;

/*
 * Pri dodavanju novog vozila klikom na dugme "Choose" otvara se folder sa slikama
 * vozila odgovarajuceg tipa, zavisno koji tip vozila je selektovan u ComboBox-u
 */

public class ChooseVehicleForInsertController implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent e) {
		GarageProperties properties = GarageProperties.getInstance();
		AdministratorPane activePane = AdministratorController.getInstance().getActivePane();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(properties.getChoosePictureTitle());
		String directoryString = properties.getVehicleFolderName();
		String vehicleType = activePane.getVehicleCBox().getValue();
		
		if(vehicleType.equals(VehicleType.MOTOCIKL.getType())) {
			directoryString += properties.getFileSeparator() + properties.getMotorcycleFolderName();
		}else if(vehicleType.equals(VehicleType.AUTOMOBIL.getType())) {
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
			//Ako ime vozila nije popunjeno, izborom slike automatski se za naziv vozila postavlja naziv slike
			if(activePane.getAddVehicleForm().getTFieldName().getText().equals("")) {
				activePane.getAddVehicleForm().getTFieldName().setText(GarageUtils.getInstance().URLToFileName(fileName));
			}
		}
	}

}
