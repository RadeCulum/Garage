package net.etfbl.garage.view.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import net.etfbl.garage.controller.LengthInputListener;
import net.etfbl.garage.controller.NumberInputListener;
import net.etfbl.garage.controller.NumberLimitListener;
import net.etfbl.garage.controller.user.AddVehicleHandler;
import net.etfbl.garage.controller.user.MoveAboveUserController;
import net.etfbl.garage.controller.user.MoveBelowUserController;
import net.etfbl.garage.controller.user.PlatformController;
import net.etfbl.garage.controller.user.UserController;
import net.etfbl.garage.model.field.TextArePlatform;
import net.etfbl.garage.utils.GarageProperties;

public class UserPane extends Pane{
	private Button moveBelowButton;
	private Button moveAboveButton;
	private Button playButton;
	private Button addButton;
	private Label platformNumberLabel;
	private TextField minimumVehicleNumberTField;
	private Label currentFreeParkingPlaces;
	private Label currentFreeParkingPlacesTextLabel;
	private Label minimumVehicleNumberLabel;
	private Label slashLabel;
	private ComboBox<Integer> platformNumberCBox;
	private TextArea platformTextArea;
	
	private PlatformController controller;
	
	GarageProperties properties;
	
	
	public UserPane() {
		super();
		properties = GarageProperties.getInstance();
		new Scene(this, 650, 580);
		this.getChildren().add(getPlayButton());
		this.getChildren().add(getMoveBelowButton());
		this.getChildren().add(getMoveAboveButton());
		this.getChildren().add(getAddButton());
		this.getChildren().add(getPlatformNumberLabel());
		this.getChildren().add(getCurrentFreeParkingPlacesTextLabel());
		this.getChildren().add(getCurrentFreeParkingPlacesLabel());
		this.getChildren().add(getMinimumVehicleNumberLabel());
		this.getChildren().add(getMinimumVehicleNumberTField());
		this.getChildren().add(getSlashLabel());
		this.getChildren().add(getPlatformNumberCBox());
		this.getChildren().add(getPlatformTextArea());
	}
	
	private void initializeButton(Button button, String title, Double x, Double y, Double width, Double height, Font font) {
		button.setText(title);
		button.setLayoutX(x);
		button.setLayoutY(y);
		button.setPrefSize(width, height);
		button.setFont(font);
	}
	
	public PlatformController getController() {
		return this.controller;
	}
	
	public void setController(PlatformController controller) {
		this.controller = controller;
	}
	public  Button getMoveBelowButton(){
		if(moveBelowButton == null) {
			moveBelowButton = new Button();
			this.initializeButton(moveBelowButton, "<<", 185.0, 2.0, 50.0, 50.0, new Font(18));
			moveBelowButton.setOnAction(new MoveBelowUserController());
		}
		return moveBelowButton;
	}
	
	public  Button getMoveAboveButton(){
		if(moveAboveButton == null) {
			moveAboveButton = new Button();
			this.initializeButton(moveAboveButton, ">>", 345.0, 2.0, 50.0, 50.0, new Font(18));
			moveAboveButton.setOnAction(new MoveAboveUserController());
		}
		return moveAboveButton;
	}
	
	public Button getPlayButton() {
		if(playButton == null) {
			playButton = new Button();
			Image playImage = new Image(GarageProperties.getInstance().getPlayIconPath(), 50, 50, true, true);
			playButton.setGraphic(new ImageView(playImage));
			this.initializeButton(playButton, "", 570.0, 2.0, 50.0, 50.0, new Font(10));
		}
		return playButton;
	}
	
	public Button getAddButton() {
		if(addButton == null) {
			addButton = new Button();
			Image addImage = new Image(GarageProperties.getInstance().getAddIconPath(), 25, 25, true, true);
			addButton.setGraphic(new ImageView(addImage));
			this.initializeButton(addButton, "", 590.0, 62.0, 36.0, 36.0, new Font(10));
			addButton.setOnAction(AddVehicleHandler.getInstance());
			addButton.setDisable(true);
		}
		return addButton;
	}
	
	public Label getPlatformNumberLabel() {
		if(platformNumberLabel == null) {
			platformNumberLabel = new Label();
			platformNumberLabel.setLayoutX(315);
			platformNumberLabel.setLayoutY(10);
			platformNumberLabel.setPrefSize(30, 30);
			platformNumberLabel.setAlignment(Pos.CENTER);
		}
		return platformNumberLabel;
	}
	
