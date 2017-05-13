import java.io.IOException;

import controller.JournalController;
import controller.JournalableController;
import model.*;
import view.View;
import view.Viewable;


public class MainTest{
	
	public static void main(String[] args) throws IOException{ 		

		Journalable<Task> model = new Journal();	

		JournalableController<Task> controller = JournalController.newInstance(model); 

		Viewable view = new View();	
		
		model.addObserver(view); 		
		controller.addObserver(model);	
		
		controller.start();
	}
}
