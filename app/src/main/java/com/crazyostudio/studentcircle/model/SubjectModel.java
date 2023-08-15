package com.crazyostudio.studentcircle.model;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SubjectModel implements Parcelable {
    private String SubName, Uri, Path, type;
    private long time;
    private ArrayList<String> notes;

    public SubjectModel() {
    }

    public SubjectModel(String subName, String uri, long time, ArrayList<String> notes, String path) {
        SubName = subName;
        Uri = uri;
        this.time = time;
        this.notes = notes;
        Path = path;
    }

    public SubjectModel(String Type, ArrayList<String> Notes) {
        type = Type;
        notes = Notes;
    }

    protected SubjectModel(Parcel in) {
        SubName = in.readString();
        Uri = in.readString();
        Path = in.readString();
        type = in.readString();
        time = in.readLong();
        notes = in.createStringArrayList();
    }

    public static final Creator<SubjectModel> CREATOR = new Creator<SubjectModel>() {
        @Override
        public SubjectModel createFromParcel(Parcel in) {
            return new SubjectModel(in);
        }

        @Override
        public SubjectModel[] newArray(int size) {
            return new SubjectModel[size];
        }
    };

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    public String getSubName() {
        return SubName;
    }

    public void setSubName(String subName) {
        SubName = subName;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SubName);
        dest.writeString(Uri);
        dest.writeString(Path);
        dest.writeString(type);
        dest.writeLong(time);
        dest.writeStringList(notes);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}