package sourceOfRealtimeData;

import java.util.Calendar;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

public class DataSender {
	private String fakeData() {
		String fakeString = "{\"memory\":\"3698.0\","
			+ "\"networkReceive\":\"25255433896\",\"networkSend\":\"56800284169\","
			+ "\"createTime\":\"1471560966101\",\"cpu\":\"0.629\"}";
		Random r = new Random();
		double cpu = r.nextDouble() * 0.2 - 0.1;
		long l = Runtime.getRuntime().freeMemory();
		long netsent = (1 << 30) + r.nextInt(1 << 28);
		long netreceive = (1 << 30) + r.nextInt(1 << 28);
		try {
			JSONObject jojo = new JSONObject(fakeString);
			JSONObject jo = new JSONObject();
			jo.accumulate("cpu", jojo.getDouble("cpu") + cpu);
			jo.accumulate("networkSend", netsent);
			jo.accumulate("networkReceive", netreceive);
			jo.accumulate("memory", jojo.getDouble("memory") + l * 0.01);
			jo.accumulate("createTime",
				Calendar.getInstance().getTimeInMillis());
			JSONObject jojori = new JSONObject();
			jojori.accumulate("data", jo);
			return jojori.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "{}";
	}

	static public void main(String[] args) {
		String uu = "http://localhost:8080/ClusterData/servlet/AddRealTimeData";
		DataSender ds = new DataSender();
		Random r = new Random();
		int i = 0;
		while (i < 10) {
			String dt = ds.fakeData();
			System.out.println("Send by qyd: " + dt);
			HttpRequester.httpPostWithJSON(uu, dt);
			try {
				Thread.sleep(r.nextInt(200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
		}
	}
}
