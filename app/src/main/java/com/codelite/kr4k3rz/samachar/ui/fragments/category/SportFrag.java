package com.codelite.kr4k3rz.samachar.ui.fragments.category;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.model.WhichCategoryNP;
import com.codelite.kr4k3rz.samachar.ui.adapter.RvAdapter;
import com.codelite.kr4k3rz.samachar.ui.adapter.SimpleDividerItemDecoration;
import com.codelite.kr4k3rz.samachar.util.CacheLang;

import java.util.List;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SportFrag extends Fragment {
    private static final String TAG = SportFrag.class.getSimpleName();
    private static final String CACHE_NAME = WhichCategoryNP.SPORT.getSecondName();
    /*for loading at postRefresh at first lunch*/
    private View rootView;
    private RecyclerView recyclerView;


    public SportFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sport, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_sport);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        List<Entry> list = Paper.book().read(CacheLang.findLang(CACHE_NAME));
        recyclerView.setAdapter(new RvAdapter(getContext(), list));
        return rootView;
    }



}
