package net.etfbl.garage.view.admin;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Callback;
import net.etfbl.garage.controller.admin.AddVehicleController;
import net.etfbl.garage.controller.admin.AdministratorController;
import net.etfbl.garage.controller.admin.ChangeVehicleController;
import net.etfbl.garage.controller.admin.MoveAboveAdminController;
import net.etfbl.garage.controller.admin.MoveBelowAdminController;
import net.etfbl.garage.controller.admin.PlayUserController;
import net.etfbl.garage.controller.admin.RemoveVehicleController;
import net.etfbl.garage.enumeration.VehicleType;
import net.etfbl.garage.model.vehicle.Car;
import net.etfbl.garage.model.vehicle.Van;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.utils.GarageProperties;

public class AdministratorPane extends Pane{
	private Button moveBelowButton;
	private Button moveAboveButton;
	private Button playButton;
	private Button addVehicleButton;
	private Button removeVehicleButton;
	private Button changeVehicleButton;
	private TextField platformNumberTField;
	private VehicleTableView vehicleTableView;
	private ComboBox<String> vehicleCBox;
	private ComboBox<Integer> platformNumberCBox;
	private Label vehicleTypeLabel;
	private Label slashLabel;
	
	static int counter = 0;
	
	AddVehicleForm addVehicleForm;
	GarageProperties properties;

	
	public AdministratorPane(){
		super();
		counter++;
		properties = GarageProperties.getInstance();
		new Scene(this, 1000, 600);
		this.getChildren().add(getPlayButton());
		this.getChildren().add(getMoveBelowButton());
		this.getChildren().add(getMoveAboveButton());
		this.getChildren().add(getPlatformNumberTField());
		this.getChildren().add(getSlashLabel());
		this.getChildren().add(getPlatformNumberCBox());
		this.getChildren().add(getVehicleTableView());
		this.getChildren().add(getVehicleCBox());
		this.getChildren().add(getAddVehicleButton());
		this.getChildren().add(getChangeVehicleButton());
		this.getChildren().add(getRemoveVehicleButton());
		this.getChildren().add(getVehicleTypeLabel());
	}
	
	//Inicijalizacija neki zajednickih stvari za sve Button-e
	private void initializeButton(Button button, String title, Double x, Double y, Double width, Double height, Font font) {
		button.setText(title);
		button.setLayoutX(x);
		button.setLayoutY(y);
		button.setPrefSize(width, height);
		button.setFont(font);
	}
	//Button za pomjeranje na platformu iznad
	public  Button getMoveBelowButton(){
		if(moveBelowButton == null) {
			moveBelowButton = new Button();
			this.initializeButton(moveBelowButton, "<<", 390.0, 20.0, 50.0, 50.0, new Font(18));
			moveBelowButton.setOnAction(new MoveBelowAdminController());
		}
		return moveBelowButton;
	}
	//Button za pomjeranje na platformu ispod
	public  Button getMoveAboveButton(){
		if(moveAboveButton == null) {
			moveAboveButton = new Button();
			this.initializeButton(moveAboveButton, ">>", 550.0, 20.0, 50.0, 50.0, new Font(18));
			moveAboveButton.setOnAction(new MoveAboveAdminController());
		}
		return moveAboveButton;
	}
	//Button kojim se otvara forma za unos nosovg vozila. Mora nesto biti selektovano u ComboBox-u
	public Button getAddVehicleButton() {
		if(addVehicleButton == null) {
			addVehicleButton = new Button();
			this.initializeButton(addVehicleButton, "Dodaj vozilo", 390.0, 500.0, 100.0, 20.0, new Font(13));
			addVehicleButton.setDisable(true);
			addVehicleButton.setOnAction(new AddVehicleController());
		}
		return addVehicleButton;
	}
	//Buton kojim se otvara forma za izmjenu SELEKTOVANOG vozila
	public Button getChangeVehicleButton() {
		if(changeVehicleButton == null) {
			changeVehicleButton = new Button();
			this.initializeButton(changeVehicleButton, "Izmjeni vozilo", 500.0, 500.0, 100.0, 20.0, new Font(13));
			changeVehicleButton.setDisable(true);
			changeVehicleButton.setOnAction(new ChangeVehicleController());
		}
		return changeVehicleButton;
	}
	//Button kojim se brije SELEKTOVANO vozilo
	public Button getRemoveVehicleButton() {
		if(removeVehicleButton == null) {
			removeVehicleButton = new Button();
			this.initializeButton(removeVehicleButton, "Obrisi vozilo", 610.0, 500.0, 100.0, 20.0, new Font(13));
			removeVehicleButton.setDisable(true);
			removeVehicleButton.setOnAction(new RemoveVehicleController());
		}
		return removeVehicleButton;
	}
	//Button za prelazak na korisnicki dio aplikacije
	public Button getPlayButton() {
		if(playButton == null) {
			playButton = new Button();
			Image playImage = new Image(GarageProperties.getInstance().getPlayIconPath(), 50, 50, true, true);
			playButton.setGraphic(new ImageView(playImage));
			this.initializeButton(playButton, "", 900.0, 18.0, 50.0, 50.0, new Font(10));
			playButton.setOnAction(new PlayUserController());
		}
		return playButton;
	}
	//TextField u kome pise ukupan broj platformi
	public TextField getPlatformNumberTField() {
		if(platformNumberTField == null) {
			platformNumberTField = new TextField();
			platformNumberTField.setLayoutX(520);
			platformNumberTField.setLayoutY(25);
			platformNumberTField.setPrefSize(30, 30);
			platformNumberTField.setEditable(false);
			platformNumberTField.setAlignment(Pos.CENTER);
//			platformNumberTField.setText(String.valueOf(AdministratorController.getInstance().getPlatformsNumber()));
		}
		return platformNumberTField;
	}
	//Labela u kojoj se nalazi "/"
	public Label getSlashLabel() {
		if(slashLabel == null) {
			slashLabel = new Label("/");
			slashLabel.setLayoutX(505);
			slashLabel.setLayoutY(25);
			slashLabel.setPrefSize(15, 20);
			slashLabel.setAlignment(Pos.CENTER);
			slashLabel.setFont(new Font(20));
		}
		return slashLabel;
	}
	
