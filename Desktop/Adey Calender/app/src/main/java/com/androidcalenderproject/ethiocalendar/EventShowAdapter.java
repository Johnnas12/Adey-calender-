package com.androidcalenderproject.ethiocalendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventShowAdapter extends RecyclerView.Adapter<EventShowAdapter.viewHolder> {
    Context context;
    DBOpenHelper dbOpenHelper;

    public EventShowAdapter(Context context, ArrayList<Events> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    ArrayList<Events> arrayList ;

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        int x = position;
     final Events events = arrayList.get(position);
        holder.Event.setText(events.getEVENT());
        holder.DateTxt.setText(events.getDATE());
        holder.Time.setText(events.getTIME());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCalEvent(events.getEVENT(), events.getDATE(), events.getTIME());
                arrayList.remove(x);
                notifyDataSetChanged();
            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
       TextView DateTxt, Event, Time;
       Button delete;
       public viewHolder(@NonNull View itemView) {
           super(itemView);
           DateTxt = itemView.findViewById(R.id.eventdate);
           Event= itemView.findViewById(R.id.eventname);
           Time = itemView.findViewById(R.id.eventtime);
           delete = itemView.findViewById(R.id.delete);

       }
   }
   private  void deleteCalEvent(String event, String date, String time){
        dbOpenHelper= new DBOpenHelper(context);
        SQLiteDatabase database1 = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.deleteEvent(event, date, time, database1);
        dbOpenHelper.close();
   }
}
