package com.example.melis.alarmuygulamasi;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Melis on 16.12.2017.
 */

public class AdminPaneli extends AppCompatActivity {
        TextView textView;
        EditText Email, Password;
        Button LogInButton, RegisterButton;
        String EmailHolder, PasswordHolder;
        Boolean EditTextEmptyHolder;
        SQLiteDatabase sqLiteDatabaseObj;
        SQLiteHelper sqLiteHelper;
        Cursor cursor;
        String TempPassword = "NOT_FOUND" ;
        public static final String UserEmail = "";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                setContentView(R.layout.adminpaneli);
                LogInButton = (Button)findViewById(R.id.buttonLogin);

                RegisterButton = (Button)findViewById(R.id.buttonRegister);

                Email = (EditText)findViewById(R.id.editEmail);
                Password = (EditText)findViewById(R.id.textPassword);

                sqLiteHelper = new SQLiteHelper(this);

                //giriş butonuna tıklandığında (Adding click listener to log in button.)
                LogInButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                        // EditText boşsa veya kayıtlı değilse çağır  (Calling EditText is empty or no method.)
                        CheckEditTextStatus();

                        // Giriş fonksiyonunu çağır (Calling login method.)
                        LoginFunction();
                }
                });


                // Kayıt ol butonuna tıkladığımızda (Adding click listener to register button.)
                RegisterButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                                // Kullanıcı oluştur ekranına bağlan (Opening new user registration activity using intent on button click.)
                                Intent intent = new Intent(AdminPaneli.this, RegisterActivity.class);
                                startActivity(intent);

                        }
                });

        }

        // Login fonksiyonunu çalıştırıyoruz (Login function starts from here.)
        public void LoginFunction(){

                if(EditTextEmptyHolder) {

                        //(SQLDatabase bağlanıyoruz, yazma izni alıyoruz) Opening SQLite database write permission.
                        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

                        // Kullanıcının girdiği e-mail veri tabanında kayırlı mı sorguluyoruz(Adding search email query to cursor.)
                        cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.Table_Column_2_Email + "=?", new String[]{EmailHolder}, null, null, null);

                        while (cursor.moveToNext()) {

                                if (cursor.isFirst()) {

                                        cursor.moveToFirst();

                                        // girilen şifre  veritabanında kayıtlı olana e-mail parola ile eşleşiyormu bakıyoruz
                                        // (Storing Password associated with entered email.)
                                        TempPassword = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3_Password));

                                        // cursor metodundan çık (Cosing cursor.)
                                        cursor.close();
                                }
                        }

                        // While dögüsünden çıkınca checkFinalResult metodunu çağır (Calling method to check final result ..)
                        CheckFinalResult();

                }
                else {

                        //eğer e-mail veya parola kutusu boşsa  kullanıcı mesaj gönder
                        //If any of login EditText empty then this block will be executed.
                        Toast.makeText(AdminPaneli.this,"Lütfen parola veya e-mail giriniz",Toast.LENGTH_LONG).show();

                }

        }

        //girilmesi gereken yerler boşsa veya yoksa
        // Checking EditText is empty or not.
        public void CheckEditTextStatus(){

                //edittex lerden değerleri oku ve dizi değişkenlerine kaydet
                // Getting value from All EditText and storing into String Variables.
                EmailHolder = Email.getText().toString(); //e-maili atadık
                PasswordHolder = Password.getText().toString(); // parolayı atadık

                // dizi değişkenleri boş mu
                // Checking EditText is empty or no using TextUtils.
                if( TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){

                        EditTextEmptyHolder = false ;

                }
                else {

                        EditTextEmptyHolder = true ;
                }
        }

        //SQLite veritabanı e-posta ile ilişkili  parolayı kontrol et
        // Checking entered password from SQLite database email associated password.
        public void CheckFinalResult(){

                if(TempPassword.equalsIgnoreCase(PasswordHolder))
                {

                        Toast.makeText(AdminPaneli.this,"Giriş Başarılı",Toast.LENGTH_LONG).show();

                        //bağlantıListesi ekranını aç
                        // Going to Dashboard activity after login success message.
                        Intent intent = new Intent(AdminPaneli.this, BaglantiListesi.class);

                        // giriş yaptığımız mesajını yolla
                        // Sending Email to Dashboard Activity using intent.
                        intent.putExtra(UserEmail, EmailHolder);

                        startActivity(intent);


                }
                else {
                        //kullanıcı adı veya şifre yanlışsa (e-mail or password is wrong)
                        Toast.makeText(AdminPaneli.this," e-mail veya şifre yanlış. Lütfen bir daha deneyin.",Toast.LENGTH_LONG).show();

                }
                TempPassword = "NOT_FOUND" ;


        }
}