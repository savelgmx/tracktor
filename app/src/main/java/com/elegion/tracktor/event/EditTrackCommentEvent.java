package com.elegion.tracktor.event;

import android.widget.TextView;

public class EditTrackCommentEvent {
    private final long mTrackId;
    private final String mCommentText;

    public EditTrackCommentEvent(long trackId, String commentText) {
        mTrackId = trackId;
        mCommentText = commentText;
    }

    public long getTrackId() {
        return mTrackId;
    }

    public String getCommentText() {
        return mCommentText;
    }
}
