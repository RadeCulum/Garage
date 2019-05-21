package net.etfbl.garage.view.admin;

import java.util.Random;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.etfbl.garage.controller.LengthInputListener;
import net.etfbl.garage.controller.NumberInputListener;
import net.etfbl.garage.controller.admin.SubmitInsertController;
import net.etfbl.garage.controller.admin.SubmitUpdateController;
import net.etfbl.garage.utils.GarageProperties;
import net.etfbl.garage.utils.GarageUtils;

public class AddVehicleForm extends Stage{
	private Label labelName;
	private Label labelChassisNumber;
	private Label labelEngineNumber;
	private Label labelPicture;
	private Label labelRegistrationNumber;
	private Label labelDoorNumberl;
	private Label labelCapacity;
	
	private TextField tFieldName;
	private TextField tFieldChassisNumber;
	private TextField tFieldEngineNumber;
	private TextField tFieldPicture;
	private TextField tFieldRegistrationNumber;
	private TextField tFiledDoorNumber;
	private TextField tFieldCapacity;
	
	private Button buttonChoosePicture;
	private Button buttonAdd;
	private Button buttonUpdate;
	
	private VBox vBox;
	
	GarageProperties properties;
	
	public  AddVehicleForm(){
		super();
		properties = GarageProperties.getInstance();
		this.setTitle(properties.getAddFormTitle());
		Group root = new Group();
		Scene scene = new Scene(root);
		
		VBox vBox = new VBox();
		this.vBox = vBox;
		vBox.setPadding(new Insets(10, 20, 20, 10));
		vBox.setSpacing(10);
		
		HBox hBox = new HBox();
		hBox.getChildren().add(this.getTFieldPicture());
		hBox.getChildren().add(this.getbuttonChoosePicture());
		
		vBox.getChildren().add(this.getLabelName());
		vBox.getChildren().add(this.getTFieldName());
		vBox.getChildren().add(this.getLabelChassisNumber());
		vBox.getChildren().add(this.getTFieldChassisNumber());
		vBox.getChildren().add(this.getLabelEngineNumber());
		vBox.getChildren().add(this.getTFieldEngineNumber());
		vBox.getChildren().add(this.getLabelPicture());
		vBox.getChildren().add(hBox);
		vBox.getChildren().add(this.getLabelRegistrationNumber());
		vBox.getChildren().add(this.getTFieldRegistrationNumber());
		
		root.getChildren().add(vBox);
		this.setScene(scene);
		this.setResizable(false);
		this.getIcons().add(new Image(GarageProperties.getInstance().getAppIconPath()));
	}
	
	public Label getLabelName() {
		if(labelName == null) {
			labelName = new Label("Naziv vozila:");
			labelName.setPrefSize(200, 10);
		}
		return labelName;
	}
	
	public TextField getTFieldName() {
		if(tFieldName == null) {
			tFieldName = new TextField();
			tFieldName.setPrefSize(270, 10);
		}
		return tFieldName;
	}
	
	public Label getLabelChassisNumber() {
		if(labelChassisNumber == null) {
			labelChassisNumber = new Label("Broj šasije:");
			labelChassisNumber.setPrefSize(200, 10);
		}
		return labelChassisNumber;
	}
	
	public TextField getTFieldChassisNumber() {
		if(tFieldChassisNumber == null) {
			tFieldChassisNumber = new TextField();
			tFieldChassisNumber.setPrefSize(270, 10);
			tFieldChassisNumber.textProperty().addListener(new NumberInputListener(tFieldChassisNumber));
			tFieldChassisNumber.textProperty().addListener(new LengthInputListener(tFieldChassisNumber, 6));
			tFieldChassisNumber.setText(String.valueOf(new Random().nextInt(999999)));
		}
		return tFieldChassisNumber;
	}
	
	public Label getLabelEngineNumber() {
		if(labelEngineNumber == null) {
			labelEngineNumber = new Label("Broj motora:");
			labelEngineNumber.setPrefSize(200, 10);
		}
		return labelEngineNumber;
	}
	
	public TextField getTFieldEngineNumber() {
		if(tFieldEngineNumber == null) {
			tFieldEngineNumber = new TextField();
			tFieldEngineNumber.setPrefSize(270, 10);
			tFieldEngineNumber.textProperty().addListener(new NumberInputListener(tFieldEngineNumber));
			tFieldEngineNumber.textProperty().addListener(new LengthInputListener(tFieldEngineNumber, 6));
			tFieldEngineNumber.setText(String.valueOf(new Random().nextInt(999999)));
		}
		return tFieldEngineNumber;
	}
	
