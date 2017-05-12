package model;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


//возможно стоило использовать PROPERTY, и объявить геттер/сеттер пары для корректной
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE) // записи чтения  
@XmlType(name = "journalOfTask")
public class Journal extends Observable implements Journalable<Task>, Observer{

	
	// ставим required, теперь если не найдет List в xml, то будет ругаться
	@XmlElement(required = true, name = "task")//, type =Task.class) //ОК, JAXB хочет класс, поэтому пока оставим так, но ЭТО НЕ КРУТО! 
	@XmlElementWrapper(name = "tasks") // для группировки коллекции в подтег
	private List<Task> taskList = new ArrayList<>();

	// Список наблюдателей(подписчиков)
	private List<Observer> observers;

	public Journal(){//Observer o){
		observers = new ArrayList<>();
		//this.addObserver(o);
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
		for (Observer observer : observers) 
			observer.update(this,arg); // второй аргумент в true, только если хотим вывести список задач 
	}
	

	@Override
	public Task createTask(String title, String desc, Date date) {
		if(title!=null && !title.isEmpty() && desc!=null && date!=null){
			return new Task(title, desc, date);
		}
		
		return null;//new Task(); // быстрое добавление задачи, если поля пустые
	}

	
	@Override
	public boolean addTask(Task task){
		boolean result;
		Task t = returnReferenceOnTask(task.getTitle());
		if (t != null) taskList.remove(t); // на случай если нашли задачу с таким же именем, удаляем старую..
		result = (taskList.add(task))? true: false;
//		notifyObservers(false);
		return result;
	}

	@Override
	public void addTasks(List<? extends Task> list) {
		if (list !=null && !list.isEmpty()){ // нужна ли здесь проверка, ведь в контроллер не выносим данный метод
			taskList.addAll(list);
//			notifyObservers(false);
		}
	}


	@Override
	public boolean deleteTask(String title){
		Task task = (title != null && !title.isEmpty())? returnReferenceOnTask(title): null;
		if (task != null){
			taskList.remove(task); // удаляем найденную задачу
//			notifyObservers(false);
			return true; 
		}

		return false;			
	}
	
	@Override
	public void clearTasks(){
		if (taskList != null && !taskList.isEmpty())
			taskList.clear();
		
//		notifyObservers(false);
	}
	
	@Override // безопасный поиск, вернет копию задачи с указанным именем
	public Task searchTask(String title){
		if (title == null || title.isEmpty()) 
			return null; // если входные данные не верны вернем null

		for (Task task : taskList) { 
			if (task.getTitle().equals(title))
				return task;
		}
		return null; // если ничего не нашли вернем null и как то обрабатывать, а то NPE
	}
	
	// вернуть ссылку, на задачу с указанным именем
	private Task returnReferenceOnTask(String title){ 
		if (title == null || title.isEmpty()) 
			return null; 

		Iterator<Task> iterator = taskList.iterator();
		Task currentTask;
		while(iterator.hasNext()){
			currentTask = iterator.next();
			if(currentTask.getTitle().equals(title))
				return currentTask;
		}
		return null;			
	}

	@Override // замена текущей, на указанную
	public boolean replaceTask(String title, Task task) {
		if(title != null && !title.isEmpty() && task != null){
			int index = taskList.indexOf(returnReferenceOnTask(title));
			if (index != -1){
				taskList.set(index, task);
//				notifyObservers(false);
				return true;
			}
		}
		return false;
	}
	
	@Override // 
	public void replaceTasks(List<? extends Task> list) {
		taskList.clear();
		taskList.addAll(list);
//		notifyObservers(false);
	}

	@Override // редактирование полей найденной задачи, можно еще добавить проверку на equals, но не уверен что стоит
	public boolean editTask(String title, String editTitle, String editDescription, Date editDate) {
		Boolean edited = false; // редактировали задачу или нет
		Task task = returnReferenceOnTask(title); // получили ссыль на нужную таску
		if (task==null)
			return false;
		
		if(editTitle != null && !editTitle.isEmpty()){ 
			task.setTitle(editTitle);
			edited = true;
		}

		if(editDescription != null && !editDescription.isEmpty()){ 
			task.setDescription(editDescription);
			edited = true;
		}

		if(editDate != null){ 
			task.setDate(editDate);
			edited = true;
		}
		
//		notifyObservers(false);
		return edited;
	}

	
	@Override
	public List<? extends Task> getTasks() {
		return taskList;
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		notifyObservers(arg);
		
	}
}
