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
import android.util.Log;
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
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.di.ViewModelModule;
import com.elegion.tracktor.util.CustomViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmResults;
import io.realm.Sort;
import toothpick.Scope;
import toothpick.Toothpick;


public class ResultsFragment extends Fragment {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @Nullable
    @BindView(R.id.ll_error)
    LinearLayout mErrorLayout;

    private ResultsAdapter mResultsAdapter;
    private boolean mSortAscending=true;
    private RealmAsyncTask asyncTransaction;


    @Inject
    ResultsViewModel mResultsViewModel;//ResultsViewModel должен инжектиться в ResultsFragment
    private Realm realm;


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
           // mResultsViewModel.loadSortedByIdTracks(mSortAscending);

            getRealmSortedTracks(mSortAscending);
            mSortAscending = !mSortAscending;

            Log.d("ResultsFragment" ," mSortAscending = "+String.valueOf(mSortAscending));



            return true;
        } else if (item.getItemId() == R.id.actionSortByDateOrDuration) {

            Log.d("ResultsFragment" ,"Sort order DateOrDuration pressed");

            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();



    }



    private List<Track> getRealmSortedTracks(boolean ascending){

        List<Track> tracks = new ArrayList<>();

        Realm realm = Realm.getDefaultInstance();


        cancelAsyncTransaction();
        asyncTransaction = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {



                RealmResults<Track> sortedTracks = realm.where(Track.class).findAll();
                if (ascending) {
                    sortedTracks.sort("distance", Sort.ASCENDING);
                }else{
                    sortedTracks.sort("distance",Sort.DESCENDING);
                }
                Log.d("ResultsFragment"," sortedTracks size= "+String.valueOf(sortedTracks.size()));

                for (int i=1;i< sortedTracks.size(); i++) {
                    tracks.add(sortedTracks.get(i) ) ;
                    Log.d("ResultsFragment"," Tracks(i)= "+String.valueOf(tracks.get(i)));


                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        }, new Realm.Transaction.OnError() {

            @Override
            public void onError(Throwable e) {
            }
        });

        return tracks;

    }

    private void cancelAsyncTransaction() {
        if (asyncTransaction != null && !asyncTransaction.isCancelled()) {
            asyncTransaction.cancel();
            asyncTransaction = null;
        }
    }



}
