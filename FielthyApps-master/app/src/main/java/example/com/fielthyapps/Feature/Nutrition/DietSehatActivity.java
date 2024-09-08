package example.com.fielthyapps.Feature.Nutrition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import example.com.fielthyapps.Feature.Smoker.InformasiMenjauhiRokokAdapter;
import example.com.fielthyapps.Feature.Smoker.SmokerTipsList;
import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class DietSehatActivity extends AppCompatActivity {
    private RecyclerView rV_tips;
    private ImageView iV_back;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_sehat);
        rV_tips = findViewById(R.id.rV_tips_diet);
        iV_back = findViewById(R.id.iV_kembali);


        Intent iin = getIntent();
        final Bundle b = iin.getExtras();

        if (b != null) {
            id = (String) b.get("id");
        }

        iV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DietSehatActivity.this, NutritionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        });


        SmokerTipsList[] myListData = new SmokerTipsList[] {
                new SmokerTipsList("Hindari Makanan yang Tinggi Lemak",R.drawable.nmbr_one,R.drawable.diet_satu),
                new SmokerTipsList("Hindari Makanan yang Mengandung Bahan Pengawet",R.drawable.nmbr_two,R.drawable.diet_dua),
                new SmokerTipsList("Pilih Makanan yang Diolah dengan Tepat",R.drawable.nmbr_three,R.drawable.diet_tiga),
                new SmokerTipsList("Hindari Penyedap Secara Berlebihan",R.drawable.nmbr_four,R.drawable.diet_empat),
                new SmokerTipsList("Perbanyak Makan Buah dan Sayur",R.drawable.nmbr_five,R.drawable.diet_lima),
                new SmokerTipsList("Perbanyak Minum Air Putih",R.drawable.nmbr_six,R.drawable.diet_enam),
                new SmokerTipsList("Hindari Makan dan Minum yang Berwarna Modifikasi",R.drawable.nmbr_seven,R.drawable.diet_tujuh),
                new SmokerTipsList("Atur Porsi Makan",R.drawable.nmbr_eight,R.drawable.diet_delapan),
                new SmokerTipsList("Makan Tepat Waktu",R.drawable.nmbrnine,R.drawable.diet_sembilan),
                new SmokerTipsList("Minum Suplemen Jika Perlu",R.drawable.nmbrten,R.drawable.diet_sepuluh),

        };
        InformasiMenjauhiRokokAdapter adapter = new InformasiMenjauhiRokokAdapter(myListData);
        rV_tips.setHasFixedSize(true);
        rV_tips.setLayoutManager(new LinearLayoutManager(this));
        rV_tips.setAdapter(adapter);
    }
}