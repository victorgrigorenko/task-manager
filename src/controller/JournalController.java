package controller;

import static constants.Exception.*;
import static constants.Command.*;
import constants.Command;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.xml.bind.JAXBException;

import model.Journal;
import model.Journalable;
import model.Task;



public class JournalController extends Observable implements JournalableController<Task> {
	private Journalable<Task> journal;

	// Список наблюдателей(подписчиков)
	private List<Observer> observers;

	private JournalController(Journalable<Task> journal) {
		observers = new ArrayList<>();
		this.journal = journal;
	}

	public static JournalController newInstance(Journalable<Task> journal) {

		return new JournalController(journal);
	}

	//**** методы для работы с Observers
	@Override
	public void addObserver(Observer o) {
		if(o != null)
			observers.add(o);
	}

	@Override
	public void deleteObserver(Observer o) {
		if(o != null)
			observers.remove(o);
	}
	
	@Override
	public void notifyObservers(Object arg) {
		for (Observer observer : observers){ // тут ловим NPE
			observer.update(this,arg); // здесь просто будем передавать сообщение
		}
	}
	

	// выплевывает енам на основании того, что мы ввели
	// т.к. неудобно вводить имя константы в точности каждый раз
	private Command returnCommandOfEnum(String command){
		Command cmd;
		String tmp = "";
		char[] chArr = command.trim().toCharArray();
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
	
	/// Весь код, что ниже забран из вьюхи,
	public void commandRead() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command;
		Command commanOfEnum;
		do { // передаем разные функции
			command = reader.readLine();
			commanOfEnum = returnCommandOfEnum(command);
			commandParse(commanOfEnum);
		} while (commanOfEnum != STOP);

	}

