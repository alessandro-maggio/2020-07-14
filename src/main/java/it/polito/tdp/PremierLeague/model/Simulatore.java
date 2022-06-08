package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.PremierLeague.model.Evento.EventType;

public class Simulatore {
	
	//coda degli eventi
	private PriorityQueue<Evento> queue;
	
	//output
	private double media;
	private int nPartiteInferioreSoglia;
	
	//input
	private int nCritici;
	private int soglia;
	
	//stato del mondo
	private List<Match> tuttiMatch;
	private Map<Integer, Team> idMap;
	private Graph<Team, DefaultWeightedEdge> grafo;
	
	private List<Adiacenza> peg;
	private List<Adiacenza> meg;
	
	
	public void init(int nCritici, int soglia, List<Match> tuttiMatch, Map<Integer,Team> idMap, Graph<Team, DefaultWeightedEdge> grafo) {
		
		this.grafo= grafo;
		this.idMap= idMap;
		this.tuttiMatch= tuttiMatch;
		this.queue= new PriorityQueue<>();
		this.nCritici= nCritici;
		this.soglia= soglia;
		
		for(Match m: tuttiMatch) {   //inizialmente imposto il n di critici di ogni partita a n dato dall'utente
			idMap.get(m.teamHomeID).setnReporter(nCritici);
			idMap.get(m.teamAwayID).setnReporter(nCritici);
			
//			m.setnReporter(nCritici);
			double caso= Math.random();
			
			if(m.getReaultOfTeamHome()==0) {
				Evento e= new Evento(m, m.getDate(), EventType.PAREGGIO);
				queue.add(e);
			}
			
			else if(caso<0.5) {
				Evento e= new Evento(m, m.getDate(), EventType.VINCENTE);
				queue.add(e);
			}
			else if(caso>0.5 && caso <0.7) {
				Evento e= new Evento(m, m.getDate(), EventType.PERDENTE);
				queue.add(e);
			}
		}
		
	}
	
	public void run() {
		
		while(!this.queue.isEmpty()) {
			Evento e= this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Evento e) {
		
		
		switch (e.getType()) {
		case VINCENTE:
			Team vecchioWin= idMap.get(e.getM().getTeamVincente());
			getPeggioMeglio(vecchioWin);
			Team migliore= scegliACaso(meg);
			if(migliore!=null) {
				vecchioWin.setnReporter(vecchioWin.getnReporter()-1);
				migliore.setnReporter(migliore.getnReporter()+1);
			}
			
			break;

		case PERDENTE:
			Team vecchioLos= idMap.get(e.getM().getTeamPerdente());
			getPeggioMeglio(vecchioLos);
			Team peggiore= scegliACaso(peg);
			if(peggiore!=null) {
				int nRepACaso=(int) Math.random()*(nCritici-1+1);
				vecchioLos.setnReporter(vecchioLos.getnReporter()-nRepACaso);
				peggiore.setnReporter(peggiore.getnReporter()+nRepACaso);
			}
			break;
			
		case PAREGGIO:
			break;
		}
		
	}

	private Team scegliACaso(List<Adiacenza> daScegliere) {
		
		List<Adiacenza> test= new ArrayList<>(daScegliere);
		
		if(daScegliere.isEmpty()) {
			return null;
		}
		
		int i= (int)(Math.random()*daScegliere.size());
		
		
		
		return daScegliere.get(i).getTeam();
		
	}
	
	public int getPartiteInferioreSoglia() {
		
		nPartiteInferioreSoglia=0;
		
		for(Team t: idMap.values()) {
			if(t.getnReporter()<soglia) {
				nPartiteInferioreSoglia++;
			}
		}
		
		return nPartiteInferioreSoglia;
	}
	
	
	private void getPeggioMeglio(Team start) {
		
		meg= new ArrayList<>();
		
		Set<DefaultWeightedEdge> verticiIn= this.grafo.incomingEdgesOf(start);
		for(DefaultWeightedEdge e: verticiIn) {
			Adiacenza ad= new Adiacenza(this.grafo.getEdgeSource(e), (int)this.grafo.getEdgeWeight(e));
			meg.add(ad);
		}
		
	
		
		peg= new ArrayList<>();
		
		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(start)) {
			Adiacenza ad= new Adiacenza(this.grafo.getEdgeTarget(e), (int)this.grafo.getEdgeWeight(e));
			peg.add(ad);
		}
		
	
		
		
	}
	

}
