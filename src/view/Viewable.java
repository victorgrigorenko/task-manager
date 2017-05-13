package view;

import observer.Observer;

public interface Viewable extends Observer{
	void display(Object arg);	
}
