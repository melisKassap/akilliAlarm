package com.example.melis.alarmuygulamasi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Melis on 16.12.2017.
 */

public class BaglantiListesi extends AppCompatActivity {

    ListView cihazListesi;
    Button cihazlariGoster;

    String EmailHolder;
    TextView Email;
    Button LogOUT ;

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> eslesmisCihazlar;
    public static String EXTRA_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.baglantilistesi);

        cihazListesi = (ListView) findViewById(R.id.listView);
        cihazlariGoster = (Button) findViewById(R.id.baglan);

        Email = (TextView)findViewById(R.id.textView1);
        LogOUT = (Button)findViewById(R.id.button1);

        Intent intent = getIntent();

        // e-posta ve parola alma

        // Receiving User Email Send By AdminPaneli.
        EmailHolder = intent.getStringExtra(AdminPaneli.UserEmail);

        // Setting up received email to TextView.
        Email.setText(Email.getText().toString()+ EmailHolder);

        // Çıkış butonuna tıklandığında çalıştır (Adding click listener to Log Out button.)
        LogOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Bitir (Finishing activity on button click.)
                finish();

                Toast.makeText(BaglantiListesi.this,"Çıkış Yaptınız", Toast.LENGTH_LONG).show();

            }
        });

        //telefondaki bluetooth bağlantılarını listele (list the Bluetooth connections on the phone)
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        //bluetooth var mı (do you have bluetooth)

        if(myBluetooth == null){

            //Bluetooth yoksa uyarı mesajı ver ve applicationı kapat (Give warning message if bluetooth is not available)
            Toast.makeText(getApplicationContext(),"Telefonunuz Bluetooth Desteklemiyor",Toast.LENGTH_LONG).show();
            finish();
        } else if (!myBluetooth.isEnabled()) {
            //Bluetooth var ama acık degılse acmak icin istekte bulun (If you have Bluetooth but it's not turned on, ask to turn it on)
            Intent BTac = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(BTac, 1);
        }

        cihazlariGoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //eşleşen cihazları göster (show matching devices)
                eslesmisCihazlariGoster();
            }
        });

    }

    //ListeView'a tıklayıp secmemizi saglayan method ( selecting connetion)
    private  AdapterView.OnItemClickListener cihazSec = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            //Mac Addresini yani, viewdaki son 17 karakteri alıyoruz(get mac address)
            String info = ((TextView)view).getText().toString();
            String address = info.substring(info.length() - 17);

            //Yeni bir Activity baslatman icin bir intent tanımlıyoruz (call BaglantiListesiGecis)
            Intent i = new Intent(BaglantiListesi.this,BaglantiListesiGecis.class);
            //Activity'i calistir
            i.putExtra(EXTRA_ADDRESS,address); // bunu gecisEkranında kullanacagiz
            startActivity(i);
        }
    };

    private void eslesmisCihazlariGoster(){
        //eslesmis cihazları al  (list devices)
        eslesmisCihazlar = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if(eslesmisCihazlar.size() > 0){
            for(BluetoothDevice bt: eslesmisCihazlar){
                //Cihazın adını ve adresini listeye ekle (add device's name and address)
                list.add(bt.getName()+"\n"+bt.getAddress());
            }
        } else {
            //cihaz bulunamadı mesajı gönder (device not found send message)
            Toast.makeText(getApplicationContext(),"Eslesmis Cihaz Bulunamadı",Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_activated_1,list);
        cihazListesi.setAdapter(adapter);
        //Listeden bir cihaz sectigimizce cagrılacak method (call when device is selected from list)
        cihazListesi.setOnItemClickListener(cihazSec);


    }
}