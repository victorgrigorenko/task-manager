package view;

import observer.Observable;

public class View  implements Viewable{

	@Override
	public void display(Object arg) {
		if (arg != null && !arg.toString().isEmpty()){
			System.out.print("view: "+arg);
		}		
	}

	@Override 
	public void update(Observable controllerObserver, Object arg) {
		display(arg);		
	}
}
