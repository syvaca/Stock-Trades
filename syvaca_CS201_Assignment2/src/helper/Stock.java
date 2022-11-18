package helper;


import java.util.ArrayList;
import java.time.LocalDate;

public class Stock {
	private String name;
	private String ticker;
	private String startDate;
	private Integer stockBrokers;
	private String description;
	private String exchangeCode;
	
	public Stock(String name, String ticker, String startDate, Integer stockBrokers,
			String description, String exchangeCode) {
		this.name = name;
		this.ticker = ticker;
		this.startDate = startDate;
		this.stockBrokers = stockBrokers;
		this.description = description;
		this.exchangeCode = exchangeCode;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public Integer getStockBrokers() {
		return stockBrokers;
	}
			
	public void setStockBrokers(int stockBrokers) {
		this.stockBrokers = stockBrokers;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getExchangeCode() {
		return exchangeCode;
	}
	
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}
	
}
