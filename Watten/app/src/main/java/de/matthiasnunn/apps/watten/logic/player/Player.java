package de.matthiasnunn.apps.watten.logic.player;

import de.matthiasnunn.apps.watten.logic.Card;
import de.matthiasnunn.apps.watten.logic.Color;
import de.matthiasnunn.apps.watten.util.DoublyCircularLinkedList;
import de.matthiasnunn.apps.watten.logic.Team;
import de.matthiasnunn.apps.watten.logic.Value;

import java.io.Serializable;
import java.util.ArrayList;


public abstract class Player implements DoublyCircularLinkedList<Player>, Serializable
{
    protected ArrayList<Card> cards = new ArrayList<Card>();
    protected Team team;
    protected Player previous;
    protected Player next;
	protected String name;

	public abstract Value getStriker();
	public abstract Color getTrump();
    public abstract Card playCard( boolean trumpOrCritical );

	public ArrayList<Card> getCards()
	{
		return cards;
	}

	public void setCards( ArrayList<Card> cards )
	{
		this.cards = cards;
	}

	public Team getTeam()
	{
		return team;
	}

	public void setTeam( Team team )
	{
		this.team = team;
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	@Override
	public Player getPrevious()
	{
		return previous;
	}

	@Override
	public void setPrevious( Player player )
	{
		previous = player;
	}

	@Override
	public Player getNext()
	{
		return next;
	}

	@Override
	public void setNext( Player player )
	{
		next = player;
	}

	protected boolean hasCritical()
	{
		for( Card card : cards )
		{
			if( card.isCritical() )
			{
				return true;
			}
		}

		return false;
	}

	protected boolean hasTrump()
	{
		for( Card card : cards )
		{
			if( card.getColor() == team.getGame().getCurrentRound().getTrump() )
			{
				return true;
			}
		}

		return false;
	}

	public boolean setCard( Card card )
	{
		if( cards.size() < 5 )
		{
			cards.add( card );
			return true;
		}

		return false;
	}
}