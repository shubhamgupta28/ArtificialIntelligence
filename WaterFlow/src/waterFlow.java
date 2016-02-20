import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;


class InputNode {
	private String task;
	private String source;
	private List<String> destinations = new ArrayList<String>();
	private List<String> middleNodes = new ArrayList<String>();
	private int numberOfPipes;
	private String graphs;
	private String startTime;
	private List<GraphInput> graphList = new ArrayList<GraphInput>();

	public InputNode(List<String> list1) {
		int j = 5;
		task = list1.get(0);
		source = list1.get(1);
		destinations = (Arrays.asList(list1.get(2).split(" ")));
		if (list1.get(3) != null && !list1.get(3).isEmpty())

		{
			middleNodes = (Arrays.asList(list1.get(3).split(" ")));
		} else
			middleNodes = null;
		numberOfPipes = Integer.parseInt(list1.get(4));
		startTime = list1.get(list1.size() - 1);
		for (int i = 0; i < numberOfPipes; i++) {
			graphList.add(new GraphInput(list1.get(j)));
			j++;
		}
	}

	public String getTask() {
		return task;
	}

	public String getSource() {
		return source;
	}

	public List<String> getDestinations() {
		return destinations;
	}

	public List<String> getMiddleNodes() {
		return middleNodes;
	}

	public int getNumberOfPipes() {
		return numberOfPipes;
	}

	public String getGraphs() {
		return graphs;
	}

	public String getStartTime() {
		return startTime;
	}

	@Override
	public String toString() {
		return "InputNode [task=" + task + ", source=" + source + ", destinations=" + destinations + ", middleNodes="
				+ middleNodes + ", numberOfPipes=" + numberOfPipes + ", graphs=" + graphs + ", startTime=" + startTime
				+ "]";
	}

	public List<GraphInput> getGraphList() {
		return graphList;
	}
}

class NodeRow {
	private int id;
	private String currState;
	private int parent;
	private int depth;
	private int cost;

	public NodeRow(int id, String currState, int parent, int depth, int cost) {
		this.id = id;
		this.currState = currState;
		this.parent = parent;
		this.depth = depth;
		this.cost = cost;
	}

	public int getId() {
		return id;
	}

	public String getCurrState() {
		return currState;
	}

	public int getParent() {
		return parent;
	}

	public int getDepth() {
		return depth;
	}

	public int getCost() {
		return cost;
	}

	@Override
	public String toString() {
		return "NodeRow [id=" + id + ", currState=" + currState + ", parent=" + parent + ", depth=" + depth + ", cost="
				+ cost + "]";
	}
}

class GraphInput implements Comparable<GraphInput> {

	private String start;
	private String end;
	private String cost;
	private int numOfOffPeriod;
	private List<String> periodList = new ArrayList<String>();
	private List<String> offList = new ArrayList<String>();

	public GraphInput(String edges) {
		String[] tempArray;
		int j;
		tempArray = edges.split(" ");
		start = tempArray[0];
		end = tempArray[1];
		cost = tempArray[2];
		numOfOffPeriod = Integer.parseInt(tempArray[3]);
		if (numOfOffPeriod > 0) {
			for (j = 4; j < numOfOffPeriod + 4; j++) {
				periodList.add(tempArray[j]);
			}
		}
	}

	public String getStart() {
		return start;
	}

	public String getEnd() {
		return end;
	}

	public String getCost() {
		return cost;
	}

	public int getOffPeriod() {
		return numOfOffPeriod;
	}

	public List<String> getPeriodList() {
		return periodList;
	}

	@Override
	public String toString() {
		return "GraphInput [start=" + start + ", end=" + end + ", length=" + cost + ", numOfOffPeriod=" + numOfOffPeriod
				+ ", periodList=" + periodList + "]";
	}

