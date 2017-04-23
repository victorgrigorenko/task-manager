package controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import model.Journal;
import model.Journalable;
import model.Task;
import model.Taskable;

public class JournalController<T extends Taskable> implements JournalableController<T> {
	private Journalable<T> journal;
	
	//private T task;
	private Taskable task;
	
	private JournalController(Journalable<T> journal){
		task = new Task(); //! Убивает всю абстракию, но как иначе сделать пока неясно
		this.journal = journal;
	}
	
	public static JournalController<?> newInstance(Journalable<?> journal){
		return new JournalController<>(journal);
	}
	
	// Создаем таски здесь и проверки везде добавляем, что б в аргументах не было бяки
	@Override // задача будет добавляться всегда успешно, т.к. нет входных данных,
	public boolean addTask() { // в которых можно ошибиться, однако для удобства работы оставляем boolean
//		try { // попробовать рефлексивно вытащить нужный метод
//			T tmp =(T) task.getClass().getDeclaredConstructor(task.getClass()).newInstance();
//			journal.addTask((T)  tmp.createTask());//getMethod("createTask").invoke(task.getClass().newInstance()));
//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
//				| SecurityException | InstantiationException e) {
//
//			e.printStackTrace();
//		}	
		journal.addTask((T) task.createTask());
		return true;
	}

	/*! Сейчас task == null, поэтому все крашится, т.е. нам изначально придется создать экземпляр конкретного класса Task
	 * и тогда уже использовать createTask. 
	 * Либо добавить статик методы для генерации таски непосредственно в класс Task.
	 * И первый и второй вариант не камельфо, однако первый более предпочтителен
	 */
	@Override 
	public boolean addTask(String title) { 
		if(title!=null && !title.trim().isEmpty()){ 
			journal.addTask((T) ((Task)task).create(title));
			return true;
		}
		return false;
	}

	@Override
	public boolean addTask(String title, String description) {
		if(title!=null && !title.trim().isEmpty() && description!=null){
			journal.addTask((T) task.createTask(title, description));
			return true;
		}
		return false;
	}

	@Override
	public boolean addTask(String title, Date date) {
		if(title!=null && !title.trim().isEmpty() && date!=null){
			journal.addTask((T) task.createTask(title, date));
			return true;
		}
		return false;
	}

	@Override
	public boolean addTask(String title, String description, Date date) {
		if(title!=null && !title.trim().isEmpty() && description!=null && date!=null){
			journal.addTask((T) task.createTask(title, description,date));
			return true;
		}
		return false;
	}	
	

	@Override // замена задачи из списка, на указанную в аргументе
	public boolean replaceTask(String title, T task){
		if(title != null && !title.trim().isEmpty() && task != null)
			return journal.replaceTask(title, task);
		return false;
	}	
	
	@Override
	public T searchTask(String title){
		return (title != null && !title.trim().isEmpty())?
				journal.searchTask(title):
				null;		
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
		return (title != null && !title.trim().isEmpty())?
				journal.deleteTask(title):
				false;
	}
	
	@Override	// очистить журнал
	public void clearTasks(){
		journal.clearTasks();
	}
	
	@Override	// сохранение
	public boolean recordJournal(){
		return journal.recordJournal();
	}

	@Override
	public boolean recordJournal(String fileName){
		return (fileName != null && !fileName.trim().isEmpty())?
				journal.recordJournal(fileName):
				false;
	}
	
	@Override	// чтение
	public Journal<?> readJournal(){
		return (Journal<?>) journal.readJournal();
	}
	
	@Override
	public Journal<?> readJournal(String fileName){
		return (fileName != null && !fileName.trim().isEmpty())?
				(Journal<?>) journal.readJournal(fileName):
				null;
	}

}
