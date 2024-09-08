package example.com.fielthyapps.Feature.Smoker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import example.com.fielthyapps.Feature.Nutrition.NutritionActivity;
import example.com.fielthyapps.Feature.Nutrition.SayurSayuranActivity;
import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class HasilTidakMerokokActivity extends AppCompatActivity {
    private TextView quoteTextView;
    private Button btn_selesai;
    private ImageView iV_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_tidak_merokok);
        quoteTextView = findViewById(R.id.tV_quotes);
        btn_selesai = findViewById(R.id.btn_selesai);
        iV_back = findViewById(R.id.iV_kembali);
        displayRandomQuote();

        btn_selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HasilTidakMerokokActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        iV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HasilTidakMerokokActivity.this, SmokerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void displayRandomQuote() {
        Random random = new Random();
        int index = random.nextInt(Quotes.quotes.length);
        String randomQuote = Quotes.quotes[index];
        quoteTextView.setText(randomQuote);
    }
}