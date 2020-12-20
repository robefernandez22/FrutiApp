package com.roberto.frutiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    private EditText nombre;
    private ImageView img;
    private TextView bestScore;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mostramos el icono en el ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // Identificamos los elementos
        nombre = findViewById(R.id.nombre);
        img = findViewById(R.id.imageView);
        bestScore = findViewById(R.id.textView);

        // Iniciamos la música
        mediaPlayer = MediaPlayer.create(this, R.raw.alphabet_song);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        // Llamamos al método que mostrará aleatoriamente una imagen
        mostrarImagen();

        // Comprobamos la mejor puntuación
        comprobarScore();
    }

    private void comprobarScore() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "db", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor consulta = db.rawQuery("SELECT * FROM puntaje WHERE score = (SELECT MAX(score) FROM puntaje)", null);
        if (consulta.moveToFirst()) {
            bestScore.setText("Mejor puntuación: " + consulta.getString(0) + " ("+consulta.getString(1)+")");
        }
        db.close();
    }

    private void mostrarImagen() {
        switch ((int)(Math.random() * 6 + 1)) {
            case 1:
                img.setImageResource(R.drawable.manzana);
                break;
            case 2:
                img.setImageResource(R.drawable.mango);
                break;
            case 3:
                img.setImageResource(R.drawable.naranja);
                break;
            case 4:
                img.setImageResource(R.drawable.sandia);
                break;
            case 5:
                img.setImageResource(R.drawable.fresa);
                break;
            case 6:
                img.setImageResource(R.drawable.uva);
                break;
        }
    }

    public void jugar(View view) {
        if (!nombre.getText().toString().equals("")) {
            // Paramos el audio y destruimos el objeto para liberar recursos
            mediaPlayer.stop();
            mediaPlayer.release();

            // Vamos al activity del primer nivel, enviamos el nombre del jugador y finalizamos este activity
            Intent intent = new Intent(this, Levels.class);
            intent.putExtra("jugador", nombre.getText().toString());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "¡Escribe tu nombre para poder jugar!", Toast.LENGTH_LONG).show();

            // Abrimos el teclado para que el usuario pueda escribir el nombre
            nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(nombre, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onBackPressed() {
        
    }
}