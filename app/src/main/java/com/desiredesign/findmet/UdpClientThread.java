package com.desiredesign.findmet;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClientThread extends Thread{
    int dstPort;
    String ubicacion;

    DatagramSocket socket;
    InetAddress address;


    public UdpClientThread(String lat, String lon, String Time) {
        super();
        dstPort = 49676;
        ubicacion = lat+","+lon+","+Time;

    }




    @Override
    public void run() {
        boolean running = true;


        try {
            socket = new DatagramSocket();
            address= InetAddress.getByName("findmytaxi.zapto.org");


            String mensaje = ubicacion;

                //Convierto el mensaje a bytes
            byte[] buffer = mensaje.getBytes();

                //Creo un datagrama
                DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, address, dstPort);

                //Lo envio con send

                socket.send(pregunta);





        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
            }
        }

    }
}
