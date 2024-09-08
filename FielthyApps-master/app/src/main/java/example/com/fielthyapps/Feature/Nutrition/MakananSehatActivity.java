package example.com.fielthyapps.Feature.Nutrition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class MakananSehatActivity extends AppCompatActivity {
private Button btn_diet,btn_nutrition;
private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makanan_sehat);
        btn_diet = findViewById(R.id.btn_diet);
        btn_nutrition = findViewById(R.id.btn_kembali_nutrition);


        Intent iin = getIntent();
        final Bundle b = iin.getExtras();

        if (b != null) {
            id = (String) b.get("id");
        }

        btn_nutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MakananSehatActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });

        btn_diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MakananSehatActivity.this, DietSehatActivity.class));
            }
        });
    }
}