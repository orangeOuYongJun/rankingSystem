package edu.neu.info6206;

public class Team {

	public Integer getFTAG() {
		return FTAG;
	}

	public void setFTAG(Integer fTAG) {
		FTAG = fTAG;
	}

	public Integer getFTHG() {
		return FTHG;
	}

	public void setFTHG(Integer fTHG) {
		FTHG = fTHG;
	}

	public String getHomeTeam() {
		return HomeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		HomeTeam = homeTeam;
	}

	public String getAwayTeam() {
		return AwayTeam;
	}

	public void setAwayTeam(String awayTeam) {
		AwayTeam = awayTeam;
	}

	private Integer FTAG; // final goal away team
	private Integer FTHG; // final goal home team
	private String HomeTeam; // home team name
	private String AwayTeam; // away team name

}
