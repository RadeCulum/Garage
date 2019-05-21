package net.etfbl.garage.utils;

public class Messages {
	private static Messages messages = null;
	
	private Messages() {}
	
	public static Messages getInstance() {
		if(messages == null) {
			messages = new Messages();
		}
		return messages;
	}
	
	public String getEmtyFiledMessages() {
		return "Popuni sva polja";
	}
	
	public String getExistRegistrationNumberMessages() {
		return "Postoji vozilo sa ovom registracijom";
	}
	
	public String getErrorMessage() {
		return "Error";
	}
}
