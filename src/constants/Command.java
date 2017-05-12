package constants;

public enum Command {
	ADD("add"), DEL("del"), SEARCH("search"), EDIT("edit"), SHOW_ALL("show all"), 
	CLEAR_ALL("clear all"), RECORD("record"), READ("read"), HELP("HELP"), STOP("stop"),
	OTHER("Вы указали неверную команду");
	
	private final String command;
	
	private Command(String command){
		this.command = command;
	}
	
	public static Command valueParse(String arg){ // считываем строку и возвращаем соответствующую команду
		Command cmd;
		String tmp = "";
		char[] chArr = arg.trim().toCharArray();
		for(int i = 0; i<chArr.length; i++){
			if (chArr[i] == ' ') chArr[i] = '_';
			tmp += chArr[i];
		}
		tmp = tmp.toUpperCase();
		try{
			cmd = Command.valueOf(tmp);
		}catch (IllegalArgumentException | NullPointerException e) {
			cmd = Command.OTHER; 
		}
		return cmd;
	}

	
	@Override
	public String toString(){
		return command;
	}

}
//break;
//case "del": del(); // удаление задачи commandShowTasks
//break;
//case "search": search(); // поиск задачи
//break;
//case "edit": edit(); // редактирование задачи
////System.out.println("Функция редактирования задачи пока отсутствует");
//break;
//case "show all": showAll(); // вывод всех задач
//break;
//case "clear all": clearAll(); // удалить все задачи
//break;
//case "record": record(); // запись журнала задач в файл
//break;
//case "read": read(); // чтение журнала задач из файла
//break;
//case "help": help(); // справка
//break;
//case "stop": run = false;