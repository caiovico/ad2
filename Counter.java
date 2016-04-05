class VolleyTeam implements Comparable<Team>{
	private String name;
	private int points;
	private int victories;
	private int balance;

	public addPoints(int i){
		this.points+=i;
	}

	public addVictory(){
		victories++
	}

	public setBalance(){}

	public String toString(){
		return this.name;
	}

	public boolean equals(Object o){
		if (o instanceof Team){
			Team t = (Team)o;
			return this.name.equals(t.name);
		}else return false;
	}

	public int compareTo(Team t){
		//compares in descending order, the first has more points, more victories and/or more balance
		if (this.points!=t.points) return t.point-this.points;
		if (this.victories!=t.victories) return t.victories-this.points;
		if(this.balance!=t.balance) return t.victories-this.victories;
		return 0;
	}

	public Team merge(Team t){
		this.addVictory(t.victories);
		this.addPoints(t.points);
		this.changeBalance(t.balance);
	}
}


class LeagueGame{

}

public class Counter{

	static Set<VolleyTeam> teams = new TreeSet<>();
	public static void getGame(String s){

	}
}