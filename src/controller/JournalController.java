package controller;

import java.util.List;

import model.Journal;
import model.Journalable;
import model.Taskable;

public class JournalController<T extends Taskable> {
	private Journalable<T> journal;
	
	private JournalController(Journalable<T> journal){
		this.journal = journal;
	}
	
	public static JournalController<?> newInstance(Journalable<?> journal){
		return new JournalController<>(journal);
	}
	
	// добавить задачу
	public void addTask(T task){
		journal.addTask(task);
	}
	// добавить список задач
	public void addTasks(List<? extends T> list){
		journal.addTasks(list);
	}

	// замена задачи из списка, на указанную в аргументе
	public void replaceTask(int index, T task){
		journal.replaceTask(index, task);
	}	
	// замена всего списка задач
	public void replaseTasks(List<? extends T> list){
		journal.replaseTasks(list);
	}

	// возвращаем одну задачу
	public T getTask(int index){
		return journal.getTask(index);
	}
	// возвращаем список задач
	public List<? extends T> getTasks(){
		return journal.getTasks();
	}

	// удалить одну, определенную задачу
	public void deleteTask(int index){
		journal.deleteTask(index);
	}
	// очистить журнал
	public void deleteTasks(){
		journal.deleteTasks();
	}
	
	// сохранение
	public boolean recordJournal(){
		return journal.recordJournal();
	}

	public boolean recordJournal(String fileName){
		return journal.recordJournal(fileName);
	}
	
	// чтение
	public Journal<?> readJournal(){
		return (Journal<?>) journal.readJournal();
	}
	
	public Journal<?> readJournal(String fileName){
		return (Journal<?>) journal.readJournal(fileName);
	}
	
}
