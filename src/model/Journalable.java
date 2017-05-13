package model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import observer.*;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "journalable")
public interface Journalable<T extends Taskable> extends Observable, Observer{
	
	T createTask(String title, String desc, Date date);

	boolean addTask(T task); 

	void addTasks(List<? extends T> list);

	boolean replaceTask(String title, T task);

	void replaceTasks(List<? extends T> list);

	boolean editTask(String title, String editTitle, String editDescription, Date editDate); 

	List<? extends T> getTasks();
	
	boolean deleteTask(String title); 
	
	void clearTasks(); 
	
	T searchTask(String title);
}
