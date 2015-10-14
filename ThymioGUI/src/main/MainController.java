package main;

import gui.ThymioInterface;
import dataanalysis.InfraRed;
import map.Map;
import gui.ThymioInterface;

public class MainController {
	
	private ThymioInterface gui;
	private Map map;
	private InfraRed myIR;

	public MainController() {
		myIR = new InfraRed();
		map = new Map(20, 8, 16.5, 17);
		gui = new ThymioInterface(map, myIR);
	}

	public static void main(String[] args) {
		MainController mc = new MainController();
		
		System.out.println("STOP! (MainController)");

	}
	
}



