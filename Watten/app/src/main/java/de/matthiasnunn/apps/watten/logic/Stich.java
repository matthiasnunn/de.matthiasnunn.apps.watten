package de.matthiasnunn.apps.watten.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.matthiasnunn.apps.watten.logic.player.Player;


public class Stich implements Serializable
{
    private List<Turn> turns = new ArrayList<Turn>();
    private Round round;
    private Player beginner;
    private Player currentPlayer;
    private Card firstPlayedCard;
    private boolean trumpOrCritical = false;
    private Card playedCard;

    public Stich( Round round, Player beginner )
    {
        this.round = round;
        this.beginner = beginner;
        this.currentPlayer = beginner;
    }

    public List<Turn> getTurns()
    {
        return turns;
    }

    public void setTurns( List<Turn> turns )
    {
        this.turns = turns;
    }

    public Round getRound()
    {
        return round;
    }

    public void setRound( Round round )
    {
        this.round = round;
    }

    public Player getBeginner()
    {
        return beginner;
    }

    public void setBeginner( Player beginner )
    {
        this.beginner = beginner;
    }

    public Player getCurrentPlayer()
    {
        return currentPlayer;
    }

    public void setCurrentPlayer( Player currentPlayer )
    {
        this.currentPlayer = currentPlayer;
    }

    public Card getFirstPlayedCard()
    {
        return firstPlayedCard;
    }

    public void setFirstPlayedCard( Card firstPlayedCard )
    {
        this.firstPlayedCard = firstPlayedCard;
    }

    public boolean getTrumpOrCritical()
    {
        return trumpOrCritical;
    }

    public void setTrumpOrCritical( boolean trumpOrCritical )
    {
        this.trumpOrCritical = trumpOrCritical;
    }

    public Card getPlayedCard()
    {
        return playedCard;
    }

    public void setPlayedCard( Card playedCard )
    {
        this.playedCard = playedCard;
    }

    private void setCardSum( Card firstPlayedCard )
    {
        if( !firstPlayedCard.isCritical() && !round.isStriker(firstPlayedCard) && !round.isTrump(firstPlayedCard) )
        {
            firstPlayedCard.setSum( firstPlayedCard.getValue().getValue() + 8 );
        }

        for( Player player : round.getGame().getPlayers() )
        {
            for( Card card : player.getCards() )
            {
                if( card != null && card.getColor() == firstPlayedCard.getColor() && !card.isCritical() && !round.isStriker(card) && !round.isTrump(card) )
                {
                    card.setSum( card.getValue().getValue() + 8 );
                }
            }
        }
    }

    public boolean isEnd()
    {
        return turns.size() == round.getGame().getPlayers().size() && turns.get(turns.size()-1).getCard() != null;
    }

    public void play()
    {
        playedCard = currentPlayer.playCard( trumpOrCritical );

        if( playedCard == null )
        {
            return;
        }

        turns.add( new Turn(currentPlayer, playedCard) );

        if( turns.size() == 1 )
        {
            firstPlayedCard = playedCard;

            setCardSum( firstPlayedCard );
        }

        if( turns.size() == 1 && round.getStiche().size() == 1 && round.isHauptschlag(firstPlayedCard) )
        {
            trumpOrCritical = true;
        }

        currentPlayer = currentPlayer.getNext();
    }

    public Player getWinner()
    {
        Turn highest = turns.get(0);

        for( int i = 1; i < turns.size(); i++ )
        {
            if( turns.get(i).getCard().getSum() > highest.getCard().getSum() )
            {
                highest = turns.get(i);
            }
        }

        highest.getPlayer().getTeam().increasingStiche();

        return highest.getPlayer();
    }
}