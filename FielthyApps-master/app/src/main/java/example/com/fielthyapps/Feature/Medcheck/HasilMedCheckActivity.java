package example.com.fielthyapps.Feature.Medcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import example.com.fielthyapps.Feature.History.HistoryActivity;
import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.R;
import example.com.fielthyapps.Service.ElevenLabs;

public class HasilMedCheckActivity extends AppCompatActivity {
    private TextView imt,lingkarperut,tekanandarah,guladarah,lemak,tV_indikator_tekanan,tV_indikator_gula,tV_indikator_imt,tV_kategori_ptm,tV_indikator_kolestrol;
    private String status,date,uid,id,get_berat,get_tinggi,get_lingkar_perut,get_sistolik,get_diastolik,get_lemak,get_guladarah,get_bmi,get_gender;
    private Button btn_selesai;
    private ElevenLabs elevenLabs;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (elevenLabs != null) {
            elevenLabs.stopMp3();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_med_check);
        imt = findViewById(R.id.tV_hasil_imt);
        lingkarperut = findViewById(R.id.tV_hasil_lingkar);
        tekanandarah = findViewById(R.id.tV_hasil_tekanan);
        guladarah = findViewById(R.id.tV_hasil_gula);
        tV_indikator_tekanan = findViewById(R.id.tV_indikator_tekanan);
        tV_indikator_gula = findViewById(R.id.tV_indikator_gula);
        tV_indikator_imt = findViewById(R.id.tV_indikator_imt);
//        tV_kategori_ptm = findViewById(R.id.tV_kategori_ptm);
        tV_indikator_kolestrol = findViewById(R.id.tV_indikator_kolestrol);
        btn_selesai = findViewById(R.id.btn_selesai);
        lemak = findViewById(R.id.tV_hasil_lemak);
        Intent iin = getIntent();
        final Bundle b = iin.getExtras();
        elevenLabs = new ElevenLabs(HasilMedCheckActivity.this);


