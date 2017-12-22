package com.example.melis.alarmuygulamasi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Melis on 18.12.2017.
 */

public class RegisterActivity extends AppCompatActivity{

    EditText Email, Password, Name ;
    Button Register, geri;
    String NameHolder, EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder ;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String F_Result = "Not_Found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Register = (Button)findViewById(R.id.buttonRegister);

        Email = (EditText)findViewById(R.id.editEmail);
        Password = (EditText)findViewById(R.id.editPassword);
        Name = (EditText)findViewById(R.id.editName);
        geri = (Button)findViewById(R.id.geri);
        sqLiteHelper = new SQLiteHelper(this);

        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, AdminPaneli.class);
                startActivity(i);
            }
        });
         Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // veritabani olustur
                SQLiteDataBaseBuild();

                // tablo olustur
                SQLiteTableBuild();

                // textleri al
                CheckEditTextStatus();

                // e mail olup olmadigini kontrol et
                CheckingEmailAlreadyExistsOrNot();

                // textleri temizle
                EmptyEditTextAfterDataInsert();


            }
        });

    }

     public void SQLiteDataBaseBuild(){

        sqLiteDatabaseObj = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    // SQLite tablosu oluşturma
    public void SQLiteTableBuild() {

        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_NAME + "(" + SQLiteHelper.Table_Column_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL, " + SQLiteHelper.Table_Column_1_Name + " VARCHAR, " + SQLiteHelper.Table_Column_2_Email + " VARCHAR, " + SQLiteHelper.Table_Column_3_Password + " VARCHAR);");

    }

    public void InsertDataIntoSQLiteDatabase(){

        if(EditTextEmptyHolder == true)
        {

            // veri tabanına e-mail, isim ve parola ekleme işlemleri
            SQLiteDataBaseQueryHolder = "INSERT INTO "+SQLiteHelper.TABLE_NAME+" (name,email,password) VALUES('"+NameHolder+"', '"+EmailHolder+"', '"+PasswordHolder+"');";


            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

            // veritabanından çıkıyor
            sqLiteDatabaseObj.close();

           Toast.makeText(RegisterActivity.this,"Kayıt Olundu.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegisterActivity.this, AdminPaneli.class);
            startActivity(intent);

        }
        else {

            Toast.makeText(RegisterActivity.this,"Lütfen, doldurulması gereken hataları doldurun.", Toast.LENGTH_LONG).show();

        }

    }

    public void EmptyEditTextAfterDataInsert(){

        Name.getText().clear();

        Email.getText().clear();

        Password.getText().clear();

    }

    public void CheckEditTextStatus(){

         NameHolder = Name.getText().toString() ;
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();

        if(TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(EmailHolder) ||  TextUtils.isEmpty(PasswordHolder)){

            EditTextEmptyHolder = false ;

        }
        else {

            EditTextEmptyHolder = true ;
        }
    }

    public void CheckingEmailAlreadyExistsOrNot(){

        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

        cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.Table_Column_2_Email + "=?", new String[]{EmailHolder}, null, null, null);

        while (cursor.moveToNext()) {

            if (cursor.isFirst()) {

                cursor.moveToFirst();

                 F_Result = "Email Found";

                cursor.close();
            }
        }

        CheckFinalResult();

    }


    public void CheckFinalResult(){

         if(F_Result.equalsIgnoreCase("Email Found"))
        {

             Toast.makeText(RegisterActivity.this,"E-posta zaten var.",Toast.LENGTH_LONG).show();

        }
        else {

             InsertDataIntoSQLiteDatabase();

        }

        F_Result = "Not_Found" ;

    }

}



