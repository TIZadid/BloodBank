package i.t.pocketbloodbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Register extends Activity {

    EditText password;
    EditText fullname;
    EditText position;
    EditText Bloodgrp;
    EditText email;
    EditText Location;
    EditText phoneNumber;
    EditText Availability;
    Button doneButton;
    Button cancel;
    TextView Teamtext;
    Spinner TeamSpinner;
    RadioButton visibilty,invisibility;
    TextView textBloodgrp, textAvailability;
    TextView textlocation;

    private DatabaseReference databaseReference,profileref,addteam;
    private FirebaseAuth userAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        profileref = FirebaseDatabase.getInstance().getReference("profile");
        userAuthentication = FirebaseAuth.getInstance();

        password = findViewById(R.id.PasswordEditText);
        fullname = findViewById(R.id.PlayerNameEditText);
        Bloodgrp = findViewById(R.id.BloodgrpEditText);
        Availability = findViewById(R.id.AvailabilityEditText);
        Location = findViewById(R.id.PlayerLocationEditText);
        email = findViewById(R.id.PlayerEmailEditText);

        phoneNumber = findViewById(R.id.PlayerPhoneEditText);
        doneButton = findViewById(R.id.idsubmitButton);
        cancel = findViewById(R.id.idcancelButton);


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,loginActivity.class));
            }
        });
    }

    private void addUser() {
        final FirebaseUser hmmuser = userAuthentication.getCurrentUser();
        //System.out.println("asd sfg sfdg sdfg sd " + hmmuser.getEmail());

        final String spassword = password.getText().toString().trim();
        final String sfullname = fullname.getText().toString().trim();
        final String bloodgroup = Bloodgrp.getText().toString().trim() ;
        final String available = Availability.getText().toString().trim() ;
        final String semail = email.getText().toString().trim();
        final String sLocation = Location.getText().toString().trim() ;


        final String sphoneNumber;

        if(TextUtils.isEmpty(phoneNumber.getText().toString().trim())) sphoneNumber = "null";
        else sphoneNumber = phoneNumber.getText().toString().trim();


        if(!Patterns.EMAIL_ADDRESS.matcher(semail).matches()){
            //Toast.makeText(this,"Please input Email Correctly",Toast.LENGTH_LONG).show();
            email.setError("Please input Email Correctly.");
            email.requestFocus();
            return;
        }
        if(spassword.length() < 8){
            //Toast.makeText(this,"Password has to be at least 8 chars",Toast.LENGTH_LONG).show();
            password.setError("Password has to be at least 8 chars");
            password.requestFocus();
            return;
        }


        if(TextUtils.isEmpty(spassword) || TextUtils.isEmpty(sfullname) || TextUtils.isEmpty(bloodgroup) || TextUtils.isEmpty(available) || TextUtils.isEmpty(semail)){
            Toast.makeText(this, "Please fill out all the sections",Toast.LENGTH_LONG).show();
        }else{
            System.out.println("@@@@@@@@@@@" + semail + " " + spassword);
            userAuthentication.createUserWithEmailAndPassword(semail,spassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                User user = new User(semail, sfullname,sphoneNumber,sLocation,bloodgroup,available,"https://i.kym-cdn.com/entries/icons/original/000/003/619/ForeverAlone.jpg");
                                //com.example.plabon.myapplication.User user =
                                String suserkey = sLocation + "&" + bloodgroup;
                                databaseReference.child(suserkey).child(sphoneNumber).setValue(user);
                                profileref.child(semail.replace('.','&')).setValue(user);
                                FirebaseUser hmmttuser = userAuthentication.getCurrentUser();
                                hmmttuser.sendEmailVerification();


                                Toast.makeText(Register.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();

                                email.setText("");
                                password.setText("");
                                phoneNumber.setText("");
                                fullname.setText("");
                                Bloodgrp.setText("");
                                Availability.setText("");

                                Intent intent = new Intent(Register.this, loginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                userAuthentication.signOut();
                                startActivity(intent);
                            }else{
                                if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(Register.this, "Email alredy taken.", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
    }

}