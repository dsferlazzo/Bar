package it.polito.tdp.bar.model;

import java.time.Duration;

public class Event  implements Comparable<Event>{
	
	public enum EventType{	//DEFINISCO QUALI SONO I MIEI EVENTI
		ARRIVO_GRUPPO_CLIENTI,
		TAVOLO_LIBERATO
	}
	
	private EventType type;		//ATTRIBUTO DEL MIO SINGOLO EVENTO
	private Duration time;		//ASSUMIAMO IL TEMPO COME UN INTERO/DURATION
	private int nPersone;
	private Duration durata;	//ASSUMIAMO LA DURATA COME UN INTERO/DURATION
	private double tolleranza;
	private Tavolo tavolo;
	
	public Event(EventType type, Duration time, int nPersone, Duration durata, double tolleranza, Tavolo tavolo) {
		super();
		this.type = type;
		this.time = time;
		this.nPersone = nPersone;
		this.durata = durata;
		this.tolleranza = tolleranza;
		this.tavolo = tavolo;
	}

	public EventType getType() {
		return type;
	}

	public Duration getTime() {
		return time;
	}

	public int getnPersone() {
		return nPersone;
	}

	public Duration getDurata() {
		return durata;
	}

	public double getTolleranza() {
		return tolleranza;
	}

	public Tavolo getTavolo() {
		return tavolo;
	}
	
	public void setTavolo(Tavolo tavolo) {
		this.tavolo=tavolo;
	}

	@Override
	public int compareTo(Event o) {	//RIMANDO AL COMPARETO GIA IMPLEMENTATO DALLA CLASSE DURATION
		return this.time.compareTo(o.getTime());
	}
	
	
	
		
}
