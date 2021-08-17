package com.elegion.tracktor.ui.map;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.di.ViewModelModule;
import com.elegion.tracktor.event.StartBtnClickedEvent;
import com.elegion.tracktor.event.StopBtnClickedEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import toothpick.Scope;
import toothpick.Toothpick;

public class CounterFragment extends Fragment {

    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.buttonStart)
    ImageButton buttonStart;
    @BindView(R.id.buttonStop)
    Button buttonStop;

  //  private
    @Inject
    MainViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_counter, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Scope scope = Toothpick.openScope(CounterFragment.class);
        scope.installModules(new ViewModelModule(this));

        Toothpick.inject(this, scope);

        viewModel.getTimeText().observe(this, s -> tvTime.setText(s));
        viewModel.getDistanceText().observe(this, s -> tvDistance.setText(s));

        viewModel.getStartEnabled().observe(this, buttonStart::setEnabled);
        viewModel.getStopEnabled().observe(this, buttonStop::setEnabled);
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.buttonStart)
    void onStartClick() {
        EventBus.getDefault().post(new StartBtnClickedEvent());
        viewModel.switchButtons();
        viewModel.clear();
     //   buttonStart.setImageDrawable(R.drawable.icons_red_start_48);

    }

    @OnClick(R.id.buttonStop)
    void onStopClick() {
        EventBus.getDefault().post(new StopBtnClickedEvent());
        viewModel.switchButtons();
    }
    @Override
    public void onDestroy() {
        Toothpick.closeScope(this);
        super.onDestroy();
    }
}
