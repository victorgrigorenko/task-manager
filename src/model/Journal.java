package model;

import static constants.Constants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
	public void notifyObservers() {
		for (Observer observer : observers) 
			observer.update(this,null); // второй аргумент нам не нужен
	}
	
	

	@Override
	public T createTask() {
		return (T) new Task();
	}

	@Override
	public T createTask(String title) {
		if(title!=null && !title.isEmpty()){ 
			return (T) new Task(title);
		}
		return null;
	}

	@Override
	public T createTask(String title, String desc) {
		if(title!=null && !title.isEmpty() && desc!=null){
			return (T) new Task(title, desc);
		}
		return null;
	}

	@Override
	public T createTask(String title, Date date) {
		if(title!=null && !title.isEmpty() && date!=null){
			return (T) new Task(title, date);
		}
		return null;
	}

	@Override
	public T createTask(String title, String desc, Date date) {
		if(title!=null && !title.isEmpty() && desc!=null && date!=null){
			return (T) new Task(title, desc, date);
		}
		return null;
	}

	
	@Override
	public void addTask(T task){
		taskList.add(task);
		notifyObservers();
	}

	@Override
	public void addTasks(List<? extends T> list) {
		if (list !=null && !list.isEmpty()){ // нужна ли здесь проверка, ведь в контроллер не выносим данный метод
			taskList.addAll(list);
			notifyObservers();
		}
	}

	@Override // удаление по id/index
	@Deprecated //! применимость метода под вопросом
	public void deleteTask(int index){ 
		taskList.remove(index);
		notifyObservers();
	}

	@Override
	public boolean deleteTask(String title){
		T task = (title != null && !title.isEmpty())? returnReferenceOnTask(title): null;
		if (task != null){
			taskList.remove(task); // удаляем найденную задачу
			notifyObservers();
			return true; 
		}

		return false;			
	}
	
	@Override
	public void clearTasks(){
		taskList.clear();
		notifyObservers();
	}
	
	@Override // безопасный поиск, вернет копию задачи с указанным именем
	public T searchTask(String title){
		if (title == null || title.isEmpty()) 
			return null; // если входные данные не верны вернем null

		for (T task : taskList) { //если хеш-коды разные, то и объекты гарантированно разные
//			if (task.getTitle().hashCode() != title.hashCode()) 
//				continue; 	// переходим к след итерации
			// если хеши совпадают, то объекты могут быть равны, проверяем
			if (task.getTitle().equals(title))
				return task;
		}
		return null; // если ничего не нашли вернем null
	}
	
	// вернуть ссылку, на задачу с указанным именем
	private T returnReferenceOnTask(String title){ // переписать, используя итератор
		if (title == null || title.isEmpty()) 
			return null; 

		Iterator<T> iterator = taskList.iterator();
		T currentTask;
		while(iterator.hasNext()){
			currentTask = iterator.next();
			if(currentTask.getTitle().equals(title))
				return currentTask;
		}
		return null;			
	}

	//! Подумать! менять не объект, а модифицировать поля
	@Override // замена текущей, на указанную
	public boolean replaceTask(String title, T task) {
		if(title != null && !title.isEmpty() && task != null){
			int index = taskList.indexOf(returnReferenceOnTask(title));
			if (index != -1){
				taskList.set(index, task);
				notifyObservers();
				return true;
			}
		}
		return false;
	}
	
	@Override // 
	public void replaceTasks(List<? extends T> list) {
		taskList.clear();
		taskList.addAll(list);
		notifyObservers();
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
	public Journal<?> readJournal(String fileName) throws JAXBException{
		// если все ок вернет лист задач, иначе вернет null
		return StorageListTask.readAllData(this, fileName);
	}
	
	// считать из дефолтного xml файла
	public Journal<?> readJournal() throws JAXBException{
		return StorageListTask.readAllData(this, null);
	}
	
	
	private static class StorageListTask<T extends Taskable>{ //implements Storage....
		private static File fileDefault;
		
		public static boolean recordAllData(Journal<?> jrnl,String fileName) throws JAXBException{
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
	    		m.marshal(jrnl, file);
		        System.out.println(fileDefault.getName());
	    		
			} catch(FileNotFoundException e){
				e.printStackTrace();
				return false;
				
			} catch (IOException e) {
				e.printStackTrace();
				return false;
				
			} catch (JAXBException e) { // ставим в конец, т.к JAXB юзаем в конце
//				e.printStackTrace();
				throw new JAXBException(e);
				//return false;
			}
	        return true;
		}

		public static Journal<?> readAllData(Journal<?> jrnl,String fileName) throws JAXBException{
	        try{
        		//еще проверку на предмет существования данного файла в директории
        		File file = (fileName != null && !fileName.isEmpty())?
        				new File(PATH+"//"+fileName+".xml"): // пользовательский
    					new File(PATH+"//"+fileDefault.getName());				
	        
	    		JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE);
	            Unmarshaller um = jaxbContext.createUnmarshaller();
	    		
	            return  (Journal<?>) um.unmarshal(file);	            
	    		
			} catch (JAXBException e) { // ставим в конец, т.к JAXB юзаем в конце
//				e.printStackTrace();
				throw new JAXBException(e);
				//return null;
			}
		}

	}

}
