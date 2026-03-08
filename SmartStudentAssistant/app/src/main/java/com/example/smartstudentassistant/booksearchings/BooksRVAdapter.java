package com.example.smartstudentassistant.booksearchings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentassistant.R;

import java.util.ArrayList;

public class BooksRVAdapter extends RecyclerView.Adapter<BooksRVAdapter.ViewHolder> {

    /* Creating variable for our array list for books */
    private final ArrayList<BooksModel> booksModelArrayList;

    /* Creating variable for a context of our class */
    Context context;

    /* Creating a default constructor for our class */
    public BooksRVAdapter(ArrayList<BooksModel> booksModelArrayList, Context context){

        this.booksModelArrayList = booksModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* Here we are inflating our layout file for items of our recycler view */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.books_rv_item,parent,false);
        /* Returning our view layout by using ViewHolder class for data items */
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /* Here we are setting data to items of our recycler view */
        BooksModel booksModel = booksModelArrayList.get(position);

        /* Getting data from class and setting it on our views */
        holder.txtSubjectName.setText(booksModel.getSubjectName());
        holder.txtBookName.setText(booksModel.getBooksName());
        holder.txtWriterName.setText(booksModel.getWriters());
        holder.txtEditionName.setText(booksModel.getEditions());

        /* Setting text values on our static text view items */
        holder.txtBookTitle.setText(R.string.book_title);
        holder.txtWriterTitle.setText(R.string.author);
        holder.txtEditionTitle.setText(R.string.edition);

    }

    @Override
    public int getItemCount() {
        /* Here we are returning size of our books array list */
        return booksModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        /* Creating variables for our text views of items */
        private final TextView txtSubjectName, txtBookTitle, txtBookName, txtWriterTitle, txtWriterName, txtEditionTitle, txtEditionName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            /* Initializing our text views of data items */
            txtSubjectName = itemView.findViewById(R.id.txt_subject_name);
            txtBookTitle = itemView.findViewById(R.id.txt_book_title);
            txtBookName = itemView.findViewById(R.id.txt_book_name);
            txtWriterTitle = itemView.findViewById(R.id.txt_author_title);
            txtWriterName = itemView.findViewById(R.id.txt_writer_name);
            txtEditionTitle = itemView.findViewById(R.id.txt_edition_title);
            txtEditionName = itemView.findViewById(R.id.txt_edition_name);
        }
    }
}
