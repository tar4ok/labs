package com.example.sportclubolimp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.sportclubolimp.data.ClubOlimpusContract.MemberEntry;

public class OlympusDpOpenHelder extends SQLiteOpenHelper {

    public OlympusDpOpenHelder(Context context ) {
        super(context,  ClubOlimpusContract.DATABASE_NAME, null, ClubOlimpusContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dp) {
        String CREAT_MEMBERS_TABLE="CREATE TABLE " + MemberEntry.TABLE_NAME+"("+MemberEntry._ID+"INTEGER PRIMARY KEY, "+ MemberEntry.COLUMN_FIRST_NAME +"TEXT"
                +MemberEntry.COLUMN_LAST_NAME+"TEXT"+MemberEntry.COLUMN_GENDER+" INTEGER NOT NULL,"+ MemberEntry.COLUMN_SPORT+" TEXT"+" )";
        dp.execSQL(CREAT_MEMBERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dp, int oldVersion, int newVersion) {
        dp.execSQL("DROP TABLE IF EXISTS " + ClubOlimpusContract.DATABASE_NAME);
        onCreate(dp);
    }
}
