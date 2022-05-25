package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openChooseMode(View view) {
        setContentView(R.layout.choose_mode);
    }

    public void openMainMenu(View view) {
        setContentView(R.layout.activity_main);
    }


    // AWARENESS GAME MODE
    private class ColorLabel {
        String name;
        int color;

        ColorLabel(String name, int color) {
            this.name = name;
            this.color = color;
        }
    }
    boolean playable = true;
    int interval = 5000;
    public void openAwarenessMode(View view) {
        setContentView(R.layout.awareness_mode);
        showColors();
        score = 0;
        TextView scoreText = (TextView)findViewById(R.id.textView2);
        scoreText.setText("Score: " + score);
        playable = true;
        interval = 5000;
    }
    int expectedColor;
    int score = 0;
    HashMap<Timer, Integer> state = new HashMap<Timer, Integer>();

    HashMap<Integer, Integer>givenColors = new HashMap<Integer, Integer>();
    public void showColors() {
        Button btn1 = (Button)findViewById(R.id.button9);
        Button btn2 = (Button)findViewById(R.id.button10);
        Button btn3 = (Button)findViewById(R.id.button11);
        Button btn4 = (Button)findViewById(R.id.button12);
        Button btn5 = (Button)findViewById(R.id.button13);
        Button btn6 = (Button)findViewById(R.id.button14);
        Button btn7 = (Button)findViewById(R.id.button15);


        List<ColorLabel> colors = new ArrayList<>();
        colors.add(new ColorLabel("Red", Color.RED));
        colors.add(new ColorLabel("Green", Color.GREEN));
        colors.add(new ColorLabel("Blue", Color.BLUE));
        colors.add(new ColorLabel("Yellow", Color.YELLOW));
        colors.add(new ColorLabel("Black", Color.BLACK));
        colors.add(new ColorLabel("Gray", Color.GRAY));

        Collections.shuffle(colors);

        btn1.setBackgroundColor(colors.get(0).color);
        btn2.setBackgroundColor(colors.get(1).color);
        btn3.setBackgroundColor(colors.get(2).color);
        btn4.setBackgroundColor(colors.get(3).color);
        btn5.setBackgroundColor(colors.get(4).color);
        btn6.setBackgroundColor(colors.get(5).color);

        givenColors.put(btn1.getId(), colors.get(0).color);
        givenColors.put(btn2.getId(), colors.get(1).color);
        givenColors.put(btn3.getId(), colors.get(2).color);
        givenColors.put(btn4.getId(), colors.get(3).color);
        givenColors.put(btn5.getId(), colors.get(4).color);
        givenColors.put(btn6.getId(), colors.get(5).color);


        int randomNumber = (int) Math.round(Math.random() * 5);
        int confusionColor = colors.get(randomNumber).color;
        randomNumber = (int) Math.round(Math.random() * 5);
        String expectedColorName = colors.get(randomNumber).name;
        expectedColor = colors.get(randomNumber).color;

        btn7.setBackgroundColor(confusionColor);
        btn7.setText(expectedColorName);

        Timer timer = new Timer();
        state.put(timer, score);
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if(state.get(timer) == score) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                ((TextView)findViewById(R.id.textView3)).setText("Time's up!");
                                playable = false;
                            } catch (Exception e) {

                            }
                        }
                    });
                }
            }
        }, interval);
    }

    public void chooseColor(View view) {
        if(!playable) return;
        if(interval > 2000) interval -= 250;

        TextView scoreText = (TextView)findViewById(R.id.textView2);
        int ChosenColor = givenColors.get(view.getId());

        if(ChosenColor == expectedColor) {
            score++;
            if(score > leaderBoard[0]) leaderBoard[0] = score;
            scoreText.setText("Score: " + score);
            showColors();
        } else {
            ((TextView)findViewById(R.id.textView3)).setText("Wrong!");
            playable = false;
        }
    }



    // MEMORY GAME MODE

    int level = 0;
    public void openMemoryMode(View view) {
        setContentView(R.layout.memory_mode);
        (findViewById(R.id.editTextMemory)).requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput((findViewById(R.id.editTextMemory)), InputMethodManager.SHOW_IMPLICIT);
        numbers = "";
        level = 0;
        showNumber();
        ((EditText)findViewById(R.id.editTextMemory)).addTextChangedListener(editTextMemoryWatcher);
    }

    String numbers = "";
    public void showNumber() {

        int number = (int) Math.round(Math.random() * 9);
        ((TextView)findViewById(R.id.textView7)).setText(number + "");

        numbers += number;
        level++;
        ((TextView)findViewById(R.id.textView5)).setText("Level: " + level);
        if(level > leaderBoard[1]) leaderBoard[1] = level;

        Timer timer = new Timer();
        state.put(timer, score);
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                ((TextView)findViewById(R.id.textView7)).setText("");
                            } catch (Exception e) {

                            }
                        }
                    });
            }
        }, 1500);
    }

    private TextWatcher editTextMemoryWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String input = ((EditText)findViewById(R.id.editTextMemory)).getText().toString();
            if(input.equalsIgnoreCase(numbers)) {
                Timer timer = new Timer();
                state.put(timer, score);
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        ((EditText)findViewById(R.id.editTextMemory)).removeTextChangedListener(editTextMemoryWatcher);
                                        ((EditText)findViewById(R.id.editTextMemory)).setText("");
                                        showNumber();
                                        ((EditText)findViewById(R.id.editTextMemory)).addTextChangedListener(editTextMemoryWatcher);
                                    } catch (Exception e) {

                                    }
                                }
                            });
                    }
                }, 500);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };


    // SEQUENCING GAME MODE
    public void openSequencingMode(View view) {
        setContentView(R.layout.sequencing_mode);
        sequence = new ArrayList<Integer>();
        answer = new ArrayList<Integer>();
        colorBoxes();
        level = 0;
        playable = true;
        showBox();
    }

    List<Integer> sequence = new ArrayList<Integer>();
    List<Integer> answer = new ArrayList<Integer>();
    public void chooseBox(View view) {
        answer.add(view.getId());

        if(answer.size() == sequence.size()) {
            for (int i = 0; i < sequence.size(); i++) {
                if((int)sequence.get(i) != (int)answer.get(i)) {
                    ((TextView)findViewById(R.id.textView13)).setText("Wrong!");
                    playable = false;
                }
            }
            if(playable) showBox();
            if(playable) level++;
            if(level > leaderBoard[2]) leaderBoard[2] = level;
            ((TextView)findViewById(R.id.textView12)).setText("Score: " + level);
        }
    }

    public void colorBoxes() {
        Button btn1 = (Button)findViewById(R.id.button19);
        Button btn2 = (Button)findViewById(R.id.button20);
        Button btn3 = (Button)findViewById(R.id.button21);
        Button btn4 = (Button)findViewById(R.id.button22);
        Button btn5 = (Button)findViewById(R.id.button23);
        Button btn6 = (Button)findViewById(R.id.button24);
        Button btn7 = (Button)findViewById(R.id.button26);


        List<ColorLabel> colors = new ArrayList<>();
        colors.add(new ColorLabel("Red", Color.RED));
        colors.add(new ColorLabel("Green", Color.GREEN));
        colors.add(new ColorLabel("Blue", Color.BLUE));
        colors.add(new ColorLabel("Yellow", Color.YELLOW));
        colors.add(new ColorLabel("Black", Color.BLACK));
        colors.add(new ColorLabel("Gray", Color.GRAY));

        btn1.setBackgroundColor(colors.get(0).color);
        btn2.setBackgroundColor(colors.get(1).color);
        btn3.setBackgroundColor(colors.get(2).color);
        btn4.setBackgroundColor(colors.get(3).color);
        btn5.setBackgroundColor(colors.get(4).color);
        btn6.setBackgroundColor(colors.get(5).color);
    }

    public void showBox() {
        Button btn1 = (Button)findViewById(R.id.button19);
        Button btn2 = (Button)findViewById(R.id.button20);
        Button btn3 = (Button)findViewById(R.id.button21);
        Button btn4 = (Button)findViewById(R.id.button22);
        Button btn5 = (Button)findViewById(R.id.button23);
        Button btn6 = (Button)findViewById(R.id.button24);
        Button btn7 = (Button)findViewById(R.id.button26);

        List<Button> buttons= new ArrayList<Button>();
        buttons.add(btn1);
        buttons.add(btn2);
        buttons.add(btn3);
        buttons.add(btn4);
        buttons.add(btn5);
        buttons.add(btn6);

        Button randomBox = buttons.get(((int)Math.round(Math.random() * 5)));
        sequence.add(randomBox.getId());
        answer = new ArrayList<Integer>();

        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            if(button.getId() != randomBox.getId()) {
                button.setVisibility(View.INVISIBLE);

                Timer timer = new Timer();
                state.put(timer, score);
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    button.setVisibility(View.VISIBLE);
                                } catch (Exception e) {

                                }
                            }
                        });
                    }
                }, 1000);
            }
        }
    }

    // LEADER BOARD
    int[] leaderBoard = {0,0,0};
    public void openLeaderBoard(View view) {
        setContentView(R.layout.leader_board);
        ((TextView)findViewById(R.id.textView8)).setText("Awareness: " + leaderBoard[0]);
        ((TextView)findViewById(R.id.textView9)).setText("Memory: " + leaderBoard[1]);
        ((TextView)findViewById(R.id.textView11)).setText("Sequencing: " + leaderBoard[2]);
    }

    // ABOUT
    public void openAbout(View view) {
        setContentView(R.layout.about);
    }

    // SETTINGS
    public void openSettings(View view) {
        setContentView(R.layout.settings);
    }

    public void whiteBg(View view) {
        ((View)this.getWindow().getDecorView()).setBackgroundColor(Color.WHITE);
    }

    public void dkgrayBg(View view) {
        ((View)this.getWindow().getDecorView()).setBackgroundColor(Color.DKGRAY);
    }
}