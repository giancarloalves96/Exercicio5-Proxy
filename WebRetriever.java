import java.io.*;
import java.net.*;
class WebRetriever {
	 Socket socket; 
	 OutputStream oStream; 
	 InputStream iStream;
	 
	 WebRetriever(String server, int port)	 throws IOException, UnknownHostException {
		 socket = new Socket(server, port);
		 oStream = socket.getOutputStream();
		 iStream = socket.getInputStream();
	 }
	 
	 void request(String path, String host) {
		PrintWriter outw = new PrintWriter(oStream, false);
		outw.print("GET " + path + " HTTP/1.1\r\n");
		outw.print("Host: " + host + ":8080\r\n");
		outw.print("Accept: */*\r\n Accept-Encoding: gzip, deflate\r\n User-Agent: runscope/0.1\r\n\r\n");
		outw.flush();
	 }

	 String get_ResponseString(){
		 int c;
		 String result = "";
		 
		 try{
			 while ((c = iStream.read()) != -1){
				 result += (char)c;
			 }
		 } catch (IOException e){
			 System.out.println("IOException");
		 }
		 return result;
	 }
	 
	 
	 public void close() {
		 try {
			 iStream.close(); oStream.close(); socket.close();
		 } catch (IOException e) {
			 System.out.println("IOException when trying to close connection");
		 }
	 }

}