package com.r.layoutsearchable;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchableFragment extends Fragment implements SearchView.OnQueryTextListener {


    public SearchableFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    SearchView searchView;
    ArrayList<ModelData> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_searchable, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerView);
        bindList(getModelData());

        searchView = ((MainActivity) getActivity()).getSearchView();
        searchView.setOnQueryTextListener(this);

        super.onViewCreated(view, savedInstanceState);
    }

    private void bindList(ArrayList<ModelData> modelData) {
        Adapter adapter = new Adapter(getContext(), modelData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    public ArrayList<ModelData> getModelData() {
        arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ModelData modelData = new ModelData();
            modelData.setTitle("Title " + i);
            modelData.setDate("Date " + (i + 1));
            arrayList.add(modelData);
        }
        return arrayList;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.equals("")) {
            bindList(getModelData());
        } else {
            ArrayList<ModelData> tempList = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getTitle().toLowerCase().contains((newText).toLowerCase())) {
                    tempList.add(arrayList.get(i));
                }
            }
            bindList(tempList);
        }
        return false;
    }
}
