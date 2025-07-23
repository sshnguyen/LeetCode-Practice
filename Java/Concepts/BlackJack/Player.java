public abstract class Player {
		private String name;
		private List<Card> hand = new ArrayList<>();
		abstract boolean canPlay();
		abstract boolean wantToPlay();
		
		public Player(String name) {
			this.name = name;
			
		}
		public String getName() {
			return name;
		}
		
		public int getTotalPoints() {
			// count the points in two ways and select the best for the player 
			int minTotal = 0; // with C_ACE worth 1 point
			int maxTotal = 0; // with C_ACE worth 11 point
			for (Card c: hand) {
				int points = c.getPoints();
				minTotal += points;
				// this would be the count with ACE counting for 11 points
				maxTotal += (c.getPoint() == Point.C_ACE_1)?11:points; 
			}
			// return the most favorable outcome. If considering C_ACE is worth 11 points pushes the 
			// total count beyond 21, return the count where it is worth 1 instead.
			return (maxTotal > 21)?minTotal:maxTotal; 
		}
	
		public void addCard(Card card) {
			hand.add(card);
		}
		
		public String toString() {
			return name;
		}
	}