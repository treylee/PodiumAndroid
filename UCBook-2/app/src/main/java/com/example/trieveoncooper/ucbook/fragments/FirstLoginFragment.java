package com.example.trieveoncooper.ucbook.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trieveoncooper.ucbook.Classes.ImageUploadInfo;
import com.example.trieveoncooper.ucbook.Classes.User;
import com.example.trieveoncooper.ucbook.R;
import com.example.trieveoncooper.ucbook.activities.Forum;
import com.example.trieveoncooper.ucbook.activities.Hub;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.trieveoncooper.ucbook.live.BaseLiveService.FIRE_BASE_REFERENCE;
import static com.example.trieveoncooper.ucbook.live.BaseLiveService.currentUser;
import static com.example.trieveoncooper.ucbook.live.BaseLiveService.userSet;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstLoginFragment extends Fragment {
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int GALLERY = 1, CAMERA = 2;
    ImageView imageview;
    int Image_Request_Code;
    View view;
    TextView directionsLabel;
    TextView label;
    TextView bioBox;
    TextView nameBox;
    Button enterButton;
    int count = 0;
    public String name;
    public String bio;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    String Storage_Path = "profile_pics/";

    // Root Database Name for Firebase Database.
    String Database_Path = "pictures";


    public FirstLoginFragment() {
        // Required empty public constructor
    }

    private void showPictureDialog(){
        // Picasso.with(this).load("https://firebasestorage.googleapis.com/v0/b/ucbok-95490.appspot.com/o/All_Image_Uploads%2F1512624594894.jpg?alt=media&token=cf8fe046-f566-44bb-bbb3-cda400a75ade").into(imageview);

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first_login, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = mAuth = FirebaseAuth.getInstance();
        imageview = (ImageView) view.findViewById(R.id.profilePicView);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        Button addPicButton = view.findViewById(R.id.floginPicButton);
        addPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        label = view.findViewById(R.id.label);
        directionsLabel = view.findViewById(R.id.directionsLabel);
        label.setFocusable(false);
        directionsLabel.setFocusable(false);
        label.setPaintFlags(0);
        directionsLabel.setPaintFlags(0);
        bioBox = view.findViewById(R.id.bioBox);
        nameBox = view.findViewById(R.id.nameBox);
        bioBox.setVisibility(View.INVISIBLE);
        enterButton = view.findViewById(R.id.enterButton);
        enterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (count == 0) {
                    name = nameBox.getText().toString();
                    directionsLabel.setText("Bio- Let others know!");
                    nameBox.setVisibility(View.INVISIBLE);
                    bioBox.setVisibility(View.VISIBLE);
                    count++;
                } else {
                    bio = bioBox.getText().toString();
                    addInfoToDB();
                    UploadImageFileToFirebaseStorage();
                    Intent activityChangeIntent = new Intent(getActivity(), Hub.class);
                    getActivity().startActivity(activityChangeIntent);
                }
            }
        });


        return view;
    }

    public void addInfoToDB() {
        Firebase df = new Firebase(FIRE_BASE_REFERENCE);
        final FirebaseUser user = mAuth.getCurrentUser();
        df.child("data").child("users").child(user.getUid()).child("user").child("name").setValue(name);
        df.child("data").child("users").child(user.getUid()).child("user").child("bio").setValue(bio);
        df.child("data").child("users").child(user.getUid()).child("user").child("firstLogin").setValue(false);


    }
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {
            Log.d("a","FIREPATHURI NOT TEMPTY!");
            // Setting progressDialog Title.


            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            // String TempImageName = ImageName.getText().toString().trim();


                            // Hiding the progressDialog after done uploading.

                            // Showing toast message after done uploading.
                            Toast.makeText(getActivity().getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            FirebaseAuth mauth = FirebaseAuth.getInstance();
                          //  @SuppressWarnings("VisibleForTests")
                          //  ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, taskSnapshot.getDownloadUrl().toString(),tempImage, mauth.getUid());
                          //  Log.d("a","CHECKING THESE TEMP STRINGS"+TempImageName+ " "+tempImage);
                            // Getting image upload ID.
                            //String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            // changes  databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                            databaseReference.child("data").child("users").child(mauth.getUid()).child("photoURL").setValue(taskSnapshot.getDownloadUrl().toString());
                            mDatabase.child("data").child("users").child(mauth.getUid()).child("user").child("photoURL").setValue(taskSnapshot.getDownloadUrl().toString());
                            currentUser.setPhotoURL(taskSnapshot.getDownloadUrl().toString());

                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception exception) {

                            // Hiding the progressDialog.

                            // Showing exception erro message.
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.

                        }
                    });
        }
        else {

            Toast.makeText(getActivity(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Log.d("a","REQUEST CODE"+requestCode);

        if (requestCode == GALLERY) {
            if (data != null) {
                FilePathUri = data.getData();

                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    imageview.setImageBitmap(bitmap);

                    ProgressDialog progressDialog;
                    Image_Request_Code = requestCode;




                } catch (Exception e) {

                }
            }
        } else if (requestCode == CAMERA) {
            FilePathUri = data.getData();
            Log.d("a", "THE FILEPATHHHURL" + FilePathUri);
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ProgressDialog progressDialog;
            Image_Request_Code = requestCode;
            imageview.setImageBitmap(thumbnail);

        }
    }



}