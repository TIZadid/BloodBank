package i.t.pocketbloodbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Requestlist extends Activity {
    private static final String TAG = "MyParticipation";

    //add Firebase Database stuff
    private FirebaseAuth userAuthentication;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef, proref, extraref;
    private DatabaseReference myRef2;
    private String userID, email;
    private TextView textView;
    public String currTime;
    ArrayList<donor> array = new ArrayList<>();
    ListView listView;

    User userman;
    String blood, location, contac;
    String userkey;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_donor);


        bundle = getIntent().getExtras();

        Log.d(TAG, "Dhuksi\n");

        mAuth = FirebaseAuth.getInstance();
        userAuthentication = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("search");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        email = user.getEmail();
        proref = FirebaseDatabase.getInstance().getReference("profile");

        proref.child(email.replace('.', '&')).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userman = dataSnapshot.getValue(User.class);

                blood = userman.getBloodGrp();
                location = userman.getLocation();
                contac = userman.getPhoneNumber();
                System.out.println(blood + " " + location + " " + contac);
                userkey = location + "&" + blood;

                myRef.child(userkey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        showData(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    //toastMessage("Successfully signed out.");
                }
                // ...
            }
        };
        //userkey ="search/"+ location + "&" + blood;

        //myRef = FirebaseDatabase.getInstance().getReference(userkey);
//        extraref = myRef.child(userkey).getRef();



    }


    private void showData(DataSnapshot dataSnapshot) {

        Log.d(TAG, "show data te dhuksi");
        FirebaseUser user = mAuth.getCurrentUser();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            String contact = (String) ds.child("contact").getValue();
            donor don = new donor(contact);
            array.add(don);
        }

            listView = (ListView) findViewById(R.id.listview);

            MyAdapter adapter = new MyAdapter(this, array);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(OngoingEvents.this, "Hello", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Requestlist.this, shownum.class);
                    intent.putExtra("contact", array.get(position).getContact());
                    //Intent intent = new Intent(MyParticipation.this, EventDetailFull.class);

                     startActivity(intent);

                }
            });



    }
}