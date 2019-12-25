package de.matthiasnunn.watten.logic;

import java.io.Serializable;


public class Team implements Serializable
{
    private Game game;
    private int score = 0;
    private int stiche = 0;

    public Team( Game game )
    {
        this.game = game;
    }

    public Game getGame()
    {
        return game;
    }

    public void setGame( Game game )
    {
        this.game = game;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore( int score )
    {
        this.score = score;
    }

    public int getStiche()
    {
        return stiche;
    }

    public void setStiche( int stiche )
    {
        this.stiche = stiche;
    }

    public void increasingScore()
    {
        increasingScore( 2 );
    }

	public void increasingScore( int increase )
	{
		score += increase;
	}

    public void increasingStiche()
    {
        stiche++;
    }

    public void resetStiche()
    {
        stiche = 0;
    }
}