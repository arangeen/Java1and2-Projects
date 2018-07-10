package PJ4;
import java.util.*;

/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.freeslots.com/poker.htm
 *
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker. 
 * The dealer uses a 52-card deck, which is played fresh after each playerHand. 
 * The player is dealt one five-card poker playerHand. 
 * After the first draw, which is automatic, you may hold any of the cards and draw 
 * again to replace the cards that you haven't chosen to hold. 
 * Your cards are compared to a table of winning combinations. 
 * The object is to get the best possible combination so that you earn the highest 
 * payout on the bet you placed. 
 *
 * Winning Combinations
 *  
 * 1. One Pair: one pair of the same card
 * 2. Two Pair: two sets of pairs of the same card denomination. 
 * 3. Three of a Kind: three cards of the same denomination. 
 * 4. Straight: five consecutive denomination cards of different suit. 
 * 5. Flush: five non-consecutive denomination cards of the same suit. 
 * 6. Full House: a set of three cards of the same denomination plus 
 * 	a set of two cards of the same denomination. 
 * 7. Four of a kind: four cards of the same denomination. 
 * 8. Straight Flush: five consecutive denomination cards of the same suit. 
 * 9. Royal Flush: five consecutive denomination cards of the same suit, 
 * 	starting from 10 and ending with an ace
 *
 */


/* This is the video poker game class.
 * It uses Decks and Card objects to implement video poker game.
 * Please do not modify any data fields or defined methods
 * You may add new data fields and methods
 * Note: You must implement defined methods
 */



public class VideoPoker {

    // default constant values
    private static final int startingBalance=100;
    private static final int numberOfCards=5;

