package com.example.tictactoe;

//Version 0 we use 3 X 3 2-d array of buttons. Place View inside the activity class

//In version 1, we add code to capture a click on any button, identify what button was clicked,
//and we place an X inside the button that was clicked.
//At that point, we are not concerned about playing the game or enforcing its rules.
//We will do that in Version 2

//Version 2: We assume 2 players playing on the same device.
//Enforce all rules like each playing only have one turn each and game overs.
//In order to enable play, TicTacToe object is added as an instance variable of our Activity class and call those methods when needed
//Play happens in update method

//V3 improve V2 by showing state of game at bottom

//V4 allow players to play another game
//Exit activity if player answers no

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TicTacToe game;
    private ButtonGridAndTextView tttView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game = new TicTacToe();
        Point size = new Point();
        getDisplay().getRealSize(size);
        int w = size.x / TicTacToe.SIDE;
        ButtonHandler bh = new ButtonHandler();
        tttView = new ButtonGridAndTextView(this, w, TicTacToe.SIDE, bh);
        tttView.setStatusText(game.result());
        setContentView(tttView);
    }


    private class ButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.w("MainActivity", "Inside onClick, v=" + v);
            for (int row = 0; row < TicTacToe.SIDE; row++)
                for (int column = 0; column < TicTacToe.SIDE; column++)
                    if (tttView.isButton((Button) v, row, column)) {
                        int play = game.play(row, column);
                        if (play == 1)
                            tttView.setButtonText(row, column, "X");
                        else if (play == 2)
                            tttView.setButtonText(row, column, "O");
                        if (game.isGameOver()) {
                            tttView.setStatusBackgroundColor(Color.RED);
                            tttView.enableButtons(false);
                            tttView.setStatusText(game.result());
                            showNewGameDialog();    // offer to play again
                        }
                    }
        }

    }


    private void showNewGameDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("This is fun");
        alert.setMessage("Play again?");
        PlayDialog playAgain = new PlayDialog();
        alert.setPositiveButton("YES", playAgain);
        alert.setNegativeButton("NO", playAgain);
        alert.show();
    }

    private class PlayDialog implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int id) {
            //yes button
            if (id == -1) {
                game.resetGame();
                tttView.enableButtons(true);
                tttView.resetButtons();
                tttView.setStatusBackgroundColor(Color.GREEN);
                tttView.setStatusText(game.result());
            }
            //no button
            else if (id == -2)
                MainActivity.this.finish();
        }

    }
}
