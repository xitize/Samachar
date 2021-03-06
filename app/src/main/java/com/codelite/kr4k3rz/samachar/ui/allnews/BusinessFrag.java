package com.codelite.kr4k3rz.samachar.ui.allnews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.WhichCategoryNP;
import com.codelite.kr4k3rz.samachar.model.feed.EntriesItem;
import com.codelite.kr4k3rz.samachar.util.CacheLang;

import java.util.List;

import io.paperdb.Paper;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessFrag extends Fragment {

    private static final String CACHE_NAME = WhichCategoryNP.BUSINESS.getSecondName();

    public BusinessFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_business, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_business);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        List<EntriesItem> list = Paper.book().read(CacheLang.findLang(CACHE_NAME));
        recyclerView.setAdapter(new RvAdapter(getContext(), list));
        return rootView;
    }


}
