package view;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import controller.JournalController;
import model.Journal;

import model.Taskable;

public class View<T extends Taskable> {
	Journal<T> model;
	JournalController<T> controller;
	
	public View(Journal<? extends T> model) {
		this.model = (Journal<T>) model;//model;//new Journal<>(new JournalObserver());
		controller = (JournalController<T>) JournalController.newInstance(model);
		this.model.addObserver(new JournalObserver());

	}
	
	public void start() throws IOException{
		commandRead();
	}
	
	public void commandRead() throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command;
		
		do{ //передаем разные функции
			command = reader.readLine();
			
		}while(commandParse(command));
		
	}
	
	public boolean commandParse(String command, String...commandArgs) throws IOException{
		boolean run = true;
		switch(command) {
			case "add": add(); // добавление задачи
				break;
			case "del": del(); // удаление задачи commandShowTasks
				break;
			case "search": search(); // поиск задачи
				break;
			case "show all": showAll(); // вывод всех задач
				break;
			case "clear all": clearAll(); // удалить все задачи
				break;
			case "record": record();
				break;
			case "read": read(); 
//					System.out.println("Функция чтения из файла пока отсутствует");
				break;
			case "help": help();
				break;
			case "stop":
					run = false;
					System.out.println("Работа планировщика остановлена");
				break;				
			default: System.out.println("Вы ввели неверную команду. Попробуйте заново или воспользуйтесь справкой 'help'");
		}
		return run;
	}
	
	// т.к. у нас несколько вариантов добавления, то будем еще раз считывать, 
	private boolean add(){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String command;
			List<String> commandArgs = new ArrayList<>();// сюда складываем аргументы
			System.out.println("Для добавления задачи построчно вводите поля (название/описание/дата),\n "
					+ "поля не являются обязательными. По завершению ввода наберите 'ok' с новой строки");
			do{ // лучше заюзать счетчик по строкам и выкидывать если более 3-х, иначе это может быть бесконечно
				command = reader.readLine();

				if (!command.equals("ok")) // здесь проверка на 'ок', либо > 3 строк
					commandArgs.add(command);
				else{
					// здесь должны вызвать подходящий метод (в нашем случае только при добавлении задач множество вариаций)
					addTask(commandArgs);
					break;
				}
			}while(true);
			return true;

		} catch (IOException e) {
			e.getMessage();
			return false;

		} catch(IllegalArgumentException e){
			System.out.println("Неверно задан формат даты");
			return false;
		}
	}	
	
	
	// вызов нужного метода Add в зависимости от кол-ва входных параметров
	private boolean addTask(List<String> commandArgs){ 
		boolean run = false;
		if (commandArgs != null){
			switch(commandArgs.size()) {
				case 0: run = controller.addTask();
					break;
				case 1: run = controller.addTask(commandArgs.get(0));
					break;
					// тут нужно как то определять какая это добавлялка: с датой или описанием
				case 2: run = controller.addTask(commandArgs.get(0), commandArgs.get(1)); 
					break;
				case 3: run = controller.addTask(commandArgs.get(0), commandArgs.get(1), Date.valueOf(commandArgs.get(2))); 
					break;
	
				default: System.out.println("Вы ввели неверную команду. Попробуйте заново или воспользуйтесь справкой 'help'");
			}
		}
		return run;
	}
	
	private boolean del(){
		boolean success = false; // 
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String title;
	
			System.out.print("Для удаления задачи введите имя: ");
			title = reader.readLine();
	
			success = controller.deleteTask(title);
			
			String message = success? "Задача успешно удалена" :"Задачи с таким названием не существут";
			System.out.println(message);
		
		} catch (IOException e) {
			e.getMessage();
			return false;
		}		
		return success;
	}

	private boolean search(){
		boolean success = false; 
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String title;
	
			System.out.print("Для поиска задачи введите имя: ");
			title = reader.readLine();
	
			T task = controller.searchTask(title);
			success = (task != null)? true: false; 
		
			String message = success? task.show()+"\n" :"Задачи с таким названием не существут"+"\n";
			System.out.println(message);

		} catch(IOException e){
			e.getMessage();
			return false;
		}		
		return success;
	}

	private void showAll(){
		// пока так
		
		System.out.println((controller.getTasks().isEmpty())?
				"Список задач пуст":
				controller.getTasks());
	}

	private void clearAll(){
		controller.clearTasks();
		System.out.println("Список задач очищен");
	}

	// запись в файл
	private void record(){
		String message = "Запись прошла без ошибок";
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Введите имя файла, в который хотите сохранить текущий список задач: ");
			String fileName = reader.readLine();
			// запишется в любом случае, даже если NPE не пройдет =) p.s. скорее всего)
			controller.recordJournal(fileName);
			
		} catch (FileNotFoundException e) {
			e.getMessage();
			message = "Файл не найден";
			
		} catch (IOException e) {
			e.getMessage();
			message = "Ошибка ввода/вывода";

		} catch (JAXBException e){
			e.getMessage();
			message = "Ошибка записи xml";
		}
		System.out.println(message);
	}

	private void read(){
		String message = "Чтение прошло без ошибок";
		try{	
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Введите имя файла, из которого хотите загрузить задачи: ");
			String fileName = reader.readLine();
			// вернуть список задач из указанного файла
			model.replaceTasks((List<? extends T>) controller.readJournal(fileName).getTasks());//! проблема ClassCastException
			
		} catch (FileNotFoundException e) {
			e.getMessage();
			message = "Файл не найден";

		} catch (IOException e) {
			e.getMessage();
			message = "Ошибка ввода/вывода";

		} catch (JAXBException e){
			e.getMessage();
			message = "Ошибка чтения xml";
		}
		System.out.println(message);
	}

	private void help(){
		try(
			BufferedReader reader = new BufferedReader(new FileReader("storage/help.txt"))
		){
			String s;
			do{
				s = reader.readLine();
				if (s == null) 
					break;				
				System.out.println(s);				
			}while(true);
			
		} catch (FileNotFoundException e) {
			e.getMessage();
			
		} catch (IOException e) {
			e.getMessage();
		}
	}

}
