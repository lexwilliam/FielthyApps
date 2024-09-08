package example.com.fielthyapps.Feature.RestPattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;

public class HasilTestRestActivity extends AppCompatActivity {
    private TextView tV_status,tV_desc_status;
    private ImageView iV_back;
    private String type_ket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_test_rest);
        iV_back = findViewById(R.id.iV_kembali);
        tV_status = findViewById(R.id.tV_status);
        tV_desc_status = findViewById(R.id.tV_desc_status);

        Intent iin = getIntent();
        final Bundle b = iin.getExtras();

        if (b != null) {
            type_ket = (String) b.get("type");

            if (type_ket.equals("Bad Sleep")){
                tV_status.setText(type_ket);
                tV_desc_status.setText("Hasil Tidur Anda: Bad Sleep\n" +
                        "Tidur Anda kurang berkualitas. Berikut rinciannya:\n" +
                        "1. Durasi Tidur Kurang: Anda tidur kurang dari 7-9 jam yang disarankan.\n" +
                        "2. Kualitas Tidur Rendah: Tidur Anda sering terganggu.\n" +
                        "3. Efisiensi Tidur Rendah: Banyak waktu di tempat tidur tapi tidak benar-benar tidur.\n" +
                        "Tips untuk Meningkatkan Tidur Anda:\n" +
                        "• Tetapkan Jadwal: Tidur dan bangun pada waktu yang sama setiap hari.\n" +
                        "• Lingkungan Nyaman: Pastikan kamar Anda sejuk, gelap, dan tenang.\n" +
                        "• Ritual Sebelum Tidur: Lakukan kegiatan menenangkan seperti membaca.\n" +
                        "• Kurangi Stimulasi: Hindari kafein dan perangkat elektronik sebelum tidur.\n" +
                        "• Aktivitas Fisik: Olahraga teratur, tapi hindari dekat waktu tidur.\n" +
                        "Jika masalah tidur berlanjut, konsultasikan dengan profesional kesehatan.");

            }else if (type_ket.equals("Good Sleep")){
                tV_status.setText(type_ket);
                tV_desc_status.setText("Tidur Anda sangat baik! Berikut rincian hasilnya:\n" +
                        "1. Durasi Tidur Optimal: Anda tidur selama [jumlah jam] jam, sesuai dengan anjuran untuk orang dewasa.\n" +
                        "2. Kualitas Tidur Tinggi: Anda memiliki keseimbangan yang baik antara tidur ringan, tidur dalam, dan tidur REM.\n" +
                        "3. Efisiensi Tidur Tinggi: Anda tidur nyenyak dengan sedikit gangguan di malam hari.\n" +
                        "Tips untuk Mempertahankan Kualitas Tidur:\n" +
                        "• Jaga Konsistensi: Tidur dan bangun pada waktu yang sama setiap hari.\n" +
                        "• Ciptakan Lingkungan Nyaman: Pastikan kamar Anda sejuk, gelap, dan tenang.\n" +
                        "• Ritual Sebelum Tidur: Lakukan kegiatan yang menenangkan seperti membaca.\n" +
                        "• Hindari Stimulasi: Kurangi kafein dan layar elektronik sebelum tidur.\n" +
                        "Terus jaga kebiasaan baik ini dan nikmati tidur yang berkualitas.");

            }else if (type_ket.equals("Over Sleep")){
                tV_status.setText(type_ket);
                tV_desc_status.setText("Anda mungkin tidur terlalu banyak. Ini adalah hasil oversleeping. Berikut penjelasannya:\n" +
                        "1. Durasi Tidur Berlebihan: Anda tidur lebih dari yang disarankan (lebih dari 9 jam).\n" +
                        "2. Kualitas Tidur Tidak Jelas: Meskipun tidur lama, Anda mungkin masih merasa lelah atau tidak segar.\n" +
                        "3. Efisiensi Tidur Menurun: Meskipun tidur lama, Anda mungkin sering terbangun atau tidur tidak nyenyak.\n" +
                        "Tips untuk Mengatasi Oversleeping:\n" +
                        "• Tetapkan Jadwal Tidur Tetap: Tidur dan bangun pada waktu yang sama setiap hari.\n" +
                        "• Batasi Tidur Siang: Hindari tidur siang terlalu lama agar tidak mengganggu tidur malam.\n" +
                        "• Perhatikan Pola Makan: Hindari makan berlebihan atau makan terlalu dekat dengan waktu tidur.\n" +
                        "• Konsultasikan dengan Dokter: Jika masalah tidur Anda berlanjut, pertimbangkan untuk berkonsultasi dengan dokter atau ahli tidur.");

            }

        }

        iV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HasilTestRestActivity.this, RestPatternActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

//        stopButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stopStopwatchService();
//                startActivity(new Intent(HasilTestRestActivity.this, HomeActivity.class));
//                finish();
//            }
//        });
    }


}