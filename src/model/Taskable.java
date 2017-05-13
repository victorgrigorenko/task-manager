package model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "taskable")
public interface Taskable {
	
	Taskable createTask();

	Taskable createTask(String title, String desc, Date date);
	
	String getTitle();
	
	void setTitle(String title);
	
	String getDecsription();
	
	void setDescription(String desc);
	
	Date getDate();
	
	void setDate(Date date);
	
	String show();
}
