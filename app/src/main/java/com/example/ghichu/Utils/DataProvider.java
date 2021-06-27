package com.example.ghichu.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ghichu.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataProvider extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "toDoList";
    private static final String TODO_TABLE ="todo";
    private static final String ID ="id";
    private static final String TASK ="task";
    private static final String STATUS ="status";
    private static final String CONTENT ="content";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + CONTENT + " TEXT, " + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DataProvider (Context context) {
        super(context, NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TODO_TABLE);
    }
    // tạo bảng ở lần chạy đầu tiên

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    //Xóa bảng cũ
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // tạo bảng mới nếu đã tồn tại
        onCreate(db);
    }
    public void openDatabase(){
        db = this.getWritableDatabase();
        // lay co so du lieu co the ghi
    }
    public void insertTask(ToDoModel task){// chèn task
        ContentValues cv = new ContentValues();// truyền tham số INSERT INTO table(task, content, status) VALUES(task, content, 0)
        cv.put(TASK, task.getTask());// đẩy giá trị vào biến cv để put truyền tham số
        cv.put(STATUS, 0);
        cv.put(CONTENT, task.getContent());
        db.insert(TODO_TABLE, null , cv);// lấy dữ liệu cv thêm vào bảng
    }
    public List<ToDoModel> getAllTasks(){ // lấy toàn bộ dữ liệu
//        ToDoModel task = new ToDoModel();
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;// con trỏ
        db.beginTransaction();// bắt đầu câu lệnh
        try{

            cur = db.query(TODO_TABLE , null , null , null , null, null , null);//lấy hết toàn bộ dữ liệu trong bảng select *from table
            if (cur !=null){
                if (cur.moveToFirst()){// nếu nó ở dòng đầu tiên thì bắt đầu
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setContent(cur.getString(cur.getColumnIndex(CONTENT)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    } while (cur.moveToNext());
                }

            }
        }//sau try có catch ,dùng để xử lý khi lỗi xảy ra,trong trường hợp này bỏ qua lỗi
        finally {// xử lý sau giai đoạn try catch
            db.endTransaction();
            cur.close();
        }
        return taskList;
    }
    public void updateStatus (int id , int status)//LƯU TRẠNG THÁI CỦA DẤU TÍCH KHI TẮT APP VẪN CÒN
    {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        // UPDATE  VALUE STATUS=STATUS WHERE ID=ID
        db.update(TODO_TABLE, cv, ID+"=?", new String[]{String.valueOf(id)});
    }
    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID +"=?", new String[]{String.valueOf(id)});
    }

    public void updateTask(int id, String task, String content) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        cv.put(CONTENT, content);
        db.update(TODO_TABLE, cv, ID+"=?", new String[]{String.valueOf(id)});
    }
}
