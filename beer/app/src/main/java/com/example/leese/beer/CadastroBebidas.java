package com.example.leese.beer;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import dao.BebidaDao;
import dao.ConexaoSQLite;
import model.Bebida;

public class CadastroBebidas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_bebidas);

        final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);

        //edtFabricante = (EditText)findViewById(R.id.edtFabricante);
        //edtEstabelecimento = (EditText)findViewById(R.id.edtEstabelecimento);
        //edtPreco = (EditText)findViewById(R.id.edtPreco);
        //edtMililitros = (EditText)findViewById(R.id.edtMililitros);



        Button btnListar = (Button) findViewById(R.id.btnListarBebidas);
        btnListar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //bebidaList = new ArrayList<>();
                //bebidaList = dao.retornarTodos();

                //this.bebidaList = dao.retornarTodos();

                try {
                     Intent intent = new Intent(CadastroBebidas.this, ListarBebidasActivity.class);
                     startActivity(intent);
                }catch (NullPointerException e) {
                    Snackbar.make(v, "Lista vazia, por favor cadastra uma bebida!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        Button btnSalvar = (Button)findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText txtFabricante = findViewById(R.id.edtFabricante);
                EditText txtMililitros =  findViewById(R.id.edtMililitros);
                EditText txtEstabelecimento =  findViewById(R.id.edtEstabelecimento);
                EditText txtPreco = findViewById(R.id.edtPreco);

                String fabricante = txtFabricante.getText().toString();

                Double mililitros = Double.parseDouble(txtMililitros.getText().toString());
                String estabelecimento = txtEstabelecimento.getText().toString();

                Double preco = Double.parseDouble(txtPreco.getText().toString());

                //salvando os dados
                BebidaDao dao = new BebidaDao(conexaoSQLite);
                boolean sucesso = dao.salvar(fabricante, mililitros, estabelecimento, preco);
                if(sucesso) {
                    //limpa os campos
                    txtEstabelecimento.setText("");
                    txtFabricante.setText("");
                    txtMililitros.setText("");
                    txtPreco.setText("");
                    Snackbar.make(view, "Salvou!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    //indViewById(R.id.includemain).setVisibility(View.VISIBLE);

                }else{
                    Snackbar.make(view, "Erro ao salvar, consulte os logs!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    //Podemos usar o Toast
                }
            }
        });
    }

    // FAZER A AÇÃO DO BOTAÇÃO SALVAR, JUNTAMENTE COM O INSERT NO SQLIGHT
    // IMPLEMENTAR A AÇÃO DO BOTÃO CANCELAR
}
