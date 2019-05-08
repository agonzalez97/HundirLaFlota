package hundirlaflota;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class BattleshipServer {

	DatagramSocket socket;
	 static int x=5;
	    static int y=5;
	    static List<Barco> barcos;
	    static String l;
	    static int n;
	    static String s;
	    static ArrayList<String> abecedario2;
	    static String [] abecedario = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
	    		"K", "L", "M","N","O","P","Q","R","S","T","U","V","W", "X","Y","Z" };
	
	public void init(int port) throws SocketException {
		socket = new DatagramSocket(port);
	}

	public void runServer() throws IOException {
		byte [] receivingData = new byte[1024];
		byte [] sendingData;
		InetAddress clientIP;
		int clientPort;

		while(true) {
			DatagramPacket packet = new DatagramPacket(receivingData,1024);
			socket.receive(packet);
			sendingData = processData(packet.getData(),packet.getLength());
			clientIP = packet.getAddress();
			clientPort = packet.getPort();
			packet = new DatagramPacket(sendingData,sendingData.length,clientIP,clientPort);
			socket.send(packet);
		}
	}

	private byte[] processData(byte[] data, int lenght) {
        String btn = new String(data,0,lenght);
        boolean trobat=false;
        boolean enfonsat=false;
        for (Barco barco : barcos) {
        	ArrayList<String> pos = barco.getPos();
			for (int i = 0; i <pos.size(); i++) {
				System.out.println("boton:"+btn);
				System.out.println("barco:"+barco.getPos().get(i));
				if (trobat!=true) {
					if (pos.get(i).equals(btn)) {
						trobat=true;
						barco.setVidas(barco.getVidas()-1);
						if (barco.getVidas()==0) {
							enfonsat=true;
							barco.setHundido(true);
							barco.setVidas(barco.getPos().size());
						}
					}
				}
			}
		}
       if(enfonsat==true){
        	btn = "Hundido";
       }else if(trobat==true){
    	   btn="Tocado";
       }else{
    	   btn="Agua";
       }
        
        return btn.getBytes();
    }
	
	public static void main(String[] args) {
		BattleshipServer server = new BattleshipServer();
		abecedario2= new ArrayList<String>();
    	cargarLetras();
    	cargarPosicionesBarcos();
		try {
			server.init(9999);
			server.runServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void cargarPosicionesBarcos(){
		barcos= new ArrayList<Barco>();
    	String posX;
    	String posY;
    	Barco b;
    	for (int i = 0; i < 2; i++) {
    		if (i==0) {
    			b = new Barco(i+1);
        		b.setNom(String.valueOf(i));
    			posX=calcularPosX(i);
    			b.addPos(posX);
			}else{
				boolean igual=true;
				b = new Barco(i+1);
				do {
		    		b.setNom(String.valueOf(i));
					posX=calcularPosX(i);
					for (Barco barco : barcos) {
						for (int j = 0; j <barco.getPos().size(); j++) {
							if (!barco.getPos().get(j).equals(posX)) {
								igual=false;
							}else{
								System.out.println("SON IGUALES");
							}
						}
					}
				} while (igual!=false);
				b.addPos(posX);
			}

			if (i==1) {
				boolean igual=true;
				do {
					posY=calcularPosY(i+1);
					for (Barco barco : barcos) {
						for (int j = 0; j <barco.getPos().size(); j++) {
							if (!barco.getPos().get(j).equals(posY)) {
								igual=false;
							}else{
								System.out.println("SON IGUALES");
							}
						}
					}
				} while (igual!=false);
				b.addPos(posY);
				//le paso el 1 para saber cual es el barco que va a poner si el de 1 o el de 2
							}
			b.to_String();
			barcos.add(b);
    	}
	}
	
	public static void cargarLetras(){
    	for (char i = 'A'; i <='Z'; i++) {
			abecedario2.add(String.valueOf(i));
		}
    }
	
    public static String calcularPosX(int i){
   	 String letra=null;
   	if (i==1) {
   		boolean dif=false;
   		do {
   			n= (int) (Math.random()*x);
	   	    	 int letraRandom=(int) (Math.random()*x);
	   	    	 int j=0;
	   	    	 l = null;
	   		    	for (String string : abecedario) {
	   					if (j==letraRandom) {
	   						l=string;
	   					}
	   					j++;
	   				}
	   		    letra= new String(n+l);
	   		    for (Barco barco : barcos) {
					for (String string : barco.getPos()) {
						dif=true;
						if (string.equals(letra)) {
							dif=false;
						}
					}
				}
			} while (dif!=true);   	
	    	
   	}else{
			 n= (int) (Math.random()*x);
	    	 int letraRandom=(int) (Math.random()*x);
	    	 int j=0;
	    	 l = null;
		    	for (String string : abecedario) {
					if (j==letraRandom) {
						l=string;
					}
					j++;
				}
	    	letra= new String(n+l);
		}
   	 
   	 return letra;
   }
   
   public static String calcularPosY(int i){

   	String posY=null;
   	int j=1;
   	String letraFinal = null;
   	for (String string : abecedario) {
			if (j==y) {
				letraFinal=string;
			}
			j++;
		}    	 
   	if (l.equals("A")&& n==0) {
   		int r=(int) (Math.random()*2);
   		if (r==0) {//derecha
				posY=new String(1+"A");
			}else if(r==1){//abajo
				posY=new String(0+"B");
			}
		}else if(l.equals("A")&& n==x-1){
			int r=(int) (Math.random()*2);
   		if (r==0) {//izquierda
				posY=new String((x-2)+"A");
			}else if(r==1){//abajo
				posY=new String((x-1)+"B");
			}
		}else if(l.equals("A")){ 
			int r=(int) (Math.random()*3);
   		if (r==0) {//izquierda
				posY=new String((n-1)+"A");
			}else if(r==1){//derecha
				posY=new String((n+1)+"A");
			}else if(r==2){//abajo
				posY=new String(n+"B");
			}
		}else if(l.equals(letraFinal)&& n==0){
			int r=(int) (Math.random()*2);
   		if (r==0) {//derecha
				posY=new String(1+letraFinal);
			}else if(r==1){//arriba
				posY=new String(0+"D");
			}
		}else if(l.equals(letraFinal)&& n==x-1){
			int r=(int) (Math.random()*2);
   		if (r==0) {//izquierda
				posY=new String((x-2)+letraFinal);
			}else if(r==1){//arriba
				posY=new String((x-1)+"D");
			}
		}else if(l.equals(letraFinal)){
			int r=(int) (Math.random()*3);
   		if (r==0) {//izquierda
				posY=new String((n-1)+letraFinal);
			}else if(r==1){//derecha
				posY=new String((n+1)+letraFinal);
			}else if(r==2){//arriba
				posY=new String(n+"D");
			}
		}else if(n==0){
			int r=(int) (Math.random()*3);
   		if (r==0) {//abajo
   			String let=calcularLetraAbajo(l);
				posY=new String(n+let);
			}else if(r==1){//derecha
				posY=new String((n+1)+l);
			}else if(r==2){//arriba
				String let= calcularLetraArriba(l);
				posY=new String(n+let);
			}
		}else if(n==x-1){
			int r=(int) (Math.random()*3);
   		if (r==0) {//abajo
   			String let=calcularLetraAbajo(l);
				posY=new String(n+let);
			}else if(r==1){//izquierda
				posY=new String((n-1)+l);
			}else if(r==2){//arriba
				String let= calcularLetraArriba(l);
				posY=new String(n+let);
			}
		}else{
				 int r=(int) (Math.random()*4);
				
				 if (r==0) {//derecha
					posY=new String((n+1)+l);
				 }else if (r==1) {//izquierda
					posY=new String((n-1)+l);
				 }else if (r==2) {//abajo
					String letra=null;
					boolean trobat=false;
					for (String string : abecedario) {
						if (trobat) {
							letra=string;
							trobat=false;
						}
						if (l.equals(string)) {
							trobat=true;
						}
					}
					posY=new String(n+letra);
				 }else if (r==3) {//arriba
					String letra=null;
					boolean trobat=false;
					for (String string : abecedario) {
						if (l.equals(string)) {
							trobat=true;
						}
						if (trobat==false) {
							letra=string;
						}
					}
					posY=new String(n+letra);
				 	}	
			}
   	
   	return posY;
   }
   
   public static String calcularLetraArriba(String letra){
   	boolean trobat=false;
   	String l=null;
   	for (String string : abecedario2) {
			if (letra.equals(string)) {
				trobat=true;
			}
			if (trobat==false) {
				l=string;
			}
		}
   	return l;
   }
   
   public static String calcularLetraAbajo(String letra){
   	boolean trobat=false;
   	String l = null;
   	for (String string : abecedario2) {
			if (trobat) {
				l=string;
				trobat=false;
			}
			if (string.equals(letra)) {
				trobat=true;
			}
		}
   	return l;
   }

}
