package example.com.fielthyapps.Feature.History;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import example.com.fielthyapps.Feature.History.adapter.NutritionAdapter;
import example.com.fielthyapps.Feature.History.data.NutritionList;
import example.com.fielthyapps.R;

public class FragmentNutrition extends Fragment {
    private String title;
    private RecyclerView recyclerView;
    private NutritionAdapter adapter;
    private List<NutritionList> itemList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);

        // Assign variables
        if (getArguments() != null) {
            title = getArguments().getString("title");
        }

        recyclerView = view.findViewById(R.id.rV_nutrition);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemList = new ArrayList<>();
        adapter = new NutritionAdapter(itemList);
        recyclerView.setAdapter(adapter);
        loadDataFromFirestore();
        return view;
    }

    private void loadDataFromFirestore() {
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("nutritiontest")
                .whereEqualTo("uid", uid)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String date = document.getString("date");
                        String id = document.getString("id");
                        NutritionList nutrition = new NutritionList();
                        nutrition.setDate(date);
                        nutrition.setId(id);
                        nutrition.setUid(uid);
                        itemList.add(nutrition);
                    }
                    adapter.notifyDataSetChanged(); // Update adapter setelah itemList diubah
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
