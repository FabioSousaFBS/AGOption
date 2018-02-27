package agoption.projetos.com.agoption.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

public class Preferencias {

    private Context context;
    private SharedPreferences preferences;

    private final String NOME_ARQUIVO = "agoption.preferencias";
    private final int MODE = 0;

    private SharedPreferences.Editor editor;

    private final String CHAVE_VEICULO_PADRAO = "identificadorVeiculoPadrao";
    private final String CHAVE_FATOR_VEICULO_PADRAO = "0.70";

    public Preferencias(Context contextoParametro){
        context = contextoParametro;
        preferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void SalvarDados(String identificadorVeiculoPadrao, String fator) {

        editor.putString(CHAVE_VEICULO_PADRAO, identificadorVeiculoPadrao);
        editor.putString(CHAVE_FATOR_VEICULO_PADRAO, fator);

        editor.commit();

    }


    public String getVeiculoPadrao(){
        return preferences.getString(CHAVE_VEICULO_PADRAO, null);
    }


    public String getFatorVeiculoPadrao(){
        return preferences.getString(CHAVE_FATOR_VEICULO_PADRAO, null);
    }


}
