package constants;

public interface ConstantMessage {

	String START = "Cтарт.. ";
	
	String TITLE = "Название: ";

	String DESCRIPTION = "Описание: ";

	String DATE = "Введите дату и время (D.MM.YYYY hh:mm): ";
	

	String ADD_MSG = "Для добавления задачи построчно вводите поля (название/описание/дата)";

	String DEL_MSG = "Для удаления задачи введите имя: ";

	String SEARCH_MSG = "Для поиска задачи введите имя: ";

	String EDIT_MSG = "Введите название задачи, которую хотите отредактировать: ";

	String RECORD_MSG = "Введите имя файла, в который хотите сохранить журнал: ";

	String READ_MSG = "Введите имя файла, из которого хотите загрузить задачи: ";

	String INFORM_MSG = "Вы ввели неверную команду. Попробуйте заново или воспользуйтесь справкой 'help'";
	
	
	String SUCCESS_ADD_TASK_MSG = "Задача успешно добалена"; 
	
	String NOT_SUCCESS_ADD_TASK_MSG = "Задача не была добавлена"; 
	
	String SUCCESS_DEL_TASK_MSG = "Задача успешно удалена"; 
	
	String NOT_SUCCESS_SEARCH_TASK_MSG = "Задачи с таким названием не существует"; 
	
	String SUCCESS_EDIT_TASK_MSG = "Задача успешно отредактирована"; 
	
	String NOT_SUCCESS_EDIT_TASK_MSG = "Ошибка при редактировании задачи"; 

	String SUCCESS_RECORD_TASK_MSG = "Валидация пройдена, запись прошла без ошибок";
	
	String NOT_SUCCESS_RECORD_TASK_MSG = "Имя файла содержит недопустимые символы (\\/:*?\"<>)";
	
	String SUCCESS_READ_TASK_MSG = "Чтение прошло без ошибок";	

	
	String NOT_VERIFY_TITLE_MSG = "Невалидное название задачи";
	
	String NOT_VERIFY_DATE_MSG = "Невалидная дата";
	
	
	String DUPLICATE_TASK_MSG = "Задача с указанным именем существует, хотите ее перетереть текущей ('y'/'n')? ";
	
	String TASK_LIST_CLEARED_MSG = "Журнал задач очищен"; 
	
	String TASK_LIST_IS_EMPTY_MSG = "Список задач пуст"; 

	String STOPPED = "Работа планировщика остановлена";
	
	String NEW_LINE = "\n";
}
