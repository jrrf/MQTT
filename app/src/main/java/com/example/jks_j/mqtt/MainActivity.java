package com.example.jks_j.mqtt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;

public class MainActivity extends AppCompatActivity {

    private Button bt_ligar;
    private Button bt_desligar;
    private Button bt_conect;
    private EditText et_host;
    private EditText et_porta;
    private EditText et_user;
    private EditText et_pass;
    private EditText et_topic;
    private static String MQTTHOST = ""; //"tcp://xxxxxxxxxx:porta"
    private static String USERNAME = "";
    private static String PASSWORD = "";
    private String topicStr = "";
    String clientId = MqttClient.generateClientId();
    MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_host = findViewById(R.id.et_host);
        et_porta = findViewById(R.id.et_porta);
        et_pass = findViewById(R.id.et_pass);
        et_user = findViewById(R.id.et_user);
        et_topic = findViewById(R.id.et_topic);
        bt_ligar = findViewById(R.id.bt_ligar);
        bt_desligar = findViewById(R.id.bt_desligar);
        bt_conect = findViewById(R.id.bt_conect);

        bt_desligar.setEnabled(false);
        bt_ligar.setEnabled(false);

        bt_conect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_host = et_host.getText().toString();
                String input_porta = et_porta.getText().toString();
                String input_pass = et_pass.getText().toString();
                String input_topic = et_topic.getText().toString();
                String input_user = et_user.getText().toString();

                if(input_host.isEmpty() || input_porta.isEmpty() || input_pass.isEmpty() || input_topic.isEmpty() || input_user.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_LONG).show();


                }else {
                    MQTTHOST = "tcp://" + input_host + ":" + input_porta;
                    USERNAME = "" + input_user;
                    topicStr = "" + input_topic;
                    PASSWORD = "" + input_pass;
                    client = new MqttAndroidClient(getApplicationContext(), MQTTHOST, clientId);
                    conectar();
                }
            }
        });

        bt_ligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ligar();
            }
        });

        bt_desligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desligar();
            }
        });

    }

    public void conectar(){

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());


        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(getApplicationContext(),"Conectado",Toast.LENGTH_LONG).show();

                    et_host.setEnabled(false);
                    et_pass.setEnabled(false);
                    et_topic.setEnabled(false);
                    et_user.setEnabled(false);

                    bt_conect.setEnabled(false);
                    bt_desligar.setEnabled(true);
                    bt_ligar.setEnabled(true);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(getApplicationContext(),"Não onectado",Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void ligar(){
        String topic = topicStr;
        String mensagem = "Ligar";
        try {
            client.publish(topic,mensagem.getBytes(),1,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void desligar(){
        String topic = topicStr;
        String mensagem = "Desligar";
        try {
            client.publish(topic,mensagem.getBytes(),1,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
