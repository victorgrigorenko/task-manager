package model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "journalable")
public interface Journalable<T extends Taskable> {
	//List<Task> taskList;
	
	// добавление задач будет происходить в конец списка, поэтом индекс не нужен
	// добавить задачу
	void addTask(T task); 
	// добавить список задач
	void addTasks(List<? extends T> list);
//	void addTasks(Collection<? extends T> list); 

	// замена задачи из списка, на указанную в аргументе
	void replaceTask(int index, T task);
	// замена всего списка задач
	void replaseTasks(List<? extends T> list);

	// возвращаем одну задачу
	T getTask(int index);
	// возвращаем список задач
	List<? extends T> getTasks();

	// при удалении же нужно знать id(индекс) удаляемого элемента 
	// удалить одну, определенную задачу
	void deleteTask(int index); 
	// удалить все задачи
	void deleteTasks();
	
	// в идеале вынести в отдельный интерфейс, но начинаются проблемы
	// запись в дефолтный xml
	boolean recordJournal();
	// запись в xml файл, с указанием имени
	boolean recordJournal(String fileName);
	
	// считать из дефолтного xml файла
	Journalable<?> readJournal();
	// считать из xml файла с указанным именем
	Journalable<?> readJournal(String fileName);
	
	
}
