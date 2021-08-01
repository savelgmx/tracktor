package com.elegion.tracktor.ui.results;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.di.ViewModelModule;
import com.elegion.tracktor.event.DeleteTrackEvent;
import com.elegion.tracktor.event.EditTrackCommentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import toothpick.Scope;
import toothpick.Toothpick;

import static com.elegion.tracktor.ui.results.ResultsActivity.RESULT_ID;


public class ResultsFragment extends Fragment {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @Nullable
    @BindView(R.id.ll_error)
    LinearLayout mErrorLayout;

    private Menu menu_results_fragment;
    private ResultsAdapter mResultsAdapter;
    private boolean mSortAscending=true; //сортировка по возрастанию/убыванию
    private int mSortByDateDurationDistance=1;//сортировка по дате/продолжительности/расстоянию циклична
    //1-по дате 2-по продолжительности 3-по расстоянию

    private long mTrackId;



    @Inject
    ResultsViewModel mResultsViewModel;//ResultsViewModel должен инжектиться в ResultsFragment


    public ResultsFragment() {
    }

    public static ResultsFragment newInstance() {
        Bundle bundle = new Bundle();
      //  bundle.putLong(RESULT_ID, trackId);
        ResultsFragment fragment = new ResultsFragment();
        fragment.setArguments(bundle);
        return fragment; // return new ResultsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Toothpick.inject(this, App.getAppScope());

        EventBus.getDefault().register(getActivity());
        EventBus.getDefault().register(this);

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


/*


        Toast.makeText(getContext(),"Долгое нажатие по 3 верхним строкам сворачивает/расширяет элемент списка" +
                "\n Короткое-выводит детальную инфу по треку ",Toast.LENGTH_SHORT).show();
*/

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mResultsAdapter = new ResultsAdapter();//mListener
        mResultsViewModel.getTracks().observe(this, tracks -> mResultsAdapter.submitList(tracks));
        //  mResultsViewModel.loadTracks();

        mResultsViewModel.loadSortedByIdTracks(mSortAscending);

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
        menu_results_fragment= menu;
        super.onCreateOptionsMenu(menu, inflater);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if (item.getItemId() == R.id.actionSortByAscOrDesc) {

            mResultsViewModel.loadSortedByIdTracks(mSortAscending);
            if (mSortAscending){
                //сортровка по возрастанию
                Toast.makeText(getContext(),"Сортировка по возрастанию Id",Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getContext(),"Сортировка по убыванию Id",Toast.LENGTH_SHORT).show();

            }
            mSortAscending = !mSortAscending;

            return true;
        } else if (item.getItemId() == R.id.actionSortByDateOrDuration) {

            mResultsViewModel.loadSortedByDateDurationDistance(mSortByDateDurationDistance);

            //---show Toast about sorting order
            switch (mSortByDateDurationDistance){
                case 1:  //sort by date
                    Toast.makeText(getContext(),"Сортировка по возрастанию Даты/Времени",Toast.LENGTH_SHORT).show();
                    break;
                case 2: //sort by duration
                    Toast.makeText(getContext(),"Сортировка по возрастанию Длительности(duration)",Toast.LENGTH_SHORT).show();                    break;
                case 3://sort by distance;
                    Toast.makeText(getContext(),"Сортировка по пройденному Расстоянию(Distance)",Toast.LENGTH_SHORT).show();
                    break;
            }

            //end show


            mSortByDateDurationDistance=mSortByDateDurationDistance+1;//сортировка по дате/продолжительности/расстоянию циклична
            //1-по дате 2-по продолжительности 3-по расстоянию
            if (mSortByDateDurationDistance>3) mSortByDateDurationDistance=1; //Переключение односторонее и цикличное

            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().unregister(getActivity());
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnDeleteTrackEvent(DeleteTrackEvent deleteTrackEvent) {
        long trackId = deleteTrackEvent.getTrackId();
        mResultsViewModel.deleteTrack(trackId);
        mResultsViewModel.loadSortedByDateDurationDistance(mSortByDateDurationDistance);
        //mResultsAdapter.submitList(mResultsViewModel.stripRealmTrack());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEditTrackCommentEvent(EditTrackCommentEvent editTrackCommentEvent){

        mTrackId=editTrackCommentEvent.getTrackId();
         Track track = mResultsViewModel.getTrack(mTrackId);
        String trackComment=mResultsViewModel.getTrackComment(mTrackId);
        //далее AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fr_comment_dialog_fragment, null);

        final TextView tvTitle = view.findViewById(R.id.tvTitle);
        final EditText etComment = view.findViewById(R.id.edAdComment);
        //здесь меняем заголовок
        tvTitle.setText(mResultsViewModel.getTitleId(mTrackId));
        etComment.setText(trackComment);



        builder.setView(view)
                .setPositiveButton(R.string.btn_save_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mResultsViewModel.updateComment(mTrackId,etComment.getText().toString());
                        track.setComment(etComment.getText().toString());
                      //  etComment.setText(mResultsViewModel.getTrackComment(mTrackId));
                        editTrackCommentEvent.getCommentText().setText(track.getComment());

                    }
                })
                .setNegativeButton(R.string.btn_cancel_label, null);
         builder.create().show();
    }
}
