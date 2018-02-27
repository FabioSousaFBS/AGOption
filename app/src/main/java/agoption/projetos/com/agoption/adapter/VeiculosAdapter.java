package agoption.projetos.com.agoption.adapter;


import agoption.projetos.com.agoption.model.Carros;
import agoption.projetos.com.agoption.R;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class VeiculosAdapter extends ArrayAdapter<Carros>{


    private ArrayList<Carros> carros;
    private Context context;


    public VeiculosAdapter(Context c, ArrayList<Carros> objects) {
        super(c, 0, objects);

        this.context = c;
        this.carros = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View view = null;

        if(carros != null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_veiculos, parent, false);

            TextView idVeiculo = (TextView) view.findViewById(R.id.txtIdVeiculo);
            TextView nomeVeiculo = (TextView) view.findViewById(R.id.txtNomeVeiculo);
            TextView fatorVeiculo = (TextView) view.findViewById(R.id.txtFator);
            TextView consumoEtanol = (TextView) view.findViewById(R.id.txtConsumoEtanol);
            TextView consumoGasolina = (TextView) view.findViewById(R.id.txtConsumoGasolina);

            Carros veiculo = carros.get(position);
            idVeiculo.setText( "Cod. " + String.valueOf(veiculo.getIdCarro()));
            nomeVeiculo.setText(veiculo.getDescricao());
            fatorVeiculo.setText("Fator: " + String.valueOf(veiculo.getFator()) );
            consumoEtanol.setText("Consumo Etanol: " + String.valueOf(veiculo.getCmEtanol()) + " Km/L" );
            consumoGasolina.setText("Consumo Gasolina: " + String.valueOf(veiculo.getCmGasolina()) + " Km/L");

        }

         return view;
    }
}