	public Label getLabelPicture() {
		if(labelPicture == null) {
			labelPicture = new Label("Slika:");
			labelPicture.setPrefSize(200, 10);
		}
		return labelPicture;
	}
	
	public Button getbuttonChoosePicture() {
		if(buttonChoosePicture == null) {
			buttonChoosePicture = new Button("Choose");
			buttonChoosePicture.setPrefSize(100, 10);
		}
		return buttonChoosePicture;
	}
	
	public TextField getTFieldPicture() {
		if(tFieldPicture == null) {
			tFieldPicture = new TextField();
			tFieldPicture.setPrefSize(180, 10);
			tFieldPicture.setEditable(false);
		}
		return tFieldPicture;
	}
	
	public Label getLabelRegistrationNumber() {
		if(labelRegistrationNumber == null) {
			labelRegistrationNumber = new Label("Registarski broj:");
			labelRegistrationNumber.setPrefSize(200, 10);
		}
		return labelRegistrationNumber;
	}
	
	public TextField getTFieldRegistrationNumber() {
		if(tFieldRegistrationNumber == null) {
			tFieldRegistrationNumber = new TextField();
			tFieldRegistrationNumber.setPrefSize(200, 10);
			tFieldRegistrationNumber.setText(GarageUtils.getInstance().getRandomRegistrationNumber());
		}
		return tFieldRegistrationNumber;
	}
	
	public Label getLabelDoorNumber() {
		if(labelDoorNumberl == null) {
			labelDoorNumberl = new Label("Broj vrata:");
			labelDoorNumberl.setPrefSize(200, 10);
		}
		return labelDoorNumberl;
	}
	
	public TextField getTFieldDoorNumber() {
		if(tFiledDoorNumber == null) {
			tFiledDoorNumber = new TextField();
			tFiledDoorNumber.setPrefSize(200, 10);
			tFiledDoorNumber.textProperty().addListener(new NumberInputListener(tFiledDoorNumber));
			tFiledDoorNumber.textProperty().addListener(new LengthInputListener(tFiledDoorNumber, 1));
			tFiledDoorNumber.setText(String.valueOf(new Random().nextInt(3) + 2));
		}
		return tFiledDoorNumber;
	}
	
	public Label getLabelCapacity() {
		if(labelCapacity == null) {
			labelCapacity = new Label("Nosivost");
			labelCapacity.setPrefSize(200, 10);
		}
		return labelCapacity;
	}
	
	public TextField getTFieldCapacity() {
		if(tFieldCapacity == null) {
			tFieldCapacity = new TextField();
			tFieldCapacity.setPrefSize(200, 10);
			tFieldCapacity.textProperty().addListener(new NumberInputListener(tFieldCapacity));
			tFieldCapacity.textProperty().addListener(new LengthInputListener(tFieldCapacity, 5));
			tFieldCapacity.setText(String.valueOf(GarageUtils.getInstance().getRandomcapacity()));

		}
		return tFieldCapacity;
	}
	
	public Button getButtonAdd() {
		if(buttonAdd == null) {
			buttonAdd = new Button("Dodaj");
			buttonAdd.setPrefSize(100, 20);
			buttonAdd.setFont(new Font(14));
			buttonAdd.setOnAction(new SubmitInsertController());
		}
		return buttonAdd;
	}
	
	public Button getButtonUpdate() {
		if(buttonUpdate == null) {
			buttonUpdate = new Button("Potvrdi");
			buttonUpdate.setPrefSize(100, 20);
			buttonUpdate.setFont(new Font(14));
			buttonUpdate.setOnAction(new SubmitUpdateController());
		}
		return buttonUpdate;
	}
	
	public void addCarField() {
		vBox.getChildren().add(this.getLabelDoorNumber());
		vBox.getChildren().add(this.getTFieldDoorNumber());
	}
	
	public void addVanFiled() {
		vBox.getChildren().add(this.getLabelCapacity());
		vBox.getChildren().add(this.getTFieldCapacity());
	}
	
	public void addButtonAdd() {
		vBox.getChildren().add(this.getButtonAdd());
	}
	
	public void addButtonUpdate() {
		vBox.getChildren().add(this.getButtonUpdate());
	}
}
