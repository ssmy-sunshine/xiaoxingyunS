package wx;


/**
 * 测试
 */
public class Test {
	public static void main(String[] args) {
//		RedPacketBiz redBiz=new RedPacketBiz(200, 8);
//		try {
//			for (int i = 0; i < 10; i++) {
//				redBiz.create();
//			}
//		} catch (BizException e) {
//			e.printStackTrace();
//		}
		double r=Math.random();
		System.out.println(r);
		System.out.println(r*9);
		System.out.println(r*9+1);
		System.out.println((r*9+1)*100000);
		System.out.println((int)((r*9+1)*100000));
	}
}
