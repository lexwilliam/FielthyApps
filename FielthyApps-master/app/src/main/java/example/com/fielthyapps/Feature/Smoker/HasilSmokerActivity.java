package example.com.fielthyapps.Feature.Smoker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import example.com.fielthyapps.Feature.History.HistoryActivity;
import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class HasilSmokerActivity extends AppCompatActivity {
private LinearLayout LL_tips,LL_berhenti;
private Button selesai;
private TextView tittle,rupiah,tV_bungkus,indicator,seminggu,sebulan,setahun,limatahun,sepuluhtahun,duapuluhtahun;
private String getBatang,getRupiah,getBungkus,getTahun,id,uid,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_smoker);
        tittle = findViewById(R.id.tV_tittle);
        rupiah = findViewById(R.id.tV_rupiah);
        tV_bungkus = findViewById(R.id.tV_bungkus);
        indicator = findViewById(R.id.tV_indicator_rokok);
        seminggu = findViewById(R.id.tV_price_seminggu);
        sebulan = findViewById(R.id.tV_price_sebulan);
        setahun = findViewById(R.id.tV_price_setahun);
        limatahun = findViewById(R.id.tV_price_limatahun);
        sepuluhtahun = findViewById(R.id.tV_price_sepuluhtahun);
        duapuluhtahun = findViewById(R.id.tV_price_duapuluhtahun);
        LL_tips = findViewById(R.id.LL_tips);
        LL_berhenti = findViewById(R.id.LL_berhenti_merokok);
        selesai = findViewById(R.id.btn_selesai);

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

            tittle.setText("Anda Mengeluarkan Uang dalam" + getTahun + "tahun terakhir sebesar");

            int harga = Integer.parseInt(getRupiah);
            int bungkus = Integer.parseInt(getBungkus);
            int batang = Integer.parseInt(getBatang);
            int tahun = Integer.parseInt(getTahun);

            int hasil = (harga/bungkus) * batang*(tahun*365);
            Locale localeID = new Locale("in", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
            rupiah.setText(formatRupiah.format((double)hasil));

            if (batang >= 20){
                indicator.setText("Perokok Berat");
            }else if (batang <20){
                indicator.setText("Perokok Ringan");
            }
            int hasil_seminggu = hasil/52/tahun;
            int hasil_sebulan  = hasil/12/tahun;
            int hasil_setahun = hasil/tahun;
            int hasil_limaTahun = (harga/bungkus)*batang*(5*365);
            int hasil_sepuluhTahun = (harga/bungkus)*batang*(10*365);
            int hasil_duapuluhTahun = (harga/bungkus)*batang*(20*365);

            seminggu.setText(formatRupiah.format((double)hasil_seminggu));
            sebulan.setText(formatRupiah.format((double)hasil_sebulan));
            setahun.setText(formatRupiah.format((double)hasil_setahun));
            limatahun.setText(formatRupiah.format((double)hasil_limaTahun));
            sepuluhtahun.setText(formatRupiah.format((double)hasil_sepuluhTahun));
            duapuluhtahun.setText(formatRupiah.format((double)hasil_duapuluhTahun));
            tV_bungkus.setText(getBatang);

        }

        LL_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HasilSmokerActivity.this,TipsBerhentiMerokokActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("uid", uid);
                intent.putExtra("batang", getBatang);
                intent.putExtra("tahun", getTahun);
                intent.putExtra("bungkus", getBungkus);
                intent.putExtra("rupiah", getRupiah);
                intent.putExtra("status", "testsmoker");
                startActivity(intent);
            }
        });

        LL_berhenti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HasilSmokerActivity.this, InformasiMenjauhiRokokActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("uid", uid);
                intent.putExtra("batang", getBatang);
                intent.putExtra("tahun", getTahun);
                intent.putExtra("bungkus", getBungkus);
                intent.putExtra("rupiah", getRupiah);
                intent.putExtra("status", "testsmoker");
                startActivity(intent);
            }
        });

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("testsmoker")){
                    Intent intent = new Intent(HasilSmokerActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else if (status.equals("historysmoker")){
                    Intent intent = new Intent(HasilSmokerActivity.this, HistoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }



            }
        });
    }
}