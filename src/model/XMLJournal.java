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
	
	@Override
	public void recordJournal(Journalable<Task> journal, String fileName) 
			throws JAXBException, FileNotFoundException {

		FileOutputStream file = (fileName != null && !fileName.isEmpty())?
				new FileOutputStream(PATH+"//"+fileName+".xml"):
				new FileOutputStream(PATH+"//"+NAME);			
		
		fileDefault = (fileName != null && !fileName.isEmpty())? 
				new File(PATH+"//"+fileName+".xml"):
				new File(PATH+"//"+NAME);
		
		JAXBContext jaxbContext = JAXBContext.newInstance(journal.getClass());
		
		Marshaller m = jaxbContext.createMarshaller();
		
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
		m.marshal(journal, file);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Journalable<Task> readJournal(Journalable<Task> journal, String fileName) throws JAXBException {
		File file = (fileName != null && !fileName.isEmpty())?
				new File(PATH+"//"+fileName+".xml"): 
				new File(PATH+"//"+fileDefault.getName()); 				
        
		JAXBContext jaxbContext = JAXBContext.newInstance(journal.getClass());
        Unmarshaller um = jaxbContext.createUnmarshaller();
		
        journal =  (Journalable<Task>) um.unmarshal(file); 
        return  journal;	            
	}	
}