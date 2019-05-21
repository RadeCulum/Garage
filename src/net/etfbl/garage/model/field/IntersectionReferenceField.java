package net.etfbl.garage.model.field;

import net.etfbl.garage.enumeration.Intersection;

public class IntersectionReferenceField extends GarageFieldReference{
	private static final long serialVersionUID = 1L;
	
	private Intersection intersectionType;

	public IntersectionReferenceField(GarageField garageField, int row, int col, Intersection type) {
		super(garageField, row, col);
		this.intersectionType = type;
	}
	
	public IntersectionReferenceField(int row, int col, int platformNumber, Intersection type) {
		super(row, col, platformNumber);
		intersectionType = type;
	}

	@Override
	public boolean equals(Object other) {
		if(this == other) {
			return true;
		}
		if(other == null) {
			return false;
		}
		if(other instanceof IntersectionReferenceField) {
			return true;
		}else {
			return false;
		}
	}
	
	public Intersection getType() {
		return intersectionType;
	}
}
