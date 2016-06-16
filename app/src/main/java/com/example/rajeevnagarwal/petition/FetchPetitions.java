package com.example.rajeevnagarwal.petition;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class FetchPetitions extends AppCompatActivity {

    ListView list;
    ProgressDialog pDialog;
    private static String url_fetch_product = "http://192.168.225.70:80/petitions/fetch_petition.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_petitions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = (ListView)findViewById(R.id.list);
        new LoadAllPetitions(getApplicationContext()).execute();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.id)).getText()
                        .toString();

                // Starting new intent
                // sending pid to next activity
                Intent i = new Intent(getApplicationContext(),Show_Petition.class);
                i.putExtra("id", pid);

                // starting new activity and expecting some response back
                startActivity(i);
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    class LoadAllPetitions extends AsyncTask<String,String,String>
    {

        private Context context;

        public LoadAllPetitions(Context context) {
            this.context = context;
        }
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog  = new ProgressDialog(FetchPetitions.this);
            pDialog.setMessage("Loading All Petitions. Please wait..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }
        protected String doInBackground(String... args)
        {
            String result="";
            String link = url_fetch_product;
            try {
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader;
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                 result = bufferedReader.readLine();
                System.out.println(result);
                return result;

            }
            catch(Exception e)
            {

            }
            return result;


        }
        protected void onPostExecute(String result)
        {
            pDialog.dismiss();
            System.out.println(result);
            String jsonStr = result;
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonArray = jsonObj.getJSONArray("products");
                    final ArrayList<HashMap<String,String>> ls = new ArrayList<HashMap<String,String>>();

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String a = obj.getString("id");
                        String b = obj.getString("heading");
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id",a);
                        map.put("heading",b);
                        ls.add(map);

                    }
                    /*for(int i=0;i<ls.size();i++)
                    {
                        System.out.println(ls.get(i).);
                    }*/
                    /*CustomAdapter custom = new CustomAdapter(getApplicationContext(),ls);
                    list.setAdapter(custom);*/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListAdapter adapter = new SimpleAdapter(context,ls,R.layout.list_item,new String[]{"id","heading"},new int[]{R.id.id,R.id.heading});
                            list.setAdapter(adapter);
                        }
                    });

                   /* if(query_result.equals("1"))
                        Toast.makeText(context, "Data inserted successfully.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context,"Data insertion failure.",Toast.LENGTH_LONG).show();*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
