package helper;

public class StockTrade {
	private int initiated;
	private String ticker;
	private int stocks_bought;
	private int price;
	
	public StockTrade(int initiated, String ticker, int stocks_bought, int price) {
		this.initiated = initiated;
		this.ticker = ticker;
		this.stocks_bought = stocks_bought;
		this.price = price;
	}
	
	public int getInitiated() {
		return initiated;
	}
	
	public void setInitiated(int initiation) {
		this.initiated = initiation;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public int getStocksBought() {
		return stocks_bought;
	}
	
	public void setStocksBought(int bought) {
		this.stocks_bought = bought;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
}
