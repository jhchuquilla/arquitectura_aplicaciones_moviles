package com.emerscience.seguimiento.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emerscience.seguimiento.activity.R;
import com.emerscience.seguimiento.pojos.Usuario;

import java.util.List;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.ViewHolderUsuarios>
        implements View.OnClickListener {

    private List<Usuario> listaUsuarios;
    private View.OnClickListener listener;
    private String estadoUsuario;

    public AdaptadorUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public ViewHolderUsuarios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_usuarios, null, false);
        view.setOnClickListener(this);
        return new ViewHolderUsuarios(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUsuarios holder, int position) {
        holder.tvUsuario.setText(listaUsuarios.get(position).getUsername());
        holder.tvRol.setText(listaUsuarios.get(position).getRol());
        if (listaUsuarios.get(position).isUsuarioActivo()){
            estadoUsuario = "Activo";
        }else{
            estadoUsuario = "Inactivo";
        }
        holder.tvEstadoUsuario.setText(estadoUsuario);
    }

    @Override
    public int getItemCount() {
        if (listaUsuarios != null){
            return listaUsuarios.size();
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

    public class ViewHolderUsuarios extends RecyclerView.ViewHolder{
        TextView tvUsuario, tvRol, tvEstadoUsuario;

        public ViewHolderUsuarios(@NonNull View itemView){
            super(itemView);
            this.tvUsuario = itemView.findViewById(R.id.tvUsuario);
            this.tvRol = itemView.findViewById(R.id.tvRol);
            this.tvEstadoUsuario = itemView.findViewById(R.id.tvEstadoUsuario);
        }
    }

}
