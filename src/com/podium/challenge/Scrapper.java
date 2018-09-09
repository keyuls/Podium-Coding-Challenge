package com.podium.challenge;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrapper {

	private Map<Dealer, Integer> leaderboard = new HashMap<Dealer,Integer>();
	private Document document;
	private Element element;
	private Elements elements;
	private String pageUrl;
	private int pageCount=0;
	private String website = "https://www.dealerrater.com";

	/*Get review page URL from the review section*/
	public void getReviewPageLink(String url) throws IOException{		
		document = scrapPage(url);
		elements = document.select("div#link > a");
		for(Element el : elements){
			if(el.text().contains("Review")){
				pageUrl = el.attr("href");
				pageCount++;
			}
		}
		getReviewList(pageUrl);
	}
	
	/*Scrap the page from the URL*/
	public Document scrapPage(String url) throws IOException{
		document = Jsoup.connect(url).get();
		return document;
	}
	
	/*Extract all reviews from the given page*/
	public void getReviewList(String pageUrl) throws IOException{			
		while(pageCount<6){
			document = scrapPage( website+pageUrl);
			analyzeReviews(document);
			String pageNumber = String.valueOf( pageCount+1); 			// get next page to scrap
			String attribute = "a[rel=page_num_"+pageNumber+"]";
			element = document.select(attribute).first();
			pageUrl=element.attr("href");
			pageCount++;
		} 
	}
	
	/* Analyze each review of the page */
	public void analyzeReviews(Document doc){
		Elements reviews = doc.select("div.review-wrapper");
		for(Element review: reviews){
			getDealersForReview(review);
		}
	}
	
	/*Get all the dealers in a specific review */
	public void getDealersForReview(Element review){
		String reviewData = review.select("p.review-content").first().text() ;
		Elements dealerList= review.select("div.review-employee");
		for(Element el : dealerList){
			Element dealerData = el.select("a").first();
			String id = dealerData.attr("data-emp-id");
			String firstName = el.text();
			firstName = firstName.substring(0,firstName.indexOf(" "));
			Dealer dealer = new Dealer(Integer.parseInt(id), firstName);
			if(!leaderboard.containsKey(dealer))
				leaderboard.put(dealer, 0);
			if(el.select("span").first()!=null){
				String rating = el.select("span").first().text();
				
				if(rating.equals("5.0")){
					mentionInReview( reviewData,  dealer);  // check in reviews only if rating is 5 stars for the dealer - For overly positive5
				}				
			}
		}
	}
	
	/*Identify number of times a dealer is mentioned in the specific review*/
	public void mentionInReview(String reviewData, Dealer dealer){
		String [] words = reviewData.split(" ");
		for(String word: words){
			if(word.equals(dealer.getFirstName())){
				leaderboard.put(dealer, leaderboard.get(dealer)+1);
			}
		}
	}
	
	
	/*Retrive top 3 dealers*/
	public void topThreeDealers(){
		int counter=0;
		Comparator<Dealer> comparator = new ValueComparator(leaderboard);  /*Ref: https://www.programcreek.com/2013/03/java-sort-map-by-value*/
		TreeMap<Dealer, Integer> result = new TreeMap<Dealer, Integer>(comparator);
		result.putAll(leaderboard);	
		System.out.println("Id" + "      "+ "Name"+"    "+ "Total Mentions");
		for(Map.Entry<Dealer, Integer> entry: result.entrySet()){
			if(counter>=3)
				break;
			System.out.println(entry.getKey().getId() + "  "+ entry.getKey().getFirstName()+"  "+ entry.getValue());
			counter++;
		}
	}
	
	
	
	
}
