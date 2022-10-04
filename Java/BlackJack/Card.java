// perhaps we can also make the card class abstract so we can use it for more games.
// we can make getPoints an abstract methods because cards like face have different
//points value to different game
public class Card {
		public enum Suit {SPADE, CLOVER, DIAMOND, HEART}
		// first 8 count of ordinal+1, others for 10 points.
		public enum Point {C_ACE_1, C_2, C_3, C_4, C_5, C_6, C_7, C_8, C_9, C_10, C_JACK, C_QUEEN, C_KING} 
		public static final int FIRST_SUIT_ORDINAL=10;
	
		private Suit suit;
		private Point point;
		
		public Card(Suit s, Point p) {
			this.point =  p;
			this.suit = s;
		}
	
		public Suit getSuit() {
			return suit;
		}
	
		public Point getPoint() {
			return point;
		}
		
		public int getPoints() {
			// here, we assume that C_ACE counts 1 point. Some extra logic in Player class
			// handles the case where C_ACE may be worth 11
			if (getPoint().ordinal() <= Point.C_10.ordinal()) return getPoint().ordinal()+1;
			else return 10; // suits		
		}
		
		public String toString() {
			return suit.name() + "-" + point.name();
		}
	}