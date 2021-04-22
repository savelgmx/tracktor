package com.elegion.tracktor.ui.results;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.di.ViewModelModule;
import com.elegion.tracktor.util.CustomViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;


public class ResultsFragment extends Fragment {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @Nullable
    @BindView(R.id.ll_error)
    LinearLayout mErrorLayout;

    private ResultsAdapter mResultsAdapter;

    @Inject
    ResultsViewModel mResultsViewModel;//ResultsViewModel должен инжектиться в ResultsFragment

    public ResultsFragment() {
    }

    public static ResultsFragment newInstance() {
        return new ResultsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Toothpick.inject(this, App.getAppScope());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Scope scope = Toothpick.openScope(ResultsFragment.class);
        scope.installModules(new ViewModelModule(this));
        Toothpick.inject(this, scope);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fr_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mResultsAdapter = new ResultsAdapter();//mListener
        mResultsViewModel.getTracks().observe(this, tracks -> mResultsAdapter.submitList(tracks));
        mResultsViewModel.loadTracks();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mResultsAdapter);

        //здесь проверку на IsEmpty и вывод страницы заглушки если список треков еще пуст
        mResultsViewModel.getIsEmpty().observe(this, isEmpty -> {
            if (isEmpty != null && !isEmpty) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mErrorLayout.setVisibility(View.GONE);
            } else {
                mRecyclerView.setVisibility(View.GONE);
                mErrorLayout.setVisibility(View.VISIBLE);
            }
        });

        //End of isEmpty test и вывода заглушки

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_results_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionSortByAscOrDesc) {

            //here increase srt ascending or descending flag

            return true;
        } else if (item.getItemId() == R.id.actionSortByDateOrDuration) {


            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
