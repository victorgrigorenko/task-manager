package controller;

import static constants.Exception.*;
import static constants.Command.*;
import static constants.Patterns.*;
import static constants.Constants.*;
import static constants.ConstantMessage.*;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import model.Journalable;
import model.Task;
import model.XMLJournal;
import model.XMLJournalible;
import observer.*;


public class JournalController implements JournalableController<Task> {
	private Journalable<Task> journal;
	private XMLJournalible<Task> xml = new XMLJournal();
	
	BufferedReader reader;

	private List<Observer> observers;

	private JournalController(Journalable<Task> journal) {
		observers = new ArrayList<>();
		reader = new BufferedReader(new InputStreamReader(System.in));

		this.journal = journal;
	}

	public static JournalController newInstance(Journalable<Task> journal) {

		return new JournalController(journal);
	}

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
		for (Observer observer : observers){ 
			observer.update(this,arg); 
		}
	}
	

	public void commandRead() throws IOException {
		String command;
		Command commanOfEnum;
		do { 
			command = reader.readLine();
			commanOfEnum = Command.valueParse(command);
			commandParse(commanOfEnum);
		} while (commanOfEnum != STOP);
		reader.close();
	}

	public String commandParse(Command command) throws IOException {
		String message = NONE; 

		switch (command) {
			case ADD: message = addTask(); 
				break;
			case DEL: message = deleteTask();
				break;
			case SEARCH: message = searchTask();
				break;
			case EDIT: message = editTask(); 
				break;
			case SHOW_ALL: message = showAll(); 
				break;
			case CLEAR_ALL: message = clearAll();
				break;
			case RECORD: message = recordJournal();
				break;
			case READ: message = readJournal();
				break;
			case HELP: message = help(); 
				break;
			case STOP: message = STOPPED; 
				break;
			case OTHER:
			default:
				message = INFORM_MSG+NEW_LINE;
		}
		notifyObservers(message); 
		return message;
	}
	
	
	private Date setupDate() throws IOException, ParseException{ 
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		notifyObservers(DATE);
		String dateString = reader.readLine();
		if (!validateField(dateString,DATE_RG)) notifyObservers(NOT_VERIFY_DATE_MSG+NEW_LINE);

		return format.parse(dateString);
	}
	

	@Override
	public String addTask() {
		String msg = NONE;
		try {
			notifyObservers(ADD_MSG+NEW_LINE);
			
			notifyObservers(TITLE);
			String title = reader.readLine();
			if (!validateField(title,TITLE_RG)) return NOT_VERIFY_TITLE_MSG+NEW_LINE;

			if(journal.searchTask(title) != null){
				notifyObservers(DUPLICATE_TASK_MSG);
				char ch = (char)reader.read();
				if(ch == 'n' || ch == 'N'){ 
					return null;
				}
				reader.readLine();
			}				
			notifyObservers(DESCRIPTION);
			String description = reader.readLine();

			Task task = journal.createTask(title, description, setupDate());
			if (task != null){
				journal.addTask(task);
				msg = SUCCESS_ADD_TASK_MSG+NEW_LINE;
			}
			else
				msg = NOT_SUCCESS_ADD_TASK_MSG+NEW_LINE;
			return msg;

		} catch (ParseException e) {
			return PARSE.toString()+NEW_LINE; 

		} catch (IllegalArgumentException e) {
			return ILLEGAL_ARGUMENT.toString()+NEW_LINE;

		} catch (IOException e) {
			return IO.toString()+NEW_LINE;
		}
	}
	
	
	@Override
	public String deleteTask() { 
		String msg;
		try {
			String title;

			notifyObservers(DEL_MSG);
			title = reader.readLine();

			msg = (journal.deleteTask(title))? 
					SUCCESS_DEL_TASK_MSG+NEW_LINE: 
					NOT_SUCCESS_SEARCH_TASK_MSG+NEW_LINE;

		} catch (IOException e) {
			msg = IO.toString()+NEW_LINE;
		}

		return msg;
	}

	@Override
	public String searchTask() { 
		String msg;
		try {
			String title;

			notifyObservers(SEARCH_MSG);
			title = reader.readLine();

			Task task = journal.searchTask(title);

			msg = (task != null)? NEW_LINE+task.show() + NEW_LINE : NOT_SUCCESS_SEARCH_TASK_MSG+NEW_LINE;

		} catch (IOException e) {
			msg = IO.toString()+NEW_LINE;
		}

		return msg;
	}
	
	
	@Override 
	public String editTask(){ 
		String msg, title, editTitle, editDescription;
		title = editTitle = editDescription = NONE;
		Task task = null;
		try{

			notifyObservers(EDIT_MSG);
			title = reader.readLine();
			task = journal.searchTask(title);
			if(task == null){
				return NOT_SUCCESS_SEARCH_TASK_MSG+NEW_LINE;
			} 
			notifyObservers("Задача '"+task.getTitle()+"' найдена, отредактируем ее\n");

			notifyObservers(TITLE);
			editTitle = reader.readLine();  
			if (!validateField(editTitle,TITLE_RG)) notifyObservers(NOT_VERIFY_TITLE_MSG+NEW_LINE);

			notifyObservers(DESCRIPTION);
			editDescription = reader.readLine();  

			msg = ((journal.editTask(title, editTitle, editDescription, setupDate()))? 
					SUCCESS_EDIT_TASK_MSG : 
					NOT_SUCCESS_EDIT_TASK_MSG);
			
		} catch (ParseException e) {
			Date oldDate = task.getDate();
			msg = ((journal.editTask(title, editTitle, editDescription, oldDate))? 
					SUCCESS_EDIT_TASK_MSG : 
					NOT_SUCCESS_EDIT_TASK_MSG);
			msg += NEW_LINE + PARSE.toString();

		} catch(IOException e){
			msg = IO.toString();
		}
		
		return msg + NEW_LINE;
	}

	@Override
	public String showAll() {
		String msg = (journal.getTasks().isEmpty()) ? 
				TASK_LIST_IS_EMPTY_MSG : 
				journal.getTasks().toString();
		return	msg+NEW_LINE;
	}

	@Override 
	public String clearAll() {
		journal.clearTasks();
		return TASK_LIST_CLEARED_MSG+NEW_LINE;
	}

	
	private boolean validateField(String field, String pattern){
		Pattern dataRg 	= Pattern.compile(pattern); 
		Matcher matcher	= dataRg.matcher(field);
		return (matcher.matches());
	}

	@Override
	public String recordJournal() {
		String message;
		try {
			notifyObservers(RECORD_MSG);
			String fileName = reader.readLine();

			if	(validateField(fileName,FILE_RG)){
				 xml.recordJournal(journal, fileName);
				message = SUCCESS_RECORD_TASK_MSG;
			}
			else
				message = NOT_SUCCESS_RECORD_TASK_MSG;
			
		} catch (FileNotFoundException e) {
			message = FILE_NOT_FOUND.toString();

		} catch (IOException e) {
			message = IO.toString();

		} catch (JAXBException e) {
			message = JAXB_RECORD.toString();
		}
		return message+NEW_LINE;
	}
	
	@Override
	public String readJournal() {
		String message;
		try {
			notifyObservers(READ_MSG);
			String fileName = reader.readLine();
			journal.replaceTasks(xml.readJournal(journal,fileName).getTasks());
			message = SUCCESS_READ_TASK_MSG;
			
		} catch (FileNotFoundException e) {
			message = FILE_NOT_FOUND.toString();	

		} catch (IOException e) {
			message = IO.toString();

		} catch (JAXBException e) {
			message = JAXB_READ.toString();
		}
		return message+NEW_LINE;
	}


	private String help() {
		StringBuilder sb = new StringBuilder();
		try (
				BufferedReader reader = new BufferedReader(new FileReader(HELP_FILE))
			){
				String s;
			do {
				s = reader.readLine();
				if (s == null)
					break;
				sb.append(s+NEW_LINE);

			} while (true);

		} catch (FileNotFoundException e) {
			sb = new StringBuilder(FILE_NOT_FOUND.toString());

		} catch (IOException e) {
			sb = new StringBuilder(IO.toString());
		}
		
		return sb.toString()+NEW_LINE;
	}

	@Override
	public void start() {
		try {
			notifyObservers(START+NEW_LINE);
			this.commandRead();
		} catch (IOException e) {
			notifyObservers(IO.toString()+NEW_LINE);
		}
	}
}
