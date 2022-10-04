public class BlackjackPlayer extends Player {
		public BlackjackPlayer(String name) {
			super(name);
		}
	
		@Override
		boolean canPlay() {
			return getTotalPoints() < 21;
		}
	
		@Override
		boolean wantToPlay() {
			// here is where the player's strategy could be elaborated. The simple strategy
			// is to keep playing as long as the count is lower than 17.
			return getTotalPoints() < 17;
		}
	}