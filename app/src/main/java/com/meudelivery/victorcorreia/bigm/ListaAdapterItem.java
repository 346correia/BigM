package com.meudelivery.victorcorreia.bigm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Toner e Tintas on 27/01/2016.
 */
public class ListaAdapterItem extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> lista;

    public ListaAdapterItem(Context context, ArrayList<Item> lista){
        super(context,0,lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item itemPosicao = this.lista.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.item, null);

        TextView txtNome = (TextView) convertView.findViewById(R.id.txtNome);
        txtNome.setText(itemPosicao.getNome());

        TextView txtDesc = (TextView) convertView.findViewById(R.id.txtDesc);
        txtDesc.setText(itemPosicao.getDescricao());

        TextView txtValor = (TextView) convertView.findViewById(R.id.txtValor);
        txtValor.setText(itemPosicao.getValor().toString());

        return convertView;
    }
}
