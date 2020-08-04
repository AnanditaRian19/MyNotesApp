package com.belajar.mynotesapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.belajar.mynotesapp.CustomOnItemClickListener;
import com.belajar.mynotesapp.NoteAddUpdateActivity;
import com.belajar.mynotesapp.R;
import com.belajar.mynotesapp.entity.Note;

import java.util.ArrayList;

// Kelas adapter berfungsi untuk menampilkan data perbaris di komponen viewgroup seperti recyclerview
// dengan data yang berasal dari objek linkedlist bernama listnote. Anda melakukan proses inflate layout yang dibuat sebelumynya
// untuk menjadi tampilan per baris di recyclerview. Termasuk didalamnya implementasi dari CustomOnItemClickListener yang
// NoteAddUpdateActivity. tujuannya untuk melakukan perubahan data oleh pengguna

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<Note> listNotes = new ArrayList<>();
    private Activity activtiy;

    // Constructor, Getter and Setter untuk context.
    // Dibutuhkan karena kita akan memanggil fungsi startActivityForResult ketika item diklik
    public NoteAdapter(Activity activtiy) {
        this.activtiy = activtiy;
    }
    public ArrayList<Note> getListNotes() {
        return listNotes;
    }

    public void setListNotes(ArrayList<Note> listNotes) {

        if (listNotes.size() > 0) {
            this.listNotes.clear();
        }
        this.listNotes.addAll(listNotes);

        notifyDataSetChanged();
    }

    // Menambah, memperbaharui, dan menghapus item di recycler view;
    public void addItem(Note note) {
        this.listNotes.add(note);
        notifyItemInserted(listNotes.size() - 1);
    }

    public void updateItem(int position, Note note) {
        this.listNotes.set(position, note);
        notifyItemChanged(position, note);
    }

    public void removeItem(int position) {
        this.listNotes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listNotes.size());
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.tvTitle.setText(listNotes.get(position).getTitle());
        holder.tvDescription.setText(listNotes.get(position).getDescription());
        holder.tvDate.setText(listNotes.get(position).getDate());
        holder.cvNote.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent mIntent = new Intent(activtiy, NoteAddUpdateActivity.class);
                mIntent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, position);
                mIntent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, listNotes.get(position));
                activtiy.startActivityForResult(mIntent, NoteAddUpdateActivity.REQUEST_UPDATE);
            }
        }));

    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvDate;
        CardView cvNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvItemTitle);
            tvDescription = itemView.findViewById(R.id.tvItemDescription);
            tvDate = itemView.findViewById(R.id.tvItemDate);
            cvNote = itemView.findViewById(R.id.cvItemNote);
        }
    }
}
