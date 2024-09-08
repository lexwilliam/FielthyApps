package example.com.fielthyapps.Feature.History;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import example.com.fielthyapps.Feature.History.adapter.PhysicalAdapter;
import example.com.fielthyapps.Feature.History.data.PhysicalList;
import example.com.fielthyapps.R;

public class FragmentPyhsical extends Fragment {
    private String title;
    private RecyclerView recyclerView;
    private PhysicalAdapter adapter;
    private List<PhysicalList> itemList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pyhsical, container, false);

        if (getArguments() != null) {
            title = getArguments().getString("title");
        }

        recyclerView = view.findViewById(R.id.rV_physical);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemList = new ArrayList<>();

        adapter = new PhysicalAdapter(itemList);
        recyclerView.setAdapter(adapter);
        loadDataFromFirestore();
        return view;
    }

    private void loadDataFromFirestore() {
        String uid = mAuth.getCurrentUser().getUid();

        itemList.clear();

        db.collection("balke")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        PhysicalList physical = new PhysicalList();
                        String date = document.getString("date");
                        String id = document.getString("id");

                        physical.setIdbalke(id);
                        physical.setDatebalke(date);
                        physical.setUid(uid);
                        physical.setAgebalke(getStringOrDefault(document, "age"));
                        physical.setGenderbalke(getStringOrDefault(document, "gender"));
                        physical.setBeratbadanbalke(getStringOrDefault(document, "beratbadan"));
                        physical.setTinggibadanbalke(getStringOrDefault(document, "tinggibadan"));
                        physical.setJaraktempuhbalke(getStringOrDefault(document, "jaraktempuh"));
                        physical.setWaktubalke(getStringOrDefault(document, "waktu"));
                        physical.setType("balke");
                        addData(physical);
                    }

                    db.collection("6mwt")
                            .whereEqualTo("uid", uid)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                for (DocumentSnapshot document : queryDocumentSnapshots1) {
                                    PhysicalList physical = new PhysicalList();
                                    String date = document.getString("date");
                                    String id = document.getString("id");

                                    physical.setIdmwt(id);
                                    physical.setDatemwt(date);
                                    physical.setUid(uid);
                                    physical.setAgemwt(getStringOrDefault(document, "age"));
                                    physical.setGendermwt(getStringOrDefault(document, "gender"));
                                    physical.setBeratbadanmwt(getStringOrDefault(document, "beratbadan"));
                                    physical.setTinggibadanmwt(getStringOrDefault(document, "tinggibadan"));
                                    physical.setJaraktempuhmwt(getStringOrDefault(document, "jaraktempuh"));
                                    physical.setWaktumwt(getStringOrDefault(document, "waktu"));
                                    physical.setType("mwt");
                                    addData(physical);
                                }

                                sortAndNotify(); // Sort data and notify adapter
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                });
    }

    private void sortAndNotify() {
        List<PhysicalList> filteredList = new ArrayList<>();
        for (PhysicalList item : itemList) {
            String date = getDate(item);
            if (date != null && !date.trim().isEmpty()) {
                filteredList.add(item);
            }
        }

        // Sort filteredList by date in descending order
        Collections.sort(filteredList, (physical1, physical2) -> {
            Date date1 = parseDate(getDate(physical1));
            Date date2 = parseDate(getDate(physical2));
            if (date1 != null && date2 != null) {
                return date2.compareTo(date1); // descending order
            }
            return 0;
        });

        itemList.clear();
        itemList.addAll(filteredList);

        adapter.notifyDataSetChanged(); // Notify adapter that data has changed
    }


    private Date parseDate(String dateString) {
        List<String> dateFormats = Arrays.asList("dd/MM/yyyy", "yyyy-MM-dd"); // Add more formats as needed
        for (String format : dateFormats) {
            try {
                DateFormat dateFormat = new SimpleDateFormat(format);
                return dateFormat.parse(dateString);
            } catch (ParseException e) {
                // Continue to try the next format
            }
        }
        return null; // Return null if no format matches
    }

    private String getDate(PhysicalList physical) {
        if (physical.getType().equals("balke")) {
            return physical.getDatebalke();
        } else {
            return physical.getDatemwt();
        }
    }

    private String getStringOrDefault(DocumentSnapshot document, String field) {
        String value = document.getString(field);
        return value != null ? value.trim() : "";
    }

    public void addData(PhysicalList newData) {
        if (adapter != null) {
            adapter.addData(newData);
        }
    }
}

