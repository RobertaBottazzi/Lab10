package it.polito.tdp.rivers.model;

import java.time.LocalDate;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		TRACIMAZIONE, //flusso in ingresso eccessivo, questo deve essere scaricato in uscita
		IRRIGAZIONE,  //probabilità pari al 5% di avere un flusso richiesto in uscita più elevato,
					  //pari a 10⋅𝑓𝑜𝑢𝑡_𝑚𝑖𝑛 in quanto i campi devono essere irrigati
		ENTRATA,	  //entrata del flusso d'acqua ad inizio giornata
		USCITA		  //uscita del flusso d'acqua a fine giornata
	}
	private LocalDate day;
	private EventType type;
	
	public Event(LocalDate day, EventType type) {
		this.day = day;
		this.type = type;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setTime(LocalDate day) {
		this.day = day;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	@Override
	public int compareTo(Event other) {
		if(this.day.equals(other.day)) {
			if(this.type == EventType.ENTRATA) //I-U
				return -1;
			else //U-I
				return 1;
		}
		else
			return this.day.compareTo(other.day);
	}

	@Override
	public String toString() {
		return "Event [day=" + day + ", type=" + type + "]";
	}
	
	
	

}
