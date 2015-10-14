package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.json.JsonArray;
import javax.swing.JFrame;

import map.Map;
import client.ThymioConnector;
import dataanalysis.InfraRed;

import java.awt.BorderLayout;

import javax.swing.*;

public class ThymioInterface implements ActionListener {

	private static final int FREI = 0;

	private ActionListener e;

	private ThymioConnector myConnector;
	private Map map1;
	private InfraRed myIR;

	private int LeftKey = 37;
	private static int UpKey = 38;
	private int RightKey = 39;
	private int DownKey = 40;

	private static JFrame window;
	private static JPanel mapPanel = new JPanel();
	private static JPanel controlPanel = new JPanel();
	private static JPanel statePanel = new JPanel();
	private static JPanel leftSide = new JPanel();

	private static ImageIcon Ecke = new ImageIcon("./resources/ecke.png");
	private static ImageIcon Frontal = new ImageIcon("./resources/frontal.png");
	private static ImageIcon Frei = new ImageIcon("./resources/free.png");
	private static ImageIcon Links = new ImageIcon("./resources/left.png");
	private static ImageIcon Rechts = new ImageIcon("./resources/right.png");
	private static ImageIcon Kante = new ImageIcon("./resources/kante.png");
	private static ImageIcon map = new ImageIcon("./resources/thymiomap.jpg");

	private static JButton fwButton = new JButton();
	private static ImageIcon up_icon = new ImageIcon(
			"./resources/button_fw.png");
	private static JButton bwButton = new JButton();
	private static ImageIcon down_icon = new ImageIcon(
			"./resources/button_bw.png");
	private static JButton leftButton = new JButton();
	private static ImageIcon left_icon = new ImageIcon(
			"./resources/button_left.png");
	private static JButton rightButton = new JButton();
	private static ImageIcon right_icon = new ImageIcon(
			"./resources/button_right.png");
	private static JButton stopButton = new JButton();
	private static ImageIcon stop_icon = new ImageIcon(
			"./resources/button_stop.png");

	private static JButton empty1 = new JButton();
	private static JButton empty2 = new JButton();
	private static JButton empty3 = new JButton();
	private static JButton empty4 = new JButton();

	private static JLabel stateEcke = new JLabel(Ecke);
	private static JLabel stateFrontal = new JLabel(Frontal);
	private static JLabel stateFrei = new JLabel(Frei);
	private static JLabel stateLinks = new JLabel(Links);
	private static JLabel stateRechts = new JLabel(Rechts);
	private static JLabel stateKante = new JLabel(Kante);
	private static JLabel mapLabel = new JLabel(map);

	public ThymioInterface(Map map, InfraRed myIR) {
		System.out.println("STOP! (ThymioInterface)");
		initComponents();
		thymioEvent(null);
		performAction(null);
		updateObstacle(0);
		initArrowKeys();

	}

	public static void main(String[] args) {

		window = new JFrame("Thymio Control");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(700, 900);

		GridLayout FrameLayout = new GridLayout(1, 2);
		GridLayout LeftLayout = new GridLayout(2, 1);

		window.setLayout(FrameLayout);
		window.add(leftSide);
		window.add(mapPanel);
		mapPanel.add(mapLabel);
		leftSide.setLayout(LeftLayout);
		leftSide.add(statePanel);
		leftSide.add(controlPanel);
		// statePanel.add(state);
		statePanel.setLayout(new BorderLayout());
		// statePanel.add(state,BorderLayout.CENTER);

		initArrowKeys();
		window.setVisible(true);

	}

	// public static void main(String[] args) {
	//
	// window = new JFrame("Thymio Control");
	// window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// window.setSize(700, 900);
	//
	// GridLayout FrameLayout = new GridLayout(1, 2);
	// GridLayout LeftLayout = new GridLayout(2, 1);
	//
	// window.setLayout(FrameLayout);
	// window.add(leftSide);
	// window.add(mapPanel);
	// mapPanel.add(mapLabel);
	// leftSide.setLayout(LeftLayout);
	// leftSide.add(statePanel);
	// leftSide.add(controlPanel);
	// statePanel.add(stateEcke);
	// statePanel.setLayout(new BorderLayout());
	// statePanel.add(stateEcke, BorderLayout.CENTER);
	//
	// window.setVisible(true);
	// }

