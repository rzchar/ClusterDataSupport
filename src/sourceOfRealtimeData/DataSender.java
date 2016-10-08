package sourceOfRealtimeData;

import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class DataSender {
	private String fakeData(String machineId) {
		Random r = new Random();
		double cpu = (r.nextDouble() > 0.85) ? r.nextDouble() * 0.95
			: r.nextDouble() * 0.04 + 0.03;
		long l = Runtime.getRuntime().freeMemory();
		long netsent = (1 << 30) + r.nextInt(1 << 28);
		long netreceive = (1 << 30) + r.nextInt(1 << 28);
		try {
			JSONObject jo = new JSONObject();
			jo.accumulate("cpu", cpu);
			jo.accumulate("networkSend", netsent);
			jo.accumulate("networkReceive", netreceive);
			jo.accumulate("memory", l);
			jo.accumulate("createTime",
				Calendar.getInstance().getTimeInMillis());
			JSONObject jojori = new JSONObject();
			jojori.accumulate("data", jo);
			jojori.accumulate("machineid", machineId);
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
				for (int i = 0; i < 20; i++) {
					String dt = ds.fakeData("127.0.0." + i);
					System.out.println("Send by qyd: " + dt);
					new SingleInfoSender(uu, dt).start();
				}
				try {
					Thread.sleep(r.nextInt(10) + 200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("fake data thread exit");
		}

		static public class SingleInfoSender extends Thread {
			String _url;
			String _content;

			SingleInfoSender(String url, String content) {
				_url = url;
				_content = content;
			}

			@Override
			public void run() {
				HttpRequester.httpPostWithJSON(_url, _content);
			}

		}
	}

	static public void main(String[] args) {
		FDThread fd = new FDThread();
		fd.start();
		Scanner sc = new Scanner(System.in);
		String str;
		while (sc.hasNext()) {
			str = sc.nextLine();
			if (str.equals("exit")) {
				fd.exit();
				break;
			}
		}
		sc.close();
	}
}
