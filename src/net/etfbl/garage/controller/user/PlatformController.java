package net.etfbl.garage.controller.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import net.etfbl.garage.controller.VehicleReader;
import net.etfbl.garage.model.field.EmptyFiled;
import net.etfbl.garage.model.field.GarageField;
import net.etfbl.garage.model.field.GarageFieldReference;
import net.etfbl.garage.model.field.ParkingFieldReference;
import net.etfbl.garage.model.field.PlatformMatrix;
import net.etfbl.garage.model.field.TextArePlatform;
import net.etfbl.garage.model.field.VehicleField;
import net.etfbl.garage.model.vehicle.AmbulanceCar;
import net.etfbl.garage.model.vehicle.AmbulanceVan;
import net.etfbl.garage.model.vehicle.Car;
import net.etfbl.garage.model.vehicle.CarWithRotation;
import net.etfbl.garage.model.vehicle.FirefighterVan;
import net.etfbl.garage.model.vehicle.Motorcycle;
import net.etfbl.garage.model.vehicle.MotorcycleWithRotation;
import net.etfbl.garage.model.vehicle.PoliceCar;
import net.etfbl.garage.model.vehicle.PoliceMotorcycle;
import net.etfbl.garage.model.vehicle.PoliceVan;
import net.etfbl.garage.model.vehicle.Van;
import net.etfbl.garage.model.vehicle.VanWithRotation;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.model.vehicle.interfaces.Rotation;
import net.etfbl.garage.utils.GarageLockers;
import net.etfbl.garage.utils.GarageProperties;
import net.etfbl.garage.utils.GarageUtils;
import net.etfbl.garage.view.user.UserPane;

public class PlatformController {
	private UserPane userPane;
	private PlatformMatrix platformMatrix;
	private TextArePlatform textAreaPlatform;

	private List<Vehicle> vehiclesOnPlatform;
	private List<Vehicle> rotationVehicleList;

	private Set<String> wantedVehicle;
	private static Set<String> registrationNumberSet = new HashSet<>();

	private boolean blockedParking = false;
	private boolean blockedPlatform = false;
	private boolean investigationTime = false;
	public static boolean existAccident = false;
	private boolean accidentOnThisPlatform = false;
	
	private int freeParkingPlacesCounter = 28;
	public static int garageVehicleCounter = 0;
	
	private int accidentSector;

	public static Object entryFiledLocker = new Object();
	public Object investigationLocker = new Object();
	public static Object waiter = new Object();

	private Logger logger = GarageUtils.getInstance().getLogger();

	public PlatformController(UserPane userPane) {
		this.userPane = userPane;
		this.userPane.setController(this);

		this.platformMatrix = new PlatformMatrix();
		this.platformMatrix.setController(this);

		this.textAreaPlatform = new TextArePlatform(userPane.getPlatformTextArea());
		TextAreasPlatformsController.getInstance().addPlatform(textAreaPlatform);

		this.vehiclesOnPlatform = new CopyOnWriteArrayList<>();
		this.rotationVehicleList = new CopyOnWriteArrayList<>();

		this.wantedVehicle = new HashSet<>();
		this.readWantedVehicle();
	}

