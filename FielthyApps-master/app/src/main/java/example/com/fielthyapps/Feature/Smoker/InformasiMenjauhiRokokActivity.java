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

public class InformasiMenjauhiRokokActivity extends AppCompatActivity {
    private RecyclerView rV_tips;
    private ImageView iV_back;
    private String getBatang,getRupiah,getBungkus,getTahun,id,uid,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_menjauhi_rokok);
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
                Intent intent = new Intent(InformasiMenjauhiRokokActivity.this, HasilSmokerActivity.class);
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
                new SmokerTipsList("Setelah 20 menit ( Cek denyut nadimu, \n" +
                        "dan rasakan bahwa denyut \n" +
                        "nadimu akan mulaiikembali normal.)",R.drawable.nmbr_one,R.drawable.page_one),
                new SmokerTipsList("Setelah 8 jam (Tingkat oksigenmu \n" +
                        "akan pulih dan tingkat karbon \n" +
                        "monoksida yang berbahaya dalam \n" +
                        "darahmu akan berkurang setengahnya.)",R.drawable.nmbr_two,R.drawable.page_two),
                new SmokerTipsList("Setelah 48 Jam (Semua karbon monoksida telah bersih.Paru-parumu akan membersihkan lendir serta \n" +
                        "indera perasa dan \n" +
                        "penciumanmu akan membaik.)",R.drawable.nmbr_three,R.drawable.page_three),
                new SmokerTipsList("Setelah 72 jam ( Jika kamu \n" +
                        "merasakan bahwa bernapas terasa \n" +
                        "lebih mudah, itu karena saluran\u2028bronkial-mu sudah mulai mengendur dan energimu juga akan meningkat.)",R.drawable.nmbr_four,R.drawable.page_four),
                new SmokerTipsList("Setelah 2 - 12 Minggu ( Darah yang dipompa ke jantung dan otot anda akan jauh lebih baik, karena sirkulasi darahmu akan meningkat.)",R.drawable.nmbr_five,R.drawable.page_five),
                new SmokerTipsList("Setelah 3 - 9 bulan ( Batuk, mengi atau masalah pernapasan apa pun akan membaik, bersamaan\u2028dengan fungsi paru -parumu yang meningkat hingga 10%.)",R.drawable.nmbr_six,R.drawable.page_six),
                new SmokerTipsList("Setelah 1 tahun (Kabar baik! Risiko serangan jantung anda akan berkurang setengahnya dibandingkan dengan perokok.)",R.drawable.nmbr_seven,R.drawable.page_seven),
                new SmokerTipsList("Setelah 10 Tahun ( Berita bagus lainnya! Risiko kematian anda akibat kanker paru-paru akan\u2028berkurang setengahnya dibandingkan dengan perokok.)",R.drawable.nmbr_eight,R.drawable.page_eight),


        };
        InformasiMenjauhiRokokAdapter adapter = new InformasiMenjauhiRokokAdapter(myListData);
        rV_tips.setHasFixedSize(true);
        rV_tips.setLayoutManager(new LinearLayoutManager(this));
        rV_tips.setAdapter(adapter);
    }
}