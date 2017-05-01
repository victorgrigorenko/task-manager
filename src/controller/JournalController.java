package controller;

import static constants.Exception.FILE_NOT_FOUND;
import static constants.Exception.ILLEGAL_ARGUMENT;
import static constants.Exception.IO;
import static constants.Exception.JAXB_READ;
import static constants.Exception.JAXB_RECORD;

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

import constants.Command;

import model.Journal;
import model.Journalable;
import model.Task;



public class JournalController extends Observable implements JournalableController<Task> {
	private Journalable<Task> journal;

	private String message; // сообщение, которое мы будем передавать на дисплей
	
	// Список наблюдателей(подписчиков)
	private List<Observer> observers;

	private JournalController(Journalable<Task> journal) {
		observers = new ArrayList<>();
		this.journal = journal;
		this.addObserver((Observer) journal); // добавляем наблюдателя (это наша модель)
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
	
	//****		
	
	// Создаем таски здесь и проверки везде добавляем, что б в аргументах не
	// было бяки
	@Override // задача будет добавляться всегда успешно, т.к. нет входных данных,
	public boolean addTask() { // в которых можно ошибиться, однако для удобства
								// работы оставляем boolean
		Task task = journal.createTask();
		journal.addTask(task);
		return (task != null) ? true : false;
	}

	@Override
	public boolean addTask(String title, String description, Calendar date) {
		Task task = journal.createTask(title, description, date);
		journal.addTask(task);
		return (task != null) ? true : false;
	}

	// ! Придумать возможно не полностью замену задачи, а редактирование полей
	// задачи
	@Override // замена задачи из списка, на указанную в аргументе
	public boolean replaceTask(String title, Task task) {
		return journal.replaceTask(title, task);
	}

	@Override
	public Task searchTask(String title) {
		return journal.searchTask(title);
	}

	@Override // возвращаем список задач
	public List<? extends Task> getTasks() {
		return journal.getTasks();
	}

	// удаление задачи по имени
	public boolean deleteTask(String title) {
		return journal.deleteTask(title);
	}

	@Override // очистить журнал
	public void clearTasks() {
		journal.clearTasks();
	}

	@Override // сохранение
	public boolean recordJournal() throws JAXBException {
		return journal.recordJournal();
	}

	@Override
	public boolean recordJournal(String fileName) throws JAXBException {
		return journal.recordJournal(fileName);
	}

	@Override // чтение
	public Journal readJournal() throws JAXBException {
		return (Journal) journal.readJournal();
	}

	@Override
	public Journal readJournal(String fileName) throws JAXBException {
		return (Journal) journal.readJournal(fileName);
	}

	@Override
	public boolean editTask(String title, String editTitle, String editDescription, Calendar editDate) {
		return journal.editTask(title, editTitle, editDescription, editDate);
	}

	/// Весь код, что ниже забран из вьюхи,
	public void commandRead() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command;

		do { // передаем разные функции
			command = reader.readLine();

		} while (!commandParse(command).equals("stop"));

	}

	private Command commandReturn(String command){
		Command cmd;
		String tmp = "";
		char[] chArr = command.toCharArray();
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
	
	// подумать, возможно оставить возвращение сообщения только в конкретных методах
	public String commandParse(String command) throws IOException {
		String message = ""; 

//		switch (command) {
		switch (commandReturn(command)) {
			case ADD: message = add(); // добавление задачи
				break;
			case DEL: message = del(); // удаление задачи commandShowTasks
				break;
			case SEARCH: message = search(); // поиск задачи
				break;
			case EDIT: message = edit(); // редактирование задачи
				break;
			case SHOW_ALL: message = showAll(); // вывод всех задач
				break;
			case CLEAR_ALL: message = clearAll(); // удалить все задачи
				break;
			case RECORD: message = record(); // запись журнала задач в файл
				break;
			case READ: message = read(); // чтение журнала задач из файла
				break;
			case HELP: message = help(); // справка
				break;
			case STOP: message = "stop";
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
	
			notifyObservers("Вы можете прекратить ввод, набрав 'OK': ");
			command = reader.readLine();
			if (command.toUpperCase().equals("OK")) 
				return listOfDate;
			else {
				notifyObservers("Продолжим ввод..\n");
			}
	
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
	private Calendar setupDate(List<String> list){
		Calendar calendar = new GregorianCalendar();
		int year, month, day, hourOfDay, minute;

		if (list == null || list.isEmpty() || list.size()<3){
			notifyObservers("При заполнении даты/времени указаны не все поля");
			return calendar;
		}
		
		try{
			if (list.size()>2 && list.size()<5){ // только дата, без времени
				year 	= (list.get(0) != null)? Integer.valueOf(list.get(0)): Calendar.YEAR; 	// NumberFormatException
				month 	= (list.get(1) != null)? Integer.valueOf(list.get(1))-1: Calendar.MONTH; // т.к. дата с 0, а не с 1
				day 	= (list.get(2) != null)? Integer.valueOf(list.get(2)): Calendar.DATE; 	// NumberFormatException
				calendar.set(year, month, day);
			}
			
			if (list.size()>4){ // только дата, без времени
				year 	= (list.get(0) != null)? Integer.valueOf(list.get(0)): Calendar.YEAR; 	// NumberFormatException
				month 	= (list.get(1) != null)? Integer.valueOf(list.get(1))-1: Calendar.MONTH; 	// NumberFormatException
				day 	= (list.get(2) != null)? Integer.valueOf(list.get(2)): Calendar.DATE; 	// NumberFormatException
				hourOfDay = (list.get(3) != null)? Integer.valueOf(list.get(3)): Calendar.HOUR;	// NumberFormatException
				minute 	= (list.get(4) != null)? Integer.valueOf(list.get(4)): Calendar.MINUTE;	// NumberFormatException
				calendar.set(year, month, day, hourOfDay, minute);
			}
		} catch (NumberFormatException e) {
			notifyObservers("Неверный формат даты/времени");
		}
		
		return calendar;
	}

	//Display()
	private String add() {
		String msg = "";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String command;
			List<String> commandArgs = new ArrayList<>();// сюда складываем
															// аргументы
			notifyObservers("Для добавления задачи построчно вводите поля (название/описание/дата)\n");
			
			notifyObservers("Название: ");
			command = reader.readLine();			
			commandArgs.add(command);
			
			notifyObservers("Описание: ");
			command = reader.readLine();
			commandArgs.add(command);

			// здесь заполняется дата
			commandArgs.addAll(fillDateField());
			msg = (addTask(commandArgs))? "Задача успешно добалена \n": "Задача не была добавлена\n";

			return msg;

		} catch (IOException e) {
			return IO.toString()+"\n";

		} catch (IllegalArgumentException e) {
			return ILLEGAL_ARGUMENT.toString()+"\n";
		}
	}
	
	
	// вызов нужного метода add в зависимости от кол-ва входных параметров
	private boolean addTask(List<String> commandArgs) {
		// будем возвращать boolean, в зависимости от того успешно прошло добавление или нет
		// или же можно возвращать сообщение
		Task task = null;
		if (commandArgs != null) {
			Calendar calendar;
			List<String> listOfDate = new ArrayList<>();
			switch (commandArgs.size()) { // это временно, потом сделаю по-человечьи
				case 5: // Если задаем только дату, без указания времени
					listOfDate.add(commandArgs.get(2)); // не каеф так, но пока так
					listOfDate.add(commandArgs.get(3));
					listOfDate.add(commandArgs.get(4));
					calendar = setupDate(listOfDate);
					task = journal.createTask(commandArgs.get(0), commandArgs.get(1), calendar);
					break;
				case 7: // Дата с указанием времени
					listOfDate.add(commandArgs.get(2));
					listOfDate.add(commandArgs.get(3));
					listOfDate.add(commandArgs.get(4));
					listOfDate.add(commandArgs.get(5));
					listOfDate.add(commandArgs.get(6));
					calendar = setupDate(listOfDate);
					task = journal.createTask(commandArgs.get(0), commandArgs.get(1), calendar);
					break;
	
				default:
					return false;
			}
		}		
		return journal.addTask(task);
	}

	
	private String del() { // так же возвращаем успех или нет, если не нашли нужную задачу
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

	//Display()
	private String search() { // здесь выводим текстовое предсавление задачи
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
	
	//!
	private String edit(){ // так же возвращаем либо message or boolean
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

//			Calendar editDate = task.getDate(); // получаем дату
			Calendar date = setupDate(fillDateField()); // устанавливаем дату
//			(date != null && !date.isEmpty())?
//					new GregorianCalendar(): // как работать с датой?!!
//					null;  
			
			msg = ((journal.editTask(title, editTitle, editDescription, date))? 
					"Задача успешно отредактирована" : 
					"Ошибка при редактировании задачи");
			
		} catch(IOException e){
			msg = IO.toString();
		}
		
		return msg+"\n";
	}

	//Display()
	private String showAll() {
		// хом хз как тут менять весь смысл же в выводе
		String msg = (journal.getTasks().isEmpty()) ? 
				"Список задач пуст" : 
					journal.getTasks().toString();
		return	msg+"\n";
	}

	//Display()
	private String clearAll() {
		journal.clearTasks();
		return "Список задач очищен\n";
	}

	// запись в файл
	private String record() {
		String message;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			notifyObservers("Введите имя файла, в который хотите сохранить текущий список задач: ");
			String fileName = reader.readLine();
			// запишется в любом случае, NPE не пройдет =) p.s. скорее всего)
			journal.recordJournal(fileName);
			message = "Запись прошла без ошибок";
			
		} catch (FileNotFoundException e) {
			message = FILE_NOT_FOUND.toString();

		} catch (IOException e) {
			message = IO.toString();

		} catch (JAXBException e) {
			message = JAXB_RECORD.toString();
		}
		return message+"\n";
	}

	//
	private String read() {
		String message;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			notifyObservers("Введите имя файла, из которого хотите загрузить задачи: ");
			String fileName = reader.readLine();
			// вернуть список задач из указанного файла
			journal.replaceTasks((List<? extends Task>) journal.readJournal(fileName).getTasks());// !
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

	//Display()
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
