package com.example.cihan.eczanebul;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cihan on 14.04.2019.
 */

public class Database extends SQLiteOpenHelper {
    private static final String TAG = "Database";

    public static final String RECETE_TABLE="Recete";
    public static final String ILAC_TABLE="Ilac";
    public static final String ECZANE_TABLE="Eczane";
    public static final String HASTA_TABLE="Hasta";
    public static final String DOKTOR_TABLE="Doktor";
    public static final String NOBETCI_TABLE="Nobetci";
    private static Database instance=null;

    public static Database getInstance(Context context){
        if(instance==null){
            instance = new Database(context);
        }
        return instance;
    }

    public Database(Context context){
        super(context,"EczaneBul.db",null,1);
        Log.d(TAG, "Database: created");

        //eczaneGir(1,"ccc","Kaya Eczanesi","Esenler","05559997766",50.0f,40.0f);
        //ilacGir("Vicks",1,15.5f,300,1,1);
        //ilacGir("Troysd",1,15.5f,360,1,5);
        //ilacGir("cc",1,10.0f,11,0,5);
        //ilacGir("cc",2,10.0f,11,0,5);
        //eczaneGir(2,"ccc","Gözde Eczanesi","Kütahya","05425424254",100.0f,500.0f);

        //String ilaclar[] = new String[]{"parol","Vicks"};
        //int mg[] = new int[]{50,300};
        //doktorReceteHazirla(ilaclar,mg,2);

        //ilacGir("parol",2,10.0f,50,0,2);
        //doktorGir(1,"Hakan","Ersoy","tcbc");
        //hastaBilgiGir("Ahmet","Demir","ccc",2);
        //doktorReceteHazirla(new String[]{"cc","Troysd"},new int[]{11,360},2);
        //doktorReceteHazirla(new String[]{"cc"},new int[]{11},2,"26-04-2019");
        //doktorReceteHazirla(new String[]{"Troysd"},new int[]{360},2,"27-04-2019");
        //eczaneStokSorgula("Kaya Eczanesi");

//        ArrayList<ArrayList<String>> list = null;
//        list = hastaReceteSorgula(1);

 /*       if(list!=null){
            Log.d(TAG, "Database: "+list.get(0).get(0));
        }
*/
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_eczane,create_hastaid,create_recete,create_doktor,create_ilac,create_nobetci;
        Log.d(TAG, "onCreate: starts");

        create_hastaid="CREATE TABLE HASTAID ( "+
                "ID INT, "+
                "SIFRE VARCHAR(30), "+
                "ISIM VARCHAR(30), "+
                "SOYISIM VARCHAR(30), " +
                "CONSTRAINT PRIM PRIMARY KEY (ID));";

        create_doktor="CREATE TABLE " + DOKTOR_TABLE + " ( "+
                "ID INT, "+
                "ISIM VARCHAR(30), "+
                "SOYISIM VARCHAR(30), "+
                "SIFRE VARCHAR(30), " +
                "CONSTRAINT PRIM PRIMARY KEY (ID));";

        create_eczane="CREATE TABLE " + ECZANE_TABLE + " ( "+
                "ID INT, "+
                "SIFRE VARCHAR(30), "+
                "ISIM VARCHAR(30), "+
                "ADRES VARCHAR(30), " +
                "TELEFON VARCHAR(30), "+
                "KOORDX FLOAT, "+
                "KOORDY FLOAT, " +
                "CONSTRAINT PRIM PRIMARY KEY (ID));";

        create_ilac="CREATE TABLE " + ILAC_TABLE + " ( "+
                "ISIM VARCHAR(30), "+
                "ECZANE INT, "+
                "FIYAT FLOAT, "+
                "MG INT, " +
                "ILAC_DISI INT, " +
                "ADET INT, " +
                "CONSTRAINT PRIM PRIMARY KEY (ISIM,MG,ECZANE));";

        create_recete="CREATE TABLE " + RECETE_TABLE + " ( "+
                "ID INT, "+
                "ILAC VARCHAR(30), "+
                "MG INT, " +
                "KUL INT, " +
                "TARIH VARCHAR(30), "+
                "SATIN_AL_TARIH VARCHAR(30), "+
                "PRIMARY KEY (ID,ILAC));";

        create_nobetci="CREATE TABLE " + NOBETCI_TABLE + " ( "+
                "ID INT, " +
                "CONSTRAINT FORE FOREIGN KEY (ID) REFERENCES ECZANE(ID));";

        sqLiteDatabase.execSQL(create_doktor);
        sqLiteDatabase.execSQL(create_recete);
        sqLiteDatabase.execSQL(create_ilac);
        sqLiteDatabase.execSQL(create_eczane);
        sqLiteDatabase.execSQL(create_nobetci);
        sqLiteDatabase.execSQL(create_hastaid);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }



