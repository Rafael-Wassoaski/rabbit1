package com.example.lucinao;

import android.os.AsyncTask;
import android.util.Log;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import static com.example.lucinao.MainActivity.PASSWD;
import static com.example.lucinao.MainActivity.VHOST;

public class EnvioRPS extends AsyncTask<Void, Void, Void> {

    private String message ;
    public EnvioRPS(){}

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;

    }



    @Override
    protected Void doInBackground(Void... voids) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(MainActivity.HOST);
        factory.setUsername(MainActivity.USER);
        factory.setPassword(PASSWD);
        factory.setVirtualHost(VHOST);
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare("ps", BuiltinExchangeType.FANOUT);



            channel.basicPublish("ps", "", null, getMessage().getBytes("UTF-8"));
            System.out.println(" Mensagem enviada para o grupo " + "ps");

            channel.close();
            connection.close();
            Log.d("Aguardo", "Fim do envio");
        } catch (Exception e) {
            Log.d("Aguardo", e.getClass() + ": " + e.getMessage());
        }
        return null;
    }
}
