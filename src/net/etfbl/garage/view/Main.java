package net.etfbl.garage.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import net.etfbl.garage.controller.admin.AdministratorController;
import net.etfbl.garage.utils.GarageProperties;
import net.etfbl.garage.utils.GarageUtils;

public class Main extends Application {
	private static Logger logger = GarageUtils.getInstance().getLogger();

	public static void main(String[] args) {
		try {
			launch(args);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage = MainStage.getInstance();
		primaryStage.setResizable(false);
		primaryStage.setTitle(GarageProperties.getInstance().getPrimaryStageTitle());
		AdministratorController.getInstance().showForm();
		primaryStage.show();

	}
}
