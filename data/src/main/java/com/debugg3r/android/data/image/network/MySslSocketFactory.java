package com.debugg3r.android.data.image.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class MySslSocketFactory extends SSLSocketFactory {
    private SSLSocketFactory mSslSocketFactory;

    public MySslSocketFactory(SSLSocketFactory sslSocketFactory) {
        super();
        this.mSslSocketFactory = sslSocketFactory;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return mSslSocketFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return mSslSocketFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        SSLSocket socket = (SSLSocket) mSslSocketFactory.createSocket(s, host, port, autoClose);
        socket.setEnabledProtocols(new String[] { "TLSv1.1", "TLSv1.2" });
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        SSLSocket socket = (SSLSocket) mSslSocketFactory.createSocket(host, port);
        socket.setEnabledProtocols(new String[] { "TLSv1.1", "TLSv1.2" });
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        SSLSocket socket = (SSLSocket) mSslSocketFactory.createSocket(host, port, localHost, localPort);
        socket.setEnabledProtocols(new String[] { "TLSv1.1", "TLSv1.2" });
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        SSLSocket socket = (SSLSocket) mSslSocketFactory.createSocket(host, port);
        socket.setEnabledProtocols(new String[] { "TLSv1.1", "TLSv1.2" });
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        SSLSocket socket = (SSLSocket) mSslSocketFactory.createSocket(address, port, localAddress, localPort);
        socket.setEnabledProtocols(new String[] { "TLSv1.1", "TLSv1.2" });
        return socket;
    }
}
