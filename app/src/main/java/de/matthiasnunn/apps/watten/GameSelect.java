package de.matthiasnunn.apps.watten;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class GameSelect extends Fragment
{
    private Bundle bundle = new Bundle();


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View view = inflater.inflate( R.layout.game_select, container, false );

        ((Button) view.findViewById(R.id.twoPlayers)).setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                bundle.putInt( "numberOfPlayers", 2 );

                startGameActivity();
            }
        });

        ((Button) view.findViewById(R.id.fourPlayers)).setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                bundle.putInt( "numberOfPlayers", 4 );

                startGameActivity();
            }
        });

        return view;
    }


    private void startGameActivity()
    {
        Intent gameActivity = new Intent( getActivity(), GameActivity.class );
               gameActivity.putExtras( bundle );

        startActivity( gameActivity );
    }
}