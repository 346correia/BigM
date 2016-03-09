package com.meudelivery.victorcorreia.bigm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Toner e Tintas on 31/01/2016.
 */
public class BDPedidoCore extends SQLiteOpenHelper {

    private static final String NOME_BD = "itemPedido";
    private static final int VERSAO_BD = 7;

    public BDPedidoCore(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table itemPedido(id integer primary key autoincrement, " +
                "idPedido integer, " +
                "idItem integer not null, " +
                "nome text not null, " +
                "valor text not null, " +
                "observacao text, " +
                "quantidade integer not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table itemPedido;");
        onCreate(db);

    }
}
