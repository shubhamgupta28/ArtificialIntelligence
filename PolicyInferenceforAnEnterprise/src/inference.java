import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Sentence {

	private List<AtomicSentence> antecedent = new ArrayList<AtomicSentence>();
	private List<AtomicSentence> consequent = new ArrayList<AtomicSentence>();

	public Sentence(String string1) {
		String tempVar1;
		List<String> tempList1 = new ArrayList<String>();
		if (string1.contains("=>")) {
			tempVar1 = string1.split("=>")[0].trim();
			if (tempVar1.contains("^")) {
				tempList1 = Arrays.asList(tempVar1.split("\\^"));
				// try to trim the above line

				for (String str1 : tempList1) {
					antecedent.add(new AtomicSentence(str1.trim())); // send
				}
			}
			// If we dont have ^
			else {
				antecedent.add(new AtomicSentence(tempVar1)); // send
			}
			String temp = string1.split("=>")[1].trim();
			consequent.add(new AtomicSentence(temp)); // send
		}
		// if variables DONT have =>
		else {
			antecedent = null;
			consequent.add(new AtomicSentence(string1));
		}
	}

	public List<AtomicSentence> getAntecedent() {
		return antecedent;
	}

	public List<AtomicSentence> getConsequent() {
		return consequent;
	}

	@Override
	public String toString() {
		return "SentenceShabbo [antecedent=" + antecedent + ", consequent=" + consequent + "]";
	}

}

class AtomicSentence {
	private String nameOfPredicate;
	private List<Argument> arguments = new ArrayList<Argument>();
	String strCopyConstruc;

	public AtomicSentence(String str) {
		List<String> atomicList = new ArrayList<String>();
		List<String> tempList = new ArrayList<String>();

		strCopyConstruc = str;
		atomicList.add(str);
		tempList = Arrays.asList((atomicList.get(0).split("[\\(||\\)]")[1]));

		for (String argument : tempList.get(0).split(",")) {
			arguments.add(new Argument(argument));
		}
		// to consider left part of (
		nameOfPredicate = (atomicList.get(0).split("[\\(||\\)]")[0]);
	}

	public AtomicSentence(AtomicSentence clause) {

	}

	public List<Argument> getArguments() {
		return arguments;
	}

	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}

	public String getPredicate() {
		return nameOfPredicate;
	}

	@Override
	public String toString() {
		return "AtomicSentence [predicate=" + nameOfPredicate + ", arguments=" + arguments + "]";
	}

}

class Argument {
	private String argString = "";

	public Argument(String arg) {
		argString = arg;
	}

	public boolean isConstant() {
		return Character.isUpperCase(argString.charAt(0));
		// if upper/CONSTANT, then return TRUE
	}

	public String getArgString() {
		return argString;
	}

	public void setArgString(String argString) {
		this.argString = argString;
	}

	@Override
	public String toString() {
		return "Argument [argString=" + argString + "]";
	}

}

public class inference {
	List<AtomicSentence> queryList = new ArrayList<AtomicSentence>();
	List<Sentence> kbList = new ArrayList<Sentence>();
	String noOfKbRules = "";

	public void readFile(String fileName) {
		String inputLine = null;
		List<String> arr = new ArrayList<String>();

		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String noOfQueries = bufferedReader.readLine();
			int count = 0;

			while ((inputLine = bufferedReader.readLine()) != null) {
				if (inputLine.trim().isEmpty()) {
					arr.clear();
				} else {
					arr.add(inputLine.trim());
					count++;
					if (count == Integer.parseInt(noOfQueries) + 1) {
						noOfKbRules = inputLine.trim();
					}
				}
			}
			for (int i = 0; i < Integer.parseInt(noOfQueries); i++) {
				queryList.add(new AtomicSentence(arr.get(i)));
			}
			for (int j = Integer.parseInt(noOfQueries) + 1; j < Integer.parseInt(noOfQueries) + 1
					+ Integer.parseInt(noOfKbRules); j++) {
				kbList.add(new Sentence(arr.get(j)));
			}
			bufferedReader.close();

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}

