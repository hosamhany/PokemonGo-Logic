package gottaCatchEmAll;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import apple.laf.JRSUIConstants.Orientation;

/*Class Description: 
 * - This class generates the maze and outputs the state size of the generated maze.
 * - TODO: startingCell(), finishingCell(), isValidCell(cell)
 * 
 */

class Pair {
	int first, second;

	public Pair(int first, int second) {
		this.first = first;
		this.second = second;
	}

	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Pair))
			return false;
		Pair p = (Pair) o;
		return (p.first == this.first && p.second == this.second);
	}

	public int hashCode() {
		return this.first * this.second;
	}
}

public class Maze {
	private int rows;
	private int columns;
	private int stepsToHatch;
	private int noOfPokemons;
	private int orienation;

	HashSet<Pair> walls;
	boolean[] vis;
	int[] dx = { 0, 1, 0, -1 };
	int[] dy = { 1, 0, -1, 0 };
	int[] trans;

	HashSet<Integer> pokemonPos;
	HashSet<Integer> pokePosCopies;
	int initialCell, goalCell;

	public void generateInitCells() {
		int pokemon_count = Math.min(rows, columns);
		pokemonPos = new HashSet<Integer>();
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for (int i = 0; i < rows * columns; i++) {
			arr.add(i);
		}
		Collections.shuffle(arr);
		for (int i = 0; i < pokemon_count; i++) {
			pokemonPos.add(arr.get(i));
		}
		pokePosCopies = (HashSet<Integer>) pokemonPos.clone();
		initialCell = arr.get(pokemon_count);
		goalCell = arr.get(pokemon_count + 1);
	}

	public int getNumberOfPokes() {
		return pokemonPos.size();
	}

