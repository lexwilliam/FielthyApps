package example.com.fielthyapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import example.com.fielthyapps.Auth.LoginActivity;

public class MainActivity extends AppCompatActivity {
private ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo = findViewById(R.id.logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    Intent home = new Intent(MainActivity.this, HomeActivity.class);
                    home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home);
                    finish();

                }else{
                    Intent home = new Intent(MainActivity.this, LoginActivity.class);
                    home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home);
                    finish();
                }
            }
        },3000);
    }
}