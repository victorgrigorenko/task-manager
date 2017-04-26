package model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "taskable")
public interface Taskable {
	
	// будем создавать задачи, на все случаи жизни
	Taskable createTask();

	Taskable createTask(String title);
	
	Taskable createTask(String title, String desc);
	
	Taskable createTask(String title, Date date);

	Taskable createTask(String title, String desc, Date date);
	
	// title
	String getTitle();
	
	void setTitle(String title);
	
	// description
	String getDecsription();
	
	void setDescription(String desc);
	
	// dateTime
	Date getDate();
	
	void setDate(Date date);
	
	String show();
		
	// contacts .. 
	// что такое контакты?? для чего нужны?
}
