class VolleyTeam implements Comparable<Team>{
	private String name;
	private int points;
	private int victories;
	private int balance;

	public VolleyTeam(String s){
		this.name=s;
	}

	public addPoints(int i){
		this.points+=i;
	}

	public addVictory(){
		victories++
	}

	public changeBalance(int i){
		balance+=i;
	}

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
		return t.victories-this.victories;
	}

	public Team merge(Team t){
		this.victories+=t.victories;
		this.addPoints(t.points);
		this.changeBalance(t.balance);
		return this;
	}
}


class LeagueGame{
	VolleyTeam home, visitor;
	public LeagueGame(String s){
		Scanner sc = new Scanner(s);
		String homeName = "";
		while (sc.next!="vs"){
			
		}
	}
}

public class Counter{

	static Set<VolleyTeam> teams = new TreeSet<>();
	public static void getGame(String s){

	}
}