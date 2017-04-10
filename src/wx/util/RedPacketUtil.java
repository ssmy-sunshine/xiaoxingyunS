package wx.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import com.google.gson.Gson;

public class RedPacketUtil {
	
	public static void main(String[] args) {
		System.out.println(new Date());
		for (int i = 0; i < 20; i++) {
			ArrayList<Double> list=getRedPacket(0.1,9);//金额，个数
			System.out.println(new Gson().toJson(list));
		}
	}
	
	/**
	 * 随机分配红包
	 * @param totalMoney 红包总额
	 * @param num 红包个数
	 * @return 每个红包的金额列表
	 */
	public static ArrayList<Double> getRedPacket(double totalMoney,int num){
		double min=totalMoney/(num*2);//红包最小值为平均值的一半
		ArrayList<Double> list=new ArrayList<Double>();
		for(int i=1;i<num;i++){
			double safe_totalMoney=(totalMoney-(num-i)*min)/(num-i);
			//随机生成一个红包金额
			double money=Math.random()*(safe_totalMoney-min)+min;
			//保留2位小数
			BigDecimal money_bd=new BigDecimal(money);
			money=money_bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			//剩余金额
			totalMoney=totalMoney-money;
			//保留2位小数
			BigDecimal totalMoney_bd=new BigDecimal(totalMoney);
			totalMoney=totalMoney_bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			list.add(money);
		}
		//加入最后一个红包
		list.add(totalMoney);
		//随机打乱list
		Collections.shuffle(list);
		return list;
	}
	
}
