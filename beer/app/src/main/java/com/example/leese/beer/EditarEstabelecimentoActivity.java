package com.example.leese.beer;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dao.ConexaoSQLite;
import dao.EstabelecimentoDao;
import model.Estabelecimento;

public class EditarEstabelecimentoActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtEndereco;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_estabelecimento);

        final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);

        this.edtNome = (EditText) findViewById(R.id.edtNome);
        this.edtEndereco = (EditText) findViewById(R.id.edtEndereco);

        Bundle bundleDadosEstabelecimento = getIntent().getExtras();

        final Estabelecimento estabelecimento = new Estabelecimento(bundleDadosEstabelecimento.getInt("id_bebida"),
                bundleDadosEstabelecimento.getString("nome_estabelecimento"),
                bundleDadosEstabelecimento.getString("endereco")
                );


        this.setDadosEstabelecimento(estabelecimento);

        Button btnSalvar = (Button)findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //carregando os campos
                EditText txtNome = findViewById(R.id.edtNome);
                EditText txtEndereco =  findViewById(R.id.edtEndereco);


                String nome = txtNome.getText().toString();

                String endereco = txtEndereco.getText().toString();

                // fabricante, String estabelecimento, Double preco, double ml)
                Estabelecimento b = new Estabelecimento(estabelecimento.getId(), nome, endereco);

                //salvando os dados
                EstabelecimentoDao dao = new EstabelecimentoDao(conexaoSQLite);
                boolean sucesso = dao.update(b);
                //boolean sucesso = dao.salvar(fabricante, mililitros, estabelecimento, preco);
                if(sucesso) {
                    //limpa os campos
                    txtNome.setText("");
                    txtEndereco.setText("");


                    Snackbar.make(view, "Atualizou!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    //indViewById(R.id.includemain).setVisibility(View.VISIBLE);

                }else{
                    Snackbar.make(view, "Erro ao atualizar, consulte os logs!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    //Podemos usar o Toast
                }
            }
        });
    }

    private void setDadosEstabelecimento(Estabelecimento estabelecimento) {

        this.edtNome.setText(String.valueOf(estabelecimento.getNome()));
        this.edtEndereco.setText(String.valueOf(estabelecimento.getEndereço()));

    }



}
