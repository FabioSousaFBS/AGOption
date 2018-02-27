package agoption.projetos.com.agoption.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.Double2;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import agoption.projetos.com.agoption.R;
import agoption.projetos.com.agoption.helper.ConfiguracaoBanco;
import agoption.projetos.com.agoption.helper.DatabaseHelper;
import agoption.projetos.com.agoption.helper.InterpretaTexto;
import agoption.projetos.com.agoption.helper.Preferencias;
import agoption.projetos.com.agoption.util.Utilities;
import android.media.MediaPlayer;

//recursos de reconhecimento de voz
import android.speech.RecognizerIntent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private EditText edtPrecoAlcool;
    private EditText edtPrecoGasolina;
    private TextView tvResultado;
    private ImageView imgBtnVoz;
    private Button btnCalcular;
    private DatabaseHelper helper;
    private Toolbar toolbar;


    private static final Locale LOCAL = new Locale("pt","BR");

    private boolean bUsouComandoVoz = false;
    private MediaPlayer player;
    private AlertDialog.Builder dialog;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.CAPTURE_AUDIO_OUTPUT
    };

    //recurso de comando de voz
    private static final int REQUEST_CODE = 1234;
    private SensorManager sManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtPrecoAlcool = (EditText) findViewById(R.id.edtPrecoAlcool);
        edtPrecoGasolina = (EditText) findViewById(R.id.edtPrecoGasolina);
        tvResultado = (TextView) findViewById(R.id.txtResultado);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgBtnVoz = (ImageView) findViewById(R.id.imgComandoVoz_main);
        btnCalcular = (Button) findViewById(R.id.btnCalcular);

        //INSTANCIA DO BANCO
        helper = new DatabaseHelper(this);

        //CONFIGURA A TOOLBAR
        if (VerificaVeiculoPadrao() == false ){
            SQLiteDatabase db = helper.getReadableDatabase();
            String strQuery = "SELECT descricao, fator FROM Carros WHERE descricao = 'Veículo Padrão'";
            Cursor cursor = db.rawQuery(strQuery, null);
            cursor.moveToFirst();
            toolbar.setTitle(cursor.getString(0));

            //ATUALIZA AS PREFERENCIAS
            Preferencias preferencias = new Preferencias(MainActivity.this);
            preferencias.SalvarDados(cursor.getString(0), String.valueOf(cursor.getDouble(1)));

            cursor.close();
        }else{
            Preferencias preferencias = new Preferencias(MainActivity.this);

            toolbar.setTitle(preferencias.getVeiculoPadrao());
        }

        setSupportActionBar(toolbar);

        imgBtnVoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.esconderTeclado(MainActivity.this);
                executaComandoVoz();
                bUsouComandoVoz = false;

            }
        });

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FazerCalculoCombustivel();
            }
        });

        tvResultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.copiar(getApplicationContext(), tvResultado.getText().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.item_adicionar_veiculo:
                Intent intent = new Intent(MainActivity.this, VeiculosActivity.class);
                startActivity(intent);

                return true;
            case R.id.item_sair:
                finish();
                return true;
            case R.id.item_redefinir:
                RedefinirConfiguracoes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean VerificaVeiculoPadrao(){
        String strVeiculo;
        Preferencias preferencias = new Preferencias(MainActivity.this);
        strVeiculo = preferencias.getVeiculoPadrao();

        if (strVeiculo == null){
            return false;
        }

        return true;
    }

    public void FazerCalculoCombustivel(){

        double dblResultado;
        double dblPrecoEtanol;
        double dblPrecoGasolina;
        double dblFator;

        Preferencias preferencias = new Preferencias(MainActivity.this);

        dblFator = Double.parseDouble(preferencias.getFatorVeiculoPadrao());
        dblPrecoEtanol = Double.parseDouble(edtPrecoAlcool.getText().toString());
        dblPrecoGasolina = Double.parseDouble(edtPrecoGasolina.getText().toString());

        dblResultado = dblPrecoEtanol / dblPrecoGasolina;

        if (dblResultado < dblFator){
            if (bUsouComandoVoz == true) {
                player = MediaPlayer.create(MainActivity.this, R.raw.audio_etanol);
                TocarSom();
                bUsouComandoVoz = false;
            }
            tvResultado.setText("Abasteça com Etanol!");
        }else{
            if (bUsouComandoVoz == true) {
                player = MediaPlayer.create(MainActivity.this, R.raw.audio_gasolina);
                TocarSom();
                bUsouComandoVoz = false;
            }
            tvResultado.setText("Abasteça com Gasolina!");
        }
    }


    @Override
    public void onInit(int status) {
    }

    private void executaComandoVoz(){

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Informe os preços dos combustíveis. (Fale o nome do combustível e logo em seguida o preço)");
        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
           ArrayList<String> matches = data.getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS);
            //resultList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
            InterpretaTexto texto = new InterpretaTexto(matches.get(0).toString());

            edtPrecoAlcool.setText(texto.RetornaValor("etanol"));
            edtPrecoGasolina.setText(texto.RetornaValor("gasolina"));

            if (edtPrecoGasolina.getText().toString() != "" && edtPrecoAlcool.getText().toString() != ""){
                bUsouComandoVoz = true;
                FazerCalculoCombustivel();
            }else{
                Toast.makeText(this, "Não foi possível identificar o que foi dito. Repita o processo falando pausadamente.", Toast.LENGTH_SHORT).show();
            }



        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void TocarSom(){
        if(player != null){
            player.start();
        }
    }

    private void RedefinirConfiguracoes(){

        dialog = new AlertDialog.Builder(MainActivity.this);
        //configurar dialog
        dialog.setTitle("Redefinir configurações");
        dialog.setMessage("Este processo irá redefinir todas as configurações do aplicativo, deseja prosseguir?");

        dialog.setCancelable(false);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);

        dialog.setNegativeButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase banco;
                String strSql;
                banco = openOrCreateDatabase("AGOption", MODE_PRIVATE, null);

                strSql = "DROP TABLE Carros";
                banco.execSQL(strSql);

                strSql = "DROP TABLE Historico";
                banco.execSQL(strSql);

                strSql = "CREATE TABLE IF NOT EXISTS Carros(";
                strSql +=  "    idCarro INTEGER PRIMARY KEY AUTOINCREMENT,";
                strSql +=  "    descricao VARCHAR(30),";
                strSql +=  "    cmEtanol NUMERIC(10,2),";
                strSql +=  "    cmGasolina NUMERIC(10,2),";
                strSql +=  "    fator NUMERIC(10,2));";

                banco.execSQL(strSql);

                //INSERINDO O VEICULO PADRÃO APÓS CRIAR A TABELA CARROS
                strSql = " INSERT INTO  Carros(descricao, cmEtanol, cmGasolina, fator) ";
                strSql +=  " SELECT temp.descricao, temp.cmEtanol, temp.cmGasolina, temp.fator FROM (SELECT 'Veículo Padrão' AS descricao, 0.0 AS cmEtanol, 0.0 AS cmGasolina, 0.70 AS fator) AS temp ";
                strSql +=  " WHERE 0 = (SELECT COUNT(*) FROM Carros);";
                banco.execSQL(strSql);


                strSql = "CREATE TABLE IF NOT EXISTS Historico(";
                strSql +=  "    idHistorico INTEGER PRIMARY KEY AUTOINCREMENT,";
                strSql +=  "    data DATETIME,";
                strSql +=  "    vlAlcool NUMERIC(10,2),";
                strSql +=  "    vlGasolina NUMERIC(10,2),";
                strSql +=  "    idCarro_fk INTEGER,";
                strSql +=  "    melhorOP VARCHAR(10),";
                strSql +=  "    fator NUMERIC(10,2),";
                strSql +=  "    FOREIGN KEY (idCarro_fk) REFERENCES Carros(idCarro));";
                banco.execSQL(strSql);

                Preferencias preferencias = new Preferencias(MainActivity.this);
                preferencias.SalvarDados("Veículo Padrão" , "0.70");

                Toast.makeText(MainActivity.this, "Processo concluído com sucesso!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setPositiveButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        helper.close();

        if (player != null){
            player.release();
            player = null;
        }

        super.onDestroy();
    }

}
