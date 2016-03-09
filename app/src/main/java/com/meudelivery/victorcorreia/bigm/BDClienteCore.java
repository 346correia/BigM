package com.meudelivery.victorcorreia.bigm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Toner e Tintas on 13/02/2016.
 */
public class BDClienteCore extends SQLiteOpenHelper {

    private static final String NOME_BD = "cliente";
    private static final int VERSAO_BD = 2;

    public BDClienteCore (Context context) {

        super(context, NOME_BD, null, VERSAO_BD);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cliente(id integer primary key, " +
                "idFb integer, " +
                "nome text not null, " +
                "email text, " +
                "fone text, " +
                "senha text, " +
                "bairro text, " +
                "rua text, " +
                "numero integer, " +
                "complemento text, " +
                "perimetro text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table cliente;");
        onCreate(db);

    }
}
