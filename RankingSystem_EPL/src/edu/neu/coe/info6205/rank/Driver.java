/**
 * 
 */
package edu.neu.coe.info6205.rank;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.neu.coe.info6205.model.JMatch;
import edu.neu.coe.info6205.model.Match;
import edu.neu.coe.info6205.model.Team;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * @author Yongjun Ou & Yuanxin Zhang
 * @create 04/10/2020
 */
public class Driver {
	public static void main(String[] args) {
		System.out.println("system runs......\n");
		jsonToModel(); // json data to model
//		testPosionRandom();

	}

	public static void jsonToModel() {
		Gson gson = new GsonBuilder().create();
		String string = readJsonFile("season-1920.json"); // read the json data file
		originalAllMatchesList = gson.fromJson(string, new TypeToken<List<JMatch>>() {
		}.getType()); // get the list of all matches

//		Initialize two teams' information
		String teamA = "";
		String teamB = "";

		int sumGA = 0;
		int sumGB = 0;
		int matchCount = 0;

//		Create a arraylist to store all the teams' name
		List<String> allTeamName = new ArrayList<String>();
		for (JMatch team : originalAllMatchesList) {
			if (!allTeamName.contains(team.getHomeTeam())) {
				allTeamName.add(team.getHomeTeam());
			}
		}

		int teamNum = allTeamName.size();

//		Create a arraylist to store all unique match pairs -- 190
		List<Match> myMatchList = new ArrayList<Match>();
		for (int i = 0; i < teamNum; i++) {
			for (int j = i + 1; j < teamNum; j++) {
				Match match = new Match();
				match.setTeamA(allTeamName.get(i));
				match.setTeamB(allTeamName.get(j));
				myMatchList.add(match); // all match pairs
			}
		}
		int matchPairNum = myMatchList.size();

//		Reconstruct the data
		for (JMatch teamMatch : originalAllMatchesList) {
			teamA = teamMatch.getHomeTeam();
			teamB = teamMatch.getAwayTeam();
//			System.out.println(teamA + " " + teamB);

			for (Match match : myMatchList) {

				if (teamA.equals(match.getTeamA()) && teamB.equals(match.getTeamB())) {
					sumGA = match.getTeamAGoal() + teamMatch.getFTHG();
					sumGB = match.getTeamBGoal() + teamMatch.getFTAG();

					match.setTeamAGoal(sumGA);
					match.setTeamBGoal(sumGB);

					matchCount = match.getMatchCount() + 1;
					match.setMatchCount(matchCount);
					break;
				}
				if (teamB.equals(match.getTeamA()) && teamA.equals(match.getTeamB())) {
					sumGA = match.getTeamAGoal() + teamMatch.getFTHG();
					sumGB = match.getTeamBGoal() + teamMatch.getFTAG();

					match.setTeamAGoal(sumGA);
					match.setTeamBGoal(sumGB);

					matchCount = match.getMatchCount() + 1;
					match.setMatchCount(matchCount);
					break;
				}
			}
		}

//		Get the probability of every matches
		for (Match match : myMatchList) {
			double lamda1 = match.getTeamAGoal() / (double) match.getMatchCount();
			double lamda2 = match.getTeamBGoal() / (double) match.getMatchCount();
			double[] tmp = PosionRandom(lamda1, lamda2);
			match.setTeamAWinProbability(tmp[0]); // set data into the list
			match.setTeamBWinProbability(tmp[1]);
			match.setTeamTieProbability(tmp[2]);
		}

		List<Team> allTeams = new ArrayList<Team>();
		for (int i = 0; i < teamNum; i++) {
			Team team = new Team(allTeamName.get(i)); // create a new team object to store info
			int totalMatchWin = 0;
			int totalMatchLose = 0;
			int totalMatchTie = 0;
			int matchNumCount = 0;
			int totalGoal = 0;
			int totalGoalDiff = 0;

			for (JMatch jMatch : originalAllMatchesList) {
				if (team.getTeamName().equals(jMatch.getHomeTeam())) {
					totalGoal += jMatch.getFTHG();
					totalGoalDiff += jMatch.getDiff();
					if (jMatch.getDiff() > 0)
						totalMatchWin += 1;
					if (jMatch.getDiff() < 0)
						totalMatchLose += 1;
					if (jMatch.getDiff() == 0)
						totalMatchTie += 1;

					matchNumCount += 1;
					if (matchNumCount == MATCH_PER_TEAM)
						break;
				}
				if (team.getTeamName().equals(jMatch.getAwayTeam())) {
					totalGoal += jMatch.getFTAG();
					totalGoalDiff -= jMatch.getDiff();

					if (jMatch.getDiff() < 0)
						totalMatchWin += 1;
					if (jMatch.getDiff() > 0)
						totalMatchLose += 1;
					if (jMatch.getDiff() == 0)
						totalMatchTie += 1;

					matchNumCount += 1;
					if (matchNumCount == MATCH_PER_TEAM)
						break;
				}
			}
			team.setTotalMatchWin(totalMatchWin);
			team.setTotalMatchLose(totalMatchLose);
			team.setTotalMatchTie(totalMatchTie);
			team.setTotalGoal(totalGoal);
			team.setTotalGoalDiff(totalGoalDiff);

			matchNumCount = 0; // Initialize the temporary variable
			for (Match match : myMatchList) {
				if (team.getTeamName().equals(match.getTeamA())) {
					team.getWinMap().put(match.getTeamB(), match.getTeamAWinProbability());
					matchNumCount += 1;
					if (matchNumCount == MATCH_PER_TEAM_OPPONENT)
						break;
				}

				if (team.getTeamName().equals(match.getTeamB())) {
					team.getWinMap().put(match.getTeamA(), match.getTeamBWinProbability());
					matchNumCount += 1;
					if (matchNumCount == MATCH_PER_TEAM_OPPONENT)
						break;
				}
			}

			allTeams.add(team);
		}

//        rankInTotalMatchWin(allTeams);
//        System.out.print(allTeams.toString());    

//        rankInTotalGoalDiff(allTeams);
//		sort criteria S = win * 1 + tie * 0.5 + 0 * fail
		Collections.sort(allTeams, Comparator.comparing(Team::getTranScore).reversed());
		System.out.println(allTeams.toString());

		outPutAllTeamPairsResult(allTeams);
		// System.out.print(myMatchList.toString());

//		write csv final table
		writeCSVFile(allTeams, "./FinaGames.csv",
				new String[] { "TeamName", "Win", "Tie", "Lose", "TranScore(W*1+T*0.5*L*0)" });
	}

