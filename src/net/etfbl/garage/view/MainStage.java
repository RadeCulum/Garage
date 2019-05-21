package net.etfbl.garage.view;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.etfbl.garage.utils.GarageProperties;

public class MainStage extends Stage{
	private static MainStage mainStage = null;
	
	private MainStage() {}
	
	public static MainStage getInstance() {
		if(mainStage == null) {
			mainStage = new MainStage();
			mainStage.getIcons().add(new Image(GarageProperties.getInstance().getAppIconPath()));
			mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent t) {
			        Platform.exit();
			        System.exit(0);
			    }
			});
		}
		return mainStage;
	}
}
