package dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Beer.db";
    private static final int DATABASE_VERSION = 1;
    //private final String CREATE_TABLE = "CREATE TABLE Clientes (ID INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT NOT NULL, " +
      //                                      "Sexo TEXT, UF TEXT NOT NULL, Vip INTEGER NOT NULL);";
    private final String CREATE_TABLE = "CREATE TABLE Bebidas (ID INTEGER PRIMARY KEY AUTOINCREMENT, Fabricante TEXT NOT NULL, " +
            "Mililitros REAL, Estabelecimento TEXT, Preço REAL NOT NULL);";

    private final String CREATE_TABLE2 = "CREATE TABLE Estabelecimento (ID INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT NOT NULL, " +
            "Endereço TEXT);";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL(CREATE_TABLE);
       // db.execSQL(CREATE_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
