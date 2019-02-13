package com.example.leese.beer;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dao.BebidaDao;
import dao.ConexaoSQLite;
import model.Bebida;

public class EditarActivity extends AppCompatActivity {

    //fabricante, String estabelecimento, Double preco, double ml

    // private EditText edtIdBebida;
    private EditText edtFabricante;
    private EditText edtEstabelecimento;
    private EditText edtPrecoBebida;
    private EditText edtMililitros;
    private Bebida bebida;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);

        this.edtFabricante = (EditText) findViewById(R.id.edtFabricante);
        this.edtEstabelecimento = (EditText) findViewById(R.id.edtEstabelecimento);
        this.edtPrecoBebida = (EditText) findViewById(R.id.edtPreco);
        this.edtMililitros = (EditText) findViewById(R.id.edtMililitros);

        // t("id_bebida", bebidaSelecionada.getId());
        //bundleDadosBebidas.putString("nome_fabricante", bebidaSelecionada.getFabricante());
        //bundleDadosBebidas.putString("nome_estabelecimento", bebidaSelecionada.getEstabelecimento());
        //bundleDadosBebidas.putDouble("preco_bebida", bebidaSelecionada.getPreco());
        //bundleDadosBebidas.putDouble("volume_bebida"

        Bundle bundleDadosBebida = getIntent().getExtras();

        final Bebida bebida = new Bebida(bundleDadosBebida.getInt("id_bebida"),
                bundleDadosBebida.getString("nome_fabricante"),
                bundleDadosBebida.getString("nome_estabelecimento"),
                bundleDadosBebida.getDouble("preco_bebida"),
                bundleDadosBebida.getDouble("volume_bebida"));

        this.setDadosBebida(bebida);

        Button btnSalvar = (Button)findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //carregando os campos
                EditText txtFabricante = findViewById(R.id.edtFabricante);
                EditText txtMililitros =  findViewById(R.id.edtMililitros);
                EditText txtEstabelecimento =  findViewById(R.id.edtEstabelecimento);
                EditText txtPreco = findViewById(R.id.edtPreco);

                String fabricante = txtFabricante.getText().toString();
                Double mililitros = Double.parseDouble(txtMililitros.getText().toString());
                String estabelecimento = txtEstabelecimento.getText().toString();
                Double preco = Double.parseDouble(txtPreco.getText().toString());
                // fabricante, String estabelecimento, Double preco, double ml)
                Bebida b = new Bebida(bebida.getId(), fabricante, estabelecimento, preco, mililitros);

                //salvando os dados
                BebidaDao dao = new BebidaDao(conexaoSQLite);
                boolean sucesso = dao.update(b);
                //boolean sucesso = dao.salvar(fabricante, mililitros, estabelecimento, preco);
                if(sucesso) {
                    //limpa os campos
                    txtEstabelecimento.setText("");
                    txtFabricante.setText("");
                    txtMililitros.setText("");
                    txtPreco.setText("");

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

    private void setDadosBebida(Bebida bebida) {

        this.edtFabricante.setText(String.valueOf(bebida.getFabricante()));
        this.edtEstabelecimento.setText(String.valueOf(bebida.getEstabelecimento()));
        this.edtPrecoBebida.setText(String.valueOf(bebida.getPreco()));
        this.edtMililitros.setText(String.valueOf(bebida.getMililitros()));
    }

    private Bebida getDadosBebidas() {

        this.bebida = new Bebida();

        if (this.edtFabricante.getText().toString().isEmpty() == false) {
            this.bebida.setFabricante(this.edtFabricante.getText().toString());
        } else {
            return null;
        }

        if (this.edtEstabelecimento.getText().toString().isEmpty() == false) {
            this.bebida.setEstabelecimento(this.edtEstabelecimento.getText().toString());
        } else {
            return null;
        }

        if (this.edtPrecoBebida.getText().toString().isEmpty() == false) {
            double precoBebida = Double.parseDouble(this.edtPrecoBebida.getText().toString());
            this.bebida.setPreco(precoBebida);
        } else {
            return null;
        }

        if (this.edtMililitros.getText().toString().isEmpty() == false) {
            double mililitros = Double.parseDouble(this.edtMililitros.getText().toString());
            this.bebida.setMililitros(mililitros);
        } else {
            return null;
        }

        return this.bebida;
    }



}
