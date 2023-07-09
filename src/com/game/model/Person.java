package com.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Person {
    
    String name;
    List<Card> cards = new ArrayList<Card>();
    boolean dealt;
    boolean isDealer;
    
    public Person(String name, boolean isDealer) {
    	this.name = name;
    	this.isDealer = isDealer;
    }
    public int getCardsValue() {
        return cards.stream().collect(Collectors.summingInt(Card::getValue));
    }
    
    public String getCardsDealt() {
        return cards.stream().map(Card::getName).collect(Collectors.joining(", "));
    }
    
    public void addCard(Card card) {
    	this.cards.add(card);
    }
    
    public String getName() {
    	return this.name;
    }
    
    public boolean isDealer() {
    	return this.isDealer;
    }
}
