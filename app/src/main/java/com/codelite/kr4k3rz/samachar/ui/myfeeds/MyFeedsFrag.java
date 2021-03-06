package com.codelite.kr4k3rz.samachar.ui.myfeeds;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codelite.kr4k3rz.samachar.R;
import com.codelite.kr4k3rz.samachar.model.Subscribe;
import com.codelite.kr4k3rz.samachar.model.search.EntriesItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyFeedsFrag extends Fragment {
    private List<Subscribe> subscribeList;
    private TextView textView;
    private RecyclerView recyclerView;


    public MyFeedsFrag() {
        subscribeList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_my_feeds, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_myFeeds);
        textView = (TextView) rootView.findViewById(R.id.textVIew_info);
        FloatingActionButton floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab_search);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        Log.i("TAG", "MyFeedFrag");
        Log.i(MyFeedsFrag.class.getSimpleName(), "In Oncreate");
        if (Paper.book().exist("SubscribedList")) {
            List<Subscribe> subscribes;
            subscribes = Paper.book().read("SubscribedList");
            recyclerView.setAdapter(new SearchQueryAdapter(subscribes));
        } else {
            textView.setVisibility(View.VISIBLE);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                if (Paper.book().exist("SubscribedList")) {
                    subscribeList = Paper.book().read("SubscribedList");
                }
                Subscribe subscribe;
                subscribe = (Subscribe) data.getSerializableExtra("SubscribedItem");
                subscribeList.add(subscribe);
                SearchQueryAdapter adapter = new SearchQueryAdapter(subscribeList);
                textView.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
                Paper.book().write("SubscribedList", subscribeList);

            }
        }
    }

    class SearchQueryAdapter extends RecyclerView.Adapter<SearchQueryAdapter.MyViewHolder> {
        final List<Subscribe> subscribeList;

        SearchQueryAdapter(List<Subscribe> subscribeList) {
            this.subscribeList = subscribeList;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final Subscribe subscribe;
            subscribe = subscribeList.get(position);
            final EntriesItem entriesItem = subscribe.getEntriesItem();
            holder.textView_title.setText(Html.fromHtml(entriesItem.getTitle()));
            holder.textView_code_snippet.setText(Html.fromHtml(entriesItem.getContentSnippet()));
            holder.button_subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), MySubscribedActivity.class);
                    intent.putExtra("EXTRA_DATA", (Serializable) subscribeList);
                    intent.putExtra("EXTRA_POSITION", holder.getAdapterPosition());
                    startActivity(intent);
                }
            });
            holder.textView_link.setText(entriesItem.getLink());

        }

        @Override
        public int getItemCount() {
            return subscribeList.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            final TextView textView_title;
            final TextView textView_code_snippet;
            final TextView textView_link;
            final LinearLayout button_subscribe;

            MyViewHolder(View itemView) {
                super(itemView);
                textView_title = (TextView) itemView.findViewById(R.id.query_title);
                textView_code_snippet = (TextView) itemView.findViewById(R.id.query_codeSnippet);
                button_subscribe = (LinearLayout) itemView.findViewById(R.id.ll_search);
                textView_link = (TextView) itemView.findViewById(R.id.query_link);
            }
        }
    }


}
