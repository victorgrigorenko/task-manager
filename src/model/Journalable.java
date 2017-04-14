package model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "journalable")
public interface Journalable {
	//List<Task> taskList;
	
	// если все ок отработало, то возвращаем true ы	
	// добавление задач будет происходить последовательно поэтом индекс нам не нужен
	void addTaskToList(Taskable task); //, int index);
	
	// при удалении же нужно знать id(индекс) удаляемого элемента 
	void deleteTaskAtList(int index); 
	
	// замена задачи из списка, на указанную в аргументе
	void replaceTaskAtList(int index, Taskable task);
	
	// возвращаем одну задачу
	Taskable getTaskAtList(int index);
	
	// возвращаем список задач
	List<Taskable> getTasks();

}
