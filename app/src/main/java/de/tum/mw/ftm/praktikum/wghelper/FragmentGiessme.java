package de.tum.mw.ftm.praktikum.wghelper;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fabischramm on 07.01.18.
 */

public class FragmentGiessme extends Fragment {
    public FragmentGiessme() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_giessme, container, false);
        return view;
    }
}


