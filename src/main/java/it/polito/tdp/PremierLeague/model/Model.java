package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Team, DefaultWeightedEdge> grafo;
	//private List<Team> vertici;
	private Map<Integer, Team> idMap;  //lista vertici
	//private List<Coppia> archi;
	private List<Adiacenza> ordinataPeggiori;
	private List<Adiacenza> ordinataMigliori;
	private List<Match> tuttiMatch;
	
	
	
	public Model() {
		this.dao= new PremierLeagueDAO();
		
		this.idMap= dao.listAllTeams();
		
		this.tuttiMatch= dao.listAllMatches();
		
	}
	
	
	public String creaGrafo(  ) {
		
		this.grafo= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici= dao.getVertici();
		
		
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		this.dao.assegnaPunti(idMap);
		
		for(Team t1: idMap.values()) {
			for(Team t2: idMap.values()) {
				if(!t1.equals(t2) && !this.grafo.edgeSet().contains(this.grafo.getEdge(t1, t2)) && !this.grafo.edgeSet().contains(this.grafo.getEdge(t2, t1))) {
					if(t1.getPunteggio()>t2.getPunteggio()) {
						Graphs.addEdge(this.grafo, t1, t2, Math.abs(t1.getPunteggio()-t2.getPunteggio()));
					}
					else if(t1.getPunteggio()<t2.getPunteggio()) {
						Graphs.addEdge(this.grafo, t2, t1, Math.abs(t1.getPunteggio()-t2.getPunteggio()));
					}
				}
			}
		}
		
		String s= "GRAFO CREATO!\n#VERTICI: "+this.grafo.vertexSet().size()+"\n#ARCHI: "+this.grafo.edgeSet().size();
		
		return s;
		
		
	}
	
	
	
	public String getPeggioMeglio(Team start) {
		
		ordinataMigliori= new ArrayList<>();
		
		Set<DefaultWeightedEdge> verticiIn= this.grafo.incomingEdgesOf(start);
		for(DefaultWeightedEdge e: verticiIn) {
			Adiacenza ad= new Adiacenza(this.grafo.getEdgeSource(e), (int)this.grafo.getEdgeWeight(e));
			ordinataMigliori.add(ad);
		}
		
		String string= "SQUADRE MIGLIORI:\n";
		
		Collections.sort(ordinataMigliori);
		
		for(Adiacenza a: ordinataMigliori) {
			string+=a.getTeam().toString()+"("+a.getPeso()+")\n";
		}
		
		ordinataPeggiori= new ArrayList<>();
		
		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(start)) {
			Adiacenza ad= new Adiacenza(this.grafo.getEdgeTarget(e), (int)this.grafo.getEdgeWeight(e));
			ordinataPeggiori.add(ad);
		}
		
		string+="\nSQUADRE PEGGIORI:\n";
		
		Collections.sort(ordinataPeggiori);
		
		for(Adiacenza a: ordinataPeggiori) {
			string+=a.getTeam().toString()+"("+a.getPeso()+")\n";
		}
		
		return string;
		
		
		
	}
	
	
	public int simula(int nCritici, int soglia) {
		
		Simulatore sim= new Simulatore();
		sim.init(nCritici, soglia, tuttiMatch, idMap, grafo);
		sim.run();
		
		for(Team t: idMap.values()) {
			System.out.println(toString().toString()+"  "+t.getnReporter());
		}
		return sim.getPartiteInferioreSoglia();
		
		
		
	}


	public Map<Integer, Team> getVertici( ){

		return idMap;
	}
	
	
	public List<Adiacenza> getMigliori(){
		return ordinataMigliori;
	}
	
	public List<Adiacenza> getPeggiori(){
		return ordinataPeggiori;
	}
	
	
}
