package com.example.ledstripcontrollor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.slider.LightnessSlider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    String colour = "FF000000";

    int r = 0, g = 0, b = 0;
    boolean p;
    int[] Url;

    void getreq(String url, String data) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest objreq = new JsonArrayRequest
                (
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                try {
                                    JSONObject object = response.getJSONObject(0);
                                    object = object.getJSONObject("fields");
                                    String temp = "";
                                    switch (data)
                                    {
                                        case "init":
                                            r = object.getInt("red");
                                            g = object.getInt("green");
                                            b = object.getInt("blue");
                                            p = object.getBoolean("power");
                                            SwitchCompat Power = findViewById(R.id.power);
                                            Power.setChecked(p);
                                            temp = "Initialised";
                                            break;
                                        case "power":
                                            p = object.getBoolean("power");
                                            temp = Boolean.toString(p);
                                            break;
                                        case "colour":
                                            r = object.getInt("red");
                                            g = object.getInt("green");
                                            b = object.getInt("blue");
                                            temp = Integer.toString(r)+","+Integer.toString(g)+","+Integer.toString(b);
                                            break;

                                    }
                                    Toast toast = Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT);
                                    toast.show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Rest Response", error.toString());
                                Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                );
        queue.add(objreq);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Url = new int[]{192, 168, 0, 164, 8000};
        Bundle extras = getIntent().getExtras();
        try {
            int[] IP = extras.getIntArray("key");
            Url = IP;
        } catch (Exception e) {
            Log.e("error", e.toString());
        }


        String a = String.valueOf(Url[0]) + "." + String.valueOf(Url[1]) + "." + String.valueOf(Url[2]) + "." + String.valueOf(Url[3]) + ":" + String.valueOf(Url[4]);
        getreq("http://" + a + "/LED_Strip/senddata", "init");


        ColorPickerView colorPickerView = findViewById(R.id.color_picker_view);
        LightnessSlider lightnessSlider = findViewById(R.id.v_lightness_slider);
        TextView text = findViewById(R.id.ColCode);
        Button ip = findViewById(R.id.Ip);
        Button update = findViewById(R.id.Update);
        TextView bg = findViewById(R.id.background);
        SwitchCompat Power = findViewById(R.id.power);


        ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ip_port_update.class);
                myIntent.putExtra("key", Url); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });


        Power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String a = String.valueOf(Url[0]) + "." + String.valueOf(Url[1]) + "." + String.valueOf(Url[2]) + "." + String.valueOf(Url[3]) + ":" + String.valueOf(Url[4]);
                String url = "";
                if (isChecked)
                    url = "http://" + a + "/LED_Strip/updatepower?p=True";
                else
                    url = "http://" + a + "/LED_Strip/updatepower?p=False";
                getreq(url, "power");
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                r = Integer.decode("0x" + colour.substring(2, 4));
                g = Integer.decode("0x" + colour.substring(4, 6));
                b = Integer.decode("0x" + colour.substring(6, 8));
                bg.setBackgroundColor(Color.rgb(r, g, b));
                String a = String.valueOf(Url[0]) + "." + String.valueOf(Url[1]) + "." + String.valueOf(Url[2]) + "." + String.valueOf(Url[3]) + ":" + String.valueOf(Url[4]);
                String url = "http://" + a + "/LED_Strip/updatergb?r=" + r + "&g=" + g + "&b=" + b;
                getreq(url, "colour");
            }
        });


        colorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                colour = Integer.toHexString(selectedColor).toUpperCase();
                text.setText(colour.substring(2));
            }
        });
    }
}