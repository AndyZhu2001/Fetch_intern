import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.time.Instant;
import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;

public class transactions{
	
	
	public class transaction implements Comparable<transaction>{
		
		String payer;
		int points;
		Instant timeStamp;
		
		public transaction(String payer, int points, String timeStamp) {
			this.payer = payer;
			this.points = points;
			this.timeStamp = Instant.parse(timeStamp);
		}

		@Override
		public int compareTo(transactions.transaction o) {
			return this.timeStamp.compareTo(o.timeStamp);
		}
		
	}
	
	PriorityQueue<transaction> pq = new PriorityQueue<>();
	Map<String,Integer> memo = new HashMap<>();
	int pointsSpend;
	int totalPoints = 0;
	
	public transactions(int pointsSpend, String filename) {
		this.pointsSpend = pointsSpend;
	    readFile(filename);
	    deductPoints(pointsSpend);
	    showRemainPoints();
	}
	
	private void readFile(String filename) {
		try {
			CSVReader reader = new CSVReader(new FileReader(filename));
			String[] nextLine = reader.readNext();
			while((nextLine = reader.readNext()) != null) {
				String payer = nextLine[0];
				int point = Integer.parseInt(nextLine[1]);
				String timeStamp = nextLine[2];
				transaction cur = new transaction(payer, point, timeStamp);
				pq.offer(cur);
				totalPoints += point;
				memo.put(payer, memo.getOrDefault(payer, 0) + point);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void deductPoints(int pointsSpent) {
		if(pointsSpent > totalPoints) {
			System.out.println("Sorry, you do not have the enough points.\nYour total points is " + totalPoints);
			return;
		}
		while(pointsSpent > 0) {
			transaction cur = pq.poll();
			int deductPoints = Math.min(pointsSpent, cur.points);
			memo.put(cur.payer, memo.get(cur.payer) - deductPoints);
			pointsSpent -= deductPoints;
		}
	}
	
	private void showRemainPoints() {
		for(Map.Entry<String,Integer> remain : memo.entrySet()) {
			System.out.println(remain.getKey() + ": " + remain.getValue());
		}
	}
	
	public static void main(String[] args) {
		transactions trans = new transactions(5000, "transactions.csv");
	}
}