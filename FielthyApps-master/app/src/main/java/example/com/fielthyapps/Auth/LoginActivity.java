package example.com.fielthyapps.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class LoginActivity extends AppCompatActivity {
    private EditText email_login,pass_login;
    private TextView forget,daftar;
    private Button btn_login;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoading;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email_login = findViewById(R.id.eT_email_login);
        pass_login = findViewById(R.id.eT_password_login);
        forget = findViewById(R.id.tV_forgetPass);
        daftar = findViewById(R.id.tV_daftar);
        btn_login = findViewById(R.id.btn_login);
        mLoading = new ProgressDialog(this);
        mLoading.setMessage("Please Wait..");
        mAuth = FirebaseAuth.getInstance();

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrasiActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialLogin();
            }
        });

    }

    private void initialLogin() {
        String email_user = email_login.getText().toString();
        final String password_user = pass_login.getText().toString();
        if (email_user.isEmpty()) {
            email_login.setError("Masukan Email");
            email_login.setFocusable(true);
        } else if (password_user.isEmpty()) {
            pass_login.setError("Masukan Password");
            pass_login.setFocusable(true);
        } else if (!email_user.matches(emailPattern)) {
            email_login.setError("Masukan Email yang Valid");
            email_login.setFocusable(true);
        } else {
            login(email_user, password_user);
        }
    }

    private void login(String email_user, String password_user) {
        mLoading.show();

        mAuth.signInWithEmailAndPassword(email_user, password_user).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                final FirebaseUser user = mAuth.getCurrentUser();
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("nama", user.getDisplayName());
                    Log.d("cobaNama", "onComplete: " +user.getDisplayName());
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mLoading.dismiss();
                Toast.makeText(LoginActivity.this, "Gagal Login", Toast.LENGTH_SHORT).show();
            }
        });
    }
}