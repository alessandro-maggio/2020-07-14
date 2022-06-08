package it.polito.tdp.PremierLeague.model;

public class Adiacenza implements Comparable<Adiacenza>{
	
	private Team team;
	private int peso;
	
	public Adiacenza(Team team, int peso) {
		super();
		this.team = team;
		this.peso = peso;
	}

	public Team getTeam() {
		return team;
	}

	public int getPeso() {
		return peso;
	}

	@Override
	public int compareTo(Adiacenza o) {
		return this.peso-o.peso;
	}
	
	

}