	public Maze(int rows, int columns) {
		// TODO: generate maze with the size of attributes passed
		this.rows = rows;
		this.columns = columns;
		trans = new int[4];
		trans[0] = 1;
		trans[1] = columns;
		trans[2] = -1;
		trans[3] = -columns;
		walls = new HashSet<Pair>();
		stepsToHatch = Math.min(this.rows, this.columns);

		orienation = (int) Math.random() * 4;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (inBounds(i + 1, j)) {
					walls.add(new Pair(i * columns + j, (i + 1) * columns + j));
					// System.out.println("Wall between (" + (i * columns + j) +
					// "," + ((i + 1) * columns + j) + ")");
				}
				if (inBounds(i, j + 1)) {
					walls.add(new Pair(i * columns + j, i * columns + j + 1));
					// System.out.println("Wall between (" + (i * columns + j) +
					// "," + (i * columns + j + 1) + ")");
				}
			}
		}
		initMaze();
		generateInitCells();
		noOfPokemons = this.pokemonPos.size();
	}

	public void initMaze() {
		// int[] parent = new int[rows * columns];
		// Arrays.fill(parent, -1);
		vis = new boolean[rows * columns];
		int startNode = (int) Math.floor(Math.random() * (rows * columns));
		dfs(startNode);
	}

	public void dfs(int u) {
		// System.out.println("[dfs] node " + u);
		vis[u] = true;
		ArrayList<Integer> candidates = new ArrayList<Integer>();
		int x, y;
		x = u / columns;
		y = u % columns;
		for (int i = 0; i < 4; i++) {
			if (inBounds(x + dx[i], y + dy[i])) {
				// System.out.println("\t node " + (x + dx[i]) + ", " + (y +
				// dy[i]) + " is in bounds");
				candidates.add((u + trans[i]));
				// System.out.println(trans[i]);
				// System.out.println("Added " + (u + trans[i]) + " to cand
				// list");
			}
		}
		Collections.shuffle(candidates);
		for (int i = 0; i < candidates.size(); i++) {
			// System.out.println("\t tried to go to node " +
			// candidates.get(i));
			int v = candidates.get(i);
			if (vis[v])
				continue;
			walls.remove(new Pair(Math.min(u, v), Math.max(u, v)));
			// System.out.println("removed wall between " + u + " and " + v);
			dfs(v);
			// parent[v] = u;
		}
	}

	public Pair numToPair(int num) {
		return new Pair(num / columns, num % columns);
	}

	public int pairtoNum(Pair pr) {
		return pr.first * columns + pr.second;
	}

	public boolean inBounds(int x, int y) {
		return (x >= 0 && x < rows && y >= 0 && y < columns);
	}

	public boolean inBounds(Pair pr) {
		return inBounds(pr.first, pr.second);
	}

	public boolean inBounds(int cl) {
		int x = cl / columns, y = cl % columns;
		return inBounds(x, y);
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public void printWalls() {
		for (Pair p : walls) {
			System.out.println(p.first + " " + p.second);
		}
	}

	public void printMaze() {
		System.out.print("*");
		for (int i = 0; i < columns; i++) {
			System.out.print("***");
		}
		System.out.println();
		for (int blk = 0; blk < rows; blk++) {
			for (int i = 0; i < 2; i++) {
				System.out.print("*");
				for (int j = 0; j < columns; j++) {
					System.out.print("  ");
					int aa = pairtoNum(new Pair(blk, j));
					int bb = pairtoNum(new Pair(blk, j + 1));
					if (inBounds(blk, j + 1) && !walls.contains(new Pair(aa, bb))) {
						System.out.print(" ");
					} else {
						System.out.print("*");
					}
				}
				System.out.println();
			}
			System.out.print("*");
			for (int i = 0; i < columns; i++) {
				int aa = pairtoNum(new Pair(blk, i));
				int bb = pairtoNum(new Pair(blk + 1, i));
				if (inBounds(blk + 1, i) && !walls.contains(new Pair(aa, bb))) {
					System.out.print("  ");
				} else {
					System.out.print("**");
				}
				System.out.print("*");
			}
			System.out.println();
		}
		System.out.println("goal cell (" + goalCell / rows + ", " + goalCell % rows + ")");
		System.out.println("Pokemon positions");
		for (Integer i : pokePosCopies) {
			System.out.println(i / rows + ", " + i % rows);
		}
		System.out.println();
		System.out.println("Starting position: " + initialCell / rows + ", " + initialCell % rows);
		// for (int i = 0; i < rows; i++) {
		// System.out.print("|");
		// for(int j = 0; j < columns; j++) {
		// int cur_node = i * columns + j;
		// int below_node = cur_node + columns;
		// if (inBounds(below_node)) {
		// if (walls.contains(new Pair(cur_node, below_node))) {
		// System.out.print("_");
		// } else {
		// System.out.print(" ");
		// }
		// }
		// int side_node = cur_node + 1;
		// if (inBounds(side_node)) {
		// if (walls.contains(new Pair(cur_node, side_node))) {
		// System.out.print("|");
		// } else {
		// System.out.print(" ");
		// }
		// }
		// }
		// System.out.println("|\n");
		// }
		// for(int i = 0; i < columns * 2 + 1; i++) {
		// System.out.print("-");
		// }
	}

	public int getGoalCell() {
		return goalCell;
	}

	public boolean isValidMove(int orgX, int orgY, int newX, int newY) {
		if (!inBounds(newX, newY)) {
			return false;
		}
		int oldCell = orgX * rows + orgY;
		int newCell = newX * rows + newY;
		return (!walls.contains(new Pair(Math.min(oldCell, newCell), Math.max(oldCell, newCell))));
	}

	public boolean hasPokemon(int x, int y) {
		if (pokemonPos.contains(x * rows + y)) {
			pokemonPos.remove(x * rows + y);
			return true;
		}
		return false;
	}

	public int getInitialCell() {
		return initialCell;
	}

	public boolean isGoalCell(int x, int y) {
		return (goalCell == (x * rows + y));
	}

	public HashSet<Integer> getPokemonPos() {
		return pokemonPos;
	}

	public void setPokemonPos(HashSet<Integer> pokemonPos) {
		this.pokemonPos = pokemonPos;
	}

	public String exportMaze() {
		int x1, x2, y1, y2;
		String KB = "";
		ArrayList<Integer> parameters;
		for (Pair pr : this.walls) {
			x1 = pr.first / this.columns;
			y1 = pr.first % this.columns;
			x2 = pr.second / this.columns;
			y2 = pr.second % this.columns;
			// System.out
			// .println("Wall positions: \n X1:" + x1 + ", " + "Y1:" + y1 + ", "
			// + "X2:" + x2 + ", " + "Y2:" + y2);
			parameters = new ArrayList<Integer>();
			parameters.add(x1);
			parameters.add(y1);
			parameters.add(x2);
			parameters.add(y2);
			KB = KB + createPredicate("wall", parameters) + ".\n";
		}
		// System.out.println("Rows:" + this.rows + "Columns:" + this.columns);
		// boundx, boundary of rows
		parameters = new ArrayList<Integer>();
		parameters.add(this.rows);
		KB = KB + createPredicate("boundx", parameters) + ".\n";
		// boundy, boundary of columns
		parameters = new ArrayList<Integer>();
		parameters.add(this.columns);
		KB = KB + createPredicate("boundy", parameters) + ".\n";
		// System.out.println(
		// "Initial Cell \n X:" + this.initialCell / this.columns + "Y:" +
		// this.initialCell % this.columns);
		parameters = new ArrayList<Integer>();
		parameters.add(this.initialCell / this.columns);
		parameters.add(this.initialCell % this.columns);
		parameters.add(-1);
		parameters.add(-2);
		KB = KB + createPredicate("at", parameters) + ".\n";
		// System.out.println("Goal Cell \n X:" + this.goalCell / this.columns +
		// "Y:" + this.goalCell % this.columns);
		parameters = new ArrayList<Integer>();
		parameters.add(this.goalCell / this.columns);
		parameters.add(this.goalCell % this.columns);
		KB = KB + createPredicate("goal", parameters) + ".\n";
		for (Integer pos : this.pokemonPos) {
			x1 = pos.intValue() / this.columns;
			y1 = pos.intValue() % this.columns;
			parameters = new ArrayList<Integer>();
			parameters.add(x1);
			parameters.add(y1);
			parameters.add(-1);
			parameters.add(-2);
			KB = KB + createPredicate("haspokemon", parameters) + ".\n";
			// System.out.println("Pokemon position \n X1:" + x1 + ", " + "Y1:"
			// + y1);
		}
		String or = "";
		ArrayList<String> orPar = new ArrayList<String>();
		switch (orienation) {
		case (0):
			or = "north";
			break;
		case (1):
			or = "east";
			break;
		case (2):
			or = "south";
			break;
		case (3):
			or = "west";
			break;
		default:
			break;
		}
		orPar.add(or);
		orPar.add("none");
		orPar.add("s0");
		KB = KB + createPredicateOr("orientation", orPar) + ".\n";
		parameters = new ArrayList<Integer>();
		parameters.add(this.noOfPokemons);
		parameters.add(-1);
		parameters.add(-2);
		KB = KB + createPredicate("pokemonsleft", parameters) + ".\n";
		parameters = new ArrayList<Integer>();
		parameters.add(this.stepsToHatch);
		parameters.add(-1);
		parameters.add(-2);
		KB = KB + createPredicate("stepstohatch", parameters) + ".\n";
		// System.out.println("Number of Pokemons:" + this.noOfPokemons + "
		// Steps to Hatch:" + this.stepsToHatch
		// + " Orientation:" + this.orienation);
		return KB;
	}

	public static String createPredicateOr(String name, ArrayList<String> orParameter) {
		String params = "";
		for (String parameter : orParameter) {
			if (params.equals("")) {
				params = params + parameter;
			} else
				params = params + "," + parameter;
		}
		return name + "(" + params + ")";
	}

	public static String createPredicate(String name, ArrayList<Integer> parameters) {
		String params = "";
		for (Integer parameter : parameters) {
			String param1;
			if (params.equals("")) {
				params = params + parameter;
			} else {
				if (parameter == -1)
					param1 = "none";
				else if (parameter == -2) {
					param1 = "s0";
				} else {
					param1 = parameter + "";
				}
				params = params + "," + param1;
			}
		}
		return name + "(" + params + ")";
	}

	public static void writeInFile(String a) {
		try {
			List<String> lines = Arrays.asList(a);
			Path file = Paths.get("/Users/hosamhany/Desktop/kb.pl");
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Maze maze = new Maze(2, 2);
		// maze.initMaze();
		maze.printMaze();
		// maze.printWalls();
		// Scanner sc = new Scanner(System.in);
		// while (true) {
		// int a, b, c, d;
		// a = sc.nextInt();
		// b = sc.nextInt();
		// c = sc.nextInt();
		// d = sc.nextInt();
		// System.out.println(maze.isValidMove(a, b, c, d));
		// }
		System.out.println(maze.exportMaze());
		maze.writeInFile(maze.exportMaze());
	}
}
