package wx.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 商家服务
 */
public class BusSL extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 设置编码
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		long t=System.currentTimeMillis();
		
		// 获取请求要做的方法
		String method = request.getParameter("method");
		// 返回客户端的信息
		String reback = "1";// 服务器响应异常
		
		try {
//			if ("login".equals(method)) {
//				/*
//				 * 登陆 
//				 * 账户密码错误返回2 
//				 * 正确返回"BusName,WaitSale,ScoreRate,Wife,WeChatID,AppID,Secret"//Gson只能封装一个对象
//				 */
//				Result res = new Result();
//				int BusNo = Integer.parseInt(request.getParameter("BusNo"));
//				String PW = request.getParameter("PW");
//				Business bus = DBBusiness.getBusiness(BusNo, PW);
//				if (bus != null) {
//					res.Code = 0;
//					res.Msg=bus.BusName+","+bus.WaitSale+","+bus.ScoreRate+","+bus.Wife;
//				} else {
//					res.Code = 2;
//				}
//				reback = res.toString();
//				
//			} else if ("check".equals(method)) {
//				/*
//				 * 查询兑换码 
//				 * 传兑换码+商家编号 
//				 * {"Code":0,"Msg":"ExScore_ExCount"}未兑换
//				 * {"Code":2,"Msg":"兑换码不存在"}
//				 * {"Code":3,"Msg":"ExScore_ExCount"}//已兑换
//				 */
//				int BusNo = Integer.parseInt(request.getParameter("BusNo"));
//				int ExCode = Integer.parseInt(request.getParameter("ExCode"));
//				Result rs=new Result();
//				//查询 申请的积分,兑换的次数
//				ExScore mExScore=DBExScore.getExScore(BusNo,ExCode);
//				if (mExScore == null) {
//					rs.Code = 2;//兑换码不存在
//				}else if(mExScore.ExTime>1000){
//					rs.Code = 3;//已兑换
//					rs.Msg=mExScore.ExScore+"_"+mExScore.ExCount;
//				}else{
//					rs.Code = 0;//未兑换
//					rs.Msg=mExScore.ExScore+"_"+mExScore.ExCount;
//				}
//				reback =rs.toString();
//				
//			} else if ("exchang".equals(method)) {
//				/*
//				 * 确定兑换 
//				 * 0兑换成功 
//				 */
//				int BusNo = Integer.parseInt(request.getParameter("BusNo"));
//				int ExCode = Integer.parseInt(request.getParameter("ExCode"));
//				float ExMoney = Float.parseFloat(request.getParameter("ExMoney"));
//				System.out.println("BusNo="+BusNo+"/ExCode="+ExCode+"/ExMoney="+ExMoney);
//				//查询申请的积分 兑换的次数
//				ExScore unExScore=DBExScore.getExScore(BusNo,ExCode);
//				//更新CurScore
//				DBCurScore.UpdateCurScore(BusNo, unExScore.UserID, unExScore.ExScore);
//				//更新ExScore
//				DBExScore.upExTime(unExScore.ID,ExMoney);
//				reback = "0";
//				
//			} else if ("wait".equals(method)) {
//				/*
//				 * 等位 
//				 * 返回客户端的是:{Code=WaitNo,Msg=二维码ticket}
//				 */
//				int BusNo = Integer.parseInt(request.getParameter("BusNo"));
//				// 查询商家今天最新排位号 Where BusNo=?,TimeStart=今天 倒序 Limit 1
//				int WaitNo = DBWait.getWaitNo(BusNo);
//				// 然后加1 插入
//				WaitNo++;
//				DBWait.addWaitNo(BusNo, WaitNo);
//				// 返回客户端的是:{Code=WaitNo,Msg=二维码下载地址}
//				Result rs = new Result();
//				rs.Code=WaitNo;
//				//生成二维码ticket,参数:BusNo+WaitNo
//				int param = Integer.parseInt(BusNo + "" + WaitNo);
//				//插入QrCode表 二维码带参:QrCodeID
//				int QrCodeID=DBQrCode.addQrCode(1,param);
//				//生成带参二维码,参数:QrCodeID
//				rs.Msg = TicketSL.getQrTicket(BusNo,QrCodeID,true);
//				reback = rs.toString();
//				
//			} else if ("seat".equals(method)) {
//				/*
//				 * 入座
//				 * 传参:BusNo,WaitNo
//				 * 返回:
//				 * {Code="1",Msg="服务器响应异常"}
//				 * {Code="2",Msg="不存在"}
//				 * {Code="3",Msg="正在入座的排队时长"}
//				 * {Code="4",Msg="已经入座的排队时长"}
//				 * {Code="5",Msg="未按时入座"}
//				 */
//				Result rs=new Result();
//				int BusNo = Integer.parseInt(request.getParameter("BusNo"));
//				int WaitNo = Integer.parseInt(request.getParameter("WaitNo"));
//				//更新排位号的入座时间
//				int upCount=DBWait.upTimeEnd(BusNo,WaitNo);
//				//获取开始排队的时间
//				Wait wait=DBWait.getWaitInfo(BusNo, WaitNo);
//				if (upCount>0) {
//					//upCount>0  刚刚入座的 则使之前的置为过期
//					DBWait.upOutDate(BusNo,WaitNo);
//					rs.Code=3;
//					System.out.println("wait.TimeEnd="+wait.TimeEnd+"==wait.TimeStart="+wait.TimeStart);
//					rs.Msg=""+((wait.TimeEnd-wait.TimeStart)/60000+1);//返回排队时长
//				}else{
//					//upCount=0 不存在或者已入座的或未按时入座的;
//					if (wait==null) {
//						rs.Code=2;
//					}else if(wait.TimeEnd!=0){
//						rs.Code=4;
//						System.out.println("wait.TimeEnd="+wait.TimeEnd+"==wait.TimeStart="+wait.TimeStart);
//						rs.Msg=""+((wait.TimeEnd-wait.TimeStart)/60000+1);//返回排队时长
//					}else{
//						rs.Code=5;
//					}
//				}
//				
//				reback=rs.toString();
//			}else if ("dataScore".equals(method)) {
//				/*
//				 * TODO 返回积分明细
//				 * 1.获取大于oldScoreID的最新的兑换明细
//				 * 2.获取大于oldScanID的扫描明细
//				 */
//			}else if ("update".equals(method)) {
//				/*
//				 * 更新指定字段(登陆密码,等位折扣,积分比率,wife)
//				 * 成功返回0
//				 */
//				int BusNo = Integer.parseInt(request.getParameter("BusNo"));
//				String Columns=request.getParameter("Columns");
//				String NewData=request.getParameter("NewData");
//				DBBusiness.update(BusNo,Columns,NewData);
//				reback="0";
//			}else if ("group".equals(method)) {
//				/*
//				 * 查看最新的团购劵列表
//				 */
//				int BusNo = Integer.parseInt(request.getParameter("BusNo"));
//				int NewID = Integer.parseInt(request.getParameter("NewID"));//本地最新ID
//				ArrayList<Group> groupList=DBGroup.getGroupList(BusNo,NewID);
//				reback=App.Gson.toJson(groupList);
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//返回客户端的 默认是"1" 服务器响应异常
		System.out.println("方法method=" + method + "==返回reback=" + reback);
		System.out.println("当前时间="+System.currentTimeMillis()+ "==响应时长=" + (System.currentTimeMillis()-t));
		PrintWriter out = response.getWriter();
		out.print(reback);
		out.flush();
		out.close();
	}

}
