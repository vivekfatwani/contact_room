package com.example.contactroom3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.contactroom3.model.Contact;
import com.example.contactroom3.model.ContactViewModel;
import com.google.android.material.snackbar.Snackbar;

public class NewContact extends AppCompatActivity {
    public static final String NAME_REPLY = "Name_reply";
    public static final String OCCUPATION = "Occupation";
    private EditText name;
private EditText occupation;
private Button saveButton;
private int contact_id=0;
private  boolean isEdit=false; // is to check if we have something to edit or not
    private Button updateButton;
    private Button deleteButton;
ContactViewModel contactViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        name=findViewById(R.id.enter_name);
        occupation=findViewById(R.id.enter_occupation);
        saveButton=findViewById(R.id.save_button);
        contactViewModel=new ViewModelProvider.AndroidViewModelFactory(NewContact.this
                .getApplication())
                .create(ContactViewModel.class);

        // retrieving the info that is passed through intent from mainActivity we create bundle of data
        //An Intent carries information about what to do and sometimes data to be delivered to the target activity.
        //It's a good practice to check if data is null before trying to access data from it to avoid potential errors.
        //To access the actual data within the Bundle, you typically use methods like getString(), getInt()
        //If a Bundle exists, getExtras() returns it. Otherwise, it returns null.

        if(getIntent().hasExtra(MainActivity.CONTACT_ID)) {
            contact_id = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);
            contactViewModel.get(contact_id).observe(this, contact -> {
                if (contact != null) {
                    name.setText(contact.getName());
                    occupation.setText(contact.getOccupation());
                }
            });
            isEdit=true;  // means we have some intent or data to edit
        }

        saveButton.setOnClickListener(v -> {
            Intent replyIntent= new Intent();
            if(!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(occupation.getText()))
            {   String name1=name.getText().toString();
                String occupation1=occupation.getText().toString();
                replyIntent.putExtra(NAME_REPLY,name1);
                replyIntent.putExtra(OCCUPATION,occupation1);

                setResult(RESULT_OK,replyIntent);


            }
            else {
                setResult(RESULT_CANCELED,replyIntent);
            }
            finish();
        });
        // delete button
        deleteButton=findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(v -> edit(true ));




        // update button
         updateButton= findViewById(R.id.update_button);

        updateButton.setOnClickListener(v -> edit(false));
        // disappearing save button while editing and other buttons while saving new contact
          if(isEdit)
          {
              saveButton.setVisibility(View.GONE);
          }
          else
          {
              updateButton.setVisibility(View.GONE);
              deleteButton.setVisibility(View.GONE);
          }


    }

    private void edit(boolean isDelete) {
        String name1=name.getText().toString().trim();
        String occupation1=occupation.getText().toString().trim();
        if(TextUtils.isEmpty(name1) || TextUtils.isEmpty(occupation1))
        {
            Snackbar.make(name,R.string.empty,Snackbar.LENGTH_SHORT)
                    .show();
        }
        else
        {
            Contact contact= new Contact();
            contact.setId(contact_id);
            contact.setName(name1);
            contact.setOccupation(occupation1);
            // now invoking delete method using viewModel
            if(isDelete)
            ContactViewModel.delete(contact);
            else
                ContactViewModel.update(contact);
            finish();
        }
    }
}