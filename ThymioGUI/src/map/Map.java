package map;

import java.util.ArrayList;
import java.util.Random;

public class Map {
	private int sizeX, sizeY; // number of elements in each direction 
							  // used to get a discrete model of the environment
	private int thymioX, thymioY; // coordinates of MapElement where Thymio is currently located on.
	private double posX, posY; // current position of Thymio in real units
	private double thymioTheta; // current orientation of Thymio in the global coordinate system

	private MapElement [][] element; // Array of MapElement representing the environment
	private double lengthX, lengthY; // each element in this map covers edgelength^2 square units.
	
	public static final int N = 20; // number of occupied elements
		
	public Map(int x, int y, double lx, double ly) {
		lengthX = lx;
		lengthY = ly;
		sizeX = x;
		sizeY = y;
		
		element = new MapElement[sizeX][sizeY];
		
		initMap();
	}
	
	public double getEdgeLengthX() {
		return lengthX;
	}
	
	public double getEdgeLengthY() {
		return lengthY;
	}
	
	public void setPose(double x, double y, double theta) {
		posX = x;
		posY = y;
		thymioTheta = theta;
	}
	
	public int getThymioX() {
		return thymioX;
	}

	
	public int getThymioY() {
		return thymioY;
	}
	
	public double getPosX() {
		return posX;
	}
	
	public double getPosY() {
		return posY;
	}
	
	public double getThymioOrientation() {
		return thymioTheta;
	}
	
	private void initMap() {
		Random r = new Random();
		ArrayList<Integer> occupiedElements = new ArrayList<Integer>();
		
		// initialize each element of the map
		
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				element[x][y] = new MapElement(x, y);
			}
		}
		
		/*
		// collect N distinct random numbers between 0 and the max number of MapElements in this Map
		
		while (occupiedElements.size() < N) {
			Integer pos = new Integer(r.nextInt(sizeX*sizeY));
			if (!occupiedElements.contains(pos)) occupiedElements.add(pos);
		}
		
		// find MapElement corresponding to each of the numbers and set its state to occupied
		
		for (int i = 0; i < N; i ++) {
			Integer pos = occupiedElements.get(i);
			int x = pos / sizeY;  // integer division by number of columns
			int y = pos % sizeX;  // rest of integer division by number of rows
			
			element[x][y].setOccupied();
		}
		*/
	}
	
	public void printMap() {
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				MapElement e = element[x][y];
				
				System.out.print(e.isOccupied() ? "T" : "F");
				System.out.print("\t");
			}
			
			System.out.print("\n");
		}
	}
	
	public int getSizeX() {
		return sizeX;
	}
	
	public int getSizeY() {
		return sizeY;
	}
}
