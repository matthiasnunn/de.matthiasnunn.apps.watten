package de.matthiasnunn.watten.logic;

import de.matthiasnunn.watten.logic.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;


public class Round implements Serializable
{
    private Game game;
    private ArrayList<Card> cards = new ArrayList<Card>();
    private Card cuttedCard;
	private Value striker;
	private Color trump;
	private ArrayList<Stich> stiche = new ArrayList<Stich>();
    private Player currentPlayer;
    private Stich currentStich;
    private Team winner;

    public Round( Game game, Player dealer )
    {
        this.game = game;
        this.currentPlayer = dealer;
    }

    public Game getGame()
    {
        return game;
    }

    public void setGame( Game game )
    {
        this.game = game;
    }

    public ArrayList<Card> getCards()
    {
        return cards;
    }

    public void setCards( ArrayList<Card> cards )
    {
        this.cards = cards;
    }

    public Card getCuttedCard()
    {
        return cuttedCard;
    }

    public void setCuttedCard( Card cuttedCard )
    {
        this.cuttedCard = cuttedCard;
    }

    public Value getStriker()
    {
        return striker;
    }

    public void setStriker( Value striker )
    {
        this.striker = striker;
    }

    public Color getTrump()
    {
        return trump;
    }

    public void setTrump( Color trump )
    {
        this.trump = trump;
    }

    public ArrayList<Stich> getStiche()
    {
        return stiche;
    }

    public void setStiche( ArrayList<Stich> stiche )
    {
        this.stiche = stiche;
    }

    public Player getCurrentPlayer()
    {
        return currentPlayer;
    }

    public void setCurrentPlayer( Player currentPlayer )
    {
        this.currentPlayer = currentPlayer;
    }

    public Stich getCurrentStich()
    {
        return currentStich;
    }

    public void setCurrentStich( Stich currentStich )
    {
        this.currentStich = currentStich;
    }

    public Team getWinner()
    {
        return winner;
    }

    public void setWinner( Team winner )
    {
        this.winner = winner;
    }

    public void initCards()
    {
        for( Value val : Value.values() )
        {
            for( Color col : Color.values() )
            {
                cards.add( new Card(val, col) );
            }
        }
    }

    public void shuffleCards()
    {
        Collections.shuffle( cards );
    }

    public void cutCards()
    {
        Player player = currentPlayer.getPrevious();
        cuttedCard = cards.get( new Random().nextInt(cards.size()) );
		
        if( cuttedCard.isCritical() )
        {
            cards.remove( cuttedCard );
            player.setCard( cuttedCard );
        }
    }
    
    public void handOutCards()
    {
        for( int i = 0; i < 5; i++ )
        {
            for( Player player : game.getPlayers() )
            {
                if( player.setCard(cards.get(0)) )
                {
                    cards.remove( 0 );
                }
            }
        }
    }

    public void setCardSum()
    {
        for( Player player : game.getPlayers() )
        {
            for( Card card : player.getCards() )
            {
                if( card == null )
                {
                    continue;
                }
                else if( card.isMax() )
                {
                    card.setSum( 42 );
                }
                else if( card.isBello() )
                {
                    card.setSum( 41 );
                }
                else if( card.isSächer() )
                {
                    card.setSum( 40 );
                }
                else if( isHauptschlag(card) )
                {
                    card.setSum( 39 );
                }
                else if( isStriker(card) )
                {
                    card.setSum( card.getValue().getValue() + 24 );
                }
                else if( isTrump(card) )
                {
                    card.setSum( card.getValue().getValue() + 16 );
                }
                else
                {
                    card.setSum( card.getValue().getValue() );
                }
            }
        }
    }

    public boolean isEnd()
    {
        if( game.getTeams().get(0).getStiche() > 2 )
        {
            winner = game.getTeams().get(0);
            game.getTeams().get(0).increasingScore();
            resetSticheCounter();

            return true;
        }
        else if( game.getTeams().get(1).getStiche() > 2 )
        {
            winner = game.getTeams().get(1);
            game.getTeams().get(1).increasingScore();
            resetSticheCounter();

            return true;
        }

        return false;
    }

    public void initStich()
    {
        currentStich = new Stich( this, currentPlayer );
        stiche.add( currentStich );
        setCardSum();
    }

    public void finishStich()
    {
        currentPlayer = currentStich.getWinner();
    }

    private void resetSticheCounter()
    {
        game.getTeams().get(0).resetStiche();
        game.getTeams().get(1).resetStiche();
    }

    public void sortPlayerCards()
    {
        for( Player player : game.getPlayers() )
        {
            Collections.sort( player.getCards(), new Comparator<Card>()
            {
                @Override
                public int compare( Card card, Card t1 )
                {
                    return t1.getSum() - card.getSum();
                }
            });
        }
    }

	public boolean isStriker( Card card )
    {
        return card.getValue() == striker;
    }

    public boolean isTrump( Card card )
    {
        return card.getColor() == trump;
    }

    public boolean isHauptschlag( Card card )
    {
        return isTrump(card) && isStriker(card);
    }
}