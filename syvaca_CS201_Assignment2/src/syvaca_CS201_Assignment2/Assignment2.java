package syvaca_CS201_Assignment2;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import helper.Stock;
import helper.StockTrade;
import helper.ThreadStock;
import helper.Companies;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Assignment2 {
	public static ArrayList<Stock> allStocks;
	private static ArrayList<StockTrade> allTrades = new ArrayList<StockTrade>();
	public static int balance;
	public static long start_time;
	
	public static String timeFormat(long milli) {
		DateFormat simple_date = new SimpleDateFormat("H:mm:ss:SSS");
		Date resultDate = new Date(milli - (16*60*60*1000));
		String resultString = "[" + simple_date.format(resultDate) + "]";
		return resultString;
	}
	
	public static boolean isValidDate(String dateStr) {
		//valid format
		try {
			String year = dateStr.substring(0,4);
			int i = Integer.parseInt(year);
			if(i>9999 || i < 1000) {
				return false;
			}
		} catch(Exception e) {
			return false;
		}
		
		try {
			String month = dateStr.substring(5, 7);
			int i = Integer.parseInt(month);
			if(i>12 || i<1) {
				return false;
			}
		} catch(Exception e) {
			return false;
		}
		
		try {
			String day = dateStr.substring(8);
			int i = Integer.parseInt(day);
			if(i>31 || i<1) {
				return false;
			}
		} catch(Exception e) {
			return false;
		}
		
		try {
			String ap1 = dateStr.substring(4,5);
			String ap2 = dateStr.substring(7,8);
			if(!ap1.equals("-") || !ap2.equals("-")) {
				return false;
			}
		} catch(Exception e) {
			return false;
		}
		
		
		SimpleDateFormat sdfrmt = new SimpleDateFormat("YYYY-mm-dd");
	    sdfrmt.setLenient(false);
        try {
        	sdfrmt.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
	
    }
	

	public static void main(String[] args) {
		//READ IN JSON FILE
		boolean validJason = false;
		String info = "";
		String company_file = "";
		Scanner in = new Scanner(System.in);
		Gson gson = null;
		Companies company = null;
		while(!validJason) {
			System.out.println("What is the name of the file containing the company information?");
			company_file = in.nextLine();
			File file = new File(company_file);
			Scanner scan;
			try {
				info = "";
				scan = new Scanner(file);
				while(scan.hasNext()) {
					info += scan.nextLine();
					validJason = true;
				}
			} catch(FileNotFoundException e) {
				System.out.println("\nThe file " + company_file + " could not be found\n");
				validJason = false;
				continue;
			} 
		
			try {
				gson = new Gson();
			}catch (Exception e) {
				System.out.println("Exception error: " + e);
			}
			
			try {
				company = gson.fromJson(info, Companies.class);
			} catch(JsonSyntaxException e) {
				System.out.println("\nThe file " + company_file + " is not formatted properly (Json Syntax Error)\n");
				validJason = false;
				continue;
			}	
			
			allStocks = company.getData();
			
			
			// JSON IS NOT FORMATTED PROPERLY
			if(!(allStocks == null)) {
				for(Stock tempStock : allStocks) {
					if(tempStock.getName() == null || tempStock.getName().equals("")) { // if empty name
						validJason = false;
						System.out.println("\nThe file " + company_file + " is missing parameters (name)\n");
						break;
					}
					if(tempStock.getTicker() == null || tempStock.getTicker().equals("")) { // if empty name
						validJason = false;
						System.out.println("\nThe file " + company_file + " is missing parameters (ticker)\n");
						break;
					}
					if(tempStock.getStartDate() == null || !isValidDate(tempStock.getStartDate())) { // if any dates are not valid
						validJason = false;
						System.out.println("\nThe file " + company_file + " is missing parameters (invalid/missing date)\n");
						break;
					}
					if(tempStock.getStockBrokers() == null || tempStock.getStockBrokers().equals("")) {
						validJason = false;
						System.out.println("\nThe file " + company_file + " is missing parameters (invalid/missing stockBrokers\n");
						break;
					}
					if(tempStock.getExchangeCode() == null || !(tempStock.getExchangeCode().equals("NYSE") || tempStock.getExchangeCode().equals("NASDAQ"))) {
						validJason = false;
						System.out.println("\nThe file " + company_file + " is missing parameters (invalid exchange)\n");
						break;
					}
				}
			}
			else {
				validJason = false;
				System.out.println("\nThe file " + company_file + " is not formatted properly");
			}
		}
		
		
		//READ IN CSV FILE 
		// used code found in https://www.javatpoint.com/how-to-read-csv-file-in-java
		boolean validCsv = false;
		Scanner scan_csv = new Scanner(System.in);
		String csvFile = "";
		String csv_info = "";
		while (!validCsv) {
			System.out.println("What is the name of the file containing the schedule information?");
			csvFile = scan_csv.nextLine();
			try {
				Scanner finalScanner = new Scanner(new File(csvFile));
				finalScanner.useDelimiter(",");  
				String[] values; 
				while (finalScanner.hasNext())  {  
					csv_info = finalScanner.nextLine();
					validCsv = true;
					values = csv_info.split(",");
					StockTrade newTrade = new StockTrade(Integer.parseInt(values[0]), values[1], Integer.parseInt(values[2]), Integer.parseInt(values[3]));
					allTrades.add(newTrade);
				}
			} catch(FileNotFoundException e) {
				System.out.println("\nThe file " + csvFile + " could not be found\n");
				validCsv = false;
				continue;
			}
		}
		

		// Get the initial balance
		boolean validNum = false;
		balance = 0;
		while(!validNum) {
			Scanner num = new Scanner(System.in);
			System.out.println("\nWhat is the Initial Balance?");
			if(num.hasNextInt()) {
				balance = num.nextInt();
				if(balance < 0) {
					System.out.println("\nThat is not a valid option.\n");
				}
				else validNum = true;
			}
			else {
				System.out.println("\n" + "That is not a valid option.\n");
			}	
		}
		
		System.out.println("Inital Balance: " + balance);
		
		ExecutorService executor = Executors.newFixedThreadPool(allTrades.size());
		HashMap<String, Semaphore> map = new HashMap<>();
		for(Stock ind_stock : allStocks) {
			Semaphore semaphore = new Semaphore(ind_stock.getStockBrokers());
			map.put(ind_stock.getTicker(), semaphore);
		}
		
		start_time = System.currentTimeMillis();
		for(StockTrade stockTrade : allTrades) {
			while((System.currentTimeMillis()-start_time)/1000 < stockTrade.getInitiated()) {};
			ThreadStock ts = new ThreadStock(stockTrade, map.get(stockTrade.getTicker()));
			executor.execute(ts);
		}
		
		executor.shutdown();
		while(!executor.isTerminated()) {
			Thread.yield();
		}
		
		System.out.println("All trades completed!");
		
	} // closes main

}
