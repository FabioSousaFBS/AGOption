package agoption.projetos.com.agoption.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import agoption.projetos.com.agoption.R;
import agoption.projetos.com.agoption.adapter.VeiculosAdapter;
import agoption.projetos.com.agoption.helper.DatabaseHelper;
import agoption.projetos.com.agoption.helper.Preferencias;
import agoption.projetos.com.agoption.model.Carros;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class VeiculosActivity extends AppCompatActivity {

    private ListView listView;
    private Toolbar toolbar;

    private ArrayList<Carros> carros;
    private ArrayAdapter<Carros> adapter;
    private ArrayList<Integer> item;

    private SQLiteDatabase banco;
    private DatabaseHelper helper;

    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculos);

        toolbar = (Toolbar) findViewById(R.id.toolbarVeiculos);
        listView = (ListView) findViewById(R.id.lsvVeiculos);
        listView.setDivider(null);

        toolbar.setTitle("Lista de Veiculos");
        toolbar.setNavigationIcon(R.drawable.ic_voltar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recuperaVeiculos();

        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Carros carro = carros.get(position);
                EditarExcluirItem(carro);
                return false;
            }
        });

    }

   /* private Carros getCarro(Integer id){

        Carros carro = new Carros();
        helper = new DatabaseHelper(this);
        banco = helper.getReadableDatabase();

        Cursor cursor = banco.rawQuery("SELECT * FROM Carros WHERE idCarro = " + id, null);
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++) {
            carro.setIdCarro(cursor.getInt(cursor.getColumnIndex("idCarro")));
            carro.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            carro.setCmEtanol(cursor.getDouble(cursor.getColumnIndex("cmEtanol")));
            carro.setCmGasolina(cursor.getDouble(cursor.getColumnIndex("cmGasolina")));
            carro.setFator(cursor.getDouble(cursor.getColumnIndex("fator")));
        }

        return carro;
    }*/

    private void recuperaVeiculos(){
        try{

            carros = new ArrayList<>();
            adapter = new VeiculosAdapter(VeiculosActivity.this, carros);
            listView.setAdapter(adapter);

            carros.clear();

            //INSTANCIA DO BANCO
            helper = new DatabaseHelper(this);

            //BANCO DE DADOS
            banco = helper.getReadableDatabase();
            String strQuery = "SELECT * FROM Carros";
            Cursor cursor = banco.rawQuery(strQuery, null);
            cursor.moveToFirst();

            for(int i = 0; i < cursor.getCount(); i++){

                int indexID = cursor.getColumnIndex("idCarro");
                int indexDS = cursor.getColumnIndex("descricao");
                int indexETANOL = cursor.getColumnIndex("cmEtanol");
                int indexGASOLINA = cursor.getColumnIndex("cmGasolina");
                int indexFATOR = cursor.getColumnIndex("fator");

                Carros carro = new Carros(
                        cursor.getInt(indexID),
                        cursor.getString(indexDS),
                        cursor.getDouble(indexETANOL),
                        cursor.getDouble(indexGASOLINA),
                        cursor.getDouble(indexFATOR)
                );

                carros.add(carro);

                cursor.moveToNext();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.veiculos_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.item_adicionar_veiculo:
                Intent intent = new Intent(VeiculosActivity.this, CadastroVeiculoActivity.class);
                startActivity(intent);

                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void EditarExcluirItem(final Carros carro){

        dialog = new AlertDialog.Builder(VeiculosActivity.this);



        //configurar dialog
        dialog.setTitle(carro.getDescricao());
        dialog.setMessage("Opções");

        dialog.setCancelable(false);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);

        dialog.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(VeiculosActivity.this, CadastroVeiculoActivity.class);
                /*intent.putExtra("nome_carro", carro.getDescricao());
                intent.putExtra("CE_carro", carro.getCmEtanol());
                intent.putExtra("CG_carro", carro.getCmGasolina());
                intent.putExtra("ID_carro", carro.getIdCarro());*/

                intent.putExtra("objeto", carro);

                startActivity(intent);
            }
        });
        dialog.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RemoverVeiculo(carro);
            }
        });
        dialog.create();
        dialog.show();
    }

    //parei aqui
    private void RemoverVeiculo(Carros carro){
        try{

            //Verifica se o veículo excluido é o veículo padrão
            String strVeiculo;
            Preferencias preferencias = new Preferencias(VeiculosActivity.this);
            strVeiculo = preferencias.getVeiculoPadrao();

            if (carro.getDescricao() != "Veículo Padrão") { //VEÍCULO PADRÃO NÃO PODE SER EXCLUIDO

                if (strVeiculo != null) {
                    if (strVeiculo.equals(carro.getDescricao())) {

                        SQLiteDatabase db = helper.getReadableDatabase();
                        String strQuery = "SELECT descricao, fator FROM Carros WHERE descricao = 'Veiculo Padrão'";
                        Cursor cursor = db.rawQuery(strQuery, null);
                        cursor.moveToFirst();

                       //ATUALIZA AS PREFERENCIAS
                        preferencias.SalvarDados(cursor.getString(0), String.valueOf(cursor.getDouble(1)));

                        cursor.close();
                    }
                }

                banco.execSQL("DELETE FROM Carros WHERE idCarro = "+ carro.getIdCarro());
                Toast.makeText(VeiculosActivity.this, "Veículo removido com sucesso!", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(VeiculosActivity.this, "O veículo padrão não pode ser removido", Toast.LENGTH_SHORT).show();
            }

            recuperaVeiculos();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        helper.close();
        super.onStop();
    }

    @Override
    protected void onRestart() {

        recuperaVeiculos();

        super.onRestart();
    }
}
