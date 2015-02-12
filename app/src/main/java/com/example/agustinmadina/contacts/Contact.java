package com.example.agustinmadina.contacts;



import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.DataType;

/**
 * Created by agustin.madina on 12/02/2015.
 */
public class Contact {

    public final static String NAME = "firstname";
    public final static String SURNAME = "lastname";
    public final static String NICKNAME = "nickname";
    public final static String IMAGE = "image";
    public final static String ID = "_id";

    @DatabaseField(generatedId = true, columnName = ID) private int id;
    @DatabaseField (columnName = NAME) private String mFirstName;
    @DatabaseField (columnName = SURNAME) private String mLastName;
    @DatabaseField (columnName = NICKNAME) private String mNickname;
    @DatabaseField (columnName = IMAGE, dataType = DataType.BYTE_ARRAY) private byte[] image;

    public Contact() {

    }

    public Contact(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getSurname() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getNickname() {
        return mNickname;
    }

    public void setNickname(String mNickname) {
        this.mNickname = mNickname;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}