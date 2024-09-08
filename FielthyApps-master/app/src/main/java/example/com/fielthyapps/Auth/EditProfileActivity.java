package example.com.fielthyapps.Auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import example.com.fielthyapps.R;

public class EditProfileActivity extends AppCompatActivity {
    private EditText name, email, location, date;
    private String namaUser, tempatLahirUser, tanggalLahirUser, emailUser, sdate;
    private ImageView fotoProfile, add_img;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private FirebaseFirestore fStore;
    private RadioButton male, female;
    DocumentReference documentReference;
    static int PReqcode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImage;
    private ProgressDialog mLoading;
    private Button btn_submit_edit_profile;
    int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 1;
    private String[] arrMonth = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        mLoading = new ProgressDialog(this);
        mLoading.setMessage("Please Wait..");
        fStore = FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();
        fotoProfile = findViewById(R.id.profile_image);
        add_img = findViewById(R.id.iV_addImg);
        name = findViewById(R.id.eT_name_profile);
//        email = findViewById(R.id.eT_email_profile);
        location = findViewById(R.id.eT_location_profile);
        date = findViewById(R.id.eT_date_profile);
        male = findViewById(R.id.rB_laki);
        female = findViewById(R.id.rB_wanita);
        btn_submit_edit_profile = findViewById(R.id.btn_edit_profile_submit);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        checkUser();

        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestForPermission();
            }
        });

        date.setShowSoftInputOnFocus(false);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        btn_submit_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
    }

    private void checkUser() {
        user = firebaseAuth.getCurrentUser();
        if (user != null) {

            DocumentReference documentReference = fStore.collection("user").document(user.getUid());
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    Log.d("Cek masuk gak", value.getString("nama"));
                    String namedb = "" + value.getString("nama");
//                    String emaildb = "" + value.getString("email");
                    String locationdb = "" + value.getString("location");
                    String datedb = "" + value.getString("birthday");
                    name.setText(namedb);
//                    email.setText(emaildb);
                    location.setText(locationdb);
                    date.setText(datedb);

                    if (user.getPhotoUrl() == null){
                        Glide.with(EditProfileActivity.this)
                                .load(R.drawable.default_profile)
                                .into(fotoProfile);
                    }else{
                        Glide.with(EditProfileActivity.this)
                                .load(user.getPhotoUrl())
                                .into(fotoProfile);
                    }
                }
            });
        }
    }

    private void updateUser() {
        namaUser = name.getText().toString();
//        emailUser = email.getText().toString();
        tanggalLahirUser = date.getText().toString();
        tempatLahirUser = location.getText().toString();

        if (pickedImage == null){
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .build();

            user.updateProfile(profileChangeRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mLoading.show();
                                String uid = user.getUid();

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("email", user.getEmail());
                                hashMap.put("uid", uid);
                                hashMap.put("nama", namaUser);
                                hashMap.put("birthday", tanggalLahirUser);
                                hashMap.put("location", tempatLahirUser);

                                // Format tanggal
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);

                                try {
                                    // Buat instance Calendar untuk sekarang
                                    Calendar now = Calendar.getInstance();

                                    // Parse string ke objek Date
                                    Date date = dateFormat.parse(tanggalLahirUser);

                                    // Buat instance Calendar untuk tanggal lahir
                                    Calendar tanggallahir = Calendar.getInstance();
                                    tanggallahir.setTime(date);

                                    // Hitung umur
                                    int years = now.get(Calendar.YEAR) - tanggallahir.get(Calendar.YEAR);
                                    int months = now.get(Calendar.MONTH) - tanggallahir.get(Calendar.MONTH);
                                    int days = now.get(Calendar.DAY_OF_MONTH) - tanggallahir.get(Calendar.DAY_OF_MONTH);

                                    // Sesuaikan bulan dan hari jika diperlukan
                                    if (days < 0) {
                                        months--;
                                        days += now.getActualMaximum(Calendar.DAY_OF_MONTH);
                                    }
                                    if (months < 0) {
                                        years--;
                                        months += 12;
                                    }

                                    int umur = years;
                                    System.out.println("Umur: " + umur);

                                    // Masukkan umur ke dalam HashMap
                                    hashMap.put("umur", umur);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (male.isChecked()) {
                                    hashMap.put("gender", "laki - Laki");
                                } else if (female.isChecked()) {
                                    hashMap.put("gender", "Perempuan");
                                }

                                DocumentReference documentReference = fStore.collection("user").document(uid);
                                documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(EditProfileActivity.this, "Profile Berhasil Di ubah", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditProfileActivity.this, "Tidak Berhasil update profile", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
        } else {
            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photo");
            final StorageReference imageFilePath = mStorage.child(pickedImage.getLastPathSegment());
            imageFilePath.putFile(pickedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(uri)
                                    .build();

                            user.updateProfile(profileChangeRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mLoading.show();
                                                String uid = user.getUid();

                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("email", user.getEmail());
                                                hashMap.put("uid", uid);
                                                hashMap.put("nama", namaUser);
                                                hashMap.put("birthday", tanggalLahirUser);
                                                hashMap.put("location", tempatLahirUser);

                                                // Format tanggal
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);

                                                try {
                                                    // Buat instance Calendar untuk sekarang
                                                    Calendar now = Calendar.getInstance();

                                                    // Parse string ke objek Date
                                                    Date date = dateFormat.parse(tanggalLahirUser);

                                                    // Buat instance Calendar untuk tanggal lahir
                                                    Calendar tanggallahir = Calendar.getInstance();
                                                    tanggallahir.setTime(date);

                                                    // Hitung umur
                                                    int years = now.get(Calendar.YEAR) - tanggallahir.get(Calendar.YEAR);
                                                    int months = now.get(Calendar.MONTH) - tanggallahir.get(Calendar.MONTH);
                                                    int days = now.get(Calendar.DAY_OF_MONTH) - tanggallahir.get(Calendar.DAY_OF_MONTH);

                                                    // Sesuaikan bulan dan hari jika diperlukan
                                                    if (days < 0) {
                                                        months--;
                                                        days += now.getActualMaximum(Calendar.DAY_OF_MONTH);
                                                    }
                                                    if (months < 0) {
                                                        years--;
                                                        months += 12;
                                                    }

                                                    int umur = years;
                                                    System.out.println("Umur: " + umur);

                                                    // Masukkan umur ke dalam HashMap
                                                    hashMap.put("umur", umur);

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                if (male.isChecked()) {
                                                    hashMap.put("gender", "laki - Laki");
                                                } else if (female.isChecked()) {
                                                    hashMap.put("gender", "Perempuan");
                                                }

                                                DocumentReference documentReference = fStore.collection("user").document(uid);
                                                documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(EditProfileActivity.this, "Profile Berhasil Di ubah", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(EditProfileActivity.this, "Tidak Berhasil update profile", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    });
                }
            });
        }
    }

    private void checkAndRequestForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 and above
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.CAMERA
                }, PReqcode);
            } else {
                openGallery();
            }
        } else {
            // Below Android 13
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                }, PReqcode);
            } else {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PReqcode && grantResults.length > 0) {
            boolean readExternalAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if (readExternalAccepted && cameraAccepted) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
            pickedImage = data.getData();
            fotoProfile.setImageURI(pickedImage);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            StringBuilder dateString = new StringBuilder()
                    .append(mDay).append(" ")
                    .append(arrMonth[mMonth]).append(", ")
                    .append(mYear).append(" ");
            date.setText(dateString);
        }
    };
}
