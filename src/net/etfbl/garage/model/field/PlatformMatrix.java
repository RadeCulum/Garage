package net.etfbl.garage.model.field;

import java.util.ArrayList;

import net.etfbl.garage.controller.user.PlatformController;
import net.etfbl.garage.enumeration.Intersection;
import net.etfbl.garage.model.vehicle.Vehicle;

public class PlatformMatrix {
	GarageFieldReference[][] matrix;
	PlatformMatrix previousPlatform = null;
	PlatformMatrix nextPlatform = null;
	static int platformCounter = 0;
	private int platformNumber;
	PlatformController controller = null;
	
	public PlatformMatrix() {
		platformCounter++;
		platformNumber = platformCounter;
		matrix = new GarageFieldReference[10][8];
		for(int i = 0; i < 10; ++i) {
			matrix[i] = new GarageFieldReference[8];
			for(int j = 0; j < 8; j++) {
				matrix[i][j] = new GarageFieldReference(i, j, platformCounter);
			}
		}
		
		//Parking polja
		for(int col = 0; col < 10; col += 7) {
			for(int row = 2; row < 10; ++row) {
				matrix[row][col] = new ParkingFieldReference(row, col, platformCounter);
			}
		}
		
		for(int col = 3; col < 5; col++) {
			for(int row = 2; row < 8; ++row) {
				matrix[row][col] = new ParkingFieldReference(row, col, platformCounter);
			}
		}
		
		//Ulazna polja
		for(int col = 1; col < 7; col += 5) {
			for(int row = 1; row < 10; row++) {
				matrix[row][col] = new InputFieldReference(row, col, platformCounter);
			}
		}
		
		for(int i = 2; i < 6; ++i) {
			matrix[9][i] = new InputFieldReference(9, i, platformCounter);
		}
		matrix[1][0] = new InputFieldReference(1, 0, platformCounter);
		matrix[1][7] = new InputFieldReference(1, 7, platformCounter);
		matrix[1][3] = new InputFieldReference(1, 3, platformCounter);
		matrix[1][4] = new InputFieldReference(1, 4, platformCounter);
		
		//Izlazna pola
		for(int col = 2; col < 6; col += 3) {
			for(int row = 2; row < 9; row++) {
				matrix[row][col] = new OutputFieldReference(row, col, platformCounter);
			}
		}
		for(int i = 3; i < 5; ++i) {
			matrix[8][i] = new OutputFieldReference(8, i, platformCounter);
		}
		for(int i = 0; i < 8; ++i) {
			matrix[0][i] = new OutputFieldReference(0, i, platformCounter);
		}
		
		//Raskrsnice
		matrix[1][1] = new IntersectionReferenceField(1, 1, platformCounter, Intersection.IN);
		matrix[1][2] = new IntersectionReferenceField(1, 2, platformCounter, Intersection.OUT);
		matrix[1][5] = new IntersectionReferenceField(1, 5, platformCounter,  Intersection.IN);
		matrix[1][6] = new IntersectionReferenceField(1, 6, platformCounter,  Intersection.INOUT);
		
		//Povezivanje polja sa mogucim susjednim poljima
		
		//Za spoljasnja parking polja
		for(int row = 2; row < 10; ++row) {
			for(int col = 0; col < 8; col += 7) {
				if(col == 0) {
					matrix[row][col].addNextField(matrix[row][col+1]);
				}
				else if(col == 7) {
					matrix[row][col].addNextField(matrix[row][col-1]);
				}
			}
		}
		//Za unutrasnja parking polja
		for(int row = 2; row < 8; ++row) {
			for(int col = 3; col < 5; col++) {
				if(col == 3) {
					matrix[row][col].addNextField(matrix[row][col-1]);
				}
				else if(col == 4) {
					matrix[row][col].addNextField(matrix[row][col+1]);
				}
			}
		}
		
		//Druga kolona uz parking mjesta
		for(int row = 2; row < 8; row++) {
			matrix[row][1].addNextField(matrix[row][0]);
			matrix[row][1].addNextField(matrix[row][2]);
			matrix[row][1].addNextField(matrix[row+1][1]);
		}
		matrix[8][1].addNextField(matrix[8][0]);
		matrix[8][1].addNextField(matrix[9][1]);
		matrix[9][1].addNextField(matrix[9][0]);
		matrix[9][1].addNextField(matrix[9][2]);
		
		//Treca kolona uz parking mjesta
		for(int row = 2; row < 8; row++) {
			matrix[row][2].addNextField(matrix[row][1]);
			matrix[row][2].addNextField(matrix[row][3]);
			matrix[row][2].addNextField(matrix[row-1][2]);
		}
		matrix[8][2].addNextField(matrix[8][1]);
		matrix[8][2].addNextField(matrix[7][2]);
		
		//Donji ulazni prolaz
		for(int col = 2; col < 7; col++) {
			matrix[9][col].addNextField(matrix[9][col+1]);
		}
		matrix[9][6].addNextField(matrix[8][6]);
		matrix[8][6].addNextField(matrix[8][7]);
		matrix[8][6].addNextField(matrix[7][6]);
		
		//Donji izlazni prolaz
		for(int col = 3; col < 6; col++) {
			matrix[8][col].addNextField(matrix[8][col - 1]);
		}
		
		//Sesta kolona uz parking polja
		for(int row = 2; row < 8; row++) {
			matrix[row][5].addNextField(matrix[row][4]);
			matrix[row][5].addNextField(matrix[row][6]);
			matrix[row][5].addNextField(matrix[row + 1][5]);
		}
		
		//Sedma kolona uz parking polja
		for(int row = 2; row < 8; row++) {
			matrix[row][6].addNextField(matrix[row][5]);
			matrix[row][6].addNextField(matrix[row][7]);
			matrix[row][6].addNextField(matrix[row-1][6]);
		}
		//Prva vrsta
		for(int col = 1; col < 8; col++) {
			matrix[0][col].addNextField(matrix[0][col-1]);
		}
		
		//Druga vrsta
		for(int col = 0; col < 7; col++) {
			matrix[1][col].addNextField(matrix[1][col+1]);
		}
		matrix[1][1].addNextField(matrix[2][1]);
		matrix[1][2].addNextField(matrix[0][2]);
		matrix[1][5].addNextField(matrix[2][5]);
		matrix[1][6].addNextField(matrix[0][6]);
		matrix[1][7].addNextField(matrix[0][7]);
		
		for(int col = 0; col < 8; col++) {
			matrix[0][col].addExtraFiled(matrix[1][col]);
			matrix[1][col].addExtraFiled(matrix[0][col]);
		}
		
		matrix[1][6].addExtraFiled(matrix[1][5]);
	}
	
