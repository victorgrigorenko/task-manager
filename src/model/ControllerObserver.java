package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ControllerObserver extends Observable implements Observer {

	private List<Observer> observers;
	
	public ControllerObserver(){
		observers = new ArrayList<>();
	}
	
	@Override
	public void addObserver(Observer o) {
		if(o != null)
			observers.add(o);
	}

	@Override
	public void deleteObserver(Observer o) {
		if(o != null)
			observers.remove(o);
	}
	
	
	@Override
	public void notifyObservers(Object arg) {
		for (Observer observer : observers) 
			observer.update(this,arg); 
	}

	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		notifyObservers(arg); // Доходим сюда, а делее не ок
	}

	
}
