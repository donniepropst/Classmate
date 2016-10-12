package com.tributetech.classmate.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tributetech.classmate.R;
import com.tributetech.classmate.adapters.ClassAdapter;
import com.tributetech.classmate.data.DatabaseHelper;


public class ClassListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView emptyView;

    public ClassListFragment() {
        // Required empty public constructor
    }


    public static ClassListFragment newInstance() {
        ClassListFragment fragment = new ClassListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_list, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseHelper dh = new DatabaseHelper(view.getContext());

        if(!dh.getAllClasses().isEmpty()){
            mRecyclerView = (RecyclerView) view.findViewById(R.id.class_recyclerlist);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(view.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new ClassAdapter(dh.getAllClasses(), this);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            emptyView = (TextView)view.findViewById(R.id.fragment_class_empty_text);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("You haven't added any classes yet.\nPress the + button to get started.");
            //view.setBackgroundColor(Color.parseColor("#B0BEC5"));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
