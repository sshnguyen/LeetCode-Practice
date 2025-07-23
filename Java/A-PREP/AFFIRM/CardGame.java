import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

// Part 1
// 2 players, deck of 52 cards [1…52] unique integers
// deal out cards
// each player will take out the first card on the top of their stack of cards
// whichever one has the highest value card wins that round
// they will get those 2 points (one‍‍‍‍‍‍‌‌‌‌‌‍‌‍‍‌‌‍‍ for each card)
// continue until cards run out winner has the highest score
// print out who is the winner + score, loser + score
/*


CLARIFYING QUESTIONS:
1.WHEN DEALING CARD, DO WE FOLLOW THE ORDER OF THE CARD WHEN IT's initialized
OR RANDOM FIRST LIKE SHUFFLE? Collections.shuffle(list)
2.Can there be a tie in card value? how do we handle a tie?
3.ANY ORDER OF THE CARD SHOULD BE PLAY? FOR EXAMPLE FIFO OR LILO?
4.What are the initial input? 2 player names? how should we initialize the initial 52 cards



Thought, create Player class, that store their score and cards dealed and score

Create CardGame class that has function to deal, add player (if only two player then initalize it in the calss)
,  turn function that play 1 turn, if tie play repeat until tie break. function to play the entire game and print winner/lsoer

*/

class Player {
    String name; 
    Stack<Integer> playerCards;
    int score;
    int highestCard;
    
    public Player(String name){
        this.name = name;
        this.playerCards = new Stack<>();
        this.score = 0;
        this.highestCard = Integer.MIN_VALUE;
    }
    
    public void addCard(int card){
        playerCards.push(card);
    }
    
    public String getName(){
        return name;
    }
    
    public void addScore(int points){
        this.score += points;
    }
    
    public int getScore() {
        return score;
    }
    
    public int playCard(){
        //play card on top of the pile
        int cardValue = playerCards.pop();
        highestCard = cardValue > cardValue? cardValue : highestCard;
        return cardValue;
    }
    
    public boolean hasCard(){
        return !playerCards.isEmpty();
    }

}

class CardGame {
    private Player player1;
    private Player player2;
    private List<Integer> deck;
    
    public CardGame(String player1, String player2){
        this.deck = new ArrayList<>();
        for(int i = 1; i <= 52; i++){
            this.deck.add(i);
        }
        this.player1 = new Player(player1);
        this.player2 = new Player(player2);
    }
    
    public void shuffle(){
        //shuffle cards,
         Collections.shuffle(deck);
    }
    
    //deal the card from deck for each player until no more card
    public void deal(){     
        //deal cards
        for (int i = 0; i < deck.size(); i++){
            if (i % 2 == 0){ //player 1 turn
                player1.addCard(deck.get(i));
            } else { // player 2 turn
                player2.addCard(deck.get(i));
            }
        }
    }
    
    public void playGame(){
        int score = 2;
        while (player1.hasCard() && player2.hasCard()){
            //compare card values
            int card1 = player1.playCard();
            int card2 = player2. playCard();
            
            if (card1 > card2){
                player1.addScore(score);
                score = 2; // reset score
            } else if (card2 > card1){
                player2.addScore(score);
                score = 2;
            } else {
                //tie, increase the score for next turn
                score += 2;
            }
        }
        
        
        //DETERMINE WINNER
        int player1Score = player1.getScore();
        int player2Score = player2.getScore();
        if (player1Score > player2Score){ //player 1 wins
            System.out.println("Player " + player1.getName() + " wins with score: " + player1Score);
            System.out.println("Player " + player2.getName() + "loses with score: " + player2Score);
        } else if (player1Score < player2Score) { //player 2 wins
            System.out.println("Player " + player2.getName() + " wins with score: " + player2Score);
            System.out.println("Player " + player1.getName() + "loses with score: " + player1Score);
        } else { //ties
            Player winner = player1.highestCard > player2.highestCard? player1: player2;
            System.out.println("Player " + winner.getName() + " wins the tie breaker with score" + winner.getScore());
        }
    }
}

class CardGamePart2 {
    List<Player> players;
    List<Integer> deck;
    public CardGamePart2(List<String> players, int deckSize){
        //create a list of players from the player's name'
        this.players = new ArrayList<>();
        for (String player : players){
            this.players.add(new Player(player));
        }
        //create a deck of size
        this.deck = new ArrayList<>();
        for (int i = 1; i <= deckSize; i++){
            deck.add(i);
        }
    }
    
    public void shuffle(){
        //shuffle cards,
         Collections.shuffle(deck);
    }
    
    public void deal(){
        
        //deal cards, keeping track of playerIndex
        int playerIndex = 0;
        for (int i = 0; i < deck.size(); i++){
            players.get(playerIndex).addCard(deck.get(i));
            playerIndex = (playerIndex + 1) % players.size();
        }
    }
    
    public void playGame(){
        //ASSUME NO TIE BREAKER - BECAUSE DISTINCT INT CARDS
    
        while (players.stream().anyMatch(Player::hasCard)) {
            int score = 0;
            int maxRoundValue = Integer.MIN_VALUE;
            Player roundWinner = null;
            for (Player player : players){ // loop through all players for each turn
                if (player.hasCard()){
                    score += 1;
                    int playerCard = player.playCard();
                    
                    if (playerCard > maxRoundValue) {
                        maxRoundValue = playerCard;
                        roundWinner = player;
                    }
                }
            }
            roundWinner.addScore(score);
        }
        //SORT THE WINENR BASED ON SCORES
        players.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));
        for (Player player : players) {
            System.out.println(player.getName() + " - Score: " + player.getScore());
        }
    }
}


public class Solution {

    public static void main(String[] args) {
        CardGame game = new CardGame( "Sean", "Ruthi");
        game.shuffle();
        game.deal();
        game.playGame();
    
    
        List<String> players = Arrays.asList("Sean", "Ruthi", "Alex", "Jones");
        CardGamePart2 game2 = new CardGamePart2(players, 100);
        game2.shuffle();
        game2.deal();
        game2.playGame();
    }
}
