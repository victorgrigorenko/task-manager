

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

		// создаем модель
		Journal model = new Journal();

		// создаем и добаляем наблюдателя за моделью
		JournalObserver journalObserver = new JournalObserver(model);
		model.addObserver(journalObserver);

		
		// создаем контроллер (модель яляется наблюдателем за контроллером)
		JournalController controller = JournalController.newInstance(model);
		
		// Создание и добавление наблюдателя сообщений из контроллера, через модель
		// наблюдатель в модели, за сообщениями из контроллера
		ControllerObserver controllerObserver = new ControllerObserver();
		// наблюдатель во вью, за сообщениями из controllerObserver(в модели)
		JournalMessageObserver msgObserver = new JournalMessageObserver();
		
		controllerObserver.addObserver(msgObserver);
		controller.addObserver(controllerObserver);
		
		View view = new View(controller);//new Journal<>(new JournalObserver()));
		view.start();
        
	}
}
