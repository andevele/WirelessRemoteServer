package cn.feeyan.www.wirelessremoteserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RemoteService extends Service {

	private static final int LOCAL_PORT = 8998;

	private Boolean startrunflag = true;
	private int recivebuffersize = 30;

	private ServiceKey serviceKey;
	// Handler handler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		serviceKey = new ServiceKey();

		PendingIntent pintent = PendingIntent.getActivity(this, 0, new Intent(this, MyMainActivity.class), 0);
		Notification.Builder build = new Notification.Builder(this);
		build.setContentTitle("wireless remote");
		build.setSmallIcon(R.drawable.ic_launcher);
		build.setWhen(System.currentTimeMillis());
		build.setContentIntent(pintent);
		Notification notification = build.build();

		/**
		 * Make this service run in the foreground, supplying the ongoing
		 * notification to be shown to the user while in this state
		 * 把当前service设为前台服务，当退出activity时，service依然在运行状态
		 */
		startForeground(0x2016, notification);

		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		startrunflag = true;
		ClientThread clientThread = new ClientThread();
		new Thread(clientThread).start();
		return super.onStartCommand(intent, flags, startId);
	}

	class ClientThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			byte[] dataBuffer = new byte[recivebuffersize];
			// 把socket对象获取到的数据保存到缓存区中
			DatagramPacket inPacket = new DatagramPacket(dataBuffer, dataBuffer.length);

			DatagramSocket serviceSocket = null;
			if (serviceSocket == null) {
				try {
					// 创建数据包socket对象
					serviceSocket = new DatagramSocket(LOCAL_PORT);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (serviceSocket != null) {
				while (startrunflag) {
					try {
						// 从socket对象中获取客户端发送过来的数据包，保存到DatagramPacket对象中
						serviceSocket.receive(inPacket);
						// 得到数据包中的数据
						byte[] byteData = inPacket.getData();
						// 把字节码数组转换成对应的16进制字符串
						String hex = Utils.printHexString(byteData);
						// 把16进制的字符串转化成标准字符串，比如0x636c69656e747269676874转化成成clientright
						String receivedFromClient = Utils.HextoString(hex);
						// 把获取到的字符串传到ServiceKey对象中
						serviceKey.setKey(receivedFromClient);
						// 启动线程ServiceKey，发送按键事件
						serviceKey.start();

					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		startrunflag = false;
		super.onDestroy();
	}

}
