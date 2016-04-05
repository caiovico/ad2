class Team implements Comparable<Team>{
	private String name;
	private int points;
	private int victories;
	private int balance;

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

	}
}

public class Counter{

	static Set<Team> teams = new TreeSet<>();
	public static void getGame(String s){

	}
}