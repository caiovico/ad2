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

	public String getStatus(){
		return name+"(p: "+points+", v: "+victories+" b: "+balance+")";
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
			difference+=
			Stream.of(tokens[i].split("-"))
				.map(String::trim)
				.mapToInt(Integer::parseInt)
				.reduce((a,b)->a-b)
				.getAsInt();
			//scoreB = tokens[i].split("-");
			//difference += Integer.parseInt(scoreB[0].trim())-Integer.parseInt(scoreB[1].trim());
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
			reader.lines().forEach(line->lines.add(line));
		}
		return lines;
	}
	public static void writeFile(File f, int n) throws IOException{
		//reads n teams which go to next phase
		if ((n>table.size())||(n<=0)) throw new IllegalArgumentException();
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(f))){
			for(int i=0;i<n;i++){
				writer.write(table.get(i).getName());
				writer.newLine();
			}
		}
	}
	private static void processGames(List<String> lines){ 
		List<VolleyTeam> raw = new ArrayList<>();
		lines.stream()
			 .map(str->new LeagueGame(str))
			 .forEach(lg->lg.getTeams().forEach(t->raw.add(t)));
		//Get all the VolleyTeams and collect it to a Map which stores as
		//vt.getName()->vt using the merge method to reduce to an unique vt
		table=	 
			raw.stream()
				.collect(
					Collectors.toMap(
						VolleyTeam::getName,
						vt->vt,
						(a,b)->a.merge(b)))//Map
							.values()//to Collection
							.stream()
							.sorted()//uses natural order
							.collect(Collectors.toList());
		}
	public static void main(String[] args){
		List<String> games=null;
		//input File is converted to a List
		try{
			games = readFile(new File(args[0]));
		}catch (IOException e){
			System.out.println("Nao foi possivel ler o arquivo especificado.");
			System.exit(0);
		}
		//process the read lines using LeagueGame objects
		processGames(games);
		try{
			writeFile(new File("out-"+args[0]), Integer.parseInt(args[1]));
		}catch(IOException e){
			System.out.println("Nao foi possivel criar o arquivo de saida.");
		}catch(IllegalArgumentException e){
			System.out.println("Numero invalido de times para a proxima fase.");
		}
	}
}