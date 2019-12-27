package de.matthiasnunn.apps.watten.logic;

import java.io.Serializable;


public enum GameState implements Serializable
{
    INIT_ROUND,
    ROUND_INITED,

    CUT_CARDS_ANIMATION_FINISHED,

    SET_STRIKER,
    STRIKER_SET,

    SET_TRUMP,
    TRUMP_SET,

    SORT_CARDS,
    CARDS_SORTED,

    INIT_STICH,
    STICH_INITED,

    PLAY_TURN,
    TURN_PLAYED,

    STICH_END,

    ROUND_END,

    GAME_END
}