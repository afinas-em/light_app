package tech.bottleneck.the_lightapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Messages extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private List<Message_item>message_items;



    RequestQueue requestQueue;

    BottomNavigationView bottombar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        recyclerView = (RecyclerView)findViewById(R.id.listmsg);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        message_items = new ArrayList<>();


        bottombar = (BottomNavigationView)findViewById(R.id.bottmnav);

        Toast.makeText(Messages.this,"No Messeges",Toast.LENGTH_SHORT).show();
        loadmsgData();



        Bottombarass();
    }

    private void  loadmsgData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://loopdata.in/Home_c/user_nf_get_app/42", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject jsonObject1= jsonObject.getJSONObject("results");
                    JSONArray array = jsonObject1.getJSONArray("item");

                    for (int i =0; i<array.length();i++){
                        JSONObject o = array.getJSONObject(i);
                        Message_item messges = new Message_item(
                                o.getString("message")
//                                o.getString("id"),
//                                o.getString("sem")
                        );
                        message_items.add(messges);
                    }
                    adapter = new Message_Adapter(message_items,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void Bottombarass(){
        bottombar.setVisibility(View.GONE);
        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){


                    case R.id.hme:
                        Intent intent = new Intent(getApplicationContext(),Homepage.class);
                        startActivity(intent);
                        return true;

                    case R.id.msge:
                        Toast.makeText(getApplicationContext(),"Youre currently in examination",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.exm:
                        Intent intent4 = new Intent(getApplicationContext(),Examination.class);
                        startActivity(intent4);
                        return true;

                    case R.id.pfil:
                        Intent intent3 = new Intent(getApplicationContext(),Profile.class);
                        startActivity(intent3);
                        return true;



                }
                return false;

            }
        });
    }
}
