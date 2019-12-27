package de.matthiasnunn.apps.watten.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.matthiasnunn.apps.watten.logic.Game;


public class GameSerializer
{
    private static final String FILE = "game.json";

    private Context context;


    public GameSerializer( Context context )
    {
        this.context = context;
    }


    public boolean hasSerializedGame()
    {
        try
        {
            context.openFileInput( FILE );
        }
        catch( FileNotFoundException e )
        {
            return false;
        }

        return true;
    }


    public Game getDeserializedGame()
    {
        Game game = null;

        try
        {
            FileInputStream fis = context.openFileInput( FILE );
            ObjectInputStream ois = new ObjectInputStream( fis );

            game = (Game) ois.readObject();

            ois.close();
            fis.close();
        }
        catch( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        catch ( ClassNotFoundException e )
        {
            e.printStackTrace();
        }

        return game;
    }


    public void serializeGame( Game game )
    {
        try
        {
            FileOutputStream fos = context.openFileOutput( FILE, Context.MODE_PRIVATE );

            ObjectOutputStream oos = new ObjectOutputStream( fos );
                               oos.writeObject( game );
                               oos.close();

            fos.close();
        }
        catch( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }


    public void deleteSerializedGame()
    {
        context.deleteFile( FILE );
    }
}