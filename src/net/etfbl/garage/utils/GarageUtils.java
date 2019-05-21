package net.etfbl.garage.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GarageUtils {
	private static GarageUtils garageUtils;
	FileHandler fileHandler = null;
	Logger logger = null;
	
	private GarageUtils() {
		logger = Logger.getLogger("Garaža");
		try {
			if(fileHandler == null) {
				fileHandler = new FileHandler("error.log", true);
			}
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static GarageUtils getInstance() {
		if(garageUtils == null) {
			garageUtils = new GarageUtils();
		}
		return garageUtils;
	}
	
	public String URLToFileName(String url) {
		return url.substring(url.lastIndexOf('\\') + 1, url.lastIndexOf('.'));
	}
	
	public Logger getLogger() {
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
		return logger;
	}
	
	public FileHandler getFileHandler() {
		return fileHandler;
	}
	
	public boolean isParkingField(int x, int y) {
		if(y > 1 && (x == 0 || x == 7)) {
			return true;
		}
		if((x == 3 || x == 4) && (y > 1 && y < 8)) {
			return true;
		}
		
		return false;
	}
	
	public boolean isCorrectRegistrationNumber(String registrationNumber) {
		if(registrationNumber.matches("[a-zA-Z]\\d\\d-[a-zA-Z]-\\d\\d\\d")) {
			return true;
		}
		return false;
	}
	
	public String getRandomRegistrationNumber() {
		String registrationNumber = "";
		Random rnd = new Random();
		do {
			registrationNumber = new StringBuilder().append((char)((rnd.nextInt('Z' - 'A') + 'A')))
				.append((char)(rnd.nextInt('9' - '0')  + '0'))
				.append((char)(rnd.nextInt('9' - '0')  + '0'))
				.append('-')
				.append((char)((rnd.nextInt('Z' - 'A') + 'A')))
				.append('-')
				.append((char)(rnd.nextInt('9' - '0')  + '0'))
				.append((char)(rnd.nextInt('9' - '0')  + '0'))
				.append((char)(rnd.nextInt('9' - '0')  + '0'))
				.toString();
		}while(registrationNumber.matches(".*W.*|.*X.*|.*Y.*|.*Q.*"));
		return registrationNumber;
	}
	
	public String getRelativePathFromFolder(String folderName) {
		ArrayList<String> list = new ArrayList<>();
		File folder = new File(folderName);
		for (final File fileEntry : folder.listFiles()) {
           list.add("file:" + fileEntry.toString());
        }
		return list.get(new Random().nextInt(list.size()));
	}
	
	public int getRandomcapacity() {
		return (new Random().nextInt(100) + 400) * 10;
	}
	
	public boolean isSameInstanceOf(Object first, Object second) {
		return first.getClass() == second.getClass();
	}
}
