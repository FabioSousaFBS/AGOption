package agoption.projetos.com.agoption.helper;

import android.database.sqlite.SQLiteDatabase;

public final class ConfiguracaoBanco {

    private static SQLiteDatabase banco;

    public static SQLiteDatabase getBanco(){

        if(banco == null){
            banco = SQLiteDatabase.openOrCreateDatabase("DbAGOption", null);
            //banco = SQLiteDatabase.op
        }

        return banco;
    }

    //CRIAR TABELAS
    public static void criarTabelas(){

        banco.execSQL("CREATE TABLE IF NOT EXISTS Carros(" +
                "    idCarro INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    descricao VARCHAR(30)," +
                "    fator NUMERIC(10,2));");

        banco.execSQL("CREATE TABLE IF NOT EXISTS Historico(" +
                "    idHistorico INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    data DATETIME," +
                "    vlAlcool NUMERIC(10,2)," +
                "    vlGasolina NUMERIC(10,2)," +
                "    idCarro_fk INTEGER," +
                "    melhorOP VARCHAR(10)," +
                "    fator NUMERIC(10,2)," +
                "    FOREIGN KEY (idCarro_fk) REFERENCES Carros(idCarro));");
    }

}
