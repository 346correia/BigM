package com.meudelivery.victorcorreia.bigm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private List<Categoria> listGroup;
    private HashMap<Categoria, List<Item>> listData;
    private ProgressDialog pd = null;
    private List<Item> itemBd;
    Timer timer;
    TimerTask timerTask;
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Big Mengão");
        setSupportActionBar(toolbar);

        //init facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());

        //init drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        //init itens de layout e logica do Victor
        TextView txtNomeCliente = (TextView) header.findViewById(R.id.txtNomeCliente);
        TextView txtEmailCliente = (TextView) header.findViewById(R.id.txtEmailCliente);

        BDCliente bdCliente = new BDCliente(this);
        List<Cliente> cliente;
        cliente = bdCliente.buscar();

        if (cliente.isEmpty()) {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            //navigationView.getMenu().findItem(R.id.nav_pedidos_anteriores).setVisible(false);

        } else {
            navigationView.getMenu().findItem(R.id.nav_entrar).setVisible(false);

            String nomeCliente = cliente.get(0).getNome();
            String emailCliente = cliente.get(0).getEmail();

            txtNomeCliente.setText(nomeCliente);
            txtEmailCliente.setText(emailCliente);
        }

        navigationView.getMenu().findItem(R.id.nav_sobre).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_fale).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_politica).setVisible(false);


        Button btnPedido = (Button) findViewById(R.id.btnPedido);

        btnPedido.setOnClickListener(this);

        //metodo que chama e faz o trabalho no WS
        boolean success = this.buildList();

        //se tudo der certo inicia a lista if(success == true)
        if (success){
            //salvar lista
            BDItem bdItem = new BDItem(this);

            BDItemCore auxBd = new BDItemCore(this);

            SQLiteDatabase db = auxBd.getWritableDatabase();
            db.execSQL("DELETE FROM item"); //delete all rows in a table
            //db.close();

            for (int i = 0; i < itemBd.size(); i++) {
                bdItem.inserir(itemBd.get(i));
            }

            //init expandableListView
            final ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
            expandableListView.setAdapter(new ExpandableAdapter(MainActivity.this, listGroup, listData));

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    Intent it = new Intent(MainActivity.this, TelaAddAoPedido.class);

                    it.putExtra("IDITEM", (int) id);

                    startActivity(it);
                    //Toast.makeText(MainActivity.this, "Group: " + groupPosition + " | Item:" + id, Toast.LENGTH_LONG).show();
                    return false;
                }
            });

            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    //Toast.makeText(MainActivity.this, "Group (Expand): " + groupPosition, Toast.LENGTH_LONG).show();
                }
            });

            expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                @Override
                public void onGroupCollapse(int groupPosition) {
                    //Toast.makeText(MainActivity.this, "Group (Collapse): " + groupPosition, Toast.LENGTH_LONG).show();
                }
            });
        }




    }

    public boolean buildList() {

        listGroup = new ArrayList<Categoria>();
        listData = new HashMap<Categoria, List<Item>>();
        //variavel para ter a certeza que tudo voltou correto
        boolean success = false;


        //Categorias
        AcessoWS acessoWS = new AcessoWS();

        String chamadaWS;

        chamadaWS = "http://webservicevictor.16mb.com/android/get_all_categoria.php";

        String resultado = acessoWS.chamadaGet(chamadaWS);

        //prevendo do resultado vir nulo e fazer todo o serviço
        if (!resultado.isEmpty() || resultado == ""){

            success = true;

            try {
                Gson g = new Gson();

                Type categoriaType = new TypeToken<List<Categoria>>() {
                }.getType();

                listGroup = (List<Categoria>) g.fromJson(resultado, categoriaType);


            } catch (Exception e) {
                e.printStackTrace();
                success = false;

            }

            //Itens
            chamadaWS = "http://webservicevictor.16mb.com/android/get_all_item.php";

            resultado = acessoWS.chamadaGet(chamadaWS);

            try {
                Gson g = new Gson();

                //private List<Item> it;

                Type categoriaType = new TypeToken<List<Item>>() {
                }.getType();

                itemBd = (List<Item>) g.fromJson(resultado, categoriaType);
                //itemRealm = (List<ItemRealm>) g.fromJson(resultado, categoriaType);

                for (int i = 0; i < listGroup.size(); i++) {

                    List<Item> auxList = new ArrayList<Item>();

                    for (int n = 0; n < itemBd.size(); n++) {
                        //Item
                        if (itemBd.get(n).getIdCategoria() == i + 1) {
                            auxList.add(itemBd.get(n));
                        }
                    }
                    listData.put(listGroup.get(i), auxList);
                }

            } catch (Exception e) {
                e.printStackTrace();
                success = false;
            }

        }

        return success;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        item.setVisible(false);

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_entrar) {

            startActivity(new Intent(this, TelaLogin.class));
            //finish();

        } else if (id == R.id.nav_logout) {

            BDClienteCore auxCliCore = new BDClienteCore(this);
            SQLiteDatabase db = auxCliCore.getWritableDatabase();
            db.execSQL("DELETE FROM cliente");

            LoginManager.getInstance().logOut();

            new MyAsyncTask().execute();
            finish();

        } else if (id == R.id.nav_sobre) {

        } else if (id == R.id.nav_fale) {

        } else if (id == R.id.nav_politica) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent(this, TelaPedido.class);
        startActivity(it);

    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Aguarde");
            pd.setMessage("Carregando...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Intent it = new Intent(MainActivity.this, MainActivity.class);
            startActivity(it);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            pd.dismiss();
        }


    }

    public void startTimer()
    {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 0, 10000); //
    }

    public void stoptimertask(View v) {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                    }
                });
            }
        };
    }

}
