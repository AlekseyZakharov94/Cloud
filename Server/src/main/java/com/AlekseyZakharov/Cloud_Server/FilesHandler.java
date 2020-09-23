package com.AlekseyZakharov.Cloud_Server;


import com.AlekseyZakharov.Cloud_Common.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

public class FilesHandler extends SimpleChannelInboundHandler<Request> {
    private Path sourceDir;

    public FilesHandler(Path sourceDir) {
        this.sourceDir = sourceDir;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        if (request instanceof AuthorizationRequest) {
            handleAuthorization((AuthorizationRequest) request);
        } else if (request instanceof SendFileRequest) {
            handleSendFile((SendFileRequest) request);
        } else if (request instanceof DeleteFileRequest) {
            handleDeleteFile((DeleteFileRequest) request);
        }else if (request instanceof DownloadFileRequest){
            handleDownloadFile((DownloadFileRequest)request,ctx);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client exit");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    private void handleAuthorization(AuthorizationRequest request) {
        if (!Files.exists(sourceDir.resolve(request.getLogin()))) {
            try {
                Files.createDirectory(sourceDir.resolve(request.getLogin()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSendFile(SendFileRequest request) {
        String filename = request.getPath();
        byte[] data = request.getData();
        try {
            Files.write(sourceDir.resolve(filename), data, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDownloadFile(DownloadFileRequest request, ChannelHandlerContext ctx) {
        String filePath = request.getPath();
        try {
            System.out.println(sourceDir.resolve(filePath).normalize().toAbsolutePath().toString());
            ctx.writeAndFlush(Files.readAllBytes(sourceDir.resolve(filePath).normalize().toAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteFile(DeleteFileRequest request) {
        Path filePath = Paths.get(request.getPath()).normalize().toAbsolutePath();
        if (Files.isDirectory(filePath)){
            try (Stream<Path> walk = Files.walk(filePath)) {
                walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
