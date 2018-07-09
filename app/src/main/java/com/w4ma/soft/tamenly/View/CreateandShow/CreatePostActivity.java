package com.w4ma.soft.tamenly.View.CreateandShow;


import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.w4ma.soft.tamenly.CategoryActivities.Models.BooksModel.BookList;
import com.w4ma.soft.tamenly.MainActivity;
import com.w4ma.soft.tamenly.R;
import com.w4ma.soft.tamenly.Utils.TinyDB;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class CreatePostActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 999;
    private static final String TAG = "CreatePostActivity";

    public @BindView(R.id.postTitle) EditText Title;
    public @BindView(R.id.postDescription) EditText Description;
    public @BindView(R.id.postPrice) EditText Price;
    public @BindView(R.id.spinner) MaterialSpinner spinner;
    public @BindView(R.id.PostImg1) ImageView imgPost1;
    public @BindView(R.id.BanneradView) AdView BanneradView;
    @BindView(R.id.btnDone)
    ImageButton btnDone;

    private InterstitialAd mInterstitialAd;

    Intent intent;

    private Bitmap bitmap;

    public FirebaseAuth mAuth;
    DatabaseReference ref;
    FirebaseStorage firebaseStorage;
    Uri uri1;
    TinyDB tinyDB;

  Dialog dialog;
  String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        loadAD();
        tinyDB = new TinyDB(this);
        dialog = new Dialog(this);
        intent = getIntent();



        if (ContextCompat.checkSelfPermission(CreatePostActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(CreatePostActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},GALLERY_REQUEST);
        }else  if (ContextCompat.checkSelfPermission(CreatePostActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            Toasty.warning(CreatePostActivity.this,getString(R.string.permission_ask)).show();
        }

        spinner.setItems("USD","EUR","EGP","AED","SAR","YER","INR","ILS","SYP","IQD");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);  // THIS METHOD SHOULD BE HERE so that ImagePicker works with fragment
            try {

                // When an Image is picked
                if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && null != data) {

                     uri1 = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(uri1, filePathColumn, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imagePath = cursor.getString(columnIndex);
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri1);
                        imgPost1.setImageBitmap(bitmap);

                        cursor.close();
                    }

                } else {
                    Toast.makeText(this, R.string.image_picks, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Crashlytics.logException(e);
                Toast.makeText(this, R.string.aasomethingwent_wrong, Toast.LENGTH_LONG).show();
            }
        }
    //THIS METHOD  excutes when the user click on share Button



    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void SharePost() {


        try {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.show();
            final String country = tinyDB.getString("country");

            ref = FirebaseDatabase.getInstance().getReference(country);


            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            final String user = currentUser.getDisplayName();
            final String userProfileP = currentUser.getPhotoUrl().toString();
            final String userID = mAuth.getUid();


            if (TextUtils.isEmpty(Title.getText().toString()) || TextUtils.isEmpty(String.valueOf(Price.getText().toString())) ||
                    TextUtils.isEmpty(String.valueOf(Description.getText().toString())) ||
                    TextUtils.isEmpty(spinner.getText().toString()) ||
                    uri1 == null) {

                Toasty.info(CreatePostActivity.this, getString(R.string.fields_empty_message), Toast.LENGTH_SHORT, true).show();


            } else {

                dialog.setContentView(R.layout.dialog_message_done);
                dialog.show();
                dialog.setCancelable(false);
                final LottieAnimationView animationView = dialog.findViewById(R.id.successAnimation);

                animationView.setAnimation(R.raw.send_mail);
                animationView.setMinFrame(91);
                animationView.setMaxFrame(114);
                animationView.playAnimation();

                firebaseStorage = FirebaseStorage.getInstance();
                final StorageReference fileReference = firebaseStorage.getReference("ProjectUploads").child(System.currentTimeMillis()
                        + "." + getFileExtension(uri1));

                imgPost1.setDrawingCacheEnabled(true);
                imgPost1.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                imgPost1.buildDrawingCache();
                imgPost1.setDrawingCacheEnabled(true);
                imgPost1.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(imgPost1.getDrawingCache());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] data = baos.toByteArray();

                final UploadTask uploadTask = fileReference.putBytes(data);

                if (TextUtils.isEmpty(data.toString())) {

                    Toasty.warning(CreatePostActivity.this,getString(R.string.image_chose_message) ,Toast.LENGTH_SHORT).show();
                }else {

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        if (uploadTask.isSuccessful()) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    final String downloadUri = uri.toString();

                                    Time today = new Time(Time.getCurrentTimezone());
                                    today.setToNow();

                                    String postTitle = Title.getText().toString();
                                    String postDescription = Description.getText().toString();
                                    String postPrice = Price.getText().toString();
                                    String category = intent.getStringExtra("name");
                                    String currency = spinner.getText().toString();


                                    final BookList modelList = new BookList();

                                    String key = ref.child(category).push().getKey(); // Post ID

                                    modelList.setPost_title(postTitle);
                                    modelList.setPost_description(postDescription);
                                    modelList.setPost_image(downloadUri);
                                    modelList.setPost_real_price(postPrice);
                                    modelList.setCategory(category);
                                    modelList.setCurrency(currency);
                                    modelList.setPost_datetime(getTimeStamp());
                                    modelList.setUser_name(user);
                                    modelList.setUser_pp(userProfileP);
                                    modelList.setPost_id(key);
                                    modelList.setUser_ID(userID);

                                    ref.child("Posts").child(category).child(key).setValue(modelList);


                                    animationView.pauseAnimation();
                                    dialog.dismiss();



                                            startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
                                            Toasty.success(CreatePostActivity.this, getString(R.string.done), Toast.LENGTH_SHORT).show();

                                            animationView.pauseAnimation();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads


                        Toasty.error(CreatePostActivity.this, "Oops, Post Failed => Error is : " + exception.getMessage()
                                , Toast.LENGTH_SHORT).show();
                    }
                });
                }
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toasty.error(CreatePostActivity.this,"Error: Exception Code " +  e.getMessage(),Toast.LENGTH_LONG).show();
            Log.d(TAG, "SharePost: " + e.getMessage());
        }
    }


    public String getTimeStamp(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ", Locale.ROOT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Africa/Cairo"));
        return simpleDateFormat.format(new Date());

    }

    public void loadAD(){

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("33BE2250B43518CCDA7DE426D04EE231")
                .build();
        BanneradView.loadAd(adRequest);
        BanneradView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Toasty.info(CreatePostActivity.this,getString(R.string.adblock_disble_message) + errorCode ,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });


    }




    @OnClick({R.id.btnDone,R.id.PostImg1})
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.PostImg1:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

                break;

            case R.id.btnDone:

                SharePost();


                break;

        }
    }
}


