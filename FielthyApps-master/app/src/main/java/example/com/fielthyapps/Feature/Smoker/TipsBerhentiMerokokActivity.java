package example.com.fielthyapps.Feature.Smoker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import example.com.fielthyapps.Feature.Stress.QuestList;
import example.com.fielthyapps.Feature.Stress.QuestSectionsatuAdapter;
import example.com.fielthyapps.R;

public class TipsBerhentiMerokokActivity extends AppCompatActivity {
    private RecyclerView rV_tips;
    private ImageView iV_back;
    private String getBatang,getRupiah,getBungkus,getTahun,id,uid,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_berhenti_merokok);
        rV_tips = findViewById(R.id.rV_tips);
        iV_back = findViewById(R.id.iV_kembali);

        Intent iin = getIntent();
        final Bundle b = iin.getExtras();

        if (b != null) {
            status = (String) b.get("status");
            id = (String) b.get("id");
            uid = (String) b.get("uid");
            getBatang = (String) b.get("batang");
            getTahun = (String) b.get("tahun");
            getBungkus = (String) b.get("bungkus");
            getRupiah = (String) b.get("rupiah");
        }

        iV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TipsBerhentiMerokokActivity.this, HasilSmokerActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("uid", uid);
                intent.putExtra("batang", getBatang);
                intent.putExtra("tahun", getTahun);
                intent.putExtra("bungkus", getBungkus);
                intent.putExtra("rupiah", getRupiah);
                intent.putExtra("status", "testsmoker");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        SmokerTipsList[] myListData = new SmokerTipsList[] {
                new SmokerTipsList("Motivasi bulatkan tekad dan\n" +
                        " tujuan anda berhenti merokok",R.drawable.nmbr_one,R.drawable.consultation),
                new SmokerTipsList("berhenti merokok seketika (total) \n" +
                        "atau melakukan pengurangan \n" +
                        "jumlah rokok yang dihisap perhari \n" +
                        "secara bertahap",R.drawable.nmbr_two,R.drawable.familly),
                new SmokerTipsList("kenali waktu dan situasi dimana \n" +
                        "anda paling sering merokok",R.drawable.nmbr_three,R.drawable.motivation),
                new SmokerTipsList("tahan keinginan anda\n" +
                        " dengan menunda",R.drawable.nmbr_four,R.drawable.no_smoking),
                new SmokerTipsList("Berolahraga secara teratur",R.drawable.nmbr_five,R.drawable.sport),
                new SmokerTipsList("mintalah dukungan dari \n" +
                        "keluarga dan kerabat",R.drawable.nmbr_six,R.drawable.time),
                new SmokerTipsList("konsultasikan dengan dokter",R.drawable.nmbr_seven,R.drawable.consultation),

        };
        InformasiMenjauhiRokokAdapter adapter = new InformasiMenjauhiRokokAdapter(myListData);
        rV_tips.setHasFixedSize(true);
        rV_tips.setLayoutManager(new LinearLayoutManager(this));
        rV_tips.setAdapter(adapter);
    }
}