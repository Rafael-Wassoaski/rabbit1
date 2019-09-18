package com.example.lucinao;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FilaConversaAdapter extends RecyclerView.Adapter {

    private List<Conversa> conversaList;

    private Acoes acao;
    private int posicaoRemovidoRecentemente;
    private Conversa conversa;

    public static class ConversaViewHolder extends RecyclerView.ViewHolder {

        TextView autorTextView;
        TextView msgTextView;

        public ConversaViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setTag(this);


            autorTextView = (TextView) itemView.findViewById(R.id.autor);
            msgTextView = (TextView) itemView.findViewById(R.id.msg);


        }

    }


    public FilaConversaAdapter(List<Conversa> conversaList, Acoes acao) {
        this.conversaList = conversaList;
        this.acao = acao;

    }


    public List<Conversa> getConversaList() {
        return conversaList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_fila, parent, false);
        ConversaViewHolder conversa = new ConversaViewHolder(view);


        return conversa;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        ConversaViewHolder conversaViewHolder = (ConversaViewHolder) viewHolder;

        ((ConversaViewHolder) conversaViewHolder).autorTextView.setText(conversaList.get(position).getEmissor());
        ((ConversaViewHolder) conversaViewHolder).msgTextView.setText(conversaList.get(position).getMsg());
        conversaViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acao.Responder(viewHolder.getAdapterPosition());
            }
        });

    }


    public void updateMsg(String novaMsg, int posicao) {
        conversaList.get(posicao).setMsg(novaMsg);
        notifyItemChanged(posicao);
    }

    public int getByEmissor(String emissor){

        for(int i = 0; i < conversaList.size(); i++){
            if(conversaList.get(i).getEmissor().equals(emissor)){
                return i;
            }
        }

        return -1;
    }

    public void inserir(Conversa conversa) {
        Log.d("Aguardo", "inici");
            conversa.setEmissor(conversa.getEmissor()+": ");
        try {
            conversaList.add(conversa);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            Log.d("Aguardo", e.getMessage() + "");
        }
        Log.d("Aguardo", "fim");
    }

    @Override
    public int getItemCount() {
        return conversaList.size();
    }


}
