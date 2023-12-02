package Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mingle.fragments.calls_fragment;
import com.example.mingle.fragments.chats_fragment;
import com.example.mingle.fragments.status_fragment;

@SuppressWarnings("ALL")
public class Tabadapter extends FragmentStateAdapter {
    public Tabadapter(@NonNull FragmentActivity fr) {
        super(fr);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new chats_fragment();
            case 1: return new status_fragment();
            case 2: return new calls_fragment();
            default: return new chats_fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }



}