package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.Estabelecimento;

public class EstabelecimentoDao {

    private final String TABLE_ESTABELECIMENTO = "Estabelecimento";
    private DbGateway gw;

    private  final ConexaoSQLite conexaoSQLite;

    public EstabelecimentoDao (ConexaoSQLite conexao){
        //gw = DbGateway.getInstance(ctx);
        conexaoSQLite = conexao;
    }


    public boolean salvar(String nome, String endereco){
        SQLiteDatabase db = conexaoSQLite.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("Nome", nome);
        cv.put("Endereço", endereco);


        return db.insert(TABLE_ESTABELECIMENTO, null, cv) > 0;
    }


    public boolean update(Estabelecimento estabelecimento){
        SQLiteDatabase db = conexaoSQLite.getWritableDatabase();

                try {
            ContentValues estabelecimentoAtributos = new ContentValues();
                    estabelecimentoAtributos.put("Nome", estabelecimento.getNome());
                    estabelecimentoAtributos.put("Endereço", estabelecimento.getEndereço());



            db.update(TABLE_ESTABELECIMENTO, estabelecimentoAtributos, "ID = ?", new String[]{String.valueOf(estabelecimento.getId())});
            return true;
        } catch (Exception e) {
            Log.d("ESTABELECIMENTODAO", "Nao foi possivel atualizar produto");
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }



}

    public List<Estabelecimento> retornarTodos(){

        SQLiteDatabase db = null;
        List<Estabelecimento> estabelecimentos = new ArrayList<>();
        Cursor cursor;

        try {
            db = conexaoSQLite.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM Estabelecimento", null);


            if(cursor.moveToFirst()) {
                Estabelecimento estabelecimentoTemporario = null;
                do{
                    estabelecimentoTemporario = new Estabelecimento();
                    estabelecimentoTemporario.setId(cursor.getInt(0));
                    estabelecimentoTemporario.setNome(cursor.getString(1));
                    estabelecimentoTemporario.setEndereço(cursor.getString(2));


                    estabelecimentos.add(estabelecimentoTemporario);
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            Log.d("ERRO LISTA PRODUTOS", "ERRO AO RETORNAR PRODUTOS");
        }finally {
            if (db != null) {
                db.close();
            }
        }

        return estabelecimentos;
    }

    public boolean excluir(int idEstabelecimento){
        SQLiteDatabase db = null;

        try {
            db = conexaoSQLite.getWritableDatabase();
            db.delete(TABLE_ESTABELECIMENTO, "ID = ?",  new String[]{String.valueOf(idEstabelecimento)});

            // Cursor cursor = gw.getDatabase().rawQuery("DELETE FROM Bebidas WHERE ID = idBebida ", null);
            return true;
        } catch(Exception e){
            Log.d("BEBIDADAO", "NÃO FOI POSSIVEL DELETAR A BEBIDA");
            return false;
        } finally {
            if( db != null){
                db.close();
            }
        }
    }

    /*
    public Bebida retornarBebidaById(int id){

        SQLiteDatabase db = null;
        Bebida bebida = new Bebida();
        Cursor cursor;

        try {
            db = conexaoSQLite.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM Bebidas WHERE ID = ?", new String[]{String.valueOf(id)});


            if(cursor.moveToFirst()) {
                Bebida bebidaTemporaria = null;
                do{
                    bebidaTemporaria = new Bebida();
                    bebidaTemporaria.setId(cursor.getInt(0));
                    bebidaTemporaria.setFabricante(cursor.getString(1));
                    bebidaTemporaria.setMililitros(cursor.getDouble(2));
                    bebidaTemporaria.setEstabelecimento(cursor.getString(3));
                    bebidaTemporaria.setPreco(cursor.getDouble(4));

                    bebida = bebidaTemporaria;
                    return bebida;
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            Log.d("ERRO LISTA PRODUTOS", "ERRO AO RETORNAR PRODUTOS");
        }finally {
            if (db != null) {
                db.close();
            }
        }

        return null;
    }
    */
}
