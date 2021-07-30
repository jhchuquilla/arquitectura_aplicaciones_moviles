package com.emerscience.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emerscience.activity.R;
import com.emerscience.pojos.Familiar;

import java.util.List;

public class AdaptadorFamiliares extends RecyclerView.Adapter<AdaptadorFamiliares.ViewHolderFamiliares>
        implements View.OnClickListener{

    private List<Familiar> listaFamiliares;
    private View.OnClickListener listener;

    public AdaptadorFamiliares(List<Familiar> listaFamiliares) {
        this.listaFamiliares = listaFamiliares;
    }

    @NonNull
    @Override
    public ViewHolderFamiliares onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_familiares, null, false);
        view.setOnClickListener(this);
        return new ViewHolderFamiliares(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFamiliares holder, int position) {
        holder.tvApellidosFam.setText(listaFamiliares.get(position).getApellidosFam());
        holder.tvNombresFam.setText(listaFamiliares.get(position).getNombresFam());
        holder.tvCedulaFam.setText(listaFamiliares.get(position).getCedulaFam());
    }

    @Override
    public int getItemCount() {
        if (listaFamiliares != null){
            return listaFamiliares.size();
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

    public class ViewHolderFamiliares extends RecyclerView.ViewHolder {

        TextView tvApellidosFam, tvNombresFam, tvCedulaFam;

        public ViewHolderFamiliares(@NonNull View itemView) {
            super(itemView);
            this.tvApellidosFam = itemView.findViewById(R.id.tvApellidosFam);
            this.tvNombresFam = itemView.findViewById(R.id.tvNombresFam);
            this.tvCedulaFam = itemView.findViewById(R.id.tvCedulaFam);
        }
    }
}
