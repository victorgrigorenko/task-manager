


import java.io.IOException;

//	Это тестовый полигон
//	Здесь пробуем различные вещи, вроде маршаллинга в XML и т.д.


import controller.JournalController;
import model.*;
//import model.storage.StorageListTask;
import view.View;


public class MainTest{
	
	public static void main(String[] args) throws IOException{ // по идее нам это нужно самим обрабатывать		

		Journal model = new Journal();	// создаем модель

		JournalController controller = JournalController.newInstance(model); // создаем контроллер

		View view = new View();	// создаем представление, будет наблюдать за сообщениями из контроллера
		
		// наблюдатели за сообщениями из контроллера
//		ControllerObserver controllerObserver = new ControllerObserver();	// создаем наблюдателя за кнтроллером
		
		model.addObserver(view); 		// добаляем наблюдателя за моделью
		controller.addObserver(model);	// добаляем наблюдателя за контроллером
//		controllerObserver.addObserver(view);// добаляем наблюдателя за наблюдателем контроллера
		
		controller.start();
        
	}
}
