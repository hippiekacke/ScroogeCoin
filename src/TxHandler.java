import java.util.ArrayList;


public class TxHandler {

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
	
	private UTXOPool uPool;
	
    public TxHandler(UTXOPool utxoPool) {
        // IMPLEMENT THIS
    	uPool = new UTXOPool(utxoPool);
        	
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
    	
    	ArrayList<UTXO> seenUTXO = new ArrayList<UTXO>();
		
    	double inSum = 0;
		double outSum = 0;
		
		//(1)
		// hole outputs der tx und prüfe in UTXO pool
		
		for (Transaction.Output output : tx.getOutputs()){
			if (!uPool.contains(output))
			
		}
		
		
		
		

		for (Transaction.Input in : tx.getInputs()) {
			
			UTXO checkUTXO = new UTXO(in.prevTxHash, in.outputIndex);
			if (seenUTXO.contains(checkUTXO)) return false; // 3
			//no UTXO is claimed multiple times by tx
			
			seenUTXO.add(checkUTXO);
			
			//if the transaction pool doesn't contain it already
			if (!uPool.contains(checkUTXO)) return false; // 1
			
			inSum += uPool.getTxOutput(checkUTXO).value;
			
			// Check Signature
			
			
			

		}
		
		for (Transaction.Output out : tx.getOutputs()) {
			if (out.value < 0) return false; // 4
			outSum += out.value;
		}
		
		if (outSum > inSum) return false; // 5
		
		return true;
		}
    

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    	public Transaction[] handleTxs(Transaction[] possibleTxs) {
    		
    		ArrayList<Transaction> goodTx = new ArrayList<Transaction>();
    		
    		boolean isDone = false;
    		
    		while (!isDone) {
    			isDone = true;
    			
    			for (int i = 0; i < possibleTxs.length; i++) {
    				if (possibleTxs[i] == null) continue;
    				if (isValidTx(possibleTxs[i])) {
    					// Remove old UTXOs from Pool
    					for (Transaction.Input in : possibleTxs[i].getInputs()) {
    						UTXO delUTXO = new UTXO(in.prevTxHash, in.outputIndex);
    						uPool.removeUTXO(delUTXO);
    					}
    					
    					// Add new UTXOs to Pool
    					for(int j = 0; j < possibleTxs[i].getOutputs().size(); j++) {
    						UTXO newUTXO = new UTXO(possibleTxs[i].getHash(), j);
    						uPool.addUTXO(newUTXO, possibleTxs[i].getOutputs().get(j));
    					}
    					
    					goodTx.add(possibleTxs[i]);
    					
    					// Set Array Element to Null
    					possibleTxs[i] = null;
    					
    					// Not Done Yet!
    					isDone = false;
    				}
    			}
    		}
    		
    		Transaction[] tArr = new Transaction[goodTx.size()];
    		tArr = goodTx.toArray(tArr);
    		return tArr;
    	}

}
