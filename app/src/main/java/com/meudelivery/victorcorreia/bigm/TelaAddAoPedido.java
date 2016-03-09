package com.meudelivery.victorcorreia.bigm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class TelaAddAoPedido extends AppCompatActivity implements View.OnClickListener{

    private TextView txtNome;
    private TextView txtDesc;
    private TextView txtValor;
    private TextView txtQuant;
    private TextView txtTotal;
    private ImageButton btnMenos;
    private ImageButton btnMais;
    private EditText edtObs;
    private Button btnPedido;
    private int quant = 1;
    private double total;
    private DecimalFormat df;
    private ItemPedido itemPedido;
    private List<Item> itemBd;
    private int id;
    private String nome = new String();
    private String desc = new String();
    private double valorDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_add_ao_pedido);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Adicionar Item");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNome = (TextView) findViewById(R.id.txtNome);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtValor = (TextView) findViewById(R.id.txtValor);
        txtQuant = (TextView) findViewById(R.id.txtQuant);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        btnMenos = (ImageButton) findViewById(R.id.btnMenos);
        btnMais = (ImageButton) findViewById(R.id.btnMais);
        btnPedido = (Button) findViewById(R.id.btnPedido);
        edtObs = (EditText)findViewById(R.id.edtObs);

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("IDITEM");

        BDItem bdItem = new BDItem(this);

        itemBd = bdItem.buscar();

        for (int i = 0; i<itemBd.size(); i++){

            if (itemBd.get(i).getId() == id){
                nome = itemBd.get(i).getNome();
                desc = itemBd.get(i).getDescricao();
                valorDouble = itemBd.get(i).getValor();

            }
        }


        txtNome.setText(nome);
        txtDesc.setText(desc);
        df = new DecimalFormat("0.00");
        txtValor.setText("R$ "+df.format(valorDouble).toString());

        total = quant*valorDouble;
        txtTotal.setText("R$ " + df.format(total).toString());

        btnMenos.setOnClickListener(this);
        btnMais.setOnClickListener(this);
        btnPedido.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnMenos:

                if(quant > 1){

                    quant--;
                }

                txtQuant.setText(String.valueOf(quant));

                total = quant * valorDouble;
                txtTotal.setText("R$ "+df.format(total).toString());

                break;

            case R.id.btnMais:

                quant++;
                txtQuant.setText(String.valueOf(quant));

                total = quant * valorDouble;
                txtTotal.setText("R$ "+df.format(total).toString());

                break;

            case R.id.btnPedido:

                ItemPedido ip = new ItemPedido();

                ip.setIdItem(id);
                ip.setNome(nome);
                ip.setValor(String.valueOf(valorDouble));
                ip.setObservacao(edtObs.getText().toString());
                ip.setQuant(quant);

                BDPedido bdPedido = new BDPedido(this);
                bdPedido.inserir(ip);

                Intent it = new Intent(this, TelaPedido.class);
                startActivity(it);
                finish();

                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}
