import java.io.IOException;
import java.util.List;

import org.joda.money.Money;

import com.coinbase.api.Coinbase;
import com.coinbase.api.CoinbaseBuilder;
import com.coinbase.api.entity.Account;
import com.coinbase.api.entity.AccountsResponse;
import com.coinbase.api.exception.CoinbaseException;
import com.oklink.api.OKLink;
import com.oklink.api.OKLinkBuilder;
import com.oklink.api.bean.Amount;
import com.oklink.api.bean.OKLinkException;
import com.oklink.api.bean.UserBalance;
import com.oklink.api.bean.Wallet;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-12 下午3:34:18
 * 类说明
 */
public class T {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Coinbase boinbase = new CoinbaseBuilder().withAccessToken("2ccdca837f647717e58e2e9554f7289cae9cd72b9f848d94b45a0eb0e3f370d5").build();
		try {
			com.coinbase.api.entity.User user = boinbase.getUser();
			Money money = user.getBalance();
			System.out.println("total amount:" + money.getAmount().doubleValue());
			AccountsResponse r = boinbase.getAccounts();
	        List<Account> accounts = r.getAccounts();
	        for(Account account : accounts){
	        	if(Account.Type.WALLET.equals(account.getType())&&account.isActive()){
	        		System.out.println("id:" + account.getId());
	        		System.out.println("name:" + account.getName());
	        		System.out.println("amount:" + account.getBalance().getAmount().doubleValue());
	        	}
	        }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoinbaseException e) {
			e.printStackTrace();
		}
		
		main2();
	}
	
	public static void main2() {
		OKLink oklink = new OKLinkBuilder().withAccessToken("8cea637cfa1bdf8260cf6614f0847564d48e25ad58ca34e526611878b7bd18a6").build();
		try {
			UserBalance ub = oklink.getUserBalance();
			Amount totalBtcBalance = ub.getTotalBtcBalance();
			Amount totalLtcBalance = ub.getTotalLtcBalance();
			System.out.println("totalBtcBalance:"+totalBtcBalance.getAmount());
			System.out.println("totalLtcBalance:"+totalLtcBalance.getAmount());
			List<Wallet> wallets = ub.getWalletBalances();
			for(Wallet wallet : wallets){
        		System.out.println("id:" + wallet.getId());
        		System.out.println("name:" + wallet.getName());
        		System.out.println("btc_amount:" + wallet.getBtcBalance().getAmount());
        		System.out.println("btc_amount:" + wallet.getLtcBalance().getAmount());
	        }
		} catch (OKLinkException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
