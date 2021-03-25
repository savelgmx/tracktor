package com.elegion.tracktor.di;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.ViewModelStoreOwner;

import com.elegion.tracktor.ui.results.DialogFragmentViewModel;
import com.elegion.tracktor.util.CustomViewModelFactory;
import com.elegion.tracktor.util.DialogFragmentViewModelCustomFactory;

import toothpick.config.Module;

public class DialogFragmentViewModelModule extends Module {

    private DialogFragmentViewModel provideDialogFragmentViewModel(ViewModelStoreOwner fragment){
        DialogFragmentViewModelCustomFactory factory = new DialogFragmentViewModelCustomFactory();
        return ViewModelProviders.of((android.support.v4.app.DialogFragment) fragment, factory).get(DialogFragmentViewModel.class);
    }

}
