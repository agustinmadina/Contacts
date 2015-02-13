package com.example.agustinmadina.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactListFragment extends ListFragment {
    final static Integer REQUEST_CODE = 1;
    DatabaseHelper mDBHelper = null;
    ContactAdapter mAdapter;
    private final static String LOG_TAG = ContactListFragment.class.getSimpleName();
    public final static String ACTION = "action";
    public final static String ACTION_MODIFY = "modify";
    public final static String ACTION_ADD = "action";

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Boolean handled = false;
        switch (id) {
            case R.id.action_add:
                Intent intent = new Intent(getActivity(), AbmActivity.class);
                intent.putExtra(ACTION,ACTION_ADD);
                startActivityForResult(intent, REQUEST_CODE);
                handled = true;
                break;
        }
        if (!handled) {
            handled = super.onOptionsItemSelected(item);
        }
        return handled;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_contact_list, menu);
    }

    public DatabaseHelper getDBHelper() {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return mDBHelper;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareListView();
    }

    private void prepareListView() {
        List<Contact> entries= getContactsSaved();
        mAdapter = new ContactAdapter(getActivity(), entries);
        setListAdapter(mAdapter);
    }

    private List<Contact> getContactsSaved() {
        List<Contact> contacts = new ArrayList<>();
        try {
            Dao<Contact,Integer> contactDao = getDBHelper().getContactDao();
            contacts = contactDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String firstname = data.getStringExtra(Contact.NAME);
                String lastname = data.getStringExtra(Contact.SURNAME);
                String nickname = data.getStringExtra(Contact.NICKNAME);
                byte[] image = data.getByteArrayExtra(Contact.IMAGE);
                Contact contact = getContact(firstname, lastname, nickname, image);
                if (data.getStringExtra(ACTION).equals(ACTION_ADD)) {
                    contact = saveContact(contact);
                    addContact(contact);
                } else {
                    contact.setId(data.getIntExtra(Contact.ID, 0));
                    if (data.getStringExtra(ACTION).equals(ACTION_MODIFY)) {
                        updateContact(contact);
                        addContact(contact);
                    } else {
                        deleteContact(contact);
                        removeContact(contact);
                    }
                }
            }
        }
    }

    private Contact getContact(String firstname, String lastname, String nickname, byte[] image) {
        Contact contact = new Contact();
        contact.setFirstName(firstname);
        contact.setLastName(lastname);
        contact.setNickname(nickname);
        contact.setImage(image);
        return contact;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Contact contact = mAdapter.getContactItem(position);
        Intent intent = doEditDeleteIntent(contact);
        startActivityForResult(intent, REQUEST_CODE);
        removeContact(contact);
    }
    private Intent doEditDeleteIntent(Contact contact) {
        Intent intent = new Intent(getActivity(), AbmActivity.class);
        intent.putExtra(Contact.NAME, contact.getName());
        intent.putExtra(Contact.SURNAME, contact.getSurname());
        intent.putExtra(Contact.NICKNAME, contact.getNickname());
        intent.putExtra(Contact.IMAGE, contact.getImage());
        intent.putExtra(Contact.ID, contact.getId());
        intent.putExtra(ACTION, ACTION_MODIFY);
        return intent;
    }
    private void addContact(Contact contact) {                     //ADAPTER
        mAdapter.add(contact);
    }

    private void removeContact(Contact contact) {
        mAdapter.remove(contact);
    } //ADAPTER



    private Contact saveContact(Contact contact) {                     //DATABASE
        try {
            Dao<Contact,Integer> dao = getDBHelper().getContactDao();
            dao.create(contact);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Failed to create DAO.", e);
        }

        return contact;
    }

    private void updateContact(Contact contact) {                       //DATABASE
        try {
            Dao<Contact,Integer> dao = getDBHelper().getContactDao();
            dao.update(contact);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Failed to create DAO.", e);
        }
    }

    private void deleteContact(Contact contact) {                            //DATABASE
        try {
            Dao<Contact,Integer> dao = getDBHelper().getContactDao();
            dao.delete(contact);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Failed to create DAO.", e);
        }
    }

    @Override
    public void onDestroy() {
        if (mDBHelper!=null){
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
        super.onDestroy();
    }

}