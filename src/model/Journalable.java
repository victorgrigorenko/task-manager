package model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "journalable")
public interface Journalable<T extends Taskable> {
	//List<Task> taskList;
	
	// будем создавать задачи, на все случаи жизни
	T createTask();

	T createTask(String title);
	
	T createTask(String title, String desc);
	
	T createTask(String title, Date date);

	T createTask(String title, String desc, Date date);

	// добавление задач будет происходить в конец списка, поэтом индекс не нужен
	// добавить задачу
	void addTask(T task); 
	// добавить список задач
	void addTasks(List<? extends T> list);
//	void addTasks(Collection<? extends T> list); 

	// поиск задачи с указанным именем и ее замена на указанную в аргументе
	boolean replaceTask(String title, T task);
	// замена всего списка задач
	void replaceTasks(List<? extends T> list);

	// возвращаем одну задачу
	T getTask(int index);
	// возвращаем список задач
	List<? extends T> getTasks();

	@Deprecated //! применимость метода под вопросом
	void deleteTask(int index); // удаление по id/index
	
	// удаление по имени
	boolean deleteTask(String title); // может и не удалить задачу, если не найдет
	
	// удалить все задачи
	void clearTasks(); // просто все почистит, влюбом случае
	
	// поиск задачи по имени
	T searchTask(String title);
	
	// в идеале вынести в отдельный интерфейс, но начинаются проблемы
	// запись в дефолтный xml
	boolean recordJournal() throws JAXBException;
	// запись в xml файл, с указанием имени
	boolean recordJournal(String fileName) throws JAXBException;
	
	// считать из дефолтного xml файла
	Journalable<?> readJournal() throws JAXBException;
	// считать из xml файла с указанным именем
	Journalable<?> readJournal(String fileName) throws JAXBException;
	
	
}
