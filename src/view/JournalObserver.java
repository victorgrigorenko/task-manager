package view;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import model.Journal;
import model.Journalable;
import model.Taskable;
// наблюдает за изменениями в модели
// но выводим только при запросе на вывод журнала

public class JournalObserver implements Observer{
//	private Journal model;

	private List<Taskable> tasks;
	
	@Override // тут некий экшен для представления должен быть, пока что пусть будет так
	public void update(Observable journal, Object arg) {
		// актуализируем список задач 
		if (journal != null && !((Journalable<Taskable>) journal).getTasks().isEmpty()){
			// создаем новый список задач
			tasks = new ArrayList<>(); 			
			// скастим к журналу и добавим в него новый список задач
			tasks.addAll(((Journalable<Taskable>) journal).getTasks());	        
		}		
		else tasks.clear(); // если пришел пустой журнал, то очищаем наш лист
		
		
		// Действия по информированию view об изменениях, пока просто будем выводить содержимое листа
		if (((boolean)arg))
			if (tasks.isEmpty())
				System.out.println("JournalObserver: Список задач пуст");
			else
				System.out.println("JournalObserver: "+tasks);
		
	}
	
	public JournalObserver(){//Journal model){
//		this.model = model;
		tasks = new ArrayList<>();
	}

}
