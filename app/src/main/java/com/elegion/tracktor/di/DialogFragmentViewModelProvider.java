package com.elegion.tracktor.di;

import android.app.DialogFragment;

import com.elegion.tracktor.ui.results.DialogFragmentViewModel;
import com.elegion.tracktor.util.DialogFragmentViewModelCustomFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/*
class FilmDetailViewModelProvider implements Provider<FilmDetailViewModel> {
    @Inject
    protected DialogFragment mFragment;
    @Inject
    protected FilmDetailViewModelCustomFactory mFactory;
    @Inject
    @Named("FilmId")
    Long mFilmId;


    @Override
    public FilmDetailViewModel get() {
        return ViewModelProviders.of(mFragment, mFactory).get(String.valueOf(mFilmId),FilmDetailViewModel.class);
    }

 */

public class DialogFragmentViewModelProvider implements Provider<DialogFragmentViewModel> {
    @Inject
    protected DialogFragment mFragment;
    @Inject
    protected DialogFragmentViewModelCustomFactory mFactory;
    @Inject
    @Named("TrackId")
    Long mTrackId;

    @Override
    public DialogFragmentViewModel get() {

      //  return  ViewModelProviders.of(mFragment,mFactory).get(String.valueOf(mTrackId),DialogFragmentViewModel.class);
        return null;
    }
}
