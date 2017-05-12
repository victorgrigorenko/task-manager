package model;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

// Интерфейс описывающий выгрузку/восстановление журнала в XML



public interface XMLJournalible<T extends Taskable> {

	// запись в дефолтный xml
	void recordJournal(Journalable<T> journal) throws JAXBException, FileNotFoundException;
	// запись в xml файл, с указанием имени
	void recordJournal(Journalable<T> journal, String fileName) throws JAXBException, FileNotFoundException;
	
	// считать из дефолтного xml файла
	Journalable<T> readJournal(Journalable<T> journal) throws JAXBException;
	// считать из xml файла с указанным именем
	Journalable<T> readJournal(Journalable<T> journal, String fileName) throws JAXBException;
	
}
