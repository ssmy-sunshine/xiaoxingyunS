package wx.biz;

import java.util.ArrayList;
import java.util.Collections;

import wx.db.RedPacketDB;
import wx.db.RedPacketDetailDB;
import wx.entity.RedPacket;
import wx.entity.RedPacketDetail;
import wx.exception.BizException;
import wx.util.InfoUtil;

public class RedPacketBiz {
	/*一个红包的最大金额*/
	private static final int MAX_MONEY=200;
	/*一个红包的最小金额*/
	private static final double MIN_MONEY=0.1d;
	/*分配的最小金额*/
	private static final double MIN_PACKET=0.01d;
	
	/**
	 * 创建红包
	 * @param totalMoney 红包总金额
	 * @param count 红包分配个数
	 */
	public int create(RedPacket redPacket) throws Exception{
		//验证金额是否有效
		double totalMoney=redPacket.getMoney();
		int count=redPacket.getCount();
		checkMoney(totalMoney, count);
		//随机分配红包金额
		ArrayList<Double> list=carveMoney(totalMoney, count, redPacket.getTaketype());
		//红包口令
		int pass=getPacketPass();
		//实例化红包对象
		redPacket.setPass(pass);
		//入库红包
		new RedPacketDB().insert(redPacket);
		//入库每个分配好的红包
		RedPacketDetailDB mRedPacketDetailDB=new RedPacketDetailDB();
		for (Double packetMoney : list) {
			RedPacketDetail redPacketDetail=new RedPacketDetail();
			redPacketDetail.setPass(pass);
			redPacketDetail.setMoney(packetMoney);
			mRedPacketDetailDB.insert(redPacketDetail);
		}
		return pass;
	}
	
	/**
	 * 根据口令抢红包
	 * @param pass 口令
	 * @param takeuser 用户id
	 * @return 成功true; 失败false;
	 */
	public void takeByPass(int pass,String takeuser) throws Exception{
		RedPacketDetailDB mRedPacketDetailDB=new RedPacketDetailDB();
		//根据口令查询可抢的红包
		int canTakeId=mRedPacketDetailDB.getCanTakeId(pass);
		if(canTakeId==0) throw new BizException("红包已抢完");
		//给红包设置用户信息(抢红包)
		boolean success=mRedPacketDetailDB.updateTakeUser(canTakeId, takeuser);
		if(!success) throw new BizException("手慢了,红包已抢光");
	}
	
	/**生成红包口令,6位随机数 */
	private int getPacketPass() throws Exception{
		//6位随机数
		int pass=(int)((Math.random()*9+1)*100000);
		//查询是否已存在
		boolean isExist=new RedPacketDB().isPassExist(pass);
		if(isExist){
			//如果已存在,则递归
			pass=getPacketPass();
		}
		return pass;
	}
	
	/**验证金额是否有效 */
	private void checkMoney(double totalMoney,int count) throws BizException{
		if(totalMoney>MAX_MONEY) throw new BizException("一个红包最多为"+MAX_MONEY+"元");
		if(totalMoney<MIN_MONEY) throw new BizException("一个红包最少为"+MIN_MONEY+"元");
		double ave=totalMoney/count;//每个红包平均值
		if(ave<MIN_PACKET) throw new BizException("每人至少要能分到"+MIN_PACKET+"元");
	}
	
	/**
	 * 随机分配红包
	 * @return 每个红包的金额列表
	 */
	private ArrayList<Double> carveMoney(double totalMoney,int count,int taketype){
		ArrayList<Double> list=new ArrayList<Double>();
		if(taketype==1){
			//1平均分
			double ave=InfoUtil.get2Double(totalMoney/count);//每个红包平均值
			for(int i=0;i<count;i++){
				list.add(ave);
			}
			return list;
		}else{
			//0拼手气
			double minPacket=totalMoney/(count*2);//红包最小值为平均值的一半
			for(int i=1;i<count;i++){
				double safeMoney=(totalMoney-(count-i)*minPacket)/(count-i);
				//随机生成一个红包金额
				double onePacket=Math.random()*(safeMoney-minPacket)+minPacket;
				//保留2位小数
				onePacket=InfoUtil.get2Double(onePacket);
				//剩余金额
				totalMoney -= onePacket;
				//保留2位小数
				totalMoney=InfoUtil.get2Double(totalMoney);
				//加入集合
				list.add(onePacket);
			}
			//加入最后一个红包
			list.add(totalMoney);
			//随机打乱list
			Collections.shuffle(list);
			return list;
		}
	}
	
}
