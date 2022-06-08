package it.polito.tdp.PremierLeague.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{
	
	public enum EventType{
		
		VINCENTE,
		PERDENTE,
		PAREGGIO
	}
	
	private Match m;
	private LocalDateTime date;
	private EventType type;
	
	public Evento(Match m, LocalDateTime date, EventType type) {
		super();
		this.m = m;
		this.date = date;
		this.type = type;
	}

	public Match getM() {
		return m;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public EventType getType() {
		return type;
	}

	@Override
	public int compareTo(Evento o) {
		return this.date.compareTo(o.date);
	}
	

}
