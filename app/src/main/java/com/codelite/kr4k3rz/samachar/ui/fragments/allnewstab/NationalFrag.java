package com.codelite.kr4k3rz.samachar.ui.fragments.allnewstab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.handler.AsyncHelper;
import com.codelite.kr4k3rz.samachar.model.Entry;
import com.codelite.kr4k3rz.samachar.model.FeedLists;
import com.codelite.kr4k3rz.samachar.model.WhichCategoryNP;
import com.codelite.kr4k3rz.samachar.ui.adapter.RvAdapter;
import com.codelite.kr4k3rz.samachar.ui.adapter.SimpleDividerItemDecoration;
import com.codelite.kr4k3rz.samachar.util.CacheLang;
import com.codelite.kr4k3rz.samachar.util.CheckInternet;
import com.codelite.kr4k3rz.samachar.util.SnackMsg;

import java.util.List;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class NationalFrag extends Fragment {
    private static final String TAG = NationalFrag.class.getSimpleName();
    private static final String CACHE_NAME = WhichCategoryNP.NATIONAL.getSecondName();
    /*for loading at postRefresh at first lunch*/
    private View rootView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public NationalFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_national, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_headlines);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout_headlines);
        initSwipeRefresh();
        return rootView;
    }

    private void initSwipeRefresh() {

        mSwipeRefreshLayout.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         Log.i(TAG, "SwipeRefresh post()");
                                         if (!Paper.book().exist(CacheLang.findLang(CACHE_NAME)) && CheckInternet.isNetworkAvailable(getContext())) {
                                             String[] rss = FeedLists.getFeedListCached(0);
                                             new AsyncHelper(rootView, mSwipeRefreshLayout, getContext(), CacheLang.findLang(CACHE_NAME), recyclerView, WhichCategoryNP.NATIONAL.ordinal()).execute(rss);

                                         } else {
                                             mSwipeRefreshLayout.setRefreshing(true);
                                             List<Entry> list = Paper.book().read(CacheLang.findLang(CACHE_NAME));
                                             if (!Paper.book().exist(CacheLang.findLang(CACHE_NAME))) {
                                                 SnackMsg.showMsgShort(rootView, "connect to internet");
                                             } else
                                                 recyclerView.setAdapter(new RvAdapter(getContext(), list));
                                             mSwipeRefreshLayout.setRefreshing(false);
                                         }

                                     }
                                 }
        );

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "SwipeRefresh()");
                if (CheckInternet.isNetworkAvailable(getContext())) {
                    String[] rss_new = FeedLists.getFeedListLatest(0);
                    new AsyncHelper(rootView, mSwipeRefreshLayout, getContext(), CacheLang.findLang(CACHE_NAME), recyclerView, WhichCategoryNP.NATIONAL.ordinal()).execute(rss_new);
                } else {
                    SnackMsg.showMsgShort(rootView, "couldn't connect to internet");
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

}
