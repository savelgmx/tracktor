package com.elegion.tracktor.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.support.annotation.NonNull;

import com.elegion.tracktor.ui.results.DialogFragmentViewModel;

/*
public class FilmDetailViewModelCustomFactory implements ViewModelProvider.Factory {
    private FRepository mRepository;
    private Gson mGson;
    private Long mFilmId;

    public FilmDetailViewModelCustomFactory(FRepository repository, Gson gson, Long filmId) {

        this.mRepository = repository;
        this.mGson = gson;
        this.mFilmId = filmId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FilmDetailViewModel(mRepository, mGson, mFilmId);

    }
}

 */
public class DialogFragmentViewModelCustomFactory implements ViewModelProvider.Factory {

    private Long mTrackId;

    public DialogFragmentViewModelCustomFactory(Long trackId){
        this.mTrackId = trackId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DialogFragmentViewModel(mTrackId);
    }
}
