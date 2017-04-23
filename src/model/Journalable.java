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
	boolean recordJournal();
	// запись в xml файл, с указанием имени
	boolean recordJournal(String fileName);
	
	// считать из дефолтного xml файла
	Journalable<?> readJournal();
	// считать из xml файла с указанным именем
	Journalable<?> readJournal(String fileName);
	
	
}
