package model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import model.storage.StorageListTaskable;



//возможно стоило использовать PROPERTY, и объявить геттер/сеттер пары для корректной
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE) // записи чтения  
@XmlType(name = "journalOfTask")
//@XmlSeeAlso({ Taskable.class })
public class Journal implements Journalable, StorageListTaskable<Taskable>{

	// ставим required, теперь если не найдет List в xml, то будет ругаться
//!	@XmlElement(required = true, type =Taskable.class) //! Taskable - интерфейс поэтому НЕ ОК !!!!!
	@XmlElement(required = true, name = "task", type =Task.class) //ОК, JAXB хочет класс, поэтому пока оставим так, но ЭТО НЕ КРУТО! 
	@XmlElementWrapper(name = "tasks") // для группировки коллекции в подтег
	private List<Taskable> taskList = new ArrayList<>();
	
	
	@Override
	public void addTaskToList(Taskable task){
		if(task!=null)			
			taskList.add(task);
	}

	@Override
	public void deleteTaskAtList(int index){
			taskList.remove(index);
	}

	@Override
	public void replaceTaskAtList(int index, Taskable task) {
		if (task != null)
			taskList.set(index, task);		
	}

	@Override
	public Taskable getTaskAtList(int index) {
		return taskList.get(index);

	}

	@Override
	public List<Taskable> getTasks() {
		return taskList;
	}
	
	// не здорово
	public boolean recordAllDatal(String fileName){
		StorageListTaskable.super.recordAllData(taskList,fileName);//(List<?>)
		return false;
	}

}
