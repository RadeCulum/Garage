package net.etfbl.garage.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import net.etfbl.garage.model.field.PlatformMatrix;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.utils.GarageUtils;
import net.etfbl.garage.view.user.UserPane;

public class PlaySimulationController implements EventHandler<ActionEvent> {

	UserController userController = UserController.getInstance();
	List<UserPane> userPaneList = userController.getAllUserPanes();
	ArrayList<PlatformMatrix> platformArray = new ArrayList<>();

	Logger logger = GarageUtils.getInstance().getLogger();

	@Override
	public void handle(ActionEvent arg) {
		userController.disableComponentForSimulation(true);
		userController.enableComponentForSimulation();
		for (int i = 0; i < userController.getPlatformsNumber(); ++i) {
			userController.getControlerOfUserPaneOnIndex(i).fillPlatform();
		}

		for (UserPane userPane : userPaneList) {
			platformArray.add(userPane.getController().getPlatform());
		}

		for (PlatformMatrix platform : platformArray) {
			for (Vehicle vehicle : platform.getController().getVehiclesForMoveOut()) {
				userController.incrementThreadCounter();
				vehicle.setMoving();
				vehicle.getVehicleController().start();
			}
		}
	}

}
