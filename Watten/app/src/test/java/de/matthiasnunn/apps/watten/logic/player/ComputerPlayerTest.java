package de.matthiasnunn.apps.watten.logic.player;

import de.matthiasnunn.apps.watten.logic.Card;
import de.matthiasnunn.apps.watten.logic.Color;
import de.matthiasnunn.apps.watten.logic.Value;

import org.junit.Test;

import static org.junit.Assert.*;


public class ComputerPlayerTest
{
    private ComputerPlayer cp;


    @Test
    public void getStriker()
    {
        cp = new ComputerPlayer();
        cp.setCard( new Card( Value.EIGHT, Color.LEAVE ) );
        cp.setCard( new Card( Value.EIGHT, Color.HEART ) );
        cp.setCard( new Card( Value.NINE,  Color.LEAVE ) );
        cp.setCard( new Card( Value.NINE,  Color.HEART ) );
        cp.setCard( new Card( Value.TEN,   Color.LEAVE ) );
        assertEquals( Value.NINE, cp.getStriker() );

        cp = new ComputerPlayer();
        cp.setCard( new Card( Value.SEVEN, Color.ACORN ) );  // critical
        cp.setCard( new Card( Value.SEVEN, Color.HEART ) );
        cp.setCard( new Card( Value.SEVEN, Color.LEAVE ) );
        cp.setCard( new Card( Value.EIGHT, Color.HEART ) );
        cp.setCard( new Card( Value.EIGHT, Color.LEAVE ) );
        assertEquals( Value.EIGHT, cp.getStriker() );
    }


    @Test
    public void getTrump()
    {
        cp = new ComputerPlayer();
        cp.setCard( new Card( Value.SEVEN, Color.HEART ) );
        cp.setCard( new Card( Value.EIGHT, Color.HEART ) );
        cp.setCard( new Card( Value.NINE,  Color.HEART ) );
        cp.setCard( new Card( Value.TEN,   Color.LEAVE ) );
        cp.setCard( new Card( Value.UNTER, Color.LEAVE ) );
        assertEquals( Color.HEART, cp.getTrump() );

        cp = new ComputerPlayer();
        cp.setCard( new Card( Value.SEVEN, Color.ACORN ) );  // critical
        cp.setCard( new Card( Value.EIGHT, Color.ACORN ) );
        cp.setCard( new Card( Value.NINE,  Color.ACORN ) );
        cp.setCard( new Card( Value.TEN,   Color.BELL  ) );
        cp.setCard( new Card( Value.UNTER, Color.BELL  ) );
        assertEquals( Color.BELL, cp.getTrump() );
    }
}