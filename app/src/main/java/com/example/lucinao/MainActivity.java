package com.example.lucinao;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Acoes {


    private static final String EXCHANGE_NAME = "ps";
    public final static String USER = "mqadmin";
    public final static String PASSWD = "mqadmin";
    public static String HOST = "192.168.0.104";
    public final static String VHOST = "/";


    public static void setIp(String novoIp) {
        HOST = novoIp;
    }

    private List<Conversa> conversaList = new ArrayList<Conversa>();
    private List<Conversa> conversaListFila = new ArrayList<Conversa>();
    private ConversaAdapter conversa;
    private FilaConversaAdapter conversaFila;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewFila;
    private Button botaoEnviar;
    private EditText mensagemAEnviar;
    private Button novaMsgButton;

    public class FilaReceptor extends AsyncTask<Void, Void, Void> {

        private Context context;

        public FilaReceptor(Context context) {
            this.context = context;
        }


        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Log.d("Aguardo", "Rodando Fila");

                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(HOST);
                factory.setUsername(USER);
                factory.setPassword(PASSWD);
                factory.setVirtualHost(VHOST);
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();

                channel.queueDeclare("rafael", false, false, false, null);


                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                            throws IOException {
                        String message = new String(body, "UTF-8");
                        Log.d("Aguardo1", message);
                        String[] emissor = message.split(";");
                        novaMsgFila escrever = new novaMsgFila(message, emissor[0].trim());
                        Thread thread = new Thread(escrever);

                        thread.run();
                    }
                };
                channel.basicConsume("rafael", true, consumer);

            } catch (Exception e) {
                Log.d("Aguardo", e.getMessage() + " " + e.getClass());
            }

            return null;
        }
    }


    public class RunRPS extends AsyncTask<Void, Void, Void> {
        //enviar no grupo
        private Context context;

        public RunRPS(Context context) {
            this.context = context;
        }


        @Override
        protected Void doInBackground(Void... voids) {


            Log.d("Aguardo", "Rodando");
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(MainActivity.HOST);
            factory.setUsername(MainActivity.USER);
            factory.setPassword(MainActivity.PASSWD);
            factory.setVirtualHost(MainActivity.VHOST);
            Connection connection = null;
            try {
                connection = factory.newConnection();

                Channel channel = null;

                channel = connection.createChannel();


                channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

                String queueName = null;

                queueName = channel.queueDeclare().getQueue();


                channel.queueBind(queueName, EXCHANGE_NAME, "");


                Log.d("Aguardo", " Aguardando mensagens do tópico ");

                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String message = new String(body, "UTF-8");
                        Log.d("Aguardo", message);
                        EscreverMsg escrever = new EscreverMsg(message);
                        Thread thread = new Thread(escrever);

                        thread.run();


                    }
                };

                channel.basicConsume(queueName, true, consumer);
            } catch (Exception e) {
                Log.d("Aguardo", e.getMessage() + " " + e.getClass());
            }

            return null;
        }
    }

    public class novaMsgFila implements Runnable {
        //enviar pessoal
        private String msg;
        private String emissor;

        public novaMsgFila(String msg, String emissor) {
            this.msg = msg;
            this.emissor = emissor;
        }

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                public void run() {


                    if (conversaFila.getByEmissor(emissor) >= 0) {
                        conversaFila.updateMsg(msg, conversaFila.getByEmissor(emissor));
                    } else {
                        conversaFila.inserir(new Conversa(emissor, msg));
                    }


                }

            });
        }
    }

    public class EscreverMsg implements Runnable {
        //enviar noi grupo
        private String msg;

        public EscreverMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                public void run() {

                    conversa.inserir(new Conversa("Rafael", msg));


                }

            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        RunRPS runRPS = new RunRPS(this);
        FilaReceptor filaReceptor = new FilaReceptor(this);

        filaReceptor.execute();
        runRPS.execute();

        setRecyclverView();
        botaoEnviar = (Button) findViewById(R.id.enviar);
        mensagemAEnviar = (EditText) findViewById(R.id.msg);
        novaMsgButton = (Button) findViewById(R.id.novaMsg);

        novaMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), responderMsg.class);
                Bundle bundle = new Bundle();
                bundle.putInt("request_code", 0);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });


        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    EnvioRPS rps = new EnvioRPS();
                    rps.setMessage(mensagemAEnviar.getText().toString());


                    conversa.inserir(new Conversa("Rafael", rps.getMessage()));

                    rps.execute();


                } catch (Exception e) {
                    Log.d("Exe1", "" + e.getMessage());
                }


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.configs) {

            Intent intent = new Intent(this, setIpActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("request_code", 18);
            bundle.putString("ip", HOST);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setRecyclverView() {

        conversa = new ConversaAdapter(conversaList, this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerMsgs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(conversa);


        conversaFila = new FilaConversaAdapter(conversaListFila, this);
        recyclerViewFila = (RecyclerView) findViewById(R.id.recyclerMsgsFila);
        recyclerViewFila.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFila.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFila.setAdapter(conversaFila);


//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelp(conversa));

    }


    @Override
    public void Responder(int position) {
        Intent intent = new Intent(this, responderMsg.class);
        Bundle bundle = new Bundle();
        bundle.putInt("request_code", 1);
        bundle.putString("Destino", conversaListFila.get(position).getEmissor().replace(":", ""));
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);

    }

    @Override
    public void Apagar() {

    }

    @Override
    public void Escrever() {

    }
}
