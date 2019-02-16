package dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.Bebida;
import model.Cesta;

public class CestaDao {

    private final String TABLE_CESTA = "Cesta";

    // private DbGateway gw;
    private  final ConexaoSQLite conexaoSQLite;

    public CestaDao(ConexaoSQLite conexao){
        //gw = DbGateway.getInstance(ctx);
        conexaoSQLite = conexao;
    }

    public boolean salvar(String nome){
        SQLiteDatabase db = conexaoSQLite.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("Nome", nome);

        return db.insert(TABLE_CESTA, null, cv) > 0;
    }

    public List<Cesta> retornarTodos(){

        SQLiteDatabase db = null;
        List<Cesta> cestas = new ArrayList<>();
        Cursor cursor;

        try {
            db = conexaoSQLite.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM Cesta", null);


            if(cursor.moveToFirst()) {
                Cesta cestaTemporaria = null;
                do{
                    cestaTemporaria = new Cesta();
                    cestaTemporaria.setId(cursor.getInt(0));
                    cestaTemporaria.setNome(cursor.getString(1));
                    cestas.add(cestaTemporaria);
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            Log.d("ERRO LISTA PRODUTOS", "ERRO AO RETORNAR PRODUTOS");
        }finally {
            if (db != null) {
                db.close();
            }
        }

        return cestas;
    }

    public Cesta retornarTodosByName(String name){

        SQLiteDatabase db = null;
        List<Cesta> cestas = new ArrayList<>();
        Cursor cursor;

        try {
            db = conexaoSQLite.getReadableDatabase();
           // cursor = db.rawQuery("SELECT * FROM Cesta", null);
            cursor = db.rawQuery("SELECT * FROM Cesta WHERE Nome = ?", new String[]{String.valueOf(name)});


            if(cursor.moveToFirst()) {
                Cesta cestaTemporaria = null;
                do{
                    cestaTemporaria = new Cesta();
                    cestaTemporaria.setId(cursor.getInt(0));
                    cestaTemporaria.setNome(cursor.getString(1));
                    //cestas.add(cestaTemporaria);
                    return cestaTemporaria;
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

    public boolean excluir(int idBebida){
        SQLiteDatabase db = null;

        try {
            db = conexaoSQLite.getWritableDatabase();
            db.delete(TABLE_CESTA, "ID = ?",  new String[]{String.valueOf(idBebida)});

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


}
