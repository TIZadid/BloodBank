package i.t.pocketbloodbank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth showSingleUserAuth;
    private DatabaseReference showSingleUserDatabaseRef,myref;

    TextView name_value;
    TextView email_value;
    TextView bloodgrp;
    TextView phoneNumber_value;
    TextView location_value;
    TextView available;
    ImageView profilePicture;
    ImageButton edit_profilePhoto_button;
    ImageButton edit_profile;

    String  userEmail,nowUser;
    User user;

    //ArrayList<String> userFiltered = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name_value = findViewById(R.id.name_value);
        email_value = findViewById(R.id.email_value);
        bloodgrp = findViewById(R.id.blood_value);
        phoneNumber_value = findViewById(R.id.phone_value);
        location_value = findViewById(R.id.location_value);
        available = findViewById(R.id.Available_value);
        profilePicture = findViewById(R.id.singleUser_profile_image);
        edit_profilePhoto_button = findViewById(R.id.editProfilepicture);
        edit_profile = findViewById(R.id.Editprofile);

        showSingleUserAuth = FirebaseAuth.getInstance();
        currentUser = showSingleUserAuth.getCurrentUser();
        userEmail = currentUser.getEmail();
        //nowUser = getIntent().getStringExtra("showUser");
        System.out.println("users/"+userEmail.replace('.','&'));
        showSingleUserDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        myref = showSingleUserDatabaseRef.child(userEmail.replace('.','&'));
        System.out.println();
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                name_value.setText(user.getName());
                email_value.setText(user.getEmail());
                bloodgrp.setText(user.getBloodGrp());
                phoneNumber_value.setText(user.getPhoneNumber());
                location_value.setText(user.getLocation());
                available.setText(user.getAvailability());
                System.out.println("@@@@@@@@@" + user.getDpURL());
                Glide.with(Profile.this)
                        .load(user.getDpURL())
                        .into(profilePicture);

                if(userEmail.equals(user.getEmail())){
                    edit_profilePhoto_button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEditProfilePicture();
            }
        });

        edit_profilePhoto_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEditProfilePicture();
            }
        });

    }

    public void toEditProfilePicture(){
        Intent intent = new Intent(this,EditProfile.class);
        //  intent.putExtra("showUser","users/"+nowUser.replace('.','&'));
        startActivity(intent);
    }
}
