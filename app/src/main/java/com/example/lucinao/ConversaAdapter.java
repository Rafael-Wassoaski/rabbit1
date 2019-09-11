package com.example.lucinao;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConversaAdapter extends RecyclerView.Adapter {

    private List<Conversa> conversaList;

    private Acoes acao;
    private int posicaoRemovidoRecentemente;
    private Conversa conversa;
    public static class ConversaViewHolder extends RecyclerView.ViewHolder{

        TextView emissorTextView;
        TextView msgTextView;
        TextView vcDizTextView;

        public ConversaViewHolder(@NonNull View itemView){
            super(itemView);
            itemView.setTag(this);

            emissorTextView=(TextView)itemView.findViewById(R.id.nomeEmissor);
            msgTextView = (TextView)itemView.findViewById(R.id.msg);
            vcDizTextView = (TextView)itemView.findViewById(R.id.voce);


        }

    }




    public  ConversaAdapter(List<Conversa> conversaList, Acoes acao){
        this.conversaList = conversaList;
        this.acao = acao;
    }


    public List<Conversa> getConversaList(){return conversaList;}



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
      ConversaViewHolder conversa = new ConversaViewHolder(view);


        return conversa;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ConversaViewHolder conversaViewHolder =(ConversaViewHolder) holder;

        if(conversaList.get(position).getEmissor().equals("Rafael")){
            ((ConversaViewHolder) holder).emissorTextView.setVisibility(View.INVISIBLE);
        }else{
            ((ConversaViewHolder) holder).vcDizTextView.setVisibility(View.INVISIBLE);
            ((ConversaViewHolder) holder).emissorTextView.setText(conversaList.get(position).getEmissor()+" diz: ");
        }

        ((ConversaViewHolder) holder).msgTextView.setText(conversaList.get(position).getMsg());
    }


    public void updateMsg(String novaMsg, int posicao){
        conversaList.get(posicao).setMsg(novaMsg);
        notifyItemChanged(posicao);
    }
    public void inserir(Conversa conversa){
        conversaList.add(conversa);
        notifyItemInserted(getItemCount());
    }

    @Override
    public int getItemCount() {
        return conversaList.size();
    }
}
