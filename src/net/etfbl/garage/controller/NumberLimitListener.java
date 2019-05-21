package net.etfbl.garage.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import net.etfbl.garage.controller.user.UserController;

public class NumberLimitListener implements ChangeListener<String> {
	TextField textField;

	public NumberLimitListener(TextField tField) {
		super();
		this.textField = tField;
	}

	@Override
	public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
		if (newValue.matches("29") || newValue.matches("[3-9][0-9]")) {
			textField.setText(oldValue);
			UserController.getInstance().setMinimumVehicleNumberTF(oldValue);
		} else {
			UserController.getInstance().setMinimumVehicleNumberTF(newValue);
		}
	}

}
