package ru.daniilazarnov.commands;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.daniilazarnov.FileLoaded;
import ru.daniilazarnov.FileWorker;

import java.util.Map;

public interface Command {
    void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal);

    void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles, byte signal);

}
