package com.erastus.tiketsaya;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyTicket> myTickets;

    public TicketAdapter(Context c, ArrayList<MyTicket> p) {
        context = c;
        myTickets = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_myticket, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        holder.namaWisata.setText(myTickets.get(pos).getNama_wisata());
        holder.lokasi.setText(myTickets.get(pos).getLokasi());
        holder.jmlhTiket.setText(myTickets.get(pos).getJumlah_tiket() + " Tickets");

        final String getNamaWisata = myTickets.get(pos).getNama_wisata();
        holder.itemView.setOnClickListener(v -> {
            Intent goMyTicketDetails = new Intent(context, MyTicketDetailActivity.class);
            goMyTicketDetails.putExtra("nama_wisata", getNamaWisata);
            context.startActivity(goMyTicketDetails);
        });
    }

    @Override
    public int getItemCount() {
        return myTickets.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView namaWisata, lokasi, jmlhTiket;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            namaWisata = itemView.findViewById(R.id.nama_wisata);
            lokasi = itemView.findViewById(R.id.lokasi);
            jmlhTiket = itemView.findViewById(R.id.jmlh_ticket);
        }
    }
}