        if (b != null) {
            id = (String) b.get("id");
            date = (String) b.get("date");
            uid = (String) b.get("uid");
            get_gender = (String) b.get("gender");
            get_berat = (String) b.get("berat");
            get_tinggi = (String) b.get("tinggi");
            get_lingkar_perut = (String) b.get("lingkarperut");
            get_sistolik = (String) b.get("sistolik");
            get_diastolik = (String) b.get("diastolik");
            get_guladarah = (String) b.get("guladarah");
            get_lemak = (String) b.get("lemak");
            get_bmi = (String) b.get("hasilbmi");
            status = (String) b.get("status");
            imt.setText(get_bmi);
            lingkarperut.setText(get_lingkar_perut);
            tekanandarah.setText(get_sistolik + "/" + get_diastolik);
            lemak.setText(get_lemak);
            guladarah.setText(get_guladarah);

            ImageButton imgBtnHasilPemeriksaan = findViewById(R.id.imgBtnHasilPemeriksaan);
            imgBtnHasilPemeriksaan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = "Hasil Pemeriksaan," +
                            "Dibawah ini adalah hasil rangkuman dari pemeriksaan fisik anda yang telah anda isi sebelumnya." +
                            "IMT(Indeks Masa Tubuh) " + get_bmi +
                            "Lingkar Perut " + get_lingkar_perut +
                            "Tekanan Darah " + get_sistolik + "/" + get_diastolik +
                            "Gula Darah " + get_guladarah +
                            "Kolestrol " + get_lemak;
                    if (elevenLabs != null) {
                        try {
                            elevenLabs.textToSpeech(text);
                        } catch (Exception e) {
                            Toast.makeText(HasilMedCheckActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });


            // Ganti koma dengan titik
            String valueWithDot = get_bmi.replace(',', '.');

            // Konversi string menjadi double
            double doubleValue = Double.parseDouble(valueWithDot);

            // Membulatkan double ke bilangan bulat terdekat
            int edu_score_imt = (int) Math.round(doubleValue);
//            float edu_score_imt = Float.parseFloat(get_bmi);


            int edu_lingkar = Integer.parseInt(get_lingkar_perut);
            int edu_sistolik = Integer.parseInt(get_sistolik);
            int edu_diastolik = Integer.parseInt(get_diastolik);
            int edu_gula = Integer.parseInt(get_guladarah);
            int edu_lemak = Integer.parseInt(get_lemak);
            String ket_imt, ket_darah, ket_gula, ket_lemak;
            String textLaporanKesehatan = "";

            if (edu_score_imt >= 27.1) {
                ket_imt = "Obesitas";
                String imtText = "Anda Menderita Obesitas" +
                        "\n1. Diperlukan diet rendah kalori dan tinggi nutrisi" +
                        "\n2. Lakukan olahraga setidaknya 30-60 menit/hari" +
                        "\n3. Dianjurkan untuk konsultasi ke dokter minimal 3-6 bulan sekali untuk memantau kondisi";
                tV_indikator_imt.setText(imtText);
                textLaporanKesehatan += imtText + "\n";

                if (get_gender.equals("Laki - Laki")) {
                    if (edu_lingkar >= 90) {
                        ket_imt = "Obesitas";
                        imtText = "Anda Menderita Obesitas" +
                                "\n1. Diperlukan diet rendah kalori dan tinggi nutrisi" +
                                "\n2. Lakukan olahraga setidaknya 30-60 menit/hari" +
                                "\n3. Dianjurkan untuk konsultasi ke dokter minimal 3-6 bulan sekali untuk memantau kondisi";
                        tV_indikator_imt.setText(imtText);
                        textLaporanKesehatan += imtText + "\n";
                    } else if (edu_lingkar < 90) {
                        ket_imt = "Normal";
                        imtText = "Berat Badan Anda Normal";
                        tV_indikator_imt.setText(imtText);
                        textLaporanKesehatan += imtText + "\n";
                    }
                } else if (get_gender.equals("Perempuan")) {
                    if (edu_lingkar >= 80) {
                        ket_imt = "Obesitas";
                        imtText = "Anda Menderita Obesitas" +
                                "\n1. Diperlukan diet rendah kalori dan tinggi nutrisi" +
                                "\n2. Lakukan olahraga setidaknya 30-60 menit/hari" +
                                "\n3. Dianjurkan untuk konsultasi ke dokter minimal 3-6 bulan sekali untuk memantau kondisi";
                        tV_indikator_imt.setText(imtText);
                        textLaporanKesehatan += imtText + "\n";
                    } else if (edu_lingkar < 80) {
                        ket_imt = "Normal";
                        imtText = "Berat Badan Anda Normal";
                        tV_indikator_imt.setText(imtText);
                        textLaporanKesehatan += imtText + "\n";
                    }
                }
            } else if (edu_score_imt >= 25.0 && edu_score_imt <= 27.0) {
                ket_imt = "Kelebihan BB";
                String imtText = "Anda Kelebihan Berat Badan" +
                        "\n1. Kurangi makanan tinggi gula dan lemak jenuh" +
                        "\n2. Lakukan olahraga setidaknya 30-60 menit/hari" +
                        "\n3. Dianjurkan untuk konsultasi ke dokter minimal 3-6 bulan sekali untuk memantau kondisi";
                tV_indikator_imt.setText(imtText);
                textLaporanKesehatan += imtText + "\n";
            } else {
                if (get_gender.equals("Laki - Laki")) {
                    if (edu_lingkar < 90) {
                        ket_imt = "Normal";
                        String imtText = "Berat Badan Anda Normal";
                        tV_indikator_imt.setText(imtText);
                        textLaporanKesehatan += imtText + "\n";
                    } else if (edu_lingkar >= 90) {
                        ket_imt = "Obesitas";
                        String imtText = "Anda Menderita Obesitas";
                        tV_indikator_imt.setText(imtText);
                        textLaporanKesehatan += imtText + "\n";
                    }
                } else if (get_gender.equals("Perempuan")) {
                    if (edu_lingkar >= 80) {
                        ket_imt = "Obesitas";
                        String imtText = "Anda Menderita Obesitas";
                        tV_indikator_imt.setText(imtText);
                        textLaporanKesehatan += imtText + "\n";
                    } else if (edu_lingkar < 80) {
                        ket_imt = "Normal";
                        String imtText = "Berat Badan Anda Normal";
                        tV_indikator_imt.setText(imtText);
                        textLaporanKesehatan += imtText + "\n";
                    }
                }

                if (edu_score_imt >= 1 && edu_score_imt <= 18.4) {
                    ket_imt = "Kekurangan BB";
                    String imtText = "Anda Kekurangan Berat Badan" +
                            "\n1. Konsumsi makanan dengan kalori yang tinggi" +
                            "\n2. Fokus olahraga pada latihan kekuatan" +
                            "\n3. Hindari aktivitas berlebih";
                    tV_indikator_imt.setText(imtText);
                    textLaporanKesehatan += imtText + "\n";
                } else {
                    if (get_gender.equals("Laki - Laki")) {
                        if (edu_lingkar < 90) {
                            ket_imt = "Normal";
                            String imtText = "Berat Badan Anda Normal";
                            tV_indikator_imt.setText(imtText);
                            textLaporanKesehatan += imtText + "\n";
                        } else if (edu_lingkar >= 90) {
                            ket_imt = "Obesitas";
                            String imtText = "Anda Menderita Obesitas";
                            tV_indikator_imt.setText(imtText);
                            textLaporanKesehatan += imtText + "\n";
                        }
                    } else if (get_gender.equals("Perempuan")) {
                        if (edu_lingkar >= 80) {
                            ket_imt = "Obesitas";
                            String imtText = "Anda Menderita Obesitas";
                            tV_indikator_imt.setText(imtText);
                            textLaporanKesehatan += imtText + "\n";
                        } else if (edu_lingkar < 80) {
                            ket_imt = "Normal";
                            String imtText = "Berat Badan Anda Normal";
                            tV_indikator_imt.setText(imtText);
                            textLaporanKesehatan += imtText + "\n";
                        }
                    }

                    if (edu_score_imt >= 18.5 && edu_score_imt <= 25.0) {
                        ket_imt = "Normal BB";
                        String imtText = "Berat Badan Anda Normal" +
                                "\n1. Pertahankan pola makan seimbang" +
                                "\n2. Lakukan olahraga teratur" +
                                "\n3. Lakukan pengecekan kesehatan rutin minimal 1 tahun sekali";
                        tV_indikator_imt.setText(imtText);
                        textLaporanKesehatan += imtText + "\n";
                    } else {
                        if (get_gender.equals("Laki - Laki")) {
                            if (edu_lingkar < 90) {
                                ket_imt = "Normal";
                                String imtText = "Berat Badan Anda Normal";
                                tV_indikator_imt.setText(imtText);
                                textLaporanKesehatan += imtText + "\n";
                            } else if (edu_lingkar >= 90) {
                                ket_imt = "Obesitas";
                                String imtText = "Anda Menderita Obesitas";
                                tV_indikator_imt.setText(imtText);
                                textLaporanKesehatan += imtText + "\n";
                            }
                        } else if (get_gender.equals("Perempuan")) {
                            if (edu_lingkar >= 80) {
                                ket_imt = "Obesitas";
                                String imtText = "Anda Menderita Obesitas";
                                tV_indikator_imt.setText(imtText);
                                textLaporanKesehatan += imtText + "\n";
                            } else if (edu_lingkar < 80) {
                                ket_imt = "Normal";
                                String imtText = "Berat Badan Anda Normal";
                                tV_indikator_imt.setText(imtText);
                                textLaporanKesehatan += imtText + "\n";
                            }
                        }
                    }
                }
            }

            if (edu_sistolik < 130 && edu_diastolik <= 84) {
                ket_darah = "Normal";
                String darahText = "Tekanan Darah Anda Normal" +
                        "\n1. Pertahankan gaya hidup sehat" +
                        "\n2. Olahraga Aerobik teratur setidaknya 150 menit/minggu" +
                        "\n3. Hindari merokok dan alkohol berlebihan" +
                        "\n4. Lakukan pengecekan darah kembali setidaknya 1 tahun sekali";
                tV_indikator_tekanan.setText(darahText);
                textLaporanKesehatan += darahText + "\n";
            } else if (edu_sistolik <= 139 && edu_diastolik <= 89) {
                ket_darah = "Berisiko";
                String darahText = "Tekanan Darah Anda Beresiko Hipertensi" +
                        "\n1. Kurangi komsumsi garam dan lebih sering untuk olahraga" +
                        "\n2. Lakukan pengecekan darah setidaknya 1 bulan sekali untuk pemantauan" +
                        "\n3. Lakukan konsultasi dengan dokter terkait setidaknya 6 bulan sekali";
                tV_indikator_tekanan.setText(darahText);
                textLaporanKesehatan += darahText + "\n";
            } else if (edu_sistolik >= 140 && edu_diastolik >= 90) {
                ket_darah = "Hipertensi";
                String darahText = "Anda Menderita Hipertensi" +
                        "\n1. Lakukan Diet DASH (Diet khusus untuk hipertensi)" +
                        "\n2. Lakukan pengencekan darah setidaknya 1 minggu sekali atau sesuai rekomendasi dokter" +
                        "\n3. Lakukan konsultasi dengan dokter terkait setidaknya 1 bulan sekali sampai tekanan darah terkendali";
                tV_indikator_tekanan.setText(darahText);
                textLaporanKesehatan += darahText + "\n";
            }

            if (edu_gula < 200) {
                ket_gula = "Normal";
                String gulaText = "Gula Darah Anda Normal" +
                        "\n1. Pertahankan gaya hidup sehat" +
                        "\n2. Lakukan olahraga teratur" +
                        "\n3. Lakukan pengecekan gula darah kembali setidaknya 3-6 bulan sekali";
                tV_indikator_gula.setText(gulaText);
                textLaporanKesehatan += gulaText + "\n";
            } else if (edu_gula >= 200) {
                ket_gula = "Diabetes";
                String gulaText = "Anda menderita Diabetes" +
                        "\n1. Hindari makanan tinggi gula" +
                        "\n2. Lakukan olahraga teratur minimal 30 menit/hari" +
                        "\n3. Periksa kembali gula darah minimal 2-4 hari sekali" +
                        "\n4. Konsultasi ke ahli gizi untuk mendapatkan rencana diet yang sesuai dan ikuti pengobatan dari dokter";
                tV_indikator_gula.setText(gulaText);
                textLaporanKesehatan += gulaText + "\n";
            }

            if (edu_lemak <= 200) {
                ket_lemak = "Normal";
                String lemakText = "Kolestrol Anda Normal" +
                        "\n1. Pertahankan gaya hidup sehat" +
                        "\n2. Lakukan olahraga teratur" +
                        "\n3. Periksa kembali kolesterol total setidaknya 1 tahun sekali";
                tV_indikator_kolestrol.setText(lemakText);
                textLaporanKesehatan += lemakText + "\n";
            } else if (edu_lemak > 200) {
                ket_lemak = "Dislipidemia";
                String lemakText = "Anda Menderita Dislipidemia" +
                        "\n1. Hindari makanan tinggi lemak jenuh, seperti gorengan dan daging merah" +
                        "\n2. Lakukan olahraga teratur minimal 30 menit/hari" +
                        "\n3. Periksa kembali kolesterol total setidaknya 3-6 bulan sekali" +
                        "\n4. Ikuti pengobatan yang dianjurkan oleh dokter";
                tV_indikator_kolestrol.setText(lemakText);
                textLaporanKesehatan += lemakText + "\n";
            }

            ImageButton imgBtnLaporanKesehatan = findViewById(R.id.imgBtnLaporanKesehatan);
            String finalTextLaporanKesehatan = textLaporanKesehatan;
            imgBtnLaporanKesehatan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (elevenLabs != null) {
                        try {
                            elevenLabs.textToSpeech(finalTextLaporanKesehatan);
                        } catch (Exception e) {
                            Toast.makeText(HasilMedCheckActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        btn_selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("testmedcheck")){
                    startActivity(new Intent(HasilMedCheckActivity.this, HomeActivity.class));
                    finish();
                }else if (status.equals("historymedcheck")){
                    startActivity(new Intent(HasilMedCheckActivity.this, HistoryActivity.class));
                    finish();
                }



            }
        });


    }
}