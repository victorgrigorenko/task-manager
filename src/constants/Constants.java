package constants;

import model.Journal;

public interface Constants {

	String PACKAGE = Journal.class.getPackage().getName();
	
	String PATH = "storage";

	String NAME = "default.xml";
	
	String DATE_FORMAT = "d.MM.yyyy HH:mm";
	
	String HELP_FILE = "storage/help.txt";
	
	String NONE = "";	
}
