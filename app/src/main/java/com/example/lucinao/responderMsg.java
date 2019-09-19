package com.example.lucinao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class responderMsg extends AppCompatActivity {

    private EditText destinoTextView;
    private EditText msgTextView;
    private Button enviar;

    private String destinatario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_responder_msg);
        destinoTextView = (EditText)findViewById(R.id.destino);
        msgTextView = (EditText)findViewById(R.id.mensagem);

        enviar = (Button)findViewById(R.id.enviar);
        Bundle bundle = getIntent().getExtras();


        final int requestCode = bundle.getInt("request_code");




        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    destinatario = destinoTextView.getText().toString();



                    Log.d("Aguardo", "Rodando envio");
                    try {
                        EnvioFila enviarFila;
                        enviarFila = new EnvioFila(msgTextView.getText().toString(), destinatario);
                        enviarFila.execute();

                        Intent returnIntent = new Intent();
                        Bundle returnBundle = new Bundle();
                        returnBundle.putString("destino", destinatario);
                        returnBundle.putString("msg", msgTextView.getText().toString());
                        returnIntent.putExtras(returnBundle);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }catch (Exception e){
                        Log.d("Aguardo1", e.getMessage()+" " +e.getClass());
                    }
                }



        });

    }



}