package com.elegion.tracktor.di;

import android.app.DialogFragment;

import com.elegion.tracktor.ui.results.DialogFragmentViewModel;
import com.elegion.tracktor.util.DialogFragmentViewModelCustomFactory;

import toothpick.config.Module;

public class DialogFragmentModule extends Module {

    private DialogFragment mFragment;
    private Long mTrackId;

    public DialogFragmentModule(DialogFragment fragment,long trackId){
        this.mFragment = fragment;
        this.mTrackId= trackId;


            bind(DialogFragment.class).toInstance(mFragment);
            bind(DialogFragmentViewModel.class).toProvider(DialogFragmentViewModelProvider.class);
            bind(Long.class).withName("mTrackId").toInstance(mTrackId);
          //  bind(DialogFragmentViewModelCustomFactory.class).toProvider(DialogFragmentViewModelCustomFactory.class);
        }

    }


