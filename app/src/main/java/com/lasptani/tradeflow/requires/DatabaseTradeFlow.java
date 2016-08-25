package com.lasptani.tradeflow.requires;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lasptani.tradeflow.Warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FamilySG on 17/06/2016.
 */
public class DatabaseTradeFlow extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tradeflow.db";
    public static int DATABASE_VERSION = 7;

    public DatabaseTradeFlow(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create tables
        db.execSQL(EsquemaDBTradeFlow.CREATE_UTIL_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_USUARIOS_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_STORES_PLAZA_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_STORES_CALENDARIO_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_STORES_CALENDARIO_ADDED_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_ALERTS_TABLE_SEPARADAS);
        db.execSQL(EsquemaDBTradeFlow.CREATE_CLIENTS_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_NEWS_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_MODULES_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_BRANDS_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_PRODUCTS_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_PRESENTATIONS_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_PRESENTATIONS_TEMP_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_SALESFLOOR_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_WAREHOUSE_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_REFILL_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_PRICES_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_EXIB_REGULAR_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_EXIB_ADICIONAL_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_MOVABLES_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_TYPE_PROMOTION_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_MATERIAL_PROMOTION_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_MATERIAL_EXHIBITION_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_PRESENTATIONS_COMPETENCE_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_CANT_TYPE_PROMOTION_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_CANT_MATERIAL_PROMOTION_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_CANT_MATERIAL_EXHIBITION_TABLE);
        db.execSQL(EsquemaDBTradeFlow.CREATE_CANT_PRESENTATION_COMPETENCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Message.showMessage(,"");
        if (newVersion > oldVersion) DATABASE_VERSION++;
        Log.e("VERSION DB> ", Integer.toString(newVersion));
    }

    public void deleteTable(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(name, null, null);
    }

    public void deleteTable(String name, String id_store, String id_client, String day) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(name, "day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "'", null);
    }

    public void copyDBPresentations() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO presentations_temp " +
                "(_id,user,id_presentation,id_product,id_brand,presentation,hasPhoto,falta,ok,seted_photo_salesfloor,finishedsalesfloor,finishedwarehouse,finishedrefill,seted_photo_warehouse,pre,surt,warehouse,seted_photo_refill) " +
                "SELECT * FROM presentations");

        db.close();
    }

    //adicionar ultima sincronizacion
    public void addDateLastSinc(String date) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.DATE_LAST_SINC, date);
        values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_PRICES, "0");
        values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_EXIB_REGULAR, "0");
        values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_SALESFLOOR, "0");
        values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_WAREHOUSE, "0");
        values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_REFILL, "0");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_UTIL, values1, null, null);

        db.close(); // Closing database connection
    }

    public void setUrlWS(String url, boolean first) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        if (first) {
            db.execSQL("INSERT OR REPLACE INTO util" +
                    "(_id,url_ws)" +
                    "VALUES ('1','" + url + "') ");
        } else {

            ContentValues values1 = new ContentValues();

            values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.URL_WS, url);

            db.update(EsquemaDBTradeFlow.TABLE_NAME_UTIL, values1, null, null);
        }
        db.close(); // Closing database connection
    }

    public String getUrlWS() {
        String lastSinc = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsUtil.URL_WS +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_UTIL + "; ", null);

        if (cursor.moveToFirst()) {
            do {
                lastSinc = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lastSinc;
    }

    public void setLoadedPrices(String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_PRICES, value);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_UTIL, values1, null, null);
        db.close(); // Closing database connection
    }

    public String getLoadedPrices() {
        String lastSinc = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_PRICES +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_UTIL + "; ", null);

        if (cursor.moveToFirst()) {
            do {
                lastSinc = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lastSinc;
    }

    public void setLoadedExibRegular(String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_EXIB_REGULAR, value);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_UTIL, values1, null, null);
        db.close(); // Closing database connection
    }

    public String getLoadedExibRegular() {
        String lastSinc = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_EXIB_REGULAR +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_UTIL + "; ", null);

        if (cursor.moveToFirst()) {
            do {
                lastSinc = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lastSinc;
    }

    public void setLoadedSalesFloor(String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_SALESFLOOR, value);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_UTIL, values1, null, null);
        db.close(); // Closing database connection
    }

    public String getLoadedSalesFloor() {
        String lastSinc = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_SALESFLOOR +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_UTIL + "; ", null);

        if (cursor.moveToFirst()) {
            do {
                lastSinc = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lastSinc;
    }

    public void setLoadedWarehouse(String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_WAREHOUSE, value);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_UTIL, values1, null, null);
        db.close(); // Closing database connection
    }

    public String getLoadedWarehouse() {
        String lastSinc = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_WAREHOUSE +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_UTIL + "; ", null);

        if (cursor.moveToFirst()) {
            do {
                lastSinc = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lastSinc;
    }

    public void setLoadedRefill(String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_REFILL, value);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_UTIL, values1, null, null);
        db.close(); // Closing database connection
    }

    public String getLoadedRefill() {
        String lastSinc = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsUtil.LOADED_REFILL +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_UTIL + "; ", null);

        if (cursor.moveToFirst()) {
            do {
                lastSinc = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lastSinc;
    }

    public String getLastSinc() {
        String lastSinc = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsUtil.DATE_LAST_SINC + ", " +
                EsquemaDBTradeFlow.NamesColumnsUtil.STORES_UNSENT +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_UTIL + "; ", null);

        if (cursor.moveToFirst()) {
            do {
                lastSinc = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lastSinc;
    }

    public void deleteStoreCalendarAdded(String name, String daytac) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED, "name='" + name + "' AND day='" + daytac + "'", null);//checar si esta en varios dia y la borro de un determinado dia la borra de todos los dias, poner actualizar

        db.close(); // Closing database connection
    }

    //adicionar registros a tabla salesfloor
    public void setPhoto(String user, String id_store, String id_client, String id_brand, String id_product, String id_presentation, String presentation, String falta, String ok, String path, String photo1, String photo2, String loc, String date_time, String hasPhoto) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("REPLACE INTO salesfloor" +
                "(user,id_stores,id_client,id_brand,id_product,id_presentation,presentation,falta,ok,path,photo1,loc,date_time,hasPhoto)" +
                "VALUES ('" + user + "','" + id_store + "','" + id_client + "','" +
                id_brand + "','" + id_product + "','" + id_presentation + "','" + presentation + "','" + falta + "','" +
                ok + "','" + path + "','" + photo1 + "','" + loc + "','" + date_time + "','" + hasPhoto + "') ");

        db.close();
    }


    //adicionar registros a tabla salesfloor
    public void fillSalesFloor(String user, String id_store, String id_client, String id_brand, String id_product, String id_presentation, String presentation, String falta, String ok, String loc, String date_time, String setPhoto, String check_in, String check_out, String name_photo, String finished, String hasPhoto, String day) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT OR IGNORE INTO salesfloor" +
                "(user,id_store,id_client,id_brand,id_product,id_presentation,presentation,falta,ok,loc,date_time,setPhoto,check_in,check_out,name_photo,finished,has_photo,day)" +
                "VALUES ('" + user + "','" + id_store + "','" + id_client + "','" +
                id_brand + "','" + id_product + "','" + id_presentation + "','" + presentation + "','" + falta + "','" +
                ok + "','" + loc + "','" + date_time + "','" + setPhoto + "','" + check_in + "','" + check_out + "','" + name_photo + "','" + finished + "','" + hasPhoto + "','" + day + "') ");


        db.close();
    }

    //adicionar registros a tabla warehouse
    public void fillWarehouse(String user, String id_store, String id_client, String id_brand, String id_product, String id_presentation, String presentation, String warehouse, String pre, String loc, String date_time, String setPhoto, String check_in, String check_out, String name_photo, String finished, String hasPhoto, String day) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT OR IGNORE INTO warehouse" +
                "(user,id_store,id_client,id_brand,id_product,id_presentation,presentation,warehouse,pre,loc,date_time,setPhoto,check_in,check_out,name_photo,finished,has_photo,day)" +
                "VALUES ('" + user + "','" + id_store + "','" + id_client + "','" +
                id_brand + "','" + id_product + "','" + id_presentation + "','" + presentation + "','" + warehouse + "','" +
                pre + "','" + loc + "','" + date_time + "','" + setPhoto + "','" + check_in + "','" + check_out + "','" + name_photo + "','" + finished + "','" + hasPhoto + "','" + day + "') ");


        db.close();
    }

    public void updateWarehouseAndPrePresentationByName(String name, String warehouse, String pre) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.WAREHOUSE, warehouse);
        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.PRE, pre);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "presentation='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updateClientFinished(String id_client, String id_store) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnClients.FINISHED, "1");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_CLIENTS, values1, "id_client='" + id_client + "' AND id_stores='" + id_store + "'", null);
        db.close(); // Closing database connection
    }

    public void updateStoreFinishedCalendar(String id_store, String finished, String day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnStores.FINISHED, finished);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR, values1, "id_stores='" + id_store + "' AND day='" + day + "'", null);
        //db.update(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED, values1, "id_stores='" + id_store + "'", null);
        db.close(); // Closing database connection
    }

    public void updateStoreFinishedCalendarAdded(String id_store, String finished, String day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnStores.FINISHED, finished);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED, values1, "id_stores='" + id_store + "' AND day='" + day + "'", null);
        //db.update(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED, values1, "id_stores='" + id_store + "'", null);
        db.close(); // Closing database connection
    }


    public void updateStoreSent(String id_store, String day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnStores.SENT, "1");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR, values1, "id_stores='" + id_store + "' AND day='" + day + "'", null);
        //db.update(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED, values1, "id_stores='" + id_store + "'", null);
        db.close(); // Closing database connection
    }


    //adicionar registros a tabla refill
    public void fillRefill(String user, String id_store, String id_client, String id_brand, String id_product, String id_presentation, String presentation, String warehouse, String surt, String loc, String date_time, String setPhoto, String check_in, String check_out, String name_photo, String finished, String hasPhoto, String day) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT OR IGNORE INTO refill" +
                "(user,id_store,id_client,id_brand,id_product,id_presentation,presentation,warehouse,surt,loc,date_time,setPhoto,check_in,check_out,name_photo,finished,has_photo,day)" +
                "VALUES ('" + user + "','" + id_store + "','" + id_client + "','" +
                id_brand + "','" + id_product + "','" + id_presentation + "','" + presentation + "','" + warehouse + "','" +
                surt + "','" + loc + "','" + date_time + "','" + setPhoto + "','" + check_in + "','" + check_out + "','" + name_photo + "','" + finished + "','" + hasPhoto + "','" + day + "') ");


        db.close();
    }

    public void updateRefillSurtPresentationByName(String name, String surt) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.SURT, surt);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "presentation='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updatePricesByName(String id_presentation, String id_store, String id_client, String column, String valor, String day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        if (column.equals("price")) values1.put(EsquemaDBTradeFlow.NamesColumnsPrices.PRICE, valor);
        if (column.equals("no_hay"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsPrices.NO_HAY, valor);
        if (column.equals("loc")) values1.put(EsquemaDBTradeFlow.NamesColumnsPrices.LOC, valor);
        if (column.equals("setPhoto"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsPrices.SETPHOTO, valor);
        if (column.equals("name_photo"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsPrices.NAME_PHOTO, valor);
        if (column.equals("finished"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsPrices.FINISHED, valor);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRICES, values1, "day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "'  AND id_presentation='" + id_presentation + "'", null);
        db.close(); // Closing database connection
    }

    public void updateExibRegularByName(String id_presentation, String id_store, String id_client, String column, String valor, String day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        if (column.equals("asig"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsExibRegular.ASIG, valor);
        else if (column.equals("total"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsExibRegular.TOTAL, valor);
        else if (column.equals("porcent"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsExibRegular.PORCENT, valor);
        else if (column.equals("loc"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsExibRegular.LOC, valor);
        else if (column.equals("setPhoto"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsExibRegular.SETPHOTO, valor);
        else if (column.equals("name_photo"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsExibRegular.NAME_PHOTO, valor);
        else if (column.equals("finished"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsExibRegular.FINISHED, valor);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_EXIB_REGULAR, values1, "day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "'  AND id_presentation='" + id_presentation + "'", null);
        db.close(); // Closing database connection
    }

    public void updateSalesFloorByName(String id_presentation, String id_store, String id_client, String column, String valor, String day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        if (column.equals("falta"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsSalesFloor.FALTA, valor);
        if (column.equals("ok"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsSalesFloor.OK, valor);
        if (column.equals("loc")) values1.put(EsquemaDBTradeFlow.NamesColumnsSalesFloor.LOC, valor);
        if (column.equals("setPhoto"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsSalesFloor.SETPHOTO, valor);
        if (column.equals("name_photo"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsSalesFloor.NAME_PHOTO, valor);
        if (column.equals("finished"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsSalesFloor.FINISHED, valor);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_SALESFLOOR, values1, "day='" + day + "' AND id_presentation='" + id_presentation + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "'", null);
        db.close(); // Closing database connection
    }

    public void updateTypePromotion(String user, String id_store, String id_client, String id_brand, String id_presentation, String brand_competence, String presentation_competence, String type_promotion, String loc, String setPhoto, String check_in, String check_out, String name_photo, String finished, String day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.USER, user);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.ID_STORE, id_store);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.ID_CLIENT, id_client);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.ID_BRAND, id_brand);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.ID_PRESENTATION, id_presentation);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.BRAND_COMPETENCE, brand_competence);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.PRESENTATION_COMPETENCE, presentation_competence);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.TYPE_PROMOTION, type_promotion);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.LOC, loc);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.SETPHOTO, setPhoto);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.CHECK_IN, check_in);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.CHECK_OUT, check_out);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.NAME_PHOTO, name_photo);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.FINISHED, finished);
        values1.put(EsquemaDBTradeFlow.NamesColumnsCantTypePromotion.DAY, day);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_CANT_TYPE_PROMOTION, values1, "day='" + day + "' AND id_presentation='" + id_presentation + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "'", null);
        db.close(); // Closing database connection
    }

    public void updateWarehouseByName(String id_presentation, String id_store, String id_client, String column, String valor, String day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        if (column.equals("warehouse"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsWarehouse.WAREHOUSE, valor);
        if (column.equals("pre"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsWarehouse.PRE, valor);
        if (column.equals("loc")) values1.put(EsquemaDBTradeFlow.NamesColumnsWarehouse.LOC, valor);
        if (column.equals("setPhoto"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsWarehouse.SETPHOTO, valor);
        if (column.equals("name_photo"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsWarehouse.NAME_PHOTO, valor);
        if (column.equals("finished"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsWarehouse.FINISHED, valor);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_WAREHOUSE, values1, "day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "'  AND id_presentation='" + id_presentation + "'", null);
        db.close(); // Closing database connection
    }

    public void updateRefillByName(String id_presentation, String id_store, String id_client, String column, String valor, String day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        if (column.equals("warehouse"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsRefill.WAREHOUSE, valor);
        if (column.equals("surt"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsRefill.SURT, valor);
        if (column.equals("loc")) values1.put(EsquemaDBTradeFlow.NamesColumnsRefill.LOC, valor);
        if (column.equals("setPhoto"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsRefill.SETPHOTO, valor);
        if (column.equals("name_photo"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsRefill.NAME_PHOTO, valor);
        if (column.equals("finished"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsRefill.FINISHED, valor);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_REFILL, values1, "day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "'  AND id_presentation='" + id_presentation + "'", null);
        db.close(); // Closing database connection
    }

    public void updateExhibAditionalByName(String id_movable, String id_store, String id_client, String column, String valor, String day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        if (column.equals("cant"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.CANT, valor);
        if (column.equals("loc"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.LOC, valor);
        if (column.equals("setPhoto"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.SETPHOTO, valor);
        if (column.equals("name_photo"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.NAME_PHOTO, valor);
        if (column.equals("finished"))
            values1.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.FINISHED, valor);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_EXIB_ADICIONAL, values1, "day='" + day + "' AND id_movable='" + id_movable + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "'", null);
        db.close(); // Closing database connection
    }

    public String getPrices(String id_presentation, String column) {
        String lastSinc = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                column +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRICES + " WHERE id_presentation='" + id_presentation + "'", null);

        if (cursor.moveToFirst()) {
            do {
                lastSinc = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return lastSinc;
    }

    //adicionar registros a tabla prices
    public void fillPrices2(String user, String id_store, String id_client, String id_brand, String id_product, String id_presentation, String presentation, String price, String no_hay, String loc, String date_time, String setPhoto) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT OR REPLACE INTO prices" +
                "(user,id_store,id_client,id_brand,id_product,id_presentation,presentation,price,no_hay,loc,date_time,setPhoto)" +
                "VALUES ('" + user + "','" + id_store + "','" + id_client + "','" +
                id_brand + "','" + id_product + "','" + id_presentation + "','" + presentation + "','" + price + "','" +
                no_hay + "','" + loc + "','" + date_time + "','" + setPhoto + "') ");

        db.close();
    }

    public void fillPrices1(String user, String no_hay, String loc, String date_time, String setPhoto) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT OR REPLACE INTO prices" +
                "(user,check_in,check_out,name_photo,finished)" +
                "VALUES ('" + user + "','" + no_hay + "','" + loc + "','" + date_time + "','" + setPhoto + "') ");

        db.close();
    }

    public int cantFaltaInSalesFloor(String day, String id_store, String id_client) {
        //instance from db to write on this
        int si;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT count(*) FROM salesfloor WHERE day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND falta='1' ; ", null);
        cursor.moveToFirst();
        si = cursor.getInt(0);

        db.close();

        return si;
    }

    public int cantFaltaInWarehouse(String day, String id_store, String id_client) {
        //instance from db to write on this
        int si;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT count(*) FROM warehouse WHERE day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' ", null);
        cursor.moveToFirst();
        si = cursor.getInt(0);

        db.close();

        return si;
    }

    public int cantFaltaInRefill(String day, String id_store, String id_client) {
        //instance from db to write on this
        int si;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT count(*) FROM warehouse WHERE day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND warehouse!='0' AND warehouse!=''; ", null);
        cursor.moveToFirst();
        si = cursor.getInt(0);

        db.close();

        return si;
    }


    //adicionar registros a tabla prices
    public void fillPrices(String user, String id_store, String id_client, String id_brand, String id_product, String id_presentation, String presentation, String price, String no_hay, String loc, String date_time, String setPhoto, String check_in, String check_out, String name_photo, String finished, String day) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT OR IGNORE INTO prices" +
                "(user,id_store,id_client,id_brand,id_product,id_presentation,presentation,price,no_hay,loc,date_time,setPhoto,check_in,check_out,name_photo,finished,day)" +
                "VALUES ('" + user + "','" + id_store + "','" + id_client + "','" +
                id_brand + "','" + id_product + "','" + id_presentation + "','" + presentation + "','" + price + "','" +
                no_hay + "','" + loc + "','" + date_time + "','" + setPhoto + "','" + check_in + "','" + check_out + "','" + name_photo + "','" + finished + "','" + day + "') ");

        db.close();
    }

    //adicionar registros a tabla prices
    public void fillExibRegular(String user, String id_store, String id_client, String id_brand, String id_product, String id_presentation, String presentation, String asig, String total, String porcent, String loc, String date_time, String setPhoto, String check_in, String check_out, String name_photo, String finished, String day) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT OR IGNORE INTO exibregular" +
                "(user,id_store,id_client,id_brand,id_product,id_presentation,presentation,asig,total,porcent,loc,date_time,setPhoto,check_in,check_out,name_photo,finished,day)" +
                "VALUES ('" + user + "','" + id_store + "','" + id_client + "','" +
                id_brand + "','" + id_product + "','" + id_presentation + "','" + presentation + "','" + asig + "','" +
                total + "','" + porcent + "','" + loc + "','" + date_time + "','" + setPhoto + "','" + check_in + "','" + check_out + "','" + name_photo + "','" + finished + "','" + day + "') ");

        db.close();
    }

    // get all brands for warehouse
    public List<Brand> getBrandsForWarehouse(String user, String id_client) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Brand> listBrands = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsBrands.ID_BRANDS + ", " +
                EsquemaDBTradeFlow.NamesColumnsBrands.ID_CLIENTS + ", " +
                EsquemaDBTradeFlow.NamesColumnsBrands.BRAND +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_SALESFLOOR + " WHERE user='" + user + "' AND falta = '1' AND id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Brand brand = new Brand();
                brand.setId_brand(cursor.getString(0));
                brand.setId_Client(cursor.getString(1));
                brand.setBrand(cursor.getString(2));
                listBrands.add(brand);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listBrands;
    }//fin get brands for warehouse


    public void deleteSalesFloor(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(EsquemaDBTradeFlow.TABLE_NAME_SALESFLOOR, "if exist name='" + name + "'", null);
        } catch (SQLiteException e) {
        }
        db.close(); // Closing database connection
    }

    //adicionar marcas
    public void addBrands(String user, String id_brand, String id_client, String brand) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        //put the content
        values.put(EsquemaDBTradeFlow.NamesColumnsBrands.USER, user);
        values.put(EsquemaDBTradeFlow.NamesColumnsBrands.ID_BRANDS, id_brand);
        values.put(EsquemaDBTradeFlow.NamesColumnsBrands.ID_CLIENTS, id_client);
        values.put(EsquemaDBTradeFlow.NamesColumnsBrands.BRAND, brand);
        values.put(EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED_WAREHOUSE, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED_REFILL, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED_PRICES, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED_EXIBREGULAR, "0");

        db.insert(EsquemaDBTradeFlow.TABLE_NAME_BRANDS, null, values);
        db.close(); // Closing database connection
    }

    //adicionar mueble a exibAdicional
    public long addMovableToExibAdicional(String id_movable, String user, String id_store, String id_client, String id_brand, String movable, String cant, String loc, String date_time, String setPhoto, String check_in, String check_out, String name_photo, String finished, String day) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_MOVABLE, id_movable);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.USER, user);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_STORE, id_store);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_CLIENT, id_client);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_BRAND, id_brand);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.MOVABLE, movable);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.CANT, cant);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.LOC, loc);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.DATE_TIME, date_time);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.SETPHOTO, setPhoto);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.CHECK_IN, check_in);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.CHECK_OUT, check_out);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.NAME_PHOTO, name_photo);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.FINISHED, finished);
        values.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.DAY, day);

        return db.insert(EsquemaDBTradeFlow.TABLE_NAME_EXIB_ADICIONAL, null, values);
    }

    public void addCantTypePromotion(String user, String id_store, String id_client, String id_brand, String id_presentation, String brand_competence, String presentation_competence, String type_promotion, String loc, String setPhoto, String check_in, String check_out, String name_photo, String finished, String day) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT OR REPLACE INTO cant_type_promotion" +
                "(user,id_store,id_client,id_brand,id_presentation,brand_competence,presentation_competence,type_promotion,loc,setPhoto,check_in,check_out,name_photo,finished,day)" +
                "VALUES ('" + user + "','" + id_store + "','" + id_client + "','" +
                id_brand + "','" + id_presentation + "','" + brand_competence + "','" + presentation_competence + "','" + type_promotion + "','" +
                loc + "','" + setPhoto + "','" + check_in + "','" + check_out + "','" + name_photo + "','" + finished + "','" + day + "') ");

        db.close();
    }

    public void addTypePromotion(String id_promotion, String promotion) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        //put the content
        values.put(EsquemaDBTradeFlow.NamesColumnsTypePromotion.ID, id_promotion);
        values.put(EsquemaDBTradeFlow.NamesColumnsTypePromotion.PROMOTION, promotion);

        db.insert(EsquemaDBTradeFlow.TABLE_NAME_TYPE_PROMOTION, null, values);
        db.close(); // Closing database connection
    }

    public void addCantMaterialPromotion(String user, String id_store, String id_client, String id_brand, String id_presentation, String brand_competence, String presentation_competence, String material_promotion, String loc, String setPhoto, String check_in, String check_out, String name_photo, String finished, String day) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT OR REPLACE INTO cant_material_promotion" +
                "(user,id_store,id_client,id_brand,id_presentation,brand_competence,presentation_competence,material_promotion,loc,setPhoto,check_in,check_out,name_photo,finished,day)" +
                "VALUES ('" + user + "','" + id_store + "','" + id_client + "','" +
                id_brand + "','" + id_presentation + "','" + brand_competence + "','" + presentation_competence + "','" + material_promotion + "','" +
                loc + "','" + setPhoto + "','" + check_in + "','" + check_out + "','" + name_photo + "','" + finished + "','" + day + "') ");

        db.close();
    }

    public void addMaterialPromotion(String id_material, String material) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        //put the content
        values.put(EsquemaDBTradeFlow.NamesColumnsMaterialPromotion.ID, id_material);
        values.put(EsquemaDBTradeFlow.NamesColumnsMaterialPromotion.MATERIAL, material);

        db.insert(EsquemaDBTradeFlow.TABLE_NAME_MATERIAL_PROMOTION, null, values);
        db.close(); // Closing database connection
    }

    public void addCantMaterialExhibition(String user, String id_store, String id_client, String id_brand, String id_presentation, String brand_competence, String presentation_competence, String material_exhibition, String loc, String setPhoto, String check_in, String check_out, String name_photo, String finished, String day) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT OR REPLACE INTO cant_material_exhibition" +
                "(user,id_store,id_client,id_brand,id_presentation,brand_competence,presentation_competence,type_promotion,loc,setPhoto,check_in,check_out,name_photo,finished,day)" +
                "VALUES ('" + user + "','" + id_store + "','" + id_client + "','" +
                id_brand + "','" + id_presentation + "','" + brand_competence + "','" + presentation_competence + "','" + material_exhibition + "','" +
                loc + "','" + setPhoto + "','" + check_in + "','" + check_out + "','" + name_photo + "','" + finished + "','" + day + "') ");

        db.close();
    }

    public void addMaterialExhibition(String id_exhibition, String exhibition) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        //put the content
        values.put(EsquemaDBTradeFlow.NamesColumnsMaterialExhibition.ID, id_exhibition);
        values.put(EsquemaDBTradeFlow.NamesColumnsMaterialExhibition.EXHIBITION, exhibition);

        db.insert(EsquemaDBTradeFlow.TABLE_NAME_MATERIAL_EXHIBITION, null, values);
        db.close(); // Closing database connection
    }

    public void addCantPresentationCompetence(String user, String id_store, String id_client, String id_brand, String id_presentation, String brand_competence, String presentation_competence, String price, String detail_promotion, String demostrator, String loc, String setPhoto, String check_in, String check_out, String name_photo, String finished, String day) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT OR REPLACE INTO cant_presentation_competence" +
                "(user,id_store,id_client,id_brand,id_presentation,brand_competence,presentation_competence,price,detail_promotion,demostrator,loc,setPhoto,check_in,check_out,name_photo,finished,day)" +
                "VALUES ('" + user + "','" + id_store + "','" + id_client + "','" +
                id_brand + "','" + id_presentation + "','" + brand_competence + "','" + presentation_competence + "','" + price + "','" + detail_promotion + "','" + demostrator + "','" +
                loc + "','" + setPhoto + "','" + check_in + "','" + check_out + "','" + name_photo + "','" + finished + "','" + day + "') ");

        db.close();
    }

    public void addPresentationsCompetence(String id_brand,String id_presentation, String brand_competence,String presentation_competence) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        //put the content
        values.put(EsquemaDBTradeFlow.NamesColumnspPresentationsCompetence.ID_BRAND, id_brand);
        values.put(EsquemaDBTradeFlow.NamesColumnspPresentationsCompetence.ID, id_presentation);
        values.put(EsquemaDBTradeFlow.NamesColumnspPresentationsCompetence.BRAND_COMPETENCE, brand_competence);
        values.put(EsquemaDBTradeFlow.NamesColumnspPresentationsCompetence.PRESENTATION_COMPETENCE, presentation_competence);

        db.insert(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_COMPENTECE, null, values);
        db.close(); // Closing database connection
    }

    //actualizar cant movable
    public void updateCantMovables(String id_store, String id_client, String id_brand, String id_movable, String cant) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.CANT, cant);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_EXIB_ADICIONAL, values1, "id_store='" + id_store + "' AND id_client='" + id_client + "' AND id_brand='" + id_brand + "' AND id_movable='" + id_movable + "'", null);

        db.close(); // Closing database connection
    }

    public void updateCantMovables(long id, String id_store, String id_client, String id_brand, String id_movable, String cant, String day) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.CANT, cant);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_EXIB_ADICIONAL, values1, "day='" + day + "' AND id='" + id + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND id_brand='" + id_brand + "' AND id_movable='" + id_movable + "'", null);

        db.close(); // Closing database connection
    }

    //finished movable
    public void setFinishedMovable(long id, String id_store, String id_client, String id_brand, String id_movable, String finished, String day) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsExibAditional.FINISHED, finished);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_EXIB_ADICIONAL, values1, "day='" + day + "' AND id='" + id + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND id_brand='" + id_brand + "' AND id_movable='" + id_movable + "'", null);

        db.close(); // Closing database connection
    }

    //finished movable
