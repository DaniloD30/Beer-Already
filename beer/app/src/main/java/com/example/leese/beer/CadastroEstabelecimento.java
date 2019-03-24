package com.example.leese.beer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import dao.ConexaoSQLite;
import dao.EstabelecimentoDao;
import dao.RetrofitService;
import dao.ServiceGenerator;
import model.Estabelecimento;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CadastroEstabelecimento extends AppCompatActivity {

    private EditText edtEndereco;
    private EditText edtNome;
    ProgressDialog dialog;



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
                txtNome.setText("");
                txtEndereco.setText("");

                dialog = new ProgressDialog(CadastroEstabelecimento.this);
                dialog.setMessage("Carregando...");
                dialog.setCancelable(false);
                dialog.show();
                Estabelecimento estabelecimento = new Estabelecimento();
                estabelecimento.setNome(nome);
                estabelecimento.setEndereço(endereco);

                RetrofitService service = ServiceGenerator
                        .createService(RetrofitService.class);
                final Call<Void> call = service.add(estabelecimento);
                call.enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(getBaseContext(), "Estabelecimento inserido com sucesso", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                    }
                });
                /*
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
                */
            }
        });
     }


}
