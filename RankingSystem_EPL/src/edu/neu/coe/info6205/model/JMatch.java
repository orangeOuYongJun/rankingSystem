/**
 * 
 */
package edu.neu.coe.info6205.model;


/**
 * @author Yongjun Ou & Yuanxin Zhang
 */
public class JMatch {
    public Integer getFTAG() {
        return FTAG;
    }

    public void setFTAG(Integer FTAG) {
        this.FTAG = FTAG;
    }

    public Integer getFTHG() {
        return FTHG;
    }

    public void setFTHG(Integer FTHG) {
        this.FTHG = FTHG;
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
    
    public int getDiff() {
    	return FTHG - FTAG;
    }

    private Integer FTAG; // Final Goal Away Team
    private Integer FTHG; // Final Goal Home Team
    private String HomeTeam; // Home Team Name
    private String AwayTeam; // Away Team Name
}
