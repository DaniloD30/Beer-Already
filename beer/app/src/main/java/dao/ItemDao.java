package dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.Bebida;

public class ItemDao  {
        private final String TABLE_CESTA = "Item";


        private  final ConexaoSQLite conexaoSQLite;
        private BebidaDao daoBebida ;

        public ItemDao(ConexaoSQLite conexao){
            conexaoSQLite = conexao;
        }

    // ID_CESTA INTEGER, ID_BEBIDA
        public boolean salvar(int idCesta, int idBebida){
            SQLiteDatabase db = conexaoSQLite.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put("ID_CESTA", idCesta);
            cv.put("ID_BEBIDA", idBebida);

            return db.insert(TABLE_CESTA, null, cv) > 0;
        }


        public List<Bebida> retornarBebidaByIdCesta(int id){

            SQLiteDatabase db = null;
            List<Bebida> bebidasCesta = new ArrayList<>();
            Cursor cursor;
            daoBebida = new BebidaDao(conexaoSQLite);
          //  Cursor cursorBebida;

            try {
                db = conexaoSQLite.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM Item WHERE ID_CESTA = ?", new String[]{String.valueOf(id)});
               // cursorBebida = db.rawQuery("SELECT * FROM Bebida", null);
                if(cursor.moveToFirst()) {
                    Bebida bebidaTemporaria = null;
                    do{
                        bebidaTemporaria = new Bebida();
                        bebidaTemporaria.setIdCesta(cursor.getInt(0)); // de qual cesta a bebida pertence
                        bebidaTemporaria.setId(cursor.getInt(1));
                       // daoBebida.retornarBebidaById(bebidaTemporaria.getId());
                        bebidasCesta.add(daoBebida.retornarBebidaById(bebidaTemporaria.getId()));
                        Log.d("ERRO LISTA PRODUTOS", "ID DA BEBIDA: " + bebidaTemporaria.getId());
                      //  bebidaTemporaria.addId();
                    }while(cursor.moveToNext());
                }
            }catch (Exception e){
                Log.d("ERRO LISTA PRODUTOS", "ERRO AO RETORNAR PRODUTOS");
            }finally {
                if (db != null) {
                    db.close();
                }
            }

            return bebidasCesta;
        }

    public boolean excluir(int idBebida){
        SQLiteDatabase db = null;

        try {
            db = conexaoSQLite.getWritableDatabase();
            db.delete(TABLE_CESTA, "ID_BEBIDA = ?",  new String[]{String.valueOf(idBebida)});

            // Cursor cursor = gw.getDatabase().rawQuery("DELETE FROM Bebidas WHERE ID = idBebida ", null);
            return true;
        } catch(Exception e){
            Log.d("ItemDAO", "NÃO FOI POSSIVEL DELETAR A BEBIDA");
            return false;
        } finally {
            if( db != null){
                db.close();
            }
        }
    }
}