package model;

import static constants.Constants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
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

import constants.Command;
import controller.JournalController;





//возможно стоило использовать PROPERTY, и объявить геттер/сеттер пары для корректной
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE) // записи чтения  
@XmlType(name = "journalOfTask")
public class Journal extends Observable implements Journalable<Task>{ //
	Integer i;
	
	// ставим required, теперь если не найдет List в xml, то будет ругаться
	@XmlElement(required = true, name = "task")//, type =Task.class) //ОК, JAXB хочет класс, поэтому пока оставим так, но ЭТО НЕ КРУТО! 
	@XmlElementWrapper(name = "tasks") // для группировки коллекции в подтег
	private List<Task> taskList = new ArrayList<>();

	// Список наблюдателей(подписчиков)
	private List<Observer> observers;

	public Journal(){//Observer o){
		observers = new ArrayList<>();
		//this.addObserver(o);
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
	public void notifyObservers(Object arg) {
		for (Observer observer : observers) 
			observer.update(this,((boolean)arg)); // второй аргумент в true, только если хотим вывести список задач 
	}
	

	@Override
	public Task createTask(String title, String desc, Calendar date) {
		if(title!=null && !title.isEmpty() && desc!=null && date!=null){
			return new Task(title, desc, date);
		}
		//.... ну или если ввели дату без описалова, добавляем дефолт, но стекущей датой
		return new Task(); // быстрое добавление задачи, если поля пустые
	}

	
	@Override
	public boolean addTask(Task task){
		boolean result;
		Task t = returnReferenceOnTask(task.getTitle());
		if (t != null) taskList.remove(t); // на случай если нашли задачу с таким же именем, удаляем старую..
		result = (taskList.add(task))? true: false;
		notifyObservers(false);
		return result;
	}

	@Override
	public void addTasks(List<? extends Task> list) {
		if (list !=null && !list.isEmpty()){ // нужна ли здесь проверка, ведь в контроллер не выносим данный метод
			taskList.addAll(list);
			notifyObservers(false);
		}
	}


	@Override
	public boolean deleteTask(String title){
		Task task = (title != null && !title.isEmpty())? returnReferenceOnTask(title): null;
		if (task != null){
			taskList.remove(task); // удаляем найденную задачу
			notifyObservers(false);
			return true; 
		}

		return false;			
	}
	
	@Override
	public void clearTasks(){
		if (taskList != null && !taskList.isEmpty())
			taskList.clear();
		
		notifyObservers(false);
	}
	
	@Override // безопасный поиск, вернет копию задачи с указанным именем
	public Task searchTask(String title){
		if (title == null || title.isEmpty()) 
			return null; // если входные данные не верны вернем null

		for (Task task : taskList) { 
			if (task.getTitle().equals(title))
				return task;
		}
		return null; // если ничего не нашли вернем null и как то обрабатывать, а то NPE
	}
	
	// вернуть ссылку, на задачу с указанным именем
	private Task returnReferenceOnTask(String title){ 
		if (title == null || title.isEmpty()) 
			return null; 

		Iterator<Task> iterator = taskList.iterator();
		Task currentTask;
		while(iterator.hasNext()){
			currentTask = iterator.next();
			if(currentTask.getTitle().equals(title))
				return currentTask;
		}
		return null;			
	}

	@Override // замена текущей, на указанную
	public boolean replaceTask(String title, Task task) {
		if(title != null && !title.isEmpty() && task != null){
			int index = taskList.indexOf(returnReferenceOnTask(title));
			if (index != -1){
				taskList.set(index, task);
				notifyObservers(false);
				return true;
			}
		}
		return false;
	}
	
	@Override // 
	public void replaceTasks(List<? extends Task> list) {
		taskList.clear();
		taskList.addAll(list);
		notifyObservers(false);
	}

	@Override // редактирование полей найденной задачи, можно еще добавить проверку на equals, но не уверен что стоит
	public boolean editTask(String title, String editTitle, String editDescription, Calendar editDate) {
		Boolean edited = false; // редактировали задачу или нет
		Task task = returnReferenceOnTask(title); // получили ссыль на нужную таску
		if (task==null)
			return false;
		
		if(editTitle != null && !editTitle.isEmpty()){ 
			task.setTitle(editTitle);
			edited = true;
		}

		if(editDescription != null && !editDescription.isEmpty()){ 
			task.setDescription(editDescription);
			edited = true;
		}

		if(editDate != null){ 
			task.setDate(editDate);
			edited = true;
		}
		
		notifyObservers(false);
		return edited;
	}

	
	@Override
	public List<? extends Task> getTasks() {
		return taskList;
	}
	
	
	/** запись на диск
	 * @param fileName - имя считываемого файла: *.xml, 
	 * @return в случае успеха вернет true, иначе false
	 * @throws JAXBException 
	 */
	public boolean recordJournal(String fileName) throws JAXBException {		
		return StorageListTask.recordAllData(this, fileName); // указывает на класс
	}
	
	// запись в дефолтный xml
	public boolean recordJournal() throws JAXBException {		
		return StorageListTask.recordAllData(this, null);
	}

	/** считывани с диска
	 * @param fileName - имя считываемого файла: *.xml, 
	 * для использования имени по умолчанию передавайте null
	 * @return в случае успеха вернет журнал, иначе null
	 */
	// считать из xml файла
	public Journal readJournal(String fileName) throws JAXBException{
		// если все ок вернет лист задач, иначе вернет null
		return StorageListTask.readAllData(this, fileName);
	}
	
	// считать из дефолтного xml файла
	public Journal readJournal() throws JAXBException{
		return StorageListTask.readAllData(this, null);
	}
	
	
	private static class StorageListTask<T extends Taskable>{ //implements Storage....
		private static File fileDefault;
		
		public static boolean recordAllData(Journal journal,String fileName) throws JAXBException{
	        boolean result = false;
			try(
	        		FileOutputStream file = (fileName != null && !fileName.isEmpty())?
					new FileOutputStream(PATH+"//"+fileName+".xml"): // пользовательский
	        		new FileOutputStream(PATH+"//"+NAME);
				){	        

	        	/* по умолчанию дефолтный файл, это указан с именем в константе NAME, но если мы
	        	 * задаем имя файлу при записи, то fileDefault будет содержать указанное нами имя.  
	        	 */
	        	fileDefault = (fileName != null && !fileName.isEmpty())? 
	        			new File(PATH+"//"+fileName+".xml"):
        				new File(PATH+"//"+NAME);
	        	
	    		JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE);
	    		
	    		Marshaller m = jaxbContext.createMarshaller();
	    		
	    		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true); // эта строка добавляет форматирование в xml файл
	    		m.marshal(journal, file);
		        System.out.println(fileDefault.getName());
		        result = true;
			} catch(FileNotFoundException e){
//				e.printStackTrace();
				result = false;

				
			} catch (IOException e) {
//				e.printStackTrace();
//				return false;
				result = false;
				
			} catch (JAXBException e) { // ставим в конец, т.к JAXB юзаем в конце
//				e.printStackTrace();
				result = false;
				throw new JAXBException(e);
			}
	        return result;
		}

		// После того как добавили календарь, xml выглядит ужасающе
		// Т.е. нужна выжимка, а не весь объект календаря писать 
		public static Journal readAllData(Journal journal,String fileName) throws JAXBException{
	        try{
        		//еще проверку на предмет существования данного файла в директории
        		File file = (fileName != null && !fileName.isEmpty())?
        				new File(PATH+"//"+fileName+".xml"): // пользовательский
    					new File(PATH+"//"+fileDefault.getName()); // если нет дефолтного файла возникает NPE				
	        
	    		JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE);
	            Unmarshaller um = jaxbContext.createUnmarshaller();
	    		
	            journal =  (Journal) um.unmarshal(file); //! Сохраняем в наш журнал и возвращаем его
	            journal.notifyObservers(false); // апдейтим
	            return  journal;	            
	    		
			} catch (JAXBException e) { // ставим в конец, т.к JAXB юзаем в конце

				throw new JAXBException(e);
				//return null;
			}
		}
	}


	@Override
	public void showAll() {
		notifyObservers(true); // здесь просто просим вывести текущее состояние журнала
	}
	

}
