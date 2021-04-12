package com.elegion.tracktor.ui.results;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.di.ViewModelModule;
import com.elegion.tracktor.ui.preferences.ReadUserPreferences;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

import static com.elegion.tracktor.ui.results.ResultsActivity.RESULT_ID;

/**
 * @author Azret Magometov
 * https://apptractor.ru/info/github/mvp-sample.html
 */
public class ResultsDetailsFragment extends Fragment implements ResultsDialogFragment.ResultsDialogListener {


    @BindView(R.id.tvTime)
    TextView mTimeText;
    @BindView(R.id.tvDistance)
    TextView mDistanceText;
    @BindView(R.id.ivScreenshot)
    ImageView mScreenshotImage;
    @BindView(R.id.tvAverageSpeed)
    TextView mAverageSpeedText;
    @BindView(R.id.tvSpentCalories)
    TextView mSpentCalories;
    @BindView(R.id.tvDate)
    TextView mDateText;
    @BindView(R.id.tvComment)
    TextView mComment;


    @Inject
    ResultsViewModel mResultsViewModel;//ResultsViewModel должен инжектиться в ResultsFragment

    private ImageButton mIbAddComment;
    private RadioGroup mRadioGroup;
    private int CheckedRadioButtonIndex;

    private Bitmap mImage;
    private RealmRepository mRealmRepository;
    private long mTrackId;

    private static final int REQUEST_COMMENT = 1; //устанавливает requestCode;
    private static final int REQUEST_ANOTHER_ONE = 2;

    private static ResultsDetailsFragment mResultsDetailsFragment;


    public static ResultsDetailsFragment newInstance(long trackId) {
        Bundle bundle = new Bundle();
        bundle.putLong(RESULT_ID, trackId);
        ResultsDetailsFragment fragment = new ResultsDetailsFragment();
        fragment.setArguments(bundle);
        mResultsDetailsFragment = fragment;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Scope scope = Toothpick.openScope(ResultsDetailsFragment.class);
        scope.installModules(new ViewModelModule(this));
        Toothpick.inject(this, scope);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fr_result_detail, container, false);
        mRadioGroup = view.findViewById(R.id.radiogroup);
        mRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        SetSavedRadioButtonChecked(CheckedRadioButtonIndex);

        mIbAddComment = view.findViewById(R.id.ibAddComment);
        mIbAddComment.setOnClickListener(mOnClickListener);


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mResultsViewModel.calculateSpentCalories(CheckedRadioButtonIndex);


        mTrackId = getArguments().getLong(RESULT_ID, 0);

        mResultsViewModel.getStringDate(mTrackId);

        mResultsViewModel.loadImage(mTrackId);
        mResultsViewModel.getImage().observe(this, image -> mScreenshotImage.setImageBitmap(image));

        mResultsViewModel.getTime().observe(this, time -> mTimeText.setText(time));
        mResultsViewModel.getDistance().observe(this, distance -> mDistanceText.setText(distance));
        mResultsViewModel.getAverageSpeed().observe(this, averageSpeed -> mAverageSpeedText.setText(averageSpeed));

        mResultsViewModel.getSpentCalories().observe(this, spentCalories -> mSpentCalories.setText(spentCalories));

        mResultsViewModel.getDate().observe(this, date -> mDateText.setText(date));

        mResultsViewModel.getComment().observe(this,comment->mComment.setText(comment));



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionShare) {
            String path = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), mImage, "Мой маршрут", null);
            //TODO check null value of the path
            Uri uri = Uri.parse(path);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_TEXT, "Время: " + mTimeText.getText() + "\nРасстояние: " + mDistanceText.getText());
            startActivity(Intent.createChooser(intent, "Результаты маршрута"));
            return true;
        } else if (item.getItemId() == R.id.actionDelete) {
            if (mRealmRepository.deleteItem(mTrackId)) {
                getActivity().onBackPressed();
            }
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    /* устанавливает предпочитаемый RadioButton согласно сохраненному Id
     */

    private void SetSavedRadioButtonChecked(int savedRadioIndex) {
        RadioButton savedCheckedRadioButton = (RadioButton) mRadioGroup
                .getChildAt(savedRadioIndex);
        savedCheckedRadioButton.setChecked(true);
    }


    /**
     * Listener для отслеживания выбора RadioButton
     * <p>
     * checkedIndexId: 0 Велосипед
     * checkedIndexId: 1 Ходьба
     * checkedIndexId: 2 Бег
     */
    private final RadioGroup.OnCheckedChangeListener onCheckedChangeListener =
            new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    RadioButton checkedRadioButton = (RadioButton) mRadioGroup
                            .findViewById(checkedId);
                    int checkedIndex = mRadioGroup.indexOfChild(checkedRadioButton);
                    ReadUserPreferences.saveKindOfActivityId(checkedIndex, getContext());

                    mResultsViewModel.calculateSpentCalories(checkedIndex);//пересчет калорий при выборе RadioButton


                }
            };



    private void addComments(){
        //диалог с предложением ввести комментарий.
       // FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        //https://habr.com/ru/post/259805/
       // https://stackoverflow.com/questions/10905312/receive-result-from-dialogfragment

        //https://android-developers.googleblog.com/2012/05/using-dialogfragments.html

        ResultsDialogFragment resultsDialogFragment = ResultsDialogFragment.newInstance(mTrackId);
        resultsDialogFragment.show(getActivity().getSupportFragmentManager(), "resultsDialogFragment");


        //  mResultsViewModel.getTrackComment(mTrackId);


    }

 /*   public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_COMMENT:
                    int weight = data.getIntExtra(ResultsDialogFragment.TAG_COMMENT_EDITED, -1);
                    //используем полученные результаты
                    //...
                    break;
                case REQUEST_ANOTHER_ONE:
                    //...
                    break;
                //обработка других requestCode
            }
            //updateUI();
        }
    }
*/

    private View.OnClickListener mOnClickListener= view-> addComments();

    @Override
    public void onFinishEditDialog(String inputText) {
        //здесь мы должны ловить строку из диалогового окна ResultsDialogfragment


    }
}

