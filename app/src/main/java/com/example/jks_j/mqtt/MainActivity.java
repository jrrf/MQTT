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
    private static String USERNAME = null;
    private static String PASSWORD = null;
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
/*
        bt_desligar.setEnabled(false);
        bt_ligar.setEnabled(false);
*/
        bt_desligar.setVisibility(View.INVISIBLE);
        bt_ligar.setVisibility(View.INVISIBLE);

        bt_conect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_host = et_host.getText().toString();
                String input_porta = et_porta.getText().toString();
                String input_pass = et_pass.getText().toString();
                String input_topic = et_topic.getText().toString();
                String input_user = et_user.getText().toString();

                if (input_host.isEmpty() || input_topic.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_LONG).show();


                } else {
                    if (input_porta.isEmpty())
                        input_porta = "1883";
                    MQTTHOST = "tcp://" + input_host + ":" + input_porta;
                    topicStr = "" + input_topic;

                    if (!input_user.isEmpty() && !input_pass.isEmpty()) {
                        USERNAME = input_user;
                        PASSWORD = input_pass;
                    }
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

    public void conectar() {

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            IMqttToken token;
            if (USERNAME != null && PASSWORD != null) {
                options.setUserName(USERNAME);
                options.setPassword(PASSWORD.toCharArray());
                token = client.connect(options);
            } else {
                token = client.connect();
            }

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();

                    et_host.setVisibility(View.INVISIBLE);
                    et_porta.setVisibility(View.INVISIBLE);
                    et_pass.setVisibility(View.INVISIBLE);
                    et_topic.setVisibility(View.INVISIBLE);
                    et_user.setVisibility(View.INVISIBLE);
                    bt_conect.setVisibility(View.INVISIBLE);
                    bt_desligar.setVisibility(View.VISIBLE);
                    bt_ligar.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getApplicationContext(), "Não onectado", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void conectarNoUser() {

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();

                    et_host.setVisibility(View.INVISIBLE);
                    et_porta.setVisibility(View.INVISIBLE);
                    et_pass.setVisibility(View.INVISIBLE);
                    et_topic.setVisibility(View.INVISIBLE);
                    et_user.setVisibility(View.INVISIBLE);
                    bt_conect.setVisibility(View.INVISIBLE);
                    bt_desligar.setVisibility(View.VISIBLE);
                    bt_ligar.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(getApplicationContext(), "Não onectado", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


    public void ligar() {
        String topic = topicStr;
        String mensagem = "Ligar";
        try {
            client.publish(topic, mensagem.getBytes(), 1, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void desligar() {
        String topic = topicStr;
        String mensagem = "Desligar";
        try {
            client.publish(topic, mensagem.getBytes(), 1, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
