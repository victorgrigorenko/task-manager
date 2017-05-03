package model;

import java.util.Calendar;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "journalable")
public interface Journalable<T extends Taskable> {
	
	T createTask(String title, String desc, Calendar date);

	// добавление задач будет происходить в конец списка, поэтом индекс не нужен
	// добавить задачу
	boolean addTask(T task); 
	// добавить список задач
	void addTasks(List<? extends T> list);
//	void addTasks(Collection<? extends T> list); 

	// поиск задачи с указанным именем и ее замена на указанную в аргументе
	boolean replaceTask(String title, T task);
	// замена всего списка задач
	void replaceTasks(List<? extends T> list);
	// попытка заменить replaseTask на более адекватную штуку
	// title - имя для поиска, edit - редактируемые поля задачи
	boolean editTask(String title, String editTitle, String editDescription, Calendar editDate); // т.е. находим задачу по названию и редактируем ее поля

	// возвращаем список задач
	List<? extends T> getTasks();
	
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
	
	// Показать список задач
	void showAll();
}
