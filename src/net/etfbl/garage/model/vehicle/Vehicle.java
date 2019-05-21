package net.etfbl.garage.model.vehicle;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.etfbl.garage.controller.user.VehicleController;
import net.etfbl.garage.model.field.GarageFieldReference;

public abstract class Vehicle implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String name;
	protected Integer chassisNumber; //6 digits
	protected Integer engineNumber;  //6 digits
	protected String  picture;
	protected String registrationNumber;
	protected transient boolean leavingParking;
	protected transient boolean moving;
	protected transient boolean hadAccident;
	protected transient boolean goingToAccident;
	protected transient boolean intersectionPrivilege;
	protected transient boolean wantedVehicle;
	
	protected transient Vehicle myAccidentVehicle;
	
	protected Instant timeStart = null;
	
	protected GarageFieldReference currentPosition = null;
	protected List<GarageFieldReference> whereICan;
	protected transient VehicleController controller = null;
	
	
	protected Vehicle() {
		super();
		whereICan = new CopyOnWriteArrayList<>();
		timeStart = Instant.now();
	}
		
	protected Vehicle(String name, int chasissNumber, int engineNumber, String picutre, String registrationNumber) {
		this();
		this.name = name;
		this.chassisNumber = chasissNumber;
		this.engineNumber = engineNumber;
		this.picture = picutre;
		this.registrationNumber = registrationNumber;
		if(whereICan == null)
			this.whereICan = new ArrayList<>();
	}
	
	protected Vehicle(Vehicle vehicle) {
		this();
		this.name = vehicle.name;
		this.chassisNumber = vehicle.chassisNumber;
		this.engineNumber = vehicle.engineNumber;
		this.picture = vehicle.picture;
		this.registrationNumber = vehicle.registrationNumber;
		this.timeStart = vehicle.timeStart;
		if(whereICan == null)
			this.whereICan = vehicle.whereICan;

	}
	

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getChassisNumber() {
		return chassisNumber;
	}
	public void setChasisNumber(int chasissNumber) {
		this.chassisNumber = chasissNumber;
	}
	public int getEngineNumber() {
		return engineNumber;
	}
	public void setEngineNumber(int engineNumber) {
		this.engineNumber = engineNumber;
	}
	public ImageView getPicture() {
		Image image = new Image(this.picture);
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		imageView.setFitHeight(80);
		imageView.setFitWidth(120);
		return imageView;
	}
	public String getImagePath() {
		return this.picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public boolean isLookingForParking() {
		return !this.leavingParking;
	}
	public void SetNotLookingForParking() {
		this.leavingParking = true;
	}
	public  void setIsLookingForParking(boolean isLooking) {
		this.leavingParking = !isLooking;
	}
	
	public void setCurrentPosition(GarageFieldReference garageFiledReference) {
		this.currentPosition = garageFiledReference;
	}
	
	public GarageFieldReference getCurrentPosition() {
		return this.currentPosition;
	}
	
	public VehicleController getVehicleController() {
		if(controller == null) {
			controller = new VehicleController(this);
		}
		return controller;
	}
	
	public void addVehicleController() {
		if(controller == null) {
			controller = new VehicleController(this);
		}
	}
	
	public GarageFieldReference getNextField() {
		if(!whereICan.isEmpty()) {
			return whereICan.get(0);
		}
		return null;
	}
	
	public void cleanWhereICan() {
		whereICan.clear();
	}
	
	public List<GarageFieldReference> getWhereICan(){
		if(whereICan == null) {
			whereICan = new ArrayList<>();
		}
		return whereICan;
	}
	
	public void addAllWhereICan(List<GarageFieldReference> list) {
		if(whereICan == null) {
			whereICan = new ArrayList<>();
		}
		whereICan.addAll(list);
	}
	
	public void addWhereICan(GarageFieldReference position) {
		if(whereICan == null) {
			whereICan = new ArrayList<>();
		}
		whereICan.add(position);
	}
	
	public boolean canMove() {
		if(this.getWhereICan().isEmpty()) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public long getDurationInHours() {
		return Duration.between(timeStart, Instant.now()).toHours();
	}
	
	public void setMoving() {
		moving = true;
	}
	
	public void setNotMoving() {
		moving = false;
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	public void setHadAccident() {
		hadAccident = true;
	}
	
	public boolean getHadAccident() {
		return hadAccident;
	}
	
	public void setGoingToAccident(boolean going) {
		goingToAccident = going;
	}
	
	public boolean isGoingToAccident() {
		return goingToAccident;
	}
	
	public void setMyAccidentVehicle(Vehicle vehicle) {
		myAccidentVehicle = vehicle;
	}
	
	public Vehicle getMyAccidentVehicle() {
		return myAccidentVehicle;
	}
	
	public void setInteresectionPrivilege(boolean privilege) {
		intersectionPrivilege = privilege;
	}
	
	public boolean isInteresectionPrivilege() {
		return intersectionPrivilege;
	}
	
	public void setIsWantedVehicle(boolean wanted) {
		wantedVehicle = wanted;
	}
	
	public boolean isWantedVehicle() {
		return wantedVehicle;
	}
	
	public static boolean isActive(Vehicle vehicle) {
		if(vehicle == null || !vehicle.isMoving()) {
			return false;
		}
		else {
			return true;
		}
	}
}
