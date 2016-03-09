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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelaCadastro extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtFone;
    private EditText edtSenha;
    private EditText edtConfSenha;
    private Button btnCadastrar;
    private int id;
    private String nome = new String();
    private String email = new String();
    private String fone = new String();
    private String senha = new String();
    private String confSenha = new String();
    private List<Cliente> auxList;
    private Cliente cliente = new Cliente();
    private String ultimaTela = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastro");
        setSupportActionBar(toolbar);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtFone = (EditText) findViewById(R.id.edtFone);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        edtConfSenha = (EditText) findViewById(R.id.edtConfSenha);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(this);

        BDCliente bdCliente = new BDCliente(this);
        auxList = bdCliente.buscar();

        if(auxList.isEmpty()){

        } else if (auxList.get(0).getIdFb().isEmpty() || auxList.get(0).getIdFb() == null ) {


        } else {

            edtNome.setVisibility(View.GONE);
            edtSenha.setVisibility(View.GONE);
            edtConfSenha.setVisibility(View.GONE);

            ultimaTela = "CadastroFacebook";
            btnCadastrar.setText("COMPLETAR CADASTRO");
            toolbar.setTitle("Complete seu cadastro");

        }


    }

    @Override
    public void onClick(View v) {

        if(ultimaTela.equals("CadastroFacebook")){


            email = edtEmail.getText().toString();
            fone = edtFone.getText().toString();

            if (email.equals("") || fone.equals("") || !isEmailValid(email)) {

                Toast.makeText(TelaCadastro.this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();

            } else {

                cliente = auxList.get(0);

                cliente.setEmail(email);
                cliente.setFone(fone);

                BDCliente bdCliente = new BDCliente(TelaCadastro.this);
                bdCliente.atualizar(cliente);

                startActivity(new Intent(this, TelaEndereco.class));
                finish();

            }

        }else {

            nome = edtNome.getText().toString();
            email = edtEmail.getText().toString();
            fone = edtFone.getText().toString();
            senha = edtSenha.getText().toString();
            confSenha = edtConfSenha.getText().toString();


            if (nome.equals("") || email.equals("") || fone.equals("") || senha.equals("") || confSenha.equals("") || !isEmailValid(email)) {

                Toast.makeText(TelaCadastro.this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();

            } else if (verificarEmail(email)) {

                Toast.makeText(TelaCadastro.this, "Email Já Cadastrado", Toast.LENGTH_SHORT).show();

            } else if (senha.equals(confSenha)) {

                cliente.setNome(nome);
                cliente.setEmail(email);
                cliente.setFone(fone);
                cliente.setSenha(senha);

                BDCliente bdCliente = new BDCliente(TelaCadastro.this);
                bdCliente.inserir(cliente);

                startActivity(new Intent(this, TelaEndereco.class));
                finish();

            } else {

                Toast.makeText(TelaCadastro.this, "Confirmação de senha inválida!", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public static boolean isEmailValid(String email) {
        if ((email == null) || (email.trim().length() == 0))
            return false;

        String emailPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean verificarEmail(String email){

        AcessoWS ar = new AcessoWS();

        String chamadaWS;

        chamadaWS = "http://webservicevictor.16mb.com/android/get_all_cliente.php";

        String resultado = ar.chamadaGet(chamadaWS);

        try {
            Gson g = new Gson();

            List<Cliente> u;

            Type usuarioType = new TypeToken<List<Cliente>>() {}.getType();

            u = (List<Cliente>) g.fromJson(resultado, usuarioType);

            for(int i=0; i < u.size();i++){
                if(u.get(i).getEmail().equals(email)){

                    return true;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(this, "Erro", Toast.LENGTH_SHORT);
            toast.show();
        }

        return false;
    }

    @Override
    public void onBackPressed()    {

        BDClienteCore auxCliCore = new BDClienteCore(this);
        SQLiteDatabase db = auxCliCore.getWritableDatabase();
        db.execSQL("DELETE FROM cliente");

        LoginManager.getInstance().logOut();

        new MyAsyncTask().execute();
        finish();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TelaCadastro.this, "Aguarde", "Carregando...", true, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Intent it = new Intent(TelaCadastro.this, MainActivity.class);
            startActivity(it);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }

    }
}

