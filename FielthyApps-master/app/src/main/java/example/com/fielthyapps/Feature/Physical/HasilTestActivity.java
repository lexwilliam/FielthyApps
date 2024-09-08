package example.com.fielthyapps.Feature.Physical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import example.com.fielthyapps.Feature.History.HistoryActivity;
import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class HasilTestActivity extends AppCompatActivity {
    private TextView tV_avg_speed, tV_vomax, tV_mets, tV_intensitas, tV_walking_sped, tV_jarak_recommend;
    private String get_umur, get_berat, get_tinggi, get_jarak, get_waktu, get_type;
    private int get_value_gender;
    private TextView tV_kategori, tV_sub;
    private String get_gender;
    private Button selesai;
    private TextView textViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_test);
        tV_kategori = findViewById(R.id.tV_kategori);
        tV_sub = findViewById(R.id.sub_kategori);
        tV_avg_speed = findViewById(R.id.tV_avg_speeed);
        tV_vomax = findViewById(R.id.tV_Vomax);
        tV_mets = findViewById(R.id.tV_mets);
        tV_intensitas = findViewById(R.id.tV_intensitas);
        tV_walking_sped = findViewById(R.id.tV_walking_speed_recommendation);
        tV_jarak_recommend = findViewById(R.id.tV_jarak_recommendasi);
        selesai = findViewById(R.id.btn_selesai);
//
//        textViewResult = findViewById(R.id.textViewResult);
//
//        String result = getIntent().getStringExtra("result");
//        if (result != null) {
//            textViewResult.setText(result.trim());
//        } else {
//            textViewResult.setText("No result available");
//        }

        Intent iin = getIntent();
        final Bundle b = iin.getExtras();

        if (b != null) {
            double vomax_value = 0;
            double mets_value = 0;

            get_type = getStringOrDefault(b, "type");
            get_umur = getStringOrDefault(b, "age");
            get_gender = getStringOrDefault(b, "gender");
            get_berat = getStringOrDefault(b, "beratbadan");
            get_tinggi = getStringOrDefault(b, "tinggibadan");
            get_jarak = getStringOrDefault(b, "jaraktempuh");
            get_waktu = getStringOrDefault(b, "waktu");

            float jarak = parseFloatOrDefault(get_jarak, 0);
            int umur = parseIntOrDefault(get_umur, 0);
            int berat = parseIntOrDefault(get_berat, 0);
            int tinggi = parseIntOrDefault(get_tinggi, 0);

            if (get_type.equals("0") || get_type.equals("3")) {
                tV_kategori.setText("6MWT");
                tV_sub.setText("6MWT\nTest");
                float avg_speed = jarak * 10 / 1000;

                if (get_gender.equals("laki - Laki")) {
                    get_value_gender = 0;
                    vomax_value = (0.053 * jarak) + (0.022 * umur) + (0.032 * tinggi) - (0.164 * berat) - 2.287;
                } else if (get_gender.equals("Perempuan")) {
                    get_value_gender = 1;
                    vomax_value = (0.053 * jarak) + (0.022 * umur) + (0.032 * tinggi) - (0.164 * berat) - 2.228 - 2.287;
                }

                mets_value = vomax_value / 3.5;
                double intensitas_value = 0.6 * mets_value;
                double walking_speed = 0.8 * avg_speed;
                double rec_jarak = jarak / 6 * 6 * 0.8;

                if (intensitas_value < 3) {
                    tV_intensitas.setText("Ringan");
                } else if (intensitas_value >= 3 || intensitas_value <= 6) {
                    tV_intensitas.setText("Sedang");
                } else if (intensitas_value > 6) {
                    tV_intensitas.setText("Berat");
                }

                tV_avg_speed.setText(String.format("%.2f km/jam", avg_speed));
                tV_jarak_recommend.setText(String.format("%.2f meter", rec_jarak));
                tV_mets.setText(String.format("%.2f", mets_value));
                tV_vomax.setText(String.format("%.2f ml/kg/menit", vomax_value));
                tV_walking_sped.setText(String.format("%.2f km/jam", walking_speed));
            } else

            if (get_type.equals("1") || get_type.equals("2")) {
                tV_kategori.setText("Balke");
                tV_sub.setText("Balke\nTest");
                float avg_speed = jarak * 4 / 1000;

                if (!get_berat.isEmpty() && !get_tinggi.isEmpty() && !get_umur.isEmpty()) {
                    vomax_value = 6.5 + 12.5 * (jarak / 1000);
                    mets_value = vomax_value / 3.5;
                } else {
                    vomax_value = 0;
                    mets_value = 0;
                }


                double intensitas_value = 0.6 * mets_value;
                double walking_speed = 0.8 * avg_speed;
                double rec_jarak = jarak / 15 * 15 * 0.8;

                if (intensitas_value < 3) {
                    tV_intensitas.setText("Ringan");
                } else if (intensitas_value >= 3 || intensitas_value <= 6) {
                    tV_intensitas.setText("Sedang");
                } else if (intensitas_value > 6) {
                    tV_intensitas.setText("Berat");
                }

                tV_avg_speed.setText(String.format("%.2f km/jam", avg_speed));
                tV_jarak_recommend.setText(String.format("%.2f meter", rec_jarak));
                tV_mets.setText(String.format("%.2f", mets_value));
                tV_vomax.setText(String.format("%.2f ml/kg/menit", vomax_value));
                tV_walking_sped.setText(String.format("%.2f km/jam", walking_speed));
            }

        }

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (get_type.equals("0") || get_type.equals("1")) {
                    Intent intent = new Intent(HasilTestActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (get_type.equals("2") || get_type.equals("3")) {
                    Intent intent = new Intent(HasilTestActivity.this, HistoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }
    private String getStringOrDefault(Bundle bundle, String key) {
        String value = bundle.getString(key);
        return value != null ? value.trim() : "";
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private float parseFloatOrDefault(String value, float defaultValue) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
