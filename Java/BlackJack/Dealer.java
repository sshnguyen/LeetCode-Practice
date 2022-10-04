public class Dealer extends Player{
		public Dealer(String name) {
			super(name);
		}
	
		@Override
		public boolean canPlay() {
			return getTotalPoints() < 21;
		}
	
		@Override
		public boolean wantToPlay() {
			// dealer will keep playing until either he beats the player or goes over.
			return true;
		}
	}