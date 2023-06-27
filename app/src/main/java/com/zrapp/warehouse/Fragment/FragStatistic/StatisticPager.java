package com.zrapp.warehouse.Fragment.FragStatistic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StatisticPager extends FragmentStateAdapter {
    public StatisticPager(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragStatisticQuantity();
            case 1:
                return new FragStatisticMoney();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
