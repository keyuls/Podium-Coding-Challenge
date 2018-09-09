package com.podium.challenge;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/*
 * Compare and sort map by value to get top three results.
 * Ref: https://www.programcreek.com/2013/03/java-sort-map-by-value
 * */
public class ValueComparator implements Comparator<Dealer>  {
	HashMap<Dealer, Integer> map = new HashMap<Dealer, Integer>();
	 
	public ValueComparator(Map<Dealer, Integer> leaderboard){
		this.map.putAll(leaderboard);
	}
 
	@Override
	public int compare(Dealer d1, Dealer d2) {
		if(map.get(d1) >= map.get(d2)){
			return -1;
		}else{
			return 1;
		}	
	}
}
