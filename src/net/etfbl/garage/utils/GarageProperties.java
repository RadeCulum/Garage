package net.etfbl.garage.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GarageProperties {
	private static GarageProperties garageProperties = null;
	private Properties prop;
	private InputStream input = null;
	
	Logger logger = GarageUtils.getInstance().getLogger();

	private GarageProperties() {
		prop = new Properties();
		try {
			String filename = "config.properties";
			input = new FileInputStream(filename);
			prop.load(input);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.fillInStackTrace().toString());
					e.printStackTrace();
				}
			}
		}
	}

	public static GarageProperties getInstance() {
		if (garageProperties == null) {
			garageProperties = new GarageProperties();
		}
		return garageProperties;
	}

	public String getPrimaryStageTitle() {
		return prop.getProperty("primaryStageTitle");
	}

	public String getAppIconPath() {
		return prop.getProperty("appIconPath");
	}

	public String getSerFilePath() {
		return prop.getProperty("serFilePath");
	}

	public String getPlayIconPath() {
		return prop.getProperty("playIconPath");
	}

	public String getAddIconPath() {
		return prop.getProperty("addIconPath");
	}

	public String getVehicleFolderName() {
		return prop.getProperty("vehicleFolderName");
	}

	public String getCarFolderName() {
		return prop.getProperty("carFolderName");
	}

	public String getMotorcycleFolderName() {
		return prop.getProperty("motorcycleFolderName");
	}

	public String getVanFolderName() {
		return prop.getProperty("vanFolderName");
	}

	public String getFileSeparator() {
		return prop.getProperty("fileSeparator");
	}

	public String getChoosePictureTitle() {
		return prop.getProperty("choosePictureTitle");
	}

	public String getAddFormTitle() {
		return prop.getProperty("addFormTitle");
	}

	public int getTableFont() {
		return Integer.parseInt(prop.getProperty("tableFont"));
	}

	public int getPlatformsNumber() {
		return Integer.parseInt(prop.getProperty("platformsNumber"));
	}

	public String getTableStyle() {
		return prop.getProperty("tableStyle");
	}

	public String getErrorLogFile() {
		return prop.getProperty("errorLogFile");
	}
	
	public String getPeymentFile() {
		return prop.getProperty("peymentFile");
	}
	
	public String getAccidentFile() {
		return prop.getProperty("accidentFile");
	}
	
	public String getWantedVehicleFile() {
		return prop.getProperty("wantedVehicleFile");
	}

	public int getSpeed() {
		return Integer.parseInt(prop.getProperty("speed"));
	}
}
