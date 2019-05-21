package net.etfbl.garage.controller.user;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import net.etfbl.garage.model.field.GarageFieldReference;
import net.etfbl.garage.model.field.ParkingFieldReference;
import net.etfbl.garage.model.field.TextArePlatform;
import net.etfbl.garage.model.field.VehicleField;
import net.etfbl.garage.model.vehicle.AmbulanceCar;
import net.etfbl.garage.model.vehicle.AmbulanceVan;
import net.etfbl.garage.model.vehicle.Car;
import net.etfbl.garage.model.vehicle.FirefighterVan;
import net.etfbl.garage.model.vehicle.Motorcycle;
import net.etfbl.garage.model.vehicle.PoliceCar;
import net.etfbl.garage.model.vehicle.PoliceMotorcycle;
import net.etfbl.garage.model.vehicle.PoliceVan;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.model.vehicle.interfaces.Ambulance;
import net.etfbl.garage.model.vehicle.interfaces.FireFighter;
import net.etfbl.garage.model.vehicle.interfaces.Police;
import net.etfbl.garage.model.vehicle.interfaces.Rotation;

public class TextAreasPlatformsController {
	static TextAreasPlatformsController textAreasPlatformsContrller = null;
	List<TextArePlatform> textAreaList;

	private TextAreasPlatformsController() {
		textAreaList = new ArrayList<>();
	}

	public static TextAreasPlatformsController getInstance() {
		if (textAreasPlatformsContrller == null) {
			textAreasPlatformsContrller = new TextAreasPlatformsController();
		}
		return textAreasPlatformsContrller;
	}

	public void addPlatform(TextArePlatform platform) {
		textAreaList.add(platform);
	}

	public TextArePlatform getPlatfrom(int i) {
		return textAreaList.get(i);
	}

	public int size() {
		return textAreaList.size();
	}

	public void setFiled(int platformNumber, int row, int col, String label) {
		TextArea textArea = textAreaList.get(platformNumber - 1).getTexArea();

		Platform.runLater(() -> {
			if (label.length() == 1) {
				textArea.selectRange((57 * (2 * row + 1)) + (7 * col + 3), (57 * (2 * row + 1)) + (7 * col + 4));
				textArea.replaceSelection(label);
			} else {
				textArea.selectRange((57 * (2 * row + 1)) + (7 * col + 2), (57 * (2 * row + 1)) + (7 * col + 5));
				textArea.replaceSelection(label);
			}
		});

	}

	public synchronized void setTextAreField(GarageFieldReference garageFieldReference) {
		String label = "";
		if (garageFieldReference.getGarageField() instanceof VehicleField) {
			VehicleField vehicleField = (VehicleField) garageFieldReference.getGarageField();
			Vehicle vehicle = vehicleField.getVehicle();
			if (garageFieldReference.isAccidentPlace()) {
				label = " X ";
			} else if (!(vehicle instanceof Rotation)) {
				label = " V ";
			} else if (vehicle instanceof Police) {
				if (vehicle instanceof Car) {
					PoliceCar policeCar = (PoliceCar) vehicle;
					if (policeCar.isRotationTurnOn()) {
						label = "P R";
					} else {
						label = " P ";
					}
				} else if (vehicle instanceof Motorcycle) {
					PoliceMotorcycle policeMoto = (PoliceMotorcycle) vehicle;
					if (policeMoto.isRotationTurnOn()) {
						label = "P R";
					} else {
						label = " P ";
					}
				} else {
					PoliceVan policeVan = (PoliceVan) vehicle;
					if (policeVan.isRotationTurnOn()) {
						label = "P R";
					} else {
						label = " P ";
					}
				}
			} else if (vehicle instanceof Ambulance) {
				if (vehicle instanceof Car) {
					AmbulanceCar ambulanceCar = (AmbulanceCar) vehicle;
					if (ambulanceCar.isRotationTurnOn()) {
						label = "A R";
					} else {
						label = " A ";
					}
				} else {
					AmbulanceVan ambulanceVan = (AmbulanceVan) vehicle;
					if (ambulanceVan.isRotationTurnOn()) {
						label = "A R";
					} else {
						label = " A ";
					}
				}
			} else if (vehicle instanceof FireFighter) {
				FirefighterVan firefighterVan = (FirefighterVan) vehicle;
				if (firefighterVan.isRotationTurnOn()) {
					label = "F R";
				} else {
					label = " F ";
				}
			}
		} else if (garageFieldReference instanceof ParkingFieldReference) {
			label = " * ";
		} else {
			label = "   ";
		}
		this.setFiled(garageFieldReference.getPlatformNumber(), garageFieldReference.getRow(),
				garageFieldReference.getCol(), label);
	}
}
