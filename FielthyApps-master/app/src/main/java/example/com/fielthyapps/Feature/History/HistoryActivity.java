package example.com.fielthyapps.Feature.History;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import example.com.fielthyapps.Feature.Smoker.InformasiMenjauhiRokokAdapter;
import example.com.fielthyapps.Feature.Smoker.SmokerTipsList;
import example.com.fielthyapps.HomeActivity;
import example.com.fielthyapps.Auth.ProfileActivity;
import example.com.fielthyapps.R;

public class HistoryActivity extends AppCompatActivity {
private RecyclerView rV_history;
    // Initialize variables
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigate);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
//// assign variable
//        tabLayout=findViewById(R.id.tab_layout);
//        viewPager=findViewById(R.id.view_pager);

        // Initialize array list
        ArrayList<String> arrayList=new ArrayList<>(0);

        // Add title in array list
        arrayList.add("Med-check");
        arrayList.add("Smoker");
        arrayList.add("Physical");
        arrayList.add("Nutrition");
        arrayList.add("Rest");
        arrayList.add("Stress");


        // Setup tab layout
        tabLayout.setupWithViewPager(viewPager);

        // Prepare view pager
        prepareViewPager(viewPager,arrayList);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View tabView = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
                if (tabView != null) {

                    int paddingDp = 10; // Set your desired padding in dp
                    int paddingPx = (int) (paddingDp * getResources().getDisplayMetrics().density);
                    tabView.setPadding(paddingPx, 12, paddingPx, 12);

                    // Example: Set margin
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    params.setMargins(paddingPx, 25, paddingPx, 12);
                    tabView.setLayoutParams(params);


                    tabView.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_background));
                }
            }
        }


        bottomNavigationView.setSelectedItemId(R.id.bottom_history);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bottom_home) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                } else if (item.getItemId() == R.id.bottom_history) {
                    return true;
                } else if (item.getItemId() == R.id.bottom_profile) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                }
                return false;
            }
        });

//        showData();
//        list.add(new HistoryList("asdads","asdasd","asdasd","asdasd"));
//        list.add(new HistoryList("asdads","asdasd","asdasd","asdasd"));
//        list.add(new HistoryList("asdads","asdasd","asdasd","asdasd"));
//        HistoryAdapter adapter = new HistoryAdapter(list);
//        rV_history.setHasFixedSize(true);
//        rV_history.setLayoutManager(new LinearLayoutManager(this));
//        rV_history.setAdapter(adapter);
    }
    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {
        // Initialize main adapter
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());

        // Use for loop
        for (int i = 0; i < arrayList.size(); i++) {
            String title = arrayList.get(i);

            // Initialize bundle
            Bundle bundle = new Bundle();
            bundle.putString("title", title);

            Fragment fragment;

            // Determine which fragment to add
            switch (title) {
                case "Med-check":
                    fragment = new FragmentMedcheck();
                    break;
                case "Smoker":
                    fragment = new FragmentSmoker();
                    break;
                case "Physical":
                    fragment = new FragmentPyhsical();
                    break;
                case "Nutrition":
                    fragment = new FragmentNutrition();
                    break;
                case "Rest":
                    fragment = new FragmentRest();
                    break;
                case "Stress":
                    fragment = new FragmentStress();
                    break;
                default:
                    fragment = new FragmentMedcheck(); // default fragment
                    break;
            }

            // Set argument
            fragment.setArguments(bundle);

            // Add fragment
            adapter.addFragment(fragment, title);
        }

        // Set adapter
        viewPager.setAdapter(adapter);
    }


    private class MainAdapter extends FragmentPagerAdapter {
        // Initialize arrayList
        ArrayList<Fragment> fragmentArrayList= new ArrayList<>();
        ArrayList<String> stringArrayList=new ArrayList<>();

//        int[] imageList={R.drawable.nmbr_one,R.drawable.nmbr_two,R.drawable.nmbr_three};

        // Create constructor
        public void addFragment(Fragment fragment,String s)
        {
            // Add fragment
            fragmentArrayList.add(fragment);
            // Add title
            stringArrayList.add(s);
        }

        public MainAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            // return fragment position
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            // Return fragment array list size
            return fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            // Initialize drawable
//            Drawable drawable= ContextCompat.getDrawable(getApplicationContext()
//                    ,imageList[position]);
//
//            // set bound
//            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),
//                    drawable.getIntrinsicHeight());

            // Initialize spannable image
            SpannableString spannableString=new SpannableString(""+stringArrayList.get(position));

            // Initialize image span
//            ImageSpan imageSpan=new ImageSpan(drawable,ImageSpan.ALIGN_BOTTOM);

            // Set span
//            spannableString.setSpan(imageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // return spannable string
            return spannableString;
        }
    }

}