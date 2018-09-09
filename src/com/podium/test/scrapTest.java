package com.podium.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.jsoup.nodes.Document;
import static org.mockito.Mockito.*;

import com.podium.challenge.Dealer;
import com.podium.challenge.Scrapper;

public class scrapTest {
	private Scrapper scrap;
	private Dealer dealer;
	
	@Test
	public void testScrapPageType() throws IOException{
		scrap = new Scrapper();
		assertTrue(scrap.scrapPage("https://www.dealerrater.com/") instanceof Document);		
	}
	
	@Test
	public void testDealerName(){
		dealer = new Dealer(123,"Dealer1");
		assertEquals("Dealer1", dealer.getFirstName());
	}
	
	@Test
	public void testDealerId(){
		dealer = new Dealer(123,"Dealer1");
		assertEquals(123, dealer.getId());
	}
	
	@Test
	public void testDealerNameType(){
		dealer = new Dealer(123,"Dealer1");
		assertTrue( dealer.getFirstName() instanceof String);
	}

}
