package com.meudelivery.victorcorreia.bigm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Toner e Tintas on 03/02/2016.
 */
public class BDItemCore extends SQLiteOpenHelper {

    private static final String NOME_BD = "item";
    private static final int VERSAO_BD = 5;

    public BDItemCore(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table item(id integer primary key, " +
                "nome text not null, " +
                "descricao text, " +
                "valor text not null, " +
                "idCategoria integer not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table item;");
        onCreate(db);

    }
}