	private void process() {

		PrintWriter writer = null;
		List<AtomicSentence> result = new ArrayList<AtomicSentence>();
		List<String> visited = new ArrayList<String>();

		try {
			writer = new PrintWriter("output.txt");

			for (AtomicSentence b : queryList) {
				result = backwardOR(b, visited);

				if (result.isEmpty()) {
					writer.println("FALSE");
				} else {
					writer.println("TRUE");
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

	private List<AtomicSentence> backwardOR(AtomicSentence goal, List<String> visited) {
		// Is clause atomicSentece or Snetence type?
		List<AtomicSentence> clause = new ArrayList<AtomicSentence>();

		List<Sentence> matchedRules = new ArrayList<Sentence>();
		Map<String, String> subs = new HashMap<String, String>();
		List<Map<String, String>> res_subs = new ArrayList<Map<String, String>>();

		matchedRules = findMatchingRule(goal, visited);
		//System.out.println("OR:" + goal);
		for (Sentence rule : matchedRules) {
			subs = findSub(rule.getConsequent().get(0), goal); 
			if (rule.getAntecedent() == null) {
				if (subs != null) {
					clause.add(subsFunc(rule.getConsequent().get(0), subs));
				}
			}

			// now there is LHS
			else {
				res_subs = backwardAnd(rule.getAntecedent(), subs, visited);

				if (res_subs != null && !res_subs.isEmpty()) {
					for (Map<String, String> s : res_subs) {
						clause.add(subsFunc(rule.getConsequent().get(0), s));
					}
				}
			}
		}

		//System.out.println("ExitOR:" + goal);
		return clause;

	}

	private List<Map<String, String>> backwardAnd(List<AtomicSentence> goals, Map<String, String> sub,
			List<String> visited) {
		List<Map<String, String>> subs = new ArrayList<Map<String, String>>();
		AtomicSentence first;
		List<AtomicSentence> rest;
		AtomicSentence modifiedFirst;
		List<AtomicSentence> rClauses = new ArrayList<AtomicSentence>();
		Map<String, String> tempSub = new HashMap<String, String>();
		List<Map<String, String>> tr_SUBS = new ArrayList<Map<String, String>>();

		//System.out.println("BA goals: " + goals + " BA sub: " + sub);
		if (goals == null || goals.isEmpty()) {
			// check If Get(0) should be there or not
			subs.add(sub);
		} else {
			// first only has one element
			first = goals.get(0);
			//System.out.println("first" + first + ":" + goals);
			rest = goals.subList(1, goals.size());

			modifiedFirst = subsFunc(first, sub);

			rClauses = backwardOR(modifiedFirst, new ArrayList<String>(visited));

			for (AtomicSentence clause : rClauses) {
				tempSub = findSub(modifiedFirst, clause);

				if (tempSub != null) {
					// tempSub.update(subs);
					tempSub.putAll(sub);
					tr_SUBS = backwardAnd(rest, tempSub, new ArrayList<String>(visited));

					if (tr_SUBS != null && !tr_SUBS.isEmpty()) {
						for (Map<String, String> tsub : tr_SUBS) {
							Map<String, String> tsub2 = new HashMap<String, String>(tsub);
							tsub2.putAll(tempSub);

							subs.add(tsub2);
						}
					}
				}
			}
		}
		//System.out.println("ExitBA goals: " + goals + " BA sub: " + sub);
		return subs;
	}

	private AtomicSentence subsFunc(AtomicSentence clause, Map<String, String> sub) {
		//System.out.println("subsFunc: Clause: " + clause);
		AtomicSentence subsiClause = new AtomicSentence(clause.strCopyConstruc);

		for (int i = 0; i < subsiClause.getArguments().size(); i++) {

			// clauseArgs->var && Hashmap sub has clauseArgss
			if (!subsiClause.getArguments().get(i).isConstant()
					&& sub.containsKey(subsiClause.getArguments().get(i).getArgString())) {

				subsiClause.getArguments().get(i)
						.setArgString(sub.get(subsiClause.getArguments().get(i).getArgString()));
			}
		}
		return subsiClause;
	}

	private Map<String, String> findSub(AtomicSentence rule, AtomicSentence goal) {
		//System.out.println("Goal: " + goal);
		//System.out.println("Rule: " + rule);

		Map<String, String> temp = new HashMap<String, String>();
		Map<String, String> temp2 = new HashMap<String, String>();

		Map<String, String> subs = new HashMap<String, String>();

		if (rule.getArguments().size() == goal.getArguments().size()) {
			for (int i = 0; i < goal.getArguments().size(); i++) {
				// Check if GoalArgs is varibale, nd Rule Args is constant
				if (!goal.getArguments().get(i).isConstant() && rule.getArguments().get(i).isConstant()) {
					if (temp.containsKey(goal.getArguments().get(i).getArgString())
							&& !temp.get(goal.getArguments().get(i).getArgString())
									.equals(rule.getArguments().get(i).getArgString())) {
						return null;
					} else {
						temp.put(goal.getArguments().get(i).getArgString(), rule.getArguments().get(i).getArgString());
					}
				}
			}

			for (int i = 0; i < goal.getArguments().size(); i++) {
				// rule->var && goal->const
				if (!rule.getArguments().get(i).isConstant() && goal.getArguments().get(i).isConstant()) {

					// if ruleargs is in subs && val r nt smae
					if (subs.containsKey(rule.getArguments().get(i).getArgString())
							&& !subs.get(rule.getArguments().get(i).getArgString())
									.equals(goal.getArguments().get(i).getArgString())) {
						return null;
					} else {
						subs.put(rule.getArguments().get(i).getArgString(), goal.getArguments().get(i).getArgString());
					}
				}
				// rule->var && goal->var
				else if (!rule.getArguments().get(i).isConstant() && !goal.getArguments().get(i).isConstant()) {
					if (temp.containsKey(goal.getArguments().get(i).getArgString())) {

						// if rule args i is in sub &&if both nt same
						if (subs.containsKey(rule.getArguments().get(i).getArgString())
								&& !subs.get(rule.getArguments().get(i).getArgString())
										.equals(goal.getArguments().get(i).getArgString())) {
							return null;
						} else {
							subs.put(rule.getArguments().get(i).getArgString(),
									temp.get(goal.getArguments().get(i).getArgString()));
						}
					} else if (temp2.containsKey(goal.getArguments().get(i).getArgString())) {
						subs.put(rule.getArguments().get(i).getArgString(),
								temp2.get(goal.getArguments().get(i).getArgString()));
					} else {
						temp2.put(goal.getArguments().get(i).getArgString(), rule.getArguments().get(i).getArgString());
					}
				}

			}
		} else {
			return null;
		}
		return subs;
	}

	private List<Sentence> findMatchingRule(AtomicSentence goal, List<String> visited) {
		List<Sentence> matchSetKB = new ArrayList<Sentence>();
		boolean flag = false;

		for (Sentence clause : kbList) {

			// // if me, no of arg, pname shld ALL be same
			if (clause.getConsequent().get(0).getPredicate().equals(goal.getPredicate())
					&& clause.getConsequent().get(0).getArguments().size() == goal.getArguments().size()) {
				flag = true;

				int sizeOfClause = clause.getConsequent().get(0).getArguments().size();

				for (int i = 0; i < sizeOfClause; i++) {
					if (goal.getArguments().get(i).isConstant()
							&& clause.getConsequent().get(0).getArguments().get(i).isConstant()
							&& !goal.getArguments().get(i).getArgString()
									.equals(clause.getConsequent().get(0).getArguments().get(i).getArgString())) {
						flag = false;
					}
				}
				if (flag == true && !visited.contains(clause.toString() + goal.toString())) {
					// add visited rules
					matchSetKB.add(clause);
					visited.add(clause.toString() + goal.toString());
				}
			}
		}

		return matchSetKB;
	}

	public static void main(String[] args) {
		inference inf1 = new inference();
		inf1.readFile(args[1]);
		inf1.process();
	}
}
