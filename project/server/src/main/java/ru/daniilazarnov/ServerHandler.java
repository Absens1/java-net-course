package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private Map<Integer, FileLoaded> uploadedFiles;
    private FileWorker fileWorker;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        String storageDir = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Storage";
        System.out.println("Подключился клиент " + ctx.channel().remoteAddress().toString());
        uploadedFiles = new HashMap<>();
        fileWorker = new FileWorker(storageDir, "");
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;

        if (buf.readableBytes() > 0) {
            byte b = buf.readByte();
            Commands command = Commands.getCommand(b);
            command.receiveAndSend(ctx, buf, fileWorker, uploadedFiles);
        }
        buf.release();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Отключился клиент " + ctx.channel().remoteAddress().toString());
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
