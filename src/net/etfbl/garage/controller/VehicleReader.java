package net.etfbl.garage.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.model.vehicle.VehicleMapWrapper;
import net.etfbl.garage.utils.GarageProperties;
import net.etfbl.garage.utils.GarageUtils;

public class VehicleReader {
	/*
	 * Sva vozila iz garaza.ser Redni broj liste odgovara rednom broju platforme na
	 * kojoj se vozila nalaze
	 */
	ArrayList<ArrayList<Vehicle>> allVehicle = null;

	public ArrayList<ArrayList<Vehicle>> getAllVehicle() {
		allVehicle = new ArrayList<>();
		Logger logger = GarageUtils.getInstance().getLogger();
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		/*
		 * Objekat moje wrapper-ske klase koji je serializable
		 */
		VehicleMapWrapper vehicleMap;
		Object object;
		
		File file = new File(GarageProperties.getInstance().getSerFilePath());
		if(!file.exists()) {
			return allVehicle;
		}

		try {
			fis = new FileInputStream(GarageProperties.getInstance().getSerFilePath());
			ois = new ObjectInputStream(fis);
			while ((object = ois.readObject()) != null) {
				/*
				 * U garaza.ser su serijalizovani objekti VehicleMapWrapper
				 */
				vehicleMap = (VehicleMapWrapper) object;
				allVehicle.add(vehicleMap.toArrayList());
			}
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (ois != null) {
					ois.close();
				}
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.fillInStackTrace().toString());
				e.printStackTrace();
			}

		}
		return allVehicle;
	}

	public ArrayList<Vehicle> GetElementFromPlatform(int index) {
		return this.getAllVehicle().get(index);
	}

}
