package model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class JournalObserver implements Observer{

	private List<Taskable> tasks;// = new ArrayList<>();
	
	@Override
	public void update(Observable journal, Object arg) {
		if (journal != null && !((Journalable<Taskable>) journal).getTasks().isEmpty()){
			tasks = new ArrayList<>(); // это нехорошо, но пока оставим так
			tasks.addAll(((Journalable<Taskable>) journal).getTasks());	        
		}
		
		else tasks.clear(); // если лист пуст, то очищаем его
		
		// Действия по информированию view об изменениях, пока просто будем выводить содержимое листа
		if (tasks.isEmpty())
			System.out.println("JournalObserver: Список задач пуст");
		else
			System.out.println("JournalObserver: "+tasks);
		
	}

}
