package net.etfbl.garage.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.etfbl.garage.model.vehicle.Car;
import net.etfbl.garage.model.vehicle.Motorcycle;
import net.etfbl.garage.model.vehicle.Van;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.model.vehicle.VehicleMapWrapper;
import net.etfbl.garage.model.vehicle.interfaces.Rotation;
import net.etfbl.garage.utils.GarageProperties;
import net.etfbl.garage.utils.GarageUtils;

public class VehicleWriter {

	public void write(List<List<Vehicle>> allVehicle) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		Logger logger = GarageUtils.getInstance().getLogger();
		try {
			fos = new FileOutputStream(GarageProperties.getInstance().getSerFilePath());
			oos = new ObjectOutputStream(fos);

			for (List<Vehicle> list : allVehicle) {
				Map<String, Vehicle> vehicleMap = new HashMap<>();
				for (Vehicle vehicle : list) {
					vehicleMap.put(vehicle.getRegistrationNumber(), vehicle);
				}
				oos.writeObject(new VehicleMapWrapper(vehicleMap));
			}
			oos.writeObject(null);
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (oos != null) {
					oos.close();
				}
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.fillInStackTrace().toString());
				e.printStackTrace();
			}

		}
	}

	public static void prpareVehicleForWrite(ArrayList<Vehicle> vehicleList) {
		ListIterator<Vehicle> iterator = vehicleList.listIterator();
		while (iterator.hasNext()) {
			Vehicle vehicle = iterator.next();
			if (vehicle instanceof Rotation) {
				if (vehicle instanceof Car) {
					Car car = new Car(vehicle);
					iterator.set(car);
				} else if (vehicle instanceof Motorcycle) {
					Motorcycle motorcycle = new Motorcycle(vehicle);
					iterator.set(motorcycle);
				} else if (vehicle instanceof Van) {
					Van van = new Van(vehicle);
					iterator.set(van);
				}
			}
		}
	}

}
