package com.example.contactroom3.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.contactroom3.model.Contact;
import com.example.contactroom3.util.ContactRoomDatabase;

import java.util.List;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application) {
        ContactRoomDatabase db = ContactRoomDatabase.getDatabase(application);
        contactDao = db.contactDao();

        allContacts = contactDao.getAllContacts();

    }
    public LiveData<List<Contact>> getAllData() { return allContacts; }
    public void insert(Contact contact) {
        //  we are using databaseWriterExecutor to do all these operations in the background so that main thread si not disturbed
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> contactDao.insert(contact));
    }
    public LiveData<Contact> get(int id) {
        return contactDao.get(id);
    }

    public void update(Contact contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> contactDao.update(contact));
    }
    public void delete(Contact contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> contactDao.delete(contact));
    }
// now we go to the viewModel and implement these functions so that it can work as public api and can work on
    // live data

}
