package net.etfbl.garage.controller.user;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MoveAboveUserController implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent e) {
		UserController userController = UserController.getInstance();
		int activePlatformNumber = userController.getIndexOfActivePane() + 1;
		if (activePlatformNumber != userController.getPlatformsNumber()) {
			userController.setActivePane(activePlatformNumber);
			userController.updatePlatformNuberCB();
		}

	}
}