	public boolean isOn(int startTime) {
		int a, b;
		for (int i = 0; i < numOfOffPeriod; i++) {
			offList = (Arrays.asList(periodList.get(i).split("-")));

			a = Integer.parseInt(offList.get(0));
			b = Integer.parseInt(offList.get(1));
			a = a % 24;
			b = b % 24;
			startTime = startTime % 24;

			if (a <= startTime && startTime <= b) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int compareTo(GraphInput o) {
		int result = 0;
		if (this.start.equals(o.getStart())) {
			result = (this.end).compareTo(o.getEnd());
		}
		return result;
	}
}

public class waterFlow {
	private static final BufferedReader pw = null;
	int id1 = 1;

	public List<InputNode> readFile(String fileName) {
		String inputLine = null;
		List<String> arr = new ArrayList<String>();
		List<InputNode> inputNodeList = new ArrayList<InputNode>();
		int a = 0;

		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String first = bufferedReader.readLine();

			while ((inputLine = bufferedReader.readLine()) != null) {
				if (inputLine.trim().isEmpty() && a > 4) {
					inputNodeList.add(new InputNode(arr));
					arr.clear();
					a = 0;
				} else {
					arr.add(inputLine);
					a++;
				}
			}

			if (arr != null && !arr.isEmpty()) {
				inputNodeList.add(new InputNode(arr));
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
		return inputNodeList;

	}

	public void process(List<InputNode> nodes) {
		NodeRow node = null;

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("output.txt");
			for (int i = 0; i < nodes.size(); i++) {
				node = processEachCase(nodes.get(i));
				if (node != null) {
					writer.println(node.getCurrState() + " " + node.getCost() % 24);
				} else {
					writer.println("None");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public NodeRow processEachCase(InputNode inpNode) {
		List<GraphInput> g = new ArrayList<GraphInput>();
		HashMap<String, List<GraphInput>> fullMap = new HashMap<String, List<GraphInput>>();
		NodeRow resultNode;
		g = inpNode.getGraphList();
		fullMap = makeAdjacencyList(g);
		resultNode = applyAlgo(fullMap, inpNode);

		return resultNode;
	}

	public HashMap<String, List<GraphInput>> makeAdjacencyList(List<GraphInput> graphInpList) {
		HashMap<String, List<GraphInput>> hmap = new HashMap<String, List<GraphInput>>();
		for (int i = 0; i < graphInpList.size(); i++) {
			List<GraphInput> gList = new ArrayList<GraphInput>();
			for (int j = i; j < graphInpList.size(); j++) {

				if ((graphInpList.get(i).getStart()).equals(graphInpList.get(j).getStart())) {
					gList.add(graphInpList.get(j));
				}
			}
			if (!hmap.containsKey(graphInpList.get(i).getStart())) {
				Collections.sort(gList);
				hmap.put(graphInpList.get(i).getStart(), gList);
				gList = new ArrayList<GraphInput>();
			}
		}
		return hmap;
	}

	// PARENT MAKE NODE
	public NodeRow makeParent(String source, int startTime) {
		int depth, cost, parentID;
		String currState;

		currState = source;
		id1 = 1;
		parentID = 0;
		depth = 0;
		cost = startTime;
		NodeRow node = new NodeRow(id1, currState, parentID, depth, cost);
		return node;
	}

	// MY MAIN MAKE NODE
	public NodeRow makeNode(GraphInput gInp, NodeRow parent) {
		int cost, parentID = 0, depth;
		String currState;

		id1 = id1 + 1;
		parentID = parent.getId();
		depth = parent.getDepth() + 1;
		cost = parent.getCost() + 1;
		currState = gInp.getEnd();

		NodeRow node = new NodeRow(id1, currState, parentID, depth, cost);
		return node;
	}

	// Priority Q MakeNdoe UCS
	public NodeRow makeNode(GraphInput gInp, NodeRow parent, int id1) {
		int cost, parentID = 0, depth;
		String currState;

		id1 = id1 + 1;
		parentID = parent.getId();
		depth = parent.getDepth() + 1;
		cost = Integer.parseInt(gInp.getCost()) + parent.getCost();
		currState = gInp.getEnd();

		NodeRow node = new NodeRow(id1, currState, parentID, depth, cost);
		return node;
	}
	// ONE COMBINED BFS DFS UCS

	// FOR BFS
	public NodeRow applyAlgo(HashMap<String, List<GraphInput>> fullHashMap, InputNode inpNode) {
		String searchType;
		NodeRow node = null;
		Queue<NodeRow> nodes = new LinkedList<NodeRow>(); //
		Stack<NodeRow> stackNodes = new Stack<NodeRow>();
		int startTime = Integer.parseInt(inpNode.getStartTime());

		searchType = inpNode.getTask();
		// System.out.println("ST: "+searchType);
		switch (searchType) {

		case "BFS": {
			List<NodeRow> explored = new ArrayList<NodeRow>();
			nodes.add(makeParent(inpNode.getSource(), startTime)); // 2
			do {
				if (nodes.isEmpty()) // 3
				{
					return null;
				} else {

					node = (nodes.remove()); // 4
					if (inpNode.getDestinations().contains(node.getCurrState())) // 5
					{
						// System.out.println(node.getCurrState() + " " +
						// (node.getCost()%24));
						// String hello =
						// node.getCurrState().concat(String.valueOf(node.getCost()));
						return node;
					}
					List<GraphInput> children = fullHashMap.get(node.getCurrState());

					if (children != null && !children.isEmpty()) {
						for (GraphInput g : children) {
							if (bfsDiscoveredCheck(g, explored, nodes)) {
								nodes.add(makeNode(g, node));
							}
						}
					}
					explored.add(node);
				}
			} while (!nodes.isEmpty());
			// System.out.println("None");
			return null;
		}
		case "DFS": {
			List<NodeRow> explored = new ArrayList<NodeRow>();
			stackNodes.add(makeParent(inpNode.getSource(), startTime)); // 2

			do {
				if (stackNodes.isEmpty()) // 3
				{
					return null;
				} else {
					node = (stackNodes.pop()); // 4
					if (inpNode.getDestinations().contains(node.getCurrState())) // 5
					{
						// System.out.println(node.getCurrState() + " " +
						// (node.getCost()%24));
						return node;
					}

					List<GraphInput> children = fullHashMap.get(node.getCurrState());
					if (children != null && !children.isEmpty()) {
						for (int i = children.size() - 1; i >= 0; i--) {
							if (dfsDiscoverCheck(children.get(i), explored)) {
								stackNodes.add(makeNode(children.get(i), node));
							}
						}
					}
					explored.add(node);
				}
			} while (!stackNodes.isEmpty());
			// System.out.println("None");
			return null;
		}

		case "UCS": {
			List<NodeRow> explored = new ArrayList<NodeRow>();
			NodeRow makingNode;

			Queue<NodeRow> priorityQ = new PriorityQueue<NodeRow>(new Comparator<NodeRow>() {
				public int compare(NodeRow n1, NodeRow n2) {
					if (n1.getCost() == n2.getCost()) {
						return (n1.getCurrState().compareToIgnoreCase(n2.getCurrState()));
					}
					return n1.getCost() - n2.getCost();
				}
			});

			priorityQ.add(makeParent(inpNode.getSource(), startTime));
			do {
				if (priorityQ.isEmpty()) // 3
				{
					return null;
				} else {

					node = (priorityQ.remove()); // 4
					List<GraphInput> children = fullHashMap.get(node.getCurrState());

					if (inpNode.getDestinations().contains(node.getCurrState())) // 5
					{
						// System.out.println(node.getCurrState() + " " +
						// (node.getCost()%24));
						return node;
					} else {
						if (children != null && !children.isEmpty()) {
							for (GraphInput g : children) {
								makingNode = makeNode(g, node, id1);
								if (g.isOn(node.getCost())) {
									if (ucsDiscoveredCheck(makingNode, explored, priorityQ)) {
										priorityQ.add(makingNode);
										// System.out.println("Priority Q:"+
										// priorityQ);
										id1++;
									}
								}
							}
						}
					}
					explored.add(node);
				}
			} while (!priorityQ.isEmpty());
			// System.out.println("None");
			return null;
		}
		default:
			System.out.println("Cannot find Task");
		}
		return null;
	}

	public boolean dfsDiscoverCheck(GraphInput child, List<NodeRow> explored) {
		for (int j = 0; j < explored.size(); j++) {
			if (explored.get(j).getCurrState().equals(child.getEnd())) {
				return false;
			}
		}
		return true;
	}

	public boolean bfsDiscoveredCheck(GraphInput child, List<NodeRow> explored, Queue<NodeRow> qu) {
		int j;
		NodeRow itrNode1;
		for (j = 0; j < explored.size(); j++) {
			if (explored.get(j).getCurrState().equals(child.getEnd())) {
				return false;
			}
		}
		Iterator<NodeRow> itr1 = qu.iterator();
		while (itr1.hasNext()) {
			itrNode1 = itr1.next();
			if (itrNode1.getCurrState().equals(child.getEnd())) {
				return false;
			}
		}
		return true;
	}

	public boolean ucsDiscoveredCheck(NodeRow child, List<NodeRow> explored, Queue<NodeRow> priorityQ) {
		NodeRow itrNode;
		int j;
		for (j = 0; j < explored.size(); j++) {
			if (explored.get(j).getCurrState().equals(child.getCurrState())) {
				return false;
			}
		}

		Iterator<NodeRow> itr = priorityQ.iterator();
		while (itr.hasNext()) {
			itrNode = itr.next();
			if (itrNode.getCurrState().equals(child.getCurrState())) {
				if ((child.getCost()) < itrNode.getCost()) {
					itr.remove();
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	// ADD NULL HANDLING EVERYWHERE
	public static void main(String[] args) {
		System.out.println(new Date());
		List<InputNode> inpReturnList = new ArrayList<InputNode>();
		waterFlow p1 = new waterFlow();
		inpReturnList = p1.readFile(args[1]);
		p1.process(inpReturnList);
		System.out.println(new Date());
	}
}
