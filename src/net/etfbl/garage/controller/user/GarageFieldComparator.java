package net.etfbl.garage.controller.user;

import java.util.Comparator;

import net.etfbl.garage.enumeration.Intersection;
import net.etfbl.garage.model.field.GarageFieldReference;
import net.etfbl.garage.model.field.InputFieldReference;
import net.etfbl.garage.model.field.IntersectionReferenceField;
import net.etfbl.garage.model.field.OutputFieldReference;
import net.etfbl.garage.model.field.ParkingFieldReference;
import net.etfbl.garage.model.field.VehicleField;
import net.etfbl.garage.model.vehicle.Vehicle;
import net.etfbl.garage.utils.GarageUtils;

public class GarageFieldComparator implements Comparator<GarageFieldReference> {

	@Override
	public int compare(GarageFieldReference first, GarageFieldReference second) {

		GarageUtils utils = GarageUtils.getInstance();
		GarageFieldReference referenceToMe = first.getReferenceToMe();
		VehicleField vehicleField = null;
		Vehicle vehicle = null;

		if (referenceToMe.getGarageField() instanceof VehicleField) {// Trebalo bi da je uvijek tacno
			vehicleField = (VehicleField) referenceToMe.getGarageField();
			vehicle = vehicleField.getVehicle();
		}

		if (utils.isSameInstanceOf(first, second)) {
			return 0;
		}
		/*
		 * Ako vozilo trazi parking mjesto razliku je se ponasanje u zavisnosti da li se
		 * trenutno nalazi na ulaznom ili izlaznom polju
		 */
		if (vehicle.isLookingForParking()) {
			// Nalazim se na ulaznom polju
			if (referenceToMe instanceof InputFieldReference) {

				// Najvisi prioritet ima prazno parking mjesto
				if (first instanceof ParkingFieldReference) {
					return 1;
				} else if (second instanceof ParkingFieldReference) {
					return -1;
				} else if (first instanceof InputFieldReference) {
					return -1;
				} else if (second instanceof InputFieldReference) {
					return 1;
				} else if (first instanceof OutputFieldReference) {
					return 1;
				} else if (second instanceof OutputFieldReference) {
					return -1;
				}
				// Ako trazi parking i naidje na raskrsnicu znci da mora skrenuti
				else if (first instanceof IntersectionReferenceField) {
					return 1;
				} else if (second instanceof IntersectionReferenceField) {
					return -1;
				}

				// Ovdje ne bi trebalo nikada doci
				else {
					System.out.println(" nesto ne valja");
					return 0;
				}
			} else if (referenceToMe instanceof IntersectionReferenceField) {
				if (first instanceof InputFieldReference) {
					return 1;
				} else if (second instanceof InputFieldReference) {
					return -1;
				} else {
					return 0;
				}
			}
			// Ako se vozilo nalazi na izlaznom polju ali trazi parking
			else {
				// Najvisi prioritet ima prazno parking mjesto
				if (first instanceof ParkingFieldReference) {
					return 1;
				} else if (second instanceof ParkingFieldReference) {
					return -1;
				}
				// Ako naidje na raskrsnicu, moze skrenuti samo ako je ona ulazna
				else if (first instanceof IntersectionReferenceField) {
					IntersectionReferenceField intersection = (IntersectionReferenceField) first;
					if (intersection.getType() == Intersection.IN) {
						return 1;
					} else {// Ne smije preci na raskrsnice koje nisu ulazne
						return -1;
					}
				} else if (second instanceof IntersectionReferenceField) {
					IntersectionReferenceField intersection = (IntersectionReferenceField) second;
					if (intersection.getType() == Intersection.IN) {
						return -1;
					} else {// Ne smije preci na raskrsnice koje nisu ulazne
						return 1;
					}
				}
				// Izlazno polje ima prednost nad ulaznim (ne uvijek, i ovo poslije popraviti)
				else if (first instanceof OutputFieldReference) {
					return 1;
				} else if (second instanceof OutputFieldReference) {
					return -1;
				} else if (first instanceof InputFieldReference) {
					return -1;
				} else if (second instanceof InputFieldReference) {
					return 1;
				} else {
					return 0;
				}
			}
		}
		// Ako vozilo odlazi
		else {
			// Ako se nalazi na parkingu imace samo jednu opciju(ili ulazno ili izlazno,
			// nikad obe). Ovaj dio ni ne mora
			if (referenceToMe instanceof ParkingFieldReference) {
				return 1;

			}
			// Ako se nadje na raskrsnici dolazi u obzir samo izlazno polje
			else if (referenceToMe instanceof IntersectionReferenceField) {
				if (first instanceof OutputFieldReference) {
					return 1;
				} else if (second instanceof OutputFieldReference) {
					return -1;
				} else {
					return 0;
				}
			}
			// Ako sa parkinga izadje na ulazno polje, tom stranom ide do raskrsnice, gedje
			// prelazi na izlazno polje
			else if (referenceToMe instanceof InputFieldReference) {
				// Samo na jednu raskrsnicu moze i mora
				if (first instanceof IntersectionReferenceField) {
					return 1;
				}
				if (second instanceof IntersectionReferenceField) {
					return -1;
				} else if (first instanceof InputFieldReference) {
					return 1;
				} else if (second instanceof InputFieldReference) {
					return -1;
				}
				// U ovo slucaju ulazno i parking polje imaju isti prioritet, ne smije ni na
				// jedno
				else if (first instanceof ParkingFieldReference) {
					return -1;
				} else if (second instanceof ParkingFieldReference) {
					return 1;
				} else if (first instanceof OutputFieldReference) {
					return -1;
				} else if (second instanceof OutputFieldReference) {
					return 1;
				} else {
					return 0;
				}
			} else {// Nlazi se na izlaznom polju i napusta parking
					// Samo na jednu raskrsnicu moze i mora
				if (first instanceof IntersectionReferenceField) {
					return 1;
				}
				if (second instanceof IntersectionReferenceField) {
					return -1;
				} else if (first instanceof OutputFieldReference) {
					return 1;
				} else if (second instanceof OutputFieldReference) {
					return -1;
				}
				// U ovo slucaju ulazno i parking polje imaju isti prioritet, ne smije ni na
				// jedno
				else if (first instanceof ParkingFieldReference) {
					return -1;
				} else if (second instanceof ParkingFieldReference) {
					return 1;
				} else {
					return 0;
				}
			}
		}

	}

}
