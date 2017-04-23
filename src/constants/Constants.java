package constants;

// Класс, хранящий константы, изначально для использования их в интерфейсах при работе с JAXB
import model.Journal;

public final class Constants {
	// получили пакет класса через рефлекшн
	public static final String PACKAGE = Journal.class.getPackage().getName();
	
	// дефолтный путь к папке с хранимой xml
	public static final String PATH = "storage";

	// дефолтное имя файла xml
	public static final String NAME = "default.xml";
	
	private Constants(){}

}
