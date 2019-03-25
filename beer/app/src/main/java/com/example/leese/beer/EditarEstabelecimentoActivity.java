package com.example.leese.beer;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dao.ConexaoSQLite;
import dao.EstabelecimentoDao;
import dao.RetrofitService;
import dao.ServiceGenerator;
import model.Estabelecimento;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarEstabelecimentoActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtEndereco;
    ProgressDialog dialog;

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
                dialog = new ProgressDialog(EditarEstabelecimentoActivity.this);
                dialog.setMessage("Carregando...");
                dialog.setCancelable(false);
                dialog.show();
                RetrofitService service1 = ServiceGenerator
                        .createService(RetrofitService.class);
                Call<Void> call = service1.alterarEstabelecimento(b.getId(), b);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(getBaseContext(), "Estabelecimento alterado com sucesso", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void setDadosEstabelecimento(Estabelecimento estabelecimento) {

        this.edtNome.setText(String.valueOf(estabelecimento.getNome()));
        this.edtEndereco.setText(String.valueOf(estabelecimento.getEndereço()));

    }



}
