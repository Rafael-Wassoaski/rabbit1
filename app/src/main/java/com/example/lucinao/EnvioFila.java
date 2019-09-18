package com.example.lucinao;

import android.os.AsyncTask;
import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class EnvioFila extends AsyncTask<String, String, String> {

    private String QUEUE_NAME = "rafael";

    private BlockingDeque<String> queue = new LinkedBlockingDeque<String>();
    private ConnectionFactory factory = new ConnectionFactory();

    private String msg;



    public EnvioFila(String msg, String receptor) {
        this.msg = msg;
        this.QUEUE_NAME = receptor;


    }


    @Override
    protected String doInBackground(String... strings) {

        factory.setHost(MainActivity.HOST);

        factory.setUsername(MainActivity.USER);

        factory.setPassword(MainActivity.PASSWD);
        factory.setVirtualHost(MainActivity.VHOST);

        try {


            Connection connection = null;
            connection = factory.newConnection();


            Channel channel = connection.createChannel();

            channel.queueDeclare(this.QUEUE_NAME, false, false, false, null);

            msg = "rafael; " + msg;

            channel.basicPublish("", this.QUEUE_NAME, null, msg.getBytes("UTF-8"));

            channel.close();
            connection.close();
        } catch (Exception e) {
            Log.d("ErroAqui", e.getClass() + "");
        }
return null;
    }

}
