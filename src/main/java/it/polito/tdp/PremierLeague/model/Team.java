package it.polito.tdp.PremierLeague.model;

public class Team {
	Integer teamID;
	String name;
	private int punteggio;
	
	private int nReporter;

	public Team(Integer teamID, String name) {
		super();
		this.teamID = teamID;
		this.name = name;
	}
	
	public Integer getTeamID() {
		return teamID;
	}
	public void setTeamID(Integer teamID) {
		this.teamID = teamID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPunteggio(int aggiungi) {
		this.punteggio+=aggiungi;
	}
	
	public void azzerapunteggio() {
		this.punteggio=0;
	}
	
	public int getPunteggio() {
		return punteggio;
	}
	
	public int getnReporter() {
		return nReporter;
	}

	public void setnReporter(int nReporter) {
		this.nReporter = nReporter;
	}
	

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((teamID == null) ? 0 : teamID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (teamID == null) {
			if (other.teamID != null)
				return false;
		} else if (!teamID.equals(other.teamID))
			return false;
		return true;
	}
	
}
