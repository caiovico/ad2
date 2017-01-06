import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.io.*;

class VolleyTeam implements Comparable<VolleyTeam>{
	private String name;
	private int points;
	private int victories;
	private int balance;

	public static VolleyTeam merge(VolleyTeam t1, VolleyTeam t2){
		if (t1.getName().equals(t2.getName())){
			t1.addPoints(t2.points);
			t1.addVictory(t2.victories);
			t1.changeBalance(t2.balance);
			return t1;
		}else{	
		throw new IllegalArgumentException("Nomes diferentes para o merge");
		}
	}

	public int compareTo(VolleyTeam t){
	//compares in descending order points, victories and balance(difference);
	if (this.points!=t.points) return t.points-this.points;
	if (this.victories!=t.victories) return t.victories-this.victories;
	return t.balance-this.balance;
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
	public void addVictory(int i){
		victories+=i;
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
		//get number of played sets, the winner and league points
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
	public static void writeFile(File f, int n, List<String> winner) throws IOException{
		//reads n teams which go to next phase
		if ((n>winner.size())||(n<=0)) throw new IllegalArgumentException("Erro interno");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(f))){
			for(int i=0;i<n;i++){
				writer.write(winner.get(i));
				writer.newLine();
			}
		}
	}
	private static List<String> processGames(List<String> lines){ 
		//List<VolleyTeam> raw = new ArrayList<>();
		List<String> vtList = 
		lines.stream()
			 .map(LeagueGame::new)
			 .flatMap(LeagueGame::getTeams)
			 .collect(Collectors.toMap(VolleyTeam::getName,
			 						   vt->vt,
			 						   VolleyTeam::merge))
			 .entrySet()
			 .stream()
			 .map(Map.Entry::getValue)
			 .sorted()
			 .map(VolleyTeam::getName)
			 .collect(Collectors.toList());

			 return vtList;
		}
	public static void main(String[] args){
		List<String> games = null;
		//input File is converted to a List
		try{
			games = readFile(new File(args[0]));
		}catch (IOException e){
			System.out.println("Nao foi possivel ler o arquivo especificado.");
			System.exit(0);
		}
		//process the read lines using LeagueGame objects
		List<String> teams = processGames(games);
		//output
		try{
			writeFile(new File("out-"+args[0]), Integer.parseInt(args[1]), teams);
		}catch(IOException e){
			System.out.println("Nao foi possivel criar o arquivo de saida.");
		}catch(IllegalArgumentException e){
			e.printStackTrace();
			System.out.println("Numero invalido de times para a proxima fase.");
		}
	}
}