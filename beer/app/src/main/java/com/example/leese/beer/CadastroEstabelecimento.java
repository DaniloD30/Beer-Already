package com.example.leese.beer;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dao.ConexaoSQLite;
import dao.EstabelecimentoDao;

public class CadastroEstabelecimento extends AppCompatActivity {

    private EditText edtEndereco;
    private EditText edtNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_estabelecimento);

        final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);

        edtEndereco = (EditText)findViewById(R.id.edtEndereco);
        edtNome = (EditText)findViewById(R.id.edtNome);

        Button btnListar = (Button) findViewById(R.id.btnListarEstabelecimento);
        btnListar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //bebidaList = new ArrayList<>();
                //bebidaList = dao.retornarTodos();

                //this.bebidaList = dao.retornarTodos();

                try {
                    Intent intent = new Intent(CadastroEstabelecimento.this, ListarEstabelecimentoActivity.class);
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

                //carregando os campos
                EditText txtNome = findViewById(R.id.edtNome);
                EditText txtEndereco = findViewById(R.id.edtEndereco);

                //pegando os valores
                String nome = txtNome.getText().toString();
                String endereco =  txtEndereco.getText().toString();

                //salvando os dados
                EstabelecimentoDao dao = new EstabelecimentoDao(conexaoSQLite);
                boolean sucesso = dao.salvar(nome, endereco);
                if(sucesso) {
                    //limpa os campos
                    txtNome.setText("");
                    txtEndereco.setText("");
                    Snackbar.make(view, "Salvou!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    //findViewById(R.id.includemain).setVisibility(View.VISIBLE);

                }else{
                    Snackbar.make(view, "Erro ao salvar, consulte os logs!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
     }

    // FALTA FAZER A AÇÃO DO SALVAR, JUNTAMENTE COM O INSERT NO SQLIGHT
    // IMPLEMENTAR A AÇÃO DO BOTÃO CANCELAR
}
