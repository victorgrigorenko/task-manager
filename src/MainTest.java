

import static constants.Constants.*;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

//	Это тестовый полигон
//	Здесь пробуем различные вещи, вроде маршаллинга в XML и т.д.

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import controller.JournalController;
import model.*;
import view.JournalMessageObserver;
import view.JournalObserver;
//import model.storage.StorageListTask;
import view.View;


public class MainTest{
	
	public static void main(String[] args) throws IOException{ // по идее нам это нужно самим обрабатывать		

		Journal model = new Journal();	// создаем модель

		JournalController controller = JournalController.newInstance(model); // создаем контроллер
		
		JournalObserver journalObserver = new JournalObserver();	// создаем наблюдателя за моделью

		// наблюдатели за сообщениями из контроллера
		ControllerObserver controllerObserver = new ControllerObserver();	// создаем наблюдателя за контроллером
		JournalMessageObserver msgObserver = new JournalMessageObserver();	// создаем наблюдателя за наблюдателем контроллера
		
		model.addObserver(journalObserver); 		// добаляем наблюдателя за моделью
		controller.addObserver(controllerObserver);	// добаляем наблюдателя за контроллером
		controllerObserver.addObserver(msgObserver);// добаляем наблюдателя за наблюдателем контроллера
		
		View view = new View(controller);	// создаем представление
		view.start();
        
	}
}
