package model.storage;

// Обход анти-паттерна: Интерфейс для констант
import static model.storage.Constants.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.Taskable;

/**
 * Интерфейс описывающий работу с сохранением данных в xml и последующим восстановлением 
 * 
 * @author Victor Grigorenko
 * 
 */
/* Мысль: Как вариант, что б избавиться от грязи: интерфейс -> абстрактный класс -> класс.
 * Мы задаем дефолтные методы, что б не париться с лишней реализацией, работая с интерфейсами в модели,
 * где всё взаимодействие должно быть построено на абстракциях.
 * Проблема: Использование абстракт класса имеет смысл только если мы хотим избежать дублирования кода при реализации, 
 * а при указании дефолтной реализации это является проблемным.
 * 
 *  Как вариант: задать интерфейс для JAXB и разбить текущий интерфейс на 2: 
 *  для одной таски и для листа тасок. Эти 2 интерфейса экстендят JAXB интерфейс
 * 
 * Для начала реализуем один деф метод и выявим проблему
 */
public interface StorageTaskable {
	
	// все же добавим на всякий случай запись/чтение для одиночных задач,
	// но сделаем ее дефолтной чтоб.
	// обработать IOException, JAXBException
	
	default boolean recordData(Taskable task, String fileName){
		try(FileOutputStream file = (fileName != null && !fileName.trim().isEmpty())?
				new FileOutputStream(PATH+"//"+fileName): // пользовательский
				new FileOutputStream(PATH+"//"+NAME);
			){

			// маршаллинг, пользуем константный класс
			JAXBContext context = JAXBContext.newInstance(PACKAGE);
    		Marshaller m = context.createMarshaller();
    		
    		// эта строка добавляет форматирование в xml файл
    		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true); 
    		m.marshal(task, file); // пишем task в file
    		
    		return true;
    		
		} catch(FileNotFoundException e){
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (JAXBException e) { // ставим в конец, т.к JAXB юзаем в конце
			e.printStackTrace();
		}
		//если не ок			
		return false;
	}
	

	/** 
	 * @param fileName - имя считываемого файла: *.xml, 
	 * для использования имени по умолчанию передавайте null
	 * @return в случае успеха вернет задачу, иначе null
	 */
	//.. нужно подумать стоит ли пробрасывать исключения выше...
	default Taskable readData(String fileName){
		//здесь проверка на корректность ввода файла, null и вообще если он найден
		try(FileInputStream file = (fileName != null && !fileName.trim().isEmpty())?
				new FileInputStream(PATH+"//"+fileName): // пользовательский
				new FileInputStream(PATH+"//"+NAME);
			){
			
			// унмаршаллинг, пользуем константный класс
			JAXBContext context = JAXBContext.newInstance(PACKAGE);
    		Unmarshaller um = context.createUnmarshaller();
    		
    		return (Taskable) um.unmarshal(file);
    		
		} catch(FileNotFoundException e){
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();

		} catch(JAXBException e){			
			e.printStackTrace();			
		}
		// если все плохо
		return null;
	}
	

}
