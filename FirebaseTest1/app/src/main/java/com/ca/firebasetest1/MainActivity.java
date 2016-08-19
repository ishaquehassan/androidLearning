package com.ca.firebasetest1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public Firebase myFirebaseRef = null;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        myFirebaseRef = new Firebase("https://caandroidtest.firebaseio.com/messages/");

        mRecyclerView = (RecyclerView) findViewById(R.id.dataView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myFirebaseRef,MyItem.class,myFirebaseRef);
        mRecyclerView.setAdapter(mAdapter);


        Button btn = (Button) findViewById(R.id.btn);
        final EditText et = (EditText) findViewById(R.id.name);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.getText().length() > 0){
                    et.setError(null);
                    Map<String, String> item = new HashMap<String, String>();
                    item.put("name", et.getText().toString());
                    myFirebaseRef.push().setValue(item);
                    et.setText("");
                }else{
                    et.setError("Please Fill Out Name!");
                }
            }
        });

    }


    public class MyAdapter extends FirebaseRecyclerAdapter<MyAdapter.ViewHolder, MyItem> {

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView textViewName;
            ImageView delBtn;

            public ViewHolder(View view) {
                super(view);
                textViewName = (TextView) view.findViewById(R.id.nameTxt);
                delBtn = (ImageView) view.findViewById(R.id.del);
            }
        }

        public MyAdapter(Query query, Class<MyItem> itemClass,Firebase dbRef) {
            super(query, itemClass,dbRef);
        }

        @Override public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false);

            return new ViewHolder(view);
        }

        @Override public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
            MyItem item = getItem(position);
            holder.textViewName.setText(item.getName());
            holder.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        }

        @Override protected void itemAdded(MyItem item, String key, int position) {
            Log.d("MyAdapter", "Added a new item to the adapter.");
        }

        @Override protected void itemChanged(MyItem oldItem, MyItem newItem, String key, int position) {
            Log.d("MyAdapter", "Changed an item.");
        }

        @Override protected void itemRemoved(MyItem item, String key, int position) {
            Log.d("MyAdapter", "Removed an item from the adapter.");
        }

        @Override protected void itemMoved(MyItem item, String key, int oldPosition, int newPosition) {
            Log.d("MyAdapter", "Moved an item.");
        }
    }
}
