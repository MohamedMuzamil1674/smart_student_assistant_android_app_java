package com.example.smartstudentassistant.notesdrivelink;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstudentassistant.R;

import java.util.ArrayList;

public class NotesRVAdapter extends RecyclerView.Adapter<NotesRVAdapter.ViewHolder> {

    /* Creating variable for our array list for notes */
    ArrayList<NotesModel> notesModelArrayList;

    /* Creating variable for a context of our class */
    Context NotesContext;

    /* Creating Variable for interface of notes click */
    private OnNotesLinkClickListener notesListener;

    /* Creating an Interface for click on notes card link */
    public interface OnNotesLinkClickListener{
        void onNotesClick(String notesLink);
    }

    /* Setting Listener on selection of notes card link */
    public void setOnNotesLinkClickListener(OnNotesLinkClickListener notesListener){
        this.notesListener = notesListener;
    }

    /* Creating a default constructor for our class for context and our notes data sharing */
    public NotesRVAdapter(ArrayList<NotesModel> notesModelArrayList, Context NotesContext){

        this.notesModelArrayList = notesModelArrayList; /* Here we initialize our details */

        this.NotesContext = NotesContext;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /* Here we are inflating our layout file for items of our recycler view */
        View Myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_rv_items,parent,false);

        /* Returning our view layout by using ViewHolder class for data items */
        return new ViewHolder(Myview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        /* Here we are setting data to items of our recycler view */
        NotesModel notesModel = notesModelArrayList.get(position);

        /* Getting data from class and setting it on our views */
        holder.notesTitle.setText(notesModel.getNotesTitle());
        holder.notesLinks.setText(notesModel.getNotesLinks());

        /* Here we are setting on click listener on our notes link */
        holder.notesLinks.setOnClickListener(v -> {
            /* Here we will notify our activity for a listener */

            if (notesListener != null){

                /* Here we are getting google link of subject notes */
                notesListener.onNotesClick(notesModel.getNotesLinks());

            }
        });

    }

    @Override
    public int getItemCount() {

        /* Here we are returning size of our notes array list */
        return notesModelArrayList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        /* Here we will bind our views of notes items */
        TextView notesTitle, notesLinks;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            /* Here we will hook our notes layout views inside adapter */
            notesTitle = itemView.findViewById(R.id.txt_notes_subject);
            notesLinks = itemView.findViewById(R.id.txt_subjects_link);
        }
    }
}