	// private void initArrowKeys() {
	// controlPanel.setSize(200, 200);
	// GridLayout layout = new GridLayout(3, 3);
	// controlPanel.setLayout(layout);
	// controlPanel.add(empty1);
	//
	// empty1.setEnabled(false);
	// fwButton.setIcon(up_icon);
	// controlPanel.add(fwButton);
	// controlPanel.add(empty2);
	// empty2.setEnabled(false);
	// leftButton.setIcon(left_icon);
	// controlPanel.add(leftButton);
	// stopButton.setIcon(stop_icon);
	// controlPanel.add(stopButton);
	// rightButton.setIcon(right_icon);
	// controlPanel.add(rightButton);
	// controlPanel.add(empty3);
	// empty3.setEnabled(false);
	// bwButton.setIcon(down_icon);
	// controlPanel.add(bwButton);
	// controlPanel.add(empty4);
	// empty4.setEnabled(false);
	//
	// setEventListeners();
	//
	// }

	private static void initArrowKeys() {

		System.out.println("initArrowKeys");
		controlPanel.setSize(200, 200);
		GridLayout layout = new GridLayout(3, 3);

		controlPanel.setLayout(layout);
		controlPanel.add(empty1);
		empty1.setEnabled(false);
		fwButton.setIcon(up_icon);
		controlPanel.add(fwButton);
		controlPanel.add(empty2);
		empty2.setEnabled(false);
		leftButton.setIcon(left_icon);
		controlPanel.add(leftButton);
		stopButton.setIcon(stop_icon);
		controlPanel.add(stopButton);
		rightButton.setIcon(right_icon);
		controlPanel.add(rightButton);
		controlPanel.add(empty3);
		empty3.setEnabled(false);
		bwButton.setIcon(down_icon);
		controlPanel.add(bwButton);
		controlPanel.add(empty4);
		empty4.setEnabled(false);
		
		setEventListeners();
	}

	private void initComponents() {

		myConnector = new ThymioConnector(this);
	}

	public void thymioEvent(JsonArray data) {
		String status;
		JsonArray obstacles;
		JsonArray position;
		int bestClass = FREI;
		double bestProb;

		status = data.getJsonObject(0).getString("status");
		if (status.equals("ok")) {
			obstacles = data.getJsonObject(1).getJsonArray("obstacles");

			bestProb = Double.MIN_VALUE;
			for (int i = 0; i < 6; i++) {
				double p = Double.parseDouble(obstacles.getJsonObject(i)
						.getString("class_" + i));

				if (p > bestProb) {
					bestProb = p;
					bestClass = i;
				}
			}
			position = data.getJsonObject(2).getJsonArray("position");

			updatePosition(
					Double.parseDouble(position.getJsonObject(0).getString(
							"pos_x")),
					Double.parseDouble(position.getJsonObject(1).getString(
							"pos_y")));
			updateObstacle(bestClass);
			window.repaint();
		} else {
			System.out.println("ERROR: " + status);
		}
	}

	protected void performAction(ActionEvent e) {
		if (e.getSource() == ThymioInterface.fwButton) {
			System.out.println("FORWARD!");

			myConnector.sendMessage("set speed 50 50");
		} else if (e.getSource() == ThymioInterface.bwButton) {
			System.out.println("BACKWARD!");
			myConnector.sendMessage("set speed -50 -50");
		} else if (e.getSource() == ThymioInterface.leftButton) {
			System.out.println("LEFT!");
			myConnector.sendMessage("set speed -50 50");
		} else if (e.getSource() == ThymioInterface.rightButton) {
			System.out.println("RIGHT!");
			myConnector.sendMessage("set speed 50 -50");
		} else if (e.getSource() == ThymioInterface.stopButton) {
			System.out.println("STOP!");
			myConnector.sendMessage("set speed 0 0");
		} else
			return;
	}

	private static void setEventListeners() {
		// Forward Button

		fwButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Released");

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Pressed");

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Exited");

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Entered");

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				ActionEvent event = new ActionEvent(fwButton, UpKey, null);
				System.out.println("Clicked");
				//performAction(event);
			}
		});
	}

	private void updatePosition(double posXmm, double posYmm) {

	}

	private void updateObstacle(int obstClass) {
		/*
		 * if(){ statePanel.add(stateEcke);} if(){ statePanel.add(stateFrontal);
		 * } if(){ statePanel.add(stateFrei);} if(){
		 * statePanel.add(stateLinks);} if(){ statePanel.add(stateRechts);}
		 */

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}