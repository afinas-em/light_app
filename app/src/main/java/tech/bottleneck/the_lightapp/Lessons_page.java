package tech.bottleneck.the_lightapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tech.bottleneck.the_lightapp.model.Data;
import tech.bottleneck.the_lightapp.model.Result;

public class Lessons_page extends AppCompatActivity {
    private static final String URL_MODULE = "http://loopdata.in/Sem1_c/get_lesson_unit_module_app/42/QURAN/module/unit";
    public TextView txtSubject;
    Spinner module, unit;
    String subject;
    ArrayList<Result> results = new ArrayList<>();
    RecyclerView recyclerviewlsn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_page);
        datainitial();

        getData();
        setData();
        setLessons(results);
        unitspinner();
        modulespinner();

//        getdataspinner(subject);

//        Toast.makeText(Lessons_page.this, (results).toString(), Toast.LENGTH_LONG).show();
    }

    //expiriment subject


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
        module.setSelected(false);
        module.setSelection(0, true);

        module.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getdataspinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void unitspinner() {
        List<String> unitt = new ArrayList<>();

        unitt.add("Select Unit");
        unitt.add("Unit-1");
        unitt.add("Unit-2");
        unitt.add("Unit-3");

        ArrayAdapter<String> unitAdapter;
        unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitt);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(unitAdapter);
        unit.setSelected(false);  // must
        unit.setSelection(0, true);  //must

        unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                getdataspinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void datainitial() {
        module = findViewById(R.id.spinnermodule);
        unit = findViewById(R.id.spinerunit);
        txtSubject = findViewById(R.id.subjecthead);
        recyclerviewlsn = findViewById(R.id.recyclerview);
    }

    private void getData() {
        results = getIntent().getParcelableArrayListExtra("RESULT");
        subject = results.get(0).getCategory();
    }

    private void setData() {
        txtSubject.setText(subject);
    }

    private void setLessons(ArrayList<Result> results) {
        Lessons_Adapter lessonsAdapter = new Lessons_Adapter(this, results);
        recyclerviewlsn.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewlsn.setAdapter(lessonsAdapter);

    }

    public void getdataspinner() {
        SharedPreferences pref = getSharedPreferences("DEFAULT", MODE_PRIVATE);
        String user_id = pref.getString("user_id", "121");

        //Log.e("kk","http://loopdata.in/Sem1_c/get_lesson_unit_module_app/42/"+subject);


        final String selectmdule = module.getSelectedItem().toString();
        final String selectunit = unit.getSelectedItem().toString();

        if (selectunit.equals("Select Unit") || selectmdule.equals("Select Module")) {
            setLessons(results);
            return;
        }

        String url = "http://loopdata.in/Sem1_c/get_lesson_unit_module_app/" + user_id + "/" + subject + "/" + selectunit + "/" + selectmdule;
//      http://loopdata.in/Sem1_c/get_lesson_unit_module_app/42/SEERAH/Module-1/Unit-1;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("result")) {
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(response);
                                processResponse(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error Registering", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void processResponse(JSONObject response) {

        Log.e("AA", "processResponse: " + response.toString());

        Gson gson = new Gson();
        Data model = gson.fromJson(response.toString(), Data.class);
        ArrayList<Result> result;
        result = model.getResult();

        setLessons(result);
    }


}
