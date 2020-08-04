package com.belajar.mynotesapp;

import android.view.View;

// Bertugas untuk membuat item seperti cardview bisa diklik di dalam adapter.
// Caranya lakukan penyesuaian pada kelas event OnClickListener. Alhasil kita bisa mengimplementasikan
// interface listener yang baru bernama OnItemClickCallback.
// Kelas tersebut dibuat untuk menghindari nilai final dari posisi yang tentunya sangat tidak direkomendasikan

public class CustomOnItemClickListener implements View.OnClickListener {

    private int position;
    private OnItemClickCallback onItemClickCallback;

    public CustomOnItemClickListener(int position, OnItemClickCallback onItemClickCallback) {
        this.position = position;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onClick(View view) {
        onItemClickCallback.onItemClicked(view, position);
    }

    public interface OnItemClickCallback {
        void onItemClicked(View view, int position);
    }
}
