package com.meudelivery.victorcorreia.bigm;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.Time;

import java.text.DecimalFormat;
import java.util.List;

public class TelaPedido extends AppCompatActivity implements View.OnClickListener {
    private AlertDialog alerta;
    private int quant;
    private TextView txtQuant;
    private TextView txtTotal;
    private TextView txtTaxaTxt;
    private TextView txtTaxa;
    private TextView txtTotalTxt;
    private List<ItemPedido> lista;
    private BDPedido bdPedido;
    private Button btnAdd;
    private Button btnPag;
    private Double total = 0.0;
    private DecimalFormat df;
    private int taxa = 7;

    private ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_pedido);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Seu Pedido");
        toolbar.setSubtitle("Aperte no item p/ alterar sua quantidade");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTaxa = (TextView) findViewById(R.id.txtTaxa);
        txtTaxaTxt = (TextView) findViewById(R.id.txtTaxaTxt);
        txtTotalTxt = (TextView) findViewById(R.id.txtTotalTxt);
        txtTotal = (TextView) findViewById(R.id.txtTotal);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnPag = (Button) findViewById(R.id.btnPag);
        btnPag.setOnClickListener(this);

        bdPedido = new BDPedido(this);

        lista = bdPedido.buscar();

        if(lista.isEmpty()){

            txtTaxa.setVisibility(View.INVISIBLE);
            txtTaxaTxt.setVisibility(View.INVISIBLE);
            txtTotalTxt.setVisibility(View.INVISIBLE);
            txtTotal.setVisibility(View.INVISIBLE);

        }

        ListAdapterPedido adapterPedido = new ListAdapterPedido(this, lista);

        ListView listView = (ListView) findViewById(R.id.lstPedido);

        listView.setAdapter(adapterPedido);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.quant_item_dialog, null);
                ImageButton btnMenos = (ImageButton) layout.findViewById(R.id.btnMenos);
                final TextView txtQuant = (TextView) layout.findViewById(R.id.txtQtd);
                ImageButton btnMais = (ImageButton) layout.findViewById(R.id.btnMais);

                txtQuant.setText(String.valueOf(lista.get(position).getQuant()));

                btnMenos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quant = Integer.parseInt(txtQuant.getText().toString());
                        if (quant > 0) {
                            quant--;
                            txtQuant.setText(String.valueOf(quant));
                        }
                    }
                });

                btnMais.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quant = Integer.parseInt(txtQuant.getText().toString());
                        quant++;
                        txtQuant.setText(String.valueOf(quant));
                    }
                });

                new AlertDialog.Builder(TelaPedido.this)
                        .setView(layout)
                        .setMessage("Altere a quantidade do item")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                quant = Integer.parseInt(txtQuant.getText().toString());
                                if (quant == 0) {
                                    deleteItem(position);
                                    restartActivity();
                                } else {
                                    lista.get(position).setQuant(quant);
                                    bdPedido.atualizar(lista.get(position));
                                    restartActivity();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing


                            }
                        }).show();


            }
        });


        for (int i = 0; i < lista.size(); i++) {
            total = total + (Double.parseDouble(lista.get(i).getValor()) * lista.get(i).getQuant());

        }

        df = new DecimalFormat("0.00");
        txtTotal.setText("R$ " + df.format(total + taxa));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new MyAsyncTask().execute();
            //startActivity(new Intent(TelaPedido.this, MainActivity.class));

            finish();
        }
        return true;
    }

    public void restartActivity() {
        Intent mIntent = getIntent();
        finish();
        startActivity(mIntent);
    }

    public void deleteItem(int n) {
        BDPedido bdPed = new BDPedido(this);
        bdPed.delete(lista.get(n));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnAdd:

                new MyAsyncTask().execute();

                //startActivity(new Intent(this, MainActivity.class));
                finish();

                break;

            case R.id.btnPag:

                BDPedido bdPedido = new BDPedido(this);
                List<ItemPedido> auxListPed;
                auxListPed = bdPedido.buscar();

                if(auxListPed.isEmpty()){

                    Toast.makeText(TelaPedido.this, "Adicione itens ao seu pedido", Toast.LENGTH_SHORT).show();

                }else {

                    BDCliente bdCliente = new BDCliente(this);
                    List<Cliente> auxList;
                    auxList = bdCliente.buscar();

                    if (auxList.isEmpty()) {
                        startActivity(new Intent(this, TelaLogin.class));

                    } else if (String.valueOf(auxList.get(0).getRua()).equals("")) {
                        startActivity(new Intent(this, TelaEndereco.class));

                    } else {

                        Time time = new Time();
                        time.setToNow();

                        if(time.hour > 00 && time.hour < 18) {
                            Toast.makeText(TelaPedido.this, "Horário de funcionamento: das 18h às 00h", Toast.LENGTH_LONG).show();
                        }else{
                            startActivity(new Intent(this, TelaFecharPedido.class));
                        }
                    }
                }

                break;
        }
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TelaPedido.this, "Aguarde", "Carregando...", true, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Intent it = new Intent(TelaPedido.this, MainActivity.class);
            startActivity(it);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }

    }

    @Override
    public void onBackPressed() {

        new MyAsyncTask().execute();
        //startActivity(new Intent(this, MainActivity.class));
        finish();
    }


}
