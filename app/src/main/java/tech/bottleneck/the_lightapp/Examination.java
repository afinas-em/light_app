package tech.bottleneck.the_lightapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Examination extends AppCompatActivity {

    private static final String Exam_URL = "http://loopdata.in/Sem1_c/get_exam_category_app/42/FIQH/Module-3";
    Spinner module, categry;
    BottomNavigationView bottombar;
    private RecyclerView exmlist;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private RecyclerView.Adapter adapter;
    private List<Exam_model> exam_modelList;

    // List<Exam_model> exam_modelList;
    // RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        bottombar = (BottomNavigationView) findViewById(R.id.bottmnav);
        //exmlist = findViewById(R.id.main_list);
        exam_modelList = new ArrayList<>();
        adapter = new Exam_adpater(getApplicationContext(), exam_modelList);
        module = findViewById(R.id.modulespinner);
        categry = findViewById(R.id.categoryspinner);

        exmlist = findViewById(R.id.main_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        exmlist.setLayoutManager(manager);
        exmlist.setAdapter(adapter);

        modulespinner();
        catogoryspinner();
        Bottombarass();
        Toast.makeText(Examination.this,"Select your subject & Module ",Toast.LENGTH_SHORT).show();

    }


    private void modulespinner() {
        List<String> modulee = new ArrayList<>();

        modulee.add("Select Module");
        modulee.add("Module-1");
        modulee.add("Module-2");
        modulee.add("Module-3");

        ArrayAdapter<String> moduleAdapter;
        moduleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modulee);
        moduleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        module.setAdapter(moduleAdapter);

        module.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    getData();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void catogoryspinner() {
        List<String> category = new ArrayList<>();

        category.add("Select Category");
        category.add("QURAN");
        category.add("Seerah");
        category.add("Fiqh");
        category.add("Aqeeda");

        ArrayAdapter<String> categoryadapter;
        categoryadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, category);
        categoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categry.setAdapter(categoryadapter);

        categry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getData() {
        SharedPreferences pref = getSharedPreferences("DEFAULT", MODE_PRIVATE);
        String user_id = pref.getString("user_id", "121");

        final String selectcategory = categry.getSelectedItem().toString();
        final String selectmodule = module.getSelectedItem().toString();

        if (selectmodule.equals("Select Module") || selectcategory.equals("Select Category")) {
            exam_modelList.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Please select details", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading please wait");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://loopdata.in/Sem1_c/get_exam_category_app/"+user_id+"/" + selectcategory + "/" + selectmodule, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.getJSONObject("result");
                    JSONArray exams = result.getJSONArray("item");
                    exam_modelList.clear();
                    for (int i = 0; i < exams.length(); i++) {
                        JSONObject examObject = exams.getJSONObject(i);
                        int id = examObject.getInt("id");
                        String name = examObject.getString("name");
                        String category = examObject.getString("category");
                        String module = examObject.getString("module");
                        String status = examObject.getString("status");


                        Exam_model exam_model = new Exam_model(id, name, category, module, status);
                        exam_modelList.add(exam_model);

                    }
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Examination.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    //bottombar


    @Override
    protected void onResume() {
        super.onResume();
        bottombar.setSelectedItemId(R.id.exm);
    }

    private void Bottombarass() {
        bottombar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {


                    case R.id.hme:
                        Intent intent = new Intent(getApplicationContext(), Homepage.class);
                        startActivity(intent);
                        return true;
                    case R.id.msge:
                        Intent intent4 = new Intent(getApplicationContext(), Messages.class);
                        startActivity(intent4);
                        return true;
                    case R.id.exm:
//                        Toast.makeText(getApplicationContext(),"youre currently in examination",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.pfil:
                        Intent intent3 = new Intent(getApplicationContext(), Profile.class);
                        startActivity(intent3);
                        return true;


                }
                return false;

            }
        });
    }
}
