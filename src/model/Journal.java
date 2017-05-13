package model;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import observer.*;
import static constants.Constants.*;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)  
@XmlType(name = "journalOfTask")
public class Journal implements Journalable<Task>{

	@XmlElement(required = true, name = "task") 
	@XmlElementWrapper(name = "tasks")
	private List<Task> taskList = new ArrayList<>();

	private List<Observer> observers;

	public Journal(){
		observers = new ArrayList<>();
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
			observer.update(this,arg);  
	}
	

	@Override
	public Task createTask(String title, String desc, Date date) {
		if(title!=null && !title.isEmpty() && desc!=null && date!=null){
			return new Task(title, desc, date);
		}
		
		return null;
	}
	
	@Override
	public boolean addTask(Task task){
		boolean result;
		Task t = returnReferenceOnTask(task.getTitle());
		if (t != null) taskList.remove(t); 
		result = (taskList.add(task))? true: false;
		notifyObservers(NONE);
		return result;
	}

	@Override
	public void addTasks(List<? extends Task> list) {
		if (list !=null && !list.isEmpty()){ 
			taskList.addAll(list);
			notifyObservers(NONE);
		}
	}


	@Override
	public boolean deleteTask(String title){
		Task task = (title != null && !title.isEmpty())? returnReferenceOnTask(title): null;
		if (task != null){
			taskList.remove(task);
			notifyObservers(NONE);
			return true; 
		}

		return false;			
	}
	
	@Override
	public void clearTasks(){
		if (taskList != null && !taskList.isEmpty())
			taskList.clear();
		
		notifyObservers(NONE);
	}
	
	@Override 
	public Task searchTask(String title){
		if (title == null || title.isEmpty()) 
			return null; 

		for (Task task : taskList) { 
			if (task.getTitle().equals(title))
				return task;
		}
		return null; 
	}
	
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

	@Override 
	public boolean replaceTask(String title, Task task) {
		if(title != null && !title.isEmpty() && task != null){
			int index = taskList.indexOf(returnReferenceOnTask(title));
			if (index != -1){
				taskList.set(index, task);
				notifyObservers(NONE);
				return true;
			}
		}
		return false;
	}
	
	@Override  
	public void replaceTasks(List<? extends Task> list) {
		taskList.clear();
		taskList.addAll(list);
		notifyObservers(NONE);
	}

	@Override
	public boolean editTask(String title, String editTitle, String editDescription, Date editDate) {
		Boolean edited = false; 
		Task task = returnReferenceOnTask(title); 
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
		
		notifyObservers(NONE);
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
