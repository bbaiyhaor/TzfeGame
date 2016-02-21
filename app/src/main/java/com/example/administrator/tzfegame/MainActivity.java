package com.example.administrator.tzfegame;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public MainActivity(){
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvScore = (TextView) findViewById(R.id.tvScore);
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void clearScore(){
        score = 0;
        showScore();
    }

    public void showScore(){
        tvScore.setText(score + "");
    }

    public void addScore(int s){
        score += s;
        showScore();
    }
    private int score = 0;
    private TextView tvScore;
    private static MainActivity mainActivity = null;
}
