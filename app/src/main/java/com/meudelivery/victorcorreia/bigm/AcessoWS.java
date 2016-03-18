package com.meudelivery.victorcorreia.bigm;


import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class AcessoWS {
    //private int  TIMEOUT_MILLISEC = 3000;
    // private String[] params;
    Timer timer;
    TimerTask timerTask;
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

/*
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/

    public String chamadaGet(String url)
    {

        HttpClient httpclient = new DefaultHttpClient();

        HttpGet chamadaget = new HttpGet(url);
        String retorno = "";

        // Instantiate a GET HTTP method
        try {

            //Aqui o ideal é colocar a requesição assíncrona
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            retorno = httpclient.execute(chamadaget, responseHandler);


        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Throwable t) {
            Log.i("erro", t.toString());
        }

        return retorno;

    }

    public void startTimer(String url)
    {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask(url);

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 3000, 3000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask(final String url) {

        timerTask = new TimerTask() {
            public void run() {
                String resultado = chamadaGet(url);
                if (resultado.isEmpty() || resultado == "") {
                    timer.cancel();
                }
            }
        };
    }



}


