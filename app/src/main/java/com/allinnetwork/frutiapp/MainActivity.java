package com.allinnetwork.frutiapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText et_nombre;
    private TextView tv_bestscore;
    private ImageView iv_personaje;
    private MediaPlayer mp;

    int num_aleatorio = (int) (Math.random() * 10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_nombre = findViewById(R.id.text_nombre);
        tv_bestscore = findViewById(R.id.textview_bestscore);
        iv_personaje = findViewById(R.id.imview_personaje);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        int id;
        if (num_aleatorio == 0 || num_aleatorio == 10) {
            id = getResources().getIdentifier("mango", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if (num_aleatorio == 1 || num_aleatorio == 9) {
            id = getResources().getIdentifier("fresa", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if (num_aleatorio == 2 || num_aleatorio == 8) {
            id = getResources().getIdentifier("sandia", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if (num_aleatorio == 3 || num_aleatorio == 7) {
            id = getResources().getIdentifier("uva", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if (num_aleatorio == 4 || num_aleatorio == 5 || num_aleatorio == 6) {
            id = getResources().getIdentifier("manzana", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "DB", null, 1);
        SQLiteDatabase DB = admin.getWritableDatabase();

        Cursor consult = DB.rawQuery(
                "select * from scored where score = (select max(score) from scored)",null);

        if (consult.moveToFirst()){
            String temp_name = consult.getString(0);
            String temp_score = consult.getString(1);
            tv_bestscore.setText("record: " + temp_score + " from " + temp_name);
            DB.close();
        } else {
            DB.close();
        }

        mp = MediaPlayer.create(this,R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);


    }

    public void Play (View view){
        String nombre = et_nombre.getText().toString();
        if (!nombre.equals("")) {
            mp.stop();
            mp.release();
            Intent intent = new Intent(this, Main2Activity_level1.class);
            intent.putExtra("player", nombre);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "you must enter your name", Toast.LENGTH_SHORT).show();
            et_nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_nombre, InputMethodManager.SHOW_IMPLICIT);
        }
    }



}
