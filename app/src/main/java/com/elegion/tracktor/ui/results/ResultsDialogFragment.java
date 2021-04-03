package com.elegion.tracktor.ui.results;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.arch.lifecycle.LifecycleOwner;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

    @BindView(R.id.tvTitle)
    protected TextView tvTitle;


    @BindView(R.id.edAdComment)
    protected EditText ibAddCommentText;

    @Inject
    DialogFragmentViewModel mDialogFragmentViewModel;




    private DialogInterface.OnClickListener mOnClickListener = (dialogInterface, i) -> {

        Log.d("ResultsDetailFragment","OnClickListener with mTrackId= "+String.valueOf(mTrackId));
        Log.d("ResultsDetailFragment","OnClickListener with Comment= "+ ibAddCommentText.getText().toString());
        Log.d("ResultsDetailFragment","mDialogFragmentViewModel="+String.valueOf(mDialogFragmentViewModel));

         mDialogFragmentViewModel.updateComment(mTrackId,ibAddCommentText.getText().toString());



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
        //ButterKnife.bind(this, view);
        initUI(view);

        builder.setView(view)
                .setPositiveButton(R.string.btn_save_label, mOnClickListener)
                .setNegativeButton(R.string.btn_cancel_label, null);

        return builder.create();
    }


    private void initUI(View view){
        // Введенный комментарий отображает  если он уже есть
        ButterKnife.bind(this, view);
        tvTitle.setText(mDialogFragmentViewModel.getTitleId(mTrackId));
      //  mDialogFragmentViewModel.getComment().observe( this, str->ibAddCommentText.setText(str));


    }



}
