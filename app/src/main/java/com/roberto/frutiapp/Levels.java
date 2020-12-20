package com.roberto.frutiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.Random;

public class Levels extends AppCompatActivity {

    private TextView nombre, score;
    private ImageView manzanas, numero1, operador, numero2;
    private EditText respuesta;
    private MediaPlayer cancion;

    private int nivel = 1;
    private int rndInt = 0;
    private int resultado = 0;
    private int vidas = 3;
    private int scoreActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        nombre = findViewById(R.id.nombre);
        nombre.setText(getIntent().getStringExtra("jugador"));

        score = findViewById(R.id.score);

        manzanas = findViewById(R.id.manzanas);

        numero1 = findViewById(R.id.numero1);
        operador = findViewById(R.id.operador);
        numero2 = findViewById(R.id.numero2);

        respuesta = findViewById(R.id.respuesta);

        cancion = MediaPlayer.create(this, R.raw.goats);
        cancion.start();
        cancion.setLooping(true);

        Toast.makeText(this, "Nivel 1 - Sumas básicas", Toast.LENGTH_LONG).show();

        cambiarImagenes();
    }

    private void cambiarImagenes() {
        switch (nivel) {
            case 1:
                suma();
                break;

            case 3:
                resta();
                break;

            case 4:
                rndInt = new Random().nextInt(2);

                if (rndInt == 0) {
                    operador.setImageResource(R.drawable.adicion);
                    operador.setContentDescription("suma");
                } else {
                    resta();
                }

                break;

            case 5:
                operador.setImageResource(R.drawable.multiplicacion);
                operador.setContentDescription("multiplicacion");
                break;

            case 6:
                rndInt = new Random().nextInt(3);
                if (rndInt == 0) {
                    operador.setImageResource(R.drawable.adicion);
                    operador.setContentDescription("suma");
                } else if (rndInt == 1) {
                    operador.setImageResource(R.drawable.multiplicacion);
                    operador.setContentDescription("multiplicacion");
                } else {
                    resta();
                }

                break;
        }

        if (nivel != 1 && !operador.getContentDescription().toString().equals("resta")) {
            rndInt = new Random().nextInt(10);
            numero1.setImageResource(getResources().getIdentifier("img" + rndInt, "drawable", getPackageName()));
            numero1.setContentDescription(rndInt+"");

            rndInt = new Random().nextInt(10);
            numero2.setImageResource(getResources().getIdentifier("img" + rndInt, "drawable", getPackageName()));
            numero2.setContentDescription(rndInt+"");
        }

        rndInt = 0;
    }

    private void suma() {
        rndInt = new Random().nextInt(10);
        numero1.setImageResource(getResources().getIdentifier("img" + rndInt, "drawable", getPackageName()));
        numero1.setContentDescription(rndInt+"");

        resultado = rndInt;

        rndInt = new Random().nextInt(10);
        resultado += rndInt;

        if (resultado > 10) {
            cambiarImagenes();
        }

        numero2.setImageResource(getResources().getIdentifier("img" + rndInt, "drawable", getPackageName()));
        numero2.setContentDescription(rndInt+"");

        operador.setContentDescription("suma");

        resultado = 0;
        rndInt = 0;
    }

    private void resta() {
        rndInt = new Random().nextInt(10);
        numero1.setImageResource(getResources().getIdentifier("img" + rndInt, "drawable", getPackageName()));
        numero1.setContentDescription(rndInt+"");

        resultado = rndInt;

        rndInt = new Random().nextInt(10);
        resultado = resultado - rndInt;

        if (resultado < 0) {
            cambiarImagenes();
        }

        numero2.setImageResource(getResources().getIdentifier("img" + rndInt, "drawable", getPackageName()));
        numero2.setContentDescription(rndInt+"");

        operador.setImageResource(R.drawable.resta);
        operador.setContentDescription("resta");

        resultado = 0;
        rndInt = 0;
    }

    public void comprobarRespuesta(View view) {
        int resultadoJugador = 0;

        MediaPlayer sonido = null;

        if (!respuesta.getText().toString().equals("")) {
            resultadoJugador = Integer.parseInt(respuesta.getText().toString());

            switch (operador.getContentDescription().toString()) {
                case "suma":
                    resultado = Integer.parseInt(numero1.getContentDescription().toString()) + Integer.parseInt(numero2.getContentDescription().toString());
                    break;
                case "resta":
                    resultado = Integer.parseInt(numero1.getContentDescription().toString()) - Integer.parseInt(numero2.getContentDescription().toString());
                    break;
                case "multiplicacion":
                    resultado = Integer.parseInt(numero1.getContentDescription().toString()) * Integer.parseInt(numero2.getContentDescription().toString());
                    break;
            }

            if (resultadoJugador == resultado) {
                Toast.makeText(this, "¡Muy bien!", Toast.LENGTH_SHORT).show();
                sonido = MediaPlayer.create(this, R.raw.wonderful);
                if (nivel == 1) {
                    scoreActual = Integer.parseInt(score.getText().toString().charAt(0)+"");
                } else {
                    scoreActual = Integer.parseInt(score.getText().toString().substring(0, 2));
                }

                scoreActual++;
                if (scoreActual == 1) {
                    score.setText("1 punto");
                } else {
                    score.setText(scoreActual + " puntos");
                }

                switch (scoreActual) {
                    case 10:
                        nivel = 2;
                        Toast.makeText(this, "Nivel 2 - Sumas avanzadas", Toast.LENGTH_LONG).show();
                        break;
                    case 20:
                        nivel = 3;
                        Toast.makeText(this, "Nivel 3 - Restas", Toast.LENGTH_LONG).show();
                        break;
                    case 30:
                        nivel = 4;
                        Toast.makeText(this, "Nivel 4 - Sumas y restas", Toast.LENGTH_LONG).show();
                        break;
                    case 40:
                        nivel = 5;
                        Toast.makeText(this, "Nivel 5 - Multiplicaciones", Toast.LENGTH_LONG).show();
                        break;
                    case 50:
                        nivel = 6;
                        Toast.makeText(this, "Nivel 6 - Sumas, restas y multiplicaciones", Toast.LENGTH_LONG).show();
                        break;
                }
            } else {
                sonido = MediaPlayer.create(this, R.raw.bad);
                vidas--;
                Toast.makeText(this, "¡Mal! El resultado era " + resultado, Toast.LENGTH_SHORT).show();
                switch (vidas) {
                    case 0:
                        Toast.makeText(this, "Te has quedado sin manzanas", Toast.LENGTH_SHORT).show();
                        finalizarActivity();
                        break;
                    case 1:
                        manzanas.setImageResource(R.drawable.unavida);
                        Toast.makeText(this, "Te queda una manzana", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        manzanas.setImageResource(R.drawable.dosvidas);
                        Toast.makeText(this, "Te quedan dos manzanas", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            sonido.start();
            resultado = 0;
            respuesta.setText("");
            cambiarImagenes();
        } else {
            Toast.makeText(this, "¡Pon un resultado!", Toast.LENGTH_LONG).show();
        }
    }

    private void finalizarActivity() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "db", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("nombre", nombre.getText().toString());
        registro.put("score", scoreActual);

        db.insert("puntaje", null, registro);
        db.close();

        cancion.stop();
        cancion.release();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}