package com.trivadis.tricoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import java.math.BigInteger;

/**
 * A simple web3j application that demonstrates a number of core features of web3j:
 *
 * <ol>
 *     <li>Connecting to a node on the Ethereum network</li>
 *     <li>Loading an Ethereum wallet file</li>
 *     <li>Sending Ether from one address to another</li>
 *     <li>Deploying a smart contract to the network</li>
 *     <li>Reading a value from the deployed smart contract</li>
 *     <li>Updating a value in the deployed smart contract</li>
 *     <li>Viewing an event logged by the smart contract</li>
 * </ol>
 *
 * <p>To run this demo, you will need to provide:
 *
 * <ol>
 *     <li>Ethereum client (or node) endpoint. The simplest thing to do is
 *     <a href="https://infura.io/register.html">request a free access token from Infura</a></li>
 *     <li>A wallet file. This can be generated using the web3j
 *     <a href="https://docs.web3j.io/command_line.html">command line tools</a></li>
 *     <li>Some Ether. This can be requested from the
 *     <a href="https://www.rinkeby.io/#faucet">Rinkeby Faucet</a></li>
 * </ol>
 *
 * <p>For further background information, refer to the project README.
 */
public class TriCoinMain {

    private static final Logger log = LoggerFactory.getLogger(TriCoinMain.class);

    public static void main(String[] args) throws Exception {
        new TriCoinMain().run();
    }

    private void run() throws Exception {

        // We start by creating a new web3j instance to connect to remote nodes on the network.
        // Note: if using web3j Android, use Web3jFactory.build(...
        Web3j web3j = Web3j.build(new HttpService(
                "https://rinkeby.infura.io/kRf3lcK5sXaytiitWie6"));
        log.info("Connected to Ethereum client version: "
                + web3j.web3ClientVersion().send().getWeb3ClientVersion());

        // We then need to load our Ethereum wallet file
        Credentials credentials =
                WalletUtils.loadCredentials(
                        "xxxx",
                        "UTC--2018-04-08T10-59-33.474000000Z--dd06426c25e2a21a121613be469c025088e89d35.json");
        log.info("Credentials loaded");
        Credentials bob =
                WalletUtils.loadCredentials(
                        "xxxx",
                        "UTC--2018-04-08T14-29-23.479000000Z--014269ef97299b8eb17d662ca85b49e85f533cd0.json");



        /*
        log.info("Sending 1 Wei ("
                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
        TransactionReceipt transferReceipt = Transfer.sendFunds(
                web3j, credentials,
                "0xdd06426c25e2a21a121613be469c025088e89d35",  // you can put any address here
                BigDecimal.ONE, Convert.Unit.WEI)  // 1 wei = 10^-18 Ether
                .send();
        log.info("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                + transferReceipt.getTransactionHash());
*/


        log.info("Loading smart contract");
        TriCoin contract =   TriCoin.load("0x78021bafea6605a9bba9a7cc8c12b5d2172fc857",web3j,credentials,ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);

        String contractAddress = contract.getContractAddress();
        log.info("View contract at https://rinkeby.etherscan.io/address/" + contractAddress);

        log.info("Total supply of TriCoins: " + contract.totalSupply().send());

        // Lets modify the value in our smart contract
        log.info("Balance of 0x65A1FeC365a19E2e2Ccd36f51DbD74043A3d572b: " + contract.balanceOf("0x65A1FeC365a19E2e2Ccd36f51DbD74043A3d572b").send());


        log.info("transferring 100 TRI to 0x65A1FeC365a19E2e2Ccd36f51DbD74043A3d572b");
        TransactionReceipt transferReceipt = contract.transfer(
                "0x65A1FeC365a19E2e2Ccd36f51DbD74043A3d572b", new BigInteger("10000000000000000000")).send();
        log.info("View Transaction at https://rinkeby.etherscan.io/tx/" + transferReceipt.getTransactionHash());


        log.info("Balance of 0x65A1FeC365a19E2e2Ccd36f51DbD74043A3d572b: " + contract.balanceOf("0x65A1FeC365a19E2e2Ccd36f51DbD74043A3d572b").send());

    }
}
