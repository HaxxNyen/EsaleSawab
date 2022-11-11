package com.nyen.User.esalesawab.UI.Fund;



import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.nyen.User.esalesawab.Fragments.LocalStore.LocalStore;
import com.nyen.User.esalesawab.Fragments.LocalStore.PurchaseHistoryLocal;
import com.nyen.User.esalesawab.R;



public class Store extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter fragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager2 = (ViewPager2) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        toolbar.setTitle("Store");
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);









        tabLayout.addTab(tabLayout.newTab().setText("Local Store"));
        tabLayout.addTab(tabLayout.newTab().setText("Purchase History"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                fragmentAdapter.createFragment(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                Toast.makeText(context, "Tab Unselect "+ tab.getPosition(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }

        });
    }

    static class FragmentAdapter extends FragmentStateAdapter {


        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position){
                case 1:
                    return new PurchaseHistoryLocal();



            }
            return new LocalStore();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}