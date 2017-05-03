package view;

import java.util.Observable;
import java.util.Observer;

// Эта штука выводит сообщения, которые мы попрождаем  контроллере и пробрасываем через модель
public class JournalMessageObserver implements Observer{

	@Override // тут некий экшен для представления должен быть, пока что пусть будет так
	public void update(Observable controllerObserver, Object arg) {
		if (arg != null && !(arg.toString()=="")){
			System.out.print("JournalMessageObserver: "+arg);
		}		
	}

}
