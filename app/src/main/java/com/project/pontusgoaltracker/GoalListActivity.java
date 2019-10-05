package com.project.pontusgoaltracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class GoalListActivity extends AppCompatActivity {



    static Goal clickedGoal;
    Context context = this;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference usersRef;

    private RecyclerView goalRecyclerView;
    TextView goalTitle,goalDescription,deadline,percentageComplete,goalType ;

    private GoalAdapter mAdapter;

    List<Goal> userGoals ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database= FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        DatabaseReference userGoalsPath  = database.getReference("users/"+user.getUid()+"/userGoals");
//        usersRef = FirebaseDatabase.getInstance().getReference("users");
//        usersRef.keepSynced(true);

        setContentView(R.layout.activity_goal_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Goals List");

        ProgressBar progressBar3 = findViewById(R.id.progressBar3);
        progressBar3.setVisibility(View.VISIBLE);
        //userGoals = new ArrayList<>();
        GoalLab crimeLab = GoalLab.get(this);
        userGoals= crimeLab.getGoals();
        goalRecyclerView = findViewById(R.id.goal_list_recycler);
        goalRecyclerView.setLayoutManager(new LinearLayoutManager(GoalListActivity.this));

        progressBar3.setVisibility(View.GONE);
        updateUI();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GoalListActivity.this,NewGoals.class);
                startActivity(i);
            }
        });
        Log.w("*********************", "before entering" + user.getUid());

        //todo: uncomment below

//        userGoalsPath.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Goal object and use the values to update the UI
//                userGoals = new ArrayList<Goal>();
//                for (DataSnapshot snapshotNode: dataSnapshot.getChildren()) {
//                    userGoals.add(snapshotNode.getValue(Goal.class));
//                }
//                progressBar3.setVisibility(View.GONE);
//                updateUI();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadGoals:onCancelled", databaseError.toException());
//                // ...
//            }
//        });





    }


    private void updateUI() {
        //GET LIST OF GOALS ATTACHED TO THIS USER and add it to recycler view
         GoalLab goalLab = GoalLab.get(this);
               userGoals= goalLab.getGoals();
        mAdapter = new GoalAdapter(userGoals);
        goalRecyclerView.setAdapter(mAdapter);
    }



    public void launchSignInPage(){
        Intent intent = new Intent(this,SignIn.class);
        startActivity(intent);
        finish();
    }

    private class GoalAdapter extends RecyclerView.Adapter<GoalHolder> {
        private List<Goal> goals;

         GoalAdapter(List<Goal> goals) {
            this.goals = goals;
        }

        @Override
        public GoalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            return new GoalHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(GoalHolder holder, int position) {
            Goal goal = goals.get(position);
            holder.bind(goal);
        }
        @Override
        public int getItemCount() {
            return goals.size();
        }


    }

    private class GoalHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        GoalHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.goal_list_item, parent, false));
            goalTitle= itemView.findViewById(R.id.goal_Title);
            deadline=itemView.findViewById(R.id.deadline_date_tv);
            percentageComplete= itemView.findViewById(R.id.percentage_tv);
            goalType = itemView.findViewById(R.id.goat_type_tv);
            itemView.setOnClickListener(this);
        }

        private Goal goal;

        void bind(Goal goal) {
            this.goal = goal;
            String formattedDate;
            if(goal.getDeadline()!=null){
                formattedDate = (goal.getDeadline());
            }
            else formattedDate="undated";

            goalTitle.setText(goal.getTitle());
            deadline.setText(formattedDate);
            goalType.setText(goal.getType());
            percentageComplete.setText(goal.calculatePercentageComplete()+"");


        }

        @Override
        public void onClick(View view) {
            clickedGoal = goal;
            Intent intent = new Intent(GoalListActivity.this,GoalDetailsActivity.class);
            //todo:  send the clicked goal through  the intent
//            intent.putExtra(Goal.GOAL_ID_STRING,goal.getGoalIdString());

            Snackbar.make(view, "snackbar: the goal detail activity would launch", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            startActivity(intent);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if( user==null){
            Intent intent = new Intent(this , SignIn.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        updateUI();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        super.onCreateOptionsMenu(menu);
        inflater.inflate(R.menu.goal_list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.signout_item:
                warningDialog().show();
//                mAuth.signOut();
//                //delete all goals from local storage
//                GoalLab.get(this).deleteAllGoals();
//                launchSignInPage();
                return super.onOptionsItemSelected(item);

            default: return super.onOptionsItemSelected(item);

        }
    }


    public void syncGoals(){

    }

    public AlertDialog.Builder warningDialog(){
       AlertDialog.Builder builder =  new AlertDialog.Builder(this)
                .setTitle("logout?")
                .setMessage("Logging out deletes all data stored on your device!, you should do sync first to prevent data loss. \n" +
                        "Ensure you have a stable data connection before logging out.\n" +
                        "If your network connection is poor you will still be logged out and lose data")
                .setPositiveButton("who cares?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        //delete all goals from local storage
                        GoalLab.get(GoalListActivity.this).deleteAllGoals();
                        launchSignInPage();
                    }
                })
                .setNeutralButton("sync ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //DO SYNC
                        for(Goal goal : GoalLab.get(GoalListActivity.this).getGoals()){
                           if(goal!=null){DatabaseReference goalPath= database.getReference("users/"+user.getUid()+"/userGoals/"+goal.getGoalIdString());
                           goalPath.setValue(goal).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   mAuth.signOut();
                                   //delete all goals from local storage
                                   GoalLab.get(GoalListActivity.this).deleteAllGoals();
                                   launchSignInPage();
                               }
                           });}
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

       return builder;


    }

}
