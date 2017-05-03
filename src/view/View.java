package view;

import static constants.Command.*;
import static constants.Exception.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import constants.Command;
import controller.JournalController;
import model.Journal;
import model.Task;
import model.Taskable;

public class View {
	JournalController controller;

	public View(JournalController controller) {
		this.controller = controller;
	}

	public void start() throws IOException {
		System.out.println("Cтарт: ");
		controller.commandRead();
	}


}
