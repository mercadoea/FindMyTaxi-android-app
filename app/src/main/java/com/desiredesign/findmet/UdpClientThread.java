package com.desiredesign.findmet;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClientThread extends Thread{
    int dstPort;
    String ubicacion;
    int taxi;

    DatagramSocket socket;
    InetAddress address;
    //InetAddress[] addresses;

    public UdpClientThread(String lat, String lon, String Time, int taxi) {
        super();
        dstPort = 49676;
        ubicacion = lat+","+lon+","+Time;
        this.taxi = taxi;

    }




    @Override
    public void run() {
        boolean running = true;


        try {
            socket = new DatagramSocket();
            //addresses[0]= InetAddress.getByName("findmytaxi.zapto.org");
            //addresses[1]= InetAddress.getByName("http://ec2-3-17-36-193.us-east-2.compute.amazonaws.com/");
            address= InetAddress.getByName("findmytaxi.ddns.net");
            //address=InetAddress.getByName("179.13.192.243");
            //danielaaddress= InetAddress.getByName("18.191.164.217");
            //address=InetAddress.getByName("ec2-18-218-6-189.us-east-2.compute.amazonaws.com");

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
