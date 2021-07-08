package com.contact.contactapplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;


public class ContactPojo implements Parcelable {

    public String name;
    public String number;

    public String getName() {
        return name==null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number==null ? "" : number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ContactPojo() {
    }

    protected ContactPojo(Parcel in) {
        name = in.readString();
        number = in.readString();
    }

    public static final Creator<ContactPojo> CREATOR = new Creator<ContactPojo>() {
        @Override
        public ContactPojo createFromParcel(Parcel in) {
            return new ContactPojo(in);
        }

        @Override
        public ContactPojo[] newArray(int size) {
            return new ContactPojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(number);
    }
}
