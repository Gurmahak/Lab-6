package com.example.lab_6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpClient client = new OkHttpClient();

        String Url = "https://www.reddit.com/.json";
        Request req = new Request.Builder().url(Url).build();
        listView = findViewById(R.id.listview);

        ArrayList<String> url = new ArrayList<String>();

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(req).execute();
                    String text = response.body().string();
                    Log.d("response", text);

                    JSONObject object = (JSONObject) new JSONTokener(text).nextValue();

                    JSONArray listings = object.getJSONObject("data").getJSONArray("children");

                    ArrayList<String> titles = new ArrayList<>(listings.length());
                    for (int i = 0; i < listings.length(); i++) {
                        JSONObject item = listings.getJSONObject(i);


                        titles.add(item.getJSONObject("data").getString("title"));
                        url.add(item.getJSONObject("data").getString("permalink"));
                    }
                    runOnUiThread(() -> {
//                        String result = titles.stream().reduce("", (a, b) -> a += "\n" + b);
//                        titles.add(result);
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, titles);
                        listView.setAdapter(arrayAdapter);
                    });
                } catch (IOException | JSONException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    });
                }
                ;
            }
        };
        t.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = listView.getItemAtPosition(position);

                String URL = url.get(position);

                Log.d("Url is", String.valueOf(URL));
                Log.d("Position ", String.valueOf(position));

                Intent intent = new Intent(MainActivity.this, RedditActivity.class);
                intent.putExtra("url", URL);
                startActivity(intent);
            }
        });
    }
}