    public void ilacGir(String isim, int eczane, float fiyat, int mg, int ilacdisi,int adet) {
        SQLiteDatabase db = getWritableDatabase();
        isim=isim.toUpperCase();
        Cursor cursor = db.query("Ilac", new String[]{"ISIM","MG","ADET"}, "ISIM='" + isim + "' and MG=" + mg +" and ECZANE="+eczane, null, null, null, null);
        if (cursor.getCount() == 0){
            String insert = "insert into Ilac values('%s',%d,%f,%d,%d,%d);";
            db.execSQL(String.format(insert, isim, eczane, fiyat, mg, ilacdisi, adet));
        }
        else{
            cursor.moveToFirst();
            String update = "update ilac set adet=%d where isim='%s' and mg=%d and eczane=%d";
            db.execSQL(String.format(update,adet+cursor.getInt(2),isim,mg,eczane));
        }
    }

    public void doktorGir(int id, String isim, String soyisim, String sifre){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor=db.query("Doktor",null,"ID="+id,null,null,null,null);
        if(cursor.getCount()==0) {
            String insert = "insert into Doktor values(%d,'%s','%s','%s');";
            db.execSQL(String.format(insert, id, isim, soyisim, sifre));
        }
        else{
            cursor.moveToFirst();
            String s="";
            for(int i=0;i<cursor.getColumnCount();i++) {
                s+=(cursor.getString(i)+" ");
            }
            Log.d(TAG, "doktorGir: "+s);
        }
    }

    public void nobetciGir(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from Nobetci;");
        Cursor cursor = db.query("Eczane",new String[]{"ID"},null,null,null,null,null);
        int arr[]=new int[cursor.getCount()];
        for(int i=0;i<3;i++) {
            int offset=(int)(Math.random()*1000.0)%cursor.getCount();
          /*while(arr[offset]==1)
                offset=(int)(Math.random()*1000.0)%cursor.getCount();
            arr[offset]=1;*/
            cursor.moveToFirst();
            cursor.move(offset);
            String insert = "insert into Nobetci values('%d');";
            db.execSQL(String.format(insert,cursor.getInt(0)));
        }
    }

    public void receteGir(int id, String isim, int mg, int kul,String tarih){
        SQLiteDatabase db = getWritableDatabase();
        String insert="insert into Recete values(%d,'%s',%d,%d,'%s',%s);";
        db.execSQL(String.format(insert,id,isim,mg,kul,tarih,null));
    }

    public void eczaneGir(int id,String sifre,String isim,String adres,String telefon,float koordx,float koordy){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query("Eczane",null,"ID="+id,null,null,null,null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            String insert = "insert into Eczane values(%d,'%s','%s','%s','%s',%f,%f);";
            db.execSQL(String.format(insert, id, sifre, isim, adres, telefon, koordx, koordy));
        }
        else{
            String s="";
            for(int i=0;i<cursor.getColumnCount();i++){
                 s+=(cursor.getString(i)+" ");
            }
            Log.d(TAG, "eczaneGir: "+s);
        }
    }

    public void hastaBilgiGir(String isim,String soyisim,String sifre,int id){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query("hastaid",null,"ID="+id,null,null,null,null);
        cursor.moveToFirst();
        if(cursor.getCount()==0){
            String insert="insert into HASTAID values(%d,'%s','%s','%s');";
            db.execSQL(String.format(insert,id,sifre,isim,soyisim));
        }
        else{
            String s="";
            for(int i=0;i<cursor.getColumnCount();i++){
                s+=(cursor.getString(i)+" ");
            }
            Log.d(TAG, "hastaBilgiGir: "+s);
        }
    }

