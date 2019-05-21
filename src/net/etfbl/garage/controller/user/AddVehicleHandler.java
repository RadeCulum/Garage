package net.etfbl.garage.controller.user;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import net.etfbl.garage.model.field.GarageFieldReference;
import net.etfbl.garage.model.field.VehicleField;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.utils.GarageLockers;
import net.etfbl.garage.utils.GarageProperties;
import net.etfbl.garage.utils.GarageUtils;

public class AddVehicleHandler implements EventHandler<ActionEvent> {
	static AddVehicleHandler handler = null;
	Logger logger;

	static Semaphore semaphore;

	private AddVehicleHandler() {
		logger = GarageUtils.getInstance().getLogger();
	}

	public static AddVehicleHandler getInstance() {
		if (handler == null) {
			handler = new AddVehicleHandler();
		}
		return handler;
	}

	@Override
	public void handle(ActionEvent arg) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				synchronized (GarageLockers.addLocker) {
					Thread addThreadDisable = new Thread(new Runnable() {
						public void run() {
							UserController.getInstance().disableVehicleAdd();
						}
					});
					Platform.runLater(addThreadDisable);
					try {
						addThreadDisable.join();
					} catch (InterruptedException e) {
						logger.log(Level.SEVERE, e.fillInStackTrace().toString());
						e.printStackTrace();
					}
					Vehicle vehicle = PlatformController.getNewVehicleType(PlatformController.makeNewVehicle());
					GarageFieldReference firstField = UserController.getInstance().getPlatformMatrix(0).getElemnt(1, 0);
					if (firstField.getGarageField() instanceof VehicleField) {
						return;
					}
					vehicle.setCurrentPosition(firstField);
					firstField.setGarageField(new VehicleField(vehicle));
					TextAreasPlatformsController.getInstance().setTextAreField(firstField);
					try {
						Thread.sleep(GarageProperties.getInstance().getSpeed());
					} catch (InterruptedException e) {
						logger.log(Level.SEVERE, e.fillInStackTrace().toString());
						e.printStackTrace();
					}
					UserController.getInstance().getPlatformMatrix(0).getController().addVehicleToGarage(vehicle);
					vehicle.setMoving();
					vehicle.getVehicleController().start();
					UserController.getInstance().incrementThreadCounter();
				}
			}
		}).start();

	}

	public static void enableAdding() {
		Logger logger = GarageUtils.getInstance().getLogger();
		Thread addThreadEnable = new Thread(new Runnable() {
			public void run() {
				UserController.getInstance().enableVehicleAdd();
			}
		});
		Platform.runLater(addThreadEnable);
		try {
			addThreadEnable.join();
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}

	public static void disableAdding() {
		Logger logger = GarageUtils.getInstance().getLogger();
		Thread addThreadEnable = new Thread(new Runnable() {
			public void run() {
				UserController.getInstance().disableVehicleAdd();
			}
		});
		Platform.runLater(addThreadEnable);
		try {
			addThreadEnable.join();
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}

}
