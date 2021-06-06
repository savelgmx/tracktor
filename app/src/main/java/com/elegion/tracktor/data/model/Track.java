package com.elegion.tracktor.data.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Azret Magometov
 */
public class Track extends RealmObject {

    @PrimaryKey
    private long id;

    private Date date;
    private long duration;
    private Double distance;
    private String imageBase64;
    private Double averagespeed;
    private String comment;
    private int action; //вид деятельности 0-Велосипед,1-Ходьба,2-Бег
    private boolean expanded;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getAverageSpeed(){
        return averagespeed;
    }
    public void setAverageSpeed(double distance,long duration ){
        double average_speed;
        if ((Integer.valueOf((int) duration)>0)&(distance>0)){
            average_speed =(Double.valueOf(distance)/Double.valueOf(duration));
        }else {average_speed=0.0;}
        this.averagespeed=average_speed;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setComment (String newComment){this.comment = newComment;}
    public String getComment(){return comment;}

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }


    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }



    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", date=" + date +
                ", duration=" + duration +
                ", distance=" + distance +
                ", imageBase64=" + imageBase64 +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Track) {
            Track newTrack = (Track) obj;
            return this.getDistance() == newTrack.getDistance() &&
                    this.getDuration() == newTrack.getDuration() &&
                    this.getDate() == newTrack.getDate() &&
                    this.getAverageSpeed() == newTrack.getAverageSpeed();
        }
        return false;
    }

}


