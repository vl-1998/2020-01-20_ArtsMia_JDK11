package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getRole (){
		String sql = "select distinct role from authorship order by role asc";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				String s = new String (res.getString("role"));
				result.add(s);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getVertex(String role, Map <Integer, Artist> idMap){
		String sql = "select distinct ar.artist_id, ar.name\n" + 
				"from artists ar, authorship au\n" + 
				"where ar.artist_id=au.artist_id\n" + 
				"and au.role = ?";
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				Artist a = new Artist (res.getInt("ar.artist_id"), res.getString("ar.name"));
				idMap.put(a.getId(), a);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<Adiacenza> getEdge(String role, Map<Integer, Artist> idMap){
		String sql = "select a1.artist_id, a2.artist_id, count(distinct eb1.exhibition_id) as peso\n" + 
				"from authorship a1, authorship a2, exhibition_objects eb1, exhibition_objects eb2\n" + 
				"where a1.artist_id <> a2.artist_id\n" + 
				"and a1.artist_id > a2.artist_id\n" + 
				"and a1.role = a2.role \n" + 
				"and a1.role = ?\n" + 
				"and a1.object_id = eb1.object_id\n" + 
				"and a2.object_id = eb2.object_id\n" + 
				"and eb1.exhibition_id = eb2.exhibition_id\n" + 
				"group by a1.artist_id, a2.artist_id";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				Artist a1 = idMap.get(res.getInt("a1.artist_id"));
				Artist a2 = idMap.get(res.getInt("a2.artist_id"));
				Adiacenza a = new Adiacenza (a1, a2, res.getInt("peso"));
				result.add(a);
			}
			conn.close();
			return result;
			
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
}
