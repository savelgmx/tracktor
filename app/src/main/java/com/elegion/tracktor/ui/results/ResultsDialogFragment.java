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

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.di.DialogFragmentModule;
import com.elegion.tracktor.di.DialogFragmentViewModelModule;
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

        //TODO remove noFactory Found Exeption
        // at com.elegion.tracktor.ui.results.ResultsDialogFragment.onCreateDialog(ResultsDialogFragment.java:78)
/*
        Scope scope = Toothpick.openScope(ResultsDialogFragment.class);
        scope.installModules(new DialogFragmentViewModelModule());
        Toothpick.inject(this, scope);
*/
        Toothpick.inject(this,App.getAppScope());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fr_comment_dialog_fragment, null);
        ButterKnife.bind(this, view);

        builder.setView(view)
                .setPositiveButton(R.string.btn_save_label, mOnClickListener)
                .setNegativeButton(R.string.btn_cancel_label, null);

        return builder.create();
    }




}
