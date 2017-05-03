package model;

import java.beans.Transient;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "task")
public class Task implements Taskable{
//	private int id; // id соответствует значению в списке

	@XmlElement
	private String title;
	
	@XmlElement
	private String description;
	
	@XmlElement
	private Calendar date;
	
	private SimpleDateFormat formatDate = new SimpleDateFormat("d MMMM YYYYг, в hh:mm");

	
	// еще одно поле контакты..
	

	public Task(){
		title = "No Name";
		description = "...";
		date = new GregorianCalendar(); // устанавливаем текущую дату/время
	}
	
	public Task(String title){
		setTitle(title);
		description = "...";
		date = new GregorianCalendar();
	}

	public Task(String title, String desc){
		setTitle(title);
		setDescription(desc);
		date = new GregorianCalendar();
	}

	public Task(String title, Calendar date){
		setTitle(title);
		description = "...";
		setDate(date); 
	}

	public Task(String title, String desc, Calendar date){
		setTitle(title);
		setDescription(desc);
		setDate(date);
	}

	@Override
	public Taskable createTask() {
		return new Task();
	}

	@Override
	public Taskable createTask(String title, String desc, Calendar date) {
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
	public Calendar getDate() {
		return date;
	}

	@Override
	public void setDate(Calendar date) { 
		this.date = date;
		
	}
	
	@Override
	public String toString(){

		return title;
	}

	@Override
	public String show(){
		
		return "название: " + this.getTitle()
		+"\nописание: "+ this.getDecsription()
		+"\nдата: "+ formatDate.format(getDate().getTime());
	}
}
