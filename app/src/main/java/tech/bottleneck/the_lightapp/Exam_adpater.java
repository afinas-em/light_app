package tech.bottleneck.the_lightapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Exam_adpater extends RecyclerView.Adapter<Exam_adpater.ViewHolder> {

    private Context context;
    private List<Exam_model> examlist;

    public Exam_adpater(Context context, List<Exam_model> examlist) {
        this.context = context;
        this.examlist = examlist;
    }


    @NonNull
    @Override
    public Exam_adpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_exam, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Exam_adpater.ViewHolder viewHolder, int i) {
        final Exam_model exam_model = examlist.get(i);
        viewHolder.examttittle.setText(exam_model.getTittle());
        viewHolder.examstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQuestions(exam_model.getId());
            }
        });
    }

    private void getQuestions(final int id) {

        StringRequest request = new StringRequest(Request.Method.GET, "http://loopdata.in/Sem1_c/get_exams_app/42/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject res = new JSONObject(response);
                    JSONObject result = res.getJSONObject("result");
                    JSONArray item = result.getJSONArray("item");
                    ArrayList<QuizModel> list = new ArrayList<>();
                    for (int i = 0; i < item.length(); i++) {

                        JSONObject obj = item.getJSONObject(i);

                        QuizModel model = new QuizModel();
                        model.setOptionB(obj.getString("option-B"));
                        model.setOptionC(obj.getString("option-C"));
                        model.setOptionA(obj.getString("option-A"));
                        model.setQuestion(obj.getString("question"));
                        model.setAnswer(obj.getString("answer"));
                        model.setOptionD(obj.getString("option-D"));
                        model.setModule(obj.getString("module"));
                        model.setName(obj.getString("name"));
                        model.setId(obj.getString("id"));
                        model.setCategory(obj.getString("category"));
                        model.setExamName(obj.getString("exam_name"));

                        list.add(model);

                    }

                    if(list.isEmpty()){
                        Toast.makeText(context, "No questions found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    context.startActivity(new Intent(context, Quiz.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putParcelableArrayListExtra("data", list)
                            .putExtra("exam_id",id)
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(context).add(request);


    }

    @Override
    public int getItemCount() {
        return examlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView examttittle, examstatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            examttittle = itemView.findViewById(R.id.examname);
            examstatus = itemView.findViewById(R.id.status);
        }
    }
}