    // default constant payout value and playerHand types
    private static final int[] multipliers={1,2,3,5,6,10,25,50,1000};
    private static final String[] goodHandTypes={ 
	  "One Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
	  "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

    // must use only one deck
    private final Decks oneDeck;

    // holding current poker 5-card hand, balance, bet    
    private List<Card> playerHand;
    private int playerBalance;
    private int playerBet;
    private HashMap<Integer, Card> playerCardsToRemoveMap;
            
    private Scanner userInput;
    
    boolean showPayoutTable = true;

    /** default constructor, set balance = startingBalance */
    public VideoPoker()
    {
	this(startingBalance);
    }

    /** constructor, set given balance */
    public VideoPoker(int balance)
    {
	this.playerBalance= balance;
        oneDeck = new Decks(1, false);
    }

    /** This display the payout table based on multipliers and goodHandTypes arrays */
    private void showPayoutTable()
    { 
	System.out.println("\n\n");
	System.out.println("Payout Table   	      Multiplier   ");
	System.out.println("=======================================");
	int size = multipliers.length;
	for (int i=size-1; i >= 0; i--) {
		System.out.println(goodHandTypes[i]+"\t|\t"+multipliers[i]);
	}
	System.out.println("\n\n");
    }

    /** Check current playerHand using multipliers and goodHandTypes arrays
     *  Must print yourHandType (default is "Sorry, you lost") at the end of function.
     *  This can be checked by testCheckHands() and main() method.
     */
    private void checkHands()
    {
        // implement this method!
        if (onePair())
            {
                System.out.println("One Pair!");
                playerBalance += multipliers[0]* playerBet;
            }
        else if(twoPair())
            {
                System.out.println("Two Pair!");
                playerBalance += multipliers[1] * playerBet;
            }
        else if(ofAKind(3))
            {
                System.out.println("Three of a Kind!");
                playerBalance += multipliers[2] * playerBet;
            }
        else if(straight())
            {
                System.out.println("Straight!");
                playerBalance += multipliers[3] * playerBet;
            }
        else if(flush())
            {
                System.out.println("Flush!");
                playerBalance += multipliers[4] * playerBet;
            }
        else if(fullHouse())
            {
                System.out.println("Full House!");
                playerBalance += multipliers[5] * playerBet;
            }
        else if(ofAKind(4))
            {
                System.out.println("Four of a Kind!");
                playerBalance += multipliers[6] * playerBet;
            }
        else if(straightFlush())
            {
                System.out.println("Straight Flush!");
                playerBalance += multipliers[7] * playerBet;
            }
        else if(royalFlush())
            {
                System.out.println("Royal Flush!");
                playerBalance += multipliers[8] * playerBet;
            }
        else
            System.out.println("Sorry, you lost");
                
    }

    /*************************************************
     *   add new private methods here ....
     *
     *************************************************/

    private boolean onePair()
    {
        HashMap<Integer, Integer> mapRanking = new HashMap<>();
        int pairCounter = 0;
        
        for(Card card : playerHand)
            {
                if (!mapRanking.containsKey(card.getRank()))
                    {
                        mapRanking.put(card.getRank(), 1);
                        pairCounter = 1;
                    }  
                else{
                        int amount = mapRanking.get(card.getRank());
                        mapRanking.put(card.getRank(), amount+1);
                        pairCounter++;
                    }
            }
        return pairCounter == 2 && mapRanking.containsValue(1);
    }
    
    private boolean twoPair()
    {
        HashMap<Integer, Integer> mapRanking = new HashMap<>();
        int pairCounter = 0;
        
        for(Card card : playerHand)
            {
                if (!mapRanking.containsKey(card.getRank()))
                    {
                        mapRanking.put(card.getRank(), 1);
                        
                    }  
                else{
                        int amount = mapRanking.get(card.getRank());
                        mapRanking.put(card.getRank(), amount+1);
                        pairCounter++;
                    }
            }
        return pairCounter == 2 && mapRanking.containsValue(1);
    }
    
    private boolean ofAKind(int type)
    {
         HashMap<Integer, Integer> mapRanking = new HashMap<>();
         
         for (Card card : playerHand)
            {
                if(!mapRanking.containsKey(card.getRank()))
                    {
                        mapRanking.put(card.getRank(), 1);
                    }
                else{
                        int amount = mapRanking.get(card.getRank());
                        mapRanking.put(card.getRank(), amount+1);
                    }
            }
        return mapRanking.containsValue(type);
    }
    
    private boolean straight()
    {
        List<Integer> cardSuits = new ArrayList<>();
        List<Integer> sortedCardRanks = new ArrayList<>();
        int firstCardSuit = playerHand.get(0).getSuit();
        
        // the for loop will make an int list containing all player ranks.
        for (Card card : playerHand)
            {
                sortedCardRanks.add(card.getRank());
                cardSuits.add(card.getSuit());
            }
        
        // Sorting the ranks
        Collections.sort(sortedCardRanks);
        
        //make sure to see all card suits are not the same
        Set<Integer> suitSet = new HashSet<>(cardSuits);
        
        if(suitSet.size() > cardSuits.size())
            {
                return false;
            }
        
        for(int a = 0; a < 4; a++)
            {
                if(!(sortedCardRanks.get(a)== (sortedCardRanks.get(a+1)-1)))
                {
                    return false;
                }
            }
        
        return true;
    }
    
    private boolean flush()
    {
        int firstCardSuit = playerHand.get(0).getSuit();
        
        // going to see if all the card suits are identical 
        for (Card card : playerHand)
        {
            if (card.getSuit() != firstCardSuit)
                {
                    return false;
                }
        }
        return true;
    }
    
    
    private boolean fullHouse()
    {
         HashMap<Integer, Integer> mapRanking = new HashMap<>();
         
         for (Card card : playerHand)
         {
             if (!mapRanking.containsKey(card.getRank()))
                {
                    mapRanking.put(card.getRank(), 1);
                  
                }
             else{
                    int amount = mapRanking.get(card.getRank());
                    mapRanking.put(card.getRank(), amount+1);
                }
         }
         return mapRanking.containsValue(3) && mapRanking.containsValue(2);
    }
    
    private boolean straightFlush()
    {
        int firstCardSuit = playerHand.get(0).getSuit();
        List<Integer> sortedCardRanks = new ArrayList<>();
        
        // making Int list that will have all player ranks
        for (Card card : playerHand)
        {
            sortedCardRanks.add(card.getRank());
        }
        
        Collections.sort(sortedCardRanks);
        
        for( Card card : playerHand)
        {
            if(card.getSuit() != firstCardSuit)
            {
                return false;
            }
        }
        
        for (int a = 0; a < 4; a++)
        {
            if (!(sortedCardRanks.get(a) == (sortedCardRanks.get(a+1)-1)))
            {
                return false;
            }
        }
        return true;
    }
    
    private boolean royalFlush()
    {
        int firstCardSuit = playerHand.get(0).getSuit();
        
        List<Integer> royalFlushRank = Arrays.asList(1,10,11,12,13);
        
        for(Card card : playerHand)
        {
            if (card.getSuit() != firstCardSuit || !royalFlushRank.contains(card.getRank()))
            {
                return false;
            }
        }
        return true;
    }
    
    private void showBalance()
    {
        System.out.print("\nBalance:$" + playerBalance);
        
    }
    
    private void getBet()
    {
        System.out.print("Enter bet: ");
        
        try{
                playerBet = userInput.nextInt();
                
                if(playerBet > playerBalance)
                {
                    System.out.println("\nBet is larger than balance, try again");
                    getBet();
                }
           }
        catch(InputMismatchException e)
        {
            System.out.println("\nPlease input integers only. Try again.");
            getBet();
        }
    }   
    
    private void updateBalance()
    {
        playerBalance -= playerBet;
    }
    
    private void dealHand()
    {
        try{
            playerHand = oneDeck.deal(numberOfCards);
        }
        catch(PlayingCardException e)
        {
            System.out.println("PlayingCardException: " + e.getMessage());
        }
    }
    
    private void getPlayerCardRemovingPositions()
    {
        System.out.print("Enter positions of cards to replace (e.g. 1 4 5 ): ");
        
        userInput = new Scanner(System.in);
        String input = userInput.nextLine();
        
        if( input.isEmpty())
        {
            return;
        }
        
        String[] positionsToReplace = input.trim().split("\\s+");
        
        try{
                for(int i =0; i < positionsToReplace.length; i++ )
                {
                    int position = Integer.parseInt(positionsToReplace[i])-1;
                    Card card = playerHand.get(position);
                    playerCardsToRemoveMap.remove(position, card);
                }
        }
        catch(Exception e){
            
                System.out.println("\nPlease input integers 1-5 only. Try again");
                getPlayerCardRemovingPositions();
        }
    }
    
    private void settingAndDisplayingNewHand()
    {
        dealHand();
        
        for(Map.Entry<Integer, Card> card : playerCardsToRemoveMap.entrySet())
        {
            playerHand.set(card.getKey(), card.getValue());
        }
        System.out.println(playerHand.toString());
    }
    
    private void leaveGame()
    {
        showBalance();
        System.out.println("\nBye!");
        System.exit(0);
    }
    
    private void checkToPlayNewGame()
    {
        System.out.println("one more game (y or n) ?");
        userInput = new Scanner(System.in);
        
        String input = userInput.nextLine();
        if(input.equals("y"))
        {
            checkIfPlayerWantsCheckoutTable();
            play();
        }
        else if (input.equals("n"))
        {
            leaveGame();
        }
        else
        {
            System.out.println("Please enter (y or n)");
            checkToPlayNewGame();
        }
    }
    
    private void checkIfPlayerWantsCheckoutTable()
    {
        System.out.println("\nWant to see payout table (y or n) ");
        String input = userInput.nextLine();
        
        if(input.equals("n"))
        {
            showPayoutTable = false;
        }
    }
    public void play() 
    {
    /** The main algorithm for single player poker game 
     *
     * Steps:
     * 		showPayoutTable()
     *
     * 
     * 		++	
     * 		show balance, get bet 
     *		verify bet value, update balance
     *		reset deck, shuffle deck, 
     *		deal cards and display cards
     *		ask for positions of cards to replace 
     *          get positions in one input line
     *		update cards
     *		check hands, display proper messages
     *		update balance if there is a payout
     *		if balance = O:
     *			end of program 
     *		else
     *			ask if the player wants to play a new game
     *			if the answer is "no" : end of program
     *			else : showPayoutTable() if user wants to see it
     *			goto ++
     */

        // implement this method!
        
       if(showPayoutTable)
       {
            showPayoutTable();
       }
      System.out.println("\n\n---------------------------------");
      showBalance();
      
      getBet();
        
      updateBalance();
      
      oneDeck.reset();
      oneDeck.shuffle();
      
      dealHand();
      System.out.println(playerHand.toString());
      
      getPlayerCardRemovingPositions();

      settingAndDisplayingNewHand();
      
      checkHands();
      
      showBalance();
      
      if(playerBalance == 0)
      {
          leaveGame();
      }
      else
      {
        checkToPlayNewGame();
      }
    }

    /*************************************************
     *   Do not modify methods below
    /*************************************************

    /** testCheckHands() is used to test checkHands() method 
     *  checkHands() should print your current hand type
     */ 

    public void testCheckHands()
    {
      	try {
    		playerHand = new ArrayList<Card>();

		// set Royal Flush
		playerHand.add(new Card(3,1));
		playerHand.add(new Card(3,10));
		playerHand.add(new Card(3,12));
		playerHand.add(new Card(3,11));
		playerHand.add(new Card(3,13));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight Flush
		playerHand.set(0,new Card(3,9));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Straight
		playerHand.set(4, new Card(1,8));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Flush 
		playerHand.set(4, new Card(3,5));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
	 	// "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

		// set Four of a Kind
		playerHand.clear();
		playerHand.add(new Card(4,8));
		playerHand.add(new Card(1,8));
		playerHand.add(new Card(4,12));
		playerHand.add(new Card(2,8));
		playerHand.add(new Card(3,8));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Three of a Kind
		playerHand.set(4, new Card(4,11));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Full House
		playerHand.set(2, new Card(2,11));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set Two Pairs
		playerHand.set(1, new Card(2,9));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set One Pair
		playerHand.set(0, new Card(2,3));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set One Pair
		playerHand.set(2, new Card(4,3));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");

		// set no Pair
		playerHand.set(2, new Card(4,6));
		System.out.println(playerHand);
    		checkHands();
		System.out.println("-----------------------------------");
      	}
      	catch (Exception e)
      	{
		System.out.println(e.getMessage());
      	}
    }

    /* Quick testCheckHands() */
    public static void main(String args[]) 
    {
	VideoPoker pokergame = new VideoPoker();
	pokergame.testCheckHands();
    }
}
