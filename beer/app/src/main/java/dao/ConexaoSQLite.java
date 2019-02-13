package dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexaoSQLite extends SQLiteOpenHelper {

    private static ConexaoSQLite INSTANCIA_CONEXAO;
    private static final int VERSAO_DB = 1;
    private static final String NOME_BD = "beer";

    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Bebidas (ID INTEGER PRIMARY KEY AUTOINCREMENT, Fabricante TEXT NOT NULL, " +
            "Mililitros REAL, Estabelecimento TEXT, Preço REAL NOT NULL);";

    private final String CREATE_TABLE2 = "CREATE TABLE IF NOT EXISTS Estabelecimento (ID INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT NOT NULL, " +
            "Endereço TEXT);";

    private final String CREATE_TABLE3 = "CREATE TABLE IF NOT EXISTS Cesta (ID INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT NOT NULL);";

    private final String CREATE_TABLE4 = "CREATE TABLE IF NOT EXISTS Item (ID_CESTA INTEGER, ID_BEBIDA INTEGER," +
            "FOREIGN KEY (ID_CESTA ) REFERENCES Cesta(ID ), " +
            "FOREIGN KEY (ID_BEBIDA) REFERENCES Bebidas(ID));";

    public ConexaoSQLite(Context context) {
        super(context, NOME_BD, null, VERSAO_DB);
    }

    public static ConexaoSQLite getInstance(Context context){

        if(INSTANCIA_CONEXAO == null){
            INSTANCIA_CONEXAO = new ConexaoSQLite(context);
        }
        return INSTANCIA_CONEXAO;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE2);
        db.execSQL(CREATE_TABLE3);
        db.execSQL(CREATE_TABLE4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