	private void readWantedVehicle() {
		File file = new File(GarageProperties.getInstance().getWantedVehicleFile());
		FileReader reader = null;
		BufferedReader bufferedReade = null;
		try {
			reader = new FileReader(file);
			bufferedReade = new BufferedReader(reader);
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
		String registrationNumber;
		try {
			while ((registrationNumber = bufferedReade.readLine()) != null) {
				wantedVehicle.add(registrationNumber);
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}

		try {
			reader.close();
			bufferedReade.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}

	public boolean existWantedVehicle() {
		for (Vehicle vehcile : vehiclesOnPlatform) {
			if (wantedVehicle.contains(vehcile.getRegistrationNumber())) {
				return true;
			}
		}
		return false;
	}

	public boolean isWantedVehicle(Vehicle vehicle) {
		return wantedVehicle.contains(vehicle.getRegistrationNumber());
	}

	public synchronized void addRotationVehicle(Vehicle vehicle) {
		if (vehicle.getVehicleController().isRotationOn()) {
			if (!rotationVehicleList.contains(vehicle)) {
				rotationVehicleList.add(vehicle);
			}
		}
	}

	public synchronized void removeRotationVehicle(Vehicle vehicle) {
		rotationVehicleList.remove(vehicle);
	}

	public synchronized boolean existsVehicleWithRotation() {
		if (rotationVehicleList.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public List<Vehicle> getAllVehicleFromPlatform() {
		return vehiclesOnPlatform;
	}

	public List<Vehicle> getVehiclesForMoveOut() {
		List<Vehicle> tempList = new ArrayList<>(vehiclesOnPlatform);
		List<Vehicle> vehiclesForOut = new ArrayList<>();
		int limit = (int) ((vehiclesOnPlatform.size() / 100.0) * 15);
		if(limit == 0 && vehiclesOnPlatform.size() > 0) {
			limit = 1;
		}
		Random random = new Random();
		for (int i = 0; i < limit; ++i) {
			int pos = random.nextInt(tempList.size());
			vehiclesForOut.add(tempList.get(pos));
			tempList.remove(pos);
		}

		return vehiclesForOut;
	}

	public int getFreeParkingNumber() {
		return this.freeParkingPlacesCounter;
	}

	public synchronized void decrementFreeParkingPlaceCounter() {
		this.freeParkingPlacesCounter--;
		if (freeParkingPlacesCounter == 0) {
			blockPlatformParking();
		}

		Platform.runLater(() -> {
			this.userPane.getCurrentFreeParkingPlacesLabel().setText(String.valueOf(freeParkingPlacesCounter));
		});

	}

	public synchronized void incrementFreeParkingPlaceCounter() {
		this.freeParkingPlacesCounter++;
		if (freeParkingPlacesCounter == 1) {
			unblockPlatformParking();
		}

		Platform.runLater(() -> {
			this.userPane.getCurrentFreeParkingPlacesLabel().setText(String.valueOf(freeParkingPlacesCounter));
		});
	}

	public void setPlatform() {
		ArrayList<Vehicle> vehiclesOnPlatform = new VehicleReader()
				.GetElementFromPlatform(platformMatrix.getPlatformNumber() - 1);
		for (Vehicle vehicle : vehiclesOnPlatform) {
			if (vehicle.getCurrentPosition() != null) {
				garageVehicleCounter++;
				registrationNumberSet.add(vehicle.getRegistrationNumber());
				decrementFreeParkingPlaceCounter();
				setVehicle(vehicle);
			}
		}

		for (Vehicle vehicle : vehiclesOnPlatform) {
			if (vehicle.getCurrentPosition() == null) {
				garageVehicleCounter++;
				registrationNumberSet.add(vehicle.getRegistrationNumber());
				decrementFreeParkingPlaceCounter();
				setVehicleRandom(vehicle);
			}
		}
	}

	public void fillPlatform() {
		int minimum = 0;
		try {
			minimum = Integer.parseInt(userPane.getMinimumVehicleNumberTField().getText());
		}catch(Exception e) {
			minimum = 0;
		}
		for (int i = vehiclesOnPlatform.size(); i < minimum; ++i) {
			Vehicle vehicle = PlatformController.makeNewVehicle();
			setVehicleRandom(vehicle);
			decrementFreeParkingPlaceCounter();
		}
	}

	public PlatformMatrix getPlatform() {
		return this.platformMatrix;
	}

	public TextArePlatform getTextAreaPlatform() {
		return this.textAreaPlatform;
	}

	public List<Vehicle> getVehicleFromPlatform() {
		return this.vehiclesOnPlatform;
	}

	public synchronized int getVehiclesNumber() {
		return vehiclesOnPlatform.size();
	}

	public void blockPlatformParking() {
		blockedParking = true;
	}

	public boolean isBlockedParking() {
		return blockedParking;
	}

	public void unblockPlatformParking() {
		blockedParking = false;
	}

	public void blockPlatform() {
		blockedPlatform = true;
	}

	public boolean isBlocked() {
		return blockedPlatform;
	}

	public void unblockPlatform() {
		blockedPlatform = false;
	}

	public static boolean isOnTheInputEnd(Vehicle vehicle) {
		if (vehicle.getCurrentPosition().getPlatformNumber() == UserController.getInstance().getPlatformsNumber()
				&& vehicle.getCurrentPosition().getCol() == 7 && vehicle.getCurrentPosition().getRow() == 1) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isOnTheOutputEnd(Vehicle vehicle) {
		if (vehicle.getCurrentPosition().getPlatformNumber() == UserController.getInstance().getPlatformsNumber()
				&& vehicle.getCurrentPosition().getCol() == 7 && vehicle.getCurrentPosition().getRow() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static Vehicle makeNewVehicle() {
		GarageUtils garageUtils = GarageUtils.getInstance();
		Vehicle vehicle = null;
		Random random = new Random();
		String vehicleName;
		String vehiclePicturePath;
		Integer chassisNumber = random.nextInt(999999);
		Integer engineNumber = random.nextInt(999999);
		String registrationNumber = garageUtils.getRandomRegistrationNumber();

		while (existRegistrationNUmber(registrationNumber)) {
			registrationNumber = garageUtils.getRandomRegistrationNumber();
		}

		int vehicleType = random.nextInt(3);
		if (vehicleType == 0) {// Motor
			vehiclePicturePath = garageUtils.getRelativePathFromFolder("VehiclePictures\\Motocikli");
			vehicleName = garageUtils.URLToFileName(vehiclePicturePath);
			vehicle = new Motorcycle(vehicleName, chassisNumber, engineNumber, vehiclePicturePath, registrationNumber);
		} else if (vehicleType == 1) {// Automobil
			vehiclePicturePath = garageUtils.getRelativePathFromFolder("VehiclePictures\\Automobili");
			vehicleName = garageUtils.URLToFileName(vehiclePicturePath);
			Integer doorNumber = random.nextInt(3) + 2;
			vehicle = new Car(vehicleName, chassisNumber, engineNumber, vehiclePicturePath, registrationNumber,
					doorNumber);
		} else {// Kombi
			vehiclePicturePath = garageUtils.getRelativePathFromFolder("VehiclePictures\\Kombiji");
			vehicleName = garageUtils.URLToFileName(vehiclePicturePath);
			int capacity = garageUtils.getRandomcapacity();
			vehicle = new Van(vehicleName, chassisNumber, engineNumber, vehiclePicturePath, registrationNumber,
					capacity);
		}
		return vehicle;

	}

	public synchronized static boolean existRegistrationNUmber(String registrationNumber) {
		UserController userController = UserController.getInstance();
		for (int i = 0; i < userController.getPlatformsNumber(); ++i) {
			if (userController.getControlerOfUserPaneOnIndex(i).getRegistrationSet().contains(registrationNumber)) {
				return true;
			}
		}
		return false;
	}

	public Set<String> getRegistrationSet() {
		return registrationNumberSet;
	}

	public void addVehicleToGarage(Vehicle vehicle) {
		this.vehiclesOnPlatform.add(vehicle);
		if (!(vehicle instanceof Rotation)) {
			UserController.basicVehicleCounter++;
		}
		this.incrementCurrentVehiclesNumber();

	}

	public void addVehicleToPlatform(Vehicle vehicle) {
		this.vehiclesOnPlatform.add(vehicle);
		this.incrementCurrentVehiclesNumber();

	}

	public void removeVehicleFromGarage(Vehicle vehicle) {
		vehicle.getCurrentPosition().setGarageField(new EmptyFiled());
		this.vehiclesOnPlatform.remove(vehicle);
		if (!(vehicle instanceof Rotation)) {
			UserController.basicVehicleCounter--;
		}
		this.decrementCurrentVehiclesNumber();

		if (vehicle.getVehicleController().isRotationOn()) {
			removeRotationVehicle(vehicle);
		}

	}

	public void removeVehicleFromPlatform(Vehicle vehicle) {
		vehicle.getCurrentPosition().setGarageField(new EmptyFiled());
		this.vehiclesOnPlatform.remove(vehicle);
		this.decrementCurrentVehiclesNumber();

		if (vehicle.getVehicleController().isRotationOn()) {
			removeRotationVehicle(vehicle);
		}

	}

	public void incrementCurrentVehiclesNumber() {
		UserController.allVehicleCounter++;
	}

	public void decrementCurrentVehiclesNumber() {
		UserController.allVehicleCounter--;

	}

	public void setVehiclesRandom(ArrayList<Vehicle> vehicleList) {
		for (Vehicle vehicle : vehicleList) {
			setVehicleRandom(vehicle);
		}
	}

	public void setVehicle(Vehicle vehicle) {
		int row = vehicle.getCurrentPosition().getRow();
		int col = vehicle.getCurrentPosition().getCol();
		GarageFieldReference garageFieldReference = this.getPlatform().getElemnt(row, col);
		GarageField garageField = new VehicleField(vehicle);
		garageFieldReference.setGarageField(garageField);
		garageFieldReference.setOccupied();

		vehicle.addVehicleController();
		vehicle.setCurrentPosition(garageFieldReference);
		this.addVehicleToGarage(vehicle);
		TextAreasPlatformsController.getInstance().setTextAreField(garageFieldReference);
	}

	public void setVehicleRandom(Vehicle vehicle) {
		ArrayList<GarageFieldReference> list = platformMatrix.getEmtyParkingFields();
		GarageFieldReference randomReference = list.get(new Random().nextInt(list.size()));
		this.setVehicle(randomReference, vehicle);
	}

	public synchronized static Vehicle getNewVehicleType(Vehicle vehicle) {
		Random rnd = new Random();
		if (((100.0 / UserController.allVehicleCounter) * UserController.basicVehicleCounter) < 90.0) {
		} else {
			int vehicleType3 = rnd.nextInt(3); // H P F
			int vehicleType2 = rnd.nextInt(2); // H P
			if (vehicle instanceof Motorcycle) {
				MotorcycleWithRotation motoWithRotation = new MotorcycleWithRotation((Motorcycle) vehicle);
				vehicle = new PoliceMotorcycle(motoWithRotation);
			} else if (vehicle instanceof Car) {
				CarWithRotation carWithRotation = new CarWithRotation((Car) vehicle);
				if (vehicleType2 == 0) {
					vehicle = new AmbulanceCar(carWithRotation);
				} else {
					vehicle = new PoliceCar(carWithRotation);
				}
			} else {
				VanWithRotation vanWithRotation = new VanWithRotation((Van) vehicle);
				if (vehicleType3 == 0) {
					vehicle = new AmbulanceVan(vanWithRotation);
				} else if (vehicleType3 == 1) {
					vehicle = new PoliceVan(vanWithRotation);
				} else {
					vehicle = new FirefighterVan(vanWithRotation);
				}
			}
		}
		return vehicle;
	}

	public void setVehicle(GarageFieldReference garageFieldReference, Vehicle vehicle) {
		garageFieldReference.setOccupied();
		if (garageFieldReference instanceof ParkingFieldReference) {
			vehicle.SetNotLookingForParking();
		}
		vehicle = getNewVehicleType(vehicle);
		VehicleField vehicleField = new VehicleField(vehicle);
		this.addVehicleToGarage(vehicle);
		garageFieldReference.setGarageField(vehicleField);
		vehicle.addVehicleController();
		vehicle.setCurrentPosition(garageFieldReference);
		TextAreasPlatformsController.getInstance().setTextAreField(garageFieldReference);
	}

	public synchronized void makeAccidnet(GarageFieldReference firstFiled, GarageFieldReference secondField) {
		synchronized (VehicleController.moveLocker) {
			existAccident = true;
			accidentOnThisPlatform = true;
			UserController.getInstance().disableVehicleAdd();
			blockPlatform();
		}

		if (!(firstFiled.getGarageField() instanceof VehicleField
				&& secondField.getGarageField() instanceof VehicleField)) {
			unblockPlatform();
			setInvestigationTime(false);
			UserController.getInstance().enableVehicleAdd();
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (GarageLockers.addLocker) {
					Vehicle ambulance = makeNewAmbulance(true);
					ambulance.setGoingToAccident(true);

					GarageFieldReference entryFiled;
					synchronized (VehicleController.moveLocker) {
						entryFiled = UserController.getInstance().getPlatformMatrix(0).getElemnt(1, 0);
						if (entryFiled.getGarageField() instanceof VehicleField) {
							Vehicle removeVehicle = ((VehicleField) entryFiled.getGarageField()).getVehicle();
							removeVehicle.getVehicleController().getMyPlatformController()
									.removeVehicleFromGarage(removeVehicle);
							removeVehicle = null;
							UserController.getInstance().decrementThreadCounter();
						}
					}

					entryFiled.setGarageField(new VehicleField(ambulance));
					ambulance.setCurrentPosition(entryFiled);
					TextAreasPlatformsController.getInstance().setTextAreField(entryFiled);
					ambulance.setMoving();
					UserController.getInstance().incrementThreadCounter();
					ambulance.getVehicleController().start();

				}
			}
		}).start();
		
		Vehicle firstVehicle = ((VehicleField) firstFiled.getGarageField()).getVehicle();
		Vehicle secondVehicle = ((VehicleField) secondField.getGarageField()).getVehicle();
		
		if(firstVehicle.getCurrentPosition().getRow() < 2) {
			accidentSector = 1;
		}
		else {
			accidentSector = 2;
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				UserController.getInstance().noteInvestigation(firstVehicle, true);
				UserController.getInstance().noteInvestigation(secondVehicle, false);
			}
		}).start();

		firstVehicle.setHadAccident();
		secondVehicle.setHadAccident();

		firstVehicle.setMyAccidentVehicle(secondVehicle);
		secondVehicle.setMyAccidentVehicle(firstVehicle);

		System.out.println("Sudar");

		firstFiled.setAccitdentPlace(true);
		secondField.setAccitdentPlace(true);

		TextAreasPlatformsController.getInstance().setTextAreField(firstFiled);
		TextAreasPlatformsController.getInstance().setTextAreField(secondField);
	}

	public synchronized void ClearAccidentFiled(Vehicle vehicle) {
		vehicle.getCurrentPosition().setAccitdentPlace(false);
		vehicle.getMyAccidentVehicle().getCurrentPosition().setAccitdentPlace(false);
		TextAreasPlatformsController.getInstance().setTextAreField(vehicle.getCurrentPosition());
		TextAreasPlatformsController.getInstance().setTextAreField(vehicle.getMyAccidentVehicle().getCurrentPosition());
		setInvestigationTime(false);
		unblockPlatform();
		accidentOnThisPlatform = false;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					synchronized (waiter) {
						waiter.wait(5000);
					}
					existAccident = false;
				} catch (InterruptedException e) {
					logger.log(Level.SEVERE, e.fillInStackTrace().toString());
					e.printStackTrace();
					existAccident = false;
				}
			}
		}).start();
	}

	public void addFireFighter() {
		Vehicle fireFighter = makeNewFireFighter(true);
		fireFighter.setGoingToAccident(true);

		GarageFieldReference entryFiled = UserController.getInstance().getPlatformMatrix(0).getElemnt(1, 0);

		entryFiled.setGarageField(new VehicleField(fireFighter));
		fireFighter.setCurrentPosition(entryFiled);
		fireFighter.setMoving();
		TextAreasPlatformsController.getInstance().setTextAreField(entryFiled);
		UserController.getInstance().incrementThreadCounter();
		fireFighter.getVehicleController().start();
	}

	private Vehicle makeNewFireFighter(boolean rotation) {
		Vehicle vehicle;
		do {
			vehicle = makeNewVehicle();
		} while (!(vehicle instanceof Van));

		Van van = (Van) vehicle;
		FirefighterVan fireFighterVan = new FirefighterVan(new VanWithRotation(van));
		fireFighterVan.setRotation(rotation);
		return fireFighterVan;
	}

	public void addPolice() {
		Vehicle police = makeNewPolice(true);
		police.setGoingToAccident(true);

		GarageFieldReference entryFiled = UserController.getInstance().getPlatformMatrix(0).getElemnt(1, 0);

		entryFiled.setGarageField(new VehicleField(police));
		police.setCurrentPosition(entryFiled);
		police.setMoving();
		TextAreasPlatformsController.getInstance().setTextAreField(entryFiled);
		UserController.getInstance().incrementThreadCounter();
		police.getVehicleController().start();
	}