    public float dist(float x1,float y1,float x2,float y2){
        return (float)Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    public ArrayList<String> hastaEnYakinEczane(float x, float y,String isim,int mg){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=null;
        if(mg!=-1)
            cursor = db.query("Eczane",new String[]{"ISIM","KOORDX","KOORDY"},"id in (select eczane from ilac where isim='"+isim+"' and mg="+mg+")",null,null,null,null);
        else
            cursor = db.query("Eczane",new String[]{"ISIM","KOORDX","KOORDY"},null,null,null,null,null);
        boolean cont = cursor.moveToFirst();
        int ct=1;
        ArrayList<Float> distList = new ArrayList<>();
        ArrayList<String> nameList = new ArrayList<>();
        while(cont){
            float x1=cursor.getFloat(1),y1=cursor.getFloat(2);
            distList.add(dist(x,y,x1,y1));
            nameList.add(cursor.getString(0));
            Log.d(TAG, "hastaEnYakinEczane: x1="+x1+" y1="+y1+" mg="+mg);
            cont=cursor.moveToNext();
            ct++;
        }
        Log.d(TAG, "hastaEnYakinEczane: koordx = "+x+" koordy = "+y);
        for(int i = 0; i < distList.size(); i++){
            for(int j = 0; j < distList.size()-1; j++){
                Log.d(TAG, "hastaEnYakinEczane: "+distList.get(j)+" "+distList.get(j+1));
                if(distList.get(j)>distList.get(j+1)){
                    float f = distList.get(j);
                    distList.set(j,distList.get(j+1));
                    distList.set(j+1,f);
                    String s = nameList.get(j);
                    nameList.set(j,nameList.get(j+1));
                    nameList.set(j+1,s);
                }
            }
        }
        return nameList;
    }

    public void doktorReceteHazirla(String ilaclar[],int mg[],int kullanici,String tarih){// HATALI,yada degil
        int i;
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor = db.query(true,"Recete",new String[]{"MAX(ID)"},null,null,null,null,null,null);
        cursor.moveToFirst();

        int id;
        if(cursor.getCount()==0)
            id=1;
        else
            id=cursor.getInt(0)+1;
        Log.d(TAG, "doktorReceteHazirla: "+id+" "+ilaclar.length);
        for(i=0;i<ilaclar.length;i++){
            ilaclar[i]=ilaclar[i].toUpperCase();
            receteGir(id,ilaclar[i],mg[i],kullanici,tarih);
        }
    }

    public boolean doktorSifreSorgula(String kadi,String sifre) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Doktor", new String[]{"ID", "SIFRE"}, "EXISTS (Select * from doktor where ID='" + kadi + "' and SIFRE='" + sifre + "')", null, null, null, null);
        if (cursor.getCount() != 0) {
            return true;
        }
        return false;
    }

    public boolean doktorIlacSorgula(String ilac,int mg){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Ilac",new String[]{"MG"},"ISIM='"+ilac+"' and MG="+mg,null,null,null,null);
        if(cursor.getCount()!=0)
            return true;
            return false;
    }

    public boolean hastaSifreSorgula(String isim,String sifre){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("HastaID",new String[]{"ID","SIFRE"},"EXISTS (Select * from hastaid where ID='"+isim+"' and SIFRE='"+sifre+"')",null,null,null,null);
        if(cursor.getCount()!=0){
            return true;
        }
        return false;
    }

    public String[] hastaNobetciEczaneSorgula(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Nobetci",null,null,null,null,null,null);
        boolean cont = cursor.moveToFirst();
        String nobetciler[]=new String[cursor.getCount()];
        int i=0;
        while(cont){
            nobetciler[i]=cursor.getString(0);
            i++;
            cont = cursor.moveToNext();
        }
        return nobetciler;
    }

