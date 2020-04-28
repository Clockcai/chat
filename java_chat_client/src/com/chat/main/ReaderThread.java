package com.chat.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.SimpleDateFormat;

import com.chat.entity.Messages;
import com.chat.util.JsonUtils;

public class ReaderThread extends Thread {

	private Socket socket;
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:ss");;

	public ReaderThread(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			is = socket.getInputStream();
			isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr);
			String regex = "^\\{\"content\".*";
			while (true) {
				String msgJson = br.readLine();
				if (msgJson.matches(regex)) {
					//�������Ϣ���ȡ��Ϣ
					Messages msg = JsonUtils.jsonToPojo(msgJson, Messages.class);
					System.out.println(msg.getUser().getName() + " " + sdf.format(msg.getDate()));
					System.out.println(msg.getContent());

				} else {
					// ���������Ϣ��ֱ�Ӷ�ȡ
					System.out.println("�ھQ��"+msgJson);
				}

			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					br.close();
					isr.close();
					is.close();
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
