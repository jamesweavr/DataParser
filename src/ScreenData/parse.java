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

	//Main function to parse data
	//TODO
	//Add defn for each list name
	public static void main(String[] args) {
		JSONParser parser = new JSONParser();

		try {            	
			Object object = parser.parse(new FileReader("Resources/JSON/ScreenData.json"));
			JSONObject jsonObject = (JSONObject)object;

			JSONArray lists = (JSONArray) jsonObject.get("list");

			Iterator it = lists.iterator();

			int prevTime = 0;
			int currTime;
			List<Integer> allTimes = new ArrayList<Integer>();

			Map<String, List<Integer>> allDates = new LinkedHashMap< String, List<Integer>>();
			Map<String, List<Integer>> allHours = new LinkedHashMap< String, List<Integer>>();





			while (it.hasNext() ) {
				
				//Extract data from json file with these names
				JSONObject slide = (JSONObject) it.next();
				String power = (String) slide.get("power");
				String date = (String) slide.get("date");
				String time = (String) slide.get("time");
				String sec = (String) slide.get("sec");

				double value = Double.parseDouble(time);
				int houri = (int) value;
				String hour = String.valueOf(houri);
				
				//Set start on time
				if (power.equals("on")) {
					prevTime = Integer.parseInt(sec);
				}
				
				//Screen turned off
				//Clock the total time on and record the date and time screen was on
				else {
					
					//Total time = time off - time on
					currTime = Integer.parseInt(sec) - prevTime;
					allTimes.add(currTime);

					//Add time on to list of dates, according to the date screen was on
					List<Integer> currentDate = allDates.get(date);
					
					//If list is empty (first entry for the date) create a new list
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

	//Print all data to std out 
	public static void printData(Map<String, List<Integer>> allDates, List<Integer> allTimes, Map<String, List<Integer>> allHours) throws IOException {

		//Clear date csv file
		PrintWriter dateWriter = new PrintWriter("Resources/CSV/DateData.csv");
		dateWriter.print("");
		dateWriter.close();
		
		//Clear time csv file
		PrintWriter timeWriter = new PrintWriter("Resources/CSV/TimeData.csv");
		timeWriter.print("");
		timeWriter.close();
		
		
		//Print the average of all time on
		int avgTimes = listAvg(allTimes);
		System.out.println("Average minutes total: " + avgTimes/60.0);

		//Print the average for each day
		for (Map.Entry<String, List<Integer>> entry : allDates.entrySet()) {
			String key = entry.getKey();
			List<Integer> intList = entry.getValue();
			int avgDateTime = listAvg(intList);
			
			System.out.println("Average minutes on " + key + ": " + avgDateTime/60.0);
			
			//Write to csv file
			writeDateCSV(key, avgDateTime/60.0);
		}
		
		//For each data separated by hour, call write csv to print to the csv file
		for (Map.Entry<String, List<Integer>> entry : allHours.entrySet()) {
			String key = entry.getKey();
			List<Integer> intList = entry.getValue();
			int avgDateTime = listAvg(intList);
			
			writeTimeCSV(key, avgDateTime/60.0);
		}

	}

	//Write data categorized by date to a csv file
	public static void writeDateCSV(String date, Double time) throws IOException{
		FileWriter pw = new FileWriter("Resources/CSV/DateData.csv",true); 
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader("Resources/CSV/DateData.csv"));
		
		//IF file is empty
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
	
	//Write data categorized by time to a csv file
	public static void writeTimeCSV(String date, Double time) throws IOException{
		FileWriter pw = new FileWriter("Resources/CSV/TimeData.csv",true); 
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader("Resources/CSV/TimeData.csv"));
		
		//If file is empty
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

	//Find the average of a list of integers
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
