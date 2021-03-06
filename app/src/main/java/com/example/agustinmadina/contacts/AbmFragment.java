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
public class AbmFragment extends Fragment {

    Button mButtonDone;
    EditText mEditTextName;
    EditText mEditTextSurname;
    EditText mEditTextNickname;
    ImageButton mImageButtonPhoto;
    Button mButtonDelete;
    Bitmap mPhoto;
    byte[] mImage;
    int mID;
    String mAction;
    public final static String DELETE_CONTACT = "delete";

    public AbmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_abm, container, false);
        getAction();
        prepareViews(rootView);
        prepareImageButton();
        prepareEditTextListeners();
        prepareButtonDoneModify(rootView);
        prepareButtonDelete();
        if (mAction.equals(ContactListFragment.ACTION_MODIFY)){
            prepareForModifyDelete();
        }
        return rootView;
    }

    private void getAction() {
        String action = getActivity().getIntent().getStringExtra(ContactListFragment.ACTION);
        if (action.equals(ContactListFragment.ACTION_ADD)){
            mAction = ContactListFragment.ACTION_ADD;
        } else {
            mAction = ContactListFragment.ACTION_MODIFY;
        }
    }

    private void prepareForModifyDelete() {
        mEditTextName.setText(getActivity().getIntent().getStringExtra(Contact.NAME));
        mEditTextSurname.setText(getActivity().getIntent().getStringExtra(Contact.SURNAME));
        mEditTextNickname.setText(getActivity().getIntent().getStringExtra(Contact.NICKNAME));
        mImageButtonPhoto.setImageBitmap(getBitmap(getActivity().getIntent().
                getByteArrayExtra(Contact.IMAGE)));
        mPhoto = getBitmap(getActivity().getIntent().getByteArrayExtra(Contact.IMAGE));
        mID = getActivity().getIntent().getIntExtra(Contact.ID,0);
        mButtonDelete.setVisibility(View.VISIBLE);
        mButtonDone.setText("Update");
    }



    private void prepareImageButton() {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {   //VUELVE DE LA CAMARA
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

    private void prepareButtonDoneModify(final View rootView) {
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
    private void prepareButtonDelete() {
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAction = DELETE_CONTACT;
                Activity activity = getActivity();
                Intent intentResult = getIntent();
                activity.setResult(Activity.RESULT_OK, intentResult);
                activity.finish();
            }
        });
    }

    private Intent getIntent() {
        Intent intentResult = new Intent();
        intentResult.putExtra(ContactListFragment.ACTION, mAction);
        intentResult.putExtra(Contact.NAME,mEditTextName.getText().toString());
        intentResult.putExtra(Contact.SURNAME,mEditTextSurname.getText().toString());
        convertBitmapImageToByteArray();
        intentResult.putExtra(Contact.IMAGE,mImage);
        checkNicknameOrNot(intentResult);

        if (mAction.equals(ContactListFragment.ACTION_MODIFY) || mAction.equals(DELETE_CONTACT)){
            intentResult.putExtra(Contact.ID, mID);
        }
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
        mButtonDelete = (Button) rootView.findViewById(R.id.button_delete_contact);
    }

    private Bitmap getBitmap(byte[] image) {
        Bitmap bmp;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        bmp = BitmapFactory.decodeByteArray(image, 0, image.length, options);
        return bmp;
    }
}
