import java.util.ArrayList;
import java.util.HashMap;

public class Board {

	public static final int WIDTH = 7;
	public static final int HEIGHT = 9;
	private static final HashMap<Integer,int[]> DIRECTIONS = new HashMap<Integer,int[]>() 
		{{ put(0, new int[] {1, 0});     
		   put(90, new int[] {0, 1});
		   put(180, new int[] {-1, 0});
		   put(270, new int[] {0, -1}); }};
	private int totalBoxes;
	private Box[][] grid;
		   
	public Board() {
		totalBoxes = 0;
		grid = new Box[WIDTH][HEIGHT];
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y ++) {
				grid[x][y] = new Box(x, y, 0);
			}
		}
	}
	
	public Board(Board oldBoard) {
		totalBoxes = oldBoard.totalBoxes;
		grid = new Box[WIDTH][HEIGHT];
		for (int x = 0; x < oldBoard.grid.length; x++) {
			System.arraycopy(oldBoard.grid[x], 0, grid[x], 0, HEIGHT);
		}
	}
	
	public Box getBox(int x, int y) {
		try {
			return grid[x][y];
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	// Need to think about whether to change totalBoxes count here
	public void setBox(int x, int y, int type) {
//		if (getBox(x, y).type == type) {
//			return;
//		}
		grid[x][y].type = type;
	}
	
	public boolean isComplete() {
		return totalBoxes == 0;
	}
	
	public ArrayList<SwapPair> generateSwaps() {
		ArrayList<SwapPair> possibleSwaps = new ArrayList<SwapPair>();
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				Box boxCenter = getBox(x, y);
				Box boxEast = getBox(x + 1, y);
				Box boxNorth = getBox(x, y + 1);
				if (boxEast != null && boxCenter.type != boxEast.type && !(boxCenter.isEmpty() && boxEast.isEmpty())) {
					// Add horizontal swaps
					possibleSwaps.add(new SwapPair(boxCenter, boxEast));
				}
				if (boxNorth != null && boxCenter.type != boxNorth.type && !(boxCenter.isEmpty() && boxNorth.isEmpty())) {
					// Add vertical swaps
					possibleSwaps.add(new SwapPair(boxCenter, boxNorth));
				}
			}
		}
		return possibleSwaps;
	}
	
	public void popBoxes() {
		// TODO: DROP BOXES
		Board newBoard = new Board(this);
		boolean isBoardChanged = true;
		while (isBoardChanged) {
			isBoardChanged = false;
			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < HEIGHT; y++) {
					if (getBox(x,y).isEmpty()) {
						continue;
					}
					for (int[] direction: DIRECTIONS.values()) {
						int count = 0;
						int boxType = getBox(x, y).type;
						Box boxNext;
						do {
							count++;
							int nextX = x + direction[0];
							int nextY = y + direction[1];
							boxNext = getBox(nextX, nextY);
							if (boxNext == null) {
								break;
							}
						} while (boxType == boxNext.type);
						if (count >= 3) {
							for (int i = 0; i < count; i++) {
								int prevX = x - direction[0];
								int prevY = y - direction[0];
								if (!getBox(prevX, prevY).isEmpty()) {
									newBoard.setBox(prevX, prevY, 0);
									newBoard.totalBoxes--;
									isBoardChanged = true;
								}
							}
						}
					}
				}
			}
		}
	}
	
	public String toString() {
		String s = "+-------------+\n";
		for (int i=8; i>=0; i--) {
			s += "|";
			for (int j=0; j<7; j++) {
				s += getBox(j, i).type + "|";
			}
			if (i != 0) {
				s += "\n|-+-+-+-+-+-+-|\n";
			}
		}
		s += "\n+-------------+";
		return s;
	}
	
	public static void main(String[] args) {
	}

}
