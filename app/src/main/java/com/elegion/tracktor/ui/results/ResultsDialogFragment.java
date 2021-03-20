package com.elegion.tracktor.ui.results;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.elegion.tracktor.R;
import com.elegion.tracktor.di.ViewModelModule;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

//отображает диалоговое окно для ввода и редактироания комментария к пройденному треку

public class ResultsDialogFragment extends DialogFragment {
    private static final String KEY_RESULTS_ID="ResultsDialogFragment.KeyResultsId";
    private static long mTrackId; //сохраняем шв екфсл чтобы правильно добвать его к записи

    @BindView(R.id.edAdComment)
    protected EditText ibAddCommentText;

    @Inject
    DialogFragmentViewModel mDialogFragmentViewModel;
/*
    @Inject
    ResultsViewModel mResultsViewModel;//ResultsViewModel должен инжектиться в ResultsFragment
*/

/*
    @Inject
    RealmRepository mRealmRepository;
*/



    private DialogInterface.OnClickListener mOnClickListener = (dialogInterface, i) -> {

        Log.d("ResultsDetailFragment","OnClickListener with mTrackId= "+String.valueOf(mTrackId));
        Log.d("ResultsDetailFragment","OnClickListener with Comment= "+ ibAddCommentText.getText().toString());

        Log.d("ResultsDetailFragment","mDialogFragmentViewModel="+String.valueOf(mDialogFragmentViewModel));

      //  mResultsViewModel.updateComment(mTrackId, ibAddCommentText.getText().toString());
    //    mDialogFragmentViewModel.updateComment(mTrackId,ibAddCommentText.getText().toString());



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

      //  toothpickInject();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fr_comment_dialog_fragment, null);
        ButterKnife.bind(this, view);

        builder.setView(view)
                .setPositiveButton(R.string.btn_save_label, mOnClickListener)
                .setNegativeButton(R.string.btn_cancel_label, null);

        return builder.create();
    }

     private void toothpickInject() {
        long id = -1;
        if (getArguments() != null) {
            id = getArguments().getLong(KEY_RESULTS_ID, -1);
        }
        Scope scope = Toothpick.openScope(ResultsDialogFragment.class);
        scope.installModules(new ViewModelModule((ViewModelStoreOwner) this));
        Toothpick.inject(this, scope);
    }



}
