package com.android.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.db.StudentDBHelper;
import com.android.db.StudentProvider;
import com.android.models.OperationType;
import com.android.models.Student;

public class MainActivity extends AppCompatActivity {

    EditText nameview, rollnoview;
    TextView textview;
    StudentAsyncTask asyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameview = findViewById(R.id.name);
        rollnoview = findViewById(R.id.rollno);
        asyncTask= new StudentAsyncTask();
        textview = findViewById(R.id.textview);

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.insert:{
                Student student = new Student(OperationType.INSERT,String.valueOf(nameview.getText()),
                        Integer.valueOf(String.valueOf(rollnoview.getText())));
                new StudentAsyncTask().execute(student);

                break;
            }
            case R.id.query:{
                Student student = new Student(OperationType.QUERY,null,0);
                new StudentAsyncTask().execute(student);
                break;
            }
            case R.id.delete:{
                Student student = new Student(OperationType.DELETE,null,
                        Integer.valueOf(String.valueOf(rollnoview.getText())));
                new StudentAsyncTask().execute(student);
                break;
            }
            case R.id.update:{
                break;
            }
        }
    }


    class StudentAsyncTask extends AsyncTask<Student,Integer,StringBuilder>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected StringBuilder doInBackground(Student... students) {
            Student student  = students[0];
            OperationType operation = student.getOperationType();
            int roll = student.getRollno();
            String name = student.getName();
            switch (operation){
                case INSERT:{
                    insert(roll,name);
                    return query();
                }
                case QUERY:{
                    return query();
                }
                case DELETE:{
                    delete();
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(StringBuilder result) {
            super.onPostExecute(result);
            if(result!=null)
                textview.setText("");
                textview.setText(result);

        }

        private void insert(int rollno,String name) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(StudentDBHelper.name,name);
            contentValues.put(StudentDBHelper.rollno,rollno);
            getContentResolver().insert(StudentProvider.CONTENT_URI,contentValues);

        }
        private StringBuilder query() {
            Cursor c = getContentResolver().query(StudentProvider.CONTENT_URI, null,
                    null, null, null);
            StringBuilder result = null;
            if(c!=null && c.moveToFirst()) {
                result = new StringBuilder();
                do{
                    result.append(c.getString(0) +"   "+ c.getInt(1)+"\n");
                }while ((c.moveToNext()));
            }
            return result;
        }

        private void delete(){

            int row = getContentResolver().delete(StudentProvider.CONTENT_URI,
                    null
                    ,null);
            if(row>0)
             query();
        }
    }


}
