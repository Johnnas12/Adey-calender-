package com.androidcalenderproject.ethiocalendar;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class EthioCalendarView  extends LinearLayout {
    ImageButton nextBtn,backBtn;
    TextView CurrentDate;
    GridView gridview;
    private static final  int MAX_CALENDAR_DAYS=42;//
    Calendar calendar= Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat= new SimpleDateFormat("MMMM yyyy",Locale.ENGLISH);
    SimpleDateFormat monthformat=  new SimpleDateFormat("MMM",Locale.ENGLISH);
    SimpleDateFormat yearformat = new SimpleDateFormat("yyyy",Locale.ENGLISH);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
    AlertDialog alertDialog;
    GridAdapter gridAdapter;
    List<Date> dates=new ArrayList<>();
    List<Events> eventsList =new ArrayList<>();
    DBOpenHelper dbopenhelper;
    public EthioCalendarView(Context context) {

        super(context);
    }

    public EthioCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context= context;
        IntializeLayout();
        setUpCalendar();

        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(calendar.MONTH,-1);
                setUpCalendar();

            }
        });
        nextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(calendar.MONTH,1);
                setUpCalendar();
            }
        });
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addview = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_event, null);
                EditText EventName = addview.findViewById(R.id.eventname);
                TextView EventTime = addview.findViewById(R.id.eventtime);
                ImageButton settime = addview.findViewById(R.id.seteventtime);
                Button addevent = addview.findViewById(R.id.addevent);
                settime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calender = Calendar.getInstance();
                        int hours = calender.get(Calendar.HOUR_OF_DAY);
                        int minutes = calender.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog= new TimePickerDialog(addview.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                               Calendar c= Calendar.getInstance();
                               c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat Hformat = new SimpleDateFormat("K:mm a" ,Locale.ENGLISH);
                                String event_time = Hformat.format(c.getTime());
                                EventTime.setText(event_time);


                            }
                        }, hours, minutes, false);
                        timePickerDialog.show();

                    }

                });
                String date = eventDateFormat.format(dates.get(position));
                String month = monthformat.format(dates.get(position));
                String year = yearformat.format(dates.get(position));

                addevent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(), EventTime.getText().toString(), date, month, year );
                        setUpCalendar();
                        alertDialog.dismiss();
                    }
                });

                builder.setView(addview);
                alertDialog=builder.create();
                alertDialog.show();
            }
        });

gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long l) {
        String date= eventDateFormat.format(dates.get(position));
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setCancelable(true);
        View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout, null);
        RecyclerView recyclerView = showView.findViewById(R.id.eventsRV);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(showView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        EventShowAdapter eventShowAdapter = new EventShowAdapter(showView.getContext(), CollectEventByDate(date));
        recyclerView.setAdapter(eventShowAdapter);
        eventShowAdapter.notifyDataSetChanged();
        builder.setView(showView);
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                setUpCalendar();
            }
        });


        return true;
    }
});

    }
    private ArrayList<Events> CollectEventByDate(String date){
        ArrayList<Events> arrayList = new ArrayList<>();
        dbopenhelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbopenhelper.getReadableDatabase();
        Cursor cursor = dbopenhelper.ReadEvent(date, database);

        while(cursor.moveToNext()){
            int eventindex= cursor.getColumnIndex(DBStructure.EVENT);
            String event= cursor.getString(eventindex);
            int timeindex= cursor.getColumnIndex(DBStructure.TIME);
            String time= cursor.getString(timeindex);
            int dateindex= cursor.getColumnIndex(DBStructure.DATE);
            String Date= cursor.getString(dateindex);
            int monthindex= cursor.getColumnIndex(DBStructure.MONTH);
            String Month= cursor.getString(monthindex);
            int yearindex= cursor.getColumnIndex(DBStructure.YEAR);
            String Year= cursor.getString(yearindex);

            Events events = new Events(event, time, Date, Month, Year);
            arrayList.add(events);

        }
     cursor.close();
        dbopenhelper.close();
        return arrayList;

    }

    public EthioCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
private void SaveEvent(String event , String time ,String date, String month, String year){
        DBOpenHelper dbOpenHelper= new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date,  month, year, database);
        dbOpenHelper.close();
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show();
}
    private void IntializeLayout(){
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.calendar_layout,this);
        nextBtn= view.findViewById(R.id.nextBtn);
        backBtn=view.findViewById(R.id.backBtn);
        CurrentDate=view.findViewById(R.id.CurentDatetxt);
        gridview =view.findViewById(R.id.gridview);


    }
    private void setUpCalendar(){
        String currentdate= dateFormat.format(calendar.getTime());
       CurrentDate.setText(currentdate);
       dates.clear();
       Calendar monthCalender = (Calendar) calendar.clone();
       monthCalender.set(Calendar.DAY_OF_MONTH,1);
       int FirstDayofMonth = monthCalender.get(Calendar.DAY_OF_WEEK)-1;
       monthCalender.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
       CollectEventsPerMonth(monthformat.format(calendar.getTime()), yearformat.format(calendar.getTime()));

       while(dates.size()<MAX_CALENDAR_DAYS){
           dates.add(monthCalender.getTime());
           monthCalender.add(Calendar.DAY_OF_MONTH,1);
       }
       gridAdapter = new GridAdapter(context, dates, calendar, eventsList);
       gridview.setAdapter(gridAdapter);
    }


    private  void CollectEventsPerMonth(String month, String year){
        eventsList.clear();
        dbopenhelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbopenhelper.getReadableDatabase();
        Cursor curs= dbopenhelper.ReadEventperMonth(month, year, database);
            while(curs.moveToNext()){
                int eventindex= curs.getColumnIndex(DBStructure.EVENT);
                String event= curs.getString(eventindex);
                int timeindex= curs.getColumnIndex(DBStructure.TIME);
                String time= curs.getString(timeindex);
                int dateindex= curs.getColumnIndex(DBStructure.DATE);
                String date= curs.getString(dateindex);
                int monthindex= curs.getColumnIndex(DBStructure.MONTH);
                String Month= curs.getString(monthindex);
                int yearindex= curs.getColumnIndex(DBStructure.YEAR);
                String Year= curs.getString(yearindex);
                Events events = new Events(event, time, date, Month, Year);
                eventsList.add(events);
            }
            curs.close();
            dbopenhelper.close();
        }
    }

