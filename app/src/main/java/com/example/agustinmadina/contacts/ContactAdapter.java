package com.example.agustinmadina.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by agustin.madina on 12/02/2015.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {

    Context mContext;
    List<Contact> mContacts;

    public ContactAdapter(Context context, List<Contact> contacts) {
        super(context, R.layout.contact_item, contacts);
        mContext = context;
        mContacts = contacts;
    }

    private void displayContentInView(int position, View rowView) {
        if (rowView != null) {
            TextView textViewName = (TextView) rowView.findViewById(R.id.text_view_contactname);
            textViewName.setText(mContacts.get(position).getName());
            TextView textViewSurname = (TextView) rowView.findViewById(R.id.text_view_contactsurname);
            textViewSurname.setText(mContacts.get(position).getSurname());
            TextView textViewNickname = (TextView) rowView.findViewById(R.id.text_view_contactnickname);
            textViewNickname.setText(mContacts.get(position).getNickname());
            ImageView imageViewPhoto = (ImageView) rowView.findViewById(R.id.image_view_photo);

            Bitmap bmp = getBitmap(position);
            imageViewPhoto.setImageBitmap(bmp);
        }
    }


    private View reuseOrGenerateRowView(View convertView, ViewGroup parent) {
        View rowView;
        if (convertView != null) {
            rowView = convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.contact_item, parent, false);
        }
        return rowView;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = reuseOrGenerateRowView(convertView, parent);
        displayContentInView(position, rowView);

        return rowView;
    }

    private Bitmap getBitmap(int position) {
        Bitmap bmp;
        byte[] image;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        image = mContacts.get(position).getImage();
        bmp = BitmapFactory.decodeByteArray(image, 0, image.length, options);
        return bmp;
    }
    public Contact getContactItem(int position){
        return mContacts.get(position);
    }

}

