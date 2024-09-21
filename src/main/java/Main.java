import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.security.SecureRandom;

public class Main {
    final static int PORT = 5000;
    final static int TAILLE = 65535;
    static byte[] data1 = new byte[TAILLE];
    static byte[] data2 = new byte[TAILLE];
    static byte[] data3 = new byte[TAILLE];
    static BigInteger p, g, k, a, b, s;
    static SecureRandom secureRandom = new SecureRandom();
    static int bitLength = 128;
    public static void main(String[] args) {
        try {
            p = BigInteger.probablePrime(bitLength, secureRandom);
            g = BigInteger.probablePrime(bitLength, secureRandom);
            do {
                k = BigInteger.probablePrime(bitLength, secureRandom);
            } while (k.compareTo(p) > 0);

            a = g.modPow(k, p);

            InetAddress inetAddress = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());

            data1 = (new String(String.valueOf(p))).getBytes();
            data2 = (new String(String.valueOf(g))).getBytes();
            data3 = (new String(String.valueOf(a))).getBytes();

            DatagramPacket packet1 = new DatagramPacket(data1, data1.length, inetAddress, PORT);
            DatagramPacket packet2 = new DatagramPacket(data2, data2.length, inetAddress, PORT);
            DatagramPacket packet3 = new DatagramPacket(data3, data3.length, inetAddress, PORT);

            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(10000);

            socket.send(packet1);
            socket.send(packet2);
            socket.send(packet3);

            byte[] dataReceived = new byte[65535];
            DatagramPacket packetReceived = new DatagramPacket(dataReceived, dataReceived.length, inetAddress, PORT);
            socket.receive(packetReceived);
            String chaine = new String(packetReceived.getData(), 0, packetReceived.getLength());
            b = new BigInteger(chaine);

            s = b.modPow(k, p);

            System.out.println("La cle publique 1: " + p);
            System.out.println("La cle publique 2: " + g);
            System.out.println("La cle secrete d'Alice: " + k);
            System.out.println("Resulting public key (A): " + a);
            System.out.println("Resulting public key (B): " + chaine);
            System.out.print("La cle secrete: " + s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}