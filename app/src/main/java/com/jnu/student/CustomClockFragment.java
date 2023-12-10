package com.jnu.student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnu.student.view.CustomClockView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomClockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomClockFragment extends Fragment {
    private CustomClockView customClockView;
    private Handler handler = new Handler();

    public CustomClockFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CustomClockFragment newInstance() {
        CustomClockFragment fragment = new CustomClockFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_clock, container, false);
        customClockView = view.findViewById(R.id.clockView);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_clock, container, false);
    }
    @Override
    public void onResume() {
        super.onResume();
        startClockUpdate();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopClockUpdate();
    }

    private void startClockUpdate() {
        handler.postDelayed(clockUpdater, 1000); // 1秒钟更新一次
    }

    private void stopClockUpdate() {
        handler.removeCallbacks(clockUpdater);
    }

    private Runnable clockUpdater = new Runnable() {
        @Override
        public void run() {
            // 更新钟表指针的位置
            customClockView.invalidate();
            handler.postDelayed(this, 1000); // 1秒钟更新一次
        }
    };
}