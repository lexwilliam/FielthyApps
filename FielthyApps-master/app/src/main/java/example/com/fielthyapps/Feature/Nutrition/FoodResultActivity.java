package example.com.fielthyapps.Feature.Nutrition;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import example.com.fielthyapps.Repository.FoodDetailListener;
import example.com.fielthyapps.Repository.NutritionRepository;
import example.com.fielthyapps.Repository.NutritionRepositoryImpl;
import example.com.fielthyapps.Service.ElevenLabs;
import example.com.fielthyapps.Service.Model.FoodDetail;
import example.com.fielthyapps.Service.Model.FoodDetailServing;
import example.com.fielthyapps.databinding.ActivityFoodResultBinding;

public class FoodResultActivity extends AppCompatActivity {
    private NutritionRepository repository;
    private ActivityFoodResultBinding binding;
    private ElevenLabs tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String value = getIntent().getStringExtra("name");
        tts = new ElevenLabs(this);
        loadData(value);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (tts != null) {
            tts.stopMp3();
        }
    }

    void loadData(String value) {
        repository = new NutritionRepositoryImpl();
        repository.getFoodDetail(
                value,
                new FoodDetailListener() {
                    @Override
                    public void onSuccess(FoodDetail foodDetail) {
                        if (foodDetail != null && value != null) {
                            FoodDetailServing serving = foodDetail.getServings().getServingList().get(0);
                            binding.tvName.setText(foodDetail.getFoodName());
                            binding.tvWeight.setText(serving.getMetricServingAmount() + " " + serving.getMetricServingUnit());
                            binding.tvCalories.setText(serving.getCalories() + " Kcal");
                            binding.tvProtein.setText(serving.getProtein() + " gr");
                            binding.tvCarbs.setText(serving.getCarbohydrate() + " gr");
                            binding.tvFat.setText(serving.getFat() + " gr");
                            binding.tvMetricServing.setText(serving.getMetricServingAmount() + " " + serving.getMetricServingUnit());
                            binding.tvSaturatedFat.setText(serving.getSaturatedFat() + " gr");
                            binding.tvPolyunsaturatedFat.setText(serving.getPolyunsaturatedFat() + " gr");
                            binding.tvMonounsaturatedFat.setText(serving.getMonounsaturatedFat() + " gr");
                            binding.tvCholesterol.setText(serving.getCholesterol() + " mg");
                            binding.tvSodium.setText(serving.getSodium() + " mg");
                            binding.tvPotassium.setText(serving.getPotassium() + " mg");
                            binding.tvVitaminA.setText(serving.getVitaminA() + " mg");
                            binding.tvCalcium.setText(serving.getCalcium() + " mg");
                            binding.tvIron.setText(serving.getIron() + " mg");
                            binding.tvFiber.setText(serving.getFiber() + " gr");
                            binding.tvSugar.setText(serving.getSugar() + " gr");
                            binding.btnTTS.setOnClickListener(v -> {
                                if (tts != null) {;
                                    tts.textToSpeech(serving.toSpeech());
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(FoodResultActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}