//package com.codeshinobi.bajeti.AsyncTasks
//
//import android.os.AsyncTask
//import android.provider.ContactsContract.CommonDataKinds.Note
//
//
//class InsertAsyncTask(dao: NoteDao) :
//    AsyncTask<Note?, Void?, Void?>() {
//    private val mNoteDao: NoteDao
//
//    init {
//        mNoteDao = dao
//    }
//
//    protected override fun doInBackground(vararg notes: Note): Void? {
//        mNoteDao.insertNotes(notes)
//        return null
//    }
//}