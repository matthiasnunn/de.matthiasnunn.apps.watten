package de.matthiasnunn.apps.watten;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace( R.id.main_fragments, new Menu() );
                            transaction.commit();
    }
}