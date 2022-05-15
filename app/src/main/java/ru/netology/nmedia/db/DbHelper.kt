package ru.netology.nmedia.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(val context: Context, val version: Int, val dbFileName: String, val DDLs: Array<String>):
    SQLiteOpenHelper(context, dbFileName, null, version){

    override fun onCreate(db: SQLiteDatabase?) {
        DDLs.forEach {
            db?.execSQL(it)
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

}