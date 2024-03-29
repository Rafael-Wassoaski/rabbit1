package com.example.lucinao;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.List;

public class ConversaAdapter extends RecyclerView.Adapter {

    private List<Conversa> conversaList;

    private Acoes acao;
    private int posicaoRemovidoRecentemente;
    private Conversa conversa;

    public static class ConversaViewHolder extends RecyclerView.ViewHolder {

        TextView msgTextView;
        TextView vcDizTextView;

        public ConversaViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setTag(this);


            msgTextView = (TextView) itemView.findViewById(R.id.msg);
            vcDizTextView = (TextView) itemView.findViewById(R.id.voce);
            vcDizTextView.setVisibility(View.INVISIBLE);
            msgTextView.setVisibility(View.INVISIBLE);

        }

    }


    public ConversaAdapter(List<Conversa> conversaList, Acoes acao) {
        this.conversaList = conversaList;
        this.acao = acao;

    }


    public List<Conversa> getConversaList() {
        return conversaList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        ConversaViewHolder conversa = new ConversaViewHolder(view);


        return conversa;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ConversaViewHolder conversaViewHolder = (ConversaViewHolder) holder;

        if (conversaList.get(position).getEmissor().equals("Rafael")) {
            conversaList.get(position).setMsg("[RAFAEL]" + conversaList.get(position).getMsg());
            ((ConversaViewHolder) holder).vcDizTextView.setText(conversaList.get(position).getMsg());
            ((ConversaViewHolder) holder).vcDizTextView.setVisibility(View.VISIBLE);

            }else{

        ((ConversaViewHolder) holder).msgTextView.setText(conversaList.get(position).getMsg());
        ((ConversaViewHolder) holder).msgTextView.setVisibility(View.VISIBLE);
        }
    }


    public void updateMsg(String novaMsg, int posicao) {
        conversaList.get(posicao).setMsg(novaMsg);
        notifyItemChanged(posicao);
    }

    public void inserir(Conversa conversa) {
        Log.d("Aguardo", "inici");

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
