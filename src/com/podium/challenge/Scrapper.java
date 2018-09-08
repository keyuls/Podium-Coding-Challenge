package com.podium.challenge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrapper {

	private Map<Dealer, Integer> leaderboard = new HashMap<Dealer,Integer>();
	private Document document;
	private Element element;
	private List<String> pageList = new ArrayList<String>();
	private String website = "https://www.dealerrater.com";
	
	public void getReviewPageLink(String url) throws IOException{		
		document = scrapPage(url);
		//element = document.getElementById("link"); 
		Elements resultLinks = document.select("div#link > a");
		for(Element el : resultLinks){
			if(el.text().contains("Review")){
				pageList.add(el.attr("href"));
				//System.out.println(el.attr("href"));
			}
		}
		getReviewList();
	}
	
	public Document scrapPage(String url) throws IOException{
		Document doc = Jsoup.connect(url).get();
		return doc;
	}
	
	
	public void getReviewList() throws IOException{
		
		 Iterator<String> iterator = pageList.iterator();
	     while (iterator.hasNext()) {
	    	 String page =iterator.next();
			document = scrapPage( website+page);
			analyzeReviews(document);
			
			// get next page
	/*		if(pageList.size()<5){
				String pageNumber = String.valueOf( pageList.size()+1);
				String attribute = "a[rel=page_num_"+pageNumber+"]";
				element = document.select(attribute).first();
				System.out.println(element.attr("href"));
				pageList.add(element.attr("href"));
			} */
		}
	}
	
	public void analyzeReviews(Document doc){
		// get all reviews from the given page
		Elements reviews = doc.select("div.review-wrapper");
		for(Element review: reviews){
			getDealersForReview(review);
		}
	}
	/*	
	public void getDealersForReview(Element review){
		String reviewData = review.select("p.review-content").first();
		
		for(){
			Dealer dealer = new Dealer(,);
			// if rating is 5 stars
			if(){
				mentionInReview();
			}
		}
	}
	
	public void mentionInReview(){
		
	}
	
	public void topThreeDealers(){
		
	}
	*/
}
