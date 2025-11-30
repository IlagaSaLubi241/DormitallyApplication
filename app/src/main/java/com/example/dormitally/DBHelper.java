package com.example.dormitally;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE = "dormitally.db";
    private static final int VERSION = 1;

    private static final String ACCOUNTS = "accounts";
    private static final String PERSONINFO = "personinfo";
    private static final String ROOMS = "rooms";
    private static final String ROOMASSIGNMENT = "roomAssignment";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // ACCOUNTS TABLE
        String tableAccounts = "CREATE TABLE " + ACCOUNTS + "(" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "firstName TEXT NOT NULL," +
                "lastName TEXT NOT NULL," +
                "userName TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL," +
                "contactNum TEXT," +
                "email TEXT," +
                "status TEXT NOT NULL" +
                ")";

        // PERSON INFO (linked to accounts.userId)
        String tablePersonInfo = "CREATE TABLE " + PERSONINFO + "(" +
                "userId INTEGER PRIMARY KEY," +
                "firstName TEXT NOT NULL," +
                "lastName TEXT NOT NULL," +
                "gender TEXT NOT NULL," +
                "age INTEGER NOT NULL," +
                "civilStatus TEXT," +
                "reasonForRent TEXT," +
                "validId BLOB NOT NULL," +
                "FOREIGN KEY(userId) REFERENCES accounts(userId)" +
                ")";

        // ROOMS TABLE
        String tableRooms = "CREATE TABLE " + ROOMS + "(" +
                "roomId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "roomNum TEXT NOT NULL," +
                "roomType TEXT NOT NULL," +
                "roomStatus TEXT NOT NULL," +
                "roomPrice REAL NOT NULL," +
                "roomCapacity INTEGER NOT NULL," +
                "roomDescription TEXT NOT NULL," +
                "roomPicture BLOB" +
                ")";

        // ROOM ASSIGNMENT
        String tableRoomAssignment = "CREATE TABLE " + ROOMASSIGNMENT + "(" +
                "assignId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER NOT NULL," +
                "roomNum TEXT NOT NULL," +
                "startDate TEXT NOT NULL," +
                "endDate TEXT," +
                "FOREIGN KEY(userId) REFERENCES accounts(userId)," +
                "FOREIGN KEY(roomNum) REFERENCES rooms(roomNum)" +
                ")";

        db.execSQL(tableAccounts);
        db.execSQL(tablePersonInfo);
        db.execSQL(tableRooms);
        db.execSQL(tableRoomAssignment);
    }

    // ---------------------------------------------------------
    // INSERT FUNCTIONS
    // ---------------------------------------------------------

    public long addAccount(Account account){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put("firstName", account.getFirstName());
        v.put("lastName", account.getLastName());
        v.put("userName", account.getUsername());
        v.put("password", account.getPassword());
        v.put("role", account.getRole());
        v.put("contactNum", account.getContactNum());
        v.put("email", account.getEmail());
        v.put("status", account.getStatus());

        long result = db.insert(ACCOUNTS, null, v);
        db.close();
        return result;
    }

    public long addPersonInfo(PersonInfo personInfo){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put("userId", personInfo.getUserId());
        v.put("firstName", personInfo.getFirstName());
        v.put("lastName", personInfo.getLastName());
        v.put("gender", personInfo.getGender());
        v.put("age", personInfo.getAge());
        v.put("civilStatus", personInfo.getCivilStatus());
        v.put("reasonForRent", personInfo.getReasonForRent());
        v.put("validId", personInfo.getValidId());

        long result = db.insert(PERSONINFO, null, v);
        db.close();
        return result;
    }

    public long addRoom(Room room) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put("roomNum", room.getRoomNum());
        v.put("roomType", room.getRoomType());
        v.put("roomStatus", room.getRoomStatus());
        v.put("roomPrice", room.getRoomPrice());
        v.put("roomCapacity", room.getRoomCapacity());
        v.put("roomDescription", room.getRoomDescription());
        v.put("roomPicture", room.getRoomImage()); // BLOB or string uri

        long result = db.insert(ROOMS, null, v);
        db.close();
        return result;
    }

    public long addRoomAssignment(RoomAssignment ra){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put("userId", ra.getUserId());
        v.put("roomNum", ra.getRoomNum());
        v.put("startDate", ra.getStartDate());
        v.put("endDate", ra.getEndDate());

        long result = db.insert(ROOMASSIGNMENT, null, v);
        db.close();
        return result;
    }
    public ArrayList<Room> getAllRooms() {
        ArrayList<Room> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(ROOMS, null, null, null, null, null, "roomPrice");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Room room = new Room(
                    cursor.getInt(cursor.getColumnIndexOrThrow("roomId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("roomNum")),
                    cursor.getString(cursor.getColumnIndexOrThrow("roomType")),
                    cursor.getString(cursor.getColumnIndexOrThrow("roomStatus")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("roomPrice")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("roomCapacity")),
                    cursor.getString(cursor.getColumnIndexOrThrow("roomDescription")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("roomPicture"))
            );
            list.add(room);
            cursor.moveToNext();
        }
        db.close();
        return list;
    }
        @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ROOMASSIGNMENT);
        db.execSQL("DROP TABLE IF EXISTS " + PERSONINFO);
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + ROOMS);
        onCreate(db);
    }
}
