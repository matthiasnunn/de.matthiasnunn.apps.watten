package de.matthiasnunn.apps.watten;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.matthiasnunn.apps.watten.util.GameSerializer;


public class Menu extends Fragment
{
    private Button newGame;
    private Button continueGame;
    private Button settings;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View view = inflater.inflate( R.layout.menu, container, false );

        newGame = (Button) view.findViewById( R.id.newGame );
        continueGame = (Button) view.findViewById( R.id.continueGame );
        settings = (Button) view.findViewById( R.id.settings );

        setOnClickListenerOnNewGame();
        setOnClickListenerOnSettings();

        return view;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        GameSerializer gameSerializer = new GameSerializer( getActivity() );

        if( gameSerializer.hasSerializedGame() )
        {
            setOnClickListenerOnContinueGame();
            continueGame.setBackgroundColor( getResources().getColor(R.color.colorPrimary ) );
        }
        else
        {
            removeOnClickListenerOnContinueGame();
            continueGame.setBackgroundColor( getResources().getColor(R.color.colorPrimaryBright) );
        }
    }


    private void setOnClickListenerOnNewGame()
    {
        newGame.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations( R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right );
                transaction.replace( R.id.main_fragments, new GameSelect() );
                transaction.addToBackStack( null );
                transaction.commit();
            }
        });
    }


    private void setOnClickListenerOnContinueGame()
    {
        continueGame.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                Bundle bundle = new Bundle();
                bundle.putBoolean( "continueGame", true );

                Intent gameActivity = new Intent( getActivity(), GameActivity.class );
                gameActivity.putExtras( bundle );

                startActivity( gameActivity );
            }
        });
    }


    private void removeOnClickListenerOnContinueGame()
    {
        continueGame.setOnClickListener( null );
    }


    private void setOnClickListenerOnSettings()
    {
        settings.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations( R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right );
                transaction.replace( R.id.main_fragments, new Settings() );
                transaction.addToBackStack( null );
                transaction.commit();
            }
        });
    }
}