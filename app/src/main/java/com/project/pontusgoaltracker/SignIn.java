package com.project.pontusgoaltracker;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.pontusgoaltracker.models.Goal;
import com.project.pontusgoaltracker.models.GoalLab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SignIn extends AppCompatActivity implements View.OnClickListener {


    EditText EmailET, PwdET;
    CheckBox remember;
    Button button;
    TextView signUP;
    ImageView google, linked, facebook;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference userGoalsPath;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //user = mAuth.getCurrentUser();
        //if(user != null ){
          //  Intent intent = new Intent(SignIn.this, GoalListActivity.class);
            //startActivity(intent);
        //}

        //Initialize all views by ID
        EmailET = findViewById(R.id.email);
        PwdET = findViewById(R.id.password);
        remember = findViewById(R.id.checkBox);
        button = findViewById(R.id.button);
        signUP = findViewById(R.id.signup);
        google = findViewById(R.id.google);
        linked = findViewById(R.id.ln);
        facebook = findViewById(R.id.fb);
        progressBar = findViewById(R.id.progressBar2);


        progressBar.setVisibility(View.GONE);


        //set onclick listeners where necessary

        button.setOnClickListener(this);
        signUP.setOnClickListener(this);
        google.setOnClickListener(this);
        linked.setOnClickListener(this);
        facebook.setOnClickListener(this);


        signUP = findViewById(R.id.signup);
        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //If the sign in button is clicked
            case R.id.button:
//Get the texts from the edittexts
                String LoginEmail = EmailET.getText().toString();
                String LoginPassword = PwdET.getText().toString();

                //Validation
                if (LoginEmail.isEmpty()) {
                    EmailET.setError("Please enter Email");
                    EmailET.requestFocus();
                } else if (LoginPassword.isEmpty()) {
                    PwdET.setError("Enter your Password Please");
                    PwdET.requestFocus();
//Check if email matches normal pattern
                } else if (!Patterns.EMAIL_ADDRESS.matcher(LoginEmail).matches()) {
                    EmailET.setError("Please enter a valid email address");
                    EmailET.requestFocus();
                    return;
                    //if the texts boxes are empty
                } else if (TextUtils.isEmpty(LoginEmail) && TextUtils.isEmpty(LoginPassword)) {

                    Toast.makeText(SignIn.this, "Please fill the Boxes", Toast.LENGTH_SHORT).show();

                    //if they are not empty, Login
                } else if (!(TextUtils.isEmpty(LoginEmail) && TextUtils.isEmpty(LoginPassword))) {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(LoginEmail, LoginPassword).addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //if login is successful
                            if (!task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                user = mAuth.getCurrentUser();

                                Toast.makeText(SignIn.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                //Proceed to next activity
                                loadGoalsFromFirebase();
                                Intent i = new Intent(SignIn.this, GoalListActivity.class);
                                startActivity(i);
                                finish();

                            }

                        }
                    });
                } else {
                    Toast.makeText(SignIn.this, "Error Occured!! ", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.signup:
                Intent i = new Intent(SignIn.this, SignUp.class);
                startActivity(i);

                break;
            case R.id.google:
                Toast.makeText(SignIn.this, "Feature under construction", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ln:
                Toast.makeText(SignIn.this, "Feature under construction", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fb:
                Toast.makeText(SignIn.this, "Feature under construction", Toast.LENGTH_SHORT).show();
                break;


        }

    }

    void loadGoalsFromFirebase(){
        userGoalsPath  = database.getReference("users/"+user.getUid()+"/userGoals");
                userGoalsPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Goal object and use the values to update the UI

                for (DataSnapshot snapshotNode: dataSnapshot.getChildren()) {
                    GoalLab.get(SignIn.this).addGoal(snapshotNode.getValue(Goal.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("ERROR", "loadGoals:onCancelled", databaseError.toException());
                // ...
            }
        });



    }
}
