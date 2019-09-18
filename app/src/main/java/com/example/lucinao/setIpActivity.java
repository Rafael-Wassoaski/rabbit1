package com.example.lucinao;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class setIpActivity extends AppCompatActivity {


    private EditText novoIp;
    private Button botaoConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ip);

        novoIp = (EditText)findViewById(R.id.ip);
        botaoConfirmar = (Button)findViewById(R.id.confirmar);


        Bundle bundle = getIntent().getExtras();

        novoIp.setText(bundle.getString("ip"));

        botaoConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!novoIp.getText().toString().isEmpty()) {
                    MainActivity.setIp(novoIp.getText().toString());

                    setResult(Activity.RESULT_OK);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "O IP não pode ser vazio", Toast.LENGTH_LONG);
                }
            }
        });

    }
}
