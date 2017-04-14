package model.storage;

import static model.storage.Constants.NAME;
import static model.storage.Constants.PACKAGE;
import static model.storage.Constants.PATH;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import model.Task;
import model.Taskable;

// List типа Taskable или подклассы, не уверен что это хорошо..
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "storageListTaskable")
@XmlSeeAlso({ Task.class })
public interface StorageListTaskable<T extends Taskable> {

	// что записываем и куда записываем
	default boolean recordAllData(List<?> list, String fileName){
		try(FileOutputStream file = (fileName != null && !fileName.trim().isEmpty())?
				new FileOutputStream(PATH+"//"+fileName): // пользовательский
				new FileOutputStream(PATH+"//"+NAME);
			){

			// маршаллинг, пользуем константный класс
			JAXBContext context = JAXBContext.newInstance(list.getClass());
    		Marshaller m = context.createMarshaller();
    		
    		// эта строка добавляет форматирование в xml файл
    		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true); 
    		m.marshal(list, file); // пишем task в file
    		
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

	// откуда читаем
	@SuppressWarnings("unchecked")
	default List<T> readAllData(String fileName){
		try(FileInputStream file = (fileName != null && !fileName.trim().isEmpty())?
				new FileInputStream(PATH+"//"+fileName): // пользовательский
				new FileInputStream(PATH+"//"+NAME);
			){
			
			// унмаршаллинг, пользуем константный класс
			JAXBContext context = JAXBContext.newInstance(PACKAGE);
    		Unmarshaller um = context.createUnmarshaller();
    		
    		return (List<T>) um.unmarshal(file);
    		
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

	//public void recordAllData(List<Taskable> list, String fileName);

	//boolean recordAllData(List<T> list, String fileName);
	

}
