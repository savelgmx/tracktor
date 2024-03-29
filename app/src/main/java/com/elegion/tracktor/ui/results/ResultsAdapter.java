package com.elegion.tracktor.ui.results;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.common.ShowResultDetailEvent;
import com.elegion.tracktor.data.model.Track;
import org.greenrobot.eventbus.EventBus;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import toothpick.Toothpick;


public class ResultsAdapter extends ListAdapter<Track, ResultHolder> {
    
    
     @Inject
    ResultsFragment mResultsFragment;
    
    
    
    private static final DiffUtil.ItemCallback<Track> DIFF_CALLBACK = new DiffUtil.ItemCallback<Track>() {
        
        @Override
        public boolean areItemsTheSame(Track oldItem, Track newItem) {
            return oldItem.getId() == newItem.getId();
        }
        
        @Override
        public boolean areContentsTheSame(Track oldItem, Track newItem) {
            return oldItem.equals(newItem);
        }
    };
    
    
    
    ResultsAdapter() {
        super(DIFF_CALLBACK);
        Toothpick.inject(this, App.getAppScope());
        
    }
    
    
    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_track, parent, false);
        return new ResultHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {
        holder.bind(getItem(position));
        holder.setOnClickListener(id ->{
            EventBus.getDefault().post(new ShowResultDetailEvent(id));
            notifyDataSetChanged();
            
        });
/*
        holder.setOnLongClickListener(id->{
            
            boolean isExpanded = getItem(position).isExpanded();
            holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            
        });
*/

        
    }
    interface OnItemClickListener {
        void onItemClick (long id);
    }
    
    public interface OnItemLongClickListener {
        void OnItemLongClick(long id);
    }
}
