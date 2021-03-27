package com.example.ledstripcontrollor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ip_port_update extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_port_update);
        Bundle extras = getIntent().getExtras();
        final int[][] Url = {extras.getIntArray("key")};

        EditText Ip1 = findViewById(R.id.Ip1);
        EditText Ip2 = findViewById(R.id.Ip2);
        EditText Ip3 = findViewById(R.id.Ip3);
        EditText Ip4 = findViewById(R.id.Ip4);
        EditText port = findViewById(R.id.port);

        Ip1.setText(String.valueOf(Url[0][0]));
        Ip2.setText(String.valueOf(Url[0][1]));
        Ip3.setText(String.valueOf(Url[0][2]));
        Ip4.setText(String.valueOf(Url[0][3]));
        port.setText(String.valueOf(Url[0][4]));

        Button back = findViewById(R.id.BACK);
        Button update = findViewById(R.id.updateid);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] temp = new int[5];
                temp[0] = Integer.parseInt(Ip1.getText().toString());
                temp[1] = Integer.parseInt(Ip2.getText().toString());
                temp[2] = Integer.parseInt(Ip3.getText().toString());
                temp[3] = Integer.parseInt(Ip4.getText().toString());
                temp[4] = Integer.parseInt(port.getText().toString());
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                if(temp[0]!=192) {
                    Toast toast = Toast.makeText(context, "Unsuccessful", duration);
                    toast.show();
                }
                else if (temp[1]!=168){
                    Toast toast = Toast.makeText(context, "Unsuccessful", duration);
                    toast.show();
                }
                else if(temp[2]<=0 || temp[2]>255){
                    Toast toast = Toast.makeText(context, "Unsuccessful", duration);
                    toast.show();
                }
                else if(temp[3]<=0 || temp[3]>255) {
                    Toast toast = Toast.makeText(context, "Unsuccessful", duration);
                    toast.show();
                }
                else if(temp[4]<=0 || temp[4]>65535){
                    Toast toast = Toast.makeText(context, "Unsuccessful", duration);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(context, "Successful", duration);
                    Url[0] = temp;
                    Log.d("tag", "onClick() returned: " + Url);
                    toast.show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ip_port_update.this, MainActivity.class);
                myIntent.putExtra("key", Url[0]); //Optional parameters
                ip_port_update.this.startActivity(myIntent);
            }
        });
    }
}