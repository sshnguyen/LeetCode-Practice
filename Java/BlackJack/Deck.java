public class Deck {
		List<Card> cards = new ArrayList<>();
		
		public Deck() {
			createDeck();
			shuffle();
		}
		public void createDeck() {
			// create a deck by inserting one card of each Point for each Suit
			for (Suit s: Suit.values()) {
				for (Point p: Point.values()) {
					cards.add(new Card(s, p));
				}
			}
		}
		
		public void shuffle() {
			Collections.shuffle(cards);
		}
		
		public Card removeOneCard() {
			// take the last card off the deck (the top one)
			if (cards.size() == 0) throw new RuntimeException("No more cards");
			Card card = cards.remove(cards.size()-1);
			return card;
		}
	}