	public static void outPutAllTeamPairsResult(List<Team> allTeams) {

		for (Team team : allTeams) {
//			System.out.println("\nThis is the table for " + team.getTeamName() + ":  ");
//			for (String key : team.getWinMap().keySet()) {
//				System.out.println(key + " " + team.getWinMap().get(key));
//			}
			StringBuilder fileNameString = new StringBuilder("./");
			fileNameString.append(team.getTeamName());
			fileNameString.append(".csv");
			writeCSVFile(team, fileNameString.toString(), new String[] { "TeamName", "Win Probability" });
		}
	}

	/**
	 * write output team pairs
	 * 
	 * @param allTeams
	 */
	public static void writeCSVFile(Team team, String FILE_NAME, String[] FILE_HEADER) {

		CSVFormat format = CSVFormat.DEFAULT.withHeader(FILE_HEADER);

		try (Writer out = new FileWriter(FILE_NAME); CSVPrinter printer = new CSVPrinter(out, format)) {
			for (String key : team.getWinMap().keySet()) {
				List<String> records = new ArrayList<>();
				records.add(key);
				records.add(String.valueOf(team.getWinMap().get(key)));
				printer.printRecord(records);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * write output
	 * 
	 * @param allTeams
	 */
	public static void writeCSVFile(List<Team> allTeams, String FILE_NAME, String[] FILE_HEADER) {

		CSVFormat format = CSVFormat.DEFAULT.withHeader(FILE_HEADER);

		try (Writer out = new FileWriter(FILE_NAME); CSVPrinter printer = new CSVPrinter(out, format)) {
			for (Team team : allTeams) {
				List<String> records = new ArrayList<>();
				records.add(team.getTeamName());
				records.add(String.valueOf(team.getTotalMatchWin()));
				records.add(String.valueOf(team.getTotalMatchTie()));
				records.add(String.valueOf(team.getTotalMatchLose()));
				records.add(String.valueOf(team.getTranScore()));
				printer.printRecord(records);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void rankInTotalMatchWin(List<Team> team) {
		sortMatchWins(team, 0, team.size() - 1);
	}

	public static void sortMatchWins(List<Team> t, int low, int high) {
		int i, j;
		if (low > high) {
			return;
		}
		i = low;
		j = high;

		Team iTeam = t.get(i); // use the first record as the pivot
		while (i < j) { // scan from two sides
			while (i < j && t.get(j).getTotalMatchWin() <= iTeam.getTotalMatchWin())
				j--;
			if (i < j)
				t.set(i++, t.get(j));// replace the lower position with larger record
			while (i < j && t.get(i).getTotalMatchWin() > iTeam.getTotalMatchWin())
				i++;
			if (i < j)
				t.set(j--, t.get(i)); // replace the higher position with smaller record
		}
		t.set(i, iTeam); // use pivot replace the i position record

		sortMatchWins(t, low, i - 1); // recurse lower part
		sortMatchWins(t, i + 1, high); // recurse higher part

	}

	public static void rankInTotalGoalDiff(List<Team> team) {
		sortGoalDiffs(team, 0, team.size() - 1);
	}

	public static void sortGoalDiffs(List<Team> t, int low, int high) {
		int i, j;
		if (low > high) {
			return;
		}
		i = low;
		j = high;

		Team iTeam = t.get(i); // use the first record as the pivot
		while (i < j) { // scan from two sides
			while (i < j && t.get(j).getTotalGoalDiff() <= iTeam.getTotalGoalDiff())
				j--;
			if (i < j)
				t.set(i++, t.get(j));// replace the lower position with larger record
			while (i < j && t.get(i).getTotalGoalDiff() > iTeam.getTotalGoalDiff())
				i++;
			if (i < j)
				t.set(j--, t.get(i)); // replace the higher position with smaller record
		}
		t.set(i, iTeam); // use pivot replace the i position record

		sortGoalDiffs(t, low, i - 1); // recurse lower part
		sortGoalDiffs(t, i + 1, high); // recurse higher part

	}

	public static void getPossibilityPairs() {

	}

	/**
	 * Monte Carlo method to simulate lamda the mean of goal
	 */
	public static double[] PosionRandom(double lamda1, double lamda2) {

		int i = 0;
		int M = 100000; // simulate times
		int w1 = 0;
		int w2 = 0;
		int w3 = 0;
		int g1 = 0;
		int g2 = 0;

		while (i < M) {
			++i;
			g1 = getPossionVariable(lamda1);
			g2 = getPossionVariable(lamda2);
			if (g1 > g2) {
//				teamA win
				w1++;
			} else if (g1 < g2) {
//				teamB win
				w2++;
			} else {
				w3++;
			}
		}

		double p1 = w1 / (double) M;
		double p2 = w2 / (double) M;
		double p3 = w3 / (double) M;
//		System.out.println("p1=" + p1 + ", p2=" + p2 + ", p3=" + p3);
		return new double[] { p1, p2, p3 };
	}

	public static String readJsonFile(String fileName) {
		String jsonStr = "";
		try {
			File jsonFile = new File(fileName);
			FileReader fileReader = new FileReader(jsonFile);

			Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
			int ch = 0;
			StringBuffer sb = new StringBuffer();
			while ((ch = reader.read()) != -1) {
				sb.append((char) ch);
			}
			fileReader.close();
			reader.close();
			jsonStr = sb.toString();
			return jsonStr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static int getPossionVariable(double lamda) {
		int x = 0;
		double y = Math.random(), cdf = getPossionProbability(x, lamda);
		while (cdf < y) {
			x++;
			cdf += getPossionProbability(x, lamda);
		}
		return x;
	}

	private static double getPossionProbability(int k, double lamda) {
		double c = Math.exp(-lamda), sum = 1;
		for (int i = 1; i <= k; i++) {
			sum *= lamda / i;
		}
		return sum * c;
	}

	private static final int MATCH_PER_TEAM_OPPONENT = 19;
	private static final int MATCH_PER_TEAM = 38;
	private static List<JMatch> originalAllMatchesList = new ArrayList<JMatch>();
}
