package example.com.fielthyapps.Feature.Stress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.com.fielthyapps.Feature.Nutrition.MakananPokokActivity;
import example.com.fielthyapps.Feature.Nutrition.NutritionActivity;
import example.com.fielthyapps.R;

public class SectionSatuActivity extends AppCompatActivity {
    private RecyclerView rV_pertama;
    private Button btn_lanjut;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fStore;
    String formattedDate;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_satu);
        rV_pertama = findViewById(R.id.rV_quest_pertama);
        btn_lanjut = findViewById(R.id.btn_lanjut_hasil);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        List<QuestList> questions = new ArrayList<>();


        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }

        // Mengatur format tanggal
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }

        // Mengonversi LocalDate ke String
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate= currentDate.format(formatter);
        }

        questions.add(new QuestList("Saya mudah kesal akibat hal-hal kecil", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya cenderung reaktif berlebihan", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya sulit merasa relaks/santai", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa mudah kecewa", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya sering merasa gugup", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa tidak sabaran", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa mudah tersinggung", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya sulit untuk melepas ketegangan ", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa mudah terganggu", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya kesulitan untuk tenang setelah mengalami kekecewaan/kekesalan", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya tidak dapat mentolerir gangguan apapun saat saya sedang melakukan sesuatu", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasakan ketegangan", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya tidak dapat mentolerir hal-hal yang mengalihkan saya dari apa yang sedang saya lakukan", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));
        questions.add(new QuestList("Saya merasa gelisah", Arrays.asList("Tidak sesuai/tidak pernah", "Perilaku muncul sesekali/jarang", "Sesuai/cukup sering muncul", "Sangat sesuai/sangat sering muncul")));

        QuestSectionsatuAdapter adapter = new QuestSectionsatuAdapter(questions);
        rV_pertama.setHasFixedSize(true);
        rV_pertama.setLayoutManager(new LinearLayoutManager(this));
        rV_pertama.setAdapter(adapter);

        Intent iin = getIntent();
        final Bundle b = iin.getExtras();

        if (b != null) {
            id = (String) b.get("id");
        }


        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = firebaseUser.getUid();
                DocumentReference documentReference = fStore.collection("stress").document(id);
                Map<String, Object> answers = new HashMap<>();
                String id_doc = documentReference.getId();
                answers.put("uid", uid);
                answers.put("id", id_doc);
                answers.put("date", formattedDate);
                for (int i = 0; i < questions.size(); i++) {
                    QuestList question = questions.get(i);
                    int selectedOption = question.getSelectedOption();
                    if (selectedOption != -1) {
                        String selectedOptionText = question.getOptions().get(selectedOption);
                        answers.put("quest" + (i + 1), selectedOptionText);
                    } else {
                        answers.put("quest" + (i + 1), "Tidak ada jawaban dipilih");
                    }
                }

                documentReference.set(answers)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                DocumentReference updatestress = fStore.collection("stresstest").document(id);
                                Map<String, Object> data = new HashMap<>();
                                data.put("stress", "1");
                                updatestress.update(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(SectionSatuActivity.this, "Berhasil input data Stress", Toast.LENGTH_SHORT).show();
                                                Log.d("Firestore", "Data berhasil disimpan.");
                                                Intent intent = new Intent(SectionSatuActivity.this,HasilStressActivity.class);
                                                intent.putExtra("status","stress");
                                                intent.putExtra("id",id);
                                                intent.putExtra("type","test");
                                                startActivity(intent);
                                            }
                                        });

                                // Tampilkan pesan berhasil atau lakukan sesuatu
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Firestore", "Gagal menyimpan data", e);
                                // Tampilkan pesan gagal atau lakukan sesuatu
                            }
                        });


//                HashMap<String, String> questionAnswerMap = new HashMap<>();
//                for (int i = 0; i < myListData.length; i++) {
//                    QuestList quest = myListData[i];
//                    int selectedOption = quest.getSelectedOption();
//                    if (selectedOption != -1) {
//                        String selectedOptionText = getResources().getString(selectedOption);
//                        // Jawaban dipilih untuk pertanyaan ke-i
//                        // Lakukan sesuatu dengan nilai selectedOption
//                        Log.d("Jawaban dipilih", "Pertanyaan ke-" + i + ": Option " + selectedOptionText);
//                        questionAnswerMap.put("Pertanyaan ke-" + (i + 1), "Option " + selectedOptionText);
//                    } else {
//                        // Tidak ada jawaban dipilih untuk pertanyaan ke-i
//                        Log.d("Jawaban dipilih", "Pertanyaan ke-" + i + ": Tidak ada jawaban dipilih");
//                    }
//                }
//                DocumentReference documentReference = fStore.collection("secsatu").document();
//
//                // Simpan HashMap ke dokumen
//                documentReference.set(questionAnswerMap)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Log.d("Firestore", "Data berhasil disimpan.");
//                                // Tampilkan pesan sukses atau lakukan sesuatu
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w("Firestore", "Gagal menyimpan data", e);
//                                // Tampilkan pesan gagal atau lakukan sesuatu
//                            }
//                        });
            }
        });
    }
}