package view;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

//Это тестовый полигон
//Здесь пробуем различные вещи, вроде маршаллинга в XML

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.*;

public class MainTest{
	// получили пакет класса через рефлекшион
	static final String PACKAGE = Journal.class.getPackage().getName();
	
	public static void main(String[] args){ // по идее нам это нужно самим обрабатывать		
        // тестим
		
        Journal jrnl = new Journal();
        jrnl.addTaskToList(new Task("Задание номер ноль"));
        jrnl.addTaskToList(new Task("Покупки","Купить картоху и лук"));
        jrnl.addTaskToList(new Task("Учеба","Попробовать поучиться =D"));
        
        try{
        	File file = new File("E:\\IT\\javaTest\\JAXBDemo.xml");
        
    		JAXBContext jc = JAXBContext.newInstance(PACKAGE);
    		
    		Marshaller m = jc.createMarshaller();
    		Unmarshaller um = jc.createUnmarshaller();
    		
    		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true); // эта строка добавляет форматирование в xml файл
    		m.marshal(jrnl, file);
    		
    		Journal jrnl2 = (Journal) um.unmarshal(file);
    		
    		System.out.println(jrnl2.getTasks().toString());
  
    		
        } catch (JAXBException e) {
			System.out.println(e+" НЕУСПЕХ!");
			
		} 
//        finally{
//			try{
//				if(file != null) file.close();
//			}
//			catch(IOException ехс) {
//				System.out.println("Oшибкa при закрытии файла");
//			}
//        }

	}
}
