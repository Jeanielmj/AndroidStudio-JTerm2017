package com.example.demouser.scarne_s_dice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView playerScoreText;
    private TextView computerScoreText;
    private TextView turnScoreText;
    private TextView messageText;
    private ImageView diceView;

    // firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabase;

    private String mUserEmail;

    private ScarnesDiceGame game;
    private PlayerState state;

    private Button rollButton, holdButton, resetButton;
    private static final int MAX_SCORE = 50;

    private Random random;

    public MainActivity() {
        this(new Random());
    }

    public MainActivity(Random random) {
        this.random = random;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if(mFirebaseUser == null){
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }else {
            mUserEmail = mFirebaseUser.getEmail();
            mUserEmail = mUserEmail.substring(0, mUserEmail.indexOf('@'))
                    .replace('.', '_')
                    .replace('#', '_')
                    .replace('$', '_')
                    .replace('[', '_')
                    .replace(']', '_');
            configureDatabase();

//            mUsername = mFirebaseUser.getDisplayName();
//            if(mFirebaseUser.getPhotoUrl()!=null){
//                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
        }

        setContentView(R.layout.activity_main);

        diceView = (ImageView) findViewById(R.id.diceView);
        playerScoreText = (TextView) findViewById(R.id.playerScore);
        computerScoreText = (TextView) findViewById(R.id.computerScore);
        turnScoreText = (TextView) findViewById(R.id.turnScore);
        messageText = (TextView) findViewById(R.id.messageText);


        rollButton = ((Button) findViewById(R.id.rollButton));
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roll();
            }
        });

        holdButton = ((Button) findViewById(R.id.holdButton));
        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hold();
            }
        });

        resetButton = ((Button) findViewById(R.id.resetButton));
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        resetScores();
    }

    public static String sanitizeEmail(String email){
        return email.replace('.', '-').replace('#', '-').replace('$', '-').replace('[', '-').replace(']', '-');
    }

    private void configureDatabase(){
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

        mFirebaseDatabase.child("players").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PlayerState newState = dataSnapshot.getValue(PlayerState.class);


                if(newState.getEmail() != state.getEmail() && newState.getStatus() == PlayerStatus.READY && state.getStatus() == PlayerStatus.READY){
                    newState.setStatus(PlayerStatus.IN_GAME);
                    state.setStatus(PlayerStatus.IN_GAME);

                    mFirebaseDatabase.child("players").child(state.getId()).setValue(state);
                    mFirebaseDatabase.child("players").child(newState.getId()).setValue(newState);

                    startGame(newState);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        state = new PlayerState(mUserEmail);
        mFirebaseDatabase.child("players").push().setValue(state);

    }

    public void startGame(PlayerState newState){
        game = new ScarnesDiceGame(state.getEmail(), newState.getEmail(), random.nextBoolean() ? MultiPlayers.PLAYER1 : MultiPlayers.PLAYER2);

        // mFirebaseDatabase.child("games").push().setValue(game);
        mFirebaseDatabase.child("games").child(game.getId()).setValue(game);


        mFirebaseDatabase.child("games").child(game.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                game = dataSnapshot.getValue(ScarnesDiceGame.class);
                updateGameView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateGameView() {
        playerScoreText.setText(String.format("%d", game.getPlayer1Score()));
        computerScoreText.setText(String.format("%d", game.getPlayer2Score()));
        turnScoreText.setText(String.format("%d", game.getCurrentTurn()));
        int roll = game.getLastRoll();
        if (roll == 1) {
            messageText.setText("Rolled a 1! Changing player!");
        } else if (currentPlayer()) {
            messageText.setText("It is your turn!");
        } else {
            messageText.setText("Waiting for other player");
        }

        if (roll > 0) {
            updateDiceImage(roll);
            checkWinner();
        }
    }

    private boolean currentPlayer() {
        String currentEmail = "";
        switch (game.getCurrentPlayer()) {
            case PLAYER1:
                currentEmail = game.getPlayer1();
                break;
            case PLAYER2:
                currentEmail = game.getPlayer2();
                break;
        }
        return currentEmail.equals(mUserEmail);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /* Roll a die > Add to turn score > If 1, change players */
    public void roll(){
        int diceValue = rollDice();

        if(diceValue == 1){
            diceView.setImageResource(R.drawable.dice1);
            messageText.setText(String.format("Oops! %s threw a 1!", game.getCurrentPlayer()));
            changePlayers();
            game.setCurrentTurn(0);
            turnScoreText.setText("0");
        } else {
            updateDiceImage(diceValue);
            game.setCurrentTurn( game.getCurrentTurn() + diceValue);
            turnScoreText.setText(String.valueOf(game.getCurrentTurn()));
            messageText.setText("Hold or continue rolling?");
            checkWinner();
        }
    }

    private int rollDice(){
        random = new Random();
        int numberRolled = random.nextInt(6) + 1;
        return numberRolled;
    }

    public static final String USER_SCORE = "com.example.demouser.scarne_s_dice.USER_SCORE";

    private void checkWinner() {
        switch (game.getCurrentPlayer()) {
            case PLAYER1:
                if (game.getPlayer1Score() + game.getCurrentTurn() > MAX_SCORE) {
                    diceView.setImageResource(R.drawable.you_won);
                    Intent intent = new Intent(this, WinActivity.class);
                    intent.putExtra(USER_SCORE, String.valueOf(game.getPlayer1Score() + game.getCurrentTurn()));
                    startActivity(intent);
                    break;
                }
            case PLAYER2:
                if (game.getPlayer2Score() + game.getCurrentTurn()  > MAX_SCORE) {
                    diceView.setImageResource(R.drawable.game_over);
                    startActivity(new Intent(this, LoseActivity.class));
                    break;
                }
        }
    }

    /* Add turn score to current player's score > Change players */
    public void hold() {

        switch (game.getCurrentPlayer()) {
            case PLAYER1:
                game.setPlayer1Score( game.getPlayer1Score() + game.getCurrentTurn());
                messageText.setText(String.format("Player 1 scored %d", game.getCurrentTurn()));
                game.setCurrentTurn(0);
                playerScoreText.setText(String.valueOf(game.getPlayer1Score()));
                break;
            case PLAYER2:
                game.setPlayer2Score( game.getPlayer2Score() + game.getCurrentTurn());
                messageText.setText(String.format("Player 2 scored %d", game.getCurrentTurn()));
                game.setCurrentTurn(0);
                computerScoreText.setText(String.valueOf(game.getPlayer2Score()));
                break;
        }

        turnScoreText.setText("0");
        diceView.setImageResource(R.drawable.let_play);
        changePlayers();
    }

    /* Set all scores to 0 */
    public void reset(){
        resetScores();
        diceView.setImageResource(R.drawable.let_play);
        diceView.setContentDescription("Let's play!");
        game.setCurrentPlayer(this.random.nextBoolean() ? MultiPlayers.PLAYER1 : MultiPlayers.PLAYER2);
    }

    private void resetScores(){
        game.setPlayer1Score(0);
        game.setPlayer2Score(0);

        game.setCurrentTurn(0);

        computerScoreText.setText("0");
        playerScoreText.setText("0");
        turnScoreText.setText("0");

        diceView.setImageResource(R.drawable.let_play);
    }

    private void updateDiceImage(int diceValue){
        switch (diceValue) {
            case 1:
                diceView.setImageResource(R.drawable.dice1);
                break;
            case 2:
                diceView.setImageResource(R.drawable.dice2);
                break;

            case 3:
                diceView.setImageResource(R.drawable.dice3);
                break;

            case 4:
                diceView.setImageResource(R.drawable.dice4);
                break;

            case 5:
                diceView.setImageResource(R.drawable.dice5);
                break;

            case 6:
                diceView.setImageResource(R.drawable.dice6);
                break;

            default:
                diceView.setImageResource(R.drawable.error);
                break;
        }
    }

    private void changePlayers(){
        switch (game.getCurrentPlayer()) {
            case PLAYER1:
                game.setCurrentPlayer(MultiPlayers.PLAYER2);
                break;

            case PLAYER2:
                game.setCurrentPlayer(MultiPlayers.PLAYER1);
                break;
        }
    }

//    private void computerTurn() {
//
//        if(computerTurns > 3 && currentTurn/ (0.0001 + computerTurns) >= 3.5){
//            hold();
//        } else{
//            roll();
//            computerTurns ++;
//        }
//    }
//
//    private void computerTurnIn500(){
//
//        timerHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                computerTurn();
//
//                if(whosTurn == Players.COMPUTER){
//                    computerTurnIn500();
//                }
//            }
//        }, 500);
//    }

}


