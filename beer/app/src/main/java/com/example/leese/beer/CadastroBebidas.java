package com.example.leese.beer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dao.BebidaDao;
import dao.ConexaoSQLite;
import dao.EstabelecimentoDao;
import dao.RetrofitService;
import dao.ServiceGenerator;
import model.Bebida;
import model.Estabelecimento;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroBebidas extends AppCompatActivity {

    private List<Estabelecimento> listaEstabelecimentos;
    private EstabelecimentoDao estabelecimentoDao;
    private String estabelecimento;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_bebidas);

        final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);
        dialog = new ProgressDialog(CadastroBebidas.this);
        dialog.setMessage("Carregando...");
        dialog.setCancelable(false);
        dialog.show();
        RetrofitService service = ServiceGenerator
                .createService(RetrofitService.class);

        Call<List<Estabelecimento>> call = service.getAll();

        call.enqueue(new Callback<List<Estabelecimento>>() {
            @Override
            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                final Spinner spnProdutos;
                final List<Estabelecimento> listaEstabelecimento;
                listaEstabelecimento = response.body();
                //  estabelecimentoList = listaEstabelecimento;
                //lsvEstabelecimento = (ListView) findViewById(R.id.lsvEstabelecimento);
                //Log.d("listafinal", " " + estabelecimentoList.isEmpty());
                //adapterListaEstabelecimento = new AdapterListaEstabelecimento(ListarEstabelecimentoActivity.this, listaEstabelecimento);
                //lsvEstabelecimento.setAdapter(adapterListaEstabelecimento);
                spnProdutos = (Spinner) findViewById(R.id.spinnerEstabelecimento);
                ArrayAdapter<Estabelecimento> spnEstabelecimentoAdapter = new ArrayAdapter<Estabelecimento>(CadastroBebidas.this, android.R.layout.simple_spinner_item, listaEstabelecimento);
                spnProdutos.setAdapter(spnEstabelecimentoAdapter);
                //Spinner
                // this.estabelecimentoDao = new EstabelecimentoDao(conexaoSQLite);
                //this.listaEstabelecimentos = this.estabelecimentoDao.retornarTodos();


                spnProdutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        estabelecimento = spnProdutos.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }


                });
            }
            @Override
            public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(getBaseContext(), "Problema de acesso", Toast.LENGTH_LONG).show();
            }//fim do onFailure
        });


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

        Button btnEstabelecimento = (Button) findViewById(R.id.btnCadastrarCadastrarEstabelecimento);
        btnEstabelecimento.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(CadastroBebidas.this, CadastroEstabelecimento.class);
                startActivity(it);
            }
        });

        Button btnSalvar = (Button)findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText txtFabricante = findViewById(R.id.edtFabricante);
                EditText txtMililitros =  findViewById(R.id.edtMililitros);
               // EditText txtEstabelecimento =  findViewById(R.id.edtEstabelecimento);
                EditText txtPreco = findViewById(R.id.edtPreco);


                String fabricante = txtFabricante.getText().toString();

                Double mililitros = Double.parseDouble(txtMililitros.getText().toString());
                //String estabelecimento = txtEstabelecimento.getText().toString();

                Double preco = Double.parseDouble(txtPreco.getText().toString());
                dialog = new ProgressDialog(CadastroBebidas.this);
                dialog.setMessage("Carregando...");
                dialog.setCancelable(false);
                dialog.show();
                Bebida bebida = new Bebida();
                bebida.setFabricante(fabricante);
                bebida.setMililitros(mililitros);
                bebida.setPreco(preco);
                bebida.setEstabelecimento(estabelecimento);

                RetrofitService service = ServiceGenerator
                        .createService(RetrofitService.class);
                final Call<Void> call = service.addBebida(bebida);
                call.enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(getBaseContext(), "Bebida inserido com sucesso", Toast.LENGTH_SHORT).show();
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
                BebidaDao dao = new BebidaDao(conexaoSQLite);
                boolean sucesso = dao.salvar(fabricante, mililitros, estabelecimento, preco);
                if(sucesso) {
                    //limpa os campos
                   // txtEstabelecimento.setText("");
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
                */
            }
        });

    }

    // FAZER A AÇÃO DO BOTAÇÃO SALVAR, JUNTAMENTE COM O INSERT NO SQLIGHT
    // IMPLEMENTAR A AÇÃO DO BOTÃO CANCELAR
}
