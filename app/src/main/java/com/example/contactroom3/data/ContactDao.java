package com.example.contactroom3.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contactroom3.model.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Contact contact);

    @Query("DELETE FROM contact_table")
    void deleteAll();

    @Query("SELECT * FROM contact_table ORDER BY name ASC")
    LiveData<List<Contact>> getAllContacts();

    // getting a particular contact  from the table
    @Query("SELECT * FROM contact_table WHERE contact_table.id==:id")
    LiveData<Contact> get(int id);
    // updating
    @Update
    void update(Contact contact);
    //delete
    @Delete
    void delete(Contact contact);
    // now we have to go to repository to implement these operations or methods

}
