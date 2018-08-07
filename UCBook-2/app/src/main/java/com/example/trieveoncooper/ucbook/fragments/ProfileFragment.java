package com.example.trieveoncooper.ucbook.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trieveoncooper.ucbook.Classes.User;
import com.example.trieveoncooper.ucbook.R;
import com.example.trieveoncooper.ucbook.activities.Forum;
import com.example.trieveoncooper.ucbook.activities.Menu;
import com.example.trieveoncooper.ucbook.activities.UserList;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

import static com.example.trieveoncooper.ucbook.live.BaseLiveService.FIRE_BASE_REFERENCE;
import static com.example.trieveoncooper.ucbook.live.BaseLiveService.currentUser;
import static com.example.trieveoncooper.ucbook.live.BaseLiveService.readReference;
import static com.example.trieveoncooper.ucbook.live.BaseLiveService.userSet;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
View view;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth =  mAuth = FirebaseAuth.getInstance();
       // setCurrentUser();

        FirebaseUser user = mAuth.getCurrentUser();


        view  = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView nameField= (TextView)view.findViewById(R.id.accountNameDisplay);
        TextView bioField= (TextView)view.findViewById(R.id.accountBioDisplay);

        ImageView pic = view.findViewById(R.id.profilePic);
        if(userSet){
            Log.d("a","USER SET"+currentUser.getPhotoURL());
            nameField.setText(currentUser.getName());
            bioField.setText(currentUser.getBio());
            if(currentUser.getPhotoURL() != null)
            Picasso.with(getContext()).load(currentUser.getPhotoURL()).into(pic);

        }

        //nameField.setText(user.getName());
       // bioField.setText(user.getBio());
        Button postBook = (Button)view.findViewById(R.id.postButton);
        postBook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getActivity(), Forum.class);
                getActivity().startActivity(activityChangeIntent);
            }
        });
        Button userListButton = (Button)view.findViewById(R.id.userListButton);
        userListButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getActivity(), UserList.class);
                getActivity().startActivity(activityChangeIntent);
            }
        });
        return view;

    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d("a","in onstart profile");


    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void setCurrentUser(){
        Firebase df = new Firebase(FIRE_BASE_REFERENCE);
        final FirebaseUser user = mAuth.getCurrentUser();
        Log.d("a", "THE UID OF THE CURRENT USER"+user.getUid());

        df.child("data").child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot dataSnapShot : dataSnapshot.getChildren()) {
                         User user = dataSnapShot.getValue(User.class);
                         currentUser = user;
                         userSet = true;
                         Log.d("a", "THE DATASNAP KEY  asdad Y\n\n\n"+user.isFirstLogin()+user.getBio());
                         if(userSet) {
                             TextView nameField = (TextView) view.findViewById(R.id.accountNameDisplay);
                             TextView bioField = (TextView) view.findViewById(R.id.accountBioDisplay);
                             ImageView profilePic = view.findViewById(R.id.profilePicView);
                             nameField.setText(currentUser.getName());
                             Log.d("a", "THE PHOTOT URLLLLLLLY\n\n\n"+user.getPhotoURL());

                             bioField.setText(currentUser.getBio());
                             new DownloadImageTask((ImageView) view.findViewById(R.id.profilePicView))
                                     .execute(user.getPhotoURL());

                             //Picasso.with(getActivity()).load(user.getPhotoURL()).into(profilePic);
                         }

                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



}
