package cn.feeyan.www.wirelessremoteserver;

import android.app.Instrumentation;
import android.view.KeyEvent;

public class ServiceKey {
	private String ReceivedKey;

	Instrumentation inst = new Instrumentation();

	public void setKey(String receivedFromClient) {
		this.ReceivedKey = receivedFromClient;
	}

	Runnable ServiceKeyThread = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 获取实际按键值
			int keycode = getKey(ReceivedKey);
			// 发送按键值
			inst.sendKeyDownUpSync(keycode);
		}

	};

	// 启动线程ServiceKeyThread
	public void start() {
		// TODO Auto-generated method stub
		new Thread(ServiceKeyThread).start();
	}

	// 把接收到的按键信息转化成实际的按键值
	protected int getKey(String ReceivedKey) {
		// TODO Auto-generated method stub
		int keycode = 0;
		if (ReceivedKey.contains("left")) {

			keycode = KeyEvent.KEYCODE_DPAD_LEFT;

		} else if (ReceivedKey.contains("right")) {

			keycode = KeyEvent.KEYCODE_DPAD_RIGHT;

		} else if (ReceivedKey.contains("up")) {

			keycode = KeyEvent.KEYCODE_DPAD_UP;

		} else if (ReceivedKey.contains("down")) {

			keycode = KeyEvent.KEYCODE_DPAD_DOWN;

		} else if (ReceivedKey.contains("ok")) {

			keycode = KeyEvent.KEYCODE_DPAD_CENTER;

		} else if (ReceivedKey.contains("exit")) {

			keycode = KeyEvent.KEYCODE_BACK;

		} else if (ReceivedKey.contains("home")) {

			keycode = KeyEvent.KEYCODE_HOME;

		} else if (ReceivedKey.contains("menu")) {

			keycode = KeyEvent.KEYCODE_MENU;

		} else if (ReceivedKey.contains("power")) {

			keycode = KeyEvent.KEYCODE_POWER;

		} else if (ReceivedKey.contains("mute")) {

			keycode = KeyEvent.KEYCODE_MUTE;

		} else if (ReceivedKey.contains("source")) {
			// tv source 通道根据具体客户的按键值来确定，此处留待用户自行修改
			// keycode = KEYCODE_SOURCE;
			keycode = 262;
			;
		}
		return keycode;
	}

}
