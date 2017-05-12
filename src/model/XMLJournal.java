package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import static constants.Constants.*;

public class XMLJournal implements XMLJournalible<Task>{
	private static File fileDefault;
	
	private void recordData(Journalable<Task> journal,String fileName) throws JAXBException, FileNotFoundException{
		FileOutputStream file = (fileName != null && !fileName.isEmpty())?
				new FileOutputStream(PATH+"//"+fileName+".xml"):// пользователь указывает куда хочет положить файл
				new FileOutputStream(PATH+"//"+NAME);			// в дефолтный файл
		
		/* по умолчанию дефолтный файл, это указан с именем в константе NAME, но если мы
		 * задаем имя файлу при записи, то fileDefault будет содержать указанное нами имя.  
		 */
		fileDefault = (fileName != null && !fileName.isEmpty())? 
				new File(PATH+"//"+fileName+".xml"):
				new File(PATH+"//"+NAME);
		
		JAXBContext jaxbContext = JAXBContext.newInstance(journal.getClass());//(PACKAGE);
		
		Marshaller m = jaxbContext.createMarshaller();
		
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true); // эта строка добавляет форматирование в xml файл
		m.marshal(journal, file);
	}

	@SuppressWarnings("unchecked")
	private Journalable<Task> readData(Journalable<Task> journal,String fileName) throws JAXBException{
		//еще проверку на предмет существования данного файла в директории
		File file = (fileName != null && !fileName.isEmpty())?
				new File(PATH+"//"+fileName+".xml"): 	// пользовательский
				new File(PATH+"//"+fileDefault.getName()); // если нет дефолтного файла возникает NPE				
        
		JAXBContext jaxbContext = JAXBContext.newInstance(journal.getClass());//(PACKAGE);
        Unmarshaller um = jaxbContext.createUnmarshaller();
		
        journal =  (Journalable<Task>) um.unmarshal(file); //! Сохраняем в наш журнал и возвращаем его
        return  journal;	            
	}

	@Override
	public void recordJournal(Journalable<Task> journal) 
			throws JAXBException, FileNotFoundException {
		recordData(journal, null);
		
	}

	@Override
	public void recordJournal(Journalable<Task> journal, String fileName) 
			throws JAXBException, FileNotFoundException {
		recordData(journal, fileName);
	}

	@Override
	public Journalable<Task> readJournal(Journalable<Task> journal) throws JAXBException {
		return readData(journal, null);
	}

	@Override
	public Journalable<Task> readJournal(Journalable<Task> journal, String fileName) throws JAXBException {
		return readData(journal, fileName);
	}
	
}