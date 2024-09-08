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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import example.com.fielthyapps.R;

public class FragmentMedcheck extends Fragment {
    private String title;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<HistoryList> itemList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public FragmentMedcheck() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_medcheck, container, false);

        if (getArguments() != null) {
            title = getArguments().getString("title");
        }

        recyclerView = view.findViewById(R.id.rV_medcheck);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemList = new ArrayList<>();
        adapter = new HistoryAdapter(itemList);
        recyclerView.setAdapter(adapter);
        loadDataFromFirestore();

        return view;
    }

    private void loadDataFromFirestore() {
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("medcheck")
                .whereEqualTo("uid", uid)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String date = document.getString("date");
                        String id = document.getString("id");
                        HistoryList history = new HistoryList();
                        history.setDate(date);
                        history.setId(id);
                        history.setGender(document.getString("gender"));
                        history.setBerat(document.getString("berat"));
                        history.setDiastolik(document.getString("diastolik"));
                        history.setGuladarah(document.getString("guladarah"));
                        history.setBmi(document.getString("hasilbmi"));
                        history.setLemak(document.getString("lemak"));
                        history.setLingkar_perut(document.getString("lingkarperut"));
                        history.setSistolik(document.getString("sistolik"));
                        history.setTinggi(document.getString("tinggi"));
                        history.setUid(document.getString("uid"));
                        itemList.add(history);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
