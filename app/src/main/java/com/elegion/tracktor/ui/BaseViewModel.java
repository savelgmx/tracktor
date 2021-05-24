package com.elegion.tracktor.ui;

import android.arch.lifecycle.ViewModel;

public abstract class BaseViewModel extends ViewModel {
    abstract protected void updateFromRepository();
}
