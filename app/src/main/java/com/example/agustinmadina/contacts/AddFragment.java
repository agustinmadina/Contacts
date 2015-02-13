package com.example.agustinmadina.contacts;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {

    Button mButtonDone;
    EditText mEditTextName;
    EditText mEditTextSurname;
    EditText mEditTextNickname;
    ImageButton mImageButtonPhoto;
    Bitmap mPhoto;
    byte[] mImage;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        prepareViews(rootView);
        prepareImageButton(rootView);
        prepareEditTextListeners();
        prepareButton(rootView);
        return rootView;
    }





    private void prepareImageButton(View rootView) {
        mPhoto = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.contactempty);

        mImageButtonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, ContactListFragment.REQUEST_CODE);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ContactListFragment.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mPhoto = (Bitmap) data.getExtras().get("data");
            mImageButtonPhoto.setImageBitmap(mPhoto);
        }
    }

    private void convertBitmapImageToByteArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        mImage = stream.toByteArray();
    }

    private void prepareButton(final View rootView) {
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                Intent intentResult = getIntent();
                activity.setResult(Activity.RESULT_OK, intentResult);
                activity.finish();
            }

        });
    }

    private Intent getIntent() {
        Intent intentResult = new Intent();
        intentResult.putExtra(Contact.NAME,mEditTextName.getText().toString());
        intentResult.putExtra(Contact.SURNAME,mEditTextSurname.getText().toString());
        convertBitmapImageToByteArray();
        intentResult.putExtra(Contact.IMAGE,mImage);
        checkNicknameOrNot(intentResult);
        return intentResult;
    }

    private void checkNicknameOrNot(Intent intentResult) {
        if (!TextUtils.isEmpty(mEditTextNickname.getText().toString())){
            intentResult.putExtra(Contact.NICKNAME, mEditTextNickname.getText().toString());
        } else {
            intentResult.putExtra(Contact.NICKNAME, "");
        }
    }

    private void prepareEditTextListeners() {
        TextWatcher listener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(mEditTextSurname.getText()) &&
                        !TextUtils.isEmpty(mEditTextName.getText())){
                    mButtonDone.setEnabled(true);
                } else {
                    mButtonDone.setEnabled(false);
                }
            }
        };

        mEditTextName.addTextChangedListener(listener);
        mEditTextSurname.addTextChangedListener(listener);
    }

    private void prepareViews(View rootView) {
        mEditTextName = (EditText) rootView.findViewById(R.id.edit_text_name);
        mEditTextSurname = (EditText) rootView.findViewById(R.id.edit_text_surname);
        mEditTextNickname = (EditText) rootView.findViewById(R.id.edit_text_nickname);
        mButtonDone = (Button) rootView.findViewById(R.id.button_done);
        mImageButtonPhoto = (ImageButton) rootView.findViewById(R.id.image_button_photo);
    }
}
