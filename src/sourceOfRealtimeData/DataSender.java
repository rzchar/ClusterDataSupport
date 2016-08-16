package sourceOfRealtimeData;

import java.util.Calendar;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

public class DataSender {
	private String fakeData(){
		Random r = new Random();
		double cpu = r.nextDouble() * 0.3 + 0.5;
		long l = Runtime.getRuntime().freeMemory();
		long netsent = (1<<30)+r.nextInt(1<<28);
		long netreceive = (1<<30)+r.nextInt(1<<28);
		JSONObject jo = new JSONObject();
		try {
			jo.accumulate("cpu", cpu);
			jo.accumulate("nts", netsent);
			jo.accumulate("ntr", netreceive);
			jo.accumulate("mem", l);
			jo.accumulate("createtime", Calendar.getInstance().getTimeInMillis());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("backend :: " + jo.toString());
		return jo.toString();
	}
	
	static public void main(String[] args){
		DataSender ds= new DataSender();
		System.out.println(ds.fakeData());
	}
}
