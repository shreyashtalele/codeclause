import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

// EncryptionUtils class for handling encryption and decryption
class EncryptionUtils {
    private static final String ALGORITHM = "AES";

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128); // Key size
        return keyGen.generateKey();
    }

    public static String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }
}

// VPNServer class for handling server-side logic
class VPNServer {

    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            // Generate a secret key
            SecretKey secretKey = EncryptionUtils.generateKey();
            System.out.println("Server Key: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));

            // Set up server socket
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Waiting for connections...");

            // Accept client connection
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            // Set up I/O streams
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Communication loop
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // Decrypt the message received from client
                String decryptedMessage = EncryptionUtils.decrypt(inputLine, secretKey);
                System.out.println("Received (Decrypted): " + decryptedMessage);

                // Encrypt and send a response
                String response = "Server received: " + decryptedMessage;
                String encryptedResponse = EncryptionUtils.encrypt(response, secretKey);
                out.println(encryptedResponse);
            }

            // Close resources
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// VPNClient class for handling client-side logic
class VPNClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            // Generate a secret key (should be the same key for real application)
            SecretKey secretKey = EncryptionUtils.generateKey();
            System.out.println("Client Key: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));

            // Set up socket connection to server
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            System.out.println("Connected to server.");

            // Set up I/O streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Send an encrypted message to server
            String message = "Hello, Server!";
            String encryptedMessage = EncryptionUtils.encrypt(message, secretKey);
            out.println(encryptedMessage);

            // Receive and decrypt the response
            String response = in.readLine();
            String decryptedResponse = EncryptionUtils.decrypt(response, secretKey);
            System.out.println("Received from server (Decrypted): " + decryptedResponse);

            // Close resources
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
