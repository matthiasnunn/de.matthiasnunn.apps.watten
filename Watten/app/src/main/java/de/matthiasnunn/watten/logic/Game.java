package de.matthiasnunn.watten.logic;

import de.matthiasnunn.watten.logic.player.*;

import java.io.Serializable;
import java.util.ArrayList;


public class Game implements Serializable
{
    private GameState gameState;
    private ArrayList<Player> players;
    private ArrayList<Team> teams = new ArrayList<Team>();
    private ArrayList<Round> rounds = new ArrayList<Round>();
    private Player dealer;
    private Round currentRound;
    private Team winner;

    public GameState getGameState()
    {
        return gameState;
    }

    public void setGameState( GameState gameState )
    {
        this.gameState = gameState;
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public void setPlayers( ArrayList<Player> players )
    {
        this.players = players;
    }

    public ArrayList<Team> getTeams()
    {
        return teams;
    }

    public void setTeams( ArrayList<Team> teams )
    {
        this.teams = teams;
    }

    public ArrayList<Round> getRounds()
    {
        return rounds;
    }

    public void setRounds( ArrayList<Round> rounds )
    {
        this.rounds = rounds;
    }

    public Player getDealer()
    {
        return dealer;
    }

    public void setDealer( Player dealer )
    {
        this.dealer = dealer;
    }

    public Round getCurrentRound()
    {
        return currentRound;
    }

    public void setCurrentRound( Round currentRound )
    {
        this.currentRound = currentRound;
    }

    public Stich getCurrentStich()
    {
        return currentRound.getCurrentStich();
    }

    public void setWinner( Team winner )
    {
        this.winner = winner;
    }

    public Team getWinner()
    {
        return winner;
    }

    public void addPlayers( ArrayList<Player> players )
    {
        this.players = players;

        initPlayers();
    }

    private void initPlayers()
    {
        teams.add( new Team(this) );
        teams.add( new Team(this) );

        for( int i = 0; i < players.size(); i += 2 )  // Add Player1 and (Player3) to Team1
        {
            players.get(i).setTeam( teams.get(0) );
        }

        for( int i = 1; i < players.size(); i += 2 )  // Add Player2 and (Player4) to Team2
        {
            players.get(i).setTeam( teams.get(1) );
        }

        players.get(0).setNext( players.get(1) );
        players.get(1).setPrevious( players.get(0) );
        players.get(0).setPrevious( players.get(1) );  // get overwritten if 4 Players
        players.get(1).setNext( players.get(0) );      // get overwritten if 4 Players

        if( players.size() == 4 )
        {
            players.get(0).setPrevious( players.get(3) );
            players.get(1).setNext( players.get(2) );
            players.get(2).setPrevious( players.get(1) );
            players.get(2).setNext( players.get(3) );
            players.get(3).setPrevious( players.get(2) );
            players.get(3).setNext( players.get(0) );
        }

        dealer = players.get(1);
    }

    private boolean isEnd()
    {
        if( teams.get(0).getScore() >= 11 )
        {
            winner = teams.get(0);
            return true;
        }
        else if( teams.get(1).getScore() >= 11 )
        {
            winner = teams.get(1);
            return true;
        }

        return false;
    }

    private void createRound()
    {
        currentRound = new Round( this, dealer );
        rounds.add( currentRound );
    }

    private void initRound()
    {
        currentRound.initCards();
        currentRound.shuffleCards();
        currentRound.cutCards();
        currentRound.handOutCards();
    }

    private void resetPlayerCards()
    {
        for( Player player : players )
        {
            player.setCards( new ArrayList<Card>() );
        }
    }

    public GameState play()
    {
        switch( gameState )
        {
            case INIT_ROUND:

                if( isEnd() )
                {
                    return gameState = GameState.GAME_END;
                }

                createRound();
                initRound();

                return gameState = GameState.ROUND_INITED;


            case SET_STRIKER:

                currentRound.setStriker( currentRound.getCurrentPlayer().getNext().getStriker() );

                if( currentRound.getStriker() != null )
                {
                    return gameState = GameState.STRIKER_SET;
                }

                break;


            case SET_TRUMP:

                currentRound.setTrump( currentRound.getCurrentPlayer().getTrump() );

                if( currentRound.getTrump() != null )
                {
                    return gameState = GameState.TRUMP_SET;
                }

                break;


            case SORT_CARDS:

                currentRound.setCardSum();
                currentRound.sortPlayerCards();

                return gameState = GameState.CARDS_SORTED;


            case INIT_STICH:

                if( currentRound.isEnd() )
                {
                    resetPlayerCards();

                    dealer = dealer.getNext();

                    return gameState = GameState.ROUND_END;
                }

                currentRound.initStich();

                return gameState = GameState.STICH_INITED;


            case PLAY_TURN:

                if( currentRound.getCurrentStich().isEnd() )
                {
                    currentRound.finishStich();

                    return gameState = GameState.STICH_END;
                }

                currentRound.getCurrentStich().play();

                if( currentRound.getCurrentStich().getPlayedCard() != null )
                {
                    return gameState = GameState.TURN_PLAYED;
                }

                break;
        }

        return gameState;
    }
}