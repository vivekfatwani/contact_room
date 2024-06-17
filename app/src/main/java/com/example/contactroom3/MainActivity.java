package com.example.contactroom3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactroom3.adapter.RecyclerViewAdapter;
import com.example.contactroom3.model.Contact;
import com.example.contactroom3.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    public static final String CONTACT_ID = "contact_id";
    private RecyclerView recyclerView;

private ContactViewModel contactViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycler_view);



        contactViewModel= new ViewModelProvider.AndroidViewModelFactory(MainActivity.this
                .getApplication())
                .create(ContactViewModel.class);
        contactViewModel.getAllContacts().observe(this, contacts -> {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            // setting up the adapter
           RecyclerViewAdapter recyclerViewAdapter= new RecyclerViewAdapter(contacts,MainActivity.this,this);
           recyclerView.setAdapter(recyclerViewAdapter);
           // StringBuilder builder=new StringBuilder();
            //for(Contact contact : contacts)
            //{
              //  builder.append("-").append(contact.getName()).append(" ").append(contact.getOccupation());
                //Log.d("TAG123", "onCreate: "+ contact.getName());
            //}


        });


        FloatingActionButton fab= findViewById(R.id.add_contact_fab);
        fab.setOnClickListener(v -> {
            Intent intent= new Intent(MainActivity.this,NewContact.class);
            startActivityForResult(intent,NEW_CONTACT_ACTIVITY_REQUEST_CODE);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK)
        {
            assert data != null;
            String name2=data.getStringExtra(NewContact.NAME_REPLY);
            String occupation2= data.getStringExtra(NewContact.OCCUPATION);
            Contact contact= new Contact(name2,occupation2);

            ContactViewModel.insert(contact);
        }
    }

    @Override
    public void onContactClick(int position) {
        Contact contact= contactViewModel.allContacts.getValue().get(position);
        Log.d("TAG12", "onContactClick: "+ contact.getId());
        // if we want to go to new activity after pressing the icons then we use method startActivity and intent
        // implementing update method using intent
        Intent intent= new Intent(MainActivity.this,NewContact.class);
        intent.putExtra(CONTACT_ID,contact.getId());
        startActivity(intent);// we are going from mainActivity to new Contact so we now get data into that class that we are passing through this class
    }
}