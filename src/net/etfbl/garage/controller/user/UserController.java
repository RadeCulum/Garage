package net.etfbl.garage.controller.user;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import net.etfbl.garage.controller.VehicleWriter;
import net.etfbl.garage.controller.admin.AdministratorController;
import net.etfbl.garage.model.field.PlatformMatrix;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.utils.GarageProperties;
import net.etfbl.garage.utils.GarageUtils;
import net.etfbl.garage.view.MainStage;
import net.etfbl.garage.view.user.UserPane;

public class UserController {
	private static UserController userController = null;
	private List<UserPane> userPaneList = null;
	private List<PlatformMatrix> platformMatrixList = null;

	private volatile static int activeThreadCounter = 0;
	public volatile static int allVehicleCounter = 0;
	public volatile static int basicVehicleCounter = 0;
	private int platformsNumber;

	public static Object counterLocker = new Object();

	private BufferedWriter peymentWriter;
	private BufferedOutputStream accidentWriter;

	Logger logger = GarageUtils.getInstance().getLogger();

	private UserController() {
		this.platformsNumber = AdministratorController.getInstance().getPlatformsNumber();
		this.userPaneList = new ArrayList<>();
		this.platformMatrixList = new ArrayList<>();
		this.addUserPanes();

		File file = new File(GarageProperties.getInstance().getPeymentFile());
		try {
			FileWriter writer = new FileWriter(file, true);
			peymentWriter = new BufferedWriter(writer);
			
			if(file.length() == 0) {
				peymentWriter.write("REGISTARSKI BROJ,CIJENA(KM)\n");
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}

		file = new File(GarageProperties.getInstance().getAccidentFile());
		try {
			FileOutputStream writer = new FileOutputStream(file);
			accidentWriter = new BufferedOutputStream(writer);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}

	public static UserController getInstance() {
		if (userController == null) {
			userController = new UserController();
			connectPlatforms();
		}
		return userController;
	}

	public void showForm() {
		setPatformNumberTF();
		setPlatformNumbersCB();
		this.getUserPaneOnIndex(0).getPlatformNumberCBox().setValue(1);
		MainStage.getInstance().setScene(userPaneList.get(0).getScene());

		PlaySimulationController playSimulation = new PlaySimulationController();
		for (UserPane userPane : userPaneList) {
			userPane.getPlayButton().setOnAction(playSimulation);
		}

	}

	public void writeVehicle() {
		List<List<Vehicle>> allVehicle = new ArrayList<>();
		for (int i = 0; i < platformsNumber; ++i) {
			List<Vehicle> list = platformMatrixList.get(i).getController().getAllVehicleFromPlatform();
			allVehicle.add(list);
		}
		new VehicleWriter().write(allVehicle);
	}

	private void addUserPanes() {
		for (int i = 0; i < platformsNumber; ++i) {
			this.addUserPane();
		}
		
		if(PlatformController.garageVehicleCounter == 0) {
			disablePlaySimulation(true);
		}
	}

	private void addUserPane() {
		UserPane userPane = new UserPane();
		PlatformController platfromController = new PlatformController(userPane);
		PlatformMatrix platformMatrix = platfromController.getPlatform();
		this.addPlatformMatrix(platformMatrix);
		userPaneList.add(userPane);
		platfromController.setPlatform();
	}


	public void setActivePane(int pos) {
		MainStage.getInstance().setScene(userPaneList.get(pos).getScene());
	}

	public int getIndexOfActivePane() {
		return getIndexOfScene(MainStage.getInstance().getScene());
	}

	public int getIndexOfScene(Scene userScene) {
		for (UserPane x : userPaneList) {
			if (x.getScene() == userScene) {
				return userPaneList.indexOf(x);
			}
		}
		return -1;
	}

	public UserPane getActivePane() {
		return userPaneList.get(this.getIndexOfActivePane());
	}
	
	public UserPane getUserPaneOnIndex(int index) {
		if (index < userPaneList.size()) {
			return userPaneList.get(index);
		}
		return null;
	}
	
	public List<UserPane> getAllUserPanes() {
		return this.userPaneList;
	}
	
	public int getPlatformsNumber() {
		return this.platformsNumber;
	}

	public void setPatformNumberTF() {
		for (UserPane UserPane : userPaneList) {
			UserPane.getPlatformNumberLabel().setText(String.valueOf(this.platformsNumber));
		}
	}

	public void setMinimumVehicleNumberTF(String value) {
		for (UserPane UserPane : userPaneList) {
			UserPane.getMinimumVehicleNumberTField().setText(value);
		}
	}

	public void updatePlatformNuberCB() {
		getActivePane().getPlatformNumberCBox().setValue(getIndexOfActivePane() + 1);
	}

	public void setPlatformNumbersCB() {
		for (int j = 0; j < userPaneList.size(); ++j) {
			for (int i = 0; i < this.platformsNumber; ++i) {
				userPaneList.get(j).getPlatformNumberCBox().getItems().add(i + 1);
			}
			userPaneList.get(j).getPlatformNumberCBox().setValue(j + 1);
		}
	}

	private void addPlatformMatrix(PlatformMatrix platformMatrix) {
		platformMatrixList.add(platformMatrix);
	}

	public PlatformMatrix getPlatformMatrix(int index) {
		if (index < platformMatrixList.size()) {
			return platformMatrixList.get(index);
		}
		return null;
	}

	public int getIndexOfPlatformMatrix(PlatformMatrix platformMatrix) {
		return platformMatrixList.indexOf(platformMatrix);
	}

	public PlatformController getPlatformContrllerOfUserPane(UserPane userPane) {
		int index = getIndexOfScene(userPane.getScene());
		return this.userPaneList.get(index).getController();
	}

	public PlatformController getControlerOfUserPaneOnIndex(int index) {
		return this.userPaneList.get(index).getController();
	}

	public static void connectPlatforms() {
		UserController userController = UserController.getInstance();
		if (userController.getPlatformsNumber() < 2) {
			return;
		}

		for (int i = 0; i < userController.getPlatformsNumber(); i++) {
			PlatformMatrix platformMatrix = userController.getPlatformMatrix(i);
			if (i == 0) {
				platformMatrix.setNextPlatform(userController.getPlatformMatrix(i + 1));
			} else if (i == userController.getPlatformsNumber() - 1) {
				platformMatrix.setPreviousPlatform(userController.getPlatformMatrix(i - 1));
			} else {
				platformMatrix.setNextPlatform(userController.getPlatformMatrix(i + 1));
				platformMatrix.setPreviousPlatform(userController.getPlatformMatrix(i - 1));
			}
		}

		for (int i = 0; i < userController.getPlatformsNumber(); i++) {
			PlatformMatrix matrix = userController.getPlatformMatrix(i);
			if (i == 0) {
				matrix.getElemnt(1, 7).addNextField(matrix.getNextPlatform().getElemnt(1, 0));
			} else if (i == userController.getPlatformsNumber() - 1) {
				matrix.getElemnt(0, 0).addNextField(matrix.getPrviousPlatform().getElemnt(0, 7));
				;
			} else {
				matrix.getElemnt(1, 7).addNextField(matrix.getNextPlatform().getElemnt(1, 0));
				matrix.getElemnt(0, 0).addNextField(matrix.getPrviousPlatform().getElemnt(0, 7));
			}
		}
	}

	public void disableComponentForSimulation(boolean disabled) {
		for (UserPane userPane : userPaneList) {
			userPane.getPlayButton().setDisable(disabled);
			userPane.getMinimumVehicleNumberTField().setDisable(disabled);
		}
	}
	
	public void disablePlaySimulation(boolean disabled) {
		for (UserPane userPane : userPaneList) {
			userPane.getPlayButton().setDisable(disabled);
		}
	}
	
	public boolean getPlaySimulationDisabled() {
		return userPaneList.get(0).getPlayButton().isDisabled();
	}

	public void enableComponentForSimulation() {
		for (UserPane userPane : userPaneList) {
			userPane.getAddButton().setDisable(false);
		}
	}

	public void incrementThreadCounter() {
		activeThreadCounter++;
	}

	public void decrementThreadCounter() {
		activeThreadCounter--;
	}

	public int getThreadCounter() {
		synchronized (counterLocker) {
			return activeThreadCounter;
		}
	}

	public void finishingSimulation() {
		userController.disableComponentAfterSimulation();
		userController.writeVehicle();
		try {
			peymentWriter.flush();
			peymentWriter.close();

			accidentWriter.flush();
			accidentWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileHandler handler = GarageUtils.getInstance().getFileHandler();
		if (handler != null) {
			handler.close();
		}

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Obevjestenje");
				alert.setHeaderText(null);
				alert.setContentText("Simulacija je uspjesno zavrsena.");
				alert.show();

			}
		});

	}

