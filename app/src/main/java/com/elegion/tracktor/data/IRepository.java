package com.elegion.tracktor.data;

import java.util.List;

/**
 * @author Azret Magometov
 */
public interface IRepository<Track> {

    Track getItem(long id);

    List<Track> getAll();

    long insertItem(Track t);

    boolean deleteItem(long id);

    long updateItem(Track t);

    List<Track> getRealmSortedTracks(boolean ascending);

}