	public int getPlatformNumber() {
		return this.platformNumber;
	}
	
	public void setController(PlatformController controller) {
		this.controller = controller;
	}
	
	public PlatformController getController() {
		return this.controller;
	}
	
	public GarageFieldReference getGarageFieldReference(int row, int col) {
		return this.matrix[row][col];
	}
	
	public GarageFieldReference getElemnt(int row, int col) {
		return matrix[row][col];
	}
	
	public PlatformMatrix getPrviousPlatform() {
		return previousPlatform;
	}
	
	public void setPreviousPlatform(PlatformMatrix platformMatrix) {
		this.previousPlatform = platformMatrix;
	}
	
	public PlatformMatrix getNextPlatform() {
		return nextPlatform;
	}
	
	public void setNextPlatform(PlatformMatrix platformMatrix) {
		this.nextPlatform = platformMatrix;
	}
	
	public ArrayList<GarageFieldReference> getEmtyParkingFields() {
		ArrayList<GarageFieldReference> list = new ArrayList<>();
		for(int i = 0; i  < 10; ++i) {
			for(int j = 0; j < 8; ++j) {
				if(this.matrix[i][j] instanceof ParkingFieldReference && this.matrix[i][j].getGarageField() instanceof EmptyFiled) {
					list.add(matrix[i][j]);
				}
			}
		}
		return list;
	}
}
