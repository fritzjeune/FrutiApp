package com.allinnetwork.frutiapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity_level1 extends AppCompatActivity {

    private TextView tv_name, tv_score;
    private ImageView iv_uno,iv_dos,iv_life;
    private MediaPlayer mp, mp_great, mp_bad;
    private EditText et_answer;

    int score, numAleatorio_uno, numAleatorio_dos, results, lifes = 3;
    String player_name, string_score, string_lifes;

    String number [] = {"cero","uno","dos","tres","cuatro", "cinco","seis","siete","ocho","nueve"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_level1);


        Toast.makeText(this, "level 1 - Basic addition", Toast.LENGTH_SHORT).show();

        tv_name = findViewById(R.id.txt_name);
        tv_score = findViewById(R.id.txt_score);
        iv_uno = findViewById(R.id.imv_num1);
        iv_dos = findViewById(R.id.imv_num2);
        iv_life = findViewById(R.id.imv_life);
        et_answer = findViewById(R.id.edt_answer);

        player_name = getIntent().getStringExtra("player");
        tv_name.setText("Player: " + player_name);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mp = MediaPlayer.create(this, R.raw.goats);
        mp.start();
        mp.setLooping(true);

        numAleatorio();


        mp_great = MediaPlayer.create(this, R.raw.wonderful);
        mp_bad = MediaPlayer.create(this, R.raw.bad);

    }

    public void compare (View view) {
        String answer = et_answer.getText().toString();


        if (!answer.equals("")) {
            int answer_player = Integer.parseInt(answer);
            if(results == answer_player){
                mp_great.start();
                score++;
                tv_score.setText("Score: " + score);
                et_answer.setText("");
                dataBase();
            } else {
                mp_bad.start();
                et_answer.setText("");
                lifes--;
                dataBase();

                switch (lifes){
                    case 3:
                        iv_life.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        iv_life.setImageResource(R.drawable.dosvidas);
                        Toast.makeText(this, "you have 2 more intent", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        iv_life.setImageResource(R.drawable.unavida);
                        Toast.makeText(this, "you have 1 more intent", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(this, "game over", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("scorefinal", score);
                        startActivity(intent);
                        finish();
                        setResult(RESULT_OK, intent);
                        mp.stop();
                        mp.release();
                        break;
                }

            }

            numAleatorio();

    } else {
            Toast.makeText(this, "you must enter an answer", Toast.LENGTH_SHORT).show();
        }
    }

    public void numAleatorio () {
        if (score <= 9){
            numAleatorio_uno = (int) (Math.random() * 10);
            numAleatorio_dos = (int) (Math.random() * 10);

            results = numAleatorio_uno + numAleatorio_dos;

            if (results <= 10){
                for(int i = 0; i < number.length; i++) {
                    int id = getResources().getIdentifier(number[i], "drawable" , getPackageName());
                    if (numAleatorio_uno == i){
                        iv_uno.setImageResource(id);
                    }  if (numAleatorio_dos == i) {
                        iv_dos.setImageResource(id);
                    }
                }
            } else {
                numAleatorio();
            }
        } else {
            Intent intent = new Intent(this, Main2Activity_level2.class);

            string_score = String.valueOf(score);
            string_lifes = String.valueOf(lifes);

            intent.putExtra("player_name" , player_name);
            intent.putExtra("score" , string_score);
            intent.putExtra("lifes" , string_lifes);

            startActivity(intent);
            finish();

            mp.stop();
            mp.release();

        }

    }

    public void dataBase () {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "DB", null, 1);
        SQLiteDatabase DB = admin.getWritableDatabase();

        Cursor consult = DB.rawQuery("select * from scored where score = (select max(score) from scored)", null);

        if(consult.moveToFirst()) {
            String temp_name = consult.getString(0);
            String temp_score = consult.getString(1);
            int best_score = Integer.parseInt(temp_score);

            if (score > best_score){
                ContentValues change = new ContentValues();
                change.put("name" , player_name);
                change.put("score" , score);

                DB.update("scored", change, "score=" + best_score, null);

            }
            DB.close();

        } else {
            ContentValues add = new ContentValues();
            add.put("name" , player_name);
            add.put("score" , score);

            DB.insert("scored", null, add);
            DB.close();
        }


    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "you can't go back to lower level",Toast.LENGTH_LONG).show();
    }
}
