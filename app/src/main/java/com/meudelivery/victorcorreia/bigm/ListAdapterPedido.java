package com.meudelivery.victorcorreia.bigm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Toner e Tintas on 04/02/2016.
 */
public class ListAdapterPedido extends ArrayAdapter<ItemPedido> {

    private Context context;
    private List<ItemPedido> lista;
    private DecimalFormat df;

    public ListAdapterPedido(Context context, List<ItemPedido> lista) {
        super(context, 0, lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        df = new DecimalFormat("0.00");

        ItemPedido itemPosition = this.lista.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.cada_item_pedido, null);

        TextView txtNome = (TextView) convertView.findViewById(R.id.txtNome);
        txtNome.setText(itemPosition.getNome()+" "+itemPosition.getObservacao());

        TextView txtValor = (TextView) convertView.findViewById(R.id.txtValor);
        txtValor.setText("R$ " + df.format(Double.parseDouble(itemPosition.getValor()) * itemPosition.getQuant()));

        TextView txtQuant = (TextView) convertView.findViewById(R.id.txtQuant);
        txtQuant.setText(String.valueOf(itemPosition.getQuant()));

        return convertView;
    }

}
