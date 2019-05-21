package net.etfbl.garage.controller.user;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MoveBelowUserController implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent e) {
		UserController userController = UserController.getInstance();
		int activePlatformNumber = userController.getIndexOfActivePane() + 1;

		if (activePlatformNumber == 1) {
			return;
		}
		userController.setActivePane(activePlatformNumber - 2);
		userController.getActivePane().getPlatformNumberCBox().setValue(activePlatformNumber - 1);
	}
}