/*
    public void setIdStoreInBrands(String id_store, String id_client,String day) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsBrands.ID_STORE, id_store);
        values1.put(EsquemaDBTradeFlow.NamesColumnsBrands.DAY, day);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_BRANDS, values1, "id_client='" + id_client + "'", null);

        db.close(); // Closing database connection
    }
*/

/*    public void setDayInClients(String day, String id_store, String id_client) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnClients.DAY, day);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_BRANDS, values1, "id_store='" + id_store + "' AND id_client='" + id_client + "'", null);

        db.close(); // Closing database connection
    }*/

/*    public void setDayAndIdsInProducts(String day, String id_store, String id_client,String id_brand) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnClients.DAY, day);
        values1.put(EsquemaDBTradeFlow.NamesColumnClients.ID_STORES, id_store);
        values1.put(EsquemaDBTradeFlow.NamesColumnClients.ID_CLIENTS, id_client);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_BRANDS, values1, "id_brand='" + id_brand + "'", null);

        db.close(); // Closing database connection
    }*/

    public List<Movable> getMovablesFromExibAditionalByIds(String id_store, String id_client, String id_brand, String day) {
        List<Movable> listMovables = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_MOVABLE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.USER + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_STORE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_CLIENT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_BRAND + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.MOVABLE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CANT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.LOC + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.DATE_TIME + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.SETPHOTO + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CHECK_IN + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CHECK_OUT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.NAME_PHOTO + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.FINISHED + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_EXIB_ADICIONAL + " WHERE day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND id_brand='" + id_brand + "'", null);

        if (cursor.moveToFirst()) {
            do {
                Movable movable = new Movable();
                movable.setId_movable(cursor.getString(0));
                movable.setUser(cursor.getString(1));
                movable.setId_store(cursor.getString(2));
                movable.setId_client(cursor.getString(3));
                movable.setId_brand(cursor.getString(4));
                movable.setMovable(cursor.getString(5));
                movable.setCant(cursor.getString(6));
                movable.setLoc(cursor.getString(7));
                movable.setDate_time(cursor.getString(8));
                movable.setSetPhoto(cursor.getString(9));
                movable.setCheck_in(cursor.getString(10));
                movable.setCheck_out(cursor.getString(11));
                movable.setName_photo(cursor.getString(12));
                movable.setFinished(cursor.getString(13));
                movable.setId(cursor.getString(14));

                listMovables.add(movable);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listMovables;
    }

    public List<Movable> getMovablesFromExibAditionalByName(String id_store, String id_client, String id_brand, String nameMovable, String day) {
        List<Movable> listMovables = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_MOVABLE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.USER + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_STORE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_CLIENT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_BRAND + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.MOVABLE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CANT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.LOC + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.DATE_TIME + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.SETPHOTO + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CHECK_IN + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CHECK_OUT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.NAME_PHOTO + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_EXIB_ADICIONAL + " WHERE day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND id_brand='" + id_brand + "' AND movable='" + nameMovable + "'", null);

        if (cursor.moveToFirst()) {
            do {
                Movable movable = new Movable();
                movable.setId_movable(cursor.getString(0));
                movable.setUser(cursor.getString(1));
                movable.setId_store(cursor.getString(2));
                movable.setId_client(cursor.getString(3));
                movable.setId_brand(cursor.getString(4));
                movable.setMovable(cursor.getString(5));
                movable.setCant(cursor.getString(6));
                movable.setLoc(cursor.getString(7));
                movable.setDate_time(cursor.getString(8));
                movable.setSetPhoto(cursor.getString(9));
                movable.setCheck_in(cursor.getString(10));
                movable.setCheck_out(cursor.getString(11));
                movable.setName_photo(cursor.getString(12));
                movable.setFinished(cursor.getString(13));

                listMovables.add(movable);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listMovables;
    }

    public List<Movable> getMovablesFromExibAditionalByName(String id, String id_store, String id_client, String id_brand, String nameMovable, String day) {
        List<Movable> listMovables = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_MOVABLE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.USER + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_STORE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_CLIENT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_BRAND + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.MOVABLE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CANT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.LOC + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.DATE_TIME + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.SETPHOTO + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CHECK_IN + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CHECK_OUT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.NAME_PHOTO + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_EXIB_ADICIONAL + " WHERE day='" + day + "' AND id='" + id + "'  AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND id_brand='" + id_brand + "' AND movable='" + nameMovable + "'", null);

        if (cursor.moveToFirst()) {
            do {
                Movable movable = new Movable();
                movable.setId_movable(cursor.getString(0));
                movable.setUser(cursor.getString(1));
                movable.setId_store(cursor.getString(2));
                movable.setId_client(cursor.getString(3));
                movable.setId_brand(cursor.getString(4));
                movable.setMovable(cursor.getString(5));
                movable.setCant(cursor.getString(6));
                movable.setLoc(cursor.getString(7));
                movable.setDate_time(cursor.getString(8));
                movable.setSetPhoto(cursor.getString(9));
                movable.setCheck_in(cursor.getString(10));
                movable.setCheck_out(cursor.getString(11));
                movable.setName_photo(cursor.getString(12));
                movable.setFinished(cursor.getString(13));

                listMovables.add(movable);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listMovables;
    }

    public List<Movable> getMovablesFromExibAditional() {
        List<Movable> listMovables = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_MOVABLE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.USER + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_STORE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_CLIENT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_BRAND + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.MOVABLE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CANT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.LOC + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.DATE_TIME + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.SETPHOTO + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CHECK_IN + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CHECK_OUT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.NAME_PHOTO + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_EXIB_ADICIONAL + " ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Movable movable = new Movable();
                movable.setId_movable(cursor.getString(0));
                movable.setUser(cursor.getString(1));
                movable.setId_store(cursor.getString(2));
                movable.setId_client(cursor.getString(3));
                movable.setId_brand(cursor.getString(4));
                movable.setMovable(cursor.getString(5));
                movable.setCant(cursor.getString(6));
                movable.setLoc(cursor.getString(7));
                movable.setDate_time(cursor.getString(8));
                movable.setSetPhoto(cursor.getString(9));
                movable.setCheck_in(cursor.getString(10));
                movable.setCheck_out(cursor.getString(11));
                movable.setName_photo(cursor.getString(12));
                movable.setFinished(cursor.getString(13));

                listMovables.add(movable);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listMovables;
    }

    //borrar mueble de ExibAdicional
    public void deleteMovableFromExibAdicional(long id, String id_store, String id_client, String id_brand, String id_movable, String day) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(EsquemaDBTradeFlow.TABLE_NAME_EXIB_ADICIONAL, "day='" + day + "' AND id='" + id + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND id_brand='" + id_brand + "' AND id_movable='" + id_movable + "'", null);
        db.close(); // Closing database connection
    }

    //adicionar productos
    public void addProducts(String user, String id_product, String id_brand, String product) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        //put the content
        values.put(EsquemaDBTradeFlow.NamesColumnsProducts.USER, user);
        values.put(EsquemaDBTradeFlow.NamesColumnsProducts.ID_PRODUCT, id_product);
        values.put(EsquemaDBTradeFlow.NamesColumnsProducts.ID_BRAND, id_brand);
        values.put(EsquemaDBTradeFlow.NamesColumnsProducts.PRODUCT, product);
        values.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_WAREHOUSE, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_REFILL, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_PRICES, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_EXIBREGULAR, "0");

        db.insert(EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS, null, values);
        db.close(); // Closing database connection
    }

    //adicionar presentaciones
    public void addPresentations(String user, String id_presentation, String id_product, String id_brand, String presentation, String hasPhoto) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        //put the content
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.USER, user);
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRESENTATION, id_presentation);
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRODUCT, id_product);
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.ID_BRAND, id_brand);
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.PRESENTATION, presentation);
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FALTA, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.OK, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.HASPHOTO, hasPhoto);
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.SETPHOTOSALESFLOOR, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FINISHEDSALESFLOOR, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FINISHEDWAREHOUSE, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FINISHEDREFILL, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.SETEDPHOTOWAREHOUSE, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnsPresentations.SETEDPHOTOREFILL, "0");

        db.insert(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS, null, values);
        db.close(); // Closing database connection
    }

    //add movable
    public void addMovable(String id, String movable, String user) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        //put the content
        values.put(EsquemaDBTradeFlow.NamesColumnsMovables.ID, id);
        values.put(EsquemaDBTradeFlow.NamesColumnsMovables.MOVABLE, movable);
        values.put(EsquemaDBTradeFlow.NamesColumnsMovables.USER, user);

        db.insert(EsquemaDBTradeFlow.TABLE_NAME_MOVABLES, null, values);
        db.close(); // Closing database connection
    }

    public void updateFaltaOrOkPresentationByName(String name, String falta, String ok) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FALTA, falta);
        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.OK, ok);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "presentation='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updateOtherdataPresentationByName(String name, String id_store, String check_in, String check_out, String loc, String id_client) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.ID_STORE, id_store);
        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.CHECK_IN, check_in);
        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.CHECK_OUT, check_out);
        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.LOC, loc);
        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.ID_CLIENT, id_client);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "presentation='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updateNamePhotoPresentationByName(String id_presentation, String namePhoto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.NAME_PHOTO, namePhoto);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "id_presentation='" + id_presentation + "'", null);
        db.close(); // Closing database connection
    }

    public void updateNamePhotoPricesByName(String id_presentation, String namePhoto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsPrices.NAME_PHOTO, namePhoto);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRICES, values1, "id_presentation='" + id_presentation + "'", null);
        db.close(); // Closing database connection
    }

    public void updateSurtPresentationRefillByName(String name, String falta) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsRefill.SURT, falta);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "presentation='" + name + "'", null);
        db.close(); // Closing database connection
    }

