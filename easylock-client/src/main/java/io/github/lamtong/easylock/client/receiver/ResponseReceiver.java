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

package io.github.lamtong.easylock.client.receiver;

import io.github.lamtong.easylock.client.cache.ResponseCache;
import io.github.lamtong.easylock.common.core.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link ResponseReceiver} is a handler for in-bound channel, which is often used to
 * receive and handle responses from server for each channel.
 *
 * @author Lam Tong
 * @version 1.1.0
 * @since 1.0.0
 */
public final class ResponseReceiver extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(ResponseReceiver.class.getName());

    private static final ResponseCache cache = ResponseCache.getCache();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "Channel is active, address: {0}", ctx.channel().remoteAddress());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (logger.isLoggable(Level.WARNING)) {
            logger.log(Level.WARNING, "Channel is inactive, address: {0}", ctx.channel().remoteAddress());
        }
        ctx.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        cache.put(((Response) msg));
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "Connection to server has not been established.");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (logger.isLoggable(Level.WARNING)) {
            logger.log(Level.WARNING, "Exception occurs, caused by {0}", cause.getMessage());
        }
        ctx.channel().close();
    }

}
