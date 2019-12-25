package de.matthiasnunn.watten.logic;

import java.io.Serializable;


public class Card implements Serializable
{
    private Value value;
    private Color color;
	private int sum;

    public Card( Value value, Color color )
    {
        this.value = value;
        this.color = color;
        this.sum = value.getValue();
    }

	public Value getValue()
	{
		return value;
	}

    public void setValue( Value value )
	{
		this.value = value;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor( Color color )
	{
		this.color = color;
	}

	public int getSum()
	{
		return sum;
	}
	
	public void setSum( int sum )
	{
		this.sum = sum;
	}

	public boolean isCritical()
	{
		return isMax() || isBello() || isSächer();
	}

	public boolean isMax()
	{
		return getValue() == Value.KING && getColor() == Color.HEART;
	}

	public boolean isBello()
	{
		return getValue() == Value.SEVEN && getColor() == Color.BELL;
	}

	public boolean isSächer()
	{
		return getValue() == Value.SEVEN && getColor() == Color.ACORN;
	}

	@Override
	public String toString()
	{
		return color.name().toLowerCase() + "_" + value.name().toLowerCase();
	}
}