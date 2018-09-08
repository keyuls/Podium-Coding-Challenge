package com.podium.challenge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
	private List<String> pageList = new ArrayList<String>();
	private String website = "https://www.dealerrater.com";
	private int pageCount=0;
	private String pageUrl;
	
	public void getReviewPageLink(String url) throws IOException{		
		document = scrapPage(url);
		Elements resultLinks = document.select("div#link > a");
		for(Element el : resultLinks){
			if(el.text().contains("Review")){
				pageUrl = el.attr("href");
				pageCount++;
			}
		}
		getReviewList(pageUrl);
		topThreeDealers();
	}
	
	public Document scrapPage(String url) throws IOException{
		Document doc = Jsoup.connect(url).get();
		return doc;
	}
	
	
	public void getReviewList(String pageUrl) throws IOException{			
		while(pageCount<6){
			document = scrapPage( website+pageUrl);
			analyzeReviews(document);
			String pageNumber = String.valueOf( pageCount+1); 			// get next page
			String attribute = "a[rel=page_num_"+pageNumber+"]";
			element = document.select(attribute).first();
			pageUrl=element.attr("href");
			pageCount++;
		} 
	}
	
	public void analyzeReviews(Document doc){
		// get all reviews from the given page
		Elements reviews = doc.select("div.review-wrapper");
		for(Element review: reviews){
			getDealersForReview(review);
		}
	}
	
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
				// if rating is 5 stars
				if(rating.equals("5.0")){
					mentionInReview( reviewData,  dealer);
				}				
			}
		}
	}
	
	public void mentionInReview(String reviewData, Dealer dealer){
		String [] words = reviewData.split(" ");
		for(String word: words){
			if(word.equals(dealer.getFirstName())){
				leaderboard.put(dealer, leaderboard.get(dealer)+1);
			}
		}
	}
	
		
	public void topThreeDealers(){
		int counter=0;
		Comparator<Dealer> comparator = new ValueComparator(leaderboard);
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
