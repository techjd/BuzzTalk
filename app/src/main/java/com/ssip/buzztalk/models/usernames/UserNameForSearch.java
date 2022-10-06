package com.ssip.buzztalk.models.usernames;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.linkedin.android.spyglass.mentions.Mentionable;


public class UserNameForSearch implements Mentionable {

    private final String mName;

    public UserNameForSearch(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public UserNameForSearch(Parcel in) {
        mName = in.readString();
    }

    public static final Parcelable.Creator<UserNameForSearch> CREATOR
            = new Parcelable.Creator<UserNameForSearch>() {
        public UserNameForSearch createFromParcel(Parcel in) {
            return new UserNameForSearch(in);
        }

        public UserNameForSearch[] newArray(int size) {
            return new UserNameForSearch[size];
        }
    };

    @NonNull
    @Override
    public String getTextForDisplayMode(@NonNull MentionDisplayMode mode) {
        switch (mode) {
            case FULL:
                return mName;
            case PARTIAL:
            case NONE:
            default:
                return "";
        }
    }

    @NonNull
    @Override
    public MentionDeleteStyle getDeleteStyle() {
        return MentionDeleteStyle.PARTIAL_NAME_DELETE;
    }

    @Override
    public int getSuggestibleId() {
        return mName.hashCode();
    }

    @NonNull
    @Override
    public String getSuggestiblePrimaryText() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
    }


}