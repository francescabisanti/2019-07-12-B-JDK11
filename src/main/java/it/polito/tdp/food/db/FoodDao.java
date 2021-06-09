package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public void listAllFoods(Map <Integer, Food> idMap){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
					Food f=new Food(res.getInt("food_code"),
							res.getString("display_name")
							);
					idMap.put(f.getFood_code(), f);
				
			}
			
			conn.close();
		

		} catch (SQLException e) {
			e.printStackTrace();
			
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List <Food> listVERTICI(Map <Integer, Food> idMap, int porzioni){
		String sql = "SELECT f.food_code AS id , COUNT(DISTINCT (p.portion_id)) AS conto "
				+ "FROM `portion` p, food f "
				+ "WHERE p.food_code=f.food_code "
				+ "GROUP BY f.food_code "
				+ "HAVING conto>= ? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, porzioni);
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
					Food f= idMap.get(res.getInt("id"));
					if(f!=null) {
						list.add(f);
					}
				
			}
			
			conn.close();
			return list;
		

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
			
		}

	}
	public List <Adiacenza> listAdiacenze(Map <Integer, Food> idMap, int porzioni){
		String sql = "SELECT DISTINCT p1.food_code AS f1, p2.food_code AS f2, (AVG(p1.saturated_fats) - AVG(p2.saturated_fats) ) AS peso "
				+ "FROM `portion` p1, `portion` p2 "
				+ "WHERE p1.food_code> p2.food_code "
				+ "GROUP BY p1.food_code, p2.food_code "
				+ "HAVING peso!=0  " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Adiacenza> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
					Food f1= idMap.get(res.getInt("f1"));
					Food f2= idMap.get(res.getInt("f2"));
					if(f1!=null&& f2!=null) {
						double peso=res.getDouble("peso");
						if(peso<0) {
							Adiacenza a = new  Adiacenza(f2,f1, Math.abs(peso));
							list.add(a);
						}
						else {
							Adiacenza a = new  Adiacenza(f1,f2, peso);
							list.add(a);
						}
					}
				
			}
			
			conn.close();
			return list;
		

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
			
		}

	}
	
}
