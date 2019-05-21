package net.etfbl.garage.controller.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/*
 * Klikom na dugme "<<" prelazi se na platformu nize
 */

public class MoveBelowAdminController implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent e) {
		AdministratorController adminController = AdministratorController.getInstance();
		int activePlatformNumber = adminController.getIndexOfActivePane();
		
		if(activePlatformNumber == 0) {
			return;
		}
		adminController.setActivePane(activePlatformNumber - 1);
		adminController.getActivePane().getPlatformNumberCBox().setValue(activePlatformNumber);
	}

}
