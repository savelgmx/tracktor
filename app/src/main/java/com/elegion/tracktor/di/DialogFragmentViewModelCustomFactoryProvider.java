package com.elegion.tracktor.di;

import com.elegion.tracktor.util.DialogFragmentViewModelCustomFactory;

import javax.inject.Inject;
import javax.inject.Provider;

/*
public class FilmDetailViewModelCustomFactoryProvider implements Provider<FilmDetailViewModelCustomFactory> {

    protected FRepository mRepository;
    private Gson mGson;
    private Long mFilmId;

    @Inject
    public FilmDetailViewModelCustomFactoryProvider(FRepository mRepository, Gson gson, @Named("FilmId") Long filmId) {
        this.mRepository = mRepository;
        this.mGson = gson;
        this.mFilmId = filmId;
    }

    @Override
    public FilmDetailViewModelCustomFactory get() {
        return new FilmDetailViewModelCustomFactory(mRepository, mGson, mFilmId);
    }
}

 */

public class DialogFragmentViewModelCustomFactoryProvider implements Provider<DialogFragmentViewModelCustomFactory> {
    private Long mTrackId;
    @Inject
    public DialogFragmentViewModelCustomFactoryProvider(Long trackId){
        this.mTrackId = trackId;

    }
    @Override
    public DialogFragmentViewModelCustomFactory get() {
        return new DialogFragmentViewModelCustomFactory();//mTrackId
    }
}
