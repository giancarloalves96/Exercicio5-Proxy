import java.io.*;
import java.net.*;
import java.util.StringTokenizer;


class WebServer implements Runnable {
	 Socket socket; 
	 OutputStream oStream; 
	 DataInputStream iStream;
	 String resource;
	 String full_request;
	 
	 WebServer(Socket s) throws IOException {
		 socket = s;
		 oStream = socket.getOutputStream();
		 iStream = new DataInputStream(socket.getInputStream());
	 }
	 
	 void get_Requisicao() {
		 try {
			 full_request="";
			 boolean header = true;
			 String message;
			 while ((message = iStream.readLine()) != null) {
				 if (message.equals(""))
					 break; // end of command block
				 StringTokenizer t = new StringTokenizer(message);
				 String token = t.nextToken(); // get first token
				 if (token.equals("GET")){ // if token is ”GET”
					 resource = t.nextToken(); // get second token
					 header = false;
				 }
				 if (header==false)
					 full_request += message + "\n";
			 }
			 System.out.println(full_request);
		 } catch (IOException e) {
			 System.out.println("Error when receiving request");
			 e.printStackTrace();
		 }
	 }
	 
	 void Retornar_Resposta() {
		 String address="", request="/";
		 boolean emptyRequest = true;
		 try {
			 char[] res = resource.toCharArray();
			 for(int i=1;i<resource.length();i++){
				 if(emptyRequest){
					 if(res[i]=='/'){
						 emptyRequest=false;
					 }
					 else
						 address += res[i];
				 }
				 else{
					 request += res[i];
				 }
			 }
			 WebRetriever w = new WebRetriever(address, 80);
			 w.request(request,address);
			 byte[] write_b = w.get_ResponseString().getBytes();
			 System.out.print(w.get_ResponseString());
			 oStream.write(write_b);
			 w.close();
		 } catch (IOException e) {
			 System.out.println("IOException");
			 e.printStackTrace();

		 }
	 }
	 
	 public void run() {
		 get_Requisicao();
		 Retornar_Resposta();
		 close();
	 }
	 	 
	
	 public void close() {
		 try {
			 iStream.close(); 
			 oStream.close(); 
			 socket.close();
		 } catch (IOException e) {
			 System.out.println("IOException in closing connection");
			 e.printStackTrace();
		 }
	 }
	 
	 public static void main(String args[]) {
		 try {
			 ServerSocket s = new ServerSocket(8080);
			 for (;;) {
				 WebServer w = new WebServer(s.accept());
				 Thread thr = new Thread(w);
				 thr.start();

			 }
		 } catch (IOException i) {
			 System.out.println("IOException in Server");
			 i.printStackTrace();
		 }
	 }

	 
} 