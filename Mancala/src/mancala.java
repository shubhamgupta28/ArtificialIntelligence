
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//trim
//changes made
//terminal equal 
//setflag affectP1
class Config {
	private String task;
	private int playerNo;
	private int CutOff;
	private List<Integer> bState2 = new ArrayList<Integer>();
	private List<Integer> bState1 = new ArrayList<Integer>();
	private int mancalaStonesInP2;
	private int mancalaStonesInP1;
	private int startPLayer;
	private String moveHistory = "root";
	private boolean extraMoveFlag = false;
	private int depth = 0;
	private int value = 0;

	public Config(List<String> list1) {
		List<String> tempListP1 = new ArrayList<String>();
		List<String> tempListP2 = new ArrayList<String>();
		task = list1.get(0);
		startPLayer = Integer.parseInt(list1.get(1));
		playerNo = Integer.parseInt(list1.get(1));
		CutOff = Integer.parseInt(list1.get(2));

		tempListP2 = Arrays.asList(list1.get(3).split(" "));
		for (String strVal : tempListP2) {
			bState2.add(Integer.parseInt(strVal));
		}
		tempListP1 = Arrays.asList(list1.get(4).split(" "));
		for (String strVal : tempListP1) {
			bState1.add(Integer.parseInt(strVal));
		}
		mancalaStonesInP2 = Integer.parseInt(list1.get(5));
		mancalaStonesInP1 = Integer.parseInt(list1.get(6));
	}

	public Config(Config a, String move, int depth, int playerNo) {
		this.task = a.task;
		this.startPLayer = a.startPLayer;
		this.CutOff = a.CutOff;
		this.mancalaStonesInP1 = a.mancalaStonesInP1;
		this.mancalaStonesInP2 = a.mancalaStonesInP2;
		this.bState1 = new ArrayList<Integer>(a.bState1);
		this.bState2 = new ArrayList<Integer>(a.bState2);
		this.moveHistory = a.moveHistory + ":" + move;
		this.depth = depth;
		this.playerNo = playerNo;
		this.value = a.value;
	}