	//Tabela
	public VehicleTableView getVehicleTableView() {
		if(vehicleTableView == null) {
			vehicleTableView = new VehicleTableView();
			
			vehicleTableView.setRowFactory(tv -> {
	            TableRow<Vehicle> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (! row.isEmpty()) {
	                	this.getChangeVehicleButton().setDisable(false);
	                	this.getRemoveVehicleButton().setDisable(false);
	                }
	            });
	            return row ;
	        });
			
			TableColumn<Vehicle, String> vehicleName = new TableColumn<Vehicle, String>("Naziv");
			vehicleName.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("name"));
			vehicleName.setSortable(false);
	        vehicleName.setCellFactory(new Callback<TableColumn<Vehicle, String>, TableCell<Vehicle, String>>() {
	            @Override
	            public TableCell<Vehicle, String> call(TableColumn<Vehicle, String> param) 
	            {
	                return new TableCell<Vehicle, String>() 
	                {
	                    @Override
	                    public void updateItem(String item, boolean empty) {
	                        super.updateItem(item, empty);

	                        if(isEmpty())
	                        {
	                            setText("");
	                        }
	                        else
	                        {
	                            setFont(Font.font (properties.getTableStyle(), properties.getTableFont()));	  
	                            setAlignment(Pos.CENTER_LEFT);
	                            setWrapText(true);
	                            setText(item);
	                        }
	                    }
	                };
	            }
	        });
			vehicleName.setPrefWidth(220);
			
			TableColumn<Vehicle, Integer> chassisNumber = new TableColumn<Vehicle, Integer>("Broj šasije");
			chassisNumber.setSortable(false);
			chassisNumber.setCellValueFactory(new PropertyValueFactory<Vehicle, Integer>("chassisNumber"));
			chassisNumber.setCellFactory(new Callback<TableColumn<Vehicle, Integer>, TableCell<Vehicle, Integer>>() {
	            @Override
	            public TableCell<Vehicle, Integer> call(TableColumn<Vehicle, Integer> param) 
	            {
	                return new TableCell<Vehicle, Integer>() 
	                {
	                    @Override
	                    public void updateItem(Integer item, boolean empty) {
	                        super.updateItem(item, empty);

	                        if(isEmpty())
	                        {
	                            setText("");
	                        }
	                        else
	                        {
	                            setFont(Font.font (properties.getTableStyle(), properties.getTableFont()));	  
	                            setAlignment(Pos.CENTER);
	                            setWrapText(true);
	                            setText(String.format("%06d", item));
	                        }
	                    }
	                };
	            }
	        });
			chassisNumber.setPrefWidth(120);
			
			TableColumn<Vehicle, Integer> engineNumber = new TableColumn<Vehicle, Integer>("Broj motora");
			engineNumber.setSortable(false);
			engineNumber.setCellValueFactory(new PropertyValueFactory<>("engineNumber"));
			engineNumber.setCellFactory(new Callback<TableColumn<Vehicle, Integer>, TableCell<Vehicle, Integer>>() {
	            @Override
	            public TableCell<Vehicle, Integer> call(TableColumn<Vehicle, Integer> param) 
	            {
	                return new TableCell<Vehicle, Integer>() 
	                {
	                    @Override
	                    public void updateItem(Integer item, boolean empty) {
	                        super.updateItem(item, empty);

	                        if(isEmpty())
	                        {
	                            setText("");
	                        }
	                        else
	                        {
	                            setFont(Font.font (properties.getTableStyle(), properties.getTableFont()));	  
	                            setAlignment(Pos.CENTER);
	                            setWrapText(true);
	                            setText(String.format("%06d", item));
	                        }
	                    }
	                };
	            }
	        });
			engineNumber.setPrefWidth(120);
			
			TableColumn<Vehicle, ImageView> picture = new TableColumn<Vehicle, ImageView>("Slika");
			picture.setCellValueFactory(new PropertyValueFactory<>("picture"));
			picture.setSortable(false);
			picture.setPrefWidth(120);
			
			TableColumn<Vehicle, String> registrationNumber = new TableColumn<Vehicle,String>("Registrski broj");
			registrationNumber.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
			registrationNumber.setCellFactory(new Callback<TableColumn<Vehicle, String>, TableCell<Vehicle, String>>() {
	            @Override
	            public TableCell<Vehicle, String> call(TableColumn<Vehicle, String> param) 
	            {
	                return new TableCell<Vehicle, String>() 
	                {
	                    @Override
	                    public void updateItem(String item, boolean empty) {
	                        super.updateItem(item, empty);

	                        if(isEmpty())
	                        {
	                            setText("");
	                        }
	                        else
	                        {
	                            setFont(Font.font (properties.getTableStyle(), properties.getTableFont()));	  
	                            setAlignment(Pos.CENTER);
	                            setWrapText(true);
	                            setText(item);
	                        }
	                    }
	                };
	            }
	        });
			registrationNumber.setPrefWidth(140);
			
			TableColumn<Vehicle, Integer> doorNumber = new TableColumn<Vehicle, Integer>("Broj vrata");
			doorNumber.setSortable(false);
			doorNumber.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Vehicle,Integer>, ObservableValue<Integer>>() {				
				@Override
				public ObservableValue<Integer> call(CellDataFeatures<Vehicle, Integer> arg) {
					if(arg.getValue() instanceof Car) {
						return new ObservableValue<Integer>() {							
							@Override
							public void removeListener(InvalidationListener arg0) {}
							
							@Override
							public void addListener(InvalidationListener arg0) {}
							
							@Override
							public void removeListener(ChangeListener<? super Integer> arg0) {}
							
							@Override
							public Integer getValue() { 
								Car car = (Car)arg.getValue();
								return car.getDoorNumber(); 
								
							}							
							@Override
							public void addListener(ChangeListener<? super Integer> arg0) {}
						};
					}
					return null;
				}
			});
			doorNumber.setCellFactory(new Callback<TableColumn<Vehicle, Integer>, TableCell<Vehicle, Integer>>() {
	            @Override
	            public TableCell<Vehicle, Integer> call(TableColumn<Vehicle, Integer> param) 
	            {
	                return new TableCell<Vehicle, Integer>() 
	                {
	                    @Override
	                    public void updateItem(Integer item, boolean empty) {
	                        super.updateItem(item, empty);
	                        if(isEmpty())
	                        {
	                            setText("");
	                        }
	                        else
	                        {
	                            setFont(Font.font (properties.getTableStyle(), properties.getTableFont()));	  
	                            setAlignment(Pos.CENTER);
	                            if(item != null) {
	                            	setText(String.valueOf(item));
	                            }
	                            setWrapText(true);
	                            
	                        }
	                    }
	                };
	            }
	        });
			doorNumber.setPrefWidth(98);
			
			TableColumn<Vehicle, Integer> capacity = new TableColumn<Vehicle, Integer>("Nosivost");
			capacity.setSortable(false);
			capacity.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Vehicle,Integer>, ObservableValue<Integer>>() {
				@Override
				public ObservableValue<Integer> call(CellDataFeatures<Vehicle, Integer> arg) {
					if(arg.getValue() instanceof Van) {
						return new ObservableValue<Integer>() {
							@Override
							public void addListener(InvalidationListener arg0) {}

							@Override
							public void removeListener(InvalidationListener arg0) {}

							@Override
							public void addListener(ChangeListener<? super Integer> arg0) {}

							@Override
							public Integer getValue() { 
								Van van = (Van)arg.getValue();
								return van.getCapacity(); 
							}

							@Override
							public void removeListener(ChangeListener<? super Integer> arg0) {}
						};
					}
					return null;
				}
			});
			capacity.setCellFactory(new Callback<TableColumn<Vehicle, Integer>, TableCell<Vehicle, Integer>>() {
	            @Override
	            public TableCell<Vehicle, Integer> call(TableColumn<Vehicle, Integer> param) 
	            {
	                return new TableCell<Vehicle, Integer>() 
	                {
	                    @Override
	                    public void updateItem(Integer item, boolean empty) {
	                        super.updateItem(item, empty);

	                        if(isEmpty())
	                        {
	                            setText("");
	                        }
	                        else
	                        {
	                            setFont(Font.font (properties.getTableStyle(), properties.getTableFont()));	  
	                            setAlignment(Pos.CENTER);
	                            if(item != null) {
	                            	setText(String.valueOf(item));
	                            }
	                            setWrapText(true);
	                        }
	                    }
	                };
	            }
	        });
			capacity.setPrefWidth(120);
			
			vehicleTableView.getColumns().add(vehicleName);
			vehicleTableView.getColumns().add(chassisNumber);
			vehicleTableView.getColumns().add(engineNumber);
			vehicleTableView.getColumns().add(picture);
			vehicleTableView.getColumns().add(registrationNumber);
			vehicleTableView.getColumns().add(doorNumber);
			vehicleTableView.getColumns().add(capacity);

			vehicleTableView.setLayoutX(30);
			vehicleTableView.setLayoutY(80);
			vehicleTableView.setPrefSize(940, 400);
		}
		return vehicleTableView;
	}
	//ComboBox sa svim tipovima vozila. Koristi se pri dodavanju novog vozila
	public ComboBox<String> getVehicleCBox(){
		if(vehicleCBox == null) {
			vehicleCBox = new ComboBox<>();
			vehicleCBox.getItems().addAll(VehicleType.AUTOMOBIL.getType(), VehicleType.MOTOCIKL.getType(), VehicleType.KOMBI.getType());
			vehicleCBox.setLayoutX(145);
			vehicleCBox.setLayoutY(500);
			vehicleCBox.setPrefWidth(215);
			vehicleCBox.setOnAction(new EventHandler<ActionEvent>() {				
				@Override
				public void handle(ActionEvent arg0) {
					addVehicleButton.setDisable(false);					
				}
			});
			vehicleCBox.setEditable(false);
		}
		return vehicleCBox;
	}
	
	//ComboBox sa brojevima svih platformi
	public ComboBox<Integer> getPlatformNumberCBox(){
		if(platformNumberCBox == null) {
			platformNumberCBox = new ComboBox<>();
			platformNumberCBox.setLayoutX(440);
			platformNumberCBox.setLayoutY(25);
			platformNumberCBox.setPrefSize(65, 30);
			platformNumberCBox.setEditable(false);
			platformNumberCBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
			for(int i = 1; i <= GarageProperties.getInstance().getPlatformsNumber(); ++i) {
				platformNumberCBox.getItems().add(i);
			}
			platformNumberCBox.setValue(counter);
			
			platformNumberCBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {

		        @Override
		        public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
		            AdministratorController.getInstance().setActivePane(arg2 - 1);
		           AdministratorController.getInstance().getActivePane().getPlatformNumberCBox().setValue(arg2);
		        }
		    });
		}
		return platformNumberCBox;
	}
	
	//Labela kod ComboBoxa za odabir tipa vozila
	private Label getVehicleTypeLabel() {
		if(vehicleTypeLabel == null) {
			vehicleTypeLabel = new Label("Vrsta vozila:");
			vehicleTypeLabel.setLayoutX(60);
			vehicleTypeLabel.setLayoutY(500);
			vehicleTypeLabel.setPrefWidth(115);
			vehicleTypeLabel.setFont(new Font(13));
		}
		return vehicleTypeLabel;
	}
	
	//Vraca formu za dodavanje vozila
	public AddVehicleForm getAddVehicleForm() {
		return addVehicleForm;
	}
	//Kreira formu za dodavanje vozila
	public void setAddVehicleForm() {
		addVehicleForm = new AddVehicleForm();

	}
	
}
