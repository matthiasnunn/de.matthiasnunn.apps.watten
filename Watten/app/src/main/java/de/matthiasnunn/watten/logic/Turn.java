package de.matthiasnunn.watten.logic;

import java.io.Serializable;

import de.matthiasnunn.watten.logic.player.Player;


public class Turn implements Serializable
{
    private Player player;
    private Card card;

    public Turn( Player player, Card card )
    {
        this.player = player;
        this.card = card;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer( Player player )
    {
        this.player = player;
    }

    public Card getCard()
    {
        return card;
    }

    public void setCard( Card card )
    {
        this.card = card;
    }
}