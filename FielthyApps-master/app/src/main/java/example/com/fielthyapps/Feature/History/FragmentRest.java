package example.com.fielthyapps.Feature.History;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import example.com.fielthyapps.Feature.History.adapter.RestAdapter;
import example.com.fielthyapps.Feature.History.data.RestList;
import example.com.fielthyapps.R;

public class FragmentRest extends Fragment {
    TextView textView;
    private String title;
    private RecyclerView recyclerView;
    private RestAdapter adapter;
    private List<RestList> itemList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_rest, container, false);

        // Assign variable
        if (getArguments() != null) {
            title = getArguments().getString("title");
        }

        recyclerView = view.findViewById(R.id.rV_rest);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemList = new ArrayList<>();
        adapter = new RestAdapter(itemList);
        recyclerView.setAdapter(adapter);

        loadDataFromFirestore();

        return view;
    }

    private void loadDataFromFirestore() {
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("restpattern")
                .whereEqualTo("uid", uid)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String date = document.getString("date");
                        String id = document.getString("id");
                        RestList rest = new RestList();
                        rest.setDate(date);
                        rest.setId(id);
                        rest.setUid(uid);
                        rest.setDay(document.getString("day"));
                        rest.setTimesleep(document.getString("timesleep"));
                        itemList.add(rest);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                });
    }
}
