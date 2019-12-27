package de.matthiasnunn.apps.watten;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.matthiasnunn.apps.watten.logic.*;
import de.matthiasnunn.apps.watten.logic.player.*;
import de.matthiasnunn.apps.watten.util.GameSerializer;

import java.util.ArrayList;
import java.util.List;


public class GameActivity extends AppCompatActivity implements View.OnClickListener
{
    private Game game;
    private Handler handler;
    private GameSerializer gameSerializer;

    private FrameLayout cutCards;
    private RelativeLayout board;

    private ImageView cuttedCard;
    private ImageView helper;

    private Button striker;
    private Button trump;

    private ImageView player2;
    private ImageView player3;
    private ImageView player4;

    private GridLayout strikerSelect;
    private Button seven;
    private Button eight;
    private Button nine;
    private Button ten;
    private Button unter;
    private Button ober;
    private Button king;
    private Button ass;

    private GridLayout trumpSelect;
    private Button acorn;
    private Button bell;
    private Button heart;
    private Button leave;

    private RelativeLayout played;
    private ImageView played0;
    private ImageView played1;
    private ImageView played2;
    private ImageView played3;

    private Button roundResult;
    private Button gameResult;

    private ImageView playerCard0;
    private ImageView playerCard1;
    private ImageView playerCard2;
    private ImageView playerCard3;
    private ImageView playerCard4;

