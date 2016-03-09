package com.meudelivery.victorcorreia.bigm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class TelaEndereco extends AppCompatActivity implements View.OnClickListener {

    private EditText edtBairro;
    private EditText edtRua;
    private EditText edtNumero;
    private EditText edtComplemento;
    private EditText edtPerimetro;
    private Button btnCadastrar;
    private String nome = new String();
    private String email = new String();
    private String fone = new String();
    private String senha = new String();
    private String bairro = new String();
    private String rua = new String();
    private String numero = new String();
    private String complemento = new String();
    private String perimetro = new String();
    private Cliente cliente = new Cliente();
    private List<Cliente> auxList;
    private List<ItemPedido> auxListPedido;
    private String ultimaTela = new String();

    private ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_endereco);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastre seu Endereço");
        setSupportActionBar(toolbar);

        edtBairro = (EditText) findViewById(R.id.edtBairro);
        edtRua = (EditText) findViewById(R.id.edtRua);
        edtNumero = (EditText) findViewById(R.id.edtNumero);
        edtComplemento = (EditText) findViewById(R.id.edtComplemento);
        edtPerimetro = (EditText) findViewById(R.id.edtPerimetro);

        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(this);

        BDCliente bdCliente = new BDCliente(this);
        auxList = bdCliente.buscar();
        cliente = auxList.get(0);

        System.out.println(auxList.get(0).getId());
        System.out.println(auxList.get(0).getIdFb());
        System.out.println(auxList.get(0).getNome());
        System.out.println(auxList.get(0).getEmail());
        System.out.println(auxList.get(0).getFone());

        BDPedido bdPedido = new BDPedido(this);
        auxListPedido = bdPedido.buscar();

        if (String.valueOf(auxList.get(0).getRua()).isEmpty() || auxList.get(0).getRua() == null) {


        } else {
            edtBairro.setText(auxList.get(0).getBairro());
            edtRua.setText(auxList.get(0).getRua());

            if (auxList.get(0).getNumero() == 0) {
                edtNumero.setText("");
            } else {
                edtNumero.setText(String.valueOf(auxList.get(0).getNumero()));
            }

            edtComplemento.setText(auxList.get(0).getComplemento());
            edtPerimetro.setText(auxList.get(0).getPerimetro());

            ultimaTela = "TelaFecharPedido";
            btnCadastrar.setText("ALTERAR ENDEREÇO");
            toolbar.setTitle("Altere seu Endereço");

        }

    }

    @Override
    public void onClick(View v) {

        bairro = edtBairro.getText().toString();
        rua = edtRua.getText().toString();
        numero = edtNumero.getText().toString();
        complemento = edtComplemento.getText().toString();
        perimetro = edtPerimetro.getText().toString();

        if (bairro.equals("") || rua.equals("") || numero.equals("") || complemento.equals("") || perimetro.equals("")) {

            Toast.makeText(TelaEndereco.this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();

        } else {

            cliente.setBairro(bairro);
            cliente.setRua(rua);
            cliente.setNumero(Integer.parseInt(numero));
            cliente.setComplemento(complemento);
            cliente.setPerimetro(perimetro);

            if (ultimaTela.equals("TelaFecharPedido")) {

                BDCliente bdCliente = new BDCliente(this);
                bdCliente.atualizar(cliente);

                startActivity(new Intent(TelaEndereco.this, TelaFecharPedido.class));
                finish();

            } else {

                new MyAsyncTask().execute();

            }

        }

    }

    @Override
    public void onBackPressed() {

        BDClienteCore auxCliCore = new BDClienteCore(this);
        SQLiteDatabase db = auxCliCore.getWritableDatabase();
        db.execSQL("DELETE FROM cliente");

        LoginManager.getInstance().logOut();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(TelaEndereco.this);
            pd.setTitle("Aguarde");
            pd.setMessage("Carregando...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://webservicevictor.16mb.com/android/insert_cliente_post.php");

            try {

                ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
                valores.add(new BasicNameValuePair("idFb", cliente.getIdFb()));
                valores.add(new BasicNameValuePair("nome", cliente.getNome()));
                valores.add(new BasicNameValuePair("email", cliente.getEmail()));
                valores.add(new BasicNameValuePair("fone", cliente.getFone()));
                valores.add(new BasicNameValuePair("senha", cliente.getSenha()));
                valores.add(new BasicNameValuePair("bairro", bairro));
                valores.add(new BasicNameValuePair("rua", rua));
                valores.add(new BasicNameValuePair("numero", numero));
                valores.add(new BasicNameValuePair("complemento", complemento));
                valores.add(new BasicNameValuePair("perimetro", perimetro));

                //Seu metodo
                valores.add(new BasicNameValuePair("method", "send-json"));

                httpPost.setEntity(new UrlEncodedFormEntity(valores));
                final HttpResponse resposta = httpClient.execute(httpPost);

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {

                            System.out.println(resposta.toString());
                            System.out.println("eta!");

                            BDCliente bdCliente = new BDCliente(TelaEndereco.this);
                            bdCliente.atualizar(cliente);

                            Toast toast = Toast.makeText(TelaEndereco.this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG);

                            startActivity(new Intent(TelaEndereco.this, MainActivity.class));

                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(TelaEndereco.this, "Erro", Toast.LENGTH_SHORT);
                toast.show();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pd.dismiss();
        }

    }
}