    public ArrayList<ArrayList<String>> hastaIlacDisiSorgula(String ilac){
        SQLiteDatabase db = getReadableDatabase();
        ilac=ilac.toUpperCase();
        Cursor cursor = db.query("Ilac",new String[]{"ECZANE","FIYAT"},"ILAC_DISI=1 and ISIM='"+ilac+"'",null,null,null,"FIYAT ASC");

        boolean cont = cursor.moveToFirst();
        Log.d(TAG, "hastaIlacDisiSorgula: "+cursor.getCount()+" "+ilac);
        HashMap<Integer,Boolean> map = new HashMap<>();
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        while(cont){
            int eczane=cursor.getInt(0);
           // if(!map.containsKey(eczane)){
                map.put(eczane,true);
                Cursor cur = db.query("Eczane",new String[]{"ISIM","ADRES"},"ID="+eczane,null,null,null,null);
                cur.moveToFirst();
                ArrayList<String> s = new ArrayList<>();
                s.add(cur.getString(0));
                s.add(cur.getString(1));
                s.add(String.format("%.2f",cursor.getFloat(1)));
                list.add(s);
            //}
            cont=cursor.moveToNext();
            Log.d(TAG, "hastaIlacDisiSorgula: boyut= "+list.size());
        }
        for(int i=0;i<list.size();i++){
            for(int j=0;j<list.size()-1;j++){
                if(Float.parseFloat(list.get(j).get(2))>Float.parseFloat(list.get(j+1).get(2))){
                    ArrayList<String> str = list.get(j);
                    list.set(j,list.get(j+1));
                    list.set(j+1,str);
                }
            }
        }
        return list;
    }

    public ArrayList<ArrayList<String>> hastaReceteSorgula(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Recete",new String[]{"ILAC","ID","MG"},"KUL="+id,null,null,null,"ID DESC");
        int i;
        cursor.moveToFirst();
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        ArrayList<String> adder = new ArrayList<>();

        if(cursor.getCount()==0){
            Log.d(TAG, "hastaReceteSorgula: bos dondu");
            return null;
        }

        int num=cursor.getInt(1);
        adder.add(String.format("%d",num));
        for(i=0;i<cursor.getCount()-1;i++){
            adder.add(cursor.getString(0)+","+cursor.getInt(2));
            cursor.moveToNext();
            if(cursor.getInt(1)!=num){
                num=cursor.getInt(1);
                list.add(adder);
                adder=new ArrayList<>();
                adder.add(String.format("%d",num));
            }
        }
        if(cursor.getInt(1)!=num) {
            adder = new ArrayList<>();
            adder.add(String.format("%d",cursor.getInt(1)));
        }

        adder.add(cursor.getString(0)+","+cursor.getString(2));
        list.add(adder);

        return list;
    }

