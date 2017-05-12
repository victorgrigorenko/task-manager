package view;

import java.util.Observable;
import java.util.Observer;

public class View  implements Observer{

	public View(){
	}

	@Override 
	public void update(Observable controllerObserver, Object arg) {
		if (arg != null && !arg.toString().isEmpty()){
			System.out.print("view: "+arg);
		}		
	}

}
