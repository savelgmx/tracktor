package com.elegion.tracktor.ui.results;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.elegion.tracktor.common.ShowResultDetailEvent;
import com.elegion.tracktor.common.SingleFragmentActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * `
 */
public class ResultsActivity extends SingleFragmentActivity  {
    public static final String RESULT_ID = "RESULT_ID";
    public static final long LIST_ID = -1L;

    public static void start(@NonNull Context context, long resultId) {
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(RESULT_ID, resultId);

        context.startActivity(intent);
    }

    @Override
    protected Fragment getFragment() {
        long resultId = getIntent().getLongExtra(RESULT_ID, LIST_ID);

        if (resultId != LIST_ID)
            return ResultsDetailsFragment.newInstance(resultId);
        else
            return ResultsFragment.newInstance();
    }
    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showResultDetailsFragment(ShowResultDetailEvent event) {
        changeFragment(ResultsDetailsFragment.newInstance(event.id));
    }
}