    private TextView teamOneName;
    private TextView teamTwoName;
    private TextView teamOneStiche;
    private TextView teamTwoStiche;
    private TextView teamOneScore;
    private TextView teamTwoScore;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.game );

        handler = new Handler( getMainLooper() );
        gameSerializer = new GameSerializer( this );

        initComponents();
        adjustImageViewSize();

        final Bundle bundle = getIntent().getExtras();

        new Thread()
        {
            @Override
            public void run()
            {
                if( bundle.getBoolean("continueGame") )
                {
                    initDeserializedGame();
                }
                else
                {
                    initNewGame( bundle.getInt("numberOfPlayers") );
                }

                setNames();
                play();
            }
        }.start();
    }


    private void initComponents()
    {
        cutCards = (FrameLayout) findViewById( R.id.cutCards );

        cuttedCard = (ImageView) findViewById( R.id.cuttedCard );
        helper     = (ImageView) findViewById( R.id.helper );

        board = (RelativeLayout) findViewById( R.id.board );

        striker = (Button) findViewById( R.id.striker );
        trump   = (Button) findViewById( R.id.trump );

        player2 = (ImageView) findViewById( R.id.player2 );
        player3 = (ImageView) findViewById( R.id.player3 );
        player4 = (ImageView) findViewById( R.id.player4 );

        strikerSelect = (GridLayout) findViewById( R.id.strikerSelect );
        seven = (Button) findViewById( R.id.seven );
        eight = (Button) findViewById( R.id.eight );
        nine  = (Button) findViewById( R.id.nine );
        ten   = (Button) findViewById( R.id.ten );
        unter = (Button) findViewById( R.id.unter );
        ober  = (Button) findViewById( R.id.ober );
        king  = (Button) findViewById( R.id.king );
        ass   = (Button) findViewById( R.id.ass );

        trumpSelect = (GridLayout) findViewById( R.id.trumpSelect );
        acorn = (Button) findViewById( R.id.acorn );
        bell  = (Button) findViewById( R.id.bell );
        heart = (Button) findViewById( R.id.heart );
        leave = (Button) findViewById( R.id.leave );

        played  = (RelativeLayout) findViewById( R.id.played );
        played0 = (ImageView) findViewById( R.id.played0 );
        played1 = (ImageView) findViewById( R.id.played1 );
        played2 = (ImageView) findViewById( R.id.played2 );
        played3 = (ImageView) findViewById( R.id.played3 );

        roundResult = (Button) findViewById( R.id.roundResult );
        gameResult  = (Button) findViewById( R.id.gameResult );

        playerCard0 = (ImageView) findViewById( R.id.playerCard0 );
        playerCard1 = (ImageView) findViewById( R.id.playerCard1 );
        playerCard2 = (ImageView) findViewById( R.id.playerCard2 );
        playerCard3 = (ImageView) findViewById( R.id.playerCard3 );
        playerCard4 = (ImageView) findViewById( R.id.playerCard4 );

        teamOneName   = (TextView) findViewById( R.id.teamOneName );
        teamTwoName   = (TextView) findViewById( R.id.teamTwoName );
        teamOneStiche = (TextView) findViewById( R.id.teamOneStiche );
        teamTwoStiche = (TextView) findViewById( R.id.teamTwoStiche );
        teamOneScore  = (TextView) findViewById( R.id.teamOneScore );
        teamTwoScore  = (TextView) findViewById( R.id.teamTwoScore );
    }


    private void adjustImageViewSize()
    {
        board.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                setCorrectCardHeightAndWidth( player2 );
                setCorrectCardHeightAndWidth( player3 );
                setCorrectCardHeightAndWidth( player4 );

                setCorrectLocation( player3 );

                setCorrectCardHeightAndWidth( played0 );
                setCorrectCardHeightAndWidth( played1 );
                setCorrectCardHeightAndWidth( played2 );
                setCorrectCardHeightAndWidth( played3 );
            }

            private void setCorrectCardHeightAndWidth( ImageView imageView )
            {
                imageView.getLayoutParams().width = playerCard0.getWidth();
                imageView.getLayoutParams().height = playerCard0.getHeight();
            }

            private void setCorrectLocation( ImageView imageView )
            {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
                int half = (playerCard0.getHeight() / 2) - playerCard0.getHeight();

                if( imageView == player3 )
                {
                    lp = (RelativeLayout.LayoutParams) player3.getLayoutParams();
                    lp.setMargins( lp.leftMargin, half, lp.rightMargin, lp.bottomMargin );
                }

                imageView.setLayoutParams( lp );
            }
        });
    }


    private void initDeserializedGame()
    {
        game = gameSerializer.getDeserializedGame();

        switch( game.getGameState() )
        {
            case ROUND_INITED:
                showCutCards();
                break;

            case ROUND_END:
                showRoundResult();

            case STICH_END:
            case TURN_PLAYED:
            case PLAY_TURN:
            case STICH_INITED:
            case CARDS_SORTED:
                updatePlayerCards();
                updatePlayedCards();

            case TRUMP_SET:
                showTrump();

            case SET_TRUMP:
                showTrumpSelect();

            case STRIKER_SET:
                hideTrumpSelect();
                showStriker();

            case SET_STRIKER:
                showStrikerSelect();

            case CUT_CARDS_ANIMATION_FINISHED:
                hideStrikerSelect();

                showBoard();
                showEnemyCards();
                showPlayedCards();
                showPlayerCards();
                updateStiche();
                updateScore();
        }
    }


    private void initNewGame( int numberOfPlayers )
    {
        SharedPreferences sharedPreferences = getSharedPreferences( "players", Context.MODE_PRIVATE );
        ArrayList<Player> players = new ArrayList<Player>();

        HumanPlayer player1 = new HumanPlayer();
        player1.setName( sharedPreferences.getString("player1", getResources().getString(R.string.player_one)) );

        ComputerPlayer player2 = new ComputerPlayer();
        player2.setName( sharedPreferences.getString("player2", getResources().getString(R.string.player_two)) );

        players.add( player1 );
        players.add( player2 );

        if( numberOfPlayers == 4 )
        {
            ComputerPlayer player3 = new ComputerPlayer();
            player3.setName( sharedPreferences.getString("player3", getResources().getString(R.string.player_three)) );

            ComputerPlayer player4 = new ComputerPlayer();
            player4.setName( sharedPreferences.getString("player4", getResources().getString(R.string.player_four)) );

            players.add( player3 );
            players.add( player4 );
        }

        game = new Game();
        game.addPlayers( players );
        game.setGameState( GameState.INIT_ROUND );

        showCutCards();
    }


    @Override
    protected void onPause()
    {
        super.onPause();

        if( game.getGameState() == GameState.GAME_END )
        {
            gameSerializer.deleteSerializedGame();
        }
        else
        {
            gameSerializer.serializeGame( game );
        }
    }


    private void play()
    {
        switch( game.play() )
        {
            case ROUND_INITED:

                handler.postDelayed( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        slideOutBoard();
                        slideInCutCards();

                        AnimatorSet as = getCutCardAnimation( game );

                        as.addListener( new Animator.AnimatorListener()
                        {
                            @Override
                            public void onAnimationStart( Animator animator ) { }

                            @Override
                            public void onAnimationEnd( Animator animator )
                            {
                                game.setGameState( GameState.CUT_CARDS_ANIMATION_FINISHED );
                                play();
                            }

                            @Override
                            public void onAnimationCancel( Animator animator ) { }

                            @Override
                            public void onAnimationRepeat( Animator animator ) { }
                        });

                        as.start();
                    }
                }, 2000 );

                break;


            case CUT_CARDS_ANIMATION_FINISHED:

                slideOutCutCards();
                slideInBoard();

                // clean view from previous round
                hideStriker();
                hideTrump();
                hideRoundResult();
                hidePlayedCards();
                updateStiche();

                updateScore();
                showEnemyCards();
                showPlayerCards();

                game.setGameState( GameState.SET_STRIKER );
                play();
                break;


            case SET_STRIKER:

                setOnClickListenerOnStrikerSelect();
                slideInStrikerSelect();
                break;


            case STRIKER_SET:

                handler.post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Animation a = getStrikerSlideInAnimation();

                        a.setAnimationListener( new Animation.AnimationListener()
                        {
                            @Override
                            public void onAnimationStart( Animation animation ) { }

                            @Override
                            public void onAnimationEnd( Animation animation )
                            {
                                game.setGameState( GameState.SET_TRUMP );
                                play();
                            }

                            @Override
                            public void onAnimationRepeat( Animation animation ) { }
                        });

                        a.start();
                    }
                });

                break;


            case SET_TRUMP:

                setOnClickListenerOnTrumpSelect();
                slideInTrumpSelect();
                break;


            case TRUMP_SET:

                handler.post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Animation a = getTrumpSlideInAnimation();

                        a.setAnimationListener( new Animation.AnimationListener()
                        {
                            @Override
                            public void onAnimationStart( Animation animation ) { }

                            @Override
                            public void onAnimationEnd( Animation animation )
                            {
                                game.setGameState( GameState.SORT_CARDS );
                                play();
                            }

                            @Override
                            public void onAnimationRepeat( Animation animation ) { }
                        });

                        a.start();
                    }
                });

                break;


            case CARDS_SORTED:

                updatePlayerCards();
                game.setGameState( GameState.INIT_STICH );
                play();
                break;


            case STICH_INITED:

                game.setGameState( GameState.PLAY_TURN );
                play();
                break;


            case PLAY_TURN:

                setOnClickListenerOnPlayerCards();
                break;


            case TURN_PLAYED:

                updateEnemyCards();
                updatePlayerCards();

                showPlayedCards();
                updatePlayedCards();

                handler.post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        new CountDownTimer( 800, 800 )
                        {
                            @Override
                            public void onTick( long millisUntilFinished ) { }

                            @Override
                            public void onFinish()
                            {
                                game.setGameState( GameState.PLAY_TURN );
                                play();
                            }
                        }.start();
                    }
                });

                break;


            case STICH_END:

                hidePlayedCards();
                updateStiche();
                game.setGameState( GameState.INIT_STICH );
                play();
                break;


            case ROUND_END:

                handler.post( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Animation a = getRoundResultSlideInAnimation();

                        a.setAnimationListener( new Animation.AnimationListener()
                        {
                            @Override
                            public void onAnimationStart( Animation animation ) { }

                            @Override
                            public void onAnimationEnd( Animation animation )
                            {
                                game.setGameState( GameState.INIT_ROUND );
                                play();
                            }

                            @Override
                            public void onAnimationRepeat( Animation animation ) { }
                        });
                    }
                });

                break;


            case GAME_END:

                updateScore();

                new CountDownTimer( 1000, 1000 )
                {
                    @Override
                    public void onTick( long millisUntilFinished ) { }

                    @Override
                    public void onFinish()
                    {
                        slideOutRoundResult();
                        slideInGameResult();
                    }
                }.start();

                break;
        }
    }


    private AnimatorSet getCutCardAnimation( Game game )
    {
        cuttedCard.setImageResource( R.drawable.backside );

        boolean isCritical = game.getCurrentRound().getCuttedCard().isCritical();
        ImageView target = helper;
        int anim;

        if( game.getDealer().getPrevious() == game.getPlayers().get(0) )
        {
            String color = game.getCurrentRound().getCuttedCard().getColor().toString().toLowerCase();
            String value = game.getCurrentRound().getCuttedCard().getValue().toString().toLowerCase();

            int id = getResources().getIdentifier( color+"_"+value, "drawable", getPackageName() );

            cuttedCard.setImageResource( id );

            AnimatorSet as = (AnimatorSet) AnimatorInflater.loadAnimator( this, R.animator.cutted_card_slide_down );
                        as.setTarget( helper );
                        as.start();

            anim   = isCritical ? R.animator.cutted_card_slide_out_bottom : R.animator.cutted_card_slide_to_center;
            target = cuttedCard;
        }
        else if( game.getPlayers().size() == 2 )
        {
            anim = isCritical ? R.animator.cutted_card_slide_out_top : R.animator.cutted_card_slide_up;
        }
        else if( game.getDealer().getPrevious() == game.getPlayers().get(1) )
        {
            anim = isCritical ? R.animator.cutted_card_slide_out_left : R.animator.cutted_card_slide_left;
        }
        else if( game.getDealer().getPrevious() == game.getPlayers().get(2) )
        {
            anim = isCritical ? R.animator.cutted_card_slide_out_top : R.animator.cutted_card_slide_up;
        }
        else
        {
            anim = isCritical ? R.animator.cutted_card_slide_out_right : R.animator.cutted_card_slide_right;
        }

        AnimatorSet as = (AnimatorSet) AnimatorInflater.loadAnimator( this, anim );
        as.setTarget( target );

        return as;
    }


    private void setOnClickListenerOnPlayerCards()
    {
        playerCard0.setOnClickListener( this );
        playerCard1.setOnClickListener( this );
        playerCard2.setOnClickListener( this );
        playerCard3.setOnClickListener( this );
        playerCard4.setOnClickListener( this );
    }


    private void removeOnClickListenerOnPlayerCards()
    {
        playerCard0.setOnClickListener( null );
        playerCard1.setOnClickListener( null );
        playerCard2.setOnClickListener( null );
        playerCard3.setOnClickListener( null );
        playerCard4.setOnClickListener( null );
    }


    private void setOnClickListenerOnStrikerSelect()
    {
        seven.setOnClickListener( this );
        eight.setOnClickListener( this );
        nine.setOnClickListener( this );
        ten.setOnClickListener( this );
        unter.setOnClickListener( this );
        ober.setOnClickListener( this );
        king.setOnClickListener( this );
        ass.setOnClickListener( this );
    }


    private void removeOnClickListenerOnStrikerSelect()
    {
        seven.setOnClickListener( null );
        eight.setOnClickListener( null );
        nine.setOnClickListener( null );
        ten.setOnClickListener( null );
        unter.setOnClickListener( null );
        ober.setOnClickListener( null );
        king.setOnClickListener( null );
        ass.setOnClickListener( null );
    }


    private void setOnClickListenerOnTrumpSelect()
    {
        acorn.setOnClickListener( this );
        bell.setOnClickListener( this );
        heart.setOnClickListener( this );
        leave.setOnClickListener( this );
    }


    private void removeOnClickListenerOnTrumpSelect()
    {
        acorn.setOnClickListener( null );
        bell.setOnClickListener( null );
        heart.setOnClickListener( null );
        leave.setOnClickListener( null );
    }


    @Override
    public void onClick( View v )
    {
        if( v == seven )
        {
            onStrikerSelected( Value.SEVEN );
        }
        else if( v == eight )
        {
            onStrikerSelected( Value.EIGHT );
        }
        else if( v == nine )
        {
            onStrikerSelected( Value.NINE );
        }
        else if( v == ten )
        {
            onStrikerSelected( Value.TEN );
        }
        else if( v == unter )
        {
            onStrikerSelected( Value.UNTER );
        }
        else if( v == ober )
        {
            onStrikerSelected( Value.OBER );
        }
        else if( v == king )
        {
            onStrikerSelected( Value.KING );
        }
        else if( v == ass )
        {
            onStrikerSelected( Value.ASS );
        }

        else if( v == acorn )
        {
            onTrumpSelected( Color.ACORN );
        }
        else if( v == bell )
        {
            onTrumpSelected( Color.BELL );
        }
        else if( v == heart )
        {
            onTrumpSelected( Color.HEART );
        }
        else if( v == leave )
        {
            onTrumpSelected( Color.LEAVE );
        }

        else if( v == playerCard0 )
        {
            onPlayerCardSelected( 0 );
        }
        else if( v == playerCard1 )
        {
            onPlayerCardSelected( 1 );
        }
        else if( v == playerCard2 )
        {
            onPlayerCardSelected( 2 );
        }
        else if( v == playerCard3 )
        {
            onPlayerCardSelected( 3 );
        }
        else if( v == playerCard4 )
        {
            onPlayerCardSelected( 4 );
        }
    }


    private void onStrikerSelected( Value val )
    {
        game.getCurrentRound().setStriker( val );
        game.setGameState( GameState.STRIKER_SET );

        removeOnClickListenerOnStrikerSelect();
        slideOutStrikerSelect();

        play();
    }


    private void onTrumpSelected( Color col )
    {
        game.getCurrentRound().setTrump( col );
        game.setGameState( GameState.TRUMP_SET );

        removeOnClickListenerOnTrumpSelect();
        slideOutTrumpSelect();

        play();
    }


    private void onPlayerCardSelected( int index )
    {
        ((HumanPlayer) game.getPlayers().get(0)).setPlaying( index );

        removeOnClickListenerOnPlayerCards();

        play();
    }


    private void setTextViewText( final TextView v, final String text )
    {
        runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                v.setText( text );
            }
        });
    }


    private void hideView( final View v )
    {
        runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                v.setVisibility( View.INVISIBLE );
            }
        });
    }

    private void showView( final View v )
    {
        runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                v.setVisibility( View.VISIBLE );
            }
        });
    }


    private void slideOutLeft( final View v )
    {
        final Animation a = AnimationUtils.loadAnimation( this, R.anim.slide_out_left );
        v.setAnimation( a );
        hideView( v );
    }

    private void slideOutRight( final View v )
    {
        final Animation a = AnimationUtils.loadAnimation( this, R.anim.slide_out_right );
        v.setAnimation( a );
        hideView( v );
    }

    private Animation getSlideInLeftAnimation()
    {
        return AnimationUtils.loadAnimation( this, R.anim.slide_in_left );
    }

    private void slideInLeft( final View v )
    {
        final Animation a = getSlideInLeftAnimation();
        v.setAnimation( a );
        showView( v );
    }

    private void slideInRight( final View v )
    {
        final Animation a = AnimationUtils.loadAnimation( this, R.anim.slide_in_right );
        v.setAnimation( a );
        showView( v );
    }


    private void showCutCards()
    {
        showView( cutCards );
    }

    private void hideCutCards()
    {
        hideView( cutCards );
    }

    private void slideInCutCards()
    {
        if( cutCards.getVisibility() != View.VISIBLE )
        {
            slideInLeft( cutCards );
        }
    }

    private void slideOutCutCards()
    {
        if( cutCards.getVisibility() == View.VISIBLE )
        {
            slideOutLeft( cutCards );
        }
    }


    private void showBoard()
    {
        showView( board );
    }

    private void hideBoard()
    {
        hideView( board );
    }

    private void slideInBoard()
    {
        if( board.getVisibility() != View.VISIBLE )
        {
            slideInRight( board );
        }
    }

    private void slideOutBoard()
    {
        if( board.getVisibility() == View.VISIBLE )
        {
            slideOutRight( board );
        }
    }


    private void showStriker()
    {
        setTextViewText( striker, game.getCurrentRound().getStriker().toString() );
        showView( striker );
    }

    private void hideStriker()
    {
        hideView( striker );
    }

    private Animation getStrikerSlideInAnimation()
    {
        showStriker();

        Animation a = getSlideInLeftAnimation();
        striker.setAnimation( a );

        return a;
    }


    private void showTrump()
    {
        setTextViewText( trump, game.getCurrentRound().getTrump().toString() );
        showView( trump );
    }

    private void hideTrump()
    {
        hideView( trump );
    }

    private Animation getTrumpSlideInAnimation()
    {
        showTrump();

        Animation a = getSlideInLeftAnimation();
        trump.setAnimation( a );

        return a;
    }


    private void showStrikerSelect()
    {
        showView( strikerSelect );
    }

    private void hideStrikerSelect()
    {
        hideView( strikerSelect );
    }

    private void slideInStrikerSelect()
    {
        if( strikerSelect.getVisibility() != View.VISIBLE )
        {
            slideInLeft( strikerSelect );
        }
    }

    private void slideOutStrikerSelect()
    {
        if( strikerSelect.getVisibility() == View.VISIBLE )
        {
            slideOutRight( strikerSelect );
        }
    }


    private void showTrumpSelect()
    {
        showView( trumpSelect );
    }

    private void hideTrumpSelect()
    {
        hideView( trumpSelect );
    }

    private void slideInTrumpSelect()
    {
        if( trumpSelect.getVisibility() != View.VISIBLE )
        {
            slideInLeft( trumpSelect );
        }
    }

    private void slideOutTrumpSelect()
    {
        if( trumpSelect.getVisibility() == View.VISIBLE )
        {
            slideOutRight( trumpSelect );
        }
    }


    private void setNames()
    {
        if( areTwoPlayers() )
        {
            setTeamOneName( game.getPlayers().get(0).getName() );
            setTeamTwoName( game.getPlayers().get(1).getName() );
        }
        else
        {
            setTeamOneName( game.getPlayers().get(0).getName() + '\n' + game.getPlayers().get(2).getName() );
            setTeamTwoName( game.getPlayers().get(1).getName() + '\n' + game.getPlayers().get(3).getName() );
        }
    }

    private void setTeamOneName( final String name )
    {
        setTextViewText( teamOneName, name );
    }

    private void setTeamTwoName( final String name )
    {
        setTextViewText( teamTwoName, name );
    }


    private void updateScore()
    {
        setScore();
    }

    private void setScore()
    {
        setScoreTeamOne( game.getTeams().get(0).getScore() );
        setScoreTeamTwo( game.getTeams().get(1).getScore() );
    }

    private void setScoreTeamOne( final int score )
    {
        setTextViewText( teamOneScore, String.valueOf(score) );
    }

    private void setScoreTeamTwo( final int score )
    {
        setTextViewText( teamTwoScore, String.valueOf(score) );
    }


    private void updateStiche()
    {
        setStiche();
    }

    private void setStiche()
    {
        if( game.getCurrentStich() != null )
        {
            setSticheTeamOne( game.getTeams().get(0).getStiche() );
            setSticheTeamTwo( game.getTeams().get(1).getStiche() );
        }
        else
        {
            setSticheTeamOne( 0 );
            setSticheTeamTwo( 0 );
        }
    }

    private void setSticheTeamOne( final int score )
    {
        setTextViewText( teamOneStiche, String.valueOf(score) );
    }

    private void setSticheTeamTwo( final int score )
    {
        setTextViewText( teamTwoStiche, String.valueOf(score) );
    }


    private void showEnemyCards()
    {
        setEnemyCards();
    }

    private void updateEnemyCards()
    {
        setEnemyCards();
    }

    private void setEnemyCards()
    {
        if( areTwoPlayers() )
        {
            final int imageResource;

            if( containsAllNull(game.getPlayers().get(1).getCards()) )
            {
                imageResource = R.drawable.placeholder;
            }
            else
            {
                imageResource = R.drawable.backside;
            }

            runOnUiThread( new Runnable()
            {
                @Override
                public void run()
                {
                    ((ImageView) findViewById( R.id.player3 )).setImageResource( imageResource );
                }
            });
        }
        else
        {
            for( int i = 1; i < game.getPlayers().size(); i++ )
            {
                if( ! game.getPlayers().get(i).getCards().isEmpty() )
                {
                    final int id = getResources().getIdentifier( "player"+(i+1), "id", getPackageName() );
                    final int ir;

                    if( containsAllNull(game.getPlayers().get(i).getCards()) )
                    {
                        ir = R.drawable.placeholder;
                    }
                    else
                    {
                        ir = R.drawable.backside;
                    }

                    runOnUiThread( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            ((ImageView) findViewById( id )).setImageResource( ir );
                        }
                    });
                }
            }
        }
    }


    private boolean areTwoPlayers()
    {
        return game.getPlayers().size() == 2;
    }


    private boolean containsAllNull( ArrayList arrayList )
    {
        if( arrayList != null )
        {
            for( int i = 0; i < arrayList.size(); i++ )
            {
                if( arrayList.get(i) != null )
                {
                    return false;
                }
            }
        }

        return true;
    }


    private void showPlayerCards()
    {
        setPlayerCards();
    }

    private void updatePlayerCards()
    {
        setPlayerCards();
    }

    private void setPlayerCards()
    {
        ArrayList<Card> cards = game.getPlayers().get(0).getCards();

        for( int i = 0; i < 5 ; i++ )
        {
            final int id = getResources().getIdentifier( "playerCard" + i, "id", getPackageName() );
            final int imageResource;

            if( cards.get(i) == null )
            {
                imageResource = R.drawable.placeholder;
            }
            else
            {
                imageResource = getResources().getIdentifier( cards.get(i).toString().toLowerCase(), "drawable", getPackageName() );
            }

            runOnUiThread( new Runnable()
            {
                @Override
                public void run()
                {
                    ((ImageView) findViewById( id )).setImageResource( imageResource );
                }
            });
        }
    }


    private void showPlayedCards()
    {
        showView( played );
    }

    private void hidePlayedCards()
    {
        hideView( played );
    }

    private void updatePlayedCards()
    {
        setPlayedCards();
    }

    private void setPlayedCards()
    {
        if( game.getCurrentStich() == null )
        {
            return;
        }

        List<Turn> turns = game.getCurrentStich().getTurns();

        for( int i = 0; i < 4; i++ )
        {
            int imageResource;

            if( i < turns.size() )
            {
                imageResource = getResources().getIdentifier( turns.get(i).getCard().toString(), "drawable", getPackageName() );
            }
            else
            {
                imageResource = R.drawable.placeholder_small;
            }

            final int id = getResources().getIdentifier( "played"+i, "id", getPackageName() );
            final int ir = imageResource;

            runOnUiThread( new Runnable()
            {
                @Override
                public void run()
                {
                    ((ImageView) findViewById( id )).setImageResource( ir );
                }
            });
        }
    }


    private String getRoundResult()
    {
        if( game.getCurrentRound().getWinner() == game.getTeams().get(0) )
        {
            if( areTwoPlayers() )
            {
                return getResources().getString( R.string.round_win );
            }
            else
            {
                return getResources().getString( R.string.round_win_team );
            }
        }
        else
        {
            if( areTwoPlayers() )
            {
                return getResources().getString( R.string.round_lose );
            }
            else
            {
                return getResources().getString( R.string.round_lose_team );
            }
        }
    }

    private void hideRoundResult()
    {
        hideView( roundResult );
    }

    private void showRoundResult()
    {
        setTextViewText( roundResult, getRoundResult() );
        showView( roundResult );
    }

    private Animation getRoundResultSlideInAnimation()
    {
        final Animation animation = getSlideInLeftAnimation();

        roundResult.setText( getRoundResult() );
        roundResult.setVisibility( View.VISIBLE );
        roundResult.setAnimation( animation );

        return animation;
    }

    private void slideOutRoundResult()
    {
        slideOutRight( roundResult );
    }


    private String getGameResult()
    {
        if( game.getWinner() == game.getTeams().get(0) )
        {
            if( areTwoPlayers() )
            {
                return getResources().getString( R.string.game_win );
            }
            else
            {
                return getResources().getString( R.string.game_win_team );
            }
        }
        else
        {
            if( areTwoPlayers() )
            {
                return getResources().getString( R.string.game_lose );
            }
            else
            {
                return getResources().getString( R.string.game_lose_team );
            }
        }
    }

    private void slideInGameResult()
    {
        setTextViewText( gameResult, getGameResult() );
        slideInLeft( gameResult );
    }
}