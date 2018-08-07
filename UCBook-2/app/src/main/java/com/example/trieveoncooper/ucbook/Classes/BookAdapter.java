package com.example.trieveoncooper.ucbook.Classes;

import android.util.Log;
import android.view.MenuInflater;

import com.example.trieveoncooper.ucbook.R;

import android.content.Context;
        import android.support.v7.widget.PopupMenu;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private Context mContext;
    public List<Book> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public BookAdapter(Context mContext, List<Book> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
        setHasStableIds(true);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Book album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText("$ "+album.getPrice());

        Log.d("a","THE LAST RUN FOR TH NIGHT'url checl"+album.getUrl());
        // loading album coverr using Glide library
        //Glide.with(mContext).load(album.getUrl()).into(holder.thumbnail);
       //  Picasso.with(mContext).load("https://firebasestorage.googleapis.com/v0/b/ucbok-95490.appspot.com/o/All_Image_Uploads%2F1512624594894.jpg?alt=media&token=cf8fe046-f566-44bb-bbb3-cda400a75ade").into(holder.thumbnail);
        Picasso.with(mContext).load(album.getUrl()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_book, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Sending Message", Toast.LENGTH_SHORT).show();
                   // activityChangeIntent.putExtra("friendUID", user.getuId());
                   // activityChangeIntent.putExtra("username", user.getName());
                   // startActivity(activityChangeIntent);
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Buying Soon to come", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}