package it.polito.tdp.rivers.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.rivers.db.RiversDAO;
import it.polito.tdp.rivers.model.Event.EventType;


public class Model {
	private RiversDAO dao;
	private List<River> rivers;
	private List<Flow> flows;
	
	//definisco gli eventi
	private PriorityQueue<Event> queue;
	
	//parametri in ingresso
	private double Q; //capienza totale
	private double C; //livello d'acqua
	private double flussoOutMin; //flusso out minimo
	
	//stato del mondo
	private double flussoIn;
	private double flussoOut;
	
	
	//durata eventi
	private int T_IN=1;  //intervallo entrata acqua e uscita, ogni giorno, utilizzo LocalDate.plusDays() 
						//per incrementare di un giorno	
	//parametri in uscita
	private int giorniSenzaIrogazioneMinima;
	private int CMedia;
	
	//durata simulazione
	private LocalDate start;
	private LocalDate end;
	
	
	public Model() {
		dao=new RiversDAO();
		rivers=dao.getAllRivers();
		flows= new ArrayList<>();
	}
	
	public void init(List<Flow> flow, double k) {
		queue= new PriorityQueue<>();
		
		this.Q=k*this.getMedia(flow)*3600*24*30; //conversione di fmed da m^3/s a m^3/giorno e poi *30 come detto da testo
		this.C=this.Q/2;
		this.flussoOutMin=0.8*this.getMedia(flow)*3600*24;
		this.flussoOut=0;
		this.giorniSenzaIrogazioneMinima=0;
		this.CMedia=0;
		this.start=flow.get(0).getDay();
		this.end=flow.get(flow.size()-1).getDay();
		LocalDate oggi=this.start;
		while(oggi.isBefore(end)) {
			this.flussoIn=this.getFlowByDate(flow, oggi)*3600*24; //conversione in m^3/giorno
			this.queue.add(new Event(oggi,EventType.ENTRATA));
			double n=Math.random()*10;
			if(n<0.5) 
				this.queue.add(new Event(oggi,EventType.IRRIGAZIONE));
			else
				this.queue.add(new Event(oggi,EventType.USCITA));
			oggi=oggi.plusDays(T_IN);
		}
		
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e= this.queue.poll(); //finchè la coda è vuota estraggo l'evento e lo gestisco
			System.out.println(e);
			processEvent(e);
		}
	}
	
	private void processEvent(Event e) {
		LocalDate oggi=e.getDay();
		switch(e.getType()) {
		case ENTRATA:
			this.C+=this.flussoIn;			
			if(this.C>this.Q)
				this.queue.add(new Event(oggi,EventType.TRACIMAZIONE));			
			break;
		case USCITA:
			if(this.C>this.flussoOutMin) {
				this.C-=this.flussoOutMin;
				this.CMedia+=this.C;
			}
			else {
				this.giorniSenzaIrogazioneMinima++;
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
				this.giorniSenzaIrogazioneMinima++;
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
	
	private double getFlowByDate(List<Flow> flows, LocalDate data) {
		double flusso = 0;
		for(Flow f: flows) {
			if(f.getDay().equals(data))
				flusso=f.getFlow();			
		}
		return flusso;
	}
	
	public List<Flow> getFlowsPerRiver(River river){
		River fiume=null;
		for(River r:rivers) {
			if(r.getId()==river.getId())
				fiume=r;
		}
		flows.addAll(dao.getAllFlows(fiume));
		return flows;
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
		return this.giorniSenzaIrogazioneMinima;
	}
	
	public double getCMedia() {
		long i=ChronoUnit.DAYS.between(this.start, this.end);
		System.out.println("DIFFERENZA GIORNI: "+i);
		return this.CMedia/i;
	}
}
