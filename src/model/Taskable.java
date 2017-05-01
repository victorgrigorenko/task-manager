package model;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "taskable")
public interface Taskable {
	
	// будем создавать задачи, на все случаи жизни
	Taskable createTask();

	Taskable createTask(String title, String desc, Calendar date);
	
	// title
	String getTitle();
	
	void setTitle(String title);
	
	// description
	String getDecsription();
	
	void setDescription(String desc);
	
	// dateTime
	Calendar getDate();
	
	void setDate(Calendar calendar);
	
	String show();
		
	// contacts .. 
	// что такое контакты?? для чего нужны?
}
