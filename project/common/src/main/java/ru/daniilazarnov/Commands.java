package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.daniilazarnov.commands.Command;
import ru.daniilazarnov.commands.CommandUnknown;
import ru.daniilazarnov.commands.CommandUpload;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Commands {
    /*
    UF - Upload file;
    UNKNOWN - default unknown command;
    */

    UF ("UF", (byte) 1, new CommandUpload()),
    UNKNOWN ("UNKNOWN", Byte.MIN_VALUE, new CommandUnknown());


    byte signal;
    String nameCommand;
    private final Command commandApply;

    private static final Map<Byte, Commands> commandsMap = Arrays.stream(Commands.values())
            .collect(Collectors.toMap(commands -> commands.signal, Function.identity()));

    Commands(String nameCommand, byte signal, Command command) {
        this.signal = signal;
        this.nameCommand = nameCommand;
        this.commandApply = command;
    }

    public void sendToServer(ChannelHandlerContext ctx) {
        commandApply.send(ctx, signal);
    }

    public void responseToClient(ChannelHandlerContext ctx, ByteBuf buf) {
        commandApply.response(ctx, buf);
    }

    public static Commands getCommand(byte code) {
        return commandsMap.getOrDefault(code, UNKNOWN);
    }
}
