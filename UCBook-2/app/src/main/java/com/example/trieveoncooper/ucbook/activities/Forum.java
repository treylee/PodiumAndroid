package com.example.trieveoncooper.ucbook.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trieveoncooper.ucbook.Classes.Book;
import com.example.trieveoncooper.ucbook.Classes.BookAdapter;
import com.example.trieveoncooper.ucbook.Classes.ImageUploadInfo;
import com.example.trieveoncooper.ucbook.Classes.User;
import com.example.trieveoncooper.ucbook.R;
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
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.trieveoncooper.ucbook.live.BaseLiveService.FIRE_BASE_REFERENCE;
import static com.example.trieveoncooper.ucbook.live.BaseLiveService.currentUser;
import static com.example.trieveoncooper.ucbook.live.BaseLiveService.userSet;

public class Forum extends AppCompatActivity {
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog ;
    String subject1;
    String course1;

    private BookAdapter adapter;
    private List<Book> albumList;
    Spinner  customspinner,customspinner2;
    int Image_Request_Code = 7;
    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";

    // Root Database Name for Firebase Database.
    String Database_Path = "All_Image_Uploads_Database";


    Context context1;

    // Creating URI.
    Uri FilePathUri;
    public ArrayList<SpinnerObject> CustomListViewValuesArr = new ArrayList<SpinnerObject>();
    CustomAdapter adapter1;
    public ArrayList<SpinnerObject> CustomListViewValuesArr2 = new ArrayList<SpinnerObject>();
    CustomAdapter adapter2;
    Forum activity = null;
    TextView priceText;
    SeekBar priceBar;
    TextView titleText;
    ImageView imageview; // pic in post alert
    Button btn; // pic button
     int GALLERY = 1, CAMERA = 2;
     Button postToForum;




