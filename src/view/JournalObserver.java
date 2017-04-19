package view;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import model.Journalable;
import model.Taskable;
// наблюдает за изменениями в модели
public class JournalObserver implements Observer{

	private List<Taskable> tasks;// = new ArrayList<>();
	
	@Override // тут некий экшен для представления должен быть, пока что пусть будет так
	public void update(Observable journal, Object arg) {
		if (journal != null && !((Journalable<Taskable>) journal).getTasks().isEmpty()){
			// создаем новый список задач
			tasks = new ArrayList<>(); // это нехорошо, но пока оставим так
			
			// скастим к журналу и добавим в него новый список задач
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
