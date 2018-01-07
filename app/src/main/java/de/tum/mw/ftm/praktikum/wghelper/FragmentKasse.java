package de.tum.mw.ftm.praktikum.wghelper;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fabischramm on 28.12.17.
 */

public class FragmentKasse extends Fragment {

    public FragmentKasse() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_kasse, container, false);
        return view;
    }
}
