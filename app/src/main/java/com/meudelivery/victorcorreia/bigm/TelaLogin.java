package com.meudelivery.victorcorreia.bigm;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TelaLogin extends AppCompatActivity  {

    private EditText edtLogin;
    private EditText edtSenha;
    private String email;
    private String senha;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Faça seu Login");
        setSupportActionBar(toolbar);

        FacebookSdk.sdkInitialize(getApplicationContext());

        edtLogin = (EditText)findViewById(R.id.edtLogin);
        edtSenha = (EditText)findViewById(R.id.edtSenha);


    }

    public void onClick(View v){

        switch (v.getId()) {

            case R.id.btnEntrar:

                email = edtLogin.getText().toString();
                senha = edtSenha.getText().toString();

                if(!isEmailValid(email)){

                    Toast.makeText(this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();
                }else if(verificarEmailSenha(email, senha)){

                    startActivity(new Intent(this, MainActivity.class));

                }else{

                    Toast.makeText(this, "Usuário não cadastrado", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.btnCad:

                startActivity(new Intent(this, TelaCadastro.class));
                finish();

                break;
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

    private boolean verificarEmailSenha(String email, String senha){

        AcessoWS ar = new AcessoWS();

        String chamadaWS;

        chamadaWS = "http://webservicevictor.16mb.com/android/get_all_cliente.php";

        String resultado = ar.chamadaGet(chamadaWS, isNetworkAvailable());

        try {
            Gson g = new Gson();

            List<Cliente> u;

            Type usuarioType = new TypeToken<List<Cliente>>() {}.getType();

            u = (List<Cliente>) g.fromJson(resultado, usuarioType);

            for(int i=0; i < u.size();i++){
                if(u.get(i).getEmail().equals(email) && u.get(i).getSenha().equals(senha)){

                    BDCliente bdCliente = new BDCliente(this);
                    bdCliente.inserir(u.get(i));

                    Toast.makeText(this, "Bem-vindo, "+u.get(i).getNome(), Toast.LENGTH_LONG).show();


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

}
