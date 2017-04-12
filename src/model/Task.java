package model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "task")
public class Task implements Taskable{
//	private int id; // id соответствует значению в списке

	@XmlElement
	private String title;
	
	@XmlElement
	private String description;
	
	@XmlElement
	private Date date;
	
	// еще одно поле контакты..
	
	
	public Task(){
		title = "No Name";
		description = "...";
		date = new Date(); // устанавливаем текущую дату/время
	}
	
	public Task(String title){
		setTitle(title);
		description = "...";
		date = new Date(); 
	}

	public Task(String title, String desc){
		setTitle(title);
		setDescription(desc);
		date = new Date(); 
	}

	public Task(String title, String desc, Date date){
		setTitle(title);
		setDescription(desc);
		setDate(date);
	}

//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		if(title!=null && !title.trim().isEmpty())
			this.title = title;
		
	}

	@Override
	public String getDecsription() {
		return description;
	}

	@Override
	public void setDescription(String desc) {
		if(desc!=null && !desc.trim().isEmpty())
			this.description = desc;
		
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public void setDate(Date date) { // возможно стоит как то получше организовать проверку даты/времени
		if(date!=null)
			this.date = date;
		
	}
}