/*    public String getFinishedPresentationByName(String name) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS + " WHERE presentation='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }*/

    public String getFinishedPresentationSalesFloorByName(String id_store, String id_client, String id_presentation, String day) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_SALESFLOOR + " WHERE day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND id_presentation='" + id_presentation + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public String getLoc(String id_store, String day) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.LOC +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR + " WHERE day='" + day + "' AND id_stores='" + id_store + "' UNION SELECT loc FROM storescalendaradded WHERE day='" + day + "' AND id_stores='" + id_store + "'", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public String getFinishedPresentationWarehouseByName(String id_presentation, String id_store, String id_client, String day) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_WAREHOUSE + " WHERE day='" + day + "' AND id_presentation='" + id_presentation + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public String getFinishedPresentationPricesByName(String name) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPrices.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRICES + " WHERE presentation='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public String getFinishedPresentationRefillByName(String id_presentation, String id_store, String id_client, String day) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsRefill.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_REFILL + " WHERE day='" + day + "' AND id_presentation='" + id_presentation + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public String getSetPhotoPresentationByName(String id_store, String id_client, String id_presentation, String day) {
        String setPhoto = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.SETPHOTO +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_SALESFLOOR + "  WHERE day='" + day + "' AND id_presentation='" + id_presentation + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                setPhoto = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return setPhoto;
    }

    public String getSetPhotoPresentationWarehouseByName(String id_presentation, String id_store, String id_client, String day) {
        String setPhoto = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.SETPHOTO +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_WAREHOUSE + " WHERE day='" + day + "' AND id_presentation='" + id_presentation + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                setPhoto = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return setPhoto;
    }

    public String getSetPhotoPresentationPricesByName(String name) {
        String setPhoto = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPrices.SETPHOTO +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRICES + " WHERE presentation='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                setPhoto = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return setPhoto;
    }

    public String getModulesFinishedByName(String name) {
        String setPhoto = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsUtil.MODULES_FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_UTIL + " ; ", null);

        if (cursor.moveToFirst()) {
            do {
                setPhoto = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return setPhoto;
    }

    public List<Movable> getMovables() {
        List<Movable> listMovables = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsMovables.ID + " , " +
                EsquemaDBTradeFlow.NamesColumnsMovables.MOVABLE + " , " +
                EsquemaDBTradeFlow.NamesColumnsMovables.USER +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_MOVABLES + " ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Movable movable = new Movable();
                movable.setId_movable(cursor.getString(0));
                movable.setMovable(cursor.getString(1));
                movable.setUser(cursor.getString(2));

                listMovables.add(movable);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listMovables;
    }

    public String getModuleFinished(String id_modules, String id_client) {
        String setPhoto = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnModules.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_MODULES + " WHERE id_modules='" + id_modules + "' AND id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                setPhoto = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return setPhoto;
    }

    public String getClientsFinishedByName(String name) {
        String setPhoto = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsUtil.CLIENTS_FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_UTIL + " ; ", null);

        if (cursor.moveToFirst()) {
            do {
                setPhoto = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return setPhoto;
    }

    public String getSetPhotoPresentationRefillByName(String id_presentation, String id_store, String id_client, String day) {
        String setPhoto = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsRefill.SETPHOTO +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_REFILL + " WHERE day='" + day + "' AND id_presentation='" + id_presentation + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                setPhoto = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return setPhoto;
    }

    public void updateFinishedProductByName(String id_brand, String name, String finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED, finished);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS, values1, "id_brand='" + id_brand + "' AND product='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updateFinishedProductWarehouseByName(String id_brand, String name, String finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_WAREHOUSE, finished);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS, values1, "id_brand='" + id_brand + "'  AND product='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updateFinishedProductPricesByName(String id_brand, String name, String finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_PRICES, finished);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS, values1, "id_brand='" + id_brand + "' AND product='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updateFinishedProductExibRegularByName(String id_brand, String name, String finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_EXIBREGULAR, finished);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS, values1, "id_brand='" + id_brand + "' AND product='" + name + "'", null);
        db.close(); // Closing database connection
    }


    public void updateFinishedProductRefillByName(String id_brand, String name, String finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_REFILL, finished);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS, values1, "id_brand='" + id_brand + "' AND product='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public String getFinishedProductByName(String id_brand, String name) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS + " WHERE id_brand='" + id_brand + "' AND product='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public String getFinishedProductWarehouseByName(String id_brand, String name) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_WAREHOUSE +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS + " WHERE id_brand='" + id_brand + "' AND product='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public String getFinishedProductPricesByName(String id_brand, String name) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_PRICES +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS + " WHERE id_brand='" + id_brand + "' AND product='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public String getFinishedProductExibRegularByName(String id_brand, String name) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_EXIBREGULAR +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS + " WHERE id_brand='" + id_brand + "' AND product='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public String getFinishedProductRefillByName(String id_brand, String name) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_REFILL +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS + " WHERE id_brand='" + id_brand + "' AND product='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public void updateFinishedBrandSalesFloorByName(String id_client, String name, String finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED, finished);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_BRANDS, values1, "id_client='" + id_client + "' AND brand='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updateFinishedBrandWarehouseByName(String id_client, String name, String finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED_WAREHOUSE, finished);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_BRANDS, values1, "id_client='" + id_client + "' AND brand='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updateFinishedBrandPricesByName(String id_client, String name, String finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED_PRICES, finished);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_BRANDS, values1, "id_client='" + id_client + "' AND brand='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updateFinishedBrandExibRegularByName(String id_client, String name, String finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED_EXIBREGULAR, finished);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_BRANDS, values1, "id_client='" + id_client + "' AND brand='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updateFinishedBrandRefillByName(String id_client, String name, String finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED_REFILL, finished);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_BRANDS, values1, "id_client='" + id_client + "' AND brand='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public String getFinishedBrandSalesFloorByName(String id_client, String name) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_BRANDS + " WHERE id_client='" + id_client + "' AND brand='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public String getFinishedBrandWarehouseByName(String id_client, String name) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED_WAREHOUSE +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_BRANDS + " WHERE id_client='" + id_client + "' AND brand='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public String getFinishedBrandPricesByName(String id_client, String name) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED_PRICES +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_BRANDS + " WHERE id_client='" + id_client + "' AND brand='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public String getFinishedBrandExibRegularByName(String id_client, String name) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED_EXIBREGULAR +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_BRANDS + " WHERE id_client='" + id_client + "' AND brand='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public String getFinishedBrandRefillByName(String id_client, String name) {
        String finished = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsBrands.FINISHED_REFILL +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_BRANDS + " WHERE id_client='" + id_client + "' AND brand='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                finished = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return finished;
    }

    public void updateFinishedPresentationSalesFloorByName(String name, boolean finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        if (finished) {
            values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FINISHEDSALESFLOOR, "1");
        } else values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FINISHEDSALESFLOOR, "0");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "presentation='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updateFinishedPresentationWarehouseByName(String name, boolean finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        if (finished) {
            values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FINISHEDWAREHOUSE, "1");
        } else values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FINISHEDWAREHOUSE, "0");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "presentation='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updateFinishedPresentationRefillByName(String name, boolean finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        if (finished) {
            values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FINISHEDREFILL, "1");
        } else values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FINISHEDREFILL, "0");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "presentation='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updatePresentationSalesFloorById(String id_presentation, boolean setphoto, boolean finished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        if (setphoto) {
            values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.SETPHOTOSALESFLOOR, "1");
        } else values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.SETPHOTOSALESFLOOR, "0");

        if (finished) {
            values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FINISHEDSALESFLOOR, "1");
        } else values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FINISHEDSALESFLOOR, "0");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "id_presentation='" + id_presentation + "'", null);
        db.close(); // Closing database connection
    }

    public void setSalesFloorFinished() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.SALESFLOOR_FINISHED, "1");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_UTIL, values1, null, null);
        db.close(); // Closing database connection
    }

    //poner id_store tambien
    public void setModuleFinished(String id_modules, String id_store, String id_client, String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnModules.FINISHED, value);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_MODULES, values1, "id_modules='" + id_modules + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "'", null);
        db.close(); // Closing database connection
    }

    //poner id_store tambien
    public void setModuleFinishedByName(String module, String id_store, String id_client, String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnModules.FINISHED, value);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_MODULES, values1, "name='" + module + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "'", null);
        db.close(); // Closing database connection
    }

    public void updateWarehouseFinished(String id_modules, String id_client) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnModules.FINISHED, "1");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_MODULES, values1, "id_modules='" + id_modules + "' AND id_client='" + id_client + "'", null);
        db.close(); // Closing database connection
    }

