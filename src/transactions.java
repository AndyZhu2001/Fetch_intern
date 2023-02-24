import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.time.Instant;
import java.io.FileReader;
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
		try {
			Scanner scnr = new Scanner(System.in);
			System.out.println("Please enter the filename");
			String filename = scnr.next();
			System.out.println("Please enter the number of the points used");
			int points = scnr.nextInt();
			transactions trans = new transactions(points, filename);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}