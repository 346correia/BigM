package com.meudelivery.victorcorreia.bigm;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TelaLoginFragment extends Fragment {

    private String nomeFb = new String();
    private String idFb = new String();
    private Cliente cliente = new Cliente();
    private List<Cliente> auxListCliente;
    private ProfileTracker mProfileTracker;
    private CallbackManager mCallbackManager;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken acessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null){
                idFb = profile.getId();
                nomeFb = profile.getName();


                if(verificarIdFb(idFb)){

                    BDCliente bdCliente = new BDCliente(getActivity());
                    bdCliente.inserir(cliente);

                    Toast.makeText(getActivity(), "Bem-vindo " +nomeFb, Toast.LENGTH_LONG).show();

                    startActivity(new Intent(getActivity(), MainActivity.class));

                }else{

                    cliente.setIdFb(idFb);
                    cliente.setNome(nomeFb);

                    BDCliente bdCliente = new BDCliente(getActivity());
                    bdCliente.inserir(cliente);

                    startActivity(new Intent(getActivity(), TelaCadastro.class));

                }
            }else{

                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        // profile2 is the new profile
                        Log.v("facebook - profile", profile2.getFirstName());

                        idFb = profile2.getId();
                        nomeFb = profile2.getName();

                        System.out.println(idFb);
                        System.out.println(nomeFb);
                        System.out.println("d'oh!");

                        if(verificarIdFb(idFb)){

                            BDCliente bdCliente = new BDCliente(getActivity());
                            bdCliente.inserir(cliente);

                            Toast.makeText(getActivity(), "Bem-vindo " +nomeFb, Toast.LENGTH_LONG).show();

                            startActivity(new Intent(getActivity(), MainActivity.class));

                        }else{

                            cliente.setIdFb(idFb);
                            cliente.setNome(nomeFb);

                            BDCliente bdCliente = new BDCliente(getActivity());
                            bdCliente.inserir(cliente);

                            startActivity(new Intent(getActivity(), TelaCadastro.class));

                        }

                        mProfileTracker.stopTracking();
                    }
                };
                mProfileTracker.startTracking();


                System.out.println("d'oh!");
            }
        }

        @Override
        public void onCancel() {
            Log.v("facebook - onCancel", "cancelled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.v("facebook - onError", error.getMessage());
        }
    };

    public TelaLoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tela_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton)view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mCallback);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

   // public void OnClick(View v){
   private boolean verificarIdFb(String idFb){

       AcessoWS ar = new AcessoWS();

       String chamadaWS;

       chamadaWS = "http://webservicevictor.16mb.com/android/get_all_cliente.php";

       String resultado = ar.chamadaGet(chamadaWS, isNetworkAvailable());

       try {
           Gson g = new Gson();

           List<Cliente> u;

           Type clienteType = new TypeToken<List<Cliente>>() {}.getType();

           u = (List<Cliente>) g.fromJson(resultado, clienteType);

           for(int i=0; i < u.size();i++){
               if(u.get(i).getIdFb().equals(idFb)){

                   cliente = u.get(i);
                   return true;

               }

           }
           return false;

       } catch (Exception e) {
           e.printStackTrace();
           Toast toast = Toast.makeText(getActivity(), "Erro", Toast.LENGTH_SHORT);
           toast.show();
       }

       return false;
   }



   // }
}
