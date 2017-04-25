package wx;

import com.google.gson.JsonObject;


/**
 * 测试
 */
public class Test {
	public static void main(String[] args) {

		JsonObject obj=new JsonObject();
		obj.addProperty("allprofit", 8.666);
		System.out.println(obj.get("allprofit").getAsDouble()+88);
	}
}
