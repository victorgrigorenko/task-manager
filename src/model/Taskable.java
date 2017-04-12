package model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "taskable")
public interface Taskable {
	
//	 id
//	int getId();
//	void setId(int id);
	
	// title
	String getTitle();
	void setTitle(String title);
	
	// description
	String getDecsription();
	void setDescription(String desc);
	
	// dateTime
	Date getDate();
	void setDate(Date date);
	
	// contacts .. 
	// что такое контакты?? для чего нужны?
}