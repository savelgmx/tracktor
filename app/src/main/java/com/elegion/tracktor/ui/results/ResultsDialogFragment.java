package com.elegion.tracktor.ui.results;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.elegion.tracktor.R;

//отображает диалоговое окно для ввода и редактироания комментария к пройденному треку

public class ResultsDialogFragment extends DialogFragment {
    private static final String KEY_RESULTS_ID="ResultsDialogFragment.KeyResultsId";


    private DialogInterface.OnClickListener mOnClickListener = (dialogInterface, i) -> {
 /*       mViewModel.apply(etName.getText().toString(),
                etDirector.getText().toString(),
                etYear.getText().toString(),
                etRate.getText().toString(),
                etImageURL.getText().toString()
        );
*/

    };


    public static ResultsDialogFragment newInstance(long id) {

        Bundle args = new Bundle();
        args.putLong(KEY_RESULTS_ID, id);
        ResultsDialogFragment fragment = new ResultsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fr_comment_dialog_fragment, null);

        builder.setView(view)
                .setPositiveButton(R.string.btn_save_label, mOnClickListener)
                .setNegativeButton(R.string.btn_cancel_label, null);

        return builder.create();
    }



}