	public TextField getMinimumVehicleNumberTField() {
		if(minimumVehicleNumberTField == null) {
			minimumVehicleNumberTField = new TextField("0");
			minimumVehicleNumberTField.setLayoutX(526);
			minimumVehicleNumberTField.setLayoutY(5);
			minimumVehicleNumberTField.setPrefSize(40, 40);
			minimumVehicleNumberTField.setFont(new Font(15));
			minimumVehicleNumberTField.setAlignment(Pos.CENTER);
			minimumVehicleNumberTField.textProperty().addListener(new NumberInputListener(minimumVehicleNumberTField));
			minimumVehicleNumberTField.textProperty().addListener(new LengthInputListener(minimumVehicleNumberTField, 2));
			minimumVehicleNumberTField.textProperty().addListener(new NumberLimitListener(minimumVehicleNumberTField));
			minimumVehicleNumberTField.textProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> arg0, String oldValue, String newValue) {
					if (!newValue.matches("\\d*")) {
						minimumVehicleNumberTField.setText(oldValue);
					}
					else {
						if(!newValue.equals("") && Integer.parseInt(newValue) != 0) {
							UserController.getInstance().disablePlaySimulation(false);
						}
						else if(PlatformController.garageVehicleCounter == 0){
							UserController.getInstance().disablePlaySimulation(true);
						}
						
					}
					
				}
			});
		}
		return minimumVehicleNumberTField;
	}
	
	public Label getCurrentFreeParkingPlacesLabel() {
		if(currentFreeParkingPlaces == null) {
			currentFreeParkingPlaces = new Label("28");
			currentFreeParkingPlaces.setLayoutX(526);
			currentFreeParkingPlaces.setLayoutY(50);
			currentFreeParkingPlaces.setPrefSize(40, 40);
			currentFreeParkingPlaces.setFont(new Font(15));
			currentFreeParkingPlaces.setAlignment(Pos.CENTER);
		}
		return currentFreeParkingPlaces;
	}
	
	public Label getMinimumVehicleNumberLabel() {
		if(minimumVehicleNumberLabel == null) {
			minimumVehicleNumberLabel = new Label("Minimum vozila:");
			minimumVehicleNumberLabel.setLayoutX(410);
			minimumVehicleNumberLabel.setLayoutY(5);
			minimumVehicleNumberLabel.setPrefSize(120, 40);
			minimumVehicleNumberLabel.setFont(new Font(15));
		}
		return minimumVehicleNumberLabel;
	}
	
	public Label getCurrentFreeParkingPlacesTextLabel() {
		if(currentFreeParkingPlacesTextLabel == null) {
			currentFreeParkingPlacesTextLabel = new Label("Broj slobodnih parking mjesta:");
			currentFreeParkingPlacesTextLabel.setLayoutX(320);
			currentFreeParkingPlacesTextLabel.setLayoutY(50);
			currentFreeParkingPlacesTextLabel.setPrefSize(210, 40);
			currentFreeParkingPlacesTextLabel.setFont(new Font(15));
		}
		return currentFreeParkingPlacesTextLabel;
	}
	
	
	
	public Label getSlashLabel() {
		if(slashLabel == null) {
			slashLabel = new Label("/");
			slashLabel.setLayoutX(300);
			slashLabel.setLayoutY(10);
			slashLabel.setPrefSize(15, 20);
			slashLabel.setAlignment(Pos.CENTER);
			slashLabel.setFont(new Font(20));
		}
		return slashLabel;
	}
	
	public ComboBox<Integer> getPlatformNumberCBox(){
		if(platformNumberCBox == null) {
			platformNumberCBox = new ComboBox<>();
			platformNumberCBox.setLayoutX(235);
			platformNumberCBox.setLayoutY(10);
			platformNumberCBox.setPrefSize(65, 30);
			platformNumberCBox.setEditable(false);
			platformNumberCBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
			
			platformNumberCBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {

		        @Override
		        public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
		            UserController.getInstance().setActivePane(arg2 - 1);
		            UserController.getInstance().getActivePane().getPlatformNumberCBox().setValue(arg2);
		        }
		    });
		}
		return platformNumberCBox;
	}
	
	public TextArea getPlatformTextArea() {
		if(platformTextArea == null) {			
			platformTextArea = new TextArea();
			platformTextArea.setPrefSize(515,455);
			platformTextArea.setLayoutX(70);
			platformTextArea.setLayoutY(90);
			platformTextArea.setFont(Font.font("Consolas", 16));	
			platformTextArea.setText(new TextArePlatform().getText());
			platformTextArea.setEditable(false);
		}
		
		return platformTextArea;
	}
	
}