	private void disableComponentAfterSimulation() {
		for (UserPane userPane : userPaneList) {
			userPane.getAddButton().setDisable(true);
		}
	}

	public void disableVehicleAdd() {
		for (UserPane userPane : userPaneList) {
			userPane.getAddButton().setDisable(true);
		}
	}

	public void enableVehicleAdd() {
		for (UserPane userPane : userPaneList) {
			userPane.getAddButton().setDisable(false);
		}
	}

	public synchronized void payment(Vehicle vehicle) {
		long vrijeme = vehicle.getDurationInHours();
		long cijena = 0;
		if (vrijeme < 1) {
			cijena = 1;
		} else if (vrijeme < 3) {
			cijena = 2;
		} else {
			cijena = (vrijeme / 24) * 8;
			if (vrijeme % 24 != 0) {
				cijena += 8;
			}
		}

		try {
			peymentWriter.write(vehicle.getRegistrationNumber() + "," + cijena + "\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public synchronized void noteInvestigation(Vehicle vehicle1, boolean time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
		Date date = new Date();
		String sDate = formatter.format(date) + "\r\n";

		String vozilo1 = vehicle1.getName() + "\r\n" + vehicle1.getRegistrationNumber() + "\r\n";
		byte[] image1 = null;

		try {
			BufferedImage bufferimage1;
			bufferimage1 = ImageIO.read(new File(vehicle1.getImagePath().split(":")[1]));
			ByteArrayOutputStream output1 = new ByteArrayOutputStream();
			ImageIO.write(bufferimage1, "png", output1);
			image1 = output1.toByteArray();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}

		try {
			if (time) {
				accidentWriter.write(sDate.getBytes());
			}
			accidentWriter.write(vozilo1.getBytes());
			accidentWriter.write(image1);
			accidentWriter.write("\r\n".getBytes());
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}

	public boolean isGarageFull() {
		for (PlatformMatrix platform : platformMatrixList) {
			if (platform.getController().getFreeParkingNumber() > 0) {
				return false;
			}
		}
		return true;
	}

}
