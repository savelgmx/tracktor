package com.elegion.tracktor.ui.results;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import toothpick.Toothpick;

//отображает диалоговое окно для ввода и редактироания комментария к пройденному треку

public class ResultsDialogFragment extends DialogFragment {
    private static final String KEY_RESULTS_ID="ResultsDialogFragment.KeyResultsId";
    private static long mTrackId; //сохраняем шв екфсл чтобы правильно добвать его к записи


    public static final String TAG_COMMENT_EDITED = "comment";//тэг для передачи результата обратно


    @BindView(R.id.tvTitle)
    protected TextView tvTitle;


    @BindView(R.id.edAdComment)
    protected EditText ibAddCommentText;

    @Inject
    ResultsViewModel mResultsViewModel;




    private DialogInterface.OnClickListener mOnClickListener = (dialogInterface, i) -> {

         mResultsViewModel.updateComment(mTrackId,ibAddCommentText.getText().toString());
        ibAddCommentText.setText(mResultsViewModel.getTrackComment(mTrackId));

        //отправляем результат обратно
        Intent intent = new Intent();
        intent.putExtra(TAG_COMMENT_EDITED, ibAddCommentText.toString());
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

    //    mResultsViewModel.getTrackComment(mTrackId);

        //mResultsViewModel.loadImage(mTrackId);

    };


    public static ResultsDialogFragment newInstance(long id) {

        mTrackId = id;

        Bundle args = new Bundle();
        args.putLong(KEY_RESULTS_ID, id);
        ResultsDialogFragment fragment = new ResultsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Toothpick.inject(this,App.getAppScope());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fr_comment_dialog_fragment, null);
        initUI(view);

        builder.setView(view)
                .setPositiveButton(R.string.btn_save_label, mOnClickListener)
                .setNegativeButton(R.string.btn_cancel_label, null);

        return builder.create();
    }


    private void initUI(View view){
        // Введенный комментарий отображает  если он уже есть
        ButterKnife.bind(this, view);
        tvTitle.setText(mResultsViewModel.getTitleId(mTrackId));
        ibAddCommentText.setText(mResultsViewModel.getTrackComment(mTrackId));
    }



}
