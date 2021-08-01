package com.elegion.tracktor.event;

import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

public class EditTrackCommentEvent {
    private final long mTrackId;
    private final TextView mCommentText;

    public EditTrackCommentEvent(long trackId, MultiAutoCompleteTextView commentText) {
        mTrackId = trackId;
        mCommentText = commentText;
    }

    public long getTrackId() {
        return mTrackId;
    }

    public TextView getCommentText() {
        return mCommentText;
    }
}
