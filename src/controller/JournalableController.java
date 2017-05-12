package controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

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
	String showAll();
	
	// очистить журнал
	String clearAll();
	
	// сохранение
	String recordJournal() 
			throws JAXBException, IOException, FileNotFoundException;
	
	// чтение
	String readJournal() 
			throws JAXBException, IOException, FileNotFoundException;	
	
	void start(); // стартуем считывание команд пользователя
}
