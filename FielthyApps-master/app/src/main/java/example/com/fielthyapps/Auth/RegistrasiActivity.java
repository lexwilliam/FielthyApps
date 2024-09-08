package example.com.fielthyapps.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import example.com.fielthyapps.R;

public class RegistrasiActivity extends AppCompatActivity {
    private EditText nama, email,location,date,password;
    private Button registrasi;
    private TextView login;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fStore;
    private ProgressDialog mLoading;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String emailUser,namaUser,locationUser,gender,passwordUser,sdate;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePickerDialog;
    private RadioGroup radioGroup;
    private RadioButton laki, perempuan;
    int select;
    int mYear, mMonth, mDay;
    static final int TIME_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID = 1;
    private String[] arrMonth = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mLoading = new ProgressDialog(this);
        mLoading.setMessage("Please Wait..");
        nama = findViewById(R.id.eT_name);
        email = findViewById(R.id.eT_email);
        location = findViewById(R.id.eT_location);
        date = findViewById(R.id.eT_date);
        radioGroup = findViewById(R.id.rG_gender);
        laki = findViewById(R.id.rB_laki);
        perempuan = findViewById(R.id.rB_wanita);
        password = findViewById(R.id.eT_password_registrasi);
        registrasi = findViewById(R.id.btn_registrasi);
        login = findViewById(R.id.tV_masuk);
        select = radioGroup.getCheckedRadioButtonId();
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrasiActivity.this,LoginActivity.class));
                finish();
            }
        });

        registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialRegist();
            }
        });


    date.setShowSoftInputOnFocus(false);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
            }
        });

    }


    private void initialRegist(){
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // format type 4
                showDialog(DATE_DIALOG_ID);
            }
        });


        emailUser = email.getText().toString();
        namaUser = nama.getText().toString();
        locationUser = location.getText().toString();
        passwordUser = password.getText().toString();



        if (emailUser.isEmpty()){
            email.setError("Masukan Email Terlebih Dahulu");
            email.setFocusable(true);
        }else if (!emailUser.matches(emailPattern)){
            email.setError("Masukan Format Email yang Benar");
            email.setFocusable(true);
        }else if (locationUser.isEmpty()) {
            location.setError("Masukan Lokasi Terlebih Dahulu");
            location.setFocusable(true);
        }else if (namaUser.isEmpty()) {
            nama.setError("Masukan Nama Terlebih Dahulu");
            nama.setFocusable(true);
        }else if (passwordUser.isEmpty()) {
            password.setError("Masukan Password Anda");
            password.setFocusable(true);
        } else if (passwordUser.length() < 8) {
            password.setError("Masukan Password minimal 8");
            password.setFocusable(true);
        }else{
            registrasiUser(emailUser, passwordUser);
        }

    }

    public void registrasiUser(String emailUser, String passwordUser) {

        firebaseAuth.createUserWithEmailAndPassword(emailUser,passwordUser)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        final FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (task.isSuccessful()){
                            String uid = user.getUid();
                            DocumentReference documentReference = fStore.collection("user").document(uid);


                            HashMap hashMap = new HashMap();
                            String emailmap = user.getEmail();
                            if (laki.isChecked()) {
                                gender = "Laki - Laki";
                            } else if (perempuan.isChecked()) {
                                gender = "Perempuan";
                            }

                            Calendar now = Calendar.getInstance();
                            Calendar tanggallahir = Calendar.getInstance();

                            tanggallahir.set(mYear, mMonth, mDay);

                            int years = now.get(Calendar.YEAR) - tanggallahir.get(Calendar.YEAR);
                            int months = now.get(Calendar.MONTH) - tanggallahir.get(Calendar.MONTH);
                            int days = now.get(Calendar.DAY_OF_MONTH) - tanggallahir.get(Calendar.DAY_OF_MONTH);
                            if (days < 0) {
                                months--;
                                days += now.getActualMaximum(Calendar.DAY_OF_MONTH);
                            }
                            if (months < 0) {
                                years--;
                                months += 12;
                            }

                            int umur = years;

                            hashMap.put("email", emailmap);
                            hashMap.put("uid", uid);
                            hashMap.put("nama", namaUser);
                            hashMap.put("location", locationUser);
                            hashMap.put("birthday",sdate);
                            hashMap.put("gender", gender);
                            hashMap.put("umur", umur);


                            documentReference.set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mLoading.dismiss();
                                    Toast.makeText(RegistrasiActivity.this, "Berhasil Daftar Dengan Email\n" + user.getEmail(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegistrasiActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrasiActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case DATE_DIALOG_ID:
                return new DatePickerDialog(
                        this, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
           sdate = LPad(mDay + "", "0", 2) + " " + arrMonth[mMonth] + ", " + mYear;
            date.setText(sdate);
        }

    };

    private static String LPad(String schar, String spad, int len) {
        String sret = schar;
        for (int i = sret.length(); i < len; i++) {
            sret = spad + sret;
        }
        return sret;
    }

}