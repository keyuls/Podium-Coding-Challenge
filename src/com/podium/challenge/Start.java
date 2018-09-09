package com.podium.challenge;

import java.io.IOException;
/*
 * Program execution begins here
 * */
public class Start {

	public static void main(String[] args) throws IOException {
		Scrapper sc = new Scrapper();
		sc.getReviewPageLink("https://www.dealerrater.com/dealer/McKaig-Chevrolet-Buick-A-Dealer-For-The-People-review-23685/"); //Start scrapping 
		sc.topThreeDealers(); // Get top three dealers

	}

}
