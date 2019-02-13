package com.example.leese.beer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dao.ConexaoSQLite;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);

        Button bBebida = (Button) findViewById(R.id.buttonBebida);
        Button bMercado = (Button) findViewById(R.id.buttonMercado);
        Button bCompare = (Button) findViewById(R.id.buttonCompare);

        bBebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, CadastroBebidas.class);
                startActivity(it);
            }
        });

        bMercado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, CadastroEstabelecimento.class);
                startActivity(it);
            }
        });


        bCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FALTA A ACTIVITY(BASIC) DO COMPARE, ONDE TERÁ A CESTA E ONDE ENTRARA A LOGICA DE NEGOCIO
                Intent it = new Intent(MainActivity.this, Compare.class);
                startActivity(it);
            }
        });



    }
}
