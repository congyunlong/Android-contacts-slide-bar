package com.cong.cly;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.cong.cly.adapter.CharacterParser;
import com.cong.cly.adapter.PinyinComparator;
import com.cong.cly.adapter.SortAdapter;
import com.cong.cly.adapter.SortModel;
import com.cong.cly.view.OnTouchLetterChangeListener;
import com.cong.cly.view.SlideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private LinearLayoutManager mLayoutManager;
    private SortAdapter sortAdapter;
    private SlideBar slideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slideBar = (SlideBar) findViewById(R.id.slide_bar);
        slideBar.setPopTextView((TextView) findViewById(R.id.pop_tv));
        slideBar.setOnTouchBackgroudColor(android.R.color.darker_gray);
        slideBar.setOnTouchLetterChangeListener(new OnTouchLetterChangeListener() {
            @Override
            public void onTouchLetterChange(String s) {
                if (sortAdapter != null) {
                    int pos = sortAdapter.getPositionByItem(s);
                    Log.e("MainActivity", "pos:" + pos);
                    if (mLayoutManager != null) {
                        mLayoutManager.scrollToPositionWithOffset(pos, 0);
                    }
                }
            }
        });
        getSupportLoaderManager().initLoader(CONTACTS_LOADER_ID,
                null,
                this);
    }

    private void loadAdapter(List list) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        if (mLayoutManager == null)
            mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        if (sortAdapter == null)
            sortAdapter = new SortAdapter(list);
        recyclerView.setAdapter(sortAdapter);

    }

    private void parseData(List<SortModel> list) {
        CharacterParser characterParser = CharacterParser.getInstance();
        List<String> azList = new ArrayList<>();
        for (SortModel sm : list) {
            sm.az = characterParser.getSelling(sm.name).toUpperCase();
            String az = sm.az.substring(0, 1);
            if (!az.matches("[A-Z]")) {
                sm.az = "#";
                az = "#";
            }
            if (!azList.contains(az)) {
                azList.add(az);
            }
        }
        for (String s : azList) {
            list.add(new SortModel(s, false));
        }
        if (slideBar != null) {
            Collections.sort(azList);
            String[] stringArray = azList.toArray(new String[azList.size()]);
            slideBar.setAz(stringArray);
            slideBar.postInvalidate();
        }
        Collections.sort(list, new PinyinComparator());
    }

    private static final int CONTACTS_LOADER_ID = 1;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == CONTACTS_LOADER_ID) {
            return contactsLoader();
        }
        return null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<String> contacts = contactsFromCursor(data);
        List<SortModel> list = new ArrayList<>();
        for (String displayName : contacts) {
            list.add(new SortModel(displayName));
        }
        parseData(list);
        loadAdapter(list);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private List<String> contactsFromCursor(Cursor cursor) {
        List<String> contacts = new ArrayList<String>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contacts.add(name);
            } while (cursor.moveToNext());
        }
        return contacts;
    }


    private Loader<Cursor> contactsLoader() {
        Uri contactsUri = ContactsContract.Contacts.CONTENT_URI; // The content URI of the phone contacts

        String[] projection = {                                  // The columns to return for each row
                ContactsContract.Contacts.DISPLAY_NAME
        };

        String selection = null;                                 //Selection criteria
        String[] selectionArgs = {};                             //Selection criteria
        String sortOrder = null;                                 //The sort order for the returned rows

        return new CursorLoader(
                getApplicationContext(),
                contactsUri,
                projection,
                selection,
                selectionArgs,
                sortOrder);
    }
}
