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

	private Integer FTAG;

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

	private Integer FTHG;
	private String HomeTeam;
	private String AwayTeam;

}
