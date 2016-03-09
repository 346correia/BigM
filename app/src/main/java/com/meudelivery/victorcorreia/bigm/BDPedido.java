package com.meudelivery.victorcorreia.bigm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toner e Tintas on 31/01/2016.
 */
public class BDPedido {
    private SQLiteDatabase db;

    public BDPedido(Context context){
        BDPedidoCore auxBd = new BDPedidoCore(context);
        db = auxBd.getWritableDatabase();
    }

    public void inserir(ItemPedido itemPedido){
        ContentValues valores = new ContentValues();
        valores.put("idPedido", itemPedido.getIdPedido());
        valores.put("idItem", itemPedido.getIdItem());
        valores.put("nome", itemPedido.getNome());
        valores.put("valor", itemPedido.getValor());
        valores.put("observacao", itemPedido.getObservacao());
        valores.put("quantidade", itemPedido.getQuant());


        db.insert("itemPedido", null, valores);

    }

    public void atualizar(ItemPedido itemPedido){
        ContentValues valores = new ContentValues();
        valores.put("idPedido", itemPedido.getIdPedido());
        valores.put("idItem", itemPedido.getIdItem());
        valores.put("nome", itemPedido.getNome());
        valores.put("valor", itemPedido.getValor());
        valores.put("observacao", itemPedido.getObservacao());
        valores.put("quantidade", itemPedido.getQuant());


        db.update("itemPedido", valores, "id = ? ",
                new String[]{"" + itemPedido.getId()});

    }

    public void delete(ItemPedido itemPedido){
        db.delete("itemPedido", "id = " + itemPedido.getId(), null);

    }

    public List<ItemPedido> buscar(){
        List<ItemPedido> list = new ArrayList<ItemPedido>();
        String[] colunas = new String[]{"id","idPedido","idItem","nome","valor","observacao","quantidade"};

        Cursor cursor = db.query("itemPedido", colunas, null, null, null, null, "idItem  ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {

                ItemPedido iPedido = new ItemPedido();
                iPedido.setId(cursor.getInt(0));
                iPedido.setIdPedido(cursor.getInt(1));
                iPedido.setIdItem(cursor.getInt(2));
                iPedido.setNome(cursor.getString(3));
                iPedido.setValor(cursor.getString(4));
                iPedido.setObservacao(cursor.getString(5));
                iPedido.setQuant(cursor.getInt(6));

                list.add(iPedido);

            } while (cursor.moveToNext());
        }

        return(list);

    }
}
