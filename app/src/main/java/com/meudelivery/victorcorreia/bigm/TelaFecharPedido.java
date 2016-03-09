package com.meudelivery.victorcorreia.bigm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.List;

public class TelaFecharPedido extends AppCompatActivity implements View.OnClickListener {

    private Button btnEndereco;
    private Button btnVoltarPedido;
    private Button btnFecharPedido;
    private Spinner spnPag;
    private TextView txtTotal;
    private TextView txtRua;
    private TextView txtNumero;
    private TextView txtBairro;
    private TextView txtComplemento;
    private TextView txtPerimetro;
    private EditText edtTrocoPara;
    private String[] arraySpnPag;
    private List<Cliente> auxList;
    private List<ItemPedido> auxListPedido;
    private String nome;
    private String emailCliente;
    private String fone;
    private String bairro;
    private String rua;
    private String numero;
    private String complemento;
    private String perimetro;
    private String pedido = new String("");
    private Double total = 0.0;
    private DecimalFormat df;
    private int taxa = 7;
    private String tipoPag;
    private Double trocoPara = 0.0;

    private static final String username = "pedidos.meudelivery@gmail.com";
    private static final String password = "DH135.0123";
    private String email = new String("bigmpedidos@gmail.com");
    private String subject = new String();
    private String message = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_fechar_pedido);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Fechar Pedido");
        setSupportActionBar(toolbar);

        txtTotal = (TextView) findViewById(R.id.txtTotal);

        btnEndereco = (Button) findViewById(R.id.btnEndereco);
        btnVoltarPedido = (Button) findViewById(R.id.btnVoltarPedido);
        btnFecharPedido = (Button) findViewById(R.id.btnFecharPedido);

        spnPag = (Spinner) findViewById(R.id.spnPag);

        txtBairro = (TextView) findViewById(R.id.txtBairro);
        txtRua = (TextView) findViewById(R.id.txtRua);
        txtNumero = (TextView) findViewById(R.id.txtNumero);
        txtComplemento = (TextView) findViewById(R.id.txtComplemento);
        txtPerimetro = (TextView) findViewById(R.id.txtPerimetro);

        edtTrocoPara = (EditText) findViewById(R.id.edtTrocoPara);

        this.arraySpnPag = new String[]{"Dinheiro", "Visa", "Mastercard", "Visa eletron", "Cielo", "Maestro"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraySpnPag);
        spnPag.setAdapter(adapter);

        BDCliente bdCliente = new BDCliente(this);
        auxList = bdCliente.buscar();

        nome = auxList.get(0).getNome();
        emailCliente = auxList.get(0).getEmail();
        fone = auxList.get(0).getFone();
        bairro = auxList.get(0).getBairro();
        rua = auxList.get(0).getRua();
        numero = String.valueOf(auxList.get(0).getNumero());
        complemento = auxList.get(0).getComplemento();
        perimetro = auxList.get(0).getPerimetro();

        txtBairro.setText(bairro);
        txtRua.setText(rua);
        txtNumero.setText(numero);
        txtComplemento.setText(complemento);
        txtPerimetro.setText(perimetro);

        BDPedido bdPedido = new BDPedido(this);
        auxListPedido = bdPedido.buscar();

        df = new DecimalFormat("0.00");

        for (int i = 0; i < auxListPedido.size(); i++) {

            pedido = pedido + auxListPedido.get(i).getQuant() + "x   " + auxListPedido.get(i).getNome() + "   " + "R$ " + df.format(Double.parseDouble(auxListPedido.get(i).getValor()) * auxListPedido.get(i).getQuant())
                    +"   "+auxListPedido.get(i).getObservacao()+"\n";
            total = total + (Double.parseDouble(auxListPedido.get(i).getValor()) * auxListPedido.get(i).getQuant());

        }

        txtTotal.setText("R$ " + df.format(total + taxa));

        btnEndereco.setOnClickListener(this);
        btnVoltarPedido.setOnClickListener(this);
        btnFecharPedido.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnEndereco:

                startActivity(new Intent(this, TelaEndereco.class));

                break;

            case R.id.btnVoltarPedido:

                //startActivity(new Intent(this, TelaPedido.class));
                finish();

                break;

            case R.id.btnFecharPedido:

                tipoPag = spnPag.getSelectedItem().toString();

                if (edtTrocoPara.getText().toString().isEmpty()) {

                    trocoPara = 0.0;

                } else {

                    trocoPara = Double.parseDouble(edtTrocoPara.getText().toString());

                }

                System.out.println(trocoPara);

                if (tipoPag.equals("Dinheiro")) {
                    if (trocoPara < total + taxa) {

                        Toast.makeText(TelaFecharPedido.this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();

                    } else {

                        message = "Cliente: " + "\n\n"
                                + "Nome: " + nome + "\n"
                                + "Email: " + emailCliente + "\n"
                                + "fone: " + fone + "\n\n"
                                + "Endereço:" + "\n\n"
                                + "Bairro: " + bairro + "\n"
                                + "Rua: " + rua + "\n"
                                + "Número: " + numero + "\n"
                                + "Complemento: " + complemento + "\n"
                                + "Perímetro: " + perimetro + "\n\n"
                                + "Pedido:" + "\n\n"
                                + pedido + "\n"
                                + "Taxa de Entrega: " + "R$ 7,00" + "\n\n"
                                + "Total: " + "R$ " + df.format(total + taxa) + "\n\n"
                                + "Tipo de Pagamento: " + tipoPag + "\n\n"
                                + "Troco para: " + "R$ " + df.format(trocoPara) + "\n\n"
                                + "Dar de troco: " + "R$ " + df.format(trocoPara - (total + taxa));

                        subject = nome;

                        sendMail(email, subject, message);

                        System.out.println(message);

                        BDPedidoCore auxPedCore = new BDPedidoCore(this);
                        SQLiteDatabase db = auxPedCore.getWritableDatabase();
                        db.execSQL("DELETE FROM itemPedido");

                        Toast.makeText(this, "Pedido realizado com sucesso!", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(this, MainActivity.class));

                    }

                } else {

                    message = "Cliente: " + "\n\n"
                            + "Nome: " + nome + "\n"
                            + "Email: " + email + "\n"
                            + "fone: " + fone + "\n\n"
                            + "Endereço:" + "\n\n"
                            + "Bairro: " + bairro + "\n"
                            + "Rua: " + rua + "\n"
                            + "Número: " + numero + "\n"
                            + "Complemento: " + complemento + "\n"
                            + "Perímetro: " + perimetro + "\n\n"
                            + "Pedido:" + "\n\n"
                            + pedido + "\n"
                            + "Taxa de Entrega: " + "R$ 7,00" + "\n\n"
                            + "Total: " + "R$ " + df.format(total + taxa) + "\n\n"
                            + "Tipo de Pagamento: " + tipoPag + "\n\n";

                    subject = nome;

                    sendMail(email, subject, message);

                    System.out.println(message);

                    BDPedidoCore auxPedCore = new BDPedidoCore(this);
                    SQLiteDatabase db = auxPedCore.getWritableDatabase();
                    db.execSQL("DELETE FROM itemPedido");

                    Toast.makeText(this, "Pedido realizado com sucesso!", Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "Pedido realizado com sucesso!", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(this, MainActivity.class));


                }

                break;
        }
    }

    //Métodos de envio automático de email

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("pedidos.meudelivery@gmail.com", "Pedido (Meu Delivery)"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TelaFecharPedido.this, "Aguarde", "Enviando Pedido...", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(this, TelaPedido.class));
        finish();
    }

}
