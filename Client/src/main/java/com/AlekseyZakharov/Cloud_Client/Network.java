package com.AlekseyZakharov.Cloud_Client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;

public class Network {
    private final Socket socket;
    private final ObjectEncoderOutputStream output;
    private final ObjectDecoderInputStream input;
    private final String serverAddress = "localhost";
    private final int port = 8189;


    public Network() throws IOException {
        socket = new Socket(serverAddress, port);
        output = new ObjectEncoderOutputStream(socket.getOutputStream());
        input = new ObjectDecoderInputStream(socket.getInputStream());
    }

    public void sendMessage(Object msg) throws IOException {
        output.writeObject(msg);
    }

    public Object receiveMessage() throws IOException, ClassNotFoundException {
        return input.readObject();
    }


}
