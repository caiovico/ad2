import java.util.*;
class VolleyTeam implements Comparable<VolleyTeam>{
	private String name;
	private int points;
	private int victories;
	private int balance;

	public VolleyTeam(String s){
		this.name=s;
	}

	public void addPoints(int i){
		this.points+=i;
	}

	public void addVictory(){
		victories++;
	}

	public void changeBalance(int i){
		balance+=i;
	}

	public String toString(){
		return this.name;
	}

	public boolean equals(Object o){
		if (o instanceof VolleyTeam){
			VolleyTeam t = (VolleyTeam)o;
			return this.name.equals(t.name);
		}else return false;
	}

	public int compareTo(VolleyTeam t){
		//compares in descending order, the first has more points, more victories and/or more balance
		if (this.points!=t.points) return t.points-this.points;
		if (this.victories!=t.victories) return t.victories-this.points;
		return t.victories-this.victories;
	}

	public VolleyTeam merge(VolleyTeam t){
		this.victories+=t.victories;
		this.addPoints(t.points);
		this.changeBalance(t.balance);
		return this;
	}
}


class LeagueGame{
	VolleyTeam home, visitor;
	String homeName, visitorName;
	public LeagueGame(String s){
		String[] tokens = s.split("vs");
		homeName = tokens[0].trim();
		tokens = tokens[1].split("/");
		visitorName = tokens[0].trim();

		for (int i = 1; i<tokens.length;i++){
			System.out.println(tokens[i]);
		}


		System.out.println(homeName);
		System.out.println(visitorName);

	}
}

public class Counter{

	public static void main(String[] args){
		LeagueGame lg1 = new LeagueGame("DENTIL-PRAIA CLUBE vs CAMPONESA-MINAS/3-2/22-25/25-15/21-25/25-22/15­-12");
		//LeagueGame lg2 = new LeagueGame("DENTIL-PRAIA CLUBE vs CAMPONESA-MINAS/3­-0/25­-21/25­-18/25­-22");

		//System.out.println(lg1.homeName.equals(lg2.homeName));



	}
}