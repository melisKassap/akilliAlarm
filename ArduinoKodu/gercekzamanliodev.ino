 #include <LiquidCrystal.h>
//ldr
int LDR_Pin = A7; //analog pin 0
int led = 13;
//hareket
int pirPin = 49; // PIR pin
int ledPin = 48; // LED pin
int deger = 0;
int state = LOW;
//bağlantı kısmı
char data = 0;            //Variable for storing received data
int baslangic=0;
LiquidCrystal lcd(12,11,5,4,3,2);
void setup()
{
   lcd.begin(16,2);
   lcd.setCursor(0,0);
    //hareket
    pinMode(pirPin, INPUT); // PIR Pin'i giriş yapılıyor
    pinMode(ledPin, OUTPUT); // LED Pin'i çıkış yapılıyor 

    //ldr
    pinMode(led, OUTPUT);
    Serial.begin(9600);   //Sets the baud for serial data transmission                               
    
}
void loop()
{
  //Serial.println(analogRead(LDR_Pin));
  lcd.setCursor(0,1);

  //program çalıştığında hareket sensörü led yansın
  while(baslangic == 0){
    digitalWrite(ledPin, HIGH);
    deger = digitalRead(pirPin); // Dijital pin okunuyor
    if(deger == HIGH){
      baslangic=1;
      break;
    }
    
    
    
  }
  hareketled();

  //ldr kısmı
  
  int kontrol =0;
  
  //bağlantı kısmı
 
   if(Serial.available() > 0)      //bağlantı varsa
   {
      
     //Serial.println(analogRead(LDR_Pin));
      // lcd.setCursor(0,0);
      data = Serial.read();        //bağlantıyı okuyor
      while(analogRead(LDR_Pin) > 200){  //ldr den gelen değer 200 den büyük olana kadar döndürüyor
         hareketled();
          
            if(data == '1'){  //mobileden gelen değer 1 ise
              digitalWrite(led, HIGH); //ledi yak
              if(kontrol ==0 ){  
                  Serial.print('3');  //mobil ldr den 3 değeri okusun diye koyduk. eğer 3 ise alarm çaldır sinyali gidiyor mobile
                  lcd.print("Alarm caliyor..");
                  kontrol =1; //alarm 1 kere çalsın sonra bitsin diye kontrol değerini 1 yaptık
              }
            
            }
            else{
            digitalWrite(led, LOW); //mobile bağlı değilse ledi söndür
          }
        }
        if(analogRead(LDR_Pin) < 200){ //ldr den okunan değer 200 den küçükse

          
          digitalWrite(led, LOW); //ledi söndür ve alarm çaldırma
          while(kontrol==0){
            hareketled();
            if(analogRead(LDR_Pin) > 200){ //okumaya devam et, eğer gelen değer 200 den büyükse 
              digitalWrite(led, HIGH); //ledi yak
              
              Serial.print('3');  
              lcd.print("Alarm caliyor..");  //mobilden alarmı çaldır ve ekrana yaz
              kontrol =1;
            
            }
          }
       }
        
}
}

void hareketled(){
  //hareket
  //digitalWrite(ledPin, HIGH);
   deger = digitalRead(pirPin); // Dijital pin okunuyor
   if(deger == HIGH){
    digitalWrite(ledPin, HIGH);
    if(state== LOW){
      //Serial.println("Hareket tespit edildi");
      state = HIGH;
      
    }
   }
   else{
    
    digitalWrite(ledPin,LOW); 
    if(state == HIGH){
      //Serial.println("Hareket yok");
      digitalWrite(ledPin,LOW); 
      state = LOW;
    }
   }
   }

