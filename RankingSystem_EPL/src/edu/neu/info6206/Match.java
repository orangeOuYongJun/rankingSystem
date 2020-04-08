package edu.neu.info6206;

public class Match {

	public int getTeamAGoal() {
		return teamAGoal;
	}

	public void setTeamAGoal(int teamAGoal) {
		this.teamAGoal = teamAGoal;
	}

	public int getTeamBGoal() {
		return teamBGoal;
	}

	public void setTeamBGoal(int teamBGoal) {
		this.teamBGoal = teamBGoal;
	}

	public String getTeamA() {
		return teamA;
	}

	public void setTeamA(String teamA) {
		this.teamA = teamA;
	}

	public String getTeamB() {
		return teamB;
	}

	public void setTeamB(String teamB) {
		this.teamB = teamB;
	}

	public int getMatchCount() {
		return matchCount;
	}

	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}

	public double getTeamAWinProbability() {
		return teamAWinProbability;
	}

	public void setTeamAWinProbability(double teamAWinProbability) {
		this.teamAWinProbability = teamAWinProbability;
	}

	public double getTeamBWinProbability() {
		return teamBWinProbability;
	}

	public double getTeamTieProbability() {
		return teamTieProbability;
	}

	public void setTeamTieProbability(double teamTieProbability) {
		this.teamTieProbability = teamTieProbability;
	}

	public void setTeamBWinProbability(double teamBWinProbability) {
		this.teamBWinProbability = teamBWinProbability;
	}

	private double teamTieProbability;
	private double teamAWinProbability;
	private double teamBWinProbability;
	private int matchCount;
	private int teamAGoal;
	private int teamBGoal;
	private String teamA;
	private String teamB;
}
