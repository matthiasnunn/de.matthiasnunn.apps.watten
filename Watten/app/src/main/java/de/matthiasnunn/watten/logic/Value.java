package de.matthiasnunn.watten.logic;

import java.io.Serializable;


public enum Value implements Serializable
{
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    UNTER(11),
    OBER(12),
    KING(13),
    ASS(14);
	
	private int value;
	
	private Value( int value )
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
}