/*    public void updateRefillFinished(String id_modules, String id_client) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnModules.WAREHOUSE_FINISHED, "1");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_REFILL, values1, "id_modules='" + id_modules + "' AND id_client='" + id_client + "'", null);
        db.close(); // Closing database connection
    }*/

/*
    public void updateSalesfloorFinished(String id_modules, String id_client) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnModules.SALESFLOOR_FINISHED, "1");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_MODULES, values1, "id_modules='" + id_modules + "' AND id_client='" + id_client + "'", null);
        db.close(); // Closing database connection
    }
*/

    public void setClientsFinished() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsUtil.CLIENTS_FINISHED, "1");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_UTIL, values1, null, null);
        db.close(); // Closing database connection
    }

    public void updateSetPhotoPresentationSalesFloorById(String id_presentation, boolean setphoto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        if (setphoto) {
            values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.SETPHOTOSALESFLOOR, "1");
        } else values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.SETPHOTOSALESFLOOR, "0");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "id_presentation='" + id_presentation + "'", null);
        db.close(); // Closing database connection
    }

    public void updateSetPhotoPresentationWarehouseById(String id_presentation, String setphoto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.SETEDPHOTOWAREHOUSE, setphoto);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "id_presentation='" + id_presentation + "'", null);
        db.close(); // Closing database connection
    }

    public void updateSetPhotoPresentationPricesById(String id_presentation, String setphoto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsPrices.SETPHOTO, setphoto);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRICES, values1, "id_presentation='" + id_presentation + "'", null);
        db.close(); // Closing database connection
    }

    public void updateSetPhotoPresentationRefillById(String id_presentation, String setphoto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.SETEDPHOTOREFILL, setphoto);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "id_presentation='" + id_presentation + "'", null);
        db.close(); // Closing database connection
    }

    public void updateSetPhotoPresentationRefillById(String id_presentation, boolean setphoto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        if (setphoto) {
            values1.put(EsquemaDBTradeFlow.NamesColumnsRefill.SETPHOTO, "1");
        } else values1.put(EsquemaDBTradeFlow.NamesColumnsRefill.SETPHOTO, "0");

        db.update(EsquemaDBTradeFlow.TABLE_NAME_REFILL, values1, "id_presentation='" + id_presentation + "'", null);
        db.close(); // Closing database connection
    }

    //adicionar alertas
    public void addAlertsSeparadas(String user, String id_store, String alert) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        //put the content
        values.put(EsquemaDBTradeFlow.NamesColumnAlertsSeparadas.ID_STORES, id_store);
        values.put(EsquemaDBTradeFlow.NamesColumnAlertsSeparadas.USER, user);
        values.put(EsquemaDBTradeFlow.NamesColumnAlertsSeparadas.ALERTS, alert);

        db.insert(EsquemaDBTradeFlow.TABLE_NAME_ALERTS_SEPARADAS, null, values);
        db.close(); // Closing database connection
    }

    // get id brand
    public ArrayList getIdOfBrand(String id_client, String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList listIds = new ArrayList();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRESENTATION +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP + " WHERE id_client='" + id_client + "' AND presentation='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                listIds.add(cursor.getString(0));
                listIds.add(cursor.getString(1));
                listIds.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listIds;
    }//fin addBrands

    public ArrayList getIdOfBrandSalesFloor(String id_client, String name, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList listIds = new ArrayList();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRESENTATION +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_SALESFLOOR + " WHERE day='" + day + "' AND id_client='" + id_client + "' AND presentation='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                listIds.add(cursor.getString(0));
                listIds.add(cursor.getString(1));
                listIds.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listIds;
    }//fin addBrands

    public ArrayList getIdOfBrandWarehouse(String id_client, String name, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList listIds = new ArrayList();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRESENTATION +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_WAREHOUSE + " WHERE day='" + day + "' AND id_client='" + id_client + "' AND presentation='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                listIds.add(cursor.getString(0));
                listIds.add(cursor.getString(1));
                listIds.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listIds;
    }//fin addBrands

    public ArrayList getIdOfBrandRefill(String id_client, String name, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList listIds = new ArrayList();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRESENTATION +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_REFILL + " WHERE day='" + day + "' AND id_client='" + id_client + "' AND presentation='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                listIds.add(cursor.getString(0));
                listIds.add(cursor.getString(1));
                listIds.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listIds;
    }//fin addBrands

    public ArrayList getIdOfBrandExibRegular(String id_client, String name, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList listIds = new ArrayList();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRESENTATION +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_EXIB_REGULAR + " WHERE day='" + day + "' AND id_client='" + id_client + "' AND presentation='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                listIds.add(cursor.getString(0));
                listIds.add(cursor.getString(1));
                listIds.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listIds;
    }//fin addBrands

    public ArrayList getIdOfBrandPrices(String id_client, String name, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList listIds = new ArrayList();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRESENTATION +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRICES + " WHERE day='" + day + "' AND id_client='" + id_client + "' AND presentation='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                listIds.add(cursor.getString(0));
                listIds.add(cursor.getString(1));
                listIds.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listIds;
    }//fin addBrands

    public ArrayList getIdOfBrandExibAditional(String id_client, String name, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList listIds = new ArrayList();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRESENTATION +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_EXIB_ADICIONAL + " WHERE day='" + day + "' AND id_client='" + id_client + "' AND presentation='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                listIds.add(cursor.getString(0));
                listIds.add(cursor.getString(1));
                listIds.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listIds;
    }//fin addBrands

    // get id brand
    public String getIdOfBrandByBrand(String id_client, String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String listIds = null;

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsBrands.ID_BRANDS +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_BRANDS + " WHERE id_client='" + id_client + "' AND brand='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                listIds = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listIds;
    }//fin addBrands

    public String getIdOfBrandByProduct(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String listIds = null;

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsProducts.ID_BRAND +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS + " WHERE product='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                listIds = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listIds;
    }//fin addBrands

    public String getModulesFinished(String id_modules, String id_client) {
        SQLiteDatabase db = this.getReadableDatabase();
        String modules = null;

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnModules.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_MODULES + " WHERE id_modules='" + id_modules + "' AND id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                modules = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return modules;
    }//fin addBrands

    public String getStoreCalendarFinished(String id_store, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        String modules = null;

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR + " WHERE id_stores='" + id_store + "' AND day='" + day + "'; ", null);

        if (cursor.moveToFirst()) {
            do {
                modules = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return modules;
    }//fin addBrands

    public void emptyTables(String id_client, String id_store, String id_brand) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED, "0");
        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_WAREHOUSE, "0");
        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_REFILL, "0");
        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_PRICES, "0");
        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_EXIBREGULAR, "0");
        values2.put(EsquemaDBTradeFlow.NamesColumnModules.FINISHED, "0");


        db.update(EsquemaDBTradeFlow.TABLE_NAME_BRANDS, values1, "id_client='" + id_client + "'", null);
        // db.update(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR, values2, "id_stores='" + id_store + "'", null);
        //db.update(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED, values2, "id_stores='" + id_store + "'", null);
        db.update(EsquemaDBTradeFlow.TABLE_NAME_MODULES, values2, "id_store='" + id_store + "' AND id_client='" + id_client + "'", null);
        db.update(EsquemaDBTradeFlow.TABLE_NAME_CLIENTS, values2, "id_client='" + id_client + "' AND id_stores='" + id_store + "'", null);
        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS, values1, "id_brand='" + id_brand + "'", null);


        db.close(); // Closing database connection
    }

    public void emptyRowsFinished() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED, "0");
        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_WAREHOUSE, "0");
        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_REFILL, "0");
        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_PRICES, "0");
        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED_EXIBREGULAR, "0");
        values2.put(EsquemaDBTradeFlow.NamesColumnModules.FINISHED, "0");


        db.update(EsquemaDBTradeFlow.TABLE_NAME_BRANDS, values1, null, null);
        db.update(EsquemaDBTradeFlow.TABLE_NAME_MODULES, values2, null, null);
        db.update(EsquemaDBTradeFlow.TABLE_NAME_CLIENTS, values2, null, null);
        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS, values1, null, null);


        db.close(); // Closing database connection
    }

    public String getStoreCalendarAddFinished(String id_store, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        String modules = null;

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED + " WHERE id_stores='" + id_store + "' AND day='" + day + "'; ", null);

        if (cursor.moveToFirst()) {
            do {
                modules = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return modules;
    }//fin addBrands

    public String getStoresCalendarFinished(String id_store, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        String modules = null;

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR + " WHERE id_stores='" + id_store + "' AND day='" + day + "' UNION SELECT finished FROM storescalendaradded WHERE id_stores='" + id_store + "' AND day='" + day + "'", null);

        if (cursor.moveToFirst()) {
            do {
                modules = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return modules;
    }//fin addBrands

    public String getStoresCalendarSent(String id_store, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        String modules = null;

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.SENT +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR + " WHERE id_stores='" + id_store + "' AND day='" + day + "' UNION SELECT sent FROM storescalendaradded WHERE id_stores='" + id_store + "' AND day='" + day + "'", null);

        if (cursor.moveToFirst()) {
            do {
                modules = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return modules;
    }//fin addBrands

    public ArrayList<String> getIdStoresCalendar(String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> modules = new ArrayList();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.ID_STORES +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR + " WHERE day='" + day + "' UNION SELECT id_stores FROM storescalendaradded WHERE day='" + day + "'", null);

        if (cursor.moveToFirst()) {
            do {
                modules.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return modules;
    }//fin addBrands

    public String getClientFinished(String id_client) {
        SQLiteDatabase db = this.getReadableDatabase();
        String modules = null;

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnClients.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_CLIENTS + " WHERE id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                modules = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return modules;
    }//fin addBrands

    public List<Module> getAllModulesFinished(String id_store, String id_client) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Module> listModules = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnModules.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_MODULES + " WHERE id_store='" + id_store + "' AND id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                //Log.e("CANT MODULOS","CANT MODULOS");
                Module module = new Module();
                module.setFinished(cursor.getString(0));
                listModules.add(module);
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        return listModules;
    }//fin addBrands

    public List<Client> getAllClientsFinished(String id_store) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Client> listClients = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnClients.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_CLIENTS + " WHERE id_stores='" + id_store + "' ; ", null);//agragar el dia tambien

        if (cursor.moveToFirst()) {
            do {
                Client store = new Client();
                store.setFinished(cursor.getString(0));
                listClients.add(store);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listClients;
    }//fin addBrands

    // get all brands
    public List<Brand> getBrands(String id_client) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Brand> listBrands = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsBrands.ID_BRANDS + ", " +
                EsquemaDBTradeFlow.NamesColumnsBrands.ID_CLIENTS + ", " +
                EsquemaDBTradeFlow.NamesColumnsBrands.BRAND +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_BRANDS + " WHERE id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Brand brand = new Brand();
                brand.setId_brand(cursor.getString(0));
                brand.setId_Client(cursor.getString(1));
                brand.setBrand(cursor.getString(2));
                listBrands.add(brand);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listBrands;
    }//fin addBrands

    // get all brands
    public List<Brand> getBrands(String user, String id_client) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Brand> listBrands = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsBrands.ID_BRANDS + ", " +
                EsquemaDBTradeFlow.NamesColumnsBrands.ID_CLIENTS + ", " +
                EsquemaDBTradeFlow.NamesColumnsBrands.BRAND +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_BRANDS + " WHERE user='" + user + "' AND id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Brand brand = new Brand();
                brand.setId_brand(cursor.getString(0));
                brand.setId_Client(cursor.getString(1));
                brand.setBrand(cursor.getString(2));
                listBrands.add(brand);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listBrands;
    }//fin addBrands

    // get all products
    public List<Product> getProducts(String user, String id_brand) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Product> listProducts = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsProducts.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsProducts.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsProducts.PRODUCT +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS + " WHERE user='" + user + "' AND id_brand='" + id_brand + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId_product(cursor.getString(0));
                product.setId_brand(cursor.getString(1));
                product.setProduct(cursor.getString(2));
                listProducts.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listProducts;
    }//fin addProducts

    // get all products
    public ArrayList getIdStoreCalendarFinished(String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList listProducts = new ArrayList();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.ID_STORES +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR + " WHERE finished='1' AND day='" + day + "'; ", null);

        if (cursor.moveToFirst()) {
            do {
                listProducts.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listProducts;
    }//fin addProducts


    // get all products
    public ArrayList getIdStoreCalendarSent(String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList listProducts = new ArrayList();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.ID_STORES +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR + " WHERE sent='0' AND finished='1' AND day='" + day + "'; ", null);

        if (cursor.moveToFirst()) {
            do {
                listProducts.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listProducts;
    }//fin addProducts

    public ArrayList<String> getIdStoresToSent(String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> listProducts = new ArrayList();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.ID_STORES +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR + " WHERE sent='0' AND finished='1' AND day='" + day + "' UNION SELECT id_stores FROM storescalendaradded WHERE sent='0' AND finished='1' AND day='" + day + "'", null);

        if (cursor.moveToFirst()) {
            do {
                listProducts.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listProducts;
    }//fin addProducts

    public ArrayList<String> getStoresSent(String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> modules = new ArrayList();

        Cursor cursor = db.rawQuery("SELECT sent FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR + " WHERE day='" + day + "' UNION SELECT sent FROM storescalendaradded WHERE day='" + day + "'", null);

        if (cursor.moveToFirst()) {
            do {
                modules.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return modules;
    }//fin addProducts

    // get all products
    public ArrayList getIdStoreCalendarAddFinished() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList listProducts = new ArrayList();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.ID_STORES +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED + " WHERE finished='1' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                listProducts.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listProducts;
    }//fin addProducts

    // get all presentations
    public List<Presentation> getPresentations(String user, String id_brand, String id_product) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Presentation> listPresentations = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.FALTA + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.OK + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.HASPHOTO + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.SETPHOTOSALESFLOOR + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.FINISHEDSALESFLOOR +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS + " WHERE user='" + user + "' AND id_brand='" + id_brand + "' AND id_product='" + id_product + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Presentation presentation = new Presentation();
                presentation.setId_presentation(cursor.getString(0));
                presentation.setId_product(cursor.getString(1));
                presentation.setId_brand(cursor.getString(2));
                presentation.setPresentation(cursor.getString(3));
                presentation.setFalta(cursor.getString(4));
                presentation.setOk(cursor.getString(5));
                presentation.setHasPhoto(cursor.getString(6));
                presentation.setSetPhoto(cursor.getString(7));
                presentation.setFinished(cursor.getString(8));
                listPresentations.add(presentation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listPresentations;
    }//fin getPresentations

    public List<PresentationCompetence> getPresentationsCompetence(String id_brand) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<PresentationCompetence> listPresentations = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnspPresentationsCompetence.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnspPresentationsCompetence.ID + ", " +
                EsquemaDBTradeFlow.NamesColumnspPresentationsCompetence.BRAND_COMPETENCE + ", " +
                EsquemaDBTradeFlow.NamesColumnspPresentationsCompetence.PRESENTATION_COMPETENCE +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_COMPENTECE + " WHERE id_brand='" + id_brand + "' ", null);

        if (cursor.moveToFirst()) {
            do {
                PresentationCompetence presentation = new PresentationCompetence();
                presentation.setId_brand(cursor.getString(0));
                presentation.setId_presentation(cursor.getString(1));
                presentation.setBrand_competence(cursor.getString(2));
                presentation.setPresentation_competence(cursor.getString(3));
                listPresentations.add(presentation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listPresentations;
    }//fin getPresentations

    // get all presentations
    public List<Presentation> getSalesFloorForSend(String id_store, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Presentation> listPresentations = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.USER + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.FALTA + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.OK + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.NAME_PHOTO + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_STORE + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.CHECK_IN + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.CHECK_OUT + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.LOC + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_CLIENT +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_SALESFLOOR + " " +
                "WHERE finished='1' AND id_store='" + id_store + "' AND day='" + day + "' ", null);

        if (cursor.moveToFirst()) {
            do {
                Presentation presentation = new Presentation();
                presentation.setUser(cursor.getString(0));
                presentation.setId_presentation(cursor.getString(1));
                presentation.setId_product(cursor.getString(2));
                presentation.setId_brand(cursor.getString(3));
                presentation.setPresentation(cursor.getString(4));
                presentation.setFalta(cursor.getString(5));
                presentation.setOk(cursor.getString(6));
                presentation.setName_photo(cursor.getString(7));
                presentation.setId_store(cursor.getString(8));
                presentation.setCheck_in(cursor.getString(9));
                presentation.setCheck_out(cursor.getString(10));
                presentation.setLoc(cursor.getString(11));
                presentation.setId_client(cursor.getString(12));
                listPresentations.add(presentation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listPresentations;
    }//fin getPresentations

    // get all presentations
    public List<Presentation> getPricesForSend(String id_store, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Presentation> listPresentations = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPrices.USER + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.ID_PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.PRICE + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.NO_HAY + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.NAME_PHOTO + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.ID_STORE + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.CHECK_IN + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.CHECK_OUT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.LOC + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.ID_CLIENT +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRICES + " " +
                "WHERE finished='1' AND id_store='" + id_store + "' AND day='" + day + "' ", null);

        if (cursor.moveToFirst()) {
            do {
                Presentation presentation = new Presentation();
                presentation.setUser(cursor.getString(0));
                presentation.setId_presentation(cursor.getString(1));
                presentation.setId_product(cursor.getString(2));
                presentation.setId_brand(cursor.getString(3));
                presentation.setPresentation(cursor.getString(4));
                presentation.setPrice(cursor.getString(5));
                presentation.setNo_hay(cursor.getString(6));
                presentation.setName_photo(cursor.getString(7));
                presentation.setId_store(cursor.getString(8));
                presentation.setCheck_in(cursor.getString(9));
                presentation.setCheck_out(cursor.getString(10));
                presentation.setLoc(cursor.getString(11));
                presentation.setId_client(cursor.getString(12));
                listPresentations.add(presentation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listPresentations;
    }//fin getPresentations

    // get all presentations
    public List<Presentation> getExibRegularForSend(String id_store, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Presentation> listPresentations = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.USER + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.ID_PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.ASIG + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.TOTAL + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.PORCENT + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.NAME_PHOTO + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.ID_STORE + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.CHECK_IN + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.CHECK_OUT + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.LOC + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.ID_CLIENT +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_EXIB_REGULAR + " " +
                "WHERE finished='1' AND id_store='" + id_store + "' AND day='" + day + "' ", null);

        if (cursor.moveToFirst()) {
            do {
                Presentation presentation = new Presentation();
                presentation.setUser(cursor.getString(0));
                presentation.setId_presentation(cursor.getString(1));
                presentation.setId_product(cursor.getString(2));
                presentation.setId_brand(cursor.getString(3));
                presentation.setPresentation(cursor.getString(4));
                presentation.setAsig(cursor.getString(5));
                presentation.setTotal(cursor.getString(6));
                presentation.setPorcent(cursor.getString(7));
                presentation.setName_photo(cursor.getString(8));
                presentation.setId_store(cursor.getString(9));
                presentation.setCheck_in(cursor.getString(10));
                presentation.setCheck_out(cursor.getString(11));
                presentation.setLoc(cursor.getString(12));
                presentation.setId_client(cursor.getString(13));
                listPresentations.add(presentation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listPresentations;
    }//fin getPresentations

    // get all presentations
    public List<Presentation> getWarehouseForSend(String id_store, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Presentation> listPresentations = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.USER + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.ID_PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.WAREHOUSE + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.PRE + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.NAME_PHOTO + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.ID_STORE + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.CHECK_IN + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.CHECK_OUT + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.LOC + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.ID_CLIENT +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_WAREHOUSE + " " +
                "WHERE finished='1' AND id_store='" + id_store + "' AND day='" + day + "' ", null);

        if (cursor.moveToFirst()) {
            do {
                Presentation presentation = new Presentation();
                presentation.setUser(cursor.getString(0));
                presentation.setId_presentation(cursor.getString(1));
                presentation.setId_product(cursor.getString(2));
                presentation.setId_brand(cursor.getString(3));
                presentation.setPresentation(cursor.getString(4));
                presentation.setPrice(cursor.getString(5));
                presentation.setNo_hay(cursor.getString(6));
                presentation.setName_photo(cursor.getString(7));
                presentation.setId_store(cursor.getString(8));
                presentation.setCheck_in(cursor.getString(9));
                presentation.setCheck_out(cursor.getString(10));
                presentation.setLoc(cursor.getString(11));
                presentation.setId_client(cursor.getString(12));
                listPresentations.add(presentation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listPresentations;
    }//fin getPresentations

    // get all presentations
    public List<Presentation> getRefillForSend(String id_store, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Presentation> listPresentations = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsRefill.USER + ", " +
                EsquemaDBTradeFlow.NamesColumnsRefill.ID_PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsRefill.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsRefill.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsRefill.PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsRefill.WAREHOUSE + ", " +
                EsquemaDBTradeFlow.NamesColumnsRefill.SURT + ", " +
                EsquemaDBTradeFlow.NamesColumnsRefill.NAME_PHOTO + ", " +
                EsquemaDBTradeFlow.NamesColumnsRefill.ID_STORE + ", " +
                EsquemaDBTradeFlow.NamesColumnsRefill.CHECK_IN + ", " +
                EsquemaDBTradeFlow.NamesColumnsRefill.CHECK_OUT + ", " +
                EsquemaDBTradeFlow.NamesColumnsRefill.LOC + ", " +
                EsquemaDBTradeFlow.NamesColumnsRefill.ID_CLIENT +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_REFILL + " " +
                "WHERE finished='1' AND id_store='" + id_store + "' AND day='" + day + "' ", null);

        if (cursor.moveToFirst()) {
            do {
                Presentation presentation = new Presentation();
                presentation.setUser(cursor.getString(0));
                presentation.setId_presentation(cursor.getString(1));
                presentation.setId_product(cursor.getString(2));
                presentation.setId_brand(cursor.getString(3));
                presentation.setPresentation(cursor.getString(4));
                presentation.setPrice(cursor.getString(5));
                presentation.setNo_hay(cursor.getString(6));
                presentation.setName_photo(cursor.getString(7));
                presentation.setId_store(cursor.getString(8));
                presentation.setCheck_in(cursor.getString(9));
                presentation.setCheck_out(cursor.getString(10));
                presentation.setLoc(cursor.getString(11));
                presentation.setId_client(cursor.getString(12));
                listPresentations.add(presentation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listPresentations;
    }//fin getPresentations

    // get all presentations
    public List<Movable> getExibAditionalForSend(String id_store, String day) {
        List<Movable> listMovables = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_MOVABLE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.USER + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_STORE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_CLIENT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.ID_BRAND + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.MOVABLE + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CANT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.LOC + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.DATE_TIME + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.SETPHOTO + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CHECK_IN + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.CHECK_OUT + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.NAME_PHOTO + " , " +
                EsquemaDBTradeFlow.NamesColumnsExibAditional.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_EXIB_ADICIONAL + " " +
                "WHERE finished='1' AND id_store='" + id_store + "' AND day='" + day + "' ", null);

        if (cursor.moveToFirst()) {
            do {
                Movable movable = new Movable();
                movable.setId_movable(cursor.getString(0));
                movable.setUser(cursor.getString(1));
                movable.setId_store(cursor.getString(2));
                movable.setId_client(cursor.getString(3));
                movable.setId_brand(cursor.getString(4));
                movable.setMovable(cursor.getString(5));
                movable.setCant(cursor.getString(6));
                movable.setLoc(cursor.getString(7));
                movable.setDate_time(cursor.getString(8));
                movable.setSetPhoto(cursor.getString(9));
                movable.setCheck_in(cursor.getString(10));
                movable.setCheck_out(cursor.getString(11));
                movable.setName_photo(cursor.getString(12));
                movable.setFinished(cursor.getString(13));

                listMovables.add(movable);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listMovables;
    }//fin getPresentations

    public void setSent(String id_client, String id_store, String id_brand) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsProducts.FINISHED, "0");


        db.update(EsquemaDBTradeFlow.TABLE_NAME_BRANDS, values1, "id_client='" + id_client + "'", null);
        db.update(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR, values1, "id_stores='" + id_store + "'", null);
        db.update(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED, values1, "id_stores='" + id_store + "'", null);
        db.update(EsquemaDBTradeFlow.TABLE_NAME_MODULES, values1, "id_client='" + id_client + "'", null);
        db.update(EsquemaDBTradeFlow.TABLE_NAME_CLIENTS, values1, "id_client='" + id_client + "' AND id_stores='" + id_store + "'", null);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRODUCTS, values1, "id_brand='" + id_brand + "'", null);


        db.close(); // Closing database connection
    }

    public List<Presentation> getFaltaOrOkPresentationByName(String name, String id_store, String id_client, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Presentation> listPresentations = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.FALTA + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.OK +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_SALESFLOOR + " WHERE day='" + day + "' AND id_store='" + id_store + "'" + " AND id_client='" + id_client + "' AND presentation='" + name + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Presentation presentation = new Presentation();
                presentation.setId_presentation(cursor.getString(0));
                presentation.setId_product(cursor.getString(1));
                presentation.setId_brand(cursor.getString(2));
                presentation.setPresentation(cursor.getString(3));
                presentation.setFalta(cursor.getString(4));
                presentation.setOk(cursor.getString(5));
                listPresentations.add(presentation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listPresentations;
    }

    // get all presentations
    public List<Presentation> getPresentationsForWarehouse(String day, String id_store, String id_client, String id_brand, String id_product) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Presentation> listPresentations = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.HASPHOTO +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_SALESFLOOR + " WHERE day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND id_brand='" + id_brand + "' AND id_product='" + id_product + "' AND falta='1' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                //Log.e("HAS PHOTO DB", cursor.getString(3) + ", " + cursor.getString(4));
                Presentation presentation = new Presentation();
                presentation.setId_presentation(cursor.getString(0));
                presentation.setId_product(cursor.getString(1));
                presentation.setId_brand(cursor.getString(2));
                presentation.setPresentation(cursor.getString(3));
                presentation.setHasPhoto(cursor.getString(4));
                listPresentations.add(presentation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listPresentations;
    }//fin getPresentations

    // get all presentations
    public List<Presentation> getCantPresentationsForWarehouse(String day, String id_store, String id_client) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Presentation> listPresentations = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsSalesFloor.HASPHOTO +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_SALESFLOOR + " WHERE day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND falta='1' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                //Log.e("HAS PHOTO DB", cursor.getString(3) + ", " + cursor.getString(4));
                Presentation presentation = new Presentation();
                presentation.setId_presentation(cursor.getString(0));
                presentation.setId_product(cursor.getString(1));
                presentation.setId_brand(cursor.getString(2));
                presentation.setPresentation(cursor.getString(3));
                presentation.setHasPhoto(cursor.getString(4));
                listPresentations.add(presentation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listPresentations;
    }//fin getPresentations


    public List<Presentation> getPresentationsFromSalesFloor(String user, String id_brand, String id_product) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Presentation> listPresentations = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsPresentations.PRESENTATION +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_SALESFLOOR + " WHERE user='" + user + "' AND id_brand='" + id_brand + "' AND id_product='" + id_product + "' AND falta='1' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                //Log.e("HAS PHOTO DB", cursor.getString(3) + ", " + cursor.getString(4));
                Presentation presentation = new Presentation();
                presentation.setId_presentation(cursor.getString(0));
                presentation.setId_product(cursor.getString(1));
                presentation.setId_brand(cursor.getString(2));
                presentation.setPresentation(cursor.getString(3));
                listPresentations.add(presentation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listPresentations;
    }//fin getPresentations


    // get all presentations for refill
    public List<Presentation> getPresentationsForRefill(String day, String id_store, String id_client, String id_brand, String id_product) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Presentation> listPresentations = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.ID_PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.HASPHOTO +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_WAREHOUSE + " WHERE day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND id_brand='" + id_brand + "' AND id_product='" + id_product + "' AND warehouse!='0' AND warehouse!=''; ", null);

        if (cursor.moveToFirst()) {
            do {
                Presentation presentation = new Presentation();
                presentation.setId_presentation(cursor.getString(0));
                presentation.setId_product(cursor.getString(1));
                presentation.setId_brand(cursor.getString(2));
                presentation.setPresentation(cursor.getString(3));
                presentation.setHasPhoto(cursor.getString(4));
                listPresentations.add(presentation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listPresentations;
    }//fin getPresentationsForRefill

    // get all alerts
    public List<AlertSeparada> getAlertsSeparadas(String user, String id_stores) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<AlertSeparada> listAlerts = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnAlertsSeparadas.ID_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnAlertsSeparadas.USER + ", " +
                EsquemaDBTradeFlow.NamesColumnAlertsSeparadas.ALERTS +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_ALERTS + " WHERE user='" + user + "' AND id_stores='" + id_stores + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                AlertSeparada alert = new AlertSeparada();
                alert.setId_store(cursor.getString(0));
                alert.setUser(cursor.getString(1));
                alert.setAlert(cursor.getString(2));
                listAlerts.add(alert);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listAlerts;
    }//fin addAlerts


    // get getDataWarehouse
    public String[] getDataWarehouse(String id_store, String id_client, String presentation, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] listWarehouse = new String[3];

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.WAREHOUSE + ", " +
                EsquemaDBTradeFlow.NamesColumnsWarehouse.PRE +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_WAREHOUSE + " WHERE day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND presentation='" + presentation + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                listWarehouse[0] = cursor.getString(0);
                listWarehouse[1] = cursor.getString(1);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listWarehouse;
    }//fin getDataWarehouse


    // get getDataWarehouse
    public Price getDataPrices(String id_store, String id_client, String id_presentation, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        Price price = new Price();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsPrices.USER + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.ID_STORE + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.ID_CLIENT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.ID_PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.PRICE + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.NO_HAY + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.LOC + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.SETPHOTO + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.CHECK_IN + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.CHECK_OUT + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.NAME_PHOTO + ", " +
                EsquemaDBTradeFlow.NamesColumnsPrices.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_PRICES + " WHERE day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND id_presentation='" + id_presentation + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                price.setUser(cursor.getString(0));
                price.setId_store(cursor.getString(1));
                price.setId_client(cursor.getString(2));
                price.setId_brand(cursor.getString(3));
                price.setId_product(cursor.getString(4));
                price.setId_presentation(cursor.getString(5));
                price.setPresentation(cursor.getString(6));
                price.setPrice(cursor.getString(7));
                price.setNo_hay(cursor.getString(8));
                price.setLoc(cursor.getString(9));
                price.setSetPhoto(cursor.getString(10));
                price.setCheck_in(cursor.getString(11));
                price.setCheck_out(cursor.getString(12));
                price.setName_photo(cursor.getString(13));
                price.setFinished(cursor.getString(14));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return price;
    }//fin getDataWarehouse

    // get getDataWarehouse
    public ExibRegular getDataExibRegular(String id_presentation, String id_store, String id_client, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        ExibRegular price = new ExibRegular();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.USER + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.ID_STORE + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.ID_CLIENT + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.ID_BRAND + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.ID_PRODUCT + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.ID_PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.PRESENTATION + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.ASIG + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.TOTAL + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.PORCENT + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.LOC + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.SETPHOTO + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.CHECK_IN + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.CHECK_OUT + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.NAME_PHOTO + ", " +
                EsquemaDBTradeFlow.NamesColumnsExibRegular.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_EXIB_REGULAR + " WHERE day='" + day + "' AND id_presentation='" + id_presentation + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                price.setUser(cursor.getString(0));
                price.setId_store(cursor.getString(1));
                price.setId_client(cursor.getString(2));
                price.setId_brand(cursor.getString(3));
                price.setId_product(cursor.getString(4));
                price.setId_presentation(cursor.getString(5));
                price.setPresentation(cursor.getString(6));
                price.setAsig(cursor.getString(7));
                price.setTotal(cursor.getString(8));
                price.setPorcent(cursor.getString(9));
                price.setLoc(cursor.getString(10));
                price.setSetPhoto(cursor.getString(11));
                price.setCheck_in(cursor.getString(12));
                price.setCheck_out(cursor.getString(13));
                price.setName_photo(cursor.getString(14));
                price.setFinished(cursor.getString(15));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return price;
    }//fin getDataWarehouse

    // get getDataWarehouse
    public String getSurtRefill(String id_presentation, String id_store, String id_client, String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        String listWarehouse = null;

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnsRefill.SURT +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_REFILL + " WHERE day='" + day + "' AND id_store='" + id_store + "' AND id_client='" + id_client + "' AND id_presentation='" + id_presentation + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                listWarehouse = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listWarehouse;
    }//fin getDataWarehouse

    // get getDataWarehouse
    public String getFinishedModuleById(String id_store, String id_module) {
        SQLiteDatabase db = this.getReadableDatabase();
        String listWarehouse = null;

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnModules.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_MODULES + " WHERE id_store='" + id_store + "' AND id_modules='" + id_module + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                listWarehouse = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listWarehouse;
    }//fin getDataWarehouse

    //inicio addNews
    public void addNews(String user, String id_client, String id_store, String news) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        //put the content
        values.put(EsquemaDBTradeFlow.NamesColumnNews.USER, user);
        values.put(EsquemaDBTradeFlow.NamesColumnNews.ID_CLIENT, id_client);
        values.put(EsquemaDBTradeFlow.NamesColumnNews.ID_STORES, id_store);
        values.put(EsquemaDBTradeFlow.NamesColumnNews.NEWS, news);

        db.insert(EsquemaDBTradeFlow.TABLE_NAME_NEWS, null, values);
        db.close(); // Closing database connection
    }//fin addNews

    // get all news
    public List<News> getNews(String user, String id_client, String id_stores) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<News> listNews = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnNews.USER + ", " +
                EsquemaDBTradeFlow.NamesColumnNews.ID_CLIENT + ", " +
                EsquemaDBTradeFlow.NamesColumnNews.ID_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnNews.NEWS +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_NEWS + " WHERE user='" + user + "' AND id_client='" + id_client + "' AND id_stores='" + id_stores + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                News news = new News();
                news.setUser(cursor.getString(0));
                news.setId_client(cursor.getString(1));
                news.setId_store(cursor.getString(2));
                news.setNews(cursor.getString(3));
                listNews.add(news);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listNews;
    }//fin getNews

    //adicionar clientes
    public void addClients(String user, String id_client, String id_store, String name, String logo) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        //put the content
        values.put(EsquemaDBTradeFlow.NamesColumnClients.ID_CLIENTS, id_client);
        values.put(EsquemaDBTradeFlow.NamesColumnClients.ID_STORES, id_store);
        values.put(EsquemaDBTradeFlow.NamesColumnClients.USER, user);
        values.put(EsquemaDBTradeFlow.NamesColumnClients.NAME, name);
        values.put(EsquemaDBTradeFlow.NamesColumnClients.LOGO, logo);
        values.put(EsquemaDBTradeFlow.NamesColumnClients.FINISHED, "0");

        db.insert(EsquemaDBTradeFlow.TABLE_NAME_CLIENTS, null, values);
        db.close(); // Closing database connection
    }

    // obtener clientes
    public List<Client> getClients(String user, String id_stores) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Client> listClients = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnClients.ID_CLIENTS + ", " +
                EsquemaDBTradeFlow.NamesColumnClients.ID_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnClients.USER + ", " +
                EsquemaDBTradeFlow.NamesColumnClients.NAME + ", " +
                EsquemaDBTradeFlow.NamesColumnClients.LOGO + ", " +
                EsquemaDBTradeFlow.NamesColumnClients.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_CLIENTS + " WHERE user='" + user + "' AND id_stores='" + id_stores + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Client client = new Client();
                client.setId_client(cursor.getString(0));
                client.setId_store(cursor.getString(1));
                client.setUser(cursor.getString(2));
                client.setName(cursor.getString(3));
                client.setLogo(cursor.getString(4));
                client.setFinished(cursor.getString(5));
                listClients.add(client);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listClients;
    }

    //adicionar modulos
    public void addModules(String user, String id_store, String id_module, String id_client, String name, String logo) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        //put the content
        values.put(EsquemaDBTradeFlow.NamesColumnModules.USER, user);
        values.put(EsquemaDBTradeFlow.NamesColumnModules.ID_STORE, id_store);
        values.put(EsquemaDBTradeFlow.NamesColumnModules.ID_CLIENT, id_client);
        values.put(EsquemaDBTradeFlow.NamesColumnModules.ID_MODULES, id_module);
        values.put(EsquemaDBTradeFlow.NamesColumnModules.NAME, name);
        values.put(EsquemaDBTradeFlow.NamesColumnModules.LOGO, logo);
        values.put(EsquemaDBTradeFlow.NamesColumnModules.FINISHED, "0");

        db.insert(EsquemaDBTradeFlow.TABLE_NAME_MODULES, null, values);
        db.close(); // Closing database connection
    }

    // obtener modulos
    public List<Module> getModules(String id_store, String id_client) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Module> listModules = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnModules.ID_MODULES + ", " +
                EsquemaDBTradeFlow.NamesColumnModules.ID_CLIENT + ", " +
                EsquemaDBTradeFlow.NamesColumnModules.USER + ", " +
                EsquemaDBTradeFlow.NamesColumnModules.NAME + ", " +
                EsquemaDBTradeFlow.NamesColumnModules.LOGO + ", " +
                EsquemaDBTradeFlow.NamesColumnModules.FINISHED +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_MODULES + " WHERE id_store='" + id_store + "' AND id_client='" + id_client + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Module module = new Module();
                module.setId_module(cursor.getString(0));
                module.setId_client(cursor.getString(1));
                module.setUser(cursor.getString(2));
                module.setName(cursor.getString(3));
                module.setLogo(cursor.getString(4));
                module.setFinished(cursor.getString(5));
                listModules.add(module);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listModules;
    }//fin get modules

    // Adding tiendas por calendario
    public void addStoresCalendario(String user, String id_stores, String name, String address, String calle, String noext, String colonia, String municipio, String string, String format, String day) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EsquemaDBTradeFlow.NamesColumnStores.USER, user);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.ID_STORES, id_stores);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.NAME_STORES, name);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.ADDRESS_STORES, address);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.CALLE_STORES, calle);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.NOEXT_STORES, noext);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.COLONIA_STORES, colonia);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.MUNICIPIO_STORES, municipio);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.STRING_STORES, string);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.FORMAT_STORES, format);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.DAY, day);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.TAC, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnStores.FINISHED, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnStores.SENT, "0");

        // Inserting Row
        db.insert(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR, null, values);
        db.close(); // Closing database connection
    }

    // Adding new User
    public void addUser(String user, String pass, String remember) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT OR REPLACE INTO users" +
                "(_id,user,pass,remember)" +
                " VALUES ('1', '" + user + "', '" + pass + "', '" + remember + "') ");
        db.close(); // Closing database connection
    }

    // Getting Users
    public String[] getUserAndPass() {
        String[] userAndPass = new String[3];
        SQLiteDatabase db = this.getReadableDatabase();
        List<User> listUsers = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " + EsquemaDBTradeFlow.NamesColumnUsers.USER + ", " +
                EsquemaDBTradeFlow.NamesColumnUsers.PASS + ", " +
                EsquemaDBTradeFlow.NamesColumnUsers.REMEMBER + " FROM " +
                EsquemaDBTradeFlow.TABLE_NAME_USERS + "; ", null);

        if (cursor.moveToFirst()) {
            do {
                userAndPass[0] = cursor.getString(0);
                userAndPass[1] = cursor.getString(1);
                userAndPass[2] = cursor.getString(2);
                /*User users = new User();
                users.setUser(cursor.getString(0));
                users.setPass(cursor.getString(1));
                listUsers.add(users);*/

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return userAndPass;
    }

    // Getting Users
    public String getRememberUserAndPass() {
        String remember = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + EsquemaDBTradeFlow.NamesColumnUsers.REMEMBER + " FROM " +
                EsquemaDBTradeFlow.TABLE_NAME_USERS + "; ", null);

        if (cursor.moveToFirst()) {
            do {
                remember = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return remember;
    }

    // Adding new store to calendar
    public void addStoreToCalendar(String user, String id_stores, String name, String address, String calle, String noext, String colonia, String municipio, String string, String format, String day) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EsquemaDBTradeFlow.NamesColumnStores.USER, user);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.ID_STORES, id_stores);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.NAME_STORES, name);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.ADDRESS_STORES, address);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.CALLE_STORES, calle);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.NOEXT_STORES, noext);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.COLONIA_STORES, colonia);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.MUNICIPIO_STORES, municipio);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.STRING_STORES, string);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.FORMAT_STORES, format);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.DAY, day);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.TAC, "1");
        values.put(EsquemaDBTradeFlow.NamesColumnStores.FINISHED, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnStores.SENT, "0");

        // Inserting Row
        db.insert(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED, null, values);
        db.close(); // Closing database connection
    }

    // Adding new store states
    public void addStoresPlaza(String user, String id_stores, String name, String address, String calle, String noext, String colonia, String municipio, String string, String format) {
        //instance from db to write on this
        SQLiteDatabase db = this.getWritableDatabase();

        //content to fill the bd
        ContentValues values = new ContentValues();

        //put the content
        values.put(EsquemaDBTradeFlow.NamesColumnStores.USER, user);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.ID_STORES, id_stores);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.NAME_STORES, name);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.ADDRESS_STORES, address);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.CALLE_STORES, calle);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.NOEXT_STORES, noext);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.COLONIA_STORES, colonia);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.MUNICIPIO_STORES, municipio);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.STRING_STORES, string);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.FORMAT_STORES, format);
        values.put(EsquemaDBTradeFlow.NamesColumnStores.TAC, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnStores.FINISHED, "0");
        values.put(EsquemaDBTradeFlow.NamesColumnStores.SENT, "0");

        // Inserting Row
        db.insert(EsquemaDBTradeFlow.TABLE_NAME_STORES_PLAZA, null, values);
        // Closing database connection
        db.close();
    }


    public void saveLocation(String name, String loc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnStores.LOC, loc);
        values2.put(EsquemaDBTradeFlow.NamesColumnStores.LOC, loc);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR, values1, "name='" + name + "'", null);
        db.update(EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED, values2, "name='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void updateFaltaPresentation(String id_presentation, String falta) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values1 = new ContentValues();

        values1.put(EsquemaDBTradeFlow.NamesColumnsPresentations.FALTA, falta);

        db.update(EsquemaDBTradeFlow.TABLE_NAME_PRESENTATIONS_TEMP, values1, "id_presentation='" + id_presentation + "'", null);
        db.close(); // Closing database connection
    }

    // Getting Stores per calendar(tiendas a mostrar del calendario de la semnana)
    public List<Store> knowIfExistStoreCalendar(String user, String dia) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Store> listStores = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.ID_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.NAME_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.ADDRESS_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.CALLE_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.NOEXT_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.COLONIA_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.MUNICIPIO_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.STRING_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.FORMAT_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.DAY + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.TAC +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR + " WHERE user='" + user + "' AND day='" + dia + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Store stores = new Store();
                stores.setId_store(cursor.getString(0));
                stores.setName(cursor.getString(1));
                stores.setAddress(cursor.getString(2));
                stores.setCalle(cursor.getString(3));
                stores.setNo_ext(cursor.getString(4));
                stores.setColonia(cursor.getString(5));
                stores.setMunicipio(cursor.getString(6));
                stores.setCadena(cursor.getString(7));
                stores.setFormato(cursor.getString(8));
                stores.setDia(cursor.getString(9));
                stores.setTac(cursor.getString(10));
                //Log.e("STATUSTIENDACALEDNARIO> ", cursor.getString(10));
                listStores.add(stores);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listStores;
    }

    // Getting Stores per calendar(tiendas a mostrar del calendario de la semnana)
    public List<Store> knowIfExistStoreCalendarAdded(String user, String dia) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Store> listStores = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.ID_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.NAME_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.ADDRESS_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.CALLE_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.NOEXT_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.COLONIA_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.MUNICIPIO_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.STRING_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.FORMAT_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.DAY + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.TAC +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED + " WHERE user='" + user + "' AND day='" + dia + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Store stores = new Store();
                stores.setId_store(cursor.getString(0));
                stores.setName(cursor.getString(1));
                stores.setAddress(cursor.getString(2));
                stores.setCalle(cursor.getString(3));
                stores.setNo_ext(cursor.getString(4));
                stores.setColonia(cursor.getString(5));
                stores.setMunicipio(cursor.getString(6));
                stores.setCadena(cursor.getString(7));
                stores.setFormato(cursor.getString(8));
                stores.setDia(cursor.getString(9));
                stores.setTac(cursor.getString(10));
                //Log.e("STATUSTIENDACALEDNARIO> ", cursor.getString(10));
                listStores.add(stores);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listStores;
    }


    // Getting Stores per calendar(tiendas a mostrar del calendario de la semnana)
    public List<Store> getStoresCalendario(String user, String dia) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Store> listStores = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.ID_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.NAME_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.ADDRESS_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.CALLE_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.NOEXT_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.COLONIA_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.MUNICIPIO_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.STRING_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.FORMAT_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.DAY + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.TAC + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.FINISHED + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.SENT +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR + " WHERE user='" + user + "' AND day='" + dia + "' UNION SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.ID_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.NAME_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.ADDRESS_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.CALLE_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.NOEXT_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.COLONIA_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.MUNICIPIO_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.STRING_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.FORMAT_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.DAY + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.TAC + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.FINISHED + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.SENT +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED + " WHERE user='" + user + "' AND day='" + dia + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Store stores = new Store();
                stores.setId_store(cursor.getString(0));
                stores.setName(cursor.getString(1));
                stores.setAddress(cursor.getString(2));
                stores.setCalle(cursor.getString(3));
                stores.setNo_ext(cursor.getString(4));
                stores.setColonia(cursor.getString(5));
                stores.setMunicipio(cursor.getString(6));
                stores.setCadena(cursor.getString(7));
                stores.setFormato(cursor.getString(8));
                stores.setDia(cursor.getString(9));
                stores.setTac(cursor.getString(10));
                stores.setFinished(cursor.getString(11));
                stores.setSent(cursor.getString(12));
                //Log.e("STATUSTIENDACALEDNARIO> ", cursor.getString(10));
                listStores.add(stores);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listStores;
    }

    // Getting Stores added to calendar(tiendas agregadas al calendario)
    public List<Store> getAAA(String user, String dia) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Store> listStores = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.NAME_STORES +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED + " WHERE user='" + user + "' AND tac='1' AND day='" + dia + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Store store = new Store();
                store.setName(cursor.getString(0));
                //Log.e("COLUMN SORE AGREGADA> ", cursor.getString(10));
                listStores.add(store);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listStores;
    }

    // Getting Stores added to calendar(tiendas agregadas al calendario)
    public List<Store> getStoresAddedCalendario(String user, String dia) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Store> listStores = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.ID_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.NAME_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.ADDRESS_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.CALLE_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.NOEXT_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.COLONIA_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.MUNICIPIO_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.STRING_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.FORMAT_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.DAY + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.TAC + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.FINISHED + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.SENT +
                " FROM " + EsquemaDBTradeFlow.TABLE_NAME_STORES_CALENDAR_ADDED + " WHERE user='" + user + "' AND tac='1' AND day='" + dia + "' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Store store = new Store();
                store.setId_store(cursor.getString(0));
                store.setName(cursor.getString(1));
                store.setAddress(cursor.getString(2));
                store.setCalle(cursor.getString(3));
                store.setNo_ext(cursor.getString(4));
                store.setColonia(cursor.getString(5));
                store.setMunicipio(cursor.getString(6));
                store.setCadena(cursor.getString(7));
                store.setFormato(cursor.getString(8));
                store.setDia(cursor.getString(9));
                store.setTac(cursor.getString(10));
                store.setFinished(cursor.getString(11));
                store.setSent(cursor.getString(12));
                //Log.e("COLUMN SORE AGREGADA> ", cursor.getString(10));
                listStores.add(store);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listStores;
    }

    // Getting all Stores(busqueda predictiva)
    public List<Store> getStoresState(String name, String user, String dia) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Store> listStores = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " +
                EsquemaDBTradeFlow.NamesColumnStores.ID_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.NAME_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.ADDRESS_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.STRING_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.FORMAT_STORES + ", " +
                EsquemaDBTradeFlow.NamesColumnStores.FINISHED + " FROM " +
                EsquemaDBTradeFlow.TABLE_NAME_STORES_PLAZA + " WHERE " +
                EsquemaDBTradeFlow.NamesColumnStores.NAME_STORES + " LIKE '%" + name + "%' ; ", null);

        if (cursor.moveToFirst()) {
            do {
                Store stores = new Store();
                stores.setId_store(cursor.getString(0));
                stores.setName(cursor.getString(1));
                stores.setAddress(cursor.getString(2));
                stores.setCadena(cursor.getString(3));
                stores.setFormato(cursor.getString(4));
                stores.setFinished(cursor.getString(5));
                //Log.e("tienda agregada> ", cursor.getString(6));
                listStores.add(stores);
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        return listStores;
    }
}
