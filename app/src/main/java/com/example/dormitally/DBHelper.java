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

        String tablePaymentInfo = "CREATE TABLE paymentInfo (" +
                "paymentId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "balanceId INTEGER NOT NULL, " +
                "userId INTEGER NOT NULL, " +
                "amount REAL NOT NULL, " +
                "paymentMethod TEXT NOT NULL, " +
                "referenceNum TEXT, " +
                "paidDate TEXT NOT NULL, " +
                "FOREIGN KEY(balanceId) REFERENCES balance(balanceId) ON DELETE CASCADE, " +
                "FOREIGN KEY(userId) REFERENCES accounts(userId) ON DELETE CASCADE" +
                ")";
        String tablemessages =
                "CREATE TABLE message(" +
                        "messageId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "senderId INTEGER NOT NULL, " +
                        "receiverId INTEGER NOT NULL, " +
                        "messageText TEXT NOT NULL, " +
                        "title TEXT NOT NULL, " +
                        "timestamp TEXT, " +
                        "status TEXT DEFAULT 'unread'" +
                        ");";



        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL(tableAccounts);
        db.execSQL(tablePersonInfo);
        db.execSQL(tableRooms);
        db.execSQL(tableRoomAssignment);
        db.execSQL(tableBalance);
        db.execSQL(tableTransaction);
        db.execSQL(tableAudit);
        db.execSQL(tablePaymentInfo);
        db.execSQL(tablemessages);
        createDefaultAdmin(db);
    }

    public int getTotalTransactionsCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM paymentInfo",
                null
        );

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }


    public int numOfActiveTenants() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM accounts WHERE role = 'Tenant' AND status = 'Active'",
                null
        );

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    public String getOccupancyRate() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Count occupied rooms
        Cursor occupiedCursor = db.rawQuery(
                "SELECT COUNT(*) FROM rooms WHERE roomStatus = 'Occupied'", null);
        occupiedCursor.moveToFirst();
        int occupied = occupiedCursor.getInt(0);
        occupiedCursor.close();

        // Count total rooms
        Cursor totalCursor = db.rawQuery(
                "SELECT COUNT(*) FROM rooms", null);
        totalCursor.moveToFirst();
        int total = totalCursor.getInt(0);
        totalCursor.close();

        db.close();

        if (total == 0) return "0%";  // avoid division by zero

        int percentage = (int) ((occupied * 100.0f) / total);

        return percentage + "%";  // example: "40%"
    }


    public String getOccupiedRoomsFraction() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Count occupied rooms
        Cursor occupiedCursor = db.rawQuery(
                "SELECT COUNT(*) FROM rooms WHERE roomStatus = 'Occupied'", null);
        occupiedCursor.moveToFirst();
        int occupied = occupiedCursor.getInt(0);
        occupiedCursor.close();

        // Count total rooms
        Cursor totalCursor = db.rawQuery(
                "SELECT COUNT(*) FROM rooms", null);
        totalCursor.moveToFirst();
        int total = totalCursor.getInt(0);
        totalCursor.close();

        db.close();

        return occupied + "/" + total;   // example: "2/5"
    }

    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM transactionTable ORDER BY paidDate DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Transaction t = new Transaction();

                t.setTransactionId(cursor.getInt(cursor.getColumnIndexOrThrow("transactionId")));
                t.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("userId")));
                t.setBalanceId(cursor.getInt(cursor.getColumnIndexOrThrow("balanceId")));
                t.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow("amount")));
                t.setDate(cursor.getString(cursor.getColumnIndexOrThrow("paidDate")));
                t.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow("paymentMethod")));
                t.setReferenceNum(cursor.getString(cursor.getColumnIndexOrThrow("referenceNum")));

                list.add(t);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public long addMessage(IssueMessage message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("senderId", message.getSenderId());
        values.put("receiverId", message.getRecieverId());
        values.put("messageText", message.getMessageText());
        values.put("title", message.getTitle());
        values.put("timestamp", message.getTimestamp());
        values.put("status", message.getStatus());
        long id = db.insert("message", null, values);

        return id;
    }
    public void createDefaultAdmin(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery(
                "SELECT * FROM accounts WHERE role = 'admin' LIMIT 1",
                null
        );

        boolean adminExists = cursor.moveToFirst();
        cursor.close();

        if (adminExists) return;

        ContentValues cv = new ContentValues();
        cv.put("userName", "admin");
        cv.put("password", "admin123");
        cv.put("role", "admin");
        cv.put("contactNum", "0000000000");
        cv.put("email", "admin@system.com");
        cv.put("status", "active");
        cv.put("startDate", String.valueOf(System.currentTimeMillis()));

        db.insert("accounts", null, cv);
    }



    public List<IssueMessage> getMessagesByUserId(int userId) {
        List<IssueMessage> messageList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM message WHERE senderId = ? OR receiverId = ? ORDER BY timestamp DESC";
        Cursor cursor = db.rawQuery(query, new String[]{
                String.valueOf(userId),
                String.valueOf(userId)
        });

        if (cursor.moveToFirst()) {
            do {
                IssueMessage msg = new IssueMessage(
                        cursor.getInt(cursor.getColumnIndexOrThrow("messageId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("senderId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("receiverId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("messageText")),
                        cursor.getString(cursor.getColumnIndexOrThrow("timestamp")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title"))
                );
                messageList.add(msg);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return messageList;
    }
    public List<IssueMessage> getAllMessages() {
        List<IssueMessage> messageList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM message ORDER BY timestamp DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                IssueMessage msg = new IssueMessage(
                        cursor.getInt(cursor.getColumnIndexOrThrow("messageId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("senderId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("receiverId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("messageText")),
                        cursor.getString(cursor.getColumnIndexOrThrow("timestamp")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title"))
                );
                messageList.add(msg);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return messageList;
    }

    public List<Transaction> getTransactionsByUserId(int userId) {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT transactionId, userId, balanceId, amount, paidDate, paymentMethod, referenceNum " +
                        "FROM transactionTable WHERE userId = ? ORDER BY transactionId DESC",
                new String[]{String.valueOf(userId)}
        );

        if (c.moveToFirst()) {
            do {
                Transaction t = new Transaction(
                        c.getInt(0),     // transactionId
                        c.getInt(1),     // userId
                        c.getInt(2),     // balanceId
                        c.getDouble(3),  // amount
                        c.getString(4),  // paidDate
                        c.getString(5),  // paymentMethod
                        c.getString(6)   // referenceNum
                );
                list.add(t);
            } while (c.moveToNext());
        }

        c.close();
        return list;
    }

    public boolean verifyPaymentByBalanceId(int balanceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            // 1. GET PAYMENT INFO BASED ON BALANCE ID
            Cursor c = db.rawQuery(
                    "SELECT paymentId, userId, amount, paymentMethod, referenceNum, paidDate " +
                            "FROM paymentinfo WHERE balanceId = ?",
                    new String[]{String.valueOf(balanceId)}
            );

            if (!c.moveToFirst()) {
                c.close();
                return false; // no payment found for this balance
            }

            int paymentId   = c.getInt(0);
            int userId      = c.getInt(1);
            double amount   = c.getDouble(2);
            String method   = c.getString(3);
            String refNum   = c.getString(4);
            String paidDate = c.getString(5);
            c.close();

            // 2. INSERT INTO transactionTable
            ContentValues t = new ContentValues();
            t.put("userId", userId);
            t.put("balanceId", balanceId);
            t.put("amount", amount);
            t.put("paymentMethod", method);
            t.put("referenceNum", refNum);
            t.put("paidDate", paidDate);

            long result = db.insert("transactionTable", null, t);
            if (result == -1) return false;

            // 3. UPDATE BALANCE TABLE
            ContentValues update = new ContentValues();
            update.put("balanceStatus", "Paid");
            update.put("verification", "Verified");

            db.update("balance", update, "balanceId=?", new String[]{String.valueOf(balanceId)});

            // 4. DELETE FROM paymentInfo
            db.delete("paymentinfo", "paymentId=?", new String[]{String.valueOf(paymentId)});

            db.setTransactionSuccessful();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            db.endTransaction();
        }
    }

    public boolean addPaymentInfo(int balanceId, int userId, double amount, String method, String refNum, String paidDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("balanceId", balanceId);
        cv.put("userId", userId);
        cv.put("amount", amount);
        cv.put("paymentMethod", method);
        cv.put("referenceNum", refNum);
        cv.put("paidDate", paidDate);

        long result = db.insert("paymentinfo", null, cv);
        return result != -1;
    }
    public TenantBillDetails getTenantBillDetailsByBalanceId(int balanceId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query =
                "SELECT " +
                        "b.balanceId, b.userId, " +
                        "p.fullName, ra.roomNum, " +
                        "b.wifiBalance, b.monthlyRentBalance, b.electricBalance, " +
                        "b.waterBalance, b.maintenanceFee, b.totalBalance, " +
                        "b.balanceStatus, b.dueDate, b.verification, b.date, " +
                        "pi.amount, pi.paymentMethod, pi.referenceNum, pi.paidDate " +
                        "FROM balance b " +
                        "LEFT JOIN personinfo p ON b.userId = p.userId " +
                        "LEFT JOIN roomAssignment ra ON b.userId = ra.userId " +
                        "LEFT JOIN paymentInfo pi ON b.balanceId = pi.balanceId " +
                        "WHERE b.balanceId = ? " +
                        "ORDER BY pi.paidDate DESC LIMIT 1";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(balanceId)});

        TenantBillDetails tb = null;

        if (c.moveToFirst()) {

            tb = new TenantBillDetails(
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
        }
        c.close();
        db.close();
        return tb;
    }
    public boolean updateBalanceToPaid(int balanceId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("balanceStatus", "Paid");
        values.put("verification", "Unverified");

        int rows = db.update("balance", values, "balanceId = ?", new String[]{String.valueOf(balanceId)});
        return rows > 0;
    }

    // Get the total sum of all pending balance for a specific user
    public double getAccountPendingBalance(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        String query = "SELECT SUM(totalBalance) FROM balance WHERE userId = ? AND balanceStatus = 'Pending'";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }


    // Get the total sum of all paid balance for a specific user
    public double getAccountPiadBalance(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        String query = "SELECT SUM(totalBalance) FROM balance WHERE userId = ? AND balanceStatus = 'Paid'";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }

    public ArrayList<Room> getAvailableRooms() {
        ArrayList<Room> roomList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                ROOMS,              // table name
                null,               // all columns
                "roomStatus = ?",   // WHERE clause
                new String[]{"Available"}, // WHERE arguments
                null, null,
                "roomNum ASC"       // order by (optional)
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int roomId = cursor.getInt(cursor.getColumnIndexOrThrow("roomId"));
                String roomNum = cursor.getString(cursor.getColumnIndexOrThrow("roomNum"));
                String roomType = cursor.getString(cursor.getColumnIndexOrThrow("roomType"));
                String roomStatus = cursor.getString(cursor.getColumnIndexOrThrow("roomStatus"));
                double roomPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("roomPrice"));
                int roomCapacity = cursor.getInt(cursor.getColumnIndexOrThrow("roomCapacity"));
                String roomDescription = cursor.getString(cursor.getColumnIndexOrThrow("roomDescription"));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("roomPicture"));

                Uri roomImageUri = null;
                if (imagePath != null) {
                    roomImageUri = Uri.parse(imagePath); // convert string to Uri
                }

                // Create Room object and add to list
                Room room = new Room(
                        roomId,
                        roomNum,
                        roomType,
                        roomStatus,
                        roomPrice,
                        roomCapacity,
                        roomDescription,
                        roomImageUri
                );

                roomList.add(room);

            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return roomList;
    }

    public Balance getLatestBalance(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Balance balance = null;

        String query = "SELECT * FROM " + BALANCE +
                " WHERE userId = ? AND balanceStatus = 'Pending'" +
                " ORDER BY balanceId DESC LIMIT 1";


        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            balance = new Balance(
                    cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("balanceId")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("wifiBalance")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("monthlyRentBalance")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("electricBalance")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("waterBalance")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("totalBalance")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("maintenanceFee")),
                    cursor.getString(cursor.getColumnIndexOrThrow("balanceStatus")),
                    cursor.getString(cursor.getColumnIndexOrThrow("dueDate")),
                    cursor.getString(cursor.getColumnIndexOrThrow("verification")),
                    cursor.getString(cursor.getColumnIndexOrThrow("date"))
            );
        }

        cursor.close();
        db.close();
        return balance;
    }
    public List<Balance> getAllBalancesByUserId(int userId) {
        List<Balance> balanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + BALANCE +
                " WHERE userId = ?" +
                " ORDER BY balanceId DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Balance balance = new Balance(
                        cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("balanceId")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("wifiBalance")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("monthlyRentBalance")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("electricBalance")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("waterBalance")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("totalBalance")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("maintenanceFee")),
                        cursor.getString(cursor.getColumnIndexOrThrow("balanceStatus")),
                        cursor.getString(cursor.getColumnIndexOrThrow("dueDate")),
                        cursor.getString(cursor.getColumnIndexOrThrow("verification")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date"))
                );
                balanceList.add(balance);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return balanceList;
    }

    public String getFullNameByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String fullName = null;

        String query = "SELECT fullName FROM " + PERSONINFO + " WHERE userId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullName"));
            }
            cursor.close();
        }

        db.close();
        return fullName;
    }

    public double getTotalPendingBalance(int userId) {
        double total = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT SUM(totalBalance) FROM balance " +
                "WHERE userId = ? AND balanceStatus = 'Pending'";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
            cursor.close();
        }

        return total;
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

        String query = "SELECT SUM(totalBalance) FROM balance WHERE balanceStatus = 'Pending'  OR verification = 'Unverified'";
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
