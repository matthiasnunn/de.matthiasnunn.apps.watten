package de.matthiasnunn.watten;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class Settings extends Fragment
{
    private SharedPreferences sharedPreferences;

    private EditText player1Name;
    private EditText player2Name;
    private EditText player3Name;
    private EditText player4Name;
    private Button  saveSettings;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        View view = inflater.inflate( R.layout.settings, container, false );

        sharedPreferences = getActivity().getSharedPreferences( "players", Context.MODE_PRIVATE );

        player1Name  = (EditText) view.findViewById( R.id.player1Name  );
        player2Name  = (EditText) view.findViewById( R.id.player2Name  );
        player3Name  = (EditText) view.findViewById( R.id.player3Name  );
        player4Name  = (EditText) view.findViewById( R.id.player4Name  );
        saveSettings = (Button)   view.findViewById( R.id.saveSettings );

        player1Name.setText( sharedPreferences.getString("player1", getResources().getString(R.string.player_one)) );
        player2Name.setText( sharedPreferences.getString("player2", getResources().getString(R.string.player_two)) );
        player3Name.setText( sharedPreferences.getString("player3", getResources().getString(R.string.player_three)) );
        player4Name.setText( sharedPreferences.getString("player4", getResources().getString(R.string.player_four)) );

        saveSettings.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString( "player1", player1Name.getText().toString() );
                editor.putString( "player2", player2Name.getText().toString() );
                editor.putString( "player3", player3Name.getText().toString() );
                editor.putString( "player4", player4Name.getText().toString() );

                editor.apply();

                getFragmentManager().popBackStack();  // back to menu
            }
        });

        return view;
    }
}
