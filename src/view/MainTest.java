package view;

import static constants.Constants.*;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

//	Это тестовый полигон
//	Здесь пробуем различные вещи, вроде маршаллинга в XML и т.д.

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import controller.JournalController;
import model.*;
//import model.storage.StorageListTask;


public class MainTest{
	
	public static void main(String[] args){ // по идее нам это нужно самим обрабатывать		
		// создаем модель
        Journal<Task> jrnl = new Journal<>();
        // создаем контроллер
        JournalController<Task> controller = (JournalController<Task>) JournalController.newInstance(jrnl);
        // Cоздадим наблюдателя
        jrnl.addObserver(new JournalObserver());
        
        // создаем представление
        // некий код...
        
        // пока просто добавляем данные руками
        controller.addTask();
        controller.addTask("Задание номер ноль=Р");
        controller.addTask("Покупки","Купить картоху и лук");
        controller.addTask("Учеба","Попробовать поучиться =D");
        controller.addTask("Режим","Восстановить его");
        controller.addTask("Контроллер","Написать контроллер",new Date(new Date().getTime()+600_000)); // +10 мин
        
        // Пробуем записать на диск
        System.out.println("\nСохранение данных: "+controller.recordJournal());
        
        // Восстанавливаем в другой объект модели
        Journal<Task> jrnl2 = (Journal<Task>) controller.readJournal();
        
        System.out.println("\nВосстановленные данные: "+jrnl2.getTasks());
        
        
	}
}
