package it.polito.tdp.rivers.model;


import java.time.LocalDate;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.rivers.db.RiversDAO;
import it.polito.tdp.rivers.model.Event.EventType;


public class Model {
	private RiversDAO dao;
	private List<River> rivers;
	
	//definisco gli eventi
	private PriorityQueue<Event> queue;
	
	//parametri in ingresso
	private double Q; //capienza totale
	private double C; //livello d'acqua
	private double flussoOutMin; //flusso out minimo
	
	//stato del mondo
	private double flussoIn;
	private double flussoOut;
	private double fMed;
	
	
	//durata eventi
  //intervallo entrata acqua e uscita, ogni giorno, utilizzo quindi la data del flusso	
	//parametri in uscita
	private int giorniSenzaIrrigazioneMinima;
	private double CMedia;
	
	//durata simulazione
	//data dalla dimensione della List<Flow> trovata per un fiume
	
	
	public Model() {
		dao=new RiversDAO();
		rivers=dao.getAllRivers();
	}
	
	public void init(List<Flow> flow, double k) {
		queue= new PriorityQueue<>();
		
		this.fMed=this.getMedia(flow)*3600*24;
		this.Q=k*this.fMed*30;  //conversione di fmed da m^3/s a m^3/giorno e poi *30 come detto da testo
		System.out.println("Q: "+this.Q);
		this.C=this.Q/2;
		System.out.println("C: "+this.C);
		this.flussoOutMin=0.8*this.fMed;
		System.out.println("FLUSSO OUT MIN: "+this.flussoOutMin);
		this.flussoOut=0;
		this.giorniSenzaIrrigazioneMinima=0;
		this.CMedia=0;
		for(Flow f: flow) {
			this.queue.add(new Event(f.getDay(),EventType.ENTRATA,f));
		}
		
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e= this.queue.poll(); //finchè la coda è vuota estraggo l'evento e lo gestisco
			processEvent(e);
			//System.out.println(e);
		}
	}
	
	private void processEvent(Event e) {
		LocalDate oggi=e.getDay();
		switch(e.getType()) {
		case ENTRATA:
			this.flussoIn=e.getFlow().getFlow()*3600*24;  //conversione in m^3/giorno
			this.C+=this.flussoIn;	
			if(this.C>this.Q)
				this.queue.add(new Event(oggi,EventType.TRACIMAZIONE,e.getFlow()));
			double n=Math.random()*10;
			if(n<0.5) 
				this.queue.add(new Event(e.getDay(),EventType.IRRIGAZIONE,e.getFlow()));
			else
				this.queue.add(new Event(e.getDay(),EventType.USCITA,e.getFlow()));
			break;
		case USCITA:
			if(this.C>this.flussoOutMin) {
				this.C-=this.flussoOutMin;
				this.CMedia+=this.C;
			}
			else {
				this.giorniSenzaIrrigazioneMinima++;
				this.C=0;
				this.CMedia+=this.C;
			}
			break;
		case IRRIGAZIONE:
			this.flussoOut=10*this.flussoOutMin;
			if(this.C>this.flussoOut) {
				this.C-=this.flussoOut;
				this.CMedia+=this.C;
			}
			else {
				this.giorniSenzaIrrigazioneMinima++;
				this.C=0;
				this.CMedia+=this.C;
			}
			break;
		case TRACIMAZIONE:
			this.flussoOut=this.C-this.Q;
			this.C-=this.flussoOut;
			break;
		}
		
	}
	
	public List<Flow> getFlowsPerRiver(River river){
		return dao.getAllFlows(river);
	}
	
	public List<River> getRivers(){
		return rivers;
	}
	
	public double getMedia(List<Flow> flows) {
		double media=0;
		for(Flow f: flows) {
			media+=f.getFlow();
		}
		media=media/flows.size();
		return media;
	}
	
	public int getGiorniSenzaIrrigazione() {
		return this.giorniSenzaIrrigazioneMinima;
	}
	
	public double getCMedia(List<Flow> flow) {
		//long i=ChronoUnit.DAYS.between(this.start, this.end);
		//System.out.println("DIFFERENZA GIORNI: "+i);
		return this.CMedia/flow.size();
	}
}
