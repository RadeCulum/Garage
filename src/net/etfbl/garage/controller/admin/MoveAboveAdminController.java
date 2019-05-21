package net.etfbl.garage.controller.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
/*
 * Klikom na dugme ">>" prelazi se na platformu vise
 */
public class MoveAboveAdminController implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent e) {
		AdministratorController adminController = AdministratorController.getInstance();
		int activePlatformNumber = adminController.getIndexOfActivePane() + 1;
		if(activePlatformNumber < adminController.getPlatformsNumber()) {
			adminController.setActivePane(activePlatformNumber);
			adminController.updatePlatformNuberCB();
		}
	}

}
