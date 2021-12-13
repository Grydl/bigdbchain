package com.grydl.orange.bigchaindb;

import java.io.IOException;
import java.security.KeyPair;
import java.util.Map;
import java.util.TreeMap;

import com.bigchaindb.builders.BigchainDbConfigBuilder;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.cryptoconditions.Fulfillment;
import com.bigchaindb.model.*;
import com.bigchaindb.util.Base58;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import okhttp3.Response;

/**
 * simple usage of BigchainDB Java driver (https://github.com/bigchaindb/java-bigchaindb-driver)
 * to create TXs on BigchainDB network
 * @author dev@bigchaindb.com
 *
 */

public class BigchainDBJavaDriverUsageExample {

    /**
     * main method
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static <assetId> void main(String args[]) throws Exception {

        BigchainDBJavaDriverUsageExample examples = new BigchainDBJavaDriverUsageExample();

        //set configuration
        BigchainDBJavaDriverUsageExample.setConfig();

        //generate Keys
        KeyPair keys = BigchainDBJavaDriverUsageExample.getKeys();

        //generate Keys
        KeyPair keys2 = BigchainDBJavaDriverUsageExample.getKeys();

        //generate Keys
        KeyPair keys3 = BigchainDBJavaDriverUsageExample.getKeys();


        System.out.println("User 1 : "+Base58.encode(keys.getPublic().getEncoded()));
        System.out.println("User 1 : "+Base58.encode(keys.getPrivate().getEncoded()));

        System.out.println("User 2 : "+Base58.encode(keys2.getPublic().getEncoded()));
        System.out.println("User 2 : "+Base58.encode(keys2.getPrivate().getEncoded()));

        System.out.println("User 3 : "+Base58.encode(keys3.getPublic().getEncoded()));
        System.out.println("User 3 : "+Base58.encode(keys3.getPrivate().getEncoded()));

        // create New asset
        Map<String, String> assetData = new TreeMap<String, String>() {{
            put("monnaie", "F CFA");
            put("decimal", "2");
            put("Entreprise", "Orange Mali");
        }};
        System.out.println("(*) Assets Prepared..");

        // create metadata
        MetaData metaData = new MetaData();
        metaData.setMetaData("reference", "Emission monnaie");
        metaData.setMetaData("total", "145");
        System.out.println("(*) Metadata Prepared..");

        //execute CREATE transaction
        String txId = examples.doCreate(assetData, metaData, keys);

        String assetId = txId;


        //let the transaction commit in block
        Thread.sleep(5000);

        System.out.println("######################################## id = "+ assetId+ " TX "+ txId);

        //create transfer metadata
        MetaData transferMetadata = new MetaData();
        transferMetadata.setMetaData("Transfer ", "amount");
        System.out.println("(*) Transfer Metadata Prepared..");

        //execute TRANSFER transaction on the CREATED asset
        String txId2 = examples.doTransfer(assetId,txId, transferMetadata, keys,keys2);


        Thread.sleep(5000);
        System.out.println("######################################## id = "+ assetId+ " TX "+ txId2);

        //execute TRANSFER transaction on the CREATED asset
        //String  txId3 = examples.doTransferInput(assetId, txId2, transferMetadata, keys,keys2,keys3);

        //System.out.println("######################################## id = "+ assetId+ " TX "+ txId3);

    }

    private void onSuccess(Response response) {
        //TODO : Add your logic here with response from server
        System.out.println("Transaction posted successfully");
    }

    private void onFailure() {
        //TODO : Add your logic here
        System.out.println("Transaction failed");
    }

    private GenericCallback handleServerResponse() {
        //define callback methods to verify response from BigchainDBServer
        GenericCallback callback = new GenericCallback() {

            @Override
            public void transactionMalformed(Response response) {
                System.out.println("malformed " + response.message());
                System.out.println("malformed " + response.request().toString());
                onFailure();

            }

            @Override
            public void pushedSuccessfully(Response response) {
                System.out.println("pushedSuccessfully");
                onSuccess(response);
            }

            @Override
            public void otherError(Response response) {
                System.out.println("otherError" + response.message());
                System.out.println("malformed " + response.request().toString());
                onFailure();
            }
        };

        return callback;
    }


    /**
     * configures connection url and credentials
     */
    public static void setConfig() {
        BigchainDbConfigBuilder
                .baseUrl("http://localhost:9984/") //or use http://testnet.bigchaindb.com
                .addToken("app_id", "")
                .addToken("app_key", "").setup();

    }
    /**
     * generates EdDSA keypair to sign and verify transactions
     * @return KeyPair
     */
    public static KeyPair getKeys() {
        //  prepare your keys
        net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
        KeyPair keyPair = edDsaKpg.generateKeyPair();
        System.out.println("(*) Keys Generated..");
        return keyPair;

    }

