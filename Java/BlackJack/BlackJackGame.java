public class BlackjackGame {
		public static void main(String[] args) {
			BlackjackGame g = new BlackjackGame("Samuel"); // Samuel is the player's name
			g.play();
		}
		
		// the deck of cards we will use
		private Deck deckOfCards = new Deck();
		// welcome Steve!
		private Player dealer = new Dealer("Steve");
		private Player player = null;
		// we will record the moves in the order they are played
		// this could be useful for future extensions
		private List<Move> moves = new ArrayList<>();
		// the hidden card held by the dealer at the beginning of the game
		private Card hiddenDealerCard = null;
		
		public BlackjackGame(String playerName) {
			player = new BlackjackPlayer(playerName);
		}
		
		public void play() {
			// the first dealer card
			hiddenDealerCard = deckOfCards.removeOneCard();
			
			// give a card to each player
			giveNewCard(dealer);
			giveNewCard(player);
			
			// let the player play as long as he wants and we are not over
			while (player.canPlay() && player.wantToPlay() && !gameEnded()) {
				giveNewCard(player);
			}
			
			// if the player did not get over (and the game ended), let the dealer play
			if (!gameEnded()) {
				// first, turn the hidden card
				giveCard(dealer, hiddenDealerCard);
				// then play until either wins
				while (dealer.canPlay() && !gameEnded()) {
					giveNewCard(dealer);
				}
			}
			
			// show who won
			showGameWinner();
		}
		
		public void giveNewCard(Player p) {
			giveCard(p, deckOfCards.removeOneCard());
		}
		
		public void giveCard(Player p, Card c) {
			Move move = new Move(p, c);
			moves.add(move);
			p.addCard(move.getCard());
			System.out.println(move.toString() + "   (" + p.getTotalPoints() + ")");
		}
		
		public boolean gameEnded() {
			if (player.getTotalPoints() >= 21) {
				return true;
			} else if (dealer.getTotalPoints() >= 21) {
				return true;
			}
			return false;
		}
		
		public void showGameWinner() {
			if (player.getTotalPoints() >= 21) {
				System.out.println(player.getName() + " has lost... " + player.getTotalPoints() + " > 21");
			} else if (dealer.getTotalPoints() >= 21) {
				System.out.println(dealer.getName() + " has lost... " + dealer.getTotalPoints() + " > 21");
			}  else {
				Player winner = (player.getTotalPoints() > dealer.getTotalPoints())?player:dealer;
					System.out.println(winner.getName() + " wins... " + winner.getTotalPoints());
			}
			
		}
	}