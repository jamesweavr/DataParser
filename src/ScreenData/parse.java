package ScreenData;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;
import java.lang.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class parse {

	public static void main(String[] args) {
		JSONParser parser = new JSONParser();

		try {            	
			Object object = parser.parse(new FileReader("Resources/ScreenData.json"));
			JSONObject jsonObject = (JSONObject)object;

			JSONArray lists = (JSONArray) jsonObject.get("list");

			Iterator it = lists.iterator();

			int prevTime = 0;
			int currTime;
			List<Integer> allTimes = new ArrayList<Integer>();

			Map<String, List<Integer>> allDates = new LinkedHashMap< String, List<Integer>>();
			Map<String, List<Integer>> allHours = new LinkedHashMap< String, List<Integer>>();





			while (it.hasNext() ) {
				JSONObject slide = (JSONObject) it.next();
				String power = (String) slide.get("power");
				String date = (String) slide.get("date");
				String time = (String) slide.get("time");
				String sec = (String) slide.get("sec");

				double value = Double.parseDouble(time);
				int houri = (int) value;
				String hour = String.valueOf(houri);
				
				if (power.equals("on")) {
					prevTime = Integer.parseInt(sec);
				}
				else {
					currTime = Integer.parseInt(sec) - prevTime;
					allTimes.add(currTime);

					List<Integer> currentDate = allDates.get(date);
					if (currentDate == null) {
						currentDate = new ArrayList<Integer>();
						allDates.put(date, currentDate);
					}
					currentDate.add(currTime);
					
					List<Integer> currentTime = allHours.get(hour);
					if (currentTime == null) {
						currentTime = new ArrayList<Integer>();
						allHours.put(hour, currentTime);
					}
					currentTime.add(currTime);
					
				}

			}
			printData(allDates, allTimes, allHours);

		}


		catch(FileNotFoundException fe)
		{
			fe.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


	}

	public static void printData(Map<String, List<Integer>> allDates, List<Integer> allTimes, Map<String, List<Integer>> allHours) throws IOException {

		int avgTimes = listAvg(allTimes);
		System.out.println("Average minutes total: " + avgTimes/60.0);

		for (Map.Entry<String, List<Integer>> entry : allDates.entrySet()) {
			String key = entry.getKey();
			List<Integer> intList = entry.getValue();
			int avgDateTime = listAvg(intList);
			
			System.out.println("Average minutes on " + key + ": " + avgDateTime/60.0);
			writeDateCSV(key, avgDateTime/60.0);
		}
		
		for (Map.Entry<String, List<Integer>> entry : allHours.entrySet()) {
			String key = entry.getKey();
			List<Integer> intList = entry.getValue();
			int avgDateTime = listAvg(intList);
			
			writeTimeCSV(key, avgDateTime/60.0);
		}

	}

	public static void writeDateCSV(String date, Double time) throws IOException{
		FileWriter pw = new FileWriter("DateData.csv",true); 
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader("DateData.csv"));
		
		if (br.readLine() == null) {
			sb.append("date");
			sb.append(',');
			sb.append("screen");
			sb.append('\n');
		}
		
		br.close();

		sb.append(date);
		sb.append(',');
		sb.append(time);
		sb.append('\n');

		pw.append(sb.toString());
		pw.close();
	}
	
	public static void writeTimeCSV(String date, Double time) throws IOException{
		FileWriter pw = new FileWriter("TimeData.csv",true); 
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader("TimeData.csv"));
		
		if (br.readLine() == null) {
			sb.append("time");
			sb.append(',');
			sb.append("screen");
			sb.append('\n');
		}
		
		br.close();

		sb.append(date);
		sb.append(',');
		sb.append(time);
		sb.append('\n');

		pw.append(sb.toString());
		pw.close();
	}


	public static int listAvg(List<Integer> nums) {
		int total = 0;
		int i = 0;
		for (int element : nums) {
			i++;
			total += element;
		}

		return (total/i);
	}

}
