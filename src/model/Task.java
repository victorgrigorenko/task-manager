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

	public Task(String title, Date date){
		setTitle(title);
		description = "...";
		setDate(date); 
	}

	public Task(String title, String desc, Date date){
		setTitle(title);
		setDescription(desc);
		setDate(date);
	}

	@Override
	public Taskable createTask() {
		return new Task();
	}

	public static Taskable create(String title) {
		return new Task(title);
	}

	@Override
	public Taskable createTask(String title) {
		return new Task(title);
	}

	@Override
	public Taskable createTask(String title, String desc) {
		return new Task(title,desc);
	}

	@Override
	public Taskable createTask(String title, Date date) {
		return new Task(title,date);
	}

	@Override
	public Taskable createTask(String title, String desc, Date date) {
		return new Task(title,desc,date);
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getDecsription() {
		return description;
	}

	@Override
	public void setDescription(String desc) {
		this.description = desc;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public void setDate(Date date) { 
		this.date = date;
		
	}
	
	//@Override
	public String toString(){

		return title;
	}
}
