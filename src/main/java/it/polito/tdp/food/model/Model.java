package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	FoodDao dao;
	Map <Integer, Food> idMap;
	
	SimpleDirectedWeightedGraph <Food, DefaultWeightedEdge> grafo;
	
	public Model() {
		dao= new FoodDao();
		idMap=new HashMap <Integer, Food>();
		dao.listAllFoods(idMap);
		
	}
	
	public void creaGrafo(int porzioni) {
		grafo= new SimpleDirectedWeightedGraph <Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.listVERTICI(idMap, porzioni));
		for(Adiacenza a: dao.listAdiacenze(idMap, porzioni)) {
			if(grafo.vertexSet().contains(a.getF1())&& grafo.vertexSet().contains(a.getF2())) {
				Graphs.addEdge(grafo, a.getF1(), a.getF2(), a.getPeso());
			}
		}
		
	}
	
	public int getNvertici() {
		return grafo.vertexSet().size();
	}
	public int getNArchi() {
		return grafo.edgeSet().size();
	}

	public FoodDao getDao() {
		return dao;
	}

	public List <Vicino> trovaVicini(Food selezionato){
		List <Vicino> vicini= new ArrayList <Vicino>();
		for(DefaultWeightedEdge e : this.grafo.edgesOf(selezionato)) {
			Food vicino= Graphs.getOppositeVertex(this.grafo, e,selezionato);
			double peso= grafo.getEdgeWeight(e);
			vicini.add(new Vicino(vicino, peso));
		}
		Collections.sort(vicini);
		return vicini;
	}

	public Map<Integer, Food> getIdMap() {
		return idMap;
	}

	

	public SimpleDirectedWeightedGraph<Food, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	
	
	
}
