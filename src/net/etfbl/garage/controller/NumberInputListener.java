package net.etfbl.garage.controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class NumberInputListener implements ChangeListener<String> {
	TextField textField;

	public NumberInputListener(TextField tField) {
		super();
		textField = tField;
	}

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (!newValue.matches("\\d*")) {
			textField.setText(oldValue);
		}
	}

}