	// подумать, возможно оставить возвращение сообщения только в конкретных методах
	public String commandParse(Command command) throws IOException {
		String message = ""; 

		switch (command) {
			case ADD: message = addTask(); // добавление задачи
				break;
			case DEL: message = deleteTask(); // удаление задачи commandShowTasks
				break;
			case SEARCH: message = searchTask(); // поиск задачи
				break;
			case EDIT: message = editTask(); // редактирование задачи
				break;
			case SHOW_ALL: showAll(); // вывод всех задач
				break;
			case CLEAR_ALL: message = clearAll(); // удалить все задачи
				break;
			case RECORD: message = recordJournal(); // запись журнала задач в файл
				break;
			case READ: message = readJournal(); // чтение журнала задач из файла
				break;
			case HELP: message = help(); // справка
				break;
			case STOP: message = "Работа планировщика остановлена"; // "stop"
				break;
			case OTHER:
			default:
				message = "Вы ввели неверную команду. Попробуйте заново или воспользуйтесь справкой 'help'\n";
		}
		notifyObservers(message); // здесь сообщения для отдельного обсервера(не  журнале)
		return message;
	}
	private List<String> fillDateField(){ // метод для заполнения листа даты
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String command;
			List<String> listOfDate = new ArrayList<>();
			
			notifyObservers("Год: ");
			command = reader.readLine();
			listOfDate.add(command);
	
			notifyObservers("Месяц: ");
			command = reader.readLine();
			listOfDate.add(command);
	
			notifyObservers("День: ");
			command = reader.readLine();
			listOfDate.add(command);
	
			// если мы хотим задать еще и время
			notifyObservers("Часы: ");
			command = reader.readLine();
			listOfDate.add(command);
	
			notifyObservers("Минуты: ");
			command = reader.readLine();
			listOfDate.add(command);
	
			return listOfDate;

		} catch (IOException e) {
			notifyObservers(IO.toString()+"\n");
			return null;

		} catch (IllegalArgumentException e) {
			notifyObservers(ILLEGAL_ARGUMENT.toString()+"\n");
			return null;
		}
	}
	
	// преобразование входных данных в дату/время
	private Calendar setupDate(List<String> list, Calendar date){
		Calendar calendar = new GregorianCalendar();
		int year, month, day, hourOfDay, minute;

		if (list == null || list.isEmpty() || list.size()<5)
			notifyObservers("При заполнении даты/времени частично или полностью указаныне все поля");
		
		try{
			if (list.size()>4){ // если передали пустое значение, то устанавливаем текущий год/месяц/... 
				year 	= (list.get(0) != null && !list.get(0).isEmpty())? Integer.valueOf(list.get(0)): date.get(Calendar.YEAR); 	// NumberFormatException
				month 	= (list.get(1) != null && !list.get(1).isEmpty())? Integer.valueOf(list.get(1))-1: date.get(Calendar.MONTH); 	// т.к. дата с 0, а не с 1
				day 	= (list.get(2) != null && !list.get(2).isEmpty())? Integer.valueOf(list.get(2)): date.get(Calendar.DATE); 	// NumberFormatException
				hourOfDay = (list.get(3) != null && !list.get(3).isEmpty())? Integer.valueOf(list.get(3)): date.get(Calendar.HOUR);	// NumberFormatException
				minute 	= (list.get(4) != null && !list.get(4).isEmpty())? Integer.valueOf(list.get(4)): date.get(Calendar.MINUTE);	// NumberFormatException
				calendar.set(year, month, day, hourOfDay, minute);
			}
		} catch (NumberFormatException e) {
			notifyObservers("Неверный формат даты/времени\n");
		}		
		return calendar;
	}
	

	// вызов нужного метода add в зависимости от кол-ва входных параметров
	private boolean add(List<String> commandArgs) {
		// будем возвращать boolean, в зависимости от того успешно прошло добавление или нет
		Task task = null;
		if (commandArgs != null) {
			Calendar calendar;
			List<String> listOfDate = new ArrayList<>();
			if (commandArgs.size() == 7) { 
				// Дата с указанием времени
				for(int i=2; i<commandArgs.size();i++){
					listOfDate.add(commandArgs.get(i));
				}
				calendar = setupDate(listOfDate,new GregorianCalendar());
				task = journal.createTask(commandArgs.get(0), commandArgs.get(1), calendar);
			}	
			else 
				return false;
		}		
		return journal.addTask(task); 
	}

	@Override
	public String addTask() {
		String msg = "";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String command;
			List<String> commandArgs = new ArrayList<>();// сюда складываем аргументы
			notifyObservers("Для добавления задачи построчно вводите поля (название/описание/дата)\n");
			
			notifyObservers("Название: ");
			command = reader.readLine();
			if(journal.searchTask(command) == null){
				commandArgs.add(command);
			}
			else{ // задача почему то не перезаписывается.. календарь создавать здесь, в зависимости, от того перезаписываем задачу или просто добавляем
				notifyObservers("Задача с указанным именем существует, хотите ее перетереть текущей ('y'/'n')? ");
				char ch = (char)reader.read();
				if(ch == 'n' || ch == 'N')
					return null;
				commandArgs.add(command);
				reader.readLine();
			}				
			notifyObservers("Описание: ");
			command = reader.readLine();
			commandArgs.add(command);

			commandArgs.addAll(fillDateField()); // здесь заполняется дата 
			msg = (add(commandArgs))? "Задача успешно добалена \n": "Задача не была добавлена\n";

			return msg;

		} catch (IOException e) {
			return IO.toString()+"\n";

		} catch (IllegalArgumentException e) {
			return ILLEGAL_ARGUMENT.toString()+"\n";
		}
	}
	
	
	@Override
	public String deleteTask() { // так же возвращаем успех или нет, если не нашли нужную задачу
		String msg;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String title;

			// выводить на дисплей в контроллере нельзя!!!!!
			notifyObservers("Для удаления задачи введите имя: ");
			title = reader.readLine();

			msg = (journal.deleteTask(title))? 
					"Задача успешно удалена\n": 
					"Задачи с таким названием не существует\n";

		} catch (IOException e) {
			msg = IO.toString()+"\n";
		}

		return msg;
	}

	@Override
	public String searchTask() { // здесь выводим текстовое предсавление задачи
		String msg;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String title;

			notifyObservers("Для поиска задачи введите имя: ");
			title = reader.readLine();

			Task task = journal.searchTask(title);

			msg = (task != null)? "\n"+task.show() + "\n" : "Задачи с таким названием не существут\n";

		} catch (IOException e) {
			msg = IO.toString()+"\n";
		}

		return msg;
	}
	
	
	@Override // Редактирование задачи
	public String editTask(){ // так же возвращаем либо message or boolean
		String msg;
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			notifyObservers("Введите название задачи, которую хотите отредактировать: ");
			String title = reader.readLine();
			Task task = journal.searchTask(title);
			if(task == null){
				return "Такой задачи не существует\n";
			} 
			notifyObservers("Задача '"+task.getTitle()+"' найдена, отредактируем ее\n");

			notifyObservers("Название:  ");
			String editTitle = reader.readLine();  

			notifyObservers("Описание:  ");
			String editDescription = reader.readLine();  

			Calendar date = task.getDate(); // получаем дату
			Calendar editDate = setupDate(fillDateField(),date); // устанавливаем дату
//			(date != null && !date.isEmpty())?
//					new GregorianCalendar(): // как работать с датой?!!
//					null;  
			
			msg = ((journal.editTask(title, editTitle, editDescription, editDate))? 
					"Задача успешно отредактирована" : 
					"Ошибка при редактировании задачи");
			
		} catch(IOException e){
			msg = IO.toString();
		}
		
		return msg+"\n";
	}

	@Override
	public void showAll() {
		// хом хз как тут менять весь смысл же в выводе
//		String msg = (journal.getTasks().isEmpty()) ? 
//				"Список задач пуст" : 
//					journal.getTasks().toString();
//		return	msg+"\n";

		journal.showAll();
	}

	@Override // очистить журнал
	public String clearAll() {
		journal.clearTasks();
		return "Список задач очищен\n";
	}

	// присутствие в имени запрещенных символов
	private boolean validateFileName(String name){
		String ignoredSymbols = "\\/:*?\"<>|";
		for(int i=0; i<name.length(); i++){
			if (ignoredSymbols.contains(String.valueOf(name.charAt(i))))
				return false; 
		}
		return true;
	}
	@Override// запись в файл
	public String recordJournal() {
		String message;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			notifyObservers("Введите имя файла, в который хотите сохранить текущий список задач: ");
			String fileName = reader.readLine();
			// проверяем имя
			if	(validateFileName(fileName))
				message = (journal.recordJournal(fileName))? "Запись прошла без ошибок": "Не удалось записать";
			else
				message = "Имя файла содержит недопустимые символы (\\/:*?\"<>)";
			
		} catch (FileNotFoundException e) {
			message = FILE_NOT_FOUND.toString();

		} catch (IOException e) {
			message = IO.toString();

		} catch (JAXBException e) {
			message = JAXB_RECORD.toString();
		}
		return message+"\n";
	}

	@Override
	public String readJournal() {
		String message;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			notifyObservers("Введите имя файла, из которого хотите загрузить задачи: ");
			String fileName = reader.readLine();
			// вернуть список задач из указанного файла
			journal.replaceTasks((List<? extends Task>) journal.readJournal(fileName).getTasks());
			message = "Чтение прошло без ошибок";
			
		} catch (FileNotFoundException e) {
			message = FILE_NOT_FOUND.toString();

		} catch (IOException e) {
			message = IO.toString();

		} catch (JAXBException e) {
			message = JAXB_READ.toString();
		}
		return message+"\n";
	}

	// вывод справки
	private String help() {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("storage/help.txt"));
			String s;
			do {
				s = reader.readLine();
				if (s == null)
					break;
				sb.append(s+"\n");

			} while (true);

		} catch (FileNotFoundException e) {
			sb = new StringBuilder(FILE_NOT_FOUND.toString());

		} catch (IOException e) {
			sb = new StringBuilder(IO.toString());
		}
		
		return sb.toString()+"\n";
	}

}
