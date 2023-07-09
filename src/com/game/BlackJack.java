package com.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.game.model.Card;
import com.game.model.Person;

public class BlackJack {

	private static List<Card> getDeck() {
		List<String> cardTypes = List.of("Hearts", "Spades", "Diamonds", "Clubs");
		List<Card> totalCards = new ArrayList<>();

		for (String type : cardTypes) {
			for (int i = 1; i <= 13; i++) {
				int val = i;
				String cardName = type + "- " + i;
				if (i == 1)
					cardName = type + "- Ace";
				// setting all face cards to default 10 value
				if (i > 10) {
					val = 10;
				}
				if (i == 11) {
					cardName = type + "- Jack";
				}
				if (i == 12) {
					cardName = type + "- Queen";
				}

				if (i == 13) {
					cardName = type + "- King";
				}

				totalCards.add(new Card(cardName, val));
			}
		}
		return totalCards;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		// prepare the deck for Game
		List<Card> deck = getDeck();
		System.out.println("Enter no.Of Players :");
		int playersCount = scanner.nextInt();
		scanner.nextLine();
		List<Person> players = getPlayers(playersCount);
		Person dealer = new Person("Dealer", true);

		List<Person> gameOverPlayers = new ArrayList<Person>();
		List<Person> winners = new ArrayList<Person>();
		List<Person> losers = new ArrayList<Person>();

		// shuffle deck once before game starts
		Collections.shuffle(deck);
		
		// deal initial 2 cards for every player
		dealInitialCards(deck, players);

		//deal Cards and check if players busted
		for (Person player : players) {
			boolean isBusted = dealCards(deck, player);
			if (isBusted) {
				System.out.println("You are Busted :" + player.getName());
				gameOverPlayers.add(player);
			}
		}
		players.removeAll(gameOverPlayers);
		
		//deal cards for Dealer separately so winning strategy can be changed.
		boolean isDealerBusted = dealerDealCards(deck, dealer);

		// final outcome of game
		if (isDealerBusted) {
			winners.addAll(players);
		} else {
			winners.addAll(players.stream().filter(p -> p.getCardsValue() > dealer.getCardsValue()).toList());
			losers.addAll(players.stream().filter(p -> p.getCardsValue() <= dealer.getCardsValue()).toList());
		}
		if(winners.isEmpty()) {
			winners.add(dealer);
		}
		System.out.println(
				"Winners of the game: " + winners.stream().map(Person::getName).collect(Collectors.joining(",")));
		System.out.println(
				"Losers of the game: " + losers.stream().map(Person::getName).collect(Collectors.joining(",")));
	}

	private static void dealInitialCards(List<Card> deck, List<Person> players) {
		for(int i= 1; i<2; i++) {
			for (Person player : players) {
				player.addCard(deck.remove(0));
			}
		}
	}

	private static boolean dealCards(List<Card> deck, Person player) {
		Scanner scanner = new Scanner(System.in);
		boolean hit = true;
		boolean isBusted = false;
		while (hit) {
			System.out.print(player.getName() + " !! Do you want to hit (Y/N)?  :");
			String decision = scanner.nextLine();
			hit = "y".equalsIgnoreCase(decision);

			if (!hit) {
				break;
			}
			// remove card from deck
			Card card = deck.remove(0);
			// check for Ace card
			if (card.getName().contains("Ace")) {
				convertAceCardVal(card, player);
			}
			player.addCard(card);
			System.out.println("Cards Dealt:" + player.getCardsDealt());
			isBusted = checkResult(player);
			hit = !isBusted;
		}
		return isBusted;
	}
	
	private static boolean dealerDealCards(List<Card> deck, Person dealer) {
		//Scanner scanner = new Scanner(System.in);
		boolean hit = true;
		boolean isBusted = false;
		while (hit) {
			//System.out.print(dealer.getName() + " !! Do you want to hit (Y/N)?  :");
			//String decision = scanner.nextLine();
			//hit = "y".equalsIgnoreCase(decision);

			if (!hit) {
				break;
			}
			// remove card from deck
			Card card = deck.remove(0);
			dealer.addCard(card);
			System.out.println("Cards Dealt:" + dealer.getCardsDealt());
			isBusted = checkResult(dealer);
			if(dealer.getCardsValue() >17) {
				break;
			}
			hit = !isBusted;
		}
		return isBusted;
	}

	private static void convertAceCardVal(Card aceCard, Person player) {
		if ((player.getCardsValue() + 11) > 21) {
			System.out.println("Player cards total value is more than 21, with Ace Card. Defaulting it to 1");
		} else {
			aceCard.setValue(11);
		}
	}

	private static boolean checkResult(Person player) {
		boolean isBusted = false;
		System.out.println(player.getName() + ", Your cards Value :" + player.getCardsValue());
		if (player.getCardsValue() > 21) {
			isBusted = true;
		}
		return isBusted;
	}

	private static List<Person> getPlayers(int playersCount) {
		List<Person> players = new ArrayList<Person>();
		for (int i = 1; i <= playersCount; i++) {
			players.add(new Person("Player " + i, false));
		}

		return players;
	}

}
