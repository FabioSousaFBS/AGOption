package agoption.projetos.com.agoption.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.pm.ActivityInfoCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean ValidaPermissao(int requestCode, Activity activity, String[] permissoes){

        if(Build.VERSION.SDK_INT >= 23){
            List<String> listapermissoes = new ArrayList<>();

            for(String permissao : permissoes){
                boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;

                if(!validaPermissao){
                    listapermissoes.add(permissao);
                }
            }

            //SE A LISTA ESTIVER VAZIA, SIGNIFICA QUE TEM TODAS AS PERMISSOES
            if(listapermissoes.isEmpty())
                return true;

            String[] novasPermissoes = new String[listapermissoes.size()];
            listapermissoes.toArray(novasPermissoes);

            //SOLICITAR NOVAS PERMISSÕES AO USUÁRIO
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);

        }

        return true;
    }


}
