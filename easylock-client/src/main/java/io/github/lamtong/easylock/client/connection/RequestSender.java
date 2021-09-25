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

import io.github.lamtong.easylock.common.core.Request;
import io.github.lamtong.easylock.common.core.Response;
import io.netty.channel.Channel;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.util.concurrent.FutureListener;

import java.util.logging.Logger;

/**
 * {@link RequestSender} sends requests, namely {@code Lock Request} and {@code Unlock Request},
 * to server and try to acquire corresponding response in {@link ResponseCache}.
 *
 * @author Lam Tong
 * @version 1.3.1
 * @see Request
 * @see Response
 * @since 1.0.0
 */
public final class RequestSender {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(RequestSender.class.getName());

    private static final RequestSender sender = new RequestSender();

    private RequestSender() {
    }

    public static RequestSender getSender() {
        return sender;
    }

    /**
     * Sends a {@link Request} instance to server and retrieves corresponding responding response.
     *
     * @param request {@link Request} instance resolved at server.
     * @return corresponding response.
     */
    public Response.ResponseProto send(Request.RequestProto request) {
        final String key = request.getKey();
        final boolean lockRequest = request.getLockRequest();
        final long identity = request.getIdentity();
        final FixedChannelPool pool = ChannelPoolProvider.getPool();
        final ResponseCache cache = ResponseCache.getCache();
        pool.acquire().addListener((FutureListener<Channel>) f -> {
            if (f.isSuccess()) {
                // Acquires a channel successfully, then send request and release the channel.
                Channel channel = f.getNow();
                channel.writeAndFlush(request);
                pool.release(channel);
            } else {
                // Fails to acquire a channel, maybe the client fails to connect to server, or network breakdown.
                // Thus requests cancel and responses are created at client to answer the requests.
                if (lockRequest) {
                    cache.put(Response.ResponseProto.newBuilder()
                            .setKey(key)
                            .setIdentity(identity)
                            .setSuccess(false)
                            .setCause("Connection to server fails, lock request cancelled")
                            .setLockResponse(true)
                            .build());
                } else {
                    cache.put(Response.ResponseProto.newBuilder()
                            .setKey(key)
                            .setIdentity(identity)
                            .setSuccess(false)
                            .setCause("Connection to server fails, unlock request cancelled")
                            .setLockResponse(false)
                            .build());
                }
            }
        });
        //
        // After sending the request successfully, current thread will try to retrieve corresponding
        // response from {@link ResponseCache} in pooling if the request is resolved at server and
        // corresponding response arrives and is stored in {@link ResponseCache}.
        //
        //     1.Current thread will check that if there exist a response whose key is the same as
        //       that of the request sends before, and if it does, then go to the next step; otherwise,
        //       continue current step.
        //     2.Current thread will check the identity of the received response with that of the
        //       request sends before. If there exists a response whose identity is the same as
        //       the request sends before, then current response in {@link ResponseCache} can be
        //       acquired with specified key and returned.
        //
        Response.ResponseProto response;
        for (; ; ) {
            Response.ResponseProto res;
            //noinspection StatementWithEmptyBody
            while ((res = cache.peek(key)) == null ||
                    (res.getLockResponse() && !lockRequest) ||
                    (!res.getLockResponse() && lockRequest)) {
                // Waiting until corresponding response arrives.
            }
            if (res.getIdentity() == identity) {
                response = cache.take(key);
                break;
            }
        }
        return response;
    }

}
