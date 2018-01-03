package com.example.blackpearl.foldscope.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.blackpearl.foldscope.Utils.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static String TAG = "DataBaseHelper";
    // destination path (location) of our database on device
    private static String DB_PATH = "";
    private static String DB_NAME = "foldscope.sqlite";// Database name
    private SQLiteDatabase mDataBase;
    private final Context mContext;

    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        super.onDowngrade(db, oldVersion, newVersion);
    }

    public void CreateDataBase() throws IOException {
        // If the database does not exist, copy it from the assets.
        boolean mDataBaseExist = CheckDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                // Copy the database from assests
                CopyDataBase();
                Utility.LogI(TAG, "Database Created");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private boolean CheckDataBase() {

        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    // Copy the database from assets
    private void CopyDataBase() throws IOException {

        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    // Open the database, so we can query it
    public boolean OpenDataBase() throws SQLException {

        String mPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null,
                SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {

        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    public void Insert(String tableName, ContentValues cv) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(tableName, null, cv);
        db.close();
    }

    public void Update(String table, ContentValues cv, String whereClause,
                       String whereArgs[]) {
        // TODO Auto-generated method stub
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.update(table, cv, whereClause, whereArgs);
            db.close();
        } catch (Exception e) {
            Utility.LogE(TAG, "Update Database Exception----->" + e.getMessage());
        }
    }

    public double Delete(String tableName, String whereClause, String[] arg) {

        SQLiteDatabase db = getWritableDatabase();
        long returnValue = db.delete(tableName, whereClause, arg);
        db.close();
        return returnValue;
    }

    public DataBaseHelper CreateDatabase() throws SQLException {

        try {
            CreateDataBase();
        } catch (IOException mIOException) {
            Utility.LogE(TAG, "Create Database Exception----->" + mIOException.toString());
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DataBaseHelper Open() throws SQLException {

        try {
            OpenDataBase();
            mDataBase = getReadableDatabase();
        } catch (SQLException mSQLException) {
            Utility.LogE(TAG, "Open Database Exception----->" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public boolean InsertIntoTable(String TableName, ContentValues contentValues) {

        if (mDataBase.isOpen() == false)
            Open();
        Insert(TableName, contentValues);
        return true;
    }

    public void UpdateIntoTable(String TableName, String FieldName, int Value, ContentValues values) {

        Update(TableName, values,
                "" + FieldName + " = '" + Value + "'", null);
        close();
    }

    public void DeleteFromTableName(String TableName, String FieldName, int Value) {

        if (mDataBase.isOpen() == false)
            Open();
        Delete(TableName, "" + FieldName + " = '" + Value + "'", null);
    }

    public Cursor SelectDataFromTable(String TableName, String FieldName, String Value) {

        try {
            Open();
            String sql = "SELECT * FROM " + TableName + " WHERE " + FieldName + "='" + Value + "'";
            Cursor mCur = mDataBase.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Utility.LogE(TAG, "Select Query Exception----->" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor SelectAllDataFromTable(String TableName) {

        try {
            Open();
            String sql = "SELECT * FROM " + TableName;
            Cursor mCur = mDataBase.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Utility.LogE(TAG, "Select Query Exception----->" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor DeleteAllDataFromTableName(String TableName) {

        try {
            Open();
            String sql = "Delete FROM " + TableName;
            Cursor mCur = mDataBase.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Utility.LogE(TAG, "Select Query Exception----->" + mSQLException.toString());
            throw mSQLException;
        }
    }
}