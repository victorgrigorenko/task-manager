package model.storage;

import model.Journalable;
import model.Taskable;

// Для чтения/сохранения журнала
public interface Storageable<T extends Journalable<V>, V extends Taskable> {
	
	// запись
	boolean recordJournal(String fileName);
	
	// чтение
	T readJournal(String fileName);

}
