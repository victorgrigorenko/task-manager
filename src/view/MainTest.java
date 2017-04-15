package view;

import static model.storage.Constants.*;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

//	Это тестовый полигон
//	Здесь пробуем различные вещи, вроде маршаллинга в XML и т.д.

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.*;
//import model.storage.StorageListTask;


public class MainTest{
	
	public static void main(String[] args){ // по идее нам это нужно самим обрабатывать		
		// тестим
        Journal<Task> jrnl = new Journal<>();
        jrnl.addTask(new Task("Задание номер ноль"));
        jrnl.addTask(new Task("Покупки","Купить картоху и лук"));
        jrnl.addTask(new Task("Учеба","Попробовать поучиться =D"));
        jrnl.addTask(new Task("Режим","Восстановить его"));
        jrnl.addTask(new Task("Контроллер","Написать контроллер"));
        
        System.out.println(jrnl.recordJournal(null));
        
        Journal<Task> jrnl2 = (Journal<Task>) jrnl.readJournal(null);

        System.out.println(jrnl2.getTasks());
	}
}
