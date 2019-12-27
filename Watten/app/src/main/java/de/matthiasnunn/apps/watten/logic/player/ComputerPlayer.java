package de.matthiasnunn.apps.watten.logic.player;

import de.matthiasnunn.apps.watten.logic.Card;
import de.matthiasnunn.apps.watten.logic.Color;
import de.matthiasnunn.apps.watten.logic.Turn;
import de.matthiasnunn.apps.watten.logic.Value;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ComputerPlayer extends Player
{
    @Override
    public Card playCard( boolean trumpOrCritical )
    {
        int index;

        if( trumpOrCritical && (hasCritical() || hasTrump()) )
        {
            index = getLowestTrumpOrCritical();
        }
        else
        {
            index = getBestFittingCardIndex();
        }

        Card card = cards.get( index );
        cards.set( index, null );
        return card;
    }

    private int getLowestTrumpOrCritical()
    {
        int index = -1;

        for( Card card : cards )
        {
            if( card.isCritical() || team.getGame().getCurrentRound().isTrump(card) )
            {
                index = cards.indexOf( card );
                break;
            }
        }

        return index;
    }

    private int getBestFittingCardIndex()
    {
        int alreadyPlayedCards = team.getGame().getCurrentStich().getTurns().size();
        int index;

        if( alreadyPlayedCards == 0 )
        {
            // playing highest card
            index = getHighestCardIndex();
        }
        else
        {
            if( alreadyPlayedCards == 1 )
            {
                // check, if already played card is beatable and beat with the lowest beatable card

                Card alreadyPlayed = team.getGame().getCurrentStich().getTurns().get( 0 ).getCard();

                index = getLowestBeatingCardIndex( alreadyPlayed.getSum() );
            }
            else if( alreadyPlayedCards == 2 )
            {
                List<Turn> turns = team.getGame().getCurrentStich().getTurns();

                // check if partner card (0) already beat then lowest card...
                if( turns.get( 0 ).getCard().getSum() > turns.get( 1 ).getCard().getSum() )
                {
                    index = getLowestCardIndex();
                }
                // ...otherwise beat with the lowest beatable
                else
                {
                    index = getLowestBeatingCardIndex( turns.get( 1 ).getCard().getSum() );
                }
            }
            else
            {
                List<Turn> turns = team.getGame().getCurrentStich().getTurns();

                // check if partner card (1) already beat then lowest card...
                if( turns.get( 1 ).getCard().getSum() > turns.get( 0 ).getCard().getSum() && turns.get( 1 ).getCard().getSum() > turns.get( 2 ).getCard().getSum() )
                {
                    index = getLowestCardIndex();
                }
                // ...otherwise beat with the lowest beatable
                else
                {
                    if( turns.get( 0 ).getCard().getSum() > turns.get( 2 ).getCard().getSum() )
                    {
                        index = getLowestBeatingCardIndex( turns.get( 0 ).getCard().getSum() );
                    }
                    else
                    {
                        index = getLowestBeatingCardIndex( turns.get( 2 ).getCard().getSum() );
                    }
                }
            }

            // if no beatable card play lowest card
            if( index == -1 )
            {
                index = getLowestCardIndex();
            }
        }

        return index;
    }

    private int getLowestBeatingCardIndex( int beatable )
    {
        for( int i = 4; i >= 0; i-- )
        {
            if( cards.get(i) != null && cards.get(i).getSum() > beatable )
            {
                return i;
            }
        }

        return -1;
    }

    private int getLowestCardIndex()
    {
        for( int i = 4; i >= 0; i-- )
        {
            if( cards.get(i) != null )
            {
                return i;
            }
        }

        return -1;
    }

    private int getHighestCardIndex()
    {
        for( int i = 0; i <= 4; i++ )
        {
            if( cards.get(i) != null )
            {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Value getStriker()
    {
        Map<Value, Integer> hm = new LinkedHashMap<Value, Integer>();

        for( Value value : Value.values() )  // fill the HashMap with every Value
        {
            hm.put( value, 0 );
        }

        for( Card card : cards )  // count the occurrence of every Value
        {
            if( card.isCritical() )
            {
                continue;
            }

            hm.put( card.getValue(), hm.get(card.getValue())+1 );
        }

        HashMap.Entry<Value, Integer> maxEntry = null;

        for( HashMap.Entry<Value, Integer> entry : hm.entrySet() )  // get the Value with the most occurrence
        {
            if( maxEntry == null || entry.getValue() >= maxEntry.getValue() )
            {
                maxEntry = entry;
            }
        }

        return maxEntry.getKey();
    }

    @Override
    public Color getTrump()
    {
        Map<Color, Integer> hm = new LinkedHashMap<Color, Integer>();

        for( Color color : Color.values() )  // fill the HashMap with every Color
        {
            hm.put( color, 0 );
        }

        for( Card card : cards )  // count the occurrence of every Color
        {
            if( card.isCritical() )
            {
                continue;
            }

            hm.put( card.getColor(), hm.get(card.getColor())+1 );
        }

        HashMap.Entry<Color, Integer> maxEntry = null;

        for( HashMap.Entry<Color, Integer> entry : hm.entrySet() )  // get the Color with the most occurrence
        {
            if( maxEntry == null )
            {
                maxEntry = entry;
            }
            else
            {
                if( entry.getValue() > maxEntry.getValue() )
                {
                    maxEntry = entry;
                }
                else if( entry.getValue() == maxEntry.getValue() )
                {
                    if( getCardSumFromColor(entry.getKey()) > getCardSumFromColor(maxEntry.getKey()) )
                    {
                        maxEntry = entry;
                    }
                }
            }
        }

        return maxEntry.getKey();
    }

    private int getCardSumFromColor( Color color )
    {
        int sum = 0;

        for( Card card : cards )
        {
            if( card.isCritical() )
            {
                continue;
            }
            else if( card.getColor() == color )
            {
                sum += card.getSum();
            }
        }

        return sum;
    }
}