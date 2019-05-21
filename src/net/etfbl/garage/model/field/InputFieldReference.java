package net.etfbl.garage.model.field;

public class InputFieldReference extends GarageFieldReference{
	private static final long serialVersionUID = 1L;

	public InputFieldReference(GarageField garageField, int row, int col) {
		super(garageField, row, col);
	}
	
	public InputFieldReference(int row, int col, int platformNumber) {
		super(row, col, platformNumber);
	}

	@Override
	public boolean equals(Object other) {
		if(this == other) {
			return true;
		}
		if(other == null) {
			return false;
		}
		if(other instanceof InputFieldReference) {
			return true;
		}else {
			return false;
		}
	}
}
