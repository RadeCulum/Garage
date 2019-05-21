package net.etfbl.garage.controller.user;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.etfbl.garage.enumeration.Intersection;
import net.etfbl.garage.model.field.EmptyFiled;
import net.etfbl.garage.model.field.GarageField;
import net.etfbl.garage.model.field.GarageFieldReference;
import net.etfbl.garage.model.field.InputFieldReference;
import net.etfbl.garage.model.field.IntersectionReferenceField;
import net.etfbl.garage.model.field.OutputFieldReference;
import net.etfbl.garage.model.field.ParkingFieldReference;
import net.etfbl.garage.model.field.VehicleField;
import net.etfbl.garage.model.vehicle.AmbulanceCar;
import net.etfbl.garage.model.vehicle.AmbulanceVan;
import net.etfbl.garage.model.vehicle.CarWithRotation;
import net.etfbl.garage.model.vehicle.FirefighterVan;
import net.etfbl.garage.model.vehicle.MotorcycleWithRotation;
import net.etfbl.garage.model.vehicle.PoliceCar;
import net.etfbl.garage.model.vehicle.PoliceMotorcycle;
import net.etfbl.garage.model.vehicle.PoliceVan;
import net.etfbl.garage.model.vehicle.VanWithRotation;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.model.vehicle.interfaces.Police;
import net.etfbl.garage.model.vehicle.interfaces.Rotation;
import net.etfbl.garage.utils.GarageProperties;
import net.etfbl.garage.utils.GarageUtils;

public class VehicleController extends Thread {

	private static final int speed = GarageProperties.getInstance().getSpeed();
	private Logger logger = GarageUtils.getInstance().getLogger();
	public static Object moveLocker = new Object();
	public static int counter = 0;

	private Vehicle vehicle = null;

	/*
	 * U konstruktoru se ostvaruje veza izmedju vozila i njegovog kontrolera
	 */
	public VehicleController(Vehicle vehicle) {
		if (this.vehicle == null)
			this.vehicle = vehicle;
	}

