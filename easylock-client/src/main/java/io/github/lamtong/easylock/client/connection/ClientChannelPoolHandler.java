/*
 *  Copyright 2021 the original author, Lam Tong
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.lamtong.easylock.client.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handler for client's <code>channel pool</code>, implemented interface {@link ChannelPoolHandler}
 * and used in class {@link ChannelPoolProvider} when each channel is released, created and acquired.
 * For each channel, an instance of {@link ClientChannelInitializer}, which extends {@link ChannelInitializer},
 * is required to initialize the channel.
 *
 * @author Lam Tong
 * @version 1.3.2
 * @see ChannelPoolProvider
 * @see ClientChannelInitializer
 * @since 1.0.0
 */
public final class ClientChannelPoolHandler implements ChannelPoolHandler {

    private static final Logger logger = Logger.getLogger(ClientChannelPoolHandler.class.getName());

    @Override
    public void channelReleased(Channel channel) {
        // Ignore channel released information.
    }

    @Override
    public void channelAcquired(Channel channel) {
        // Ignore channel acquired information.
    }

    @Override
    public void channelCreated(Channel channel) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "Channel created, channel id = {0}", channel.id());
        }
        SocketChannel socketChannel = (SocketChannel) channel;
        socketChannel.pipeline()
                .addLast(new ClientChannelInitializer(new ClientHandler()));
    }

}
