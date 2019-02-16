package com.example.leese.beer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapters.AdapterCestas;
import dao.CestaDao;
import dao.ConexaoSQLite;
import model.Cesta;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AdapterCestas adapterCestas;
    private ListView lsvCestas;
    private final ConexaoSQLite conexaoSQLite = ConexaoSQLite.getInstance(this);
    private CestaDao daoCesta = new CestaDao(conexaoSQLite);
    private List<Cesta> cestaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//       setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, CestaActivity.class);
                startActivity(intent);

            }
        });
        cestaList = new ArrayList<>();
        this.cestaList = daoCesta.retornarTodos();
        //Log.d("Compare","Tamanho na lista de cestas " + this.cestaList.size());

        lsvCestas = (ListView) findViewById(R.id.lsvCestas);

        this.adapterCestas = new AdapterCestas(Main2Activity.this, this.cestaList);

        this.lsvCestas.setAdapter(this.adapterCestas);

        this.lsvCestas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final Cesta cestaSelecionada = (Cesta) adapterCestas.getItem(position);


                //Toast.makeText(ListarBebidasActivity.this, "Bebida: " + bebidaSelecionada.getFabricante(), Toast.LENGTH_SHORT);
                final AlertDialog.Builder janelaEscolha = new AlertDialog.Builder(Main2Activity.this);

                janelaEscolha.setNeutralButton("Melhor bebida", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundleDadosCesta = new Bundle();
                        // fabricante, String estabelecimento, Double preco, double ml
                        Log.d("CestaActivity","ID CESTA COMPARE:  " + cestaSelecionada.getId());
                        bundleDadosCesta.putInt("id_cesta", cestaSelecionada.getId());
                        bundleDadosCesta.putString("nome", cestaSelecionada.getNome());

                        Intent intentEditarBebidas = new Intent(Main2Activity.this, OrdenadaActivity.class);
                        intentEditarBebidas.putExtras(bundleDadosCesta);
                        startActivity(intentEditarBebidas);
                    }
                });

                janelaEscolha.setNegativeButton("Excluir cesta", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //excluir o produto
                        boolean excluiu = daoCesta.excluir(cestaSelecionada.getId());

                        dialog.cancel();

                        if(excluiu) {
                            adapterCestas.removerBebida(position);
                            Toast.makeText(Main2Activity.this, "Cesta excluida com sucessso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Main2Activity.this, "Erro ao excluir produto", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                // Toast.makeText(CestaActivity.this, "Bebida adicionada na cesta com sucesso", Toast.LENGTH_SHORT).show();

                // PEGAR A BEBIDA SELECIONADA, COLOCAR NA CESTA
                // PRRENCHER A RECLYCLE VIEW DA CESTA NA TELA " Compare "
                janelaEscolha.create().show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*  <activity
            android:name=".Main2Activity"
            android:label="@string/title_activity_main2"
            android:theme="@style/AppTheme.NoActionBar"></activity>*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent it = new Intent(Main2Activity.this, CadastroBebidas.class);
            startActivity(it);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent it = new Intent(Main2Activity.this, CadastroEstabelecimento.class);
            startActivity(it);

        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //executa o evento de click do botão atualizar
    public void eventAtualizar(View view){
        this.adapterCestas.atualizar(daoCesta.retornarTodos());
    }
}
