package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.Bebida;

public class BebidaDao {

    private final String TABLE_BEBIDAS = "Bebidas";
   // private DbGateway gw;
    private  final ConexaoSQLite conexaoSQLite;
    public BebidaDao(ConexaoSQLite conexao){
        //gw = DbGateway.getInstance(ctx);
        conexaoSQLite = conexao;
    }

   // = "CREATE TABLE Bebidas (ID INTEGER PRIMARY KEY AUTOINCREMENT, Fabricante TEXT NOT NULL, " +
     //       "Mililitros REAL, Estabelecimento TEXT, Preço REAL NOT NULL);";

    public boolean salvar(String fabricante, Double mililitros, String estabelecimento, Double preco){
        SQLiteDatabase db = conexaoSQLite.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("Fabricante", fabricante);
        cv.put("Mililitros", mililitros);
        cv.put("Estabelecimento", estabelecimento);
        cv.put("Preço", preco);
        return db.insert(TABLE_BEBIDAS, null, cv) > 0;
    }


    public boolean update(Bebida bebida){
        SQLiteDatabase db = conexaoSQLite.getWritableDatabase();

            try {
                ContentValues bebidaAtributos = new ContentValues();
                bebidaAtributos.put("Fabricante", bebida.getFabricante());
                bebidaAtributos.put("Mililitros", bebida.getMililitros());
                bebidaAtributos.put("Estabelecimento", bebida.getEstabelecimento());
                bebidaAtributos.put("Preço", bebida.getPreco());

                db.update(TABLE_BEBIDAS, bebidaAtributos, "ID = ?", new String[]{String.valueOf(bebida.getId())});
                return true;
            } catch (Exception e) {
                Log.d("BEBIDADAO", "Nao foi possivel atualizar produto");
                return false;
            } finally {
                if (db != null) {
                    db.close();
                }
            }



    }

    public List<Bebida> retornarTodos(){

        SQLiteDatabase db = null;
        List<Bebida> bebidas = new ArrayList<>();
        Cursor cursor;

        try {
            db = conexaoSQLite.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM Bebidas", null);


            if(cursor.moveToFirst()) {
                Bebida bebidaTemporaria = null;
                do{
                    bebidaTemporaria = new Bebida();
                    bebidaTemporaria.setId(cursor.getInt(0));
                    bebidaTemporaria.setFabricante(cursor.getString(1));
                    bebidaTemporaria.setMililitros(cursor.getDouble(2));
                    bebidaTemporaria.setEstabelecimento(cursor.getString(3));
                    bebidaTemporaria.setPreco(cursor.getDouble(4));

                    bebidas.add(bebidaTemporaria);
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            Log.d("ERRO LISTA PRODUTOS", "ERRO AO RETORNAR PRODUTOS");
        }finally {
            if (db != null) {
                db.close();
            }
        }

        return bebidas;
    }

    public boolean excluir(int idBebida){
        SQLiteDatabase db = null;

        try {
            db = conexaoSQLite.getWritableDatabase();
            db.delete(TABLE_BEBIDAS, "ID = ?",  new String[]{String.valueOf(idBebida)});

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



}
