package com.example.dormitally;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE = "dormitally.db";
    private static final int VERSION = 1;

    private static final String ACCOUNTS = "accounts";
    private static final String PERSONINFO = "personinfo";
    private static final String ROOMS = "rooms";
    private static final String ROOMASSIGNMENT = "roomAssignment";
    private static final String AUDIT = "audit";
    private static final String TRANSACTION_TABLE = "transactionTable";
    private static final String BALANCE = "balance";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE, null, VERSION);
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tableAccounts = "CREATE TABLE " + ACCOUNTS + "(" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userName TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL," +
                "contactNum TEXT," +
                "email TEXT," +
                "status TEXT NOT NULL," +
                "startDate TEXT NOT NULL" +
                ")";

        String tablePersonInfo = "CREATE TABLE " + PERSONINFO + "(" +
                "userId INTEGER PRIMARY KEY," +
                "fullName TEXT NOT NULL," +
                "gender TEXT NOT NULL," +
                "age INTEGER NOT NULL," +
                "address TEXT NOT NULL," +
                "civilStatus TEXT," +
                "reasonForRent TEXT," +
                "validId TEXT NOT NULL," +
                "FOREIGN KEY(userId) REFERENCES accounts(userId) ON DELETE CASCADE" +
                ")";

        String tableRooms = "CREATE TABLE " + ROOMS + "(" +
                "roomId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "roomNum TEXT NOT NULL UNIQUE," +
                "roomType TEXT NOT NULL," +
                "roomStatus TEXT NOT NULL," +
                "roomPrice REAL NOT NULL," +
                "roomCapacity INTEGER NOT NULL," +
                "roomDescription TEXT NOT NULL," +
                "roomPicture TEXT" +
                ")";

        String tableRoomAssignment = "CREATE TABLE " + ROOMASSIGNMENT + "(" +
                "assignmentId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER NOT NULL," +
                "roomNum TEXT NOT NULL," +
                "totalBalance DOUBLE NOT NULL," +
                "FOREIGN KEY(userId) REFERENCES accounts(userId) ON DELETE CASCADE," +
                "FOREIGN KEY(roomNum) REFERENCES rooms(roomNum) ON DELETE CASCADE" +
                ")";
        String tableBalance = "CREATE TABLE balance (" +
                "balanceId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER NOT NULL," +
                "wifiBalance REAL DEFAULT 0," +
                "monthlyRentBalance REAL DEFAULT 0," +
                "electricBalance REAL DEFAULT 0," +
                "waterBalance REAL DEFAULT 0," +
                "totalBalance REAL DEFAULT 0," +
                "maintenanceFee REAL DEFAULT 0," +
                "balanceStatus TEXT," +
                "dueDate TEXT," +
                "verification TEXT," +
                "date TEXT," +
                "FOREIGN KEY(userId) REFERENCES personinfo(userId) ON DELETE CASCADE" +
                ")";

        String tableTransaction = "CREATE TABLE transactionTable (" +
                "transactionId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER NOT NULL," +
                "balanceId INTEGER NOT NULL," +
                "amount REAL NOT NULL," +
                "paidDate TEXT," +
                "paymentMethod TEXT NOT NULL," +
                "referenceNum TEXT," +
                "FOREIGN KEY(userId) REFERENCES accounts(userId),"+
                "FOREIGN KEY(balanceId) REFERENCES balance(balanceId) ON DELETE CASCADE" +
                ")";
        String tableAudit = "CREATE TABLE audit (" +
                "auditId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "transactionId INTEGER," +
                "userId INTEGER," +  // the admin/staff who logged the action
                "actionType TEXT NOT NULL," +
                "timestamp TEXT NOT NULL," +
                "details TEXT," +
                "FOREIGN KEY(transactionId) REFERENCES transactionTable(transactionId) ON DELETE CASCADE," +
                "FOREIGN KEY(userId) REFERENCES accounts(userId)" +
                ")";

        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL(tableAccounts);
        db.execSQL(tablePersonInfo);
        db.execSQL(tableRooms);
        db.execSQL(tableRoomAssignment);
        db.execSQL(tableBalance);
        db.execSQL(tableTransaction);
        db.execSQL(tableAudit);
    }
    public int loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;

        String query = "SELECT userId FROM " + ACCOUNTS +
                " WHERE userName = ? AND password = ?";

        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }

        cursor.close();
        return userId;
    }

    public int getAvailableRoomCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        String query = "SELECT COUNT(*) FROM " + ROOMS +
                " WHERE roomStatus = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"Available"});

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    public int getActiveTenantCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        String query = "SELECT COUNT(*) FROM " + ACCOUNTS +
                " WHERE role = ? AND status = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"Tenant", "Active"});

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    public List<Account> getAllPendingAccounts() {
        List<Account> pendingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + ACCOUNTS + " WHERE status = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"Pending"});

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("userName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("role")),
                        cursor.getString(cursor.getColumnIndexOrThrow("contactNum")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("startDate"))
                );

                pendingList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return pendingList;
    }

    public double getTotalPendingAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalPending = 0;

        String query = "SELECT SUM(totalBalance) FROM balance WHERE balanceStatus = 'Pending'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            totalPending = cursor.getDouble(0);
        }

        cursor.close();
        return totalPending;
    }
    public double getTotalCollectedAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalCollected = 0;

        String query = "SELECT SUM(amount) FROM transactionTable";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            totalCollected = cursor.getDouble(0);
        }

        cursor.close();
        return totalCollected;
    }


    public ArrayList<TenantBillDetails> getAllTenantBills() {
        ArrayList<TenantBillDetails> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " +
                "b.balanceId, " +
                "b.userId, " +
                "p.fullName, " +
                "ra.roomNum, " +
                "b.wifiBalance, " +
                "b.monthlyRentBalance, " +
                "b.electricBalance, " +
                "b.waterBalance, " +
                "b.maintenanceFee, " +
                "b.totalBalance, " +
                "b.balanceStatus, " +
                "b.dueDate, " +
                "b.verification, " +
                "b.date, " +
                "t.paymentMethod, " +
                "t.paidDate " +
                "FROM balance b " +
                "LEFT JOIN personinfo p ON b.userId = p.userId " +
                "LEFT JOIN roomAssignment ra ON b.userId = ra.userId " +
                "LEFT JOIN transactionTable t ON b.balanceId = t.balanceId " +
                "ORDER BY b.date DESC";


        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                TenantBillDetails tb = new TenantBillDetails(
                        c.getInt(c.getColumnIndexOrThrow("userId")),
                        c.getInt(c.getColumnIndexOrThrow("balanceId")),
                        c.getString(c.getColumnIndexOrThrow("fullName")),
                        c.getString(c.getColumnIndexOrThrow("roomNum")),
                        c.getDouble(c.getColumnIndexOrThrow("wifiBalance")),
                        c.getDouble(c.getColumnIndexOrThrow("monthlyRentBalance")),
                        c.getDouble(c.getColumnIndexOrThrow("electricBalance")),
                        c.getDouble(c.getColumnIndexOrThrow("waterBalance")),
                        c.getDouble(c.getColumnIndexOrThrow("maintenanceFee")),
                        c.getDouble(c.getColumnIndexOrThrow("totalBalance")),

                        c.getString(c.getColumnIndexOrThrow("paymentMethod")),
                        c.getString(c.getColumnIndexOrThrow("balanceStatus")),
                        c.getString(c.getColumnIndexOrThrow("dueDate")),
                        c.getString(c.getColumnIndexOrThrow("verification")),
                        c.getString(c.getColumnIndexOrThrow("paidDate")),
                        c.getString(c.getColumnIndexOrThrow("date"))
                );

                list.add(tb);

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public double getMonthlyRentBalance(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double balance = 0.0;

        Cursor c = db.rawQuery(
                "SELECT totalBalance FROM roomassignment WHERE userId = ?",
                new String[]{String.valueOf(userId)}
        );

        if (c != null && c.moveToFirst()) {
            balance = c.getDouble(0);
        }

        if (c != null) c.close();
        db.close();

        return balance;
    }



    public int getUserIdByFullName(String fullName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;

        Cursor c = db.rawQuery(
                "SELECT userId FROM personinfo WHERE fullName = ?",
                new String[]{fullName}
        );

        if (c.moveToFirst()) {
            userId = c.getInt(0);
        }

        c.close();
        return userId;
    }

    public long addBalance(Balance balance) {
        long result = -1;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("userId", balance.getUserId());
        cv.put("wifiBalance", balance.getWifiBalance());
        cv.put("monthlyRentBalance", balance.getMonthlyRentBalance());
        cv.put("electricBalance", balance.getElectricBalance());
        cv.put("waterBalance", balance.getWaterBalance());
        cv.put("totalBalance", balance.getTotalBalance());
        cv.put("maintenanceFee", balance.getMaintenanceFee());
        cv.put("balanceStatus", balance.getBalanceStatus());
        cv.put("dueDate", balance.getDueDate());
        cv.put("verification", balance.getVerification());
        cv.put("date", balance.getDate());

        result = db.insert(BALANCE, null, cv);
        //db.close();
        return result;
    }

    public long addAccount(Account account){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put("userName", account.getUsername());
        v.put("password", account.getPassword());
        v.put("role", account.getRole());
        v.put("contactNum", account.getContactNum());
        v.put("email", account.getEmail());
        v.put("status", account.getStatus());
        v.put("startDate", account.getStartDate());

        long result = db.insert(ACCOUNTS, null, v);
        db.close();
        return result;
    }

    public long addPersonInfo(PersonInfo personInfo){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put("userId", personInfo.getUserId());
        v.put("fullName", personInfo.getFullName());
        v.put("gender", personInfo.getGender());
        v.put("age", personInfo.getAge());
        v.put("address", personInfo.getAddress());
        v.put("civilStatus", personInfo.getCivilStatus());
        v.put("reasonForRent", personInfo.getReasonForRent());
        v.put("validId", personInfo.getValidId().toString());

        long result = db.insert(PERSONINFO, null, v);
        db.close();
        return result;
    }

    public long addRoom(Room room){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put("roomNum", room.getRoomNum());
        v.put("roomType", room.getRoomType());
        v.put("roomStatus", room.getRoomStatus());
        v.put("roomPrice", room.getRoomPrice());
        v.put("roomCapacity", room.getRoomCapacity());
        v.put("roomDescription", room.getRoomDescription());
        v.put("roomPicture", room.getRoomImage().toString());

        long result = db.insert(ROOMS, null, v);
        db.close();
        return result;
    }

    public long addRoomAssignment(RoomAssignment ra){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put("userId", ra.getUserId());
        v.put("roomNum", ra.getRoomNum());
        v.put("totalBalance", ra.getTotalBalance());

        long result = db.insert(ROOMASSIGNMENT, null, v);
        db.close();
        return result;
    }


    public ArrayList<Account> getAllAccounts(){
        ArrayList<Account> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(ACCOUNTS, null, null, null, null, null, "userId DESC");

        while(c.moveToNext()){
            list.add(new Account(
                    c.getInt(c.getColumnIndexOrThrow("userId")),
                    c.getString(c.getColumnIndexOrThrow("userName")),
                    c.getString(c.getColumnIndexOrThrow("password")),
                    c.getString(c.getColumnIndexOrThrow("role")),
                    c.getString(c.getColumnIndexOrThrow("contactNum")),
                    c.getString(c.getColumnIndexOrThrow("email")),
                    c.getString(c.getColumnIndexOrThrow("status")),
                    c.getString(c.getColumnIndexOrThrow("startDate"))
            ));
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<Room> getAllRooms() {
        ArrayList<Room> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(ROOMS, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String pictureStr = cursor.getString(cursor.getColumnIndexOrThrow("roomPicture"));
            Uri imageUri = (pictureStr != null && !pictureStr.isEmpty()) ? Uri.parse(pictureStr) : null;

            Room room = new Room(
                    cursor.getInt(cursor.getColumnIndexOrThrow("roomId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("roomNum")),
                    cursor.getString(cursor.getColumnIndexOrThrow("roomType")),
                    cursor.getString(cursor.getColumnIndexOrThrow("roomStatus")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("roomPrice")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("roomCapacity")),
                    cursor.getString(cursor.getColumnIndexOrThrow("roomDescription")),
                    imageUri
            );
            list.add(room);
        }

        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM transactionTable", null);

        if (c.moveToFirst()) {
            do {
                Transaction t = new Transaction(
                        c.getInt(c.getColumnIndexOrThrow("transactionId")),
                        c.getInt(c.getColumnIndexOrThrow("balanceId")),
                        c.getInt(c.getColumnIndexOrThrow("userId")),
                        c.getDouble(c.getColumnIndexOrThrow("amount")),
                        c.getString(c.getColumnIndexOrThrow("date")),
                        c.getString(c.getColumnIndexOrThrow("paymentMethod")),
                        c.getString(c.getColumnIndexOrThrow("referenceNum"))
                );
                list.add(t);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }
    public ArrayList<Audit> getAllAudits() {
        ArrayList<Audit> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM audit", null);

        if (c.moveToFirst()) {
            do {
                Audit a = new Audit(
                        c.getInt(c.getColumnIndexOrThrow("auditId")),
                        c.getInt(c.getColumnIndexOrThrow("transactionId")),
                        c.getInt(c.getColumnIndexOrThrow("userId")),
                        c.getString(c.getColumnIndexOrThrow("actionType")),
                        c.getString(c.getColumnIndexOrThrow("timestamp")),
                        c.getString(c.getColumnIndexOrThrow("details"))
                );
                list.add(a);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }


    public Room getRoomById(int roomId){
        SQLiteDatabase db = getReadableDatabase();
        Room room = null;

        Cursor cursor = db.query(
                ROOMS,
                null,
                "roomId = ?",
                new String[]{String.valueOf(roomId)},
                null, null, null
        );

        if(cursor != null && cursor.moveToFirst()){
            room = new Room(
                    cursor.getInt(cursor.getColumnIndexOrThrow("roomId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("roomNum")),
                    cursor.getString(cursor.getColumnIndexOrThrow("roomType")),
                    cursor.getString(cursor.getColumnIndexOrThrow("roomStatus")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("roomPrice")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("roomCapacity")),
                    cursor.getString(cursor.getColumnIndexOrThrow("roomDescription")),
                    Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow("roomPicture")))
            );
        }

        if(cursor != null) cursor.close();
        db.close();
        return room;
    }

    public ArrayList<RoomAssignment> getAllAssignments(){
        ArrayList<RoomAssignment> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(ROOMASSIGNMENT, null, null, null, null, null, "assignmentId DESC");

        while(c.moveToNext()){
            list.add(new RoomAssignment(
                    c.getInt(c.getColumnIndexOrThrow("assignmentId")),
                    c.getDouble(c.getColumnIndexOrThrow("totalBalance")),
                    c.getInt(c.getColumnIndexOrThrow("userId")),
                    c.getString(c.getColumnIndexOrThrow("roomNum"))
            ));
        }

        c.close();
        db.close();
        return list;
    }



    public int updateStatus(int id, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put("status", status);

        return db.update(ACCOUNTS, v, "userId = ?", new String[]{String.valueOf(id)});
    }
    public boolean setRoomStatus(String roomNum, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("roomStatus", status);  // your column name in rooms table

        int rows = db.update("rooms", values, "roomNum = ?", new String[]{roomNum});
        db.close();

        return rows > 0; // true if updated
    }



    public int updateRoom(Room room){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put("roomNum", room.getRoomNum());
        v.put("roomType", room.getRoomType());
        v.put("roomStatus", room.getRoomStatus());
        v.put("roomPrice", room.getRoomPrice());
        v.put("roomCapacity", room.getRoomCapacity());
        v.put("roomDescription", room.getRoomDescription());
        v.put("roomPicture", room.getRoomImage().toString());

        return db.update(ROOMS, v, "roomId = ?", new String[]{String.valueOf(room.getRoomId())});
    }
    public int updateBalance(Balance balance){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put("wifiBalance", balance.getWifiBalance());
        v.put("monthlyRentBalance", balance.getMonthlyRentBalance());
        v.put("electricBalance", balance.getElectricBalance());
        v.put("waterBalance", balance.getWaterBalance());
        v.put("totalBalance", balance.getTotalBalance());
        v.put("maintenanceFee", balance.getMaintenanceFee());
        v.put("balanceStatus", balance.getBalanceStatus());
        v.put("dueDate", balance.getDueDate());
        v.put("verification", balance.getVerification());
        v.put("date", balance.getDate());

        return db.update(BALANCE, v, "balanceId = ?", new String[]{String.valueOf(balance.getBalanceId())});
    }


    public int deleteAccount(int userId){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(ACCOUNTS, "userId=?", new String[]{String.valueOf(userId)});
    }
    public int deleteBalance(int balanceId){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(BALANCE, "balanceId=?", new String[]{String.valueOf(balanceId)});
    }


    public int deleteRoom(int roomId){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(ROOMS, "roomId=?", new String[]{String.valueOf(roomId)});
    }


    public TenantFullDetails getTenantFullDetailsById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query =
                "SELECT a.userId, a.userName, a.contactNum, a.email, a.status, a.startDate, " +
                        "p.fullName, p.gender, p.age, p.address, p.civilStatus, p.reasonForRent, p.validId, " +
                        "r.roomNum, r.totalBalance " +
                        "FROM accounts a " +
                        "LEFT JOIN personInfo p ON a.userId = p.userId " +
                        "LEFT JOIN roomAssignment r ON a.userId = r.userId " +
                        "WHERE a.userId = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        TenantFullDetails tf = null;

        if (cursor.moveToFirst()) {
            tf = new TenantFullDetails();
            tf.setUserId(cursor.getInt(0));
            tf.setUserName(cursor.getString(1));
            tf.setContactNum(cursor.getString(2));
            tf.setEmail(cursor.getString(3));
            tf.setStatus(cursor.getString(4));
            tf.setAccountStartDate(cursor.getString(5));

            tf.setFullName(cursor.getString(6));
            tf.setGender(cursor.getString(7));
            tf.setAge(cursor.getInt(8));
            tf.setAddress(cursor.getString(9));
            tf.setCivilStatus(cursor.getString(10));
            tf.setReasonForRent(cursor.getString(11));
            tf.setValidId(Uri.parse(cursor.getString(12)));

            tf.setRoomNum(cursor.getString(13));
            tf.setTotalBalance(cursor.getDouble(14));

            tf.setRoomStartDate(null); // because no room start date exists in your table
        }

        cursor.close();
        db.close();
        return tf;
    }
    public ArrayList<String> getAllActiveTenantNames() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query =
                "SELECT p.fullName " +
                        "FROM personinfo p " +
                        "INNER JOIN accounts a ON p.userId = a.userId " +
                        "WHERE a.status = 'Active' " +
                        "ORDER BY p.fullName ASC";

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                list.add(c.getString(0));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
