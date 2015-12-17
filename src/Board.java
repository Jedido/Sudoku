import java.awt.Font;


public class Board {
//BOARD LAYOUT:
/*
Cell 1 at [0][0] Bottom-Left, cell 9 and [8][8] Top-Right
*/

	private class Cell {
		private final Font MINI = new Font("Arial", Font.BOLD, 12);
		private final Font NORMAL = new Font("Arial", Font.PLAIN, 32);
		private final double TEXT_OFFSET_X = 0.5;
		private final double TEXT_OFFSET_Y = 0.42;
		int xPos, yPos, num, left;
		boolean[] possible;
		public Cell(int x, int y) {
			xPos = y;
			yPos = x;
			left = 9;
			possible = new boolean[9];
			for(int i = 0; i < possible.length; i++) {
				possible[i] = true;
			}
		}
		public Cell(int x, int y, int num) {
			this(x, y);
			if(num != 0) {
				setNum(num);
			}
		}
		public Cell(Cell c) {
			xPos = c.xPos;
			yPos = c.yPos;
			left = c.left;
			num = c.num;
			possible = new boolean[9];
			for(int i = 0; i < possible.length; i++) {
				possible[i] = c.possible[i];
			}
		}
		public void setNum(int num) {
			this.num = num;
			left = 0;
			for(int i = 0; i < 9; i++) {
				if(possible[i]) {
					possible[i] = false;
					tick(this, i + 1);
				}
			}
		}
		public boolean isPossible(int num) {
			return possible[num];
		}
		public void eliminate(int num) {
			if(!possible[num - 1]) {
				return;
			}
			possible[num - 1] = false;
			left--;
			if(left == 1) {
				for(int i = 0; i < 9; i++) {
					if(possible[i]) {
						this.num = i+1;
						clear(this);
						break;
					}
				}
			}
		}
		public void draw() {
			if(num > 0) {
				StdDraw.setPenColor(50, 50, 50);
				StdDraw.setFont(NORMAL);
				StdDraw.text(xPos + TEXT_OFFSET_X, yPos + TEXT_OFFSET_Y, num + "");
			} else {
//				StdDraw.setPenColor(200, 200, 200);
//				StdDraw.setFont(MINI);
//				for(int i = 0; i < 9; i++) {
//					if(possible[i]) {
//						StdDraw.text(xPos + (i % 3) / 3.0 + 0.17, yPos + 1 - (i / 3) / 3.0 - 0.2, (i+1)+"");
//					}
//				}
			}
			if(num < 0) {
				StdDraw.setPenColor(220, 255, 220);
				StdDraw.filledRectangle(xPos + 0.5, yPos + 0.5, 0.5, 0.5);
			}
		}
		public int getSquare() {
			return yPos / 3 * 3 + xPos / 3;
		}
		public int getRow() {
			return yPos;
		}
		public int getCol() {
			return xPos;
		}
		public int getNum() {
			return num;
		}
		public int options() {
			return left;
		}
		public String toString() {
			return num + " ";
		}
	}

	private Cell[][] board;
	//int[location][num]
	private int[][] rowTrack, colTrack, squareTrack;
	
	private Board() {
		board = new Cell[9][9];
		rowTrack = new int[9][9];
		colTrack = new int[9][9];
		squareTrack = new int[9][9];
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				rowTrack[i][j] = 9;
				colTrack[i][j] = 9;
				squareTrack[i][j] = 9;
			}
		}
	}
	public Board(Board b) {
		board = new Cell[9][9];
		rowTrack = new int[9][9];
		colTrack = new int[9][9];
		squareTrack = new int[9][9];
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				rowTrack[i][j] = b.rowTrack[i][j];
				colTrack[i][j] = b.colTrack[i][j];
				squareTrack[i][j] = b.squareTrack[i][j];
				board[i][j] = new Cell(b.board[i][j]);
			}
		}
	}
	public Board(int[][] cells) {
		this();
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				board[i][j] = new Cell(i, j, cells[8-i][j]);
			}
		}
