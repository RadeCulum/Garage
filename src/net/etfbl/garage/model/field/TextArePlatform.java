package net.etfbl.garage.model.field;

import javafx.scene.control.TextArea;

public class TextArePlatform {
	private TextArea textArea;
	String platform;
			
	
	public TextArePlatform() {
		platform = 
				"========================================================\r\n" + 
				"                                                        \r\n" + 
				"--------------        -------------        -------------\r\n" + 
				"              |                           |             \r\n" + 
				"=======               =============               ======\r\n" + 
				"|  *          |         *   |  *          |         *  |\r\n" + 
				"=======               =============               ======\r\n" + 
				"|  *          |         *   |  *          |         *  |\r\n" + 
				"=======               =============               ======\r\n" + 
				"|  *          |         *   |  *          |         *  |\r\n" + 
				"=======               =============               ======\r\n" + 
				"|  *          |         *   |  *          |         *  |\r\n" + 
				"=======               =============               ======\r\n" + 
				"|  *          |         *   |  *          |         *  |\r\n" + 
				"=======               =============               ======\r\n" + 
				"|  *          |         *   |  *          |         *  |\r\n" + 
				"=======               =============               ======\r\n" + 
				"|  *          |                           |         *  |\r\n" + 
				"=======       -----------------------------       ======\r\n" + 
				"|  *                                                *  |\r\n" + 
				"========================================================";
				
	}
	
	public TextArePlatform(TextArea textArea) {
		this();
		this.textArea = textArea;
	}
	
	public TextArea getTexArea() {
		return this.textArea;
	}
	
	public String getText() {
		return platform;
	}
}
