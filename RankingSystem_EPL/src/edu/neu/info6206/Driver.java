package edu.neu.info6206;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("system runs....");
		jsonToModel();
	}

	public static void jsonToModel() {
		Gson gson = new GsonBuilder().create();
		String string = readJsonFile("season-1819.json");
		teams = gson.fromJson(string, new TypeToken<List<Team>>() {
		}.getType());
		for (Team team : teams) {
			System.out.println(team.getAwayTeam());
			System.out.println(team.getHomeTeam());
			System.out.println(team.getFTHG());
			System.out.println(team.getFTAG());
			System.out.println("---------------------");
		}
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

	private static List<Team> teams = new ArrayList<Team>();
}
