package com.emerscience.seguimiento.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emerscience.seguimiento.activity.R;
import com.emerscience.seguimiento.pojos.Seguimiento;

import java.util.List;

public class AdaptadorSeguimiento extends RecyclerView.Adapter<AdaptadorSeguimiento.ViewHolderSeguimiento>
        implements View.OnClickListener{

    private List<Seguimiento> listaSeguimiento;
    private View.OnClickListener listener;

    public AdaptadorSeguimiento(List<Seguimiento> listaSeguimiento){
        this.listaSeguimiento = listaSeguimiento;
    }

    @NonNull
    @Override
    public ViewHolderSeguimiento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_seguimiento, null, false);
        view.setOnClickListener(this);
        return new ViewHolderSeguimiento(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSeguimiento holder, int position) {
        holder.tvFechaRegistro.setText(listaSeguimiento.get(position).getFechaRegistroSeguimiento());
    }

    @Override
    public int getItemCount() {
        if (listaSeguimiento != null){
            return listaSeguimiento.size();
        }
        return 0;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }

    public class ViewHolderSeguimiento extends RecyclerView.ViewHolder {
        TextView tvFechaRegistro;

        public ViewHolderSeguimiento(@NonNull View itemView) {
            super(itemView);
            this.tvFechaRegistro = itemView.findViewById(R.id.tvFechaRegistro);
        }
    }

}
