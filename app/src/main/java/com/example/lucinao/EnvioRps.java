package com.example.lucinao;

import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;

public class EnvioRps implements Runnable{

    private final static String QUEUE_NAME = "rafael";
    private final static String VHOST = "/";
    public final static String USER = "guest";
    public final static String PASSWD = "guest";
    public final static String HOST = "localhost";
    private BlockingDeque<String> queue = new LinkedBlockingDeque <String>();
    private ConnectionFactory factory = new ConnectionFactory();

    private String msg;
    private String receptor;


    public EnvioRps(String msg, String receptor){
        this.msg = msg;
        this.receptor = receptor;
    }


    @Override
    public void run() {
        factory.setHost(HOST);

        factory.setUsername(USER);

        factory.setPassword(PASSWD);
        factory.setVirtualHost(VHOST);
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false,false, null);
            msg = "["+receptor+"] "+ msg;
            channel.basicPublish("",  QUEUE_NAME, null, msg.getBytes("UTF-8"));
            Log.d("Confirma", "Msg foi");
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }
}
