package net.etfbl.garage.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class LengthInputListener implements ChangeListener<String> {
	TextField textField;
	int length;

	public LengthInputListener(TextField tField, int length) {
		super();
		this.textField = tField;
		this.length = length;
	}

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (newValue.matches("\\w{" + (length + 1) + "}")) {
			textField.setText(oldValue.substring(0, this.length));
		}
	}

}
