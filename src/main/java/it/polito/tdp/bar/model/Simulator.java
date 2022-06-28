package it.polito.tdp.bar.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.bar.model.Event.EventType;

public class Simulator implements Comparator<Tavolo>{
	
	//PARAMETRI DI OUTPUT, IN QUESTO CASO RACCHIUSI IN UN'UNICA CLASSE
	private Statistiche statistiche;
	
	//PARAMETRI DELLO STATO DEL MONDO
	private List<Tavolo> tavoli;
	
	//PARAMETRI DELLA SIMULAZIONE, FISSI, PER AVERE UNA SIMULAZIONE PIU GENERALIZZATA
	private int NUM_EVENTI = 2000;
	private int T_ARRIVO_MAX = 10;
	private int NUM_PERSONE_MAX = 10;
	private int DURATA_MIN = 60;
	private int DURATA_MAX = 120;
	private double TOLLERANZA_MAX = 0.9;
	private double OCCUPAZIONE_MAX = 0.5;
	
	//CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;
	
	public void init() {
		this.queue = new PriorityQueue<Event>();
		this.statistiche = new Statistiche();
		
		//CREARE I TAVOLI E GLI EVENTI
		creaTavoli();
		creaEventi();
		
	}
	
	public void run() {
		while(!queue.isEmpty()) {
			Event e = queue.poll();
			processaEvento(e);
		}
	}

	private void processaEvento(Event e) {
		
		switch(e.getType()) {
		case ARRIVO_GRUPPO_CLIENTI:
			this.statistiche.incrementaClienti(e.getnPersone()); 	//CONTO I CLIENTI TOTALI
			Tavolo tavolo = null;
			for(Tavolo t : this.tavoli) {
				if(t.getnPosti()>=e.getnPersone() && t.isOccupato()==false &&
						t.getnPosti()*this.OCCUPAZIONE_MAX<=e.getnPersone()) {
					tavolo=t;
				}
			}
			if(tavolo!=null) {
				System.out.format("Trovato un tavolo da %d per %d persone ", tavolo.getnPosti(), e.getnPersone());
				statistiche.incrementaSoddisfatti(e.getnPersone());
				e.setTavolo(tavolo); 	//ASSEGNO IL TAVOLO ALL'EVENTO
				tavolo.setOccupato(true);
				//CREO L'EVENTO DOVE I CLIENTI SE NE VANNO
				queue.add(new Event(EventType.TAVOLO_LIBERATO, e.getTime().plus(e.getDurata()),
						e.getnPersone(),e.getDurata(), e.getTolleranza(), tavolo ));
			} else {
				//CE SOLO IL BANCONE
				double bancone = Math.random();
				if(bancone<=e.getTolleranza()) {
					System.out.format("%d persone si fermano al bancone", e.getnPersone());
					statistiche.incrementaSoddisfatti(e.getnPersone());	//SI FERMANO AL BANCONE
				}
				else {
					System.out.format("%d persone vanno a casa", e.getnPersone());
					statistiche.incrementaInsoddisfatti(e.getnPersone());	//NON SI FERMANO AL BANCONE
				}
			}
			
			
			
			break;
		case TAVOLO_LIBERATO:
			e.getTavolo().setOccupato(false); 	//IMPOSTO A LIBERO I TAVOLI CHE SI SONO LIBERATI
			break;
		
		}
		
	}

	private void creaTavolo(int quantita, int dimensione) {
		for(int i = 0;i<quantita;i++)
			tavoli.add(new Tavolo(dimensione, false));
	}
	
	private void creaTavoli() {
		creaTavolo(2,10);
		creaTavolo(4,8);
		creaTavolo(4,6);
		creaTavolo(5,4);
		
		Collections.sort(this.tavoli, new Comparator<Tavolo>() {
			@Override
			public int compare(Tavolo o1, Tavolo o2) {
				return o1.getnPosti()-o2.getnPosti();
			}
		});	//DA IMPLEMENTARE, CON COMPARATOR
		
		
	}
	
	private void creaEventi() {
		Duration arrivo = Duration.ofMinutes(0);
		for(int i = 0;i<this.NUM_EVENTI;i++) {
			int nPersone = (int) Math.random()*this.NUM_PERSONE_MAX + 1;
			Duration durata = Duration.ofMinutes(this.DURATA_MIN + (int)(Math.random()*(this.DURATA_MAX-this.DURATA_MIN+1)));
			double tolleranza = Math.random() + this.TOLLERANZA_MAX;	//(?)
			
			Event e = new Event(EventType.ARRIVO_GRUPPO_CLIENTI, arrivo, nPersone,
					durata, tolleranza, null);
			this.queue.add(e);
			arrivo = arrivo.plusMinutes((int) (Math.random()*this.T_ARRIVO_MAX + 1));
		}
	}

	@Override
	public int compare(Tavolo o1, Tavolo o2) {
		return o1.getnPosti()-o2.getnPosti();
	}


	
	
}
