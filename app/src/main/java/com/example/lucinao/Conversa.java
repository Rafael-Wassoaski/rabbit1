package com.example.lucinao;

import java.io.Serializable;

public class Conversa implements Serializable {

    private String emissor;
    private String ipEmissor;
    private String msg;
    private String suaMsg;


    public Conversa(String emissor, String ipEmissor, String msg, String suaMsg) {
        this.emissor = emissor;
        this.ipEmissor = ipEmissor;
        this.msg = msg;
        this.suaMsg = suaMsg;
    }

    public String getEmissor(){
        return emissor;
    }

    public String getIpEmissor(){
        return ipEmissor;
    }

    public String msg(){
        return msg;
    }

    public String getMsg(){
        return suaMsg;
    }


    public void setEmissor(String emissor) {
        this.emissor = emissor;
    }

    public void setIpEmissor(String ipEmissor) {
        this.ipEmissor = ipEmissor;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void setSuaMsg(String msg) {
        this.suaMsg = msg;
    }
}


