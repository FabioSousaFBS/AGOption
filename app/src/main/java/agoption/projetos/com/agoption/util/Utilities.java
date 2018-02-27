package agoption.projetos.com.agoption.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static android.telecom.DisconnectCause.LOCAL;

public final class Utilities {

    private static final Locale LOCAL = new Locale("pt","BR");

    public static void copiar(Context context,String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Texto copiado: ", text);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(context, "Texto copiado", Toast.LENGTH_LONG).show();
    }

    public static void esconderTeclado(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String formataNumero(String numero){

        //Aplica máscara de formatação para numeros decimais e passa o local com parâmetro
        DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(LOCAL));
        String resultado = df.format(numero);

        return resultado;
    }


}
