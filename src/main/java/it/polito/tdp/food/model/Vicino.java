package it.polito.tdp.food.model;

public class Vicino implements Comparable <Vicino>{
	Food f;
	Double differenza ;
	public Vicino(Food f, Double differenza) {
		super();
		this.f = f;
		this.differenza = differenza;
	}
	public Food getF() {
		return f;
	}
	public void setF(Food f) {
		this.f = f;
	}
	public Double getDifferenza() {
		return differenza;
	}
	public void setDifferenza(Double differenza) {
		this.differenza = differenza;
	}
	@Override
	public int compareTo(Vicino o) {
		return this.differenza.compareTo(o.differenza);
		
	}
	@Override
	public String toString() {
		return  f + " = " + differenza + "\n";
	}
	
	
}
