package edu.neu.info6206;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Driver {

	public static void main(String[] args) {
		System.out.println("system runs......\n");
		jsonToModel(); // json data to model
//		testPosionRandom();
	}

	public static void jsonToModel() {
		Gson gson = new GsonBuilder().create();
		String string = readJsonFile("season-1619.json");
		originalAllMatchesList = gson.fromJson(string, new TypeToken<List<Team>>() {
		}.getType());

		String teamA = "";
		String teamB = "";
		int sumGA = 0;
		int sumGB = 0;
		int matchCount = 0;

		List<String> allTeams = new ArrayList<String>();
		for (Team team : originalAllMatchesList) {
			if (!allTeams.contains(team.getHomeTeam())) {
				allTeams.add(team.getHomeTeam());
			}
		}

		int teamNum = allTeams.size();
		List<Match> myMatchList = new ArrayList<Match>();

		for (int i = 0; i < teamNum; i++) {
			for (int j = i + 1; j < teamNum; j++) {
				Match match = new Match();
				match.setTeamA(allTeams.get(i));
				match.setTeamB(allTeams.get(j));
				myMatchList.add(match);
			}
		}

//		reconstruct the data
		for (Team team : originalAllMatchesList) {
			teamA = team.getHomeTeam();
			teamB = team.getAwayTeam();

			for (Match match : myMatchList) {

				if (teamA.equals(match.getTeamA()) && teamB.equals(match.getTeamB())) {
					sumGA = match.getTeamAGoal() + team.getFTHG();
					match.setTeamAGoal(sumGA);
					sumGB = match.getTeamBGoal() + team.getFTAG();
					match.setTeamBGoal(sumGB);
					matchCount = match.getMatchCount() + 1;
					match.setMatchCount(matchCount);
					break;
				}
				if (teamB.equals(match.getTeamA()) && teamA.equals(match.getTeamB())) {
					sumGA = match.getTeamAGoal() + team.getFTAG();
					match.setTeamAGoal(sumGA);
					sumGB = match.getTeamBGoal() + team.getFTHG();
					match.setTeamBGoal(sumGB);
					matchCount = match.getMatchCount() + 1;
					match.setMatchCount(matchCount);
					break;
				}
			}
		}

		// get the probability of every matches
		for (Match match : myMatchList) {
			double lamda1 = match.getTeamAGoal() / (double) match.getMatchCount();
			double lamda2 = match.getTeamBGoal() / (double) match.getMatchCount();
			double[] tmp = PosionRandom(lamda1, lamda2);
			match.setTeamAWinProbability(tmp[0]);
			match.setTeamBWinProbability(tmp[1]);
			match.setTeamTieProbability(tmp[2]);
		}

		System.out.println("=====================");
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

	private static List<Team> originalAllMatchesList = new ArrayList<Team>();
}