    /**
     * performs CREATE transactions on BigchainDB network
     * @param assetData data to store as asset
     * @param metaData data to store as metadata
     * @param keys keys to sign and verify transaction
     * @return id of CREATED asset
     */
    public String doCreate(Map<String, String> assetData, MetaData metaData, KeyPair keys) throws Exception {

        try {
            //build and send CREATE transaction
            Transaction transaction = null;

            transaction = BigchainDbTransactionBuilder
                    .init()
                    .addAssets(assetData, TreeMap.class)
                    .addMetaData(metaData)
                    .addOutput(String.valueOf(145), (EdDSAPublicKey) keys.getPublic())
                    .operation(Operations.CREATE)
                    .buildAndSign((EdDSAPublicKey) keys.getPublic(), (EdDSAPrivateKey) keys.getPrivate())
                    .sendTransaction(handleServerResponse());

            System.out.println("(*) CREATE Transaction sent.. - " + transaction.getId());
            return transaction.getId();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * performs TRANSFER operations on CREATED assets
     * @param txId id of transaction/asset
     * @param metaData data to append for this transaction
     * @param keysSender keys to sign and verify transactions
     * @param keysReceiver keys to receive the output
     */
    public String doTransfer(String assetId, String txId, MetaData metaData, KeyPair keysSender, KeyPair keysReceiver) throws Exception {

        Map<String, String> assetData = new TreeMap<String, String>();
        assetData.put("id", assetId);

        try {

            //which transaction you want to fulfill?
            FulFill fulfill = new FulFill();
            fulfill.setOutputIndex(0);
            fulfill.setTransactionId(txId);

            //build and send TRANSFER transaction
            Transaction transaction = BigchainDbTransactionBuilder
                    .init()
                    .addInput(null, fulfill, (EdDSAPublicKey) keysSender.getPublic())
                    .addOutput(String.valueOf(140), (EdDSAPublicKey) keysReceiver.getPublic())
                    .addOutput(String.valueOf(3), (EdDSAPublicKey) keysReceiver.getPublic())
                    .addOutput(String.valueOf(2), (EdDSAPublicKey) keysSender.getPublic())
                    .addAssets(assetId, String.class)
                    .addMetaData(metaData)
                    .operation(Operations.TRANSFER)
                    .buildAndSign((EdDSAPublicKey) keysSender.getPublic(), (EdDSAPrivateKey) keysSender.getPrivate())
                    .sendTransaction(handleServerResponse());

            System.out.println("(*) TRANSFER Transaction sent.. - " + transaction.getId());

            return transaction.getId();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }



    public String doTransferInput(String assetID, String txId, MetaData metaData, KeyPair keysSender, KeyPair keysReceiver, KeyPair other) throws Exception {

        Map<String, String> assetData = new TreeMap<String, String>();
        assetData.put("id", assetID);

        try {

            //which transaction you want to fulfill?
            FulFill fulfill = new FulFill();
            fulfill.setOutputIndex(0);
            fulfill.setTransactionId(txId);

            //which transaction you want to fulfill?
            FulFill fulfill2 = new FulFill();
            fulfill2.setOutputIndex(1);
            fulfill2.setTransactionId(txId);

            //which transaction you want to fulfill?
            FulFill fulfill3 = new FulFill();
            fulfill3.setOutputIndex(2);
            fulfill3.setTransactionId(txId);

            //build and send TRANSFER transaction
            Transaction transaction = BigchainDbTransactionBuilder
                    .init()
                    .addInput((String) null, fulfill, (EdDSAPublicKey) keysReceiver.getPublic())
                    .addInput((String) null, fulfill2, (EdDSAPublicKey) keysReceiver.getPublic())
                    //.addInput((String)null, fulfill2, (EdDSAPublicKey) keysSender.getPublic())
                    //.addInput((String)null, fulfill3, (EdDSAPublicKey) keysSender.getPublic())
                    //.addOutput(String.valueOf(5), (EdDSAPublicKey) other.getPublic())
                    .addOutput(String.valueOf(140), (EdDSAPublicKey) other.getPublic())
                    .addOutput(String.valueOf(5), (EdDSAPublicKey) other.getPublic())
                    .addAssets(assetID, String.class)
                    .addMetaData(metaData)
                    .operation(Operations.TRANSFER)
                    .buildAndSign((EdDSAPublicKey) keysReceiver.getPublic(), (EdDSAPrivateKey) keysReceiver.getPrivate())
                    .sendTransaction(handleServerResponse());

            System.out.println("(*) TRANSFER Transaction sent.. - " + transaction.getId());

            return transaction.getId();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


}
