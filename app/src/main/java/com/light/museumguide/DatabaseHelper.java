package com.light.museumguide;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int version = 1;
    private static final String dbName = "database.db";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DatabaseHelper(Context context){
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE user(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "   name_expo TEXT," +
                "   add_info TEXT" +
                ")";
        db.execSQL(query);
        db.execSQL("INSERT INTO user (name_expo, add_info) VALUES('First Expo','this is add info for first expo')");
        db.execSQL("INSERT INTO user (name_expo, add_info) VALUES('Музыкальный инструмент «Кобыз»', 'Российская империя, II-я пол. XIX в.\n" +
                "\n" +
                "Кобыз — казахский национальный струнный смычковый музыкальный инструмент.\n" +
                "Кобыз не имеет верхней доски и состоит из выдолбленного, обтянутого пузырем полушара, с приделанной к нему наверху ручкой и с выпуском внизу для утверждения подставки. Струны, навязываемые на кобыз в количестве двух, свиваются из конских волос.\n" +
                "Играют на кобызе, сжимая его в коленах (как виолончель), коротким смычком.')");
        db.execSQL("INSERT INTO user (name_expo, add_info) VALUES('Музыкальный инструмент «Домбра»', 'Российская империя, нач. XX в.\n" +
                "\n" +
                "Домбра — казахский двухструнный щипковый музыкальный инструмент. \n" +
                "Домбра имеет корпус грушевидной формы и очень длинный гриф, разделенный ладами. Струн — две. Обычно настроены струны в кварту или квинту. Один из основателей казахской музыки на домбре является Курмангазы, чья композиция «Адай» до сих пор популярна в Казахстане и не только.\n" +
                "Звук извлекается щипком, ударом кисти или медиатора. Игрой на домбре сопровождают свое пение народные сказители — акыны. ')");
        db.execSQL("INSERT INTO user (name_expo, add_info) VALUES('Домашний орган', 'США, сер. XIX в.\n" +
                "\n" +
                "Орган (лат. organum) — клавишный духовой музыкальный инструмент, который звучит при помощи труб (металлических, деревянных, без язычков и с язычками) различных тембров, в которые с помощью мехов нагнетается воздух.\n" +
                "В XV в. стали популярны и широко распространены маленький переносной (органетто) и небольшой стационарный (позитив) орган. На экспозиции представлен небольшой стационарный орган, бытовавший немецкой семье.\n')");
        db.execSQL("INSERT INTO user (name_expo, add_info) VALUES('Музыкальный инструмент «Варган»', 'Российская империя, нач. XX в.\n" +
                "\n" +
                "Варган — древнейший духовой язычковый музыкальный инструмент. Наиболее употребляемые названия: шанкобыз, аура, комуз, хомус, доромб, дрымба, зубанка, васанг, морчанг, пымель, вывко, тумра, кубыз, ванияр, маультроммель, коусян, дан мои.\n" +
                "Обычно делается из металла или дерева. При игре варган прижимают к зубам или к губам, ротовая полость служит резонатором. Изменение артикуляции рта даёт возможность менять тон и тембр инструмента. Кроме того, положение диафрагмы вносит новые оттенки в звучание.\n" +
                " В России варганы наиболее широко распространены в Якутии, Туве и на Алтае. ')");
        db.execSQL("INSERT INTO user (name_expo, add_info) VALUES('Культовое место манси (реконструкция)', 'Российская империя, окрестности с. Саранпауль\n" +
                "Кон. XIX - нач. XX вв.,\n" +
                "(совр. Ханты-Мансийский автономный округ - Югра)\n" +
                "Доставлен в музей в 1930-е гг. А.Ф. Палашенковым из Остяко-Вогульского национального\n" +
                "округа, на берегу ручья, впадающего с левой стороны в р. Ляпин (приток Северной Сосьвы),\n" +
                "в 2-2,5 км от Саранпуля вниз по реке')");
        db.execSQL("INSERT INTO user (name_expo, add_info) VALUES('Традиционная казахская юрта', 'Казахстан, г. Петропавловск, 2015 г.\n" +
                "Традиционная казахская юрта с полным внутренним убранством в 2015 г. была подарена\n" +
                "ОГИК музею акимом Северо-Казахстанской области')");
        db.execSQL("INSERT INTO user (name_expo, add_info) VALUES('Second Expo', 'this is add info for second expo')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getDataFromDB(){
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT _id, name_expo, add_info FROM user", null);
        return cursor;
    }
}
