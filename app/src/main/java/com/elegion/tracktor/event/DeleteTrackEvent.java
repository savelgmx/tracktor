package com.elegion.tracktor.event;

public class DeleteTrackEvent {
    private long mTrackId;

    public DeleteTrackEvent(long trackId) {
        mTrackId = trackId;
    }

    public long getTrackId() {
        return mTrackId;
    }
}
