package com.meudelivery.victorcorreia.bigm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toner e Tintas on 03/02/2016.
 */
public class BDItem {
    private SQLiteDatabase db;

    public BDItem(Context context){
        BDItemCore auxBd = new BDItemCore(context);
        db = auxBd.getWritableDatabase();
    }

    public void inserir(Item item){

        ContentValues valores = new ContentValues();
        valores.put("id", item.getId());
        valores.put("nome", item.getNome());
        valores.put("descricao", item.getDescricao());
        valores.put("valor", item.getValor());
        valores.put("idCategoria", item.getIdCategoria());

        db.insert("item", null, valores);

    }

    public void atualizar(Item item){
        ContentValues valores = new ContentValues();
        valores.put("id", item.getId());
        valores.put("nome", item.getNome());
        valores.put("descricao", item.getDescricao());
        valores.put("valor", item.getValor());
        valores.put("idCategoria", item.getIdCategoria());

        db.update("item", valores, "id = ? ",
                new String[]{"" + item.getId()});

    }

    public void delete(Item item){
        db.delete("item", "id = " + item.getId(), null);

    }

    public List<Item> buscar(){
        List<Item> list = new ArrayList<Item>();
        String[] colunas = new String[]{"id","nome","descricao","valor","idCategoria"};

        Cursor cursor = db.query("item", colunas, null, null, null, null, "id ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {

                Item i = new Item();
                i.setId(cursor.getInt(0));
                i.setNome(cursor.getString(1));
                i.setDescricao(cursor.getString(2));
                i.setValor(cursor.getDouble(3));
                i.setIdCategoria(cursor.getInt(4));

                list.add(i);

            } while (cursor.moveToNext());
        }

        return(list);

    }
}