    private void subjectSpinner(){

        Resources res = getResources();
        adapter1 = new CustomAdapter(activity, R.layout.spinner_dropdown, CustomListViewValuesArr, res);
        customspinner.setAdapter(adapter1);

        customspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    TextView txt = (TextView) view.findViewById(R.id.dropdown_txt);
                    String text = txt.getText().toString();
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    CustomListViewValuesArr2 = new ArrayList<SpinnerObject>();;
                    CustomListViewValuesArr2.add(0,new SpinnerObject("Choose"));
                    setCourseSpinner(text);
                    subject1 = text;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public  void setCourseSpinner(String subject){
        Firebase df = new Firebase(FIRE_BASE_REFERENCE);
        FirebaseAuth  mAuth = FirebaseAuth.getInstance();
       // String subject =  customspinner.getSelectedItem().toString();
        df.child("subject").child(subject).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int index = 1;
                    for (DataSnapshot dataSnapShot : dataSnapshot.getChildren()) {
                        Log.d("a","yytyytytytyt"+dataSnapShot.getKey());

                        CustomListViewValuesArr2.add(index,new SpinnerObject(dataSnapShot.getKey()));
                        index++;
                        courseSpinner();
                    }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

}
    public  void setSubjectSpinner(){
        Firebase df = new Firebase(FIRE_BASE_REFERENCE);
        FirebaseAuth  mAuth = FirebaseAuth.getInstance();

        df.child("subject").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int index = 1;
                for (DataSnapshot dataSnapShot : dataSnapshot.getChildren()) {
                    Log.d("a","KSADAKD"+dataSnapShot.getKey());
                    CustomListViewValuesArr.add(index,new SpinnerObject(dataSnapShot.getKey()));
                    index++;
                    subjectSpinner();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    private void courseSpinner(){



        Resources res = getResources();
        adapter2 = new CustomAdapter(activity, R.layout.spinner_dropdown, CustomListViewValuesArr2, res);
        customspinner2.setAdapter(adapter2);

        customspinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    TextView txt = (TextView) view.findViewById(R.id.dropdown_txt);
                    String text = txt.getText().toString();
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    course1 = text;
                    Log.d("a","PREPARING ALBUMS");
                    prepareAlbums(subject1,course1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // CustomSpinner Adapter
    public class CustomAdapter extends ArrayAdapter<String> {


        private Activity activity;
        private ArrayList data;
        public Resources res;
        SpinnerObject tempValues = null;
        LayoutInflater inflater;

        public CustomAdapter(
                Forum activitySpinner,
                int textViewResourceId,
                ArrayList objects,
                Resources resLocal
        ) {
            super(activitySpinner, textViewResourceId, objects);

            activity = activitySpinner;
            data = objects;
            res = resLocal;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = inflater.inflate(R.layout.spinner_dropdown, parent, false);
            tempValues = null;
            tempValues = (SpinnerObject) data.get(position);

            TextView txt = (TextView) row.findViewById(R.id.dropdown_txt);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(16);
           // txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_downarrow, 0);
            txt.setText(tempValues.getname());
            txt.setTextColor(Color.parseColor("#1171d0"));
            txt.setSingleLine(true);
            txt.setEllipsize(TextUtils.TruncateAt.END);
            txt.setSingleLine(true);

            return row;
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            View row = inflater.inflate(R.layout.spinner_dropdown, parent, false);
            tempValues = null;
            tempValues = (SpinnerObject) data.get(position);

            TextView txt = (TextView) row.findViewById(R.id.dropdown_txt);
            txt.setText(tempValues.getname());
            txt.setTextSize(17);
            txt.setPadding(0, 30, 0, 30);
            txt.setGravity(Gravity.CENTER);
            txt.setTextColor(Color.parseColor("#1171d0"));
            txt.setBackgroundColor(Color.parseColor("#FFFFFF"));

            return row;
        }
    }

    // CustomSpinner Setter-Getter
    public class SpinnerObject {

        private String name;

        public String getname() {
            return name;
        }

        public SpinnerObject(String name) {
            this.name = name;
        }

    }

    private void showPictureDialog(){
       // Picasso.with(this).load("https://firebasestorage.googleapis.com/v0/b/ucbok-95490.appspot.com/o/All_Image_Uploads%2F1512624594894.jpg?alt=media&token=cf8fe046-f566-44bb-bbb3-cda400a75ade").into(imageview);

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
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

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {
        Log.d("a","FIREPATHURI NOT TEMPTY!");
            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                           // String TempImageName = ImageName.getText().toString().trim();

                            String TempImageName = titleText.getText().toString();
                            String tempImage = String.valueOf(priceBar.getProgress());
                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            FirebaseAuth mauth = FirebaseAuth.getInstance();
                            @SuppressWarnings("VisibleForTests")
                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, taskSnapshot.getDownloadUrl().toString(),tempImage, mauth.getUid());
                            Log.d("a","CHECKING THESE TEMP STRINGS"+TempImageName+ " "+tempImage);
                            // Getting image upload ID.
                            String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                          // changes  databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                            databaseReference.child(subject1).child(course1).child(ImageUploadId).setValue(imageUploadInfo);

                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(Forum.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });
        }
        else {

            Toast.makeText(Forum.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
    Log.d("a","REQUEST CODE"+requestCode);

        if (requestCode == GALLERY) {
            if (data != null) {
                FilePathUri = data.getData();

                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        activity = this;
        context1 = this;
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        progressDialog = new ProgressDialog(Forum.this);
        Button refresh = findViewById(R.id.findButton);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareAlbums(subject1,course1);
            }
        });
        Button postButton = findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Forum.this);
                LayoutInflater inflater = Forum.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.post_alert, null);
                dialogBuilder.setView(dialogView);
                btn = (Button) dialogView.findViewById(R.id.chooseImageButton);
                titleText = dialogView.findViewById(R.id.bookTitleBox);

                imageview = (ImageView) dialogView.findViewById(R.id.picHolder);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPictureDialog();
                    }
                });
                postToForum = dialogView.findViewById(R.id.postForumBook);
                postToForum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        UploadImageFileToFirebaseStorage();
                    }
                });

                priceText = dialogView.findViewById(R.id.priceText);
                priceBar = dialogView.findViewById(R.id.priceBar);
                priceBar.setMax(200);
              //  priceBar.setOnSeekBarChangeListener(Forum.this);
                priceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                        // TODO Auto-generated method stub

                        priceText.setText("$ "+progress);

                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

            }

        });
        CustomListViewValuesArr.add(0,new SpinnerObject("Choose"));
        CustomListViewValuesArr2.add(0,new SpinnerObject("Choose"));

        customspinner = (Spinner) findViewById(R.id.subjectSpinner);
        customspinner2 = (Spinner) findViewById(R.id.courseSpinner);
        setSubjectSpinner();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new BookAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

       // prepareAlbums(course1,subject1);

        try {
            Glide.with(this).load(R.drawable.coverr).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    public boolean inList(Book book){
        for (Book b: adapter.albumList){
            if(b.getuId() == book.getuId() && b.getName() == b.getName()){
                return  true;
            }
        }
        return false;
    }
    private void prepareAlbums(String course,String subject) {
        Firebase df = new Firebase(FIRE_BASE_REFERENCE);
        FirebaseAuth  mAuth = FirebaseAuth.getInstance();
        // String subject =  customspinner.getSelectedItem().toString();
        albumList = new ArrayList<>();
        df.child("All_Image_Uploads_Database").child(course).child(subject).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int index = 1;
                for (DataSnapshot dataSnapShot : dataSnapshot.getChildren()) {
                    Log.d("a","all imhggy lop"+dataSnapShot.getKey());
                      ImageUploadInfo book = dataSnapShot.getValue(ImageUploadInfo.class);
                      for (DataSnapshot d: dataSnapShot.getChildren()){
                          Log.d("a","thtfsffsg"+d.getValue());

                      }

                      Book b = new Book(book.imageName,book.getImagePrice(),book.getImageURL(),book.getuID());
                      if(!inList(b) && !adapter.albumList.contains(b))
                        albumList.add(b);

                }

                adapter.albumList = albumList;
                adapter.notifyDataSetChanged();
                for(Book b: adapter.albumList){
                    Log.d("a","Book names"+b.getName());
                }
                Log.d("a","Book list end");

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        /*
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
        };

        Book a = new Book("True Romance", 13, covers[0]);
        albumList.add(a);

        a = new Book("Xscpae", 8, covers[1]);
        albumList.add(a);

        a = new Book("Maroon 5", 11, covers[2]);
        albumList.add(a);

        a = new Book("Born to Die", 12, covers[3]);
        albumList.add(a);

        a = new Book("Honeymoon", 14, covers[4]);
        albumList.add(a);

        a = new Book("I Need a Doctor", 1, covers[5]);
        albumList.add(a);

        a = new Book("Loud", 11, covers[6]);
        albumList.add(a);

        a = new Book("Legend", 14, covers[7]);
        albumList.add(a);

        a = new Book("Hello", 11, covers[8]);
        albumList.add(a);
*/

    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}