	private Vehicle makeNewPolice(boolean rotation) {
		Vehicle vehicle = makeNewVehicle();
		if (vehicle instanceof Car) {
			Car car = (Car) vehicle;
			PoliceCar policeCar = new PoliceCar(new CarWithRotation(car));
			policeCar.setRotation(rotation);
			return policeCar;
		} else if (vehicle instanceof Motorcycle) {
			Motorcycle motorcycle = (Motorcycle) vehicle;
			PoliceMotorcycle policeMotorcycle = new PoliceMotorcycle(new MotorcycleWithRotation(motorcycle));
			policeMotorcycle.setRotation(rotation);
			return policeMotorcycle;
		} else {
			Van van = (Van) vehicle;
			PoliceVan policeVan = new PoliceVan(new VanWithRotation(van));
			policeVan.setRotation(rotation);
			return policeVan;
		}
	}

	private Vehicle makeNewAmbulance(boolean rotation) {
		Vehicle vehicle;
		do {
			vehicle = makeNewVehicle();
		} while (vehicle instanceof Motorcycle);
		if (vehicle instanceof Car) {
			Car car = (Car) vehicle;
			AmbulanceCar ambulanceCar = new AmbulanceCar(new CarWithRotation(car));
			ambulanceCar.setRotation(rotation);
			return ambulanceCar;
		} else {
			Van van = (Van) vehicle;
			AmbulanceVan ambulanceVan = new AmbulanceVan(new VanWithRotation(van));
			ambulanceVan.setRotation(rotation);
			return ambulanceVan;
		}
	}

	public synchronized void canILockParkingFiled() {
		int counter = 0;
		for (Vehicle vehicle : vehiclesOnPlatform) {
			if (!vehicle.getVehicleController().isRotationOn() && Vehicle.isActive(vehicle)
					&& vehicle.isLookingForParking() && vehicle.getCurrentPosition().getRow() > 1 ||
					(vehicle.getCurrentPosition().getRow() == 1 && vehicle.getCurrentPosition().getCol() == 1) &&
					!(vehicle.getCurrentPosition() instanceof ParkingFieldReference)) {
				counter++;
			}
		}
		if (counter  >= freeParkingPlacesCounter) {
			if (platformMatrix.getPlatformNumber() == UserController.getInstance().getPlatformsNumber()) {
				UserController.getInstance().disableVehicleAdd();
			}
			blockPlatformParking();
		}
	}

	public void setInvestigationTime(boolean is) {
		investigationTime = is;
	}

	public boolean isInvestigationTime() {
		return investigationTime;
	}
	
	public boolean getAccidentOnThisPlatform() {
		return accidentOnThisPlatform;
	}
	
	public int getAccidentSector() {
		return accidentSector;
	}

}
