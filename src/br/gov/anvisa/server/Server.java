package br.gov.anvisa.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Server {
	
	public static final int METHOD = 0;
	public static final int PATH_CONTENT = 1;
	public static final int PROTOCOL = 2;

	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(8081);
		System.out.println("Listenning");
		
		
		while(true){
			
			Socket clientSk = server.accept();
			
			
			if(clientSk.isConnected()){
				System.out.println("Connected");
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(clientSk.getInputStream()));
				
				String line = reader.readLine();
				String[] dadosReq 		= line.split(" ");
				
				while(!line.isEmpty()){
					System.out.println(line);
					line = reader.readLine();
				}
				
				
				String resourcePath 	= null;
				String status			= null;
				
				if(!dadosReq[PATH_CONTENT].equals("/") && !dadosReq[PATH_CONTENT].equals("/index.html")){
					resourcePath	= "error.html";
					status			= dadosReq[PROTOCOL]+" 404 Not Found\r\n";
				}else{
					resourcePath = "index.html";
					status			= dadosReq[PROTOCOL]+" 200 OK\r\n";
				}
				
				byte[] resource			= null;
		        File resourceFile = new File(resourcePath);
		        
		        if(resourceFile.exists()){
		        	resource = Files.readAllBytes(resourceFile.toPath());
		        }
				
				String header = getHttpHeader(status, resourcePath, resource.length);
				
				
				OutputStream response =  clientSk.getOutputStream();
				response.write(header.getBytes());
				response.write(resource);
				
				response.flush();
				response.close();
					
			}
		}
		
//		server.close();

	}
	
	private static String getHttpHeader(String status, String resourcePath, Integer contentLength) throws IOException{
		SimpleDateFormat formatador = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
        formatador.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date data 				= new Date();
        String dataFormatada 	= formatador.format(data) + " GMT";
        
        StringBuilder header 	= new StringBuilder(status);
        header.append("Location: http://localhost:8000/\r\n");
        header.append("Date: " + dataFormatada + "\r\n");
        header.append("Server: MeuServidor/1.0\r\n");
        header.append("Content-Type: text/html\r\n");
        header.append("Content-Length: " + contentLength + "\r\n");
        header.append("Connection: close\r\n");
        header.append("\r\n");
        
        return header.toString();
	}

}
















