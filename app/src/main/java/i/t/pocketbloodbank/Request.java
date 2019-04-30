package i.t.pocketbloodbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class Request extends Activity {

    private FirebaseUser currentuser;
    private FirebaseAuth showSingleUserAuth;
    private DatabaseReference editUserDatabaseRef,databaseReference,bloodref,searchdata, myRef,Teamref,addteam;
    private StorageReference editUserStorageReference;

    EditText editPhoneNumber;
    Button play, pause;
    String userkkey;

    Spinner locationspinner, bloodspinner;
    donor donorman;
    String slocation, sblood, contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        databaseReference = FirebaseDatabase.getInstance().getReference("City");
        bloodref = FirebaseDatabase.getInstance().getReference("Blood");
        searchdata = FirebaseDatabase.getInstance().getReference("search");

        locationspinner = findViewById(R.id.spinner3);
        bloodspinner = findViewById(R.id.spinner2);
        editPhoneNumber = findViewById(R.id.searchContact);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> teams = new ArrayList<String>();
                for (DataSnapshot TeamSnapshot: dataSnapshot.getChildren()) {
                    String teamName = TeamSnapshot.child("name").getValue(String.class);

                    teams.add(teamName);

                }
                ArrayAdapter<String> teamsAdapter = new ArrayAdapter<String>(Request.this, R.layout.myspinner, teams);
                teamsAdapter.setDropDownViewResource(R.layout.myspinner);
                locationspinner.setAdapter(teamsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bloodref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> teams = new ArrayList<String>();
                for (DataSnapshot TeamSnapshot: dataSnapshot.getChildren()) {
                    String teamName = TeamSnapshot.child("group").getValue(String.class);

                    teams.add(teamName);

                }
                ArrayAdapter<String> teamsAdapter = new ArrayAdapter<String>(Request.this, R.layout.myspinner, teams);
                teamsAdapter.setDropDownViewResource(R.layout.myspinner);
                bloodspinner.setAdapter(teamsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        pause.setVisibility(View.GONE);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(locationspinner.getSelectedItem().toString().equals("none")) slocation = "";
                else slocation = locationspinner.getSelectedItem().toString();

                if(bloodspinner.getSelectedItem().toString().equals("none"))sblood = "";
                else sblood = bloodspinner.getSelectedItem().toString();

                contact = editPhoneNumber.getText().toString().trim();

                if(TextUtils.isEmpty(contact)) Toast.makeText(Request.this, "Please fill out all the sections",Toast.LENGTH_LONG).show();

                userkkey = slocation + "&" + sblood;
                System.out.println(sblood + " " + slocation + " "+ contact);
                //usekey = slocation+"&"+sblood;
                //searchdata = FirebaseDatabase.getInstance().getReference(userkkey);
                donorman = new donor(contact);

                searchdata.child(userkkey).child(contact).setValue(donorman);
                //searchdata.child(userkkey).child(contact).setValue(donorman);
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
            }
        });

    }
}
