package de.matthiasnunn.apps.watten.logic.player;

import de.matthiasnunn.apps.watten.logic.Card;
import de.matthiasnunn.apps.watten.logic.Color;
import de.matthiasnunn.apps.watten.logic.Value;

public class HumanPlayer extends Player
{
    private Card playing;

    public Card getPlaying()
    {
        return playing;
    }

    public void setPlaying( int index )
    {
        playing = cards.get( index );
    }

    @Override
    public Value getStriker()
    {
        // striker were set manually in controller
        return null;
    }

    @Override
    public Color getTrump()
    {
        // trump were set manually in controller
        return null;
    }

    @Override
    public Card playCard( boolean trumpOrCritical )
    {
        int index = cards.indexOf( playing );

        if( index == -1 )
        {
            return null;
        }
        else if( trumpOrCritical && (hasCritical() || hasTrump()) )
        {
            if( !(cards.get(index).isCritical() || team.getGame().getCurrentRound().isTrump(cards.get(index))) )
            {
                return null;
            }
        }

        cards.set( index, null );

        Card temp = playing;

        playing = null;

        return temp;
    }
}
