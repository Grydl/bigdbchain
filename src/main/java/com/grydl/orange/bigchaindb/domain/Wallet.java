package com.grydl.orange.bigchaindb.domain;

public class Wallet {

    String nom;
    String pubKey;
    String privateKey;

    public Wallet(String nom, String pubKey, String privateKey) {
        this.nom = nom;
        this.pubKey = pubKey;
        this.privateKey = privateKey;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
