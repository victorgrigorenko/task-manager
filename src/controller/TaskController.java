package controller;

import java.util.Date;

import model.Taskable;

public class TaskController {
	private Taskable task;
	
	private TaskController(Taskable task) {
		this.task = task;
	}
	
	// Создаем экземпляр контроллера
	public static TaskController newInstance(Taskable task){
		return new TaskController(task);
	}
	
	public String getTitle(){
		return task.getTitle();
	}
	
	public void setTitle(String title){
		task.setTitle(title);
	}
	
	// description
	public String getDecsription(){
		return task.getDecsription();
	}
	
	public void setDescription(String description){
		task.setDescription(description);
	}
	
	// dateTime
	public Date getDate(){
		return task.getDate();
	}
	
	public void setDate(Date date){
		task.setDate(date);
	}
}
