package br.gov.anvisa.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("www.google.com.br", 80);
		
		if(socket.isConnected()){
			System.out.println("conected");
			
			OutputStream write = socket.getOutputStream();
			String requisicao = 
						"GET / HTTP/1.1\r\n"+
						"Host: www.google.com.br\r\n"+
						"\r\n";
			write.write(requisicao.getBytes());
			write.flush();
			
			Scanner read = new Scanner(socket.getInputStream());
			
			while(read.hasNext()){
				System.out.println(read.nextLine());
			}
		}else
			System.out.println("error");
		
		

		socket.close();
	}

}
