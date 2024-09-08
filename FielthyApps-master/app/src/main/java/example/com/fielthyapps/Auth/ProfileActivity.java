package example.com.fielthyapps.Auth;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.fielthyapps.Feature.History.HistoryActivity;
import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fstore;
    private EditText eT_name, eT_email, eT_location, eT_date, eT_gender, eT_age;
    private Button btn_logout, btn_edit_profile,btn_edit_auth;
    private CircleImageView image_profile;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://fielthyapps.appspot.com");    //change the url according to your firebase app
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigate);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
        image_profile = findViewById(R.id.profile_image);
        eT_name = findViewById(R.id.eT_name_profile);
        eT_email = findViewById(R.id.eT_email_profile);
        eT_location = findViewById(R.id.eT_location_profile);
        eT_date = findViewById(R.id.eT_date_profile);
        eT_gender = findViewById(R.id.eT_gender_profile);
        eT_age = findViewById(R.id.eT_age_profile);
        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        btn_logout = findViewById(R.id.btn_logout);
        btn_edit_auth = findViewById(R.id.btn_edit_auth);
        eT_name.setClickable(true);


        checkUser();
        disableEditText(eT_name);
        disableEditText(eT_email);
        disableEditText(eT_location);
        disableEditText(eT_date);
        disableEditText(eT_gender);
        disableEditText(eT_age);
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));
            }
        });

        btn_edit_auth.setVisibility(View.GONE);

        btn_edit_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditAuthActivity.class);
//                intent.putExtra("email",eT_email.getText().toString());
                startActivity(intent);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bottom_home) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                } else if (item.getItemId() == R.id.bottom_history) {
                    startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.bottom_profile) {
                    return true;
                }
                return false;
            }
        });
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
    }

    private void checkUser() {
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            DocumentReference documentReference = fstore.collection("user").document(user.getUid());
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    Log.d("Cek masuk gak", value.getString("nama"));

                    eT_name.setText(value.getString("nama"));
                    eT_email.setText(value.getString("email"));
                    eT_location.setText(value.getString("location"));
                    eT_gender.setText(value.getString("gender"));
                    eT_date.setText(value.getString("birthday"));
                    eT_age.setText(String.valueOf(value.get("umur")) + " Tahun");

                    if (user.getPhotoUrl() == null){
                        Glide.with(ProfileActivity.this)
                                .load(R.drawable.default_profile)
                                .into(image_profile);
                    }else{
                        Glide.with(ProfileActivity.this)
                                .load(user.getPhotoUrl())
                                .into(image_profile);

                    }




                }
            });
        }
    }
}