//		for(int i = 0; i < 9; i++) {
//			for(int j = 0; j < 9; j++) {
//				if(board[i][j].num != 0) {
//					clear(board[i][j]);
//				}
//			}
//		}
//		if(!isSolved()) {
//			board = brute(this).board;
//		}
	}
	
	public void solve(int[][] cells) {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				board[i][j] = new Cell(i, j, cells[8-i][j]);
			}
		}
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(board[i][j].num != 0) {
					clear(board[i][j]);
				}
			}
		}
		if(!isSolved()) {
			board = brute(this).board;
		}
	}

	private void draw() {
		StdDraw.setCanvasSize(540, 540);
		StdDraw.setScale(0, 9);
		StdDraw.show();
		drawLines();
		drawCells();
	}
	private void drawLines() {
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.004);
		for(double i = 0; i <= 3; i++) {
			StdDraw.line(i*3, 0, i*3, 9);
			StdDraw.line(0, i*3, 9, i*3);
		}
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.setPenRadius(0.002);
		for(double i = 0; i < 9; i++) {
			StdDraw.line(i,  0,  i, 9);
			StdDraw.line(0,  i,  9, i);
		}
	}
	private void drawCells() {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				board[i][j].draw();
			}
		}
	}

	public void clear(Cell c) {
		clearSquare(c);
		clearRow(c);
		clearColumn(c);
		tickAll(c);
	}
	private void clearSquare(Cell c) {
		int squareY = c.getCol() / 3 * 3;
		int squareX = c.getRow() / 3 * 3;
		for(int i = squareX; i < squareX + 3; i++) {
			for(int j = squareY; j < squareY + 3; j++) {
				if(board[i][j].isPossible(c.getNum() - 1)) {
					board[i][j].eliminate(c.getNum());
					tick(board[i][j], c.getNum());
				}
			}
		}
	}
	private void clearRow(Cell c) {
		int row = c.getRow();
		for(int i = 0; i < board.length; i++) {
			if(board[row][i].isPossible(c.getNum() - 1)) {
				board[row][i].eliminate(c.getNum());
				tick(board[row][i], c.getNum());
			}
		}
	}
	private void clearColumn(Cell c) {
		int col = c.getCol();
		for(int i = 0; i < board.length; i++) {
			if(board[i][col].isPossible(c.getNum() - 1)) {
				board[i][col].eliminate(c.getNum());
				tick(board[i][col], c.getNum());
			}
		}
	}

	private void tickAll(Cell source) {
		int squareY = source.getCol() / 3 * 3;
		int squareX = source.getRow() / 3 * 3;
		for(int i = squareX; i < squareX + 3; i++) {
			for(int j = squareY; j < squareY + 3; j++) {
				if(board[i][j].isPossible(source.getNum() - 1)) {
					tick(board[i][j], source.getNum());
				}
			}
		}
		int row = source.getRow();
		for(int i = 0; i < board.length; i++) {
			if(board[row][i].isPossible(source.getNum() - 1)) {
				tick(board[row][i], source.getNum());
			}
		}
		int col = source.getCol();
		for(int i = 0; i < board.length; i++) {
			if(board[i][col].isPossible(source.getNum() - 1)) {
				tick(board[i][col], source.getNum());
			}
		}
	}
	private void tick(Cell c, int num) {
		num--;
		rowTrack[c.getRow()][num]--;
		if(rowTrack[c.getRow()][num] == 1 && !rowContains(c.getRow(), num+1)) {
			int find = -1;
			for(int i = 0; i < 9; i++) {
				if(board[c.getRow()][i].isPossible(num)) {
					find = i;
					break;
				}
			}
			if(find != -1) {
				board[c.getRow()][find].setNum(num+1);
				clear(board[c.getRow()][find]);
			}
		}
		colTrack[c.getCol()][num]--;
		if(colTrack[c.getCol()][num] == 1 && !colContains(c.getCol(), num+1)) {
			int find = -1;
			for(int i = 0; i < 9; i++) {
				if(board[i][c.getCol()].isPossible(num)) {
					find = i;
					break;
				}
			}
			if(find != -1) {
				board[find][c.getCol()].setNum(num+1);
				clear(board[find][c.getCol()]);
			}
		}
		squareTrack[c.getSquare()][num]--;
		if(squareTrack[c.getSquare()][num] == 1 && !squareContains(c.getSquare(), num+1)) {
			int find = -1;
			for(int i = 0; i < 9; i++) {
				if(board[c.getSquare()/3*3 + i/3][c.getSquare()%3*3 + i%3].isPossible(num)) {
					find = i;
					break;
				}
			}
			if(find != -1) {
				board[c.getSquare()/3*3 + find/3][c.getSquare()%3*3 + find%3].setNum(num+1);
				clear(board[c.getSquare()/3*3 + find/3][c.getSquare()%3*3 + find%3]);
			}
		}
	}

	private boolean rowContains(int row, int num) {
		for(int i = 0; i < 9; i++) {
			if(board[row][i].getNum() == num) {
				return true;
			}
		}
		return false;
	}
	private boolean colContains(int col, int num) {
		for(int i = 0; i < 9; i++) {
			if(board[i][col].getNum() == num) {
				return true;
			}
		}
		return false;
	}
	private boolean squareContains(int square, int num) {
		for(int i = 0; i < 9; i++) {
			if(board[square / 3 * 3 + i / 3][square % 3 * 3 + i % 3].getNum() == num) {
				return true;
			}
		}
		return false;
	}

	public Board brute(Board original) {
		if(!original.hasSolution()) {
			return original;
		}
		Board b = new Board(original);
		int limit = 2;
		while(!b.isSolved()) {
			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 9; j++) {
					if(b.board[i][j].left == limit) {
						for(int k = 0; k < 9; k++) {
							if(b.board[i][j].isPossible(k)) {
								b.board[i][j].setNum(k+1);
								b.clear(b.board[i][j]);
								b = brute(b);
								if(b.isSolved()) {
									original = b;
									return original;
								} else {
									b = new Board(original);
								}
							}
						}
					}
				}
				limit++;
			}
		}
		return b;
	}
	
 	public boolean isSolved() {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(board[i][j].options() > 0) {
//					System.out.println("failed? " + i + " " + j + " " + board[i][j].options());
					return false;
				}
			}
		}
		//Should return hasSolution()
		return hasSolution();
	}
 	public boolean hasSolution() {
 		//Only checks for contradictions currently on board
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(board[i][j].options() == 0 && board[i][j].getNum() == 0) {
					return false;
				}
			}
		}
		return checkSquares() && checkRows() && checkCols();
 	}
 	private boolean checkSquares() {
		boolean[][] checklist = new boolean[9][9]; 		
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				Cell current = board[i][j];
				if(current.getNum() != 0) {
					checklist[i / 3 * 3 + j / 3][current.getNum() - 1] = true;
				}
				for(int k = 0; k < 9; k++) {
					if(current.isPossible(k)) {
						checklist[i / 3 * 3 + j / 3][k] = true;						
					}
				}
			}
		}
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(checklist[i][j] == false) {
					return false;
				}
			}
		}
		return true;
 	}
	private boolean checkRows() {
		boolean[][] checklist = new boolean[9][9];
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				Cell current = board[i][j];
				if(current.getNum() != 0) {
					checklist[i][current.getNum() - 1] = true;
				}
				for(int k = 0; k < 9; k++) {
					if(current.isPossible(k)) {
						checklist[i][k] = true;						
					}
				}
			}
		}
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(checklist[i][j] == false) {
					return false;
				}
			}
		}
		return true;
	}
	private boolean checkCols() {
		boolean[][] checklist = new boolean[9][9];
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				Cell current = board[i][j];
				if(current.getNum() != 0) {
					checklist[j][current.getNum() - 1] = true;
				}
				for(int k = 0; k < 9; k++) {
					if(current.isPossible(k)) {
						checklist[j][k] = true;						
					}
				}
			}
		}
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(checklist[i][j] == false) {
					return false;
				}
			}
		}
		return true;
	}
	
	

	//Manual Solve
	public void select(int x, int y) {
		if(!isValid(x, y) || board[x][y].num != 0) {
			return;
		}
		board[x][y].num = -1;		
		board[x][y].draw();
	}
	
	public boolean isValid(int x, int y) {
		return x >= 0 && y >= 0 && x < 9 && y < 9;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(int i = 8; i >= 0; i--) {
			for(int j = 0; j < 9; j++) {
				s.append(board[i][j].toString());
			}
			s.append("\n");
		}
		return s.toString();
	}
	
	public static void main(String[] args) {
		int[][] easy1 = {
				{0, 0, 0, 0, 0, 8, 0, 0, 4},
				{0, 8, 4, 0, 1, 6, 0, 0, 0},
				{0, 0, 0, 5, 0, 0, 1, 0, 0},
				{1, 0, 3, 8, 0, 0, 9, 0, 0},
				{6, 0, 8, 0, 0, 0, 4, 0, 3},
				{0, 0, 2, 0, 0, 9, 5, 0, 1},
				{0, 0, 7, 0, 0, 2, 0, 0, 0},
				{0, 0, 0, 7, 8, 0, 2, 6, 0},
				{2, 0, 0, 3, 0, 0, 0, 0, 0}
		};
		int[][] easy2 = {
				{0, 0, 0, 0, 8, 0, 0, 0, 9},
				{0, 7, 6, 2, 0, 0, 0, 3, 0},
				{9, 0, 0, 0, 0, 0, 0, 6, 0},
				{4, 0, 0, 1, 0, 2, 3, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 2, 0},
				{0, 2, 0, 6, 7, 8, 0, 0, 0},
				{8, 0, 0, 0, 0, 7, 0, 0, 1},
				{0, 0, 3, 5, 0, 0, 0, 9, 4},
				{2, 0, 5, 0, 0, 0, 6, 0, 0}
		};
		int[][] medium1 = {
				{0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 3, 0, 8, 5},
				{0, 0, 1, 0, 2, 0, 0, 0, 0},
				{0, 0, 0, 5, 0, 7, 0, 0, 0},
				{0, 0, 4, 0, 0, 0, 1, 0, 0},
				{0, 9, 0, 0, 0, 0, 0, 0, 0},
				{5, 0, 0, 0, 0, 0, 0, 7, 3},
				{0, 0, 2, 0, 1, 0, 0, 0, 0},
				{0, 0, 0, 0, 4, 0, 0, 0, 9}
		};
		int[][] brute1 = {
				{0, 9, 0, 0, 0, 3, 6, 0, 0},
				{0, 0, 0, 1, 0, 0, 2, 0, 0},
				{3, 0, 2, 0, 0, 6, 0, 9, 8},
				{0, 0, 0, 0, 0, 0, 1, 2, 5},
				{0, 0, 4, 0, 0, 0, 8, 0, 0},
				{5, 2, 9, 0, 0, 0, 0, 0, 0},
				{2, 4, 0, 7, 0, 0, 5, 0, 3},
				{0, 0, 3, 0, 0, 2, 0, 0, 0},
				{0, 0, 8, 3, 0, 0, 0, 1, 0}
		};
		Board b = new Board(medium1);
		b.draw();
		while(!b.isSolved()) {
			if(StdDraw.mousePressed()) {
//				System.out.println("(" + StdDraw.mouseX() + ", " + StdDraw.mouseY() + ")");
				int x = (int) StdDraw.mouseY();
				int y = (int) StdDraw.mouseX();
				b.select(x, y);
			}
		}
	}
	
}
