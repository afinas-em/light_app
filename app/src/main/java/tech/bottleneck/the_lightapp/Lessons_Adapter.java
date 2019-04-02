package tech.bottleneck.the_lightapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tech.bottleneck.the_lightapp.model.Result;

public class Lessons_Adapter extends RecyclerView.Adapter<Lessons_Adapter.LessonsViewHolder> {


    private Context mContext;
    private ArrayList<Result> lessons_modelList;


    Lessons_Adapter(Context mContext, ArrayList<Result> lessons_modelList) {
        this.mContext = mContext;
        this.lessons_modelList = lessons_modelList;
    }

    @NonNull
    @Override
    public LessonsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.model_lesson,viewGroup,false);
        return new LessonsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonsViewHolder lessonsviewHolder, int i) {
        final Result result = lessons_modelList.get(i);
        lessonsviewHolder.lessontittle.setText(result.getTitle());
        lessonsviewHolder.desriptn.setText(result.getDescription());
        lessonsviewHolder.cardviewitm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,Course_view.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("RESULT",result);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lessons_modelList.size();
    }


    class LessonsViewHolder extends RecyclerView.ViewHolder{
        TextView lessontittle,desriptn,viewmore;
        CardView cardviewitm;

        LessonsViewHolder(@NonNull View itemView) {
            super(itemView);
            lessontittle = itemView.findViewById(R.id.lessonname);
            desriptn =itemView.findViewById(R.id.lessondesc);
            viewmore =itemView.findViewById(R.id.viewmore);
            cardviewitm = itemView.findViewById(R.id.cardviewleson);

        }
    }

}
