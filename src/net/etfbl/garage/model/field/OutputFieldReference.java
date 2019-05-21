package net.etfbl.garage.model.field;

public class OutputFieldReference extends GarageFieldReference{
	private static final long serialVersionUID = 1L;

	public OutputFieldReference(GarageField garageField, int row, int col) {
		super(garageField, row, col);
	}
	
	public OutputFieldReference(int row, int col, int platformNumber) {
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
		if(other instanceof OutputFieldReference) {
			return true;
		}else {
			return false;
		}
	}
}
