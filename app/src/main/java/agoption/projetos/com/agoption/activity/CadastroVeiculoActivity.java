package agoption.projetos.com.agoption.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.support.v7.widget.Toolbar;

import agoption.projetos.com.agoption.R;
import agoption.projetos.com.agoption.helper.ConfiguracaoBanco;
import agoption.projetos.com.agoption.helper.Preferencias;
import agoption.projetos.com.agoption.model.Carros;

public class CadastroVeiculoActivity extends AppCompatActivity {

    private Button btnSalvarDados;
    private EditText edtNomeVeiculo;
    private EditText edtConsumoAlcool;
    private EditText edtConsumoGasolina;
    private TextView txtCalculoFator;
    private Toolbar toolbar;
    private CheckBox chkVeiculoPadrao;
    private SQLiteDatabase banco;
    private Bundle bundle;
    private Carros carro_aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_veiculo);

        btnSalvarDados = (Button) findViewById(R.id.btnSalvarDadosVeiculo);
        edtNomeVeiculo = (EditText) findViewById(R.id.edtCadNomeVeiculo);
        txtCalculoFator = (TextView) findViewById(R.id.txtCalculoFator);
        edtConsumoAlcool = (EditText) findViewById(R.id.edtCadConsulmoEtanol);
        edtConsumoGasolina= (EditText) findViewById(R.id.edtCadConsumoGasolina);
        toolbar = (Toolbar) findViewById(R.id.toolbarCadVeiculo);
        chkVeiculoPadrao = (CheckBox) findViewById(R.id.chkVeiculoPadrão);

        Intent extras = getIntent();
        carro_aux = (Carros) extras.getSerializableExtra("objeto");

        if (carro_aux != null ){
            edtNomeVeiculo.setText(carro_aux.getDescricao());
            edtConsumoAlcool.setText(String.valueOf(carro_aux.getCmEtanol()));
            edtConsumoGasolina.setText(String.valueOf(carro_aux.getCmGasolina()));
        }

        /*bundle = getIntent().getExtras();
        if (bundle != null){
            edtNomeVeiculo.setText(bundle.getString("nome_carro"));
            edtConsumoAlcool.setText(String.valueOf(bundle.getDouble("CE_carro")));
            edtConsumoGasolina.setText(String.valueOf(bundle.getDouble("CG_carro")));
        }*/

        //CONFIGURAR TOOBAR
        toolbar.setTitle("Cadastro de Veículos");
        toolbar.setNavigationIcon(R.drawable.ic_voltar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //BANCO DE DADOS
        banco = openOrCreateDatabase("AGOption", MODE_PRIVATE, null);
        //BOTÕES
        btnSalvarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalvarNovoVeiculo();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void SalvarNovoVeiculo(){

        //VERIFICA SE OS CAMPOS FORAM PREENCHIDOS
       if (bValidacoesCadastro() == false){
           Toast.makeText(CadastroVeiculoActivity.this, "Existem campos que não foram preenchidos, Verifique!", Toast.LENGTH_LONG).show();
       }else{

           double vlConsumoEtanol = Double.parseDouble(edtConsumoAlcool.getText().toString());
           double vlConsumoGasolina = Double.parseDouble(edtConsumoGasolina.getText().toString());
           double vlIndice = dblArredondar((vlConsumoEtanol / vlConsumoGasolina), 2, 0);

           String strSql;
            if(carro_aux != null ) {
                strSql = "UPDATE Carros SET descricao = '" + edtNomeVeiculo.getText().toString().trim() + "', "
                    + "cmEtanol = " + vlConsumoEtanol  + ", "
                + "cmGasolina = " + vlConsumoGasolina + ", "
                + "fator = " + vlIndice + " "
                + "WHERE idCarro = " + carro_aux.getIdCarro()  ;

            }else{
                strSql = "INSERT INTO  Carros(descricao, cmEtanol, cmGasolina, fator) VALUES('" + edtNomeVeiculo.getText().toString().trim() +"', "
                        + vlConsumoEtanol + ", " + vlConsumoGasolina + ", " + vlIndice + ")" ;
            }

           banco.execSQL(strSql);
           Toast.makeText(CadastroVeiculoActivity.this, "Veículo salvo com sucesso!", Toast.LENGTH_SHORT).show();

           if(chkVeiculoPadrao.isChecked()){
               Preferencias preferencias = new Preferencias(CadastroVeiculoActivity.this);
               preferencias.SalvarDados(edtNomeVeiculo.getText().toString().trim() , String.valueOf(vlIndice));
           }

           //LIMPA CAMPOS
           edtConsumoAlcool.setText("");
           edtNomeVeiculo.setText("");
           edtConsumoGasolina.setText("");
           chkVeiculoPadrao.setChecked(false);

       }

    }

    private boolean bValidacoesCadastro(){

        if (edtNomeVeiculo.getText().toString().isEmpty()) return false;
        if (edtConsumoAlcool.getText().toString().isEmpty()) return false;
        if (edtConsumoGasolina.getText().toString().isEmpty()) return false;

        return true;

    }
    //Parâmetros:
    /**
     * 	1 - Valor a arredondar.
     * 	2 - Quantidade de casas depois da vírgula.
     *  3 - Arredondar para cima ou para baixo?
     *  		Para cima = 0 (ceil)
     *  		Para baixo = 1 ou qualquer outro inteiro (floor)
     **/
    double dblArredondar(double valor, int casas, int ceilOrFloor) {
        double arredondado = valor;
        arredondado *= (Math.pow(10, casas));
        if (ceilOrFloor == 0) {
            arredondado = Math.ceil(arredondado);
        } else {
            arredondado = Math.floor(arredondado);
        }
        arredondado /= (Math.pow(10, casas));
        return arredondado;
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
