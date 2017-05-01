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
public class Journal extends Observable implements Journalable<Task>, Observer{ //
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
	public void notifyObservers() {
		for (Observer observer : observers) 
			observer.update(this,null); // второй аргумент нам не нужен
	}
	
	@Override
	public Task createTask() {
		return new Task();
	}

	@Override
	public Task createTask(String title, String desc, Calendar date) {
		if(title!=null && !title.isEmpty() && desc!=null && date!=null){
			return new Task(title, desc, date);
		}
		return null;
	}

	
	@Override
	public boolean addTask(Task task){
		boolean result;
		result = (taskList.add(task))? true: false;
		notifyObservers();
		return result;
	}

	@Override
	public void addTasks(List<? extends Task> list) {
		if (list !=null && !list.isEmpty()){ // нужна ли здесь проверка, ведь в контроллер не выносим данный метод
			taskList.addAll(list);
			notifyObservers();
		}
	}


	@Override
	public boolean deleteTask(String title){
		Task task = (title != null && !title.isEmpty())? returnReferenceOnTask(title): null;
		if (task != null){
			taskList.remove(task); // удаляем найденную задачу
			notifyObservers();
			return true; 
		}

		return false;			
	}
	
	@Override
	public void clearTasks(){
		if (taskList != null && !taskList.isEmpty())
			taskList.clear();
		
		notifyObservers();
	}
	
	@Override // безопасный поиск, вернет копию задачи с указанным именем
	public Task searchTask(String title){
		if (title == null || title.isEmpty()) 
			return null; // если входные данные не верны вернем null

		for (Task task : taskList) { //если хеш-коды разные, то и объекты гарантированно разные
//			if (task.getTitle().hashCode() != title.hashCode()) 
//				continue; 	// переходим к след итерации
			// если хеши совпадают, то объекты могут быть равны, проверяем
			if (task.getTitle().equals(title))
				return task;
		}
		return null; // если ничего не нашли вернем null
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

	//! Подумать! менять не объект, а модифицировать поля
	@Override // замена текущей, на указанную
	public boolean replaceTask(String title, Task task) {
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
	public void replaceTasks(List<? extends Task> list) {
		taskList.clear();
		taskList.addAll(list);
		notifyObservers();
	}

	@Override // редактирование полей найденной задачи, можно еще добавить проверку на equals, но не уверен что стоит
	public boolean editTask(String title, String editTitle, String editDescription, Calendar editDate) {
		Task task = returnReferenceOnTask(title); // получили ссыль на нужную таску
		if (task==null)
			return false;
		
		if(editTitle != null && !editTitle.isEmpty()) 
			task.setTitle(editTitle);

		if(editDescription != null && !editDescription.isEmpty()) 
			task.setDescription(editDescription);

		if(editDate != null) 
			task.setDate(editDate);
		
		return true;
	}

	
	@Override
	public Task getTask(int index) {
		return taskList.get(index);
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
		
		public static boolean recordAllData(Journal jrnl,String fileName) throws JAXBException{
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

		public static Journal readAllData(Journal jrnl,String fileName) throws JAXBException{
	        try{
        		//еще проверку на предмет существования данного файла в директории
        		File file = (fileName != null && !fileName.isEmpty())?
        				new File(PATH+"//"+fileName+".xml"): // пользовательский
    					new File(PATH+"//"+fileDefault.getName());				
	        
	    		JAXBContext jaxbContext = JAXBContext.newInstance(PACKAGE);
	            Unmarshaller um = jaxbContext.createUnmarshaller();
	    		
	            return  (Journal) um.unmarshal(file);	            
	    		
			} catch (JAXBException e) { // ставим в конец, т.к JAXB юзаем в конце
//				e.printStackTrace();
				throw new JAXBException(e);
				//return null;
			}
		}
	}
	
	// Эта штука будет принимать последнюю команду, которую получит от контроллера 
	// и выводить соответствующее сообщение, вторым аргументом так же может принимать
	// статус: успешно(true)/неуспешно(выполнено), пока пробуем без статуса
//	public String display(String command, String message){
//		String msg;
//		switch (Command.valueOf(command)) {
//			case ADD:  
//			case DEL: 
//			case SEARCH: 
//			case SHOW_ALL:
//			case CLEAR_ALL: 
//			case HELP: 
//			case STOP: msg = message;
//				break;
//			default:
//				msg = "Вы ввели неверную команду. Попробуйте заново или воспользуйтесь справкой 'help'";
//		}
//		//notifyObservers();
//		return msg;
//
//	}

//	@Override
	public void update(Observable o, Object arg) {
		//this.display(command, message)
//		message = arg.toString();
		notifyObservers(arg); // Доходим сюда, а делее не ок
	}

}
