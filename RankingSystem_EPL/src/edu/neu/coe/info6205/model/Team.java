/**
 * 
 */
package edu.neu.coe.info6205.model;

import java.util.HashMap;

/**
 * @author Yuanxin
 *
 */
public class Team {
	public Team(String name) {
		this.teamName = name;
	}

	public String getTeamName() {
		return teamName;
	}

	public int getTotalMatchWin() {
		return totalMatchWin;
	}

	public void setTotalMatchWin(int totalMatchWin) {
		this.totalMatchWin = totalMatchWin;
	}

	public int getTotalMatchLose() {
		return totalMatchLose;
	}

	public void setTotalMatchLose(int totalMatchLose) {
		this.totalMatchLose = totalMatchLose;
	}

	public int getTotalMatchTie() {
		return totalMatchTie;
	}

	public void setTotalMatchTie(int totalMatchTie) {
		this.totalMatchTie = totalMatchTie;
	}

	public int getTotalGoal() {
		return totalGoal;
	}

	public void setTotalGoal(int totalGoal) {
		this.totalGoal = totalGoal;
	}

	public int getTotalGoalDiff() {
		return totalGoalDiff;
	}

	public void setTotalGoalDiff(int totalGoalDiff) {
		this.totalGoalDiff = totalGoalDiff;
	}

	public double getTranScore() {
		return (totalMatchWin * 1 + totalMatchTie * 0.5 + totalMatchLose * 0);
	}

	public void setTranScore(double tranScore) {
		this.tranScore = tranScore;
	}

	@Override
	public String toString() {
		return "[" + teamName + ", " + totalMatchWin + ", " + totalMatchLose + ", " + totalMatchTie + ", " + totalGoal
				+ ", " + totalGoalDiff + "] " + "\n";
	}

	public HashMap<String, Double> getWinMap() {
		return winMap;
	}

	private HashMap<String, Double> winMap = new HashMap<>();
	private int totalMatchWin;
	private int totalMatchLose;
	private int totalMatchTie;
	private int totalGoal;
	private int totalGoalDiff;
	private String teamName;
	private double tranScore;
}
