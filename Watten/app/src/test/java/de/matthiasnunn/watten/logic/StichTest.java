package de.matthiasnunn.watten.logic;

import org.junit.Test;

import java.util.ArrayList;

import de.matthiasnunn.watten.logic.player.ComputerPlayer;
import de.matthiasnunn.watten.logic.player.Player;

import static org.junit.Assert.assertEquals;


public class StichTest
{
    @Test
    public void play()
    {
        ComputerPlayer cp1 = new ComputerPlayer();
                       cp1.setCard( new Card(Value.SEVEN, Color.ACORN) );
                       cp1.setCard( new Card(Value.EIGHT, Color.ACORN) );
                       cp1.setCard( new Card(Value.NINE,  Color.ACORN) );
                       cp1.setCard( new Card(Value.TEN,   Color.ACORN) );
                       cp1.setCard( new Card(Value.OBER,  Color.ACORN) );

        ComputerPlayer cp2 = new ComputerPlayer();
                       cp2.setCard( new Card(Value.SEVEN, Color.BELL) );
                       cp2.setCard( new Card(Value.EIGHT, Color.BELL) );
                       cp2.setCard( new Card(Value.NINE,  Color.BELL) );
                       cp2.setCard( new Card(Value.TEN,   Color.BELL) );
                       cp2.setCard( new Card(Value.UNTER, Color.BELL) );

        ArrayList<Player> players = new ArrayList<Player>();
                          players.add( cp1 );
                          players.add( cp2 );

        Game game = new Game();
             game.addPlayers( players );

        // create and init Round
        game.setGameState( GameState.INIT_ROUND );
        game.play();

        game.getCurrentRound().setStriker( Value.KING );
        game.getCurrentRound().setTrump( Color.HEART );

        // setCardSum and sortPlayerCards
        game.setGameState( GameState.SORT_CARDS );
        game.play();

        /*
         *  Stich 1
         */

        // initStich
        game.setGameState( GameState.INIT_STICH );
        game.play();

        game.setGameState( GameState.PLAY_TURN );
        game.play();

        assertEquals( Value.SEVEN, game.getCurrentStich().getTurns().get(0).getCard().getValue() );  // cp2
        assertEquals( Color.BELL,  game.getCurrentStich().getTurns().get(0).getCard().getColor() );

        game.setGameState( GameState.PLAY_TURN );
        game.play();

        assertEquals( Value.EIGHT, game.getCurrentStich().getTurns().get(1).getCard().getValue() );  // cp1
        assertEquals( Color.ACORN, game.getCurrentStich().getTurns().get(1).getCard().getColor() );

        // finishStich
        game.play();

        /*
         *  Stich 2
         */

        // initStich
        game.setGameState( GameState.INIT_STICH );
        game.play();

        game.setGameState( GameState.PLAY_TURN );
        game.play();

        assertEquals( Value.UNTER, game.getCurrentStich().getTurns().get(0).getCard().getValue() );  // cp2
        assertEquals( Color.BELL,  game.getCurrentStich().getTurns().get(0).getCard().getColor() );

        game.setGameState( GameState.PLAY_TURN );
        game.play();

        assertEquals( Value.SEVEN, game.getCurrentStich().getTurns().get(1).getCard().getValue() );  // cp1
        assertEquals( Color.ACORN, game.getCurrentStich().getTurns().get(1).getCard().getColor() );

        // finishStich
        game.play();
    }
}