package example.com.fielthyapps.Feature.Physical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import example.com.fielthyapps.Feature.Smoker.SmokerActivity;
import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class PhysicalActivity extends AppCompatActivity {
private LinearLayout LL_mwt,LL_balke;
private ImageView iV_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);
        LL_balke = findViewById(R.id.LL_balke);
        LL_mwt = findViewById(R.id.LL_6mwt);
        iV_back = findViewById(R.id.iV_kembali);

        iV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhysicalActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        LL_balke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PhysicalActivity.this, BalkeActivity.class));
            }
        });

        LL_mwt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PhysicalActivity.this, PhysicalTestActivity.class));
            }
        });
    }
}