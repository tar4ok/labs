package com.example.sportclubolimp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.sportclubolimp.data.ClubOlimpusContract.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OlympusContentProvider extends ContentProvider {

    OlympusDpOpenHelder dbOpenHelder;

    private static final int MEMBERS = 111;
    private static final int MEMBERS_ID = 222;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(ClubOlimpusContract.AUTHORITY,ClubOlimpusContract.PATH_MEMBERS,MEMBERS);
        uriMatcher.addURI(ClubOlimpusContract.AUTHORITY,ClubOlimpusContract.PATH_MEMBERS+"/#",MEMBERS_ID    );

    }
    @Override
    public boolean onCreate() {
        dbOpenHelder = new OlympusDpOpenHelder(getContext());
        return true;
    }


    @Override
    public Cursor query(  Uri uri,   String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        SQLiteDatabase dp = dbOpenHelder.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match){
            case MEMBERS:
                cursor = dp.query(MemberEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            case MEMBERS_ID:
                selection = MemberEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = dp.query(MemberEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:

                throw new IllegalThreadStateException("can't query incorrect URI " +  uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }





    @Override
    public Uri insert(  Uri uri,   ContentValues  values) {

        String firstName = values.getAsString(MemberEntry.COLUMN_FIRST_NAME);
        if (firstName ==null){
            throw new IllegalThreadStateException("You have to input first name" +  uri);
        }
        String lastName = values.getAsString(MemberEntry.COLUMN_LAST_NAME);
        if (lastName ==null){
            throw new IllegalThreadStateException("You have to input last name" +  uri);
        }
        Integer gender = values.getAsInteger(MemberEntry.COLUMN_GENDER);
        if (gender == null||!(gender==MemberEntry.GENDER_UNKNOWN||gender==MemberEntry.GENDER_MALE||gender==MemberEntry.GENDER_FEMALE)){
            throw new IllegalThreadStateException("You have to input correct gender" +  uri);
        }
        String sport = values.getAsString(MemberEntry.COLUMN_SPORT);
        if (sport ==null){
            throw new IllegalThreadStateException("You have to input sport" +  uri);
        }

        SQLiteDatabase db = dbOpenHelder.getWritableDatabase();

        int match = uriMatcher.match(uri);
        switch (match){
            case MEMBERS:
                long id = db.insert(MemberEntry.TABLE_NAME,null,values);
                if (id == -1 ){
                    Log.e("insertMethod","Insertion of data in the table failed for " + uri);
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri,null);

               return  ContentUris.withAppendedId(uri,id);


            default:

                throw new IllegalThreadStateException("Insertion of data in the table failed for  " +  uri);

        }

    }

    @Override
    public int delete(  Uri uri,   String selection, String[] selectionArgs) {

        int rowsDeleted;

        SQLiteDatabase db = dbOpenHelder.getWritableDatabase();
        int match = uriMatcher.match(uri);

        switch (match){
            case MEMBERS:
                rowsDeleted =  db.delete(MemberEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case MEMBERS_ID:
                selection = MemberEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted =  db.delete(MemberEntry.TABLE_NAME,selection,selectionArgs);
                break;

            default:

                throw new IllegalThreadStateException("can't delete this URI " +  uri);

        }
        if (rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;

    }

    @Override
    public int update(  Uri uri,  ContentValues values, String selection,  String[] selectionArgs) {

        if (values.containsKey(MemberEntry.COLUMN_FIRST_NAME)){
            String firstName = values.getAsString(MemberEntry.COLUMN_FIRST_NAME);
            if (firstName ==null){
                throw new IllegalThreadStateException("You have to input first name" +  uri);
            }
        }
        if (values.containsKey(MemberEntry.COLUMN_LAST_NAME)){
            String lastName = values.getAsString(MemberEntry.COLUMN_LAST_NAME);
            if (lastName ==null){
                throw new IllegalThreadStateException("You have to input last name" +  uri);
            }
        }
        if (values.containsKey(MemberEntry.COLUMN_GENDER)){
            Integer gender = values.getAsInteger(MemberEntry.COLUMN_GENDER);
            if (gender == null||!(gender==MemberEntry.GENDER_UNKNOWN||gender==MemberEntry.GENDER_MALE||gender==MemberEntry.GENDER_FEMALE)){
                throw new IllegalThreadStateException("You have to input correct gender" +  uri);
            }
        }
        if (values.containsKey(MemberEntry.COLUMN_SPORT)){
            String sport = values.getAsString(MemberEntry.COLUMN_SPORT);
            if (sport ==null){
                throw new IllegalThreadStateException("You have to input sport" +  uri);
            }
        }


        SQLiteDatabase db = dbOpenHelder.getWritableDatabase();
        int match = uriMatcher.match(uri);

        int rowsUpdated;

        switch (match){
            case MEMBERS:
                rowsUpdated =  db.update(MemberEntry.TABLE_NAME,values,selection,selectionArgs);

                break;
             case MEMBERS_ID:
                selection = MemberEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated =  db.update(MemberEntry.TABLE_NAME,values,selection,selectionArgs);

                break;
             default:

                throw new IllegalThreadStateException("can't update this URI " +  uri);

        }
        if (rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;


    }
    @Override
    public String getType( Uri uri) {

        int match = uriMatcher.match(uri);

        switch (match){
            case MEMBERS:
                return MemberEntry.CONTENT_MULTIPLE_ITEMS;

            case MEMBERS_ID:

                return MemberEntry.CONTENT_SINGLE_ITEM;
            default:

                throw new IllegalThreadStateException("Unknown URI: " +  uri);

        }

    }
}
//URI - Unified Resource Identifier
//content://com.andoroid.uraall.sportclubolimp/members
//URL - Unified Resource Locator
// https://google.com
//content://com.android.contacts/contacts
//content://com.android.calendar/events
