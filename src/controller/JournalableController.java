package controller;

import java.lang.annotation.Inherited;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.JAXBException;

import model.Journal;
import model.Taskable;

public interface JournalableController<T extends Taskable> {
	
	// добавить задачу 
	String addTask(); 

	// удаление по имени
	String deleteTask(); // может и не удалить задачу, если не найдет

	// попытка заменить replaseTask на более адекватную штуку
	String editTask();

	// поиск задачи по имени, нашли и вернули
	String searchTask(); 
	
	// выводим список задач
	void showAll();
	
	// очистить журнал
	String clearAll();
	
	// сохранение
	String recordJournal() throws JAXBException;
	
	// чтение
	String readJournal() throws JAXBException;	

}