	/*
	 * Preklopljena metoda klase Thread Uposlenim cekanjem nad statickom metodom
	 * isActive(Vehicle) nit se odrzava u "zivotu" Metoda isActive iz klase Vehicle
	 * mora biti staticka zato sto njen parametar moze biti i null
	 * 
	 * Vozila sa rotacijom se krecu duplo brze da bi se demonstrriro njihov
	 * prioritet
	 * 
	 * Kod "obicnih" vozila je uvedena varijacija u brzini da bi se povecale sanse
	 * za sudar
	 */
	@Override
	public void run() {
		while (Vehicle.isActive(vehicle)) {
			prepareForMove();
			try {
				if (isRotationOn()) {
					sleep(speed / 2);
				} else {
					sleep(speed);
				}
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, e.fillInStackTrace().toString());
				e.printStackTrace();
			}
		}
	}

	/*
	 * Postoji razlika u kretanju vozila sa rotaijom i vozila bez rotacije
	 * 
	 * Kako vozila sa rotacijom blokiraju ostala vozila potrebno je voditi evideciju
	 * o njihovom prisustvu na platformi Dodavanja duplikata nije moguce
	 */
	private void prepareForMove() {
		if (isRotationOn()) {
			getMyPlatformController().addRotationVehicle(vehicle);
			moveWithPriority();
		} else {
			moveWithoutPriority();
		}

	}

	private void moveWithoutPriority() {
		synchronized (getMyPlatformController().investigationLocker) {
			if (getMyPlatformController().isInvestigationTime()) {
				if (vehicle.getCurrentPosition().isAccidentPlace()) {
					try {
						Random rnd = new Random();
						long time = rnd.nextInt(8) + 3;
						sleep(time * 1000);
					} catch (InterruptedException e) {
						logger.log(Level.SEVERE, e.fillInStackTrace().toString());
						e.printStackTrace();
					}
					getMyPlatformController().ClearAccidentFiled(vehicle);
				} else {
					return;
				}
			} else if (getMyPlatformController().isBlocked()) {
				return;
			}
		}

		if (vehicle.isInteresectionPrivilege() && vehicle.getCurrentPosition().isNextByWantedVehicle()) {
			try {
				Vehicle wantedVehicle = vehicle.getCurrentPosition().getWantedVehicle();
				wantedVehicle.setIsWantedVehicle(true);
				wantedVehicle.setIsLookingForParking(false);
				wantedVehicle.setMoving();

				new Thread(new Runnable() {
					@Override
					public void run() {
						UserController.getInstance().noteInvestigation(wantedVehicle, true);
					}
				}).start();

				vehicle.setIsLookingForParking(false);
				vehicle.setInteresectionPrivilege(false);

				Random rnd = new Random();
				long time = rnd.nextInt(3) + 3;
				sleep(time * 1000);

				wantedVehicle.getVehicleController().setWhereICan();
				wantedVehicle.getVehicleController().start();

				GarageFieldReference wantedVehicleNextFiled = wantedVehicle.getNextField();
				if (wantedVehicleNextFiled.getClass() != vehicle.getCurrentPosition().getClass()) {
					getMyPlatformController().removeVehicleFromPlatform(vehicle);
					vehicle.getCurrentPosition().setGarageField(new EmptyFiled());
					TextAreasPlatformsController.getInstance().setTextAreField(vehicle.getCurrentPosition());
					wantedVehicleNextFiled.setOldGarageFiled(new VehicleField(vehicle));

					synchronized (wantedVehicle) {
						wantedVehicle.wait();
					}
				}

				getMyPlatformController().incrementFreeParkingPlaceCounter();
//				UserController.getInstance().incrementThreadCounter();

				return;
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, e.fillInStackTrace().toString());
				e.printStackTrace();
			}
		}
		synchronized (moveLocker) {
			PlatformController controller = getMyPlatformController();
			if (!Vehicle.isActive(vehicle)) {
				return;
			}

			if (controller.isBlocked()) {
				return;
			}

			if (controller.existsVehicleWithRotation()) {
				return;
			}

			if (isLeaveGarage()) {
				leaveGarage();
				return;
			}

			int row = vehicle.getCurrentPosition().getRow();
			int col = vehicle.getCurrentPosition().getCol();
			boolean moveToHigherPlatform = row == 1 && col == 7;
			boolean moveToLowerlatform = row == 0 && col == 0;

			if (!isFreeResurceWithoutPriority()) {
				if (!moveToHigherPlatform && !moveToLowerlatform) {
					GarageFieldReference otherFiled = getNextFiledForAccident();
					if (otherFiled != null) {
						counter++;
//						System.out.println("counter:" + counter);
						if (counter >= 10) {
							counter = 0;
							getMyPlatformController().makeAccidnet(vehicle.getCurrentPosition(), otherFiled);
						}
					}
				}
				vehicle.cleanWhereICan();
				return;

			}
			if (isPlatformTransitionMove()) {
				makePlatformTransition();
			}

			if (row == 1 && col == 0 && vehicle instanceof Police && getMyPlatformController().existWantedVehicle()) {
//				System.out.println("Imam te");
				vehicle.setInteresectionPrivilege(true);
			} else if (row == 1 && col == 0 && vehicle instanceof Police
					&& !getMyPlatformController().existWantedVehicle()) {
				vehicle.setInteresectionPrivilege(false);
			}

			GarageFieldReference currentPosition = vehicle.getCurrentPosition();
			GarageFieldReference next = vehicle.getNextField();
			VehicleField vehicleField = new VehicleField(vehicle);
			UserController userController = UserController.getInstance();

			if (currentPosition.getRow() == 1 && currentPosition.getCol() == 1) {
				getMyPlatformController().canILockParkingFiled();
			}

			if (currentPosition instanceof ParkingFieldReference) {
				getMyPlatformController().incrementFreeParkingPlaceCounter();
				vehicle.setIsLookingForParking(false);
			} else if (next instanceof ParkingFieldReference) {
				vehicle.getVehicleController().getMyPlatformController().decrementFreeParkingPlaceCounter();
				if (UserController.getInstance().isGarageFull()) {
					UserController.getInstance().disableVehicleAdd();
				}
				vehicle.setNotMoving();
				synchronized (UserController.counterLocker) {
					userController.decrementThreadCounter();
				}

			}

			if (currentPosition.getOldFiled() != null) {
				currentPosition.setGarageField(currentPosition.getOldFiled());
				((VehicleField) currentPosition.getOldFiled()).getVehicle().setCurrentPosition(currentPosition);
				currentPosition.clearOldGarageFiled();
			} else {
				currentPosition.setFree();
				currentPosition.setGarageField(new EmptyFiled());
			}

			next.setOccupied();
			next.setGarageField(vehicleField);
			vehicle.setCurrentPosition(next);

			TextAreasPlatformsController.getInstance().setTextAreField(currentPosition);
			TextAreasPlatformsController.getInstance().setTextAreField(next);

			if (next instanceof ParkingFieldReference && userController.getThreadCounter() == 0) {
				userController.finishingSimulation();
			}

			if (currentPosition.getPlatformNumber() == 1 && currentPosition.getRow() == 1
					&& currentPosition.getCol() == 0) {
				AddVehicleHandler.enableAdding();
			}

			if (next.getPlatformNumber() == 1 && currentPosition.getRow() == 0 && currentPosition.getCol() == 1
					&& userController.getThreadCounter() == 1) {
				AddVehicleHandler.disableAdding();
			}

			if (vehicle.isWantedVehicle() && !(currentPosition instanceof ParkingFieldReference)) {
				synchronized (vehicle) {
					vehicle.notify();
				}
			}

			vehicle.cleanWhereICan();
		}
	}

	private void moveWithPriority() {
		synchronized (moveLocker) {
			if (isLeaveGarage()) {
				leaveGarage();
				return;
			}
			if (!isFreeResurceWithPriority()) {
				vehicle.cleanWhereICan();
				return;
			}
			if (isPlatformTransitionMove()) {
				makePlatformTransition();
			}

			GarageFieldReference currentPosition = vehicle.getCurrentPosition();
			GarageFieldReference next = vehicle.getNextField();
			VehicleField vehicleField = new VehicleField(vehicle);
			UserController userController = UserController.getInstance();

			if (currentPosition instanceof ParkingFieldReference) {
				getMyPlatformController().incrementFreeParkingPlaceCounter();
				vehicle.setIsLookingForParking(false);
			} else if (next.getGarageField() instanceof VehicleField) {
				next.setOldGarageFiled(next.getGarageField());
			}

			if (currentPosition.getOldFiled() != null) {
				currentPosition.setGarageField(currentPosition.getOldFiled());
				((VehicleField) currentPosition.getOldFiled()).getVehicle().setCurrentPosition(currentPosition);
				currentPosition.clearOldGarageFiled();
			} else {
				currentPosition.setFree();
				currentPosition.setGarageField(new EmptyFiled());
			}
			next.setOccupied();
			next.setGarageField(vehicleField);
			vehicle.setCurrentPosition(next);

			TextAreasPlatformsController.getInstance().setTextAreField(currentPosition);
			TextAreasPlatformsController.getInstance().setTextAreField(next);

			if (next instanceof ParkingFieldReference && userController.getThreadCounter() == 0) {
				userController.finishingSimulation();
			}
			GarageFieldReference entryFiled = UserController.getInstance().getPlatformMatrix(0).getElemnt(1, 0);
			if (currentPosition == entryFiled) {
				if (isAbulanceRotation()) {
					getMyPlatformController().addFireFighter();
				} else if (isFireFighterRotation()) {
					getMyPlatformController().addPolice();
				} else if (isPoliceRotation()) {
					AddVehicleHandler.enableAdding();
				}
			}

			if (next.getPlatformNumber() == 1 && currentPosition.getRow() == 0 && currentPosition.getCol() == 1
					&& userController.getThreadCounter() == 1) {
				AddVehicleHandler.disableAdding();
			}

			if (vehicle.isWantedVehicle() && !(currentPosition instanceof ParkingFieldReference)) {
				synchronized (vehicle) {
					vehicle.notify();
				}
			}

			vehicle.cleanWhereICan();
		}
	}

	private boolean isFreeResurceWithoutPriority() {
		vehicle.getVehicleController().setWhereICan();
		if (vehicle.getWhereICan().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isFreeResurceWithPriority() {
		vehicle.getVehicleController().setWhereICanWithPriority();
		if (vehicle.getWhereICan().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public void setWhereICan() {
		vehicle.cleanWhereICan();

		if (vehicle.getCurrentPosition().getGarageField() instanceof EmptyFiled) {
			return;
		}

		VehicleField vehicleFiled = (VehicleField) vehicle.getCurrentPosition().getGarageField();
		Vehicle vehicle = vehicleFiled.getVehicle();
		boolean blocked = vehicle.getVehicleController().getMyPlatformController().isBlockedParking();

		for (GarageFieldReference reference : vehicle.getCurrentPosition().getNextFilds()) {
			if (reference.getGarageField() instanceof EmptyFiled) {
				// Sa parkinga uvije moze samo na to jedno
				if (vehicle.getCurrentPosition() instanceof ParkingFieldReference) {
					vehicle.addAllWhereICan(vehicle.getCurrentPosition().getNextFilds());
				}

				else if (PlatformController.isOnTheInputEnd(vehicle)) {
					vehicle.addWhereICan(reference);
					vehicle.SetNotLookingForParking();
				}

				// Ako trazim parking
				else if (vehicle.isLookingForParking()) {
					if (!vehicle.isInteresectionPrivilege() && blocked
							&& vehicle.getCurrentPosition() instanceof IntersectionReferenceField) {
						IntersectionReferenceField intersection = (IntersectionReferenceField) vehicle
								.getCurrentPosition();
						if (intersection.getType() == Intersection.IN) {
							if (reference instanceof IntersectionReferenceField)
								vehicle.addWhereICan(reference);
						} else if (reference instanceof InputFieldReference) {
							vehicle.addWhereICan(reference);
						}
					} else if (!vehicle.isInteresectionPrivilege() && reference instanceof ParkingFieldReference) {
						vehicle.addWhereICan(reference);
					} else if (!vehicle.isInteresectionPrivilege() && reference.isNextByFreeParkingPlace()) {
						vehicle.addWhereICan(reference);
					} else if (this.vehicle.getCurrentPosition() instanceof OutputFieldReference) {
						if (reference instanceof OutputFieldReference) {
							vehicle.addWhereICan(reference);
						} else if (reference instanceof IntersectionReferenceField && !blocked) {
							vehicle.addWhereICan(reference);
						}
					} else if (this.vehicle.getCurrentPosition() instanceof IntersectionReferenceField) {
						if (reference instanceof InputFieldReference) {
							vehicle.addWhereICan(reference);
						} else if (reference instanceof OutputFieldReference) {
							vehicle.addWhereICan(reference);
						}
					}

					else if (reference instanceof InputFieldReference) {
						vehicle.addWhereICan(reference);
					} else if (reference instanceof IntersectionReferenceField) {
						vehicle.addWhereICan(reference);
					}
				}

				// Napustam parking
				else {
					if (this.vehicle.getCurrentPosition() instanceof IntersectionReferenceField) {
						if (reference instanceof OutputFieldReference) {
							vehicle.addWhereICan(reference);
						}
					}

					else if (this.vehicle.getCurrentPosition() instanceof OutputFieldReference) {
						if (reference instanceof OutputFieldReference) {
							vehicle.addWhereICan(reference);
						} else if (reference instanceof IntersectionReferenceField) {
							IntersectionReferenceField ifr = (IntersectionReferenceField) reference;
							if (ifr.getType() != Intersection.IN) {
								vehicle.addWhereICan(reference);
							}
						}
					} else if (this.vehicle.getCurrentPosition() instanceof InputFieldReference) {
						if (reference instanceof InputFieldReference) {
							vehicle.addWhereICan(reference);
						} else if (reference instanceof IntersectionReferenceField) {
							vehicle.addWhereICan(reference);
						}
					}

					else if (reference instanceof IntersectionReferenceField) {
						vehicle.addWhereICan(reference);
					}
				}

			}
		}
	}

	public void setWhereICanWithPriority() {
		vehicle.cleanWhereICan();
		if (vehicle.getCurrentPosition().getGarageField() instanceof EmptyFiled) {
			return;
		}

		VehicleField vehicleFiled = (VehicleField) vehicle.getCurrentPosition().getGarageField();
		Vehicle vehicle = vehicleFiled.getVehicle();
		GarageFieldReference currentPosition = vehicle.getCurrentPosition();

		for (GarageFieldReference reference : currentPosition.getNextFilds()) {
			if ((reference.isAccidentPlace() || currentPosition.existsExtraAccidentFiled())
					&& vehicle.isGoingToAccident()) {
				if (!getMyPlatformController().isInvestigationTime()) {
					getMyPlatformController().setInvestigationTime(true);
				}
				vehicle.cleanWhereICan();
				return;
			} else if (reference.isAccidentPlace()) {
				return;
			}
			if (reference instanceof ParkingFieldReference) {
				continue;
			}
			if (reference.getGarageField() instanceof VehicleField) {
				Vehicle otherVehicle = ((VehicleField) reference.getGarageField()).getVehicle();
				if (otherVehicle.getVehicleController().isRotationOn()) {
					continue;
				}
				if (otherVehicle.canMove()) {
					continue;
				}
			}
			if (vehicle.getCurrentPosition() instanceof ParkingFieldReference) {
				vehicle.addAllWhereICan(vehicle.getCurrentPosition().getNextFilds());
			}

			else if (PlatformController.isOnTheInputEnd(vehicle)) {
				vehicle.addWhereICan(reference);
				vehicle.SetNotLookingForParking();
			}

			// Ako trazim parking
			else if (vehicle.isLookingForParking()) {
				if (this.vehicle.getCurrentPosition() instanceof OutputFieldReference) {
					if (reference instanceof OutputFieldReference) {
						vehicle.addWhereICan(reference);
					} else if (reference instanceof IntersectionReferenceField) {
						vehicle.addWhereICan(reference);
					}
				} else if (this.vehicle.getCurrentPosition() instanceof IntersectionReferenceField) {
					IntersectionReferenceField intersection = (IntersectionReferenceField) vehicle.getCurrentPosition();
					if (intersection.getType() == Intersection.IN) {
						if (!getMyPlatformController().getAccidentOnThisPlatform()
								|| (getMyPlatformController().getAccidentOnThisPlatform()
										&& getMyPlatformController().getAccidentSector() == 1)) {
							if (reference instanceof IntersectionReferenceField) {
								vehicle.addWhereICan(reference);
							} else {
								continue;
							}
						} else {
							if (reference instanceof InputFieldReference) {
								vehicle.addWhereICan(reference);
							}
						}
					} else {
						if (reference instanceof InputFieldReference) {
							vehicle.addWhereICan(reference);
						}
					}
				}

				else if (reference instanceof InputFieldReference) {
					vehicle.addWhereICan(reference);
				} else if (reference instanceof IntersectionReferenceField) {
					vehicle.addWhereICan(reference);
				}
			}

			// Napustam parking
			else {
				if (this.vehicle.getCurrentPosition() instanceof IntersectionReferenceField) {
					if (reference instanceof OutputFieldReference) {
						vehicle.addWhereICan(reference);
					}
				}

				else if (this.vehicle.getCurrentPosition() instanceof OutputFieldReference) {
					if (reference instanceof OutputFieldReference) {
						vehicle.addWhereICan(reference);
					} else if (reference instanceof IntersectionReferenceField) {
						IntersectionReferenceField ifr = (IntersectionReferenceField) reference;
						if (ifr.getType() != Intersection.IN) {
							vehicle.addWhereICan(reference);
						}
					}
				} else if (this.vehicle.getCurrentPosition() instanceof InputFieldReference) {
					if (reference instanceof InputFieldReference) {
						vehicle.addWhereICan(reference);
					} else if (reference instanceof IntersectionReferenceField) {
						vehicle.addWhereICan(reference);
					}
				}

				else if (reference instanceof IntersectionReferenceField) {
					vehicle.addWhereICan(reference);
				}
			}
		}
	}

	private GarageFieldReference getNextFiledForAccident() {
		int col = vehicle.getCurrentPosition().getCol();
		if (PlatformController.existAccident) {
			return null;
		}
		if (col < 3) {
			return null;
		}
		if (vehicle.getVehicleController().isRotationOn()) {
			return null;
		}
		if (vehicle.getHadAccident()) {
			return null;
		}
		GarageFieldReference entryFiled = UserController.getInstance().getPlatformMatrix(0).getElemnt(1, 0);
		if (entryFiled.getGarageField() instanceof VehicleField) {
			Vehicle vehicle = ((VehicleField) entryFiled.getGarageField()).getVehicle();
			if (vehicle.getVehicleController().isRotationOn()) {
				return null;
			}
		}
		vehicle.cleanWhereICan();

		VehicleField vehicleFiled = (VehicleField) vehicle.getCurrentPosition().getGarageField();
		Vehicle vehicle = vehicleFiled.getVehicle();

		for (GarageFieldReference reference : vehicle.getCurrentPosition().getNextFilds()) {
			if (reference instanceof ParkingFieldReference) {
				continue;
			} else if (reference.getGarageField() instanceof EmptyFiled) {
				continue;
			} else if (reference.getGarageField() instanceof VehicleField) {
				Vehicle otherVehicle = ((VehicleField) reference.getGarageField()).getVehicle();
				if (otherVehicle instanceof Rotation) {
					continue;
				}
				if (otherVehicle.getHadAccident()) {
					continue;
				} else {
					vehicle.addWhereICan(reference);
				}
			}

		}

		if (vehicle.getNextField() == null
				|| vehicle.getCurrentPosition().getPlatformNumber() != vehicle.getNextField().getPlatformNumber()) {
			return null;
		}
		return vehicle.getNextField();
	}

	private boolean isLeaveGarage() {
		if (vehicle.getCurrentPosition().getPlatformNumber() == 1 && vehicle.getCurrentPosition().getCol() == 0
				&& vehicle.getCurrentPosition().getRow() == 0) {
			return true;
		} else {
			return false;
		}
	}

	private void leaveGarage() {
		UserController userController = UserController.getInstance();
		GarageFieldReference currentPosition = vehicle.getCurrentPosition();

		if (isRotationOn() && currentPosition.getOldFiled() != null) {
			currentPosition.setGarageField(currentPosition.getOldFiled());
			((VehicleField) currentPosition.getOldFiled()).getVehicle().setCurrentPosition(currentPosition);
			currentPosition.clearOldGarageFiled();
		} else {
			vehicle.getCurrentPosition().setGarageField(new EmptyFiled());
			vehicle.getCurrentPosition().setFree();
		}

		PlatformController currentPlatformController = userController.getPlatformMatrix(0).getController();
		currentPlatformController.removeVehicleFromGarage(vehicle);
		TextAreasPlatformsController.getInstance().setTextAreField(vehicle.getCurrentPosition());
		if (!(vehicle instanceof Rotation) && !vehicle.isLookingForParking()) {
			userController.payment(vehicle);
		}
		if (isRotationOn()) {
			getMyPlatformController().removeRotationVehicle(vehicle);
		}

		synchronized (UserController.counterLocker) {
			userController.decrementThreadCounter();
			vehicle = null;
//			System.out.println(userController.getThreadCounter());
			if (userController.getThreadCounter() == 0) {
				userController.finishingSimulation();
			}
		}

	}

	private boolean isPlatformTransitionMove() {
		GarageFieldReference currentPosition = vehicle.getCurrentPosition();

		if (currentPosition.getRow() == 0 && currentPosition.getCol() == 0
				|| currentPosition.getRow() == 1 && currentPosition.getCol() == 7) {
			return true;
		} else {
			return false;
		}
	}

	private void makePlatformTransition() {
		GarageFieldReference currentPosition = vehicle.getCurrentPosition();

		if (currentPosition.getRow() == 0 && currentPosition.getCol() == 0) {
			makeTransitionToLowPlatform(vehicle);
		} else if (currentPosition.getRow() == 1 && currentPosition.getCol() == 7) {
			makeTransitionToHigherPlatform(vehicle);
		}

	}

	private void makeTransitionToLowPlatform(Vehicle vehicle) {
		UserController userController = UserController.getInstance();
		int currentPlatformNumber = vehicle.getCurrentPosition().getPlatformNumber();
		if (currentPlatformNumber > 1) {
			PlatformController currentPlatformController = userController.getPlatformMatrix(currentPlatformNumber - 1)
					.getController();
			currentPlatformController.removeVehicleFromPlatform(vehicle);
			PlatformController lowPlatformControler = userController.getPlatformMatrix(currentPlatformNumber - 2)
					.getController();
			lowPlatformControler.addVehicleToPlatform(vehicle);
		}
	}

	private void makeTransitionToHigherPlatform(Vehicle vehicle) {
		UserController userController = UserController.getInstance();
		int currentPlatformNumber = vehicle.getCurrentPosition().getPlatformNumber();
		if (currentPlatformNumber < userController.getPlatformsNumber()) {
			PlatformController currentPlatformController = userController.getPlatformMatrix(currentPlatformNumber - 1)
					.getController();
			currentPlatformController.removeVehicleFromPlatform(vehicle);
			PlatformController lowPlatformControler = userController.getPlatformMatrix(currentPlatformNumber)
					.getController();
			lowPlatformControler.addVehicleToPlatform(vehicle);
		}
	}

	public synchronized boolean doIHaveWaitOnIntersection() {
		UserController userController = UserController.getInstance();
		int platformNumber = vehicle.getCurrentPosition().getPlatformNumber() - 1;
		int row = vehicle.getCurrentPosition().getRow();
		int col = vehicle.getCurrentPosition().getCol();
		boolean stop = false;

		if (PlatformController.isOnTheOutputEnd(vehicle)) {
			stop = false;
		}

		else if (row == 1 && col == 0) {
			GarageField garageField = userController.getPlatformMatrix(platformNumber).getGarageFieldReference(2, 2)
					.getGarageField();
			boolean vehicleOnRightSide = garageField instanceof VehicleField;
			if (vehicleOnRightSide) {
				Vehicle vehicle = ((VehicleField) garageField).getVehicle();
				if (vehicle.isLookingForParking()) {
					stop = true;
				}
			}
		}

		else if ((row == 1 || row == 2) && col == 2) {
			GarageField garageField = userController.getPlatformMatrix(platformNumber).getGarageFieldReference(0, 3)
					.getGarageField();
			boolean vehicleOnRightSide = garageField instanceof VehicleField;
			if (vehicleOnRightSide) {
				if (!this.vehicle.isLookingForParking()) {
					stop = true;
				}
			}
		}

		if (row == 1 && col == 4) {
			GarageField garageFieldOut = userController.getPlatformMatrix(platformNumber).getGarageFieldReference(6, 2)
					.getGarageField();
			GarageField garageFieldIn = userController.getPlatformMatrix(platformNumber).getGarageFieldReference(5, 2)
					.getGarageField();
			boolean vehicleOnRightSide = garageFieldOut instanceof VehicleField;
			boolean canMoveRight = garageFieldIn instanceof EmptyFiled;
			if (vehicleOnRightSide && !canMoveRight) {
				stop = true;
			}
		}

		else if (row == 2 && col == 6) {
			GarageField garageField = userController.getPlatformMatrix(platformNumber).getGarageFieldReference(0, 7)
					.getGarageField();
			boolean vehicleOnRightSide = garageField instanceof VehicleField;
			if (vehicleOnRightSide) {
				stop = true;
			}
		}

		return stop;
	}

	public boolean isAbulanceRotation() {
		if (vehicle instanceof AmbulanceCar) {
			AmbulanceCar car = (AmbulanceCar) vehicle;
			return car.isRotationTurnOn();
		} else if (vehicle instanceof AmbulanceVan) {
			AmbulanceVan moto = (AmbulanceVan) vehicle;
			return moto.isRotationTurnOn();
		}
		return false;
	}

	public boolean isFireFighterRotation() {
		if (vehicle instanceof FirefighterVan) {
			FirefighterVan van = (FirefighterVan) vehicle;
			return van.isRotationTurnOn();
		}
		return false;
	}

	public boolean isPoliceRotation() {
		if (vehicle instanceof PoliceCar) {
			PoliceCar car = (PoliceCar) vehicle;
			return car.isRotationTurnOn();
		} else if (vehicle instanceof PoliceMotorcycle) {
			PoliceMotorcycle moto = (PoliceMotorcycle) vehicle;
			return moto.isRotationTurnOn();
		} else if (vehicle instanceof PoliceVan) {
			PoliceVan van = (PoliceVan) vehicle;
			return van.isRotationTurnOn();
		}
		return false;
	}

	public boolean isRotationOn() {
		if (vehicle instanceof CarWithRotation) {
			CarWithRotation car = (CarWithRotation) vehicle;
			return car.isRotationTurnOn();
		} else if (vehicle instanceof MotorcycleWithRotation) {
			MotorcycleWithRotation moto = (MotorcycleWithRotation) vehicle;
			return moto.isRotationTurnOn();
		} else if (vehicle instanceof VanWithRotation) {
			VanWithRotation van = (VanWithRotation) vehicle;
			return van.isRotationTurnOn();
		}
		return false;
	}

	public PlatformController getMyPlatformController() {
		Vehicle vehicle = this.vehicle;
		PlatformController controller = UserController.getInstance()
				.getPlatformMatrix(vehicle.getCurrentPosition().getPlatformNumber() - 1).getController();
		return controller;
	}

}
