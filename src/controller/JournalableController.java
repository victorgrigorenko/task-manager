package controller;

import java.lang.annotation.Inherited;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import model.Journal;
import model.Taskable;

public interface JournalableController<T extends Taskable> {
	
	// добавить задачу на все случаи жизни
	boolean addTask(); 

	boolean addTask(String title);

	boolean addTask(String title, String desc);
	
	boolean addTask(String title, Date date);

	boolean addTask(String title, String desc, Date date);
	
	// замена задачи из списка, на указанную в аргументе
	boolean replaceTask(String title, T task); // ищем по имени и заменяем

	// поиск задачи по имени, нашли и вернули
	T searchTask(String title); 
	
	@Deprecated //! применимость метода под вопросом
	T getTask(int index); //! по индексу 
	
	// возвращаем список задач
	List<? extends T> getTasks();
	
	@Deprecated //! применимость метода под вопросом
	void deleteTask(int index);	// удаление по id/index

	// удаление по имени
	boolean deleteTask(String title); // может и не удалить задачу, если не найдет

	// очистить журнал
	void clearTasks();
	
	// сохранение
	boolean recordJournal() throws JAXBException;

	boolean recordJournal(String fileName) throws JAXBException;
	
	// чтение
	Journal<?> readJournal() throws JAXBException;
	
	Journal<?> readJournal(String fileName) throws JAXBException;

}
