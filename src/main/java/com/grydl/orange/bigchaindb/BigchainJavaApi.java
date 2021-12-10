package com.grydl.orange.bigchaindb;

import com.bigchaindb.util.Base58;

import java.security.Key;
import java.security.KeyPair;
import java.security.PublicKey;

/**
 * Cette Classe représente les API Java pour communiquer avec BigcahinDB
 */
public class BigchainJavaApi {

    /**
     * Generation de clé privé et public EdDSA pour signer et vérifier les transactions.
     * @return KeyPair
     */
    public static KeyPair getKeys() {
        net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
        KeyPair keyPair = edDsaKpg.generateKeyPair();

        System.out.println("(*) Keys Generated..");
        return keyPair;
    }


}
