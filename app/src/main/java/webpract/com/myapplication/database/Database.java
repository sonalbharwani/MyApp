package webpract.com.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import webpract.com.myapplication.interfaces.KeyInterface;
import webpract.com.myapplication.models.fetchAllData.BrandList;

public class Database implements KeyInterface{
    public static final String DATABASE_NAME = "MyDb.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db = null;
    private Context context = null;
    private DatabaseHelper DBHelper = null;
    @SuppressWarnings("unused")
    private static final String[] ALL_COLUMNS = new String[]
            {"*"};

    private static final String TAG = "==== Database";

    public Database(Context ctx) {
        context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public SQLiteDatabase getSqliteDatabase() {
        return db;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createUserTable());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {
            }
        }
    }

    public Database OpenDatabase() {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void closeDatabase() {
        db.close();
    }


    public static String createUserTable() {
        String query = new StringBuilder().append(CREATE_TABLE)
                .append(TABLE_BRAND).append(_START_BRACKET_).append(ID).append(INTEGER).append(PRIMARY_KEY).append(AUTOINCREMENT).
                        append(_sp_).append(BRAND_NAME).append(TEXT_C).append(BRAND_DESC).append(TEXT_C).append(CREATED_AT).
                        append(TEXT).append(_END_BRACKET_).toString();
        Log.e("Query", query);
        return query;
    }

    public ArrayList<BrandList> getAllBrands() {

//        String query=new StringBuilder().append(SELECT).append("*").append(FROM).append(TABLE_USERS)
//                .append(WHERE).append(USER_NAME).append("=").append("'").append(userName).append("'").append(AND).append(PASSWORD)
//                .append("=").append("'").append(password).append("'").toString();
        String query = new StringBuilder().append(SELECT).append("*").append(FROM).append(TABLE_BRAND).toString();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst())
            return null;

        Log.e("cursor size", cursor.getCount() + "");
        cursor.moveToFirst();

        ArrayList<BrandList> list = new ArrayList<>();
        do {
            list.add(getBrand(cursor));
        }
        while (cursor.moveToNext());

        cursor.close();
        return list;
    }

    private BrandList getBrand(Cursor cursor) {
        BrandList brandList = new BrandList();
        brandList.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(ID))));
        brandList.setName(cursor.getString(cursor.getColumnIndex(BRAND_NAME)));
        brandList.setDescription(cursor.getString(cursor.getColumnIndex(BRAND_DESC)));
        brandList.setCreatedAt(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
        return brandList;
    }

    public BrandList getBrand(String id) {
        BrandList brandList ;
        String query=new StringBuilder().append(SELECT).append(" * ")
                .append(FROM).append(TABLE_BRAND).append(WHERE).append(ID)
                .append(" = ? ").toString();
        Cursor cursor = db.rawQuery(query,
                new String[] { String.valueOf(id)});

        if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst())
            return null;

        cursor.moveToFirst();

        do {
            brandList = getBrand(cursor);
////            Log.e("Tablename", ":" + cursor.getString(cursor.getColumnIndex(DATECREATED)) + "==>" + tableName);
//
//            dateAttraction = CheckDates(dateAttraction, cursor.getString(cursor.getColumnIndex(DATECREATED)));
//            if (!Utility.isValueNull(cursor.getString(cursor.getColumnIndex(DATEMODIFIED))))
//            {
//                dateAttraction = CheckDates(dateAttraction, cursor.getString(cursor.getColumnIndex(DATEMODIFIED)));
//            }
        }
        while (cursor.moveToNext());

        cursor.close();
        return brandList;
    }

    public void insertBrandDataTable(BrandList brandList) {
        if (db == null || brandList == null)
            return;

        if (isBrandExists(Integer.parseInt(brandList.getId())))
        {
            db.update(TABLE_BRAND, getUserContentValues(brandList), new StringBuilder().append(ID).append(" = ? ").toString(), new String[]
                    {String.valueOf(brandList.getId())});
            Log.e(TAG, TABLE_BRAND + " Update");
        }
        else
        {
        db.insert(TABLE_BRAND, null, getUserContentValues(brandList));
        Log.e(TAG, TABLE_BRAND + " Insert");
        }
    }

    public void insertBrandDataTable(String brandName , String brandDesc) {
        if (db == null)
            return;

        if (isBrandExists(brandName , brandDesc))
        {
            db.update(TABLE_BRAND, getUserContentValues(brandName , brandDesc), new StringBuilder().append(BRAND_NAME).append(" = ? ").append(AND).append(BRAND_DESC).append(" = ? ").toString(), new String[]
                    {String.valueOf(brandName) , String.valueOf(brandDesc)});
            Log.e(TAG, TABLE_BRAND + " Update");
        }
        else
        {
            db.insert(TABLE_BRAND, null, getUserContentValues(brandName , brandDesc));
            Log.e(TAG, TABLE_BRAND + " Insert");
        }
    }


//    private boolean isMemberExists(String userName)
//	{
//		Cursor cursor = db.rawQuery("select * from " + TABLE_LOCAL_BANDSCANLOG + " where " + USER_NAME + "='" + userName+"'", null);
//		if (cursor != null && cursor.getCount() > 0)
//		{
//			cursor.close();
//			return true;
//		}
//		cursor.close();
//		return false;
//
//	}

    private ContentValues getUserContentValues(BrandList brandList) {

        if (brandList == null)
            return null;

        ContentValues contentValues = new ContentValues();

        contentValues.put(BRAND_NAME, brandList.getName());
        contentValues.put(BRAND_DESC, brandList.getDescription());
        contentValues.put(CREATED_AT, brandList.getCreatedAt());

        return contentValues;
    }

    private ContentValues getUserContentValues(String brandName , String brandDesc) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(BRAND_NAME, brandName);
        contentValues.put(BRAND_DESC, brandDesc);
        contentValues.put(CREATED_AT , System.currentTimeMillis());

        return contentValues;
    }


    public void deleteData(int id) {
        db.delete(TABLE_BRAND, new StringBuilder().append(ID).append(" = ")
                .append(id).toString(), null);
        Log.e("Delete", "Done");
    }

    public boolean isBrandExists(int id) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from " + TABLE_BRAND + " where " + ID + "=" + id , null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    public boolean isBrandExists(String brandName , String brandDesc) {
        Cursor cursor = null;
        try {
//            cursor = db.rawQuery("select * from " + tablename + " where " + EMAIL + "='" + email + "'" , null);
            String query=new StringBuilder().append(SELECT).append(" * ")
                    .append(FROM).append(TABLE_BRAND).append(WHERE).append(BRAND_NAME)
                    .append(" = ? ").append(AND).append(BRAND_DESC).append(" = ? ").toString();
            Log.e("query" , query);
            cursor = db.rawQuery(query,
                    new String[] { String.valueOf(brandName) , String.valueOf(brandDesc)});

            if (cursor != null && cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }
}