package model;

import static model.storage.Constants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;





//возможно стоило использовать PROPERTY, и объявить геттер/сеттер пары для корректной
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE) // записи чтения  
@XmlType(name = "journalOfTask")
public class Journal<T extends Taskable> extends Observable implements Journalable<T>{

	 
	// ставим required, теперь если не найдет List в xml, то будет ругаться
//!	@XmlElement(required = true, type =Taskable.class) //! Taskable - интерфейс поэтому НЕ ОК !!!!!
	@XmlElement(required = true, name = "task", type =Task.class) //ОК, JAXB хочет класс, поэтому пока оставим так, но ЭТО НЕ КРУТО! 
	@XmlElementWrapper(name = "tasks") // для группировки коллекции в подтег
	private List<T> taskList = new ArrayList<>();

	// Список наблюдателей(подписчиков)
	private List<Observer> observers;

	public Journal(){
		observers = new ArrayList<>();
	}

	@Override
	public void addObserver(Observer o) {
		if(o != null)
			observers.add(o);
	}

	@Override
	public void deleteObserver(Observer o) {
		if(o != null)
			observers.remove(o);
	}
	
	@Override
	public void notifyObservers() {
		for (Observer observer : observers) 
			observer.update(this,null); // второй аргумент нам не нужен
	}
	
	@Override
	public void addTask(T task){
		if(task!=null){			
			taskList.add(task);
			notifyObservers();
		}
	}

	@Override
	public void addTasks(List<? extends T> list) {
		if (list !=null && !list.isEmpty()){
			taskList.addAll(list);
			notifyObservers();
		}
	}


	@Override // возможно стоит добавить проверку индекса
	public void deleteTask(int index){
		taskList.remove(index);
		notifyObservers();
	}

	@Override
	public void deleteTasks(){
		taskList.clear();
		notifyObservers();
	}

	@Override
	public void replaceTask(int index, T task) {
		if (task != null){
			taskList.set(index, task);
			notifyObservers();
		}
	}
	
	@Override // возможно стоит исправить, не здорово, что происходит 2 оповещения
	public void replaseTasks(List<? extends T> list) {
		taskList.removeAll(taskList);
		taskList.addAll(list);
	}

	@Override
	public T getTask(int index) {
		return taskList.get(index);
	}

	@Override
	public List<? extends T> getTasks() {
		return taskList;
	}
	
	
	/** запись на диск
	 * @param fileName - имя считываемого файла: *.xml, 
	 * @return в случае успеха вернет true, иначе false
	 */
	public boolean recordJournal(String fileName) {		
		// указывает на класс
		return StorageListTask.recordAllData(this, fileName);
	}
	
	// запись в дефолтный xml
	public boolean recordJournal() {		
		return StorageListTask.recordAllData(this, null);
	}

	/** считывани с диска
	 * @param fileName - имя считываемого файла: *.xml, 
	 * для использования имени по умолчанию передавайте null
	 * @return в случае успеха вернет журнал, иначе null
	 */
	// считать из xml файла
	public Journal<?> readJournal(String fileName){
		
		return StorageListTask.readAllData(this, fileName);
	}
	
	// считать из дефолтного xml файла
	public Journal<?> readJournal(){
		return StorageListTask.readAllData(this, null);
	}
	
	
	private static class StorageListTask<T extends Taskable>{ //implements Storage....
		private static File fileDefault;
		
		public static boolean recordAllData(Journal<?> jrnl,String fileName){
	        try(
	        		FileOutputStream file = (fileName != null && !fileName.trim().isEmpty())?
					new FileOutputStream(PATH+"//"+fileName+".xml"): // пользовательский
	        		new FileOutputStream(PATH+"//"+NAME);
				){	        
	        	
	        	fileDefault = (fileName != null && !fileName.trim().isEmpty())? 
	        			new File(PATH+"//"+fileName+".xml"):
        				new File(PATH+"//"+NAME);
	        	
	    		JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE);
	    		
	    		Marshaller m = jaxbContext.createMarshaller();
	    		
	    		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true); // эта строка добавляет форматирование в xml файл
	    		m.marshal(jrnl, file);
		        System.out.println(fileDefault.getName());
	    		
			} catch(FileNotFoundException e){
				e.printStackTrace();
				return false;
				
			} catch (IOException e) {
				e.printStackTrace();
				return false;
				
			} catch (JAXBException e) { // ставим в конец, т.к JAXB юзаем в конце
				e.printStackTrace();
				return false;
			}
	        return true;
		}

		public static Journal<?> readAllData(Journal<?> jrnl,String fileName){
	        try{
        		//еще проверку на предмет существования данного файла в директории
        		File file = (fileName != null && !fileName.trim().isEmpty())?
        				new File(PATH+"//"+fileName+".xml"): // пользовательский
    					new File(PATH+"//"+fileDefault.getName());				
	        
	    		JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE);
	            Unmarshaller um = jaxbContext.createUnmarshaller();
	    		
	            return  (Journal<?>) um.unmarshal(file);	            
	    		
			} catch (JAXBException e) { // ставим в конец, т.к JAXB юзаем в конце
				e.printStackTrace();
				return null;
			}
		}

	}

}
