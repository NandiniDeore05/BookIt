package com.example.bookit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private TextView heytv;
    private Spinner select_bus;
    private EditText select_date;
    private Button search_buses;

    ArrayList<String> bus_spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heytv = findViewById(R.id.heytv);
        select_bus = findViewById(R.id.select_bus);
        select_date = findViewById(R.id.select_date);
        search_buses = findViewById(R.id.search_buses);

        //select_bus.setOnItemClickListener(this);

        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<String> arrayList = new ArrayList<String>();
        String[] strs ={"05:55","06:05","06:15","06:25","06:35","06:45","06:50","06:55","07:00","07:05","07:10","07:15","07:20","07:25","07:30","07:45","07:50","07:55","08:00","08:05","08:10","08:15","08:20","08:25", "08:35","08:50","09:00","09:05","09:10","09:15","09:20","09:25","09:30","09:35","09:45","10:00", "10:10","10:15","10:20","10:25","10:30","10:35","10:45","10:55","11:10","11:20","11:30","11:40","11:55","12:05","12:10","12:15","12:20","12:25","12:35","12:50","13:00","13:05","13:10","13:15","13:20","13:25","13:30","13:35","13:40","13:55","14:00","14:05","14:10","14:15","14:20","14:25"};

        for(int i=0;i<strs.length;i++)
            arrayList.add(strs[i]);

        map.put("time",arrayList);
        db.collection("Buses").document("Swargate To Pune Station").set(map).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Toast.makeText(MainActivity.this, "Swargate To Pune Station", Toast.LENGTH_SHORT).show();
            }
        });*/

        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        String txt_email = currentUser.getEmail();

        FirebaseFirestore db =FirebaseFirestore.getInstance();
        db.collection("Passenger").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
            {
                for(DocumentSnapshot snap : value)
                {
                    System.out.println(snap.getString("id")+" " +txt_email.equals(snap.getString("email")) + txt_email + "   ############");
                    if( txt_email.equalsIgnoreCase(snap.getString("email")) )
                    {
                        heytv.setText("Hey "+snap.getString("fname")+"..!!");
                    }
                }
            }
        });

        bus_spinner = new ArrayList<String>();
        bus_spinner.add("Select Bus");

        db.collection("Buses").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
            {
                for(DocumentSnapshot snap : value)
                {
                    bus_spinner.add(snap.getId());
                }
            }
        });
        ArrayAdapter adapter = new ArrayAdapter(this , android.R.layout.simple_spinner_dropdown_item, bus_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_bus.setAdapter(adapter);

        Intent intent=new Intent(MainActivity.this , MainActivity2.class);

        select_bus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0) {
                    intent.putExtra("bus" , "");       // making it empty
                    return;
                }
                intent.putExtra("bus" , bus_spinner.get(position));
                //setDestSpinnerList(bus_no.getSelectedItem()final String[] txt_bus = {new String()};.toString());
                //dest.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                return;
            }
        });


        search_buses.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String txt_date = select_date.getText().toString();
                intent.putExtra("date",txt_date);
                System.out.println(intent.getStringExtra("bus") +"   ##################1");
                System.out.println(txt_date+"   ##################3");
                if(  TextUtils.isEmpty(intent.getStringExtra("bus"))  &&  TextUtils.isEmpty(txt_date) )
                    Toast.makeText(MainActivity.this, "Please Select Bus & Date", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(txt_date))
                    Toast.makeText(MainActivity.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(intent.getStringExtra("bus")))
                    Toast.makeText(MainActivity.this, "Please Select Bus", Toast.LENGTH_SHORT).show();
                else
                {
                    startActivity(intent);
                }
            }
        });

    }
}