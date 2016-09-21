package sourceOfRealtimeData;

import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class DataSender {
	private String fakeData() {
		String fakeString = "{\"memory\":\"3698.0\","
			+ "\"networkReceive\":\"25255433896\",\"networkSend\":\"56800284169\","
			+ "\"createTime\":\"1471560966101\",\"cpu\":\"0.629\"}";
		Random r = new Random();
		double cpu = (r.nextDouble() > 0.8) ? r.nextDouble() * 0.95
			: r.nextDouble() * 0.04 + 0.03;
		long l = Runtime.getRuntime().freeMemory();
		long netsent = (1 << 30) + r.nextInt(1 << 28);
		long netreceive = (1 << 30) + r.nextInt(1 << 28);
		try {
			JSONObject jojo = new JSONObject(fakeString);
			JSONObject jo = new JSONObject();
			jo.accumulate("cpu",cpu);
			jo.accumulate("networkSend", netsent);
			jo.accumulate("networkReceive", netreceive);
			jo.accumulate("memory", jojo.getDouble("memory") + l * 0.01);
			jo.accumulate("createTime",
				Calendar.getInstance().getTimeInMillis());
			JSONObject jojori = new JSONObject();
			jojori.accumulate("data", jo);
			jojori.accumulate("machineid", "default");
			return jojori.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "{}";
	}

	static public class FDThread extends Thread {
		public void exit() {
			goon = false;
		}

		private boolean goon = true;

		@Override
		public void run() {
			String uu = "http://localhost:8080/ClusterData/servlet/AddRealTimeData";
			DataSender ds = new DataSender();
			Random r = new Random();
			while (goon) {
				String dt = ds.fakeData();
				System.out.println("Send by qyd: " + dt);
				HttpRequester.httpPostWithJSON(uu, dt);
				try {
					Thread.sleep(r.nextInt(10)+200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("fake data thread exit");
		}
	}

	static public void main(String[] args) {
		FDThread fd = new FDThread();
		fd.start();
		Scanner sc = new Scanner(System.in);
		String str;
		while (sc.hasNext()) {
			str = sc.nextLine();
			if (str.equals( "exit")) {
				fd.exit();
				break;
			}
		}sc.close();
	}
}
