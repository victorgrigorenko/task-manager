package controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import model.Journal;
import model.Journalable;
import model.Task;
import model.Taskable;

public class JournalController<T extends Taskable> implements JournalableController<T> {
	private Journalable<T> journal;
	
//	//private T task;
//	private Taskable task;
	
	private JournalController(Journalable<T> journal){
		this.journal = journal;
	}
	
	public static JournalController<?> newInstance(Journalable<?> journal){
		return new JournalController<>(journal);
	}
	
	// Создаем таски здесь и проверки везде добавляем, что б в аргументах не было бяки
	@Override // задача будет добавляться всегда успешно, т.к. нет входных данных,
	public boolean addTask() { // в которых можно ошибиться, однако для удобства работы оставляем boolean
		T task = journal.createTask();
		journal.addTask(task);
		return (task != null)? true: false;
	}

	@Override 
	public boolean addTask(String title) { 
		T task = journal.createTask(title);
		journal.addTask(task);
		return (task != null)? true: false;
	}

	@Override
	public boolean addTask(String title, String description) {
		T task = journal.createTask(title, description);
		journal.addTask(task);
		return (task != null)? true: false;
	}

	@Override
	public boolean addTask(String title, Date date) {
		T task = journal.createTask(title, date);
		journal.addTask(task);
		return (task != null)? true: false;
	}

	@Override
	public boolean addTask(String title, String description, Date date) {
		T task = journal.createTask(title, description, date);
		journal.addTask(task);
		return (task != null)? true: false;
	}	
	
	//! Придумать возможно не полностью замену задачи, а редактирование полей задачи
	@Override // замена задачи из списка, на указанную в аргументе
	public boolean replaceTask(String title, T task){
		return journal.replaceTask(title, task);
	}	
	
	@Override
	public T searchTask(String title){
		return journal.searchTask(title);
	}

	@Deprecated
	@Override// возвращаем одну задачу
	public T getTask(int index){ 
		return journal.getTask(index);
	}
	@Override// возвращаем список задач
	public List<? extends T> getTasks(){
		return journal.getTasks();
	}

	@Override	// удаление по id/index
	@Deprecated //! применимость метода под вопросом
	public void deleteTask(int index){
		journal.deleteTask(index);
	}
	
	// удаление задачи по имени
	public boolean deleteTask(String title){
		return journal.deleteTask(title);
	}
	
	@Override	// очистить журнал
	public void clearTasks(){
		journal.clearTasks();
	}
	
	@Override	// сохранение
	public boolean recordJournal() throws JAXBException{
		return journal.recordJournal();
	}

	@Override
	public boolean recordJournal(String fileName) throws JAXBException{
		return journal.recordJournal(fileName);
	}
	
	@Override	// чтение
	public Journal<T> readJournal() throws JAXBException{
		return (Journal<T>) journal.readJournal();
	}
	
	@Override
	public Journal<T> readJournal(String fileName) throws JAXBException{
		return (Journal<T>) journal.readJournal(fileName);
	}

}
