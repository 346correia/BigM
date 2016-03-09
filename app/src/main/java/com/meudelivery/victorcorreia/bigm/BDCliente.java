package com.meudelivery.victorcorreia.bigm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toner e Tintas on 13/02/2016.
 */
public class BDCliente {
    private SQLiteDatabase db;

    public BDCliente(Context context){
        BDClienteCore auxBd = new BDClienteCore(context);
        db = auxBd.getWritableDatabase();
    }


    public void inserir(Cliente cliente){
        ContentValues valores = new ContentValues();
        valores.put("idFb", cliente.getIdFb());
        valores.put("nome", cliente.getNome());
        valores.put("email", cliente.getEmail());
        valores.put("fone", cliente.getFone());
        valores.put("senha", cliente.getSenha());
        valores.put("bairro", cliente.getBairro());
        valores.put("rua", cliente.getRua());
        valores.put("numero", cliente.getNumero());
        valores.put("complemento", cliente.getComplemento());
        valores.put("perimetro", cliente.getPerimetro());


        db.insert("cliente", null, valores);

    }

    public void atualizar(Cliente cliente){
        ContentValues valores = new ContentValues();
        valores.put("idFb", cliente.getIdFb());
        valores.put("nome", cliente.getNome());
        valores.put("email", cliente.getEmail());
        valores.put("fone", cliente.getFone());
        valores.put("senha", cliente.getSenha());
        valores.put("bairro", cliente.getBairro());
        valores.put("rua", cliente.getRua());
        valores.put("numero", cliente.getNumero());
        valores.put("complemento", cliente.getComplemento());
        valores.put("perimetro", cliente.getPerimetro());


        db.update("cliente", valores, "id = ? ",
                new String[]{"" + cliente.getId()});

    }

    public void delete(Cliente cliente){
        db.delete("cliente", "id = " + cliente.getId(), null);

    }

    public List<Cliente> buscar(){
        List<Cliente> list = new ArrayList<Cliente>();
        String[] colunas = new String[]{"id","idFb","nome","email","fone","senha","bairro","rua","numero","complemento","perimetro"};

        Cursor cursor = db.query("cliente", colunas, null, null, null, null, "nome  ASC");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {

                Cliente cli = new Cliente();
                cli.setId(cursor.getInt(0));
                cli.setIdFb(cursor.getString(1));
                cli.setNome(cursor.getString(2));
                cli.setEmail(cursor.getString(3));
                cli.setFone(cursor.getString(4));
                cli.setSenha(cursor.getString(5));
                cli.setBairro(cursor.getString(6));
                cli.setRua(cursor.getString(7));
                cli.setNumero(cursor.getInt(8));
                cli.setComplemento(cursor.getString(9));
                cli.setPerimetro(cursor.getString(10));

                list.add(cli);

            } while (cursor.moveToNext());
        }

        return(list);

    }
}
