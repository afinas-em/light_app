package tech.bottleneck.the_lightapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.ViewHolder>  {


    private List<Message_item> message_items;
    private Context context;

    public Message_Adapter(List<Message_item> message_items, Context context) {
        this.message_items = message_items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.messge_model,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Message_item message_item = message_items.get(i);

        viewHolder.msghead.setText(message_item.getMessages());

    }

    @Override
    public int getItemCount() {
        return message_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       public TextView msghead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            msghead = (TextView)itemView.findViewById(R.id.textmsg);
        }
    }


}
