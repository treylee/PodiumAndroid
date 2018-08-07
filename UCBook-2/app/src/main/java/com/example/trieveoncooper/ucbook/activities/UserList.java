package com.example.trieveoncooper.ucbook.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trieveoncooper.ucbook.Classes.RecyclerOnTouchListener;
import com.example.trieveoncooper.ucbook.Classes.User;
import com.example.trieveoncooper.ucbook.Classes.UsersAdapter;
import com.example.trieveoncooper.ucbook.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.zxing.common.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.trieveoncooper.ucbook.live.BaseLiveService.FIRE_BASE_REFERENCE;
import static com.example.trieveoncooper.ucbook.live.BaseLiveService.currentUser;
import static com.example.trieveoncooper.ucbook.live.BaseLiveService.userSet;


public class UserList extends AppCompatActivity {
    private List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UsersAdapter mAdapter;
    private Toolbar mToolbar;

    private RecyclerView mUsersList;

    private DatabaseReference mUsersDatabase;

    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list2);
        makeList();

        recyclerView = (RecyclerView) findViewById(R.id.users_list);
        recyclerView.addOnItemTouchListener(new RecyclerOnTouchListener(getApplicationContext(), recyclerView, new RecyclerOnTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                User user = userList.get(position);
                Toast.makeText(getApplicationContext(), user.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent activityChangeIntent = new Intent(UserList.this, Chat.class);
                 activityChangeIntent.putExtra("friendUID", user.getuId());
                activityChangeIntent.putExtra("username", user.getName());
                startActivity(activityChangeIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



    }


    public void makeList() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Firebase df = new Firebase(FIRE_BASE_REFERENCE);
        final FirebaseUser user = mAuth.getCurrentUser();
        df.child("data").child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapShot : dataSnapshot.getChildren()) {
                    DataSnapshot contactSnapshot = dataSnapShot;

                    Iterable<DataSnapshot> contactChildren = contactSnapshot.getChildren();
                        for (DataSnapshot contact : contactChildren) {
                            Log.d("Contact key: ", contact.getKey());
                            User us = contact.getValue(User.class);
                            Log.d("Cusername: ", us.getName());


                            userList.add(us);
                        }


                    Log.d("skippinh: ", dataSnapshot.getKey());
                    // Log.d("a","TESTING USER MAKE LIST \n\n"+u.isFirstLogin());
                    //    userList.add(u);
                    for(User u1: userList){
                        Log.d("print",u1.getEmail());
                    }
                }
                mAdapter = new UsersAdapter(userList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

            }



            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

/*
        df.child("data").child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean completed = false;
                boolean completed2 = false;
                for (DataSnapshot dataSnapShot : dataSnapshot.getChildren()) {
                    DataSnapshot contactSnapshot = dataSnapshot;

                    Iterable<DataSnapshot> contactChildren = contactSnapshot.getChildren();
                    if(!completed) {
                        for (DataSnapshot contact : contactChildren) {
                            Log.d("Contact key: ", contact.getKey());

                            String data = contact.getValue().toString();
                            String result = data.substring(data.indexOf(", ") + 1, data.indexOf("}"));
                            Log.d("result", result);
                            List<String> items = Arrays.asList(result.split("\\s*,\\s*"));
                            String name = items.get(1).substring(items.get(1).indexOf("=") + 1);
                            String bio = items.get(2).substring(items.get(2).indexOf("=") + 1);
                            String email = items.get(3).substring(items.get(3).indexOf("=") + 1);
                            String uid = contact.getKey();
                            Log.d("name ", name);
                            Log.d("bio ", bio);
                            Log.d("email ", email);
                            User u = new User(email);
                            u.setBio(bio);
                            u.setName(name);
                            u.setuId(uid);
                            userList.add(u);
                        }
                        completed = true;

                    }
                    Log.d("skippinh: ", dataSnapshot.getKey());
                    // Log.d("a","TESTING USER MAKE LIST \n\n"+u.isFirstLogin());
                    //    userList.add(u);
                    for(User u1: userList){
                        Log.d("print",u1.getEmail());
                    }
                }
                mAdapter = new UsersAdapter(userList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            }



            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
*/
    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDisplayName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserStatus(String status){

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);



        }
        public void setImageURL(String url, Activity context){
            ImageView imageView = mView.findViewById(R.id.user_single_image);
            Picasso.with(context).load(currentUser.getPhotoURL()).into(imageView);

        }

        public void setUserImage(String thumb_image, Context ctx){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);

            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);

        }


    }

}