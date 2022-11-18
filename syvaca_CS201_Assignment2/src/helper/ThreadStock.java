package helper;

import java.util.concurrent.Semaphore;

import syvaca_CS201_Assignment2.Assignment2;

public class ThreadStock extends Thread {
	private StockTrade st;
	private Semaphore sema;
	
	public ThreadStock(StockTrade st, Semaphore sema) {
		this.st = st;
		this.sema = sema;
	}
	
	public int update(int balance, int amount) {
		int b = balance - amount;
		return b;
	}
	
	public void run() {
		try {
			sema.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			if(st.getStocksBought() > 0) { //stocks are being bought
				System.out.println(Assignment2.timeFormat(System.currentTimeMillis()-Assignment2.start_time) + 
						" Starting purchase of " + st.getStocksBought() + " stocks of " + st.getTicker()); 
				if(Assignment2.balance >= (st.getStocksBought()*st.getPrice())) { //sufficient balance
					Thread.sleep(2000);
					int temp_b = Assignment2.balance;
					Assignment2.balance -= (st.getStocksBought()*st.getPrice());					
					System.out.println(Assignment2.timeFormat(System.currentTimeMillis()-Assignment2.start_time) + 
							" Finishing purchase of " + st.getStocksBought() + " stocks of " + st.getTicker() + '\n' +
							"Current Balance after trade: " + update(temp_b, st.getStocksBought()*st.getPrice()));
				}
				else { //insufficient balance
					System.out.println("Transaction failed due to insufficient balance. Unsuccessful purchase of " +
							st.getStocksBought() + " stocks of " + st.getTicker());
					return;
				}
			}
			if(st.getStocksBought() < 0) { //stocks are being sold
				System.out.println( Assignment2.timeFormat(System.currentTimeMillis()-Assignment2.start_time) + 
						" Starting sale of " + Math.abs(st.getStocksBought()) + " stocks of " + st.getTicker());
				Thread.sleep(3000);
				int temp_b  = Assignment2.balance;
				Assignment2.balance += (Math.abs(st.getStocksBought())*st.getPrice());
				System.out.println(Assignment2.timeFormat(System.currentTimeMillis()-Assignment2.start_time) + 
						" Finishing sale of " + Math.abs(st.getStocksBought()) + " stocks of " + st.getTicker() + '\n' +
						"Current Balance after trade: " + update(temp_b, st.getStocksBought()*st.getPrice()));
			}			
		} catch (Exception e) {
			System.out.println("semaphore exception: " + e);
		} finally {
			sema.release();
		}
	}
	
}