    public ArrayList<String> hastaIlacNerede(int receteid,int hastaid,float koordx,float koordy){
        ArrayList<ArrayList<String>> hastaReceteler=hastaReceteSorgula(hastaid);

        if(hastaReceteler==null)
            return null;
        Log.d(TAG, "hastaIlacNerede: ct"+hastaReceteler.size());
//        Log.d(TAG, "hastaIlacNerede: "+hastaReceteler.get(1).get(0)+ " "+ hastaReceteler.get(1).get(1)+" "+hastaReceteler.get(1).get(2));

        ArrayList<String> stringList=new ArrayList<>();
        int i,j;

        for(i=0;i<hastaReceteler.size();i++){
            Log.d(TAG, "hastaIlacNerede: "+hastaReceteler.get(i).get(0));
            if(Integer.parseInt(hastaReceteler.get(i).get(0))==receteid){
                SQLiteDatabase db = getReadableDatabase();

                Cursor cursor = db.query("Eczane",new String[]{"ID","ISIM","KOORDX","KOORDY","TELEFON"},null,null,null,null,null);
                boolean cont = cursor.moveToFirst();

                while(cont) {
                    String telefon;
                    int eczid=cursor.getInt(0);
                    Log.d(TAG, "hastaIlacNerede: eczid= "+eczid);
                    Cursor myCursor = db.query("Ilac",new String[]{"ISIM","MG","FIYAT"},"ECZANE="+eczid,null,null,null,null);
                    HashMap<String,Float> map = new HashMap<>();
                    boolean cont2=myCursor.moveToFirst();
                    telefon=cursor.getString(4);
                    while (cont2){
                        Log.d(TAG, "hastaIlacNerede: "+myCursor.getString(0)+","+myCursor.getInt(1));
                        map.put(myCursor.getString(0)+","+myCursor.getInt(1),myCursor.getFloat(2));
                        cont2=myCursor.moveToNext();
                    }
                    for (j = 1; j < hastaReceteler.get(i).size(); j++) {
                        if(!map.containsKey(hastaReceteler.get(i).get(j)))
                        break;

                    }
                    if(j==hastaReceteler.get(i).size())
                        stringList.add(cursor.getString(1)+","+telefon+","+cursor.getFloat(2)+","+cursor.getFloat(3));
                    cont = cursor.moveToNext();
                }
                break;
            }
        }
        ArrayList<Float> distance = new ArrayList<>();
        for(i=0;i<stringList.size();i++){
            String sortArray[]=stringList.get(i).split(",");
            float x=Float.parseFloat(sortArray[2]),y=Float.parseFloat(sortArray[3]);
            distance.add((float)Math.sqrt((x-koordx)*(x-koordx)+(y-koordy)*(y-koordy)));
            stringList.set(i,sortArray[0]+","+sortArray[1]);
        }
        for(i=0;i<stringList.size();i++){
            for(j=0;j<stringList.size()-1;j++){
                if(distance.get(j)>distance.get(j+1)){
                    float x=distance.get(j);
                    distance.set(j,distance.get(j+1));
                    distance.set(j+1,x);
                    String s=stringList.get(j);
                    stringList.set(j,stringList.get(j+1));
                    stringList.set(j+1,s);
                }
            }
        }


        return stringList;
    }

    public boolean eczaneSifreSorgula(String isim, String sifre){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Eczane",new String[]{"ID","SIFRE"},"EXISTS (Select * from eczane where ID='"+isim+"' and SIFRE='"+sifre+"')",null,null,null,null);
        if(cursor.getCount()!=0){
            return true;
        }
        return false;
    }

    public ArrayList<ArrayList<String>> eczaneStokSorgula(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("Ilac",new String[]{"ISIM","MG","ADET"},"Eczane="+id,null,null,null,null,null);
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        boolean cont=cursor.moveToFirst();

        while(cont){
            ArrayList<String> add = new ArrayList<>();
            add.add(cursor.getString(0));
            add.add(cursor.getString(1));
            add.add(cursor.getString(2));
            list.add(add);
            cont=cursor.moveToNext();
        }
        return list;
    }

    public void eczaneStokDus(String isim,int receteId) {//HATALI
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Recete", new String[]{"ILAC", "MG"}, null, null, null, null, null);

        boolean cont = cursor.moveToFirst();

        while (cont) {
            Cursor cursor2 = db.query(true,"ILAC",new String[]{"MIN(ID)"},"ILAC='"+cursor.getString(0)+"' AND MG="+cursor.getInt(1),null,null,null,null,null);
            cursor2.moveToFirst();
            int id = cursor2.getInt(0);
            db = getWritableDatabase();
            db.execSQL("DELETE FROM ILAC WHERE ID="+id);
            cont = cursor.moveToNext();
        }
    }



    public ArrayList<String> eczaneAdresTelefon(String isim){
        ArrayList<String> list = new ArrayList<>();
        list.add(isim);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Eczane",new String[]{"ADRES","TELEFON"},"ISIM='"+isim+"'",null,null,null,null);
        cursor.moveToFirst();
        list.add(cursor.getString(0));
        list.add(cursor.getString(1));
        Log.d(TAG, "eczaneAdresTelefon: "+list.get(1));
        return list;
    }

    public String receteTarihVer(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(true,"RECETE",new String[]{"TARIH"},"ID="+id,null,null,null,null,null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public String receteSatinAlinma(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(true,"RECETE",new String[]{"SATIN_AL_TARIH"},"ID="+id,null,null,null,null,null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }
}