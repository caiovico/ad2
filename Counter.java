import java.util.*;
import java.util.stream.*;
import java.io.*;

class VolleyTeam implements Comparable<VolleyTeam>{
	private String name;
	private int points;
	private int victories;
	private int balance;
	public int compareTo(VolleyTeam t){
	//compares in descending order points, victories and balance(difference);
	if (this.points!=t.points) return t.points-this.points;
	if (this.victories!=t.victories) return t.victories-this.victories;
	return t.balance-this.balance;
	}
	public VolleyTeam merge(VolleyTeam t){
		if (this.name.equals(t.name)){
			this.points+=t.points;
			this.victories+=t.victories;
			this.balance+=t.balance;
			return this;
		}else{	
		throw new IllegalArgumentException("Nomes diferentes para o merge");
		}
	}
	@Override
	public String toString(){
		return "\nNome: "+name+
				"\nPontos: "+points+
				"\nVitorias: "+victories+
				"\nSaldo: "+balance+
				"\n__________________";
	}

	public String getName(){
		return name;
	}
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
}

class LeagueGame{
	VolleyTeam home, visitor;
	String homeName, visitorName;
	private static void setLeaguePoints(VolleyTeam winner, VolleyTeam loser, int w, int l){
		winner.addVictory();
		if (w-l>1)
			winner.addPoints(3);
		else{
			winner.addPoints(2);
			loser.addPoints(1);
		}
	}
	public Stream<VolleyTeam> getTeams(){
		return Stream.of(home, visitor);
	}
	public VolleyTeam getHome(){
		return home;
	}
	public VolleyTeam getVisitor(){
		return visitor;
	}
	public LeagueGame(String s){
		//get names
		String[] tokens = s.split("vs");
		homeName = tokens[0].trim();
		tokens = tokens[1].split("/");
		visitorName = tokens[0].trim();	
		//create teams
		home = new VolleyTeam(homeName);
		visitor = new VolleyTeam(visitorName);	
		//get number of played sets and league points
		String[] sets = tokens[1].split("-");
		int homeSets, visitorSets;
		homeSets = Integer.parseInt(sets[0]);
		visitorSets = Integer.parseInt(sets[1]);
		if (homeSets>visitorSets)
			setLeaguePoints(home, visitor, homeSets, visitorSets);
		else
			setLeaguePoints(visitor, home, visitorSets, homeSets);
		//set the balance (difference) of points for each team
		int difference = 0;
		String[] scoreB = null;
		for(int i=2;i<tokens.length;i++){
			scoreB = tokens[i].split("-");
			difference += Integer.parseInt(scoreB[0].trim())-Integer.parseInt(scoreB[1].trim());
		}
		home.changeBalance(difference);
		visitor.changeBalance(-difference);
	}
}

public class Counter{
	private static List<VolleyTeam> table = new ArrayList<>();
	public static List<String> readFile(File f) throws IOException{
		//reads the file and returns a List with the lines
		List<String> lines = new ArrayList<>();
		try(BufferedReader reader = new BufferedReader(new FileReader(f))){
			String s;
			while((s=reader.readLine()) != null){
				lines.add(s);
			}
		}
		return lines;
	}
	public static void writeFile(File f, int n) throws IOException{
		//reads n teams which go to next phase
		Collections.sort(table);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(f))){
			for(int i=0;i<n;i++){
				writer.write(table.get(i).getName());
				writer.newLine();
			}
		}
	}
	private static void processGames(List<String> lines){ 
		List<VolleyTeam> raw = new ArrayList<>();
		Set<String> names = new HashSet<>();
		lines.stream()
			 .map(str->new LeagueGame(str))
			 .forEach(lg->lg.getTeams()
			 				.peek(t->names.add(t.getName()))
			 				.forEach(t->raw.add(t))
			 		  );
		names.forEach(x->table.add(raw
									.stream()
									.filter(vt->vt.getName().equals(x))
									.reduce((a,b)->a.merge(b))
									.get()
								  )
					 );
	}
	public static void main(String[] args) throws IOException{
		List<String> games=null;
		//input file is converted to a List
		try{
			games = readFile(new File(args[0]));
		}catch (IOException e){
			System.out.println("Nao foi possivel ler o arquivo especificado.");
		}
		//process the lines read using LeagueGame objects
		processGames(games);
		try{
			writeFile(new File("out-"+args[0]), Integer.parseInt(args[1]));
		}catch(IOException e){
			System.out.println("Nao foi possivel criar o arquivo de saida.");
		}
	}
}