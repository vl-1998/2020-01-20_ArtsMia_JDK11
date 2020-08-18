package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private ArtsmiaDAO dao;
	private Graph <Artist,DefaultWeightedEdge> grafo;
	private Map<Integer, Artist> idMap;
	private List<Artist> bestCammino;
	
	public Model () {
		this.dao = new ArtsmiaDAO ();
	}
	
	public List<String> getRole(){
		return this.dao.getRole();
	}
	
	public void creaGrafo(String role) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		this.dao.getVertex(role, idMap);
		
		for (Artist a : this.idMap.values()) {
			this.grafo.addVertex(a);
		}
		
		for (Adiacenza a : this.dao.getEdge(role, idMap)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	
	public List<Artist> getVertex(){
		List<Artist> vertici = new ArrayList<>(this.grafo.vertexSet()); 
		return vertici;
	}
	public List<DefaultWeightedEdge> getEdge(){
		List<DefaultWeightedEdge> archi = new ArrayList<>(this.grafo.edgeSet()); 
		return archi;
	}
	
	public List<Adiacenza> direttamenteConnessi(String role){
		List<Adiacenza> result = this.dao.getEdge(role, idMap);
				
		Collections.sort(result);
		return result;
	}
	
	public boolean idCorretto (Integer artistId) {
		if (this.idMap.containsKey(artistId)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public List<Artist> getCammino (Integer artistId){
		this.bestCammino = new ArrayList<>();
		
		List<Artist> parziale = new ArrayList<>();
		Artist partenza = this.idMap.get(artistId);
		parziale.add(partenza);
		
		cerca(parziale, -10);
		
		return bestCammino;
	}
	
	private void cerca(List<Artist> parziale, Integer peso) {
		Artist ultimo = parziale.get(parziale.size()-1);
		List<Artist> vicini = Graphs.neighborListOf(this.grafo, ultimo);
		
		for(Artist a : vicini) {
			if (!parziale.contains(a) && peso == -10) { //se parziale non contiene già l'artista selezionato	
				parziale.add(a);
				peso = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, a));
				cerca(parziale, peso);
				parziale.remove(a);
			} else {
				if(!parziale.contains(a) && (int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, a)) == peso) {
					parziale.add(a);
					cerca(parziale, peso);
					parziale.remove(a);
				}
			}
		}
		
		
		if(parziale.size()>this.bestCammino.size()) {
			this.bestCammino = new ArrayList<>(parziale);
		}
		
	}
	

	
	

}
