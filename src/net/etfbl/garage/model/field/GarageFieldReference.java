package net.etfbl.garage.model.field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.etfbl.garage.controller.user.TextAreasPlatformsController;
import net.etfbl.garage.model.vehicle.Vehicle;

public class GarageFieldReference implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected transient GarageField garageField;
	protected transient List<GarageFieldReference> nextFields;
	protected transient List<GarageFieldReference> extraFileds;
	protected transient GarageFieldReference referenceToMe;
	protected transient GarageField oldGarageFiled;
	protected transient boolean occupied;
	protected transient boolean accidentPlace;
	int platformNumber;
	int row;
	int col;
	
	public GarageFieldReference(int row, int col, int platformNumber) {
		garageField = new EmptyFiled();
		nextFields = new CopyOnWriteArrayList<>();
		extraFileds = new ArrayList<>();
		this.platformNumber = platformNumber;
		this.row = row;
		this.col = col;
	}
	
	public GarageFieldReference(GarageField garageField, int row, int col) {
		this.garageField = garageField;
		nextFields = new  CopyOnWriteArrayList<>();
		extraFileds = new ArrayList<>();
		this.row = row;
		this.col = col;
	}
	
	public void setOccupied() {
		this.occupied = true;
	}
	public void setFree() {
		this.occupied = false;
	}
	
	public boolean isOccupied() {
		return this.occupied;
	}
	
	public void setGarageField(GarageField garageField) {
		this.garageField = garageField;
	}
	
	public GarageField getGarageField() {
		return this.garageField;
	}
	
	public void setReferenceToMeInNextFields() {
		Iterator<GarageFieldReference> itr = nextFields.iterator(); 
        while (itr.hasNext()) {
            GarageFieldReference gfr = (GarageFieldReference)itr.next();
            gfr.setReferenceToMe(this);
        }
	}
	
	public void setReferenceToMe(GarageFieldReference garageFieldReference) {
		referenceToMe = garageFieldReference;
	}
	
	public GarageFieldReference getReferenceToMe() {
		return this.referenceToMe;
	}
	
	public void addNextField(GarageFieldReference garageFieldReference) {
		nextFields.add(garageFieldReference);
	}
	public void addExtraFiled(GarageFieldReference garageFiledReference) {
		if(!extraFileds.contains(garageFiledReference)) {
			extraFileds.add(garageFiledReference);
		}
	}
	
	public boolean  existsExtraAccidentFiled() {
		for(GarageFieldReference field : extraFileds) {
			if(field.isAccidentPlace()) {
				return true;
			}
		}
		return false;
	}
	
	
	public List<GarageFieldReference> getNextFilds(){
		return nextFields;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.col;
	}
	
	public int getPlatformNumber() {
		return this.platformNumber;
	}
	
	public void setToTextArea() {
		TextAreasPlatformsController.getInstance().setTextAreField(this);
	}
	
	public void setAccitdentPlace(boolean is) {
		accidentPlace = is;
	}
	
	public boolean isAccidentPlace() {
		return accidentPlace;
	}
	
	public void setOldGarageFiled(GarageField oldFiled) {
		oldGarageFiled = oldFiled;
	}
	
	public void clearOldGarageFiled() {
		oldGarageFiled = null;
	}
	
	public GarageField getOldFiled() {
		return oldGarageFiled;
	}
	
	
	@Override 
	public String toString() {
		String text = "[" + row + "][" + col + "]= ";
		for(GarageFieldReference garageFieldReference : nextFields) {
			if(garageFieldReference instanceof ParkingFieldReference)
				text += "P";
			else if(garageFieldReference instanceof InputFieldReference)
				text += "U";
			else if(garageFieldReference instanceof OutputFieldReference)
				text += "I";
			else if(garageFieldReference instanceof IntersectionReferenceField)
				text += "R";
			else
				text += "G";
			text += "[" + garageFieldReference.getRow() + "][" + garageFieldReference.getCol() + "], ";
		}
		return text;
	}
//	
	public boolean isNextByFreeParkingPlace() {
		for(GarageFieldReference field : nextFields) {
			if(field instanceof ParkingFieldReference && field.getGarageField() instanceof EmptyFiled) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isNextByWantedVehicle() {
		Vehicle vehicle;
		for(GarageFieldReference outerFiled : nextFields) {
			for(GarageFieldReference innerFiled : outerFiled.nextFields) {
				if(innerFiled instanceof ParkingFieldReference && innerFiled.getGarageField() instanceof VehicleField) {
					vehicle = ((VehicleField)innerFiled.getGarageField()).getVehicle();
					if(vehicle.getVehicleController().getMyPlatformController().isWantedVehicle(vehicle)) {
						//innerFiled.setAccitdentPlace(true);
						TextAreasPlatformsController.getInstance().setTextAreField(innerFiled);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Vehicle getWantedVehicle() {
		Vehicle vehicle;
		for(GarageFieldReference outerFiled : nextFields) {
			for(GarageFieldReference innerFiled : outerFiled.nextFields) {
				if(innerFiled instanceof ParkingFieldReference && innerFiled.getGarageField() instanceof VehicleField) {
					vehicle = ((VehicleField)innerFiled.getGarageField()).getVehicle();
					if(vehicle.getVehicleController().getMyPlatformController().isWantedVehicle(vehicle)) {
						return vehicle;
					}
				}
			}
		}
		return null;
	}
	
	
	@Override
	public boolean equals(Object other) {
		if(this == other) {
			return true;
		}
		if(other == null) {
			return false;
		}
		if(other instanceof GarageFieldReference) {
			return true;
		}else {
			return false;
		}
	}
	
}
