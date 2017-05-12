package constants;

public enum Exception { // Хранят сообщения по экспешнеам
	FILE_NOT_FOUND("Файл не найден"), 
	IO("Ошибка ввода/вывода"), 
	JAXB_READ("Ошибка чтения xml"), 
	JAXB_RECORD("Ошибка записи xml"), 
	ILLEGAL_ARGUMENT("Неверный аргумент"),
	PARSE("Неверный формат даты/времени");
	
	private final String exception;
	
	private Exception(String exception){
		this.exception = exception;
	}
	
	@Override
	public String toString(){
		return exception;
	}
}
