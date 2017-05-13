package controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import model.Taskable;
import observer.*;

public interface JournalableController<T extends Taskable> extends Observable{
	
	String addTask(); 

	String deleteTask();

	String editTask();

	String searchTask(); 
	
	String showAll();
	
	String clearAll();
	
	String recordJournal() 
			throws JAXBException, IOException, FileNotFoundException;
	
	String readJournal() 
			throws JAXBException, IOException, FileNotFoundException;	
	
	void start();
}
