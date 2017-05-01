package view;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import model.ControllerObserver;
import model.Journal;
import model.Journalable;
import model.Taskable;
// Эта штука выводит сообщения, которые мы попрождаем  контроллере и пробрасываем через модель
public class JournalMessageObserver implements Observer{
//	private ControllerObserver controllerObserver;

//	private List<Taskable> tasks;// = new ArrayList<>();
	
	@Override // тут некий экшен для представления должен быть, пока что пусть будет так
	public void update(Observable controllerObserver, Object arg) {
		if (arg != null && !(arg.toString()=="")){
			System.out.print("JournalMessageObserver: "+arg);
		}
		
		else
			System.out.println("JournalMessageObserver: пустое сообщение");
		
	}
	
//	public JournalMessageObserver(Observable controllerObserver){
//		this.controllerObserver = (ControllerObserver) controllerObserver;
//	}

}
