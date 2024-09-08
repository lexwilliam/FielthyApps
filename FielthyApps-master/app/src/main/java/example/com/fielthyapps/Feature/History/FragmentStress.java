package example.com.fielthyapps.Feature.History;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import example.com.fielthyapps.Feature.History.adapter.PhysicalAdapter;
import example.com.fielthyapps.Feature.History.adapter.StressAdapter;
import example.com.fielthyapps.Feature.History.data.PhysicalList;
import example.com.fielthyapps.Feature.History.data.StressList;
import example.com.fielthyapps.R;


public class FragmentStress extends Fragment {
    TextView textView;
    private String title;
    private RecyclerView recyclerView;
    private StressAdapter adapter;
    private List<StressList> itemList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view =inflater.inflate(R.layout.fragment_stress, container, false);

        // Get Title
        // Assign variable
        if (getArguments() != null) {
            title = getArguments().getString("title");
        }

        recyclerView = view.findViewById(R.id.rV_stress);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemList = new ArrayList<>();
        // Add data to itemList based on fragment type

        adapter = new StressAdapter(itemList);
        recyclerView.setAdapter(adapter);
        loadDataFromFirestore();
//        loadDataMwt();
        return view;
    }

    private void loadDataFromFirestore() {
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("stresstest")
                .whereEqualTo("uid", uid)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String date = document.getString("date");
                        String id = document.getString("id");
                        StressList stressList = new StressList();
                        stressList.setId(id);
                        stressList.setDate(date);
                        stressList.setUid(uid);
                        itemList.add(stressList);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                });
    }



}