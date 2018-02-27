package agoption.projetos.com.agoption.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String BANCO_DADOS = "AGOption";
    private static final int VERSAO = 2;

    private String strQuery = "";

    public DatabaseHelper(Context context) {
        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        strQuery = "CREATE TABLE IF NOT EXISTS Carros(";
        strQuery +=  "    idCarro INTEGER PRIMARY KEY AUTOINCREMENT,";
        strQuery +=  "    descricao VARCHAR(30),";
        strQuery +=  "    cmEtanol NUMERIC(10,2),";
        strQuery +=  "    cmGasolina NUMERIC(10,2),";
        strQuery +=  "    fator NUMERIC(10,2));";

        db.execSQL(strQuery);

        //INSERINDO O VEICULO PADRÃO APÓS CRIAR A TABELA CARROS
        strQuery = " INSERT INTO  Carros(descricao, cmEtanol, cmGasolina, fator) ";
        strQuery +=  " SELECT temp.descricao, temp.cmEtanol, temp.cmGasolina, temp.fator FROM (SELECT 'Veículo Padrão' AS descricao, 0.0 AS cmEtanol, 0.0 AS cmGasolina, 0.70 AS fator) AS temp ";
        strQuery +=  " WHERE 0 = (SELECT COUNT(*) FROM Carros);";
        db.execSQL(strQuery);


        strQuery = "CREATE TABLE IF NOT EXISTS Historico(";
        strQuery +=  "    idHistorico INTEGER PRIMARY KEY AUTOINCREMENT,";
        strQuery +=  "    data DATETIME,";
        strQuery +=  "    vlAlcool NUMERIC(10,2),";
        strQuery +=  "    vlGasolina NUMERIC(10,2),";
        strQuery +=  "    idCarro_fk INTEGER,";
        strQuery +=  "    melhorOP VARCHAR(10),";
        strQuery +=  "    fator NUMERIC(10,2),";
        strQuery +=  "    FOREIGN KEY (idCarro_fk) REFERENCES Carros(idCarro));";
        db.execSQL(strQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
