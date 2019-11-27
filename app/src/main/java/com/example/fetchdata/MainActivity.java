package com.example.fetchdata;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText txtvalue;
    Button btnfetch;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtvalue = (EditText)findViewById(R.id.editText);
        btnfetch = (Button)findViewById(R.id.buttonfetch);
        listview = (ListView)findViewById(R.id.listView);
        btnfetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }


    private void getData() {

        String value = txtvalue.getText().toString().trim();

        if (value.equals("")) {
            Toast.makeText(this, " Enter Data Value", Toast.LENGTH_LONG).show();
            return;
        }

        String url = Config5.DATA_URL + txtvalue.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSON(String response) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config5.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config5.KEY_ID);
                String data = jo.getString(Config5.KEY_NAME);
                String title = jo.getString(Config5.KEY_EMAIL);
                String date = jo.getString(Config5.KEY_PHONE);



                final HashMap<String, String> employees = new HashMap<>();
                employees.put(Config5.KEY_EMAIL,  "Date = "+title);
                employees.put(Config5.KEY_PHONE, date);
                employees.put(Config5.KEY_NAME, data);
                employees.put(Config5.KEY_ID, id);

                list.add(employees);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                MainActivity.this, list, R.layout.activity_mylist,
                new String[]{Config5.KEY_ID, Config5.KEY_NAME, Config5.KEY_PHONE, Config5.KEY_EMAIL},
                new int[]{R.id.title, R.id.date, R.id.data, R.id.tvid});

        listview.setAdapter(adapter);

    }
}