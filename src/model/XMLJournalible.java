package model;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

public interface XMLJournalible<T extends Taskable> {

	void recordJournal(Journalable<T> journal, String fileName) throws JAXBException, FileNotFoundException;
	
	Journalable<T> readJournal(Journalable<T> journal, String fileName) throws JAXBException;	
}
