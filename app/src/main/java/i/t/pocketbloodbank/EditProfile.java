package i.t.pocketbloodbank;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class EditProfile extends Activity {

    String userPath;
    private final static int PICK_IMAGE_NUM = 107;

    private FirebaseUser currentuser;
    private FirebaseAuth showSingleUserAuth;
    private DatabaseReference editUserDatabaseRef, myRef,Teamref,addteam;
    private StorageReference editUserStorageReference;

    EditText editUserName;
    EditText editUserBloodgrp;
    EditText editUserAvailabilty;
    EditText editUserPhoneNumber;
    EditText editUserLocation;
    Button doneEditButton;
    ImageButton selectImage;
    ImageView editProfilePicture;

    String dpURL;
    Uri imageURI;

    User user;
    String Useremail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // userPath = getIntent().getStringExtra("showUser");
        showSingleUserAuth = FirebaseAuth.getInstance();
        currentuser = showSingleUserAuth.getCurrentUser();
        Useremail = currentuser.getEmail();
        editUserDatabaseRef = FirebaseDatabase.getInstance().getReference("profile");
        myRef = editUserDatabaseRef.child(Useremail.replace('.','&'));
        editUserStorageReference = FirebaseStorage.getInstance().getReference("profilepictures");
        Teamref = FirebaseDatabase.getInstance().getReference("Teams");
        addteam = FirebaseDatabase.getInstance().getReference("user_team").child(Useremail.replace('.','&')).child("teamname");


        editUserName = findViewById(R.id.editUserName);
        editUserPhoneNumber = findViewById(R.id.editUserPhone);
        //editUserLocation = findViewById(R.id.editUserLocation);
        doneEditButton = findViewById(R.id.editUserDone);
        selectImage = findViewById(R.id.editUserSelectButton);
        editProfilePicture = findViewById(R.id.editUserProfileImage);





        //doneEditButton.setVisibility(View.GONE);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                editUserName.setText(user.getName());
                editUserPhoneNumber.setText(user.getPhoneNumber());
              //  editUserLocation.setText(user.getLocation());
             //   editUserBloodgrp.setText(user.getBloodGrp());
             //   editUserAvailabilty.setText(user.getAvailability());
                dpURL = user.getDpURL();

                System.out.println("$$$$$$$" + user.getDpURL());
                Glide.with(getApplicationContext())
                        .load(user.getDpURL())
                        .into(editProfilePicture);

                doneEditButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        doneEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goEditProfile();
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    public void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_NUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_NUM && resultCode == RESULT_OK && data != null && data.getData()!=null){
            imageURI = data.getData();

            Glide.with(this)
                    .load(imageURI)
                    .into(editProfilePicture);
        }
    }

    public void goEditProfile(){

        final User changedUser = user;


        changedUser.setName(editUserName.getText().toString().trim());
        changedUser.setLocation(editUserLocation.getText().toString().trim());
        changedUser.setBloodGrp(editUserBloodgrp.getText().toString().trim());
        changedUser.setPhoneNumber(editUserPhoneNumber.getText().toString().trim());
        changedUser.setDpURL(dpURL);
        changedUser.setAvailability(editUserAvailabilty.getText().toString().trim());

        if(imageURI != null){
            doneEditButton.setVisibility(View.GONE);
            final StorageReference fileReference = editUserStorageReference.child(user.getEmail().replace('.','&'));

            StorageTask uploadTask = fileReference.putFile(imageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("Picture Gese.");
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    dpURL = uri.toString();
                                    changedUser.setDpURL(dpURL);
                                    myRef.setValue(changedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //addteam.setValue(changedUser.getTeam());
                                            Toast.makeText(EditProfile.this,"Update Done",Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    });
                                }
                            });

                            doneEditButton.setVisibility(View.VISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // System.out.println("Picture Hoynai");
                            Toast.makeText(EditProfile.this,"Failed",Toast.LENGTH_LONG).show();
                            doneEditButton.setVisibility(View.VISIBLE);
                            myRef.setValue(changedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //addteam.setValue(changedUser.getTeam());
                                    Toast.makeText(EditProfile.this,"Update Done",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        }
                    });


        }
        else{
            myRef.setValue(changedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //addteam.setValue(changedUser.getTeam());
                    Toast.makeText(EditProfile.this,"Update Done",Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }

    }
}