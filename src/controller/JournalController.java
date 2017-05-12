package controller;

import static constants.Exception.*;
import static constants.Command.*;
import constants.Command;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import model.Journalable;
import model.Task;
import model.XMLJournal;
import model.XMLJournalible;



public class JournalController extends Observable implements JournalableController<Task> {
	private Journalable<Task> journal;
	private XMLJournalible<Task> xml = new XMLJournal();
	private String fileRg, dateRg, titleRg;
	
	BufferedReader reader;

	// Список наблюдателей(подписчиков)
	private List<Observer> observers;

	private JournalController(Journalable<Task> journal) {
		observers = new ArrayList<>();
		reader = new BufferedReader(new InputStreamReader(System.in));
		fileRg	= "[^\\/:*?\"<|>]+"; // шаблон для именования файла
		dateRg 	= "[\\d\\s\\.:]+"; // шаблон даты
		titleRg	= "(([\\w\\s\\а-яА-Я?!:;,.'\"&@№$%()_-]+))"; // шаблон для названия таски 

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

	
	/// Весь код, что ниже забран из вьюхи,
	public void commandRead() throws IOException {
		String command;
		Command commanOfEnum;
		do { // передаем разные функции
			command = reader.readLine();
			commanOfEnum = Command.valueParse(command);
			commandParse(commanOfEnum);
		} while (commanOfEnum != STOP);
		reader.close();
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
			case SHOW_ALL: message = showAll(); // вывод всех задач
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
	
	private Date setupDate() throws IOException, ParseException{ // метод для заполнения даты
		SimpleDateFormat format = new SimpleDateFormat("d.MM.yyyy hh:mm");
		notifyObservers("Введите дату и время (D.MM.YYYY hh:mm): ");
		String dateString = reader.readLine();
		if (!validateField(dateString,dateRg)) notifyObservers("Невалидная дата\n");

		return format.parse(dateString);
	}
	

	@Override
	public String addTask() {
		String msg = "";
		try {
			notifyObservers("Для добавления задачи построчно вводите поля (название/описание/дата)\n");
			
			notifyObservers("Название: ");
			String title = reader.readLine();
			if (!validateField(title,titleRg)) return "Невалидное имя задачи при добавлении\n";

			if(journal.searchTask(title) != null){
				notifyObservers("Задача с указанным именем существует, хотите ее перетереть текущей ('y'/'n')? ");
				char ch = (char)reader.read();
				if(ch == 'n' || ch == 'N'){
					return null;
				}
				reader.readLine();
			}				
			notifyObservers("Описание: ");
			String description = reader.readLine();

			Task task = journal.createTask(title, description, setupDate());
			if (task != null){
				journal.addTask(task);
				msg = "Задача успешно добалена \n";
			}
			else
				msg = "Задача не была добавлена\n";
			return msg;

		} catch (ParseException e) {
			return PARSE.toString()+"\n"; 

		} catch (IllegalArgumentException e) {
			return ILLEGAL_ARGUMENT.toString()+"\n";

		} catch (IOException e) {
			return IO.toString()+"\n";
		}
	}
	
	
	@Override
	public String deleteTask() { // так же возвращаем успех или нет, если не нашли нужную задачу
		String msg;
		try {
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
		String msg, title, editTitle, editDescription;
		title = editTitle = editDescription = "";
		Task task = null;
		try{

			notifyObservers("Введите название задачи, которую хотите отредактировать: ");
			title = reader.readLine();
			task = journal.searchTask(title);
			if(task == null){
				return "Такой задачи не существует\n";
			} 
			notifyObservers("Задача '"+task.getTitle()+"' найдена, отредактируем ее\n");

			notifyObservers("Название:  ");
			editTitle = reader.readLine();  
			if (!validateField(editTitle,titleRg)) notifyObservers("Невалидное название задачи при редактировании\n");


			notifyObservers("Описание:  ");
			editDescription = reader.readLine();  

			msg = ((journal.editTask(title, editTitle, editDescription, setupDate()))? 
					"Задача успешно отредактирована" : 
					"Ошибка при редактировании задачи");
			
		} catch (ParseException e) {
			Date oldDate = task.getDate(); // получаем старую дату
			msg = ((journal.editTask(title, editTitle, editDescription, oldDate))? 
					"Задача успешно отредактирована" : 
					"Ошибка при редактировании задачи");
			msg += "\n"+PARSE.toString();

		} catch(IOException e){
			msg = IO.toString();
		}
		
		return msg+"\n";
	}

	@Override
	public String showAll() {
		// хом хз как тут менять весь смысл же в выводе
		String msg = (journal.getTasks().isEmpty()) ? 
				"Список задач пуст" : 
					journal.getTasks().toString();
		return	msg+"\n";

		//journal.showAll();
	}

	@Override // очистить журнал
	public String clearAll() {
		journal.clearTasks();
		return "Список задач очищен\n";
	}

	
	// валидация поля
	private boolean validateField(String field, String pattern){
		Pattern dataRg 	= Pattern.compile(pattern); 
		Matcher matcher	= dataRg.matcher(field);
		return (matcher.matches());
	}

	@Override// запись в файл
	public String recordJournal() {
		String message;
		try {
			notifyObservers("Введите имя файла, в который хотите сохранить журнал: ");
			String fileName = reader.readLine();

			// проверяем имя
			if	(validateField(fileName,fileRg)){
				 xml.recordJournal(journal, fileName);
				message = "Валидация пройдена, запись прошла без ошибок";
			}
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
			notifyObservers("Введите имя файла, из которого хотите загрузить задачи: ");
			String fileName = reader.readLine();
			// вернуть список задач из указанного файла
			journal.replaceTasks(xml.readJournal(journal,fileName).getTasks());
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
		try (
				BufferedReader reader = new BufferedReader(new FileReader("storage/help.txt"));
			){
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

	@Override
	public void start() {
		try {
			System.out.println("Cтарт: ");
			this.commandRead();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println(IO.toString());
		}

	}
	
	

}