	public Config(Config a) {
		this.task = a.task;
		this.startPLayer = a.startPLayer;
		this.CutOff = a.CutOff;
		this.mancalaStonesInP1 = a.mancalaStonesInP1;
		this.mancalaStonesInP2 = a.mancalaStonesInP2;
		this.bState1 = new ArrayList<Integer>(a.bState1);
		this.bState2 = new ArrayList<Integer>(a.bState2);
		this.moveHistory = a.moveHistory;
		this.depth = a.depth;
		this.playerNo = a.playerNo;
		this.value = a.value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getStartPLayer() {
		return startPLayer;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public boolean isExtraMoveFlag() {
		return extraMoveFlag;
	}

	public void setExtraMoveFlag(boolean extraMoveFlag) {
		this.extraMoveFlag = extraMoveFlag;
	}

	public int evalFunc() {
		if (startPLayer == 1) {
			return getMancalaStonesInP1() - getMancalaStonesInP2();
		} else
			return getMancalaStonesInP2() - getMancalaStonesInP1();

	}

	public String getMoveHistory() {
		return moveHistory;
	}

	public void setMoveHistory(String moveHistory) {
		this.moveHistory = moveHistory;
	}

	public String getTask() {
		return task;
	}

	public int getPlayerNo() {
		return playerNo;
	}

	public void setPlayerNo(int playerNo) {
		this.playerNo = playerNo;
	}

	public int getCutOff() {
		return CutOff;
	}

	public List<Integer> getbState2() {
		return bState2;
	}

	public List<Integer> getbState1() {
		return bState1;
	}

	public int getMancalaStonesInP2() {
		return mancalaStonesInP2;
	}

	public void setMancalaStonesInP2(int manacalaStonesInP2) {
		this.mancalaStonesInP2 = manacalaStonesInP2;
	}

	public int getMancalaStonesInP1() {
		return mancalaStonesInP1;
	}

	public void setMancalaStonesInP1(int mancalaStonesInP1) {
		this.mancalaStonesInP1 = mancalaStonesInP1;
	}

	@Override
	public String toString() {
		return "Config [bState2=" + bState2 + ",bState1=" + bState1 + ", value=" + value + ", depth=" + depth + ",task="
				+ task + ", playerNo=" + playerNo + ", CutOff=" + CutOff + ", bState2=" + bState2 + ", bState1="
				+ bState1 + ", mancalaStonesInP2=" + mancalaStonesInP2 + ", mancalaStonesInP1=" + mancalaStonesInP1
				+ ", startPLayer=" + startPLayer + ", moveHistory=" + moveHistory + ", extraMoveFlag=" + extraMoveFlag
				+ "]";
	}
}

public class mancala {
	PrintWriter myWriter = null;
	PrintWriter nextStateWriter = null;

	public Config readFile(String fileName) {
		String inputLine = null;
		List<String> arr = new ArrayList<String>();
		Config inputConfig = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((inputLine = bufferedReader.readLine()) != null) {
				if (inputLine.trim().isEmpty()) {
					inputConfig = new Config(arr);
					arr.clear();
				} else {
					arr.add(inputLine.trim());
				}
			}
			if (arr != null && !arr.isEmpty()) {
				inputConfig = (new Config(arr));
				arr.clear();
			} else {
				System.out.println("No records Found!");
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
		return inputConfig;
	}

	public List<String> possibleMoves(Config ConfigList) {
		List<String> possMovesList = new ArrayList<String>();
		String val;
		if (ConfigList.getPlayerNo() == 1) {
			for (int i = 0; i < ConfigList.getbState1().size(); i++) {
				if ((ConfigList.getbState1().get(i)) > 0) {
					val = Integer.toString(i + 2);
					possMovesList.add("B" + val);
				}
			}
		} else if (ConfigList.getPlayerNo() == 2) {
			for (int i = 0; i < ConfigList.getbState2().size(); i++) {
				if ((ConfigList.getbState2().get(i)) > 0) {
					val = Integer.toString(i + 2);
					possMovesList.add("A" + val);
				}
			}
		}
		return possMovesList;
	}

	private void finalSolution(Config config, String nextStateFilename, String traverseLogFilename) {

		int task;
		task = Integer.parseInt(config.getTask());
		switch (task) {

		case 1: {
			greedySolution(config, nextStateFilename);
			break;
		}
		case 2: {
			minimaxSolution(config, nextStateFilename, traverseLogFilename);
			break;
		}
		case 3: {
			alphaBetaSolution(config, nextStateFilename, traverseLogFilename);
			break;
		}

		case 4: {
			alphaBetaSolution(config, nextStateFilename, traverseLogFilename);
			break;
		}
		}
	}

	private void alphaBetaSolution(Config config, String nextStateFilename, String traverseLogFilename) {
		Config temp = null;
		try {
			myWriter = new PrintWriter(traverseLogFilename);
			myWriter.println("Node,Depth,Value,Alpha,Beta");
			Config x = alphaBetaSearch(config);
			temp = x;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (myWriter != null) {
				myWriter.close();
			}
		}

		try {
			nextStateWriter = new PrintWriter(nextStateFilename);
			List<String> arrList = new ArrayList<String>();
			List<String> tempList = new ArrayList<String>();
			String varB = "";

			// TODO check for B4:B6 print stae upto there.

			tempList = Arrays.asList(temp.getMoveHistory().split(":"));
			if (temp.getStartPLayer() == 1) {
				varB = "B";
			} else if (temp.getStartPLayer() == 2) {
				varB = "A";
			}
			for (int i = 1; i < tempList.size(); i++) {
				if (tempList.get(i).substring(0, 1).equals(varB)) {
					arrList.add(tempList.get(i));
				} else
					break;
			}

			// use config to play moves
			Config c = config;
			for (String aaa : arrList) {
				c = affect(c, aaa, config.getDepth(), config.getStartPLayer());
			}
			String log = "";

			for (int i = 0; i < c.getbState2().size(); i++) {
				log += c.getbState2().get(i) + " ";
			}
			nextStateWriter.println(log.trim());
			log = "";
			for (int i = 0; i < c.getbState1().size(); i++) {
				log += c.getbState1().get(i) + " ";
			}
			nextStateWriter.println(log.trim());
			nextStateWriter.println(c.getMancalaStonesInP2());
			nextStateWriter.println(c.getMancalaStonesInP1());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (nextStateWriter != null) {
				nextStateWriter.close();
			}
		}
	}

	private Config alphaBetaSearch(Config config) {

		Config w;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		config.setValue(Integer.MIN_VALUE);

		w = alphaMaxValue(config, alpha, beta, 0, config.getPlayerNo());

		return w;
	}

	private Config alphaMinValue(Config config, int alpha, int beta, int depth, int currentPlayer) {
		// current player is already changed
		int changedPlayer = 0;
		List<String> possMovesList = new ArrayList<String>();
		Integer v = Integer.MAX_VALUE;
		Config c, w, temp = null;

		// TERMINAL CONDITION
		if (config.getCutOff() == depth) {
			config.setValue(config.evalFunc());
			alphaPrintLog("mt1", config, config.getDepth(), alpha, beta);
			return config;
		} else if (possibleMoves(config) == null || possibleMoves(config).isEmpty()) {
			config.setValue(config.evalFunc());
			alphaPrintLog("mt2", config, config.getDepth(), alpha, beta);
			return config;
		}

		if (!config.isExtraMoveFlag()) {
			config.setPlayerNo(currentPlayer);
		}

		// calculate poss moves for next player
		possMovesList = possibleMoves(config);

		for (String a : possMovesList) {
			alphaPrintLog("ms", config, config.getDepth(), alpha, beta);

			if (config.isExtraMoveFlag()) {
				c = affect(config, a, depth + 1, currentPlayer);
			} else {
				c = affect(config, a, depth + 1, currentPlayer);
			}

			// use flag here to decide whether a player needs changing or not
			// if same player..keep calling the same function
			if (!c.isExtraMoveFlag()) {
				changedPlayer = changePlayer(currentPlayer);
				c.setValue(Integer.MIN_VALUE);
				w = alphaMaxValue(c, alpha, beta, depth + 1, changedPlayer);
			} else {
				c.setValue(Integer.MAX_VALUE);
				// c.setValue(c.evalFunc());
				w = alphaMinValue(c, alpha, beta, depth, currentPlayer);
			}
			// changing to >
			if (w != null && v > w.evalFunc()) {
				v = w.evalFunc();
				config.setValue(v);
				temp = new Config(w);
			}
			if (v <= alpha) {
				alphaPrintLog("mp", config, config.getDepth(), alpha, beta);
				return temp;
			}
			beta = Math.min(beta, v);

		}
		alphaPrintLog("me", config, config.getDepth(), alpha, beta);
		return temp;
	}

	private Config alphaMaxValue(Config config, int alpha, int beta, int depth, int currentPlayer) {
		int changedPlayer = 0;
		List<String> possMovesList = new ArrayList<String>();
		Integer v = Integer.MIN_VALUE;
		Config c, w, temp = null;

		// TERMINAL CONDITION
		if (config.getCutOff() == depth) {
			config.setValue(config.evalFunc());
			alphaPrintLog("MT1", config, config.getDepth(), alpha, beta);
			return config;
		} else if (possibleMoves(config) == null || possibleMoves(config).isEmpty()) {
			config.setValue(config.evalFunc());
			alphaPrintLog("MT2", config, config.getDepth(), alpha, beta);
			return config;
		}

		// changing player accordingly
		if (!config.isExtraMoveFlag()) {
			config.setPlayerNo(currentPlayer);
		}

		// calculate poss moves for next player
		possMovesList = possibleMoves(config);

		for (String b : possMovesList) {
			alphaPrintLog("MS", config, config.getDepth(), alpha, beta);

			if (config.isExtraMoveFlag()) {
				c = affect(config, b, depth + 1, currentPlayer);
			} else {
				c = affect(config, b, depth + 1, currentPlayer);
			}
			if (!c.isExtraMoveFlag()) {
				changedPlayer = changePlayer(currentPlayer);
				c.setValue(Integer.MAX_VALUE);
				w = alphaMinValue(c, alpha, beta, depth + 1, changedPlayer);
			} else {
				c.setValue(Integer.MIN_VALUE);
				// c.setValue(c.evalFunc());
				w = alphaMaxValue(c, alpha, beta, depth, currentPlayer);
			}

			// changing to <

			if (w != null && v < w.evalFunc()) {
				v = w.evalFunc();
				// i think should be commented
				config.setValue(v);
				temp = new Config(w);
			}
			if (v >= beta) {
				alphaPrintLog("MP", config, config.getDepth(), alpha, beta);
				return temp;
			}
			alpha = Math.max(alpha, v);

		}
		alphaPrintLog("ME", config, config.getDepth(), alpha, beta);
		return temp;
	}

	private String alphaInfinityCheck(int check) {
		String val = "";

		if (check == Integer.MAX_VALUE) {
			val = "Infinity";

		} else if (check == Integer.MIN_VALUE) {
			val = "-Infinity";

		} else {
			val = Integer.toString(check);
		}
		return val;

	}

	private void alphaPrintLog(String s, Config a, int depth, int alpha, int beta) {
		List<String> arrList = new ArrayList<String>();
		List<String> tempList = new ArrayList<String>();

		tempList = Arrays.asList(a.getMoveHistory().split(":"));
		if (tempList.get(0) == null) {
			myWriter.println("root,0" + a.getValue());
		} else {
			for (String strVal : tempList) {
				arrList.add(strVal);
			}

			myWriter.println(arrList.get((arrList.size() - 1)) + "," + depth + "," + alphaInfinityCheck(a.getValue())
					+ "," + alphaInfinityCheck(alpha) + "," + alphaInfinityCheck(beta));

		}

	}

	private String minimaxInfinityCheck(int check) {
		String val = "";

		if (check == Integer.MAX_VALUE) {
			val = "Infinity";

		} else if (check == Integer.MIN_VALUE) {
			val = "-Infinity";

		} else {
			val = Integer.toString(check);
		}
		return val;

	}

	private void printLog(String ab, Config a, int depth) {
		List<String> arrList = new ArrayList<String>();
		List<String> tempList = new ArrayList<String>();

		tempList = Arrays.asList(a.getMoveHistory().split(":"));
		if (tempList.get(0) == null) {
		} else {
			for (String strVal : tempList) {
				arrList.add(strVal);
			}
			// System.out.println(arrList);

			myWriter.println(
					arrList.get((arrList.size() - 1)) + "," + depth + "," + minimaxInfinityCheck(a.getValue()));

			System.out.println(
					ab + arrList.get((arrList.size() - 1)) + "," + depth + "," + minimaxInfinityCheck(a.getValue()));

		}
	}

	private void minimaxSolution(Config config, String nextStateFilename, String traverseLogFilename) {

		// TODO
		Config temp = null;
		try

		{
			myWriter = new PrintWriter(traverseLogFilename);
			myWriter.println("Node,Depth,Value");
			Config x = minimaxDecision(config);
			temp = x;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (myWriter != null) {
				myWriter.close();
			}
		}

		try {
			nextStateWriter = new PrintWriter(nextStateFilename);

			List<String> arrList = new ArrayList<String>();
			List<String> tempList = new ArrayList<String>();
			String varB = "";

			tempList = Arrays.asList(temp.getMoveHistory().split(":"));
			if (temp.getStartPLayer() == 1) {
				varB = "B";
			} else if (temp.getStartPLayer() == 2) {
				varB = "A";
			}

			for (int i = 1; i < tempList.size(); i++) {
				if (tempList.get(i).substring(0, 1).equals(varB)) {
					arrList.add(tempList.get(i));
				} else
					break;
			}

			// use config to play moves
			Config c = config;
			for (String aaa : arrList) {
				c = affect(c, aaa, config.getDepth(), config.getStartPLayer());
			}

			String log = "";
			for (int i = 0; i < c.getbState2().size(); i++) {
				log += c.getbState2().get(i) + " ";
			}
			nextStateWriter.println(log.trim());
			log = "";
			for (int i = 0; i < c.getbState1().size(); i++) {
				log += c.getbState1().get(i) + " ";
			}
			nextStateWriter.println(log.trim());
			nextStateWriter.println(c.getMancalaStonesInP2());
			nextStateWriter.println(c.getMancalaStonesInP1());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (nextStateWriter != null) {
				nextStateWriter.close();
			}
		}

	}

	private int changePlayer(int player) {
		if (player == 1) {
			return 2;
		} else
			return 1;
	}

	private Config minimaxDecision(Config config) {
		Integer v = Integer.MIN_VALUE;
		List<String> possMovesList = new ArrayList<String>();
		Config w = null, temp = null;
		int changedPlayer = 0;
		config.setValue(Integer.MIN_VALUE);

		possMovesList = possibleMoves(config);
		for (String move : possMovesList) {
			// print the parent of the current move
			printLog("mds", config, config.getDepth());
			Config c = affect(config, move, config.getDepth() + 1, config.getStartPLayer());
			if (c.isExtraMoveFlag()) {
				c.setValue(Integer.MIN_VALUE);
				w = maxValue(c, 0, config.getPlayerNo());
			} else {

				changedPlayer = changePlayer(c.getPlayerNo());
				c.setValue(Integer.MAX_VALUE);
				w = minValue(c, 1, changedPlayer);
			}
			if (w != null && v < w.evalFunc()) {
				v = w.evalFunc();
				config.setValue(v);
				temp = new Config(w);
			}

		}
		printLog("mde", config, config.getDepth());
		return temp;
	}

	private Config minValue(Config config, int depth, int currentPlayer) {
		// curent player is already chaanged
		int changedPlayer = 0;
		List<String> possMovesList = new ArrayList<String>();
		Integer v = Integer.MAX_VALUE;
		Config c, w, temp = null;

		// TERMINAL CONDITION
		// leaf node and depth cutoff
		if (config.getCutOff() == depth) {
			config.setValue(config.evalFunc());
			printLog("mvt", config, config.getDepth());
			return config;
		} else if (possibleMoves(config) == null || possibleMoves(config).isEmpty()) {
			config.setValue(config.evalFunc());
			printLog("mvt2", config, config.getDepth());
			return config;
		}

		if (!config.isExtraMoveFlag()) {
			config.setPlayerNo(currentPlayer);
		}

		// calculate poss moves for next player
		possMovesList = possibleMoves(config);

		for (String a : possMovesList) {
			// for(int i=0;i<possMovesList.size();i++)
			printLog("mvs", config, config.getDepth());

			if (config.isExtraMoveFlag()) {
				c = affect(config, a, depth + 1, currentPlayer);
			} else {
				c = affect(config, a, depth + 1, currentPlayer);
			}

			if (!c.isExtraMoveFlag()) {
				changedPlayer = changePlayer(currentPlayer);
				c.setValue(Integer.MIN_VALUE);
				w = maxValue(c, depth + 1, changedPlayer);
			} else {
				c.setValue(Integer.MAX_VALUE);
				// c.setValue(c.evalFunc());
				w = minValue(c, depth, currentPlayer);
			}
			// changing to >
			if (w != null && v > w.evalFunc()) {
				v = w.evalFunc();
				config.setValue(v);
				temp = new Config(w);
			}
		}
		printLog("mve", config, config.getDepth());
		return temp;
	}

	private Config maxValue(Config config, int depth, int currentPlayer) {
		int changedPlayer = 0;
		List<String> possMovesList = new ArrayList<String>();
		Integer v = Integer.MIN_VALUE;
		Config c, w, temp = null;

		// TERMINAL CONDITION
		if (config.getCutOff() == depth) {
			config.setValue(config.evalFunc());
			printLog("MVT1", config, config.getDepth());
			return config;
		} else if (possibleMoves(config) == null || possibleMoves(config).isEmpty()) {
			config.setValue(config.evalFunc());
			printLog("MVT2", config, config.getDepth());
			return config;
		}

		// changing player accordingly
		if (!config.isExtraMoveFlag()) {
			config.setPlayerNo(currentPlayer);
		}

		// calculate poss moves for next player
		possMovesList = possibleMoves(config);

		for (String b : possMovesList) {
			// problem here as parent is being printed with v=-INF value
			printLog("MVS", config, config.getDepth());

			if (config.isExtraMoveFlag()) {
				c = affect(config, b, depth + 1, currentPlayer);
			} else {
				c = affect(config, b, depth + 1, currentPlayer);
			}
			if (!c.isExtraMoveFlag()) {
				changedPlayer = changePlayer(currentPlayer);
				c.setValue(Integer.MAX_VALUE);
				w = minValue(c, depth + 1, changedPlayer);
			} else {
				c.setValue(Integer.MIN_VALUE);
				w = maxValue(c, depth, currentPlayer);
			}

			// changing to <
			if (w != null && v < w.evalFunc()) {
				v = w.evalFunc();
				config.setValue(v);
				temp = new Config(w);
			}
		}
		printLog("MME", config, config.getDepth());
		return temp;
	}

	//
	private void greedySolution(Config config, String nextStateFilename) {
		// we call nextstate and get all the unsorted states possible

		List<Config> CompleteList = nextstates(config);
		// System.out.println(CompleteList);
		// System.out.println(CompleteList);
		// to sort the multiple states
		Collections.sort(CompleteList, new Comparator<Config>() {

			public int compare(Config l1, Config l2) {
				if (l2.evalFunc() == l1.evalFunc()) {

					String[] l1History = l1.getMoveHistory().split(":");
					String[] l2History = l2.getMoveHistory().split(":");

					int loop = Math.min(l1History.length, l2History.length);
					for (int i = 1; i < loop; i++) {
						if (l1History[i].substring(0, 0).equals(l2History[i].substring(1, 1)))
							;
						{
							int diff = Integer.parseInt(l1History[i].substring(1))
									- Integer.parseInt(l2History[i].substring(1));

							if (diff != 0) {
								return diff;
							}

						}
					}
					return l1.getMoveHistory().compareTo(l2.getMoveHistory());
				}
				return l2.evalFunc() - l1.evalFunc();
			}
		});
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(nextStateFilename);
			String log = "";
			for (int i = 0; i < CompleteList.get(0).getbState2().size(); i++) {
				log += CompleteList.get(0).getbState2().get(i) + " ";
			}
			writer.println(log.trim());
			log = "";
			for (int i = 0; i < CompleteList.get(0).getbState1().size(); i++) {

				log += CompleteList.get(0).getbState1().get(i) + " ";
			}
			writer.println(log.trim());
			writer.println(CompleteList.get(0).getMancalaStonesInP2());
			writer.println(CompleteList.get(0).getMancalaStonesInP1());
		} catch (

		FileNotFoundException e)

		{
			e.printStackTrace();
		} finally

		{
			if (writer != null) {
				writer.close();
			}
		}
	}

	private List<Config> nextstates(Config config) {
		List<Config> multipleConfigStates = new ArrayList<Config>();

		List<String> possMovesList = possibleMoves(config);
		for (String move : possMovesList) {

			Config c = affect(config, move, 0, config.getStartPLayer());
			// recursive part for an extra move
			// && possibleMoves(config).isEmpty()

			List<String> list = possibleMoves(c);

			if (c.isExtraMoveFlag() && list != null && !list.isEmpty()) {
				multipleConfigStates.addAll(nextstates(c));
			} else {
				multipleConfigStates.add(c);
			}

		}
		return multipleConfigStates;
	}

	private Config affect(Config config, String move, int depth, int player) {
		// TODO
		// copy constructor object
		if (config.getPlayerNo() == 1) {
			return affectP1(config, move, depth, player);
		} else if (config.getPlayerNo() == 2) {
			return affectP2(config, move, depth, player);
		}
		return null;
	}

	private Config affectP1(Config config, String moveString, int depth, int player) {
		int currMancala, p1Loop = 0;
		boolean captureFlag = false;
		int stonesP1 = config.getMancalaStonesInP1();

		// System.out.println(config);
		Config copyConfig = new Config(config, moveString, depth, player);

		// DECODE MOVE

		int move = (Integer.parseInt(moveString.substring(1)) - 2);

		// stores no of beans present in "move" mancala
		currMancala = copyConfig.getbState1().get(move);

		// sets the selected pit to zero
		copyConfig.getbState1().set(move, 0);

		while (currMancala > 0) {
			for (p1Loop = move + 1; p1Loop < copyConfig.getbState1().size() && currMancala != 0; p1Loop++) {
				if (currMancala > 0) {
					copyConfig.getbState1().set(p1Loop, copyConfig.getbState1().get(p1Loop) + 1);
					currMancala--;
				}
				// capture
				if (currMancala == 0 && copyConfig.getbState1().get(p1Loop) == 1) {
					captureFlag = true;
					break;
				}
			}
			move = -1;

			if (currMancala > 0) {
				stonesP1 = copyConfig.getMancalaStonesInP1();
				stonesP1++;
				// try
				copyConfig.setMancalaStonesInP1(stonesP1);
				currMancala--;

				if (currMancala == 0) {
					copyConfig.setExtraMoveFlag(true);
					// // end state special wali
					// for (int i = 0; i < copyConfig.getbState1().size(); i++)
					// {
					// if (copyConfig.getbState1().get(i) != 0) {
					// // System.out.println(copyConfig.getbState1().get(i));
					//
					// }
					// }
				}
			}
			if (currMancala > 0) {
				for (int i = copyConfig.getbState2().size() - 1; i >= 0; i--) {
					if (currMancala > 0) {
						copyConfig.getbState2().set(i, copyConfig.getbState2().get(i) + 1);
						currMancala--;
					}
				}
			}

		}
		// end of while

		// capture condition in end for P1
		if (captureFlag == true) {

			int capturestones = copyConfig.getbState1().get(p1Loop) + copyConfig.getbState2().get(p1Loop);

			copyConfig.setMancalaStonesInP1(stonesP1 + capturestones);

			// set p2 cell to be zero
			copyConfig.getbState1().set(p1Loop, 0);
			copyConfig.getbState2().set(p1Loop, 0);

		}

		// end state
		int flagP1 = 0, flagP2 = 0, sumP2 = 0, sumP1 = 0;
		for (int j = 0; j < copyConfig.getbState1().size(); j++) {

			if (copyConfig.getbState1().get(j) != 0) {
				flagP1++;
				// flag==0 means P1 is empty
			}
			if (copyConfig.getbState2().get(j) != 0) {
				flagP2++;
			}
		}
		if (flagP2 == 0) {

			for (int j = 0; j < copyConfig.getbState1().size(); j++) {
				// keep adding each remeainng value to sum
				sumP1 = sumP1 + copyConfig.getbState1().get(j);
				// keep setting each value to zero
				copyConfig.getbState1().set(j, 0);
			}
			copyConfig.setMancalaStonesInP1(copyConfig.getMancalaStonesInP1() + sumP1);
		} else if (flagP1 == 0) {

			for (int j = 0; j < copyConfig.getbState2().size(); j++) {
				// keep adding each remeainng value to sum
				sumP2 = sumP2 + copyConfig.getbState2().get(j);
				// keep setting each value to zero
				copyConfig.getbState2().set(j, 0);
			}
			copyConfig.setMancalaStonesInP2(copyConfig.getMancalaStonesInP2() + sumP2);

		}
		return copyConfig;
	}

	private Config affectP2(Config config, String moveString, int depth, int player) {
		int currMancala, i = 0;
		boolean captureFlag = false;
		int stonesP2 = config.getMancalaStonesInP2();

		Config copyConfig = new Config(config, moveString, depth, player);
		// DECODE MOVE

		int move = (Integer.parseInt(moveString.substring(1)) - 2);

		// stores no of beans present in "move" mancala
		currMancala = copyConfig.getbState2().get(move);

		// sets the selected pit to zero
		copyConfig.getbState2().set(move, 0);

		while (currMancala > 0) {
			for (i = move - 1; i >= 0 && currMancala != 0; i--) {
				if (currMancala > 0) {
					copyConfig.getbState2().set(i, copyConfig.getbState2().get(i) + 1);
					currMancala--;
				}
				// capture
				if (currMancala == 0 && copyConfig.getbState2().get(i) == 1) {
					captureFlag = true;
					break;
				}
			}
			move = copyConfig.getbState2().size();
			if (currMancala > 0) {
				stonesP2 = copyConfig.getMancalaStonesInP2();
				stonesP2++;
				copyConfig.setMancalaStonesInP2(stonesP2);
				currMancala--;

				if (currMancala == 0) {
					copyConfig.setExtraMoveFlag(true);
					// for (int j = 0; j < copyConfig.getbState2().size(); j++)
					// {
					// if (copyConfig.getbState2().get(j) != 0) {
					// // System.out.println(copyConfig.getbState1().get(i));
					//
					// }
					// }
				}
			}
			if (currMancala > 0) {
				for (int j = 0; j < copyConfig.getbState1().size(); j++) {
					if (currMancala > 0) {
						copyConfig.getbState1().set(j, copyConfig.getbState1().get(j) + 1);
						currMancala--;
					}

				}
			}
		}
		// end of while

		if (captureFlag == true) {

			int capturestones = copyConfig.getbState1().get(i) + copyConfig.getbState2().get(i);
			copyConfig.setMancalaStonesInP2(stonesP2 + capturestones);

			// set p2 cell to be zero
			copyConfig.getbState1().set(i, 0);
			copyConfig.getbState2().set(i, 0);
		}

		int flagP1 = 0, flagP2 = 0, sumP2 = 0, sumP1 = 0;
		for (int j = 0; j < copyConfig.getbState1().size(); j++) {

			if (copyConfig.getbState1().get(j) != 0) {
				flagP1++;
				// flag==0 means P1 is empty
			}
			if (copyConfig.getbState2().get(j) != 0) {
				flagP2++;
			}

		}
		if (flagP2 == 0) {

			for (int j = 0; j < copyConfig.getbState1().size(); j++) {
				// keep adding each remeainng value to sum
				sumP1 = sumP1 + copyConfig.getbState1().get(j);

				// keep setting each value to zero
				copyConfig.getbState1().set(j, 0);
			}
			copyConfig.setMancalaStonesInP1(copyConfig.getMancalaStonesInP1() + sumP1);
		} else if (flagP1 == 0) {

			for (int j = 0; j < copyConfig.getbState2().size(); j++) {

				// keep adding each remeainng value to sum
				sumP2 = sumP2 + copyConfig.getbState2().get(j);
				// keep setting each value to zero
				copyConfig.getbState2().set(j, 0);
			}
			copyConfig.setMancalaStonesInP2(copyConfig.getMancalaStonesInP2() + sumP2);

		}
		return copyConfig;

	}

	public static void main(String[] args) {
		// Config inpReturn;
		// mancala m1 = new mancala();
		// for (int i = 31; i <= 31; i++) {
		// System.out.println("start" + "input_" + i + ".txt");
		// inpReturn = m1.readFile("input_" + i + ".txt");
		// m1.finalSolution(inpReturn, "next_state_" + i + ".txt",
		// "traverse_log_" + i + ".txt");
		// System.out.println("end" + "input_" + i + ".txt");
		// }

		Config inpReturn;
		mancala m1 = new mancala();
		inpReturn = m1.readFile(args[1]);
		m1.finalSolution(inpReturn, "next_state.txt", "traverse_log.txt");

	}
}
