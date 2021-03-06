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

package io.github.lamtong.easylock.server.property;

/**
 * {@link ServerProperties} provides properties to launch lock server.
 *
 * @author Lam Tong
 * @version 1.0.0
 * @see Properties
 * @since 1.0.0
 */
public final class ServerProperties extends Properties {

    private static final ServerProperties properties = new ServerProperties();

    private int port = 40417;

    private int backlog = 1024;

    private ServerProperties() {
    }

    public static ServerProperties getServerProperties() {
        return properties;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBacklog() {
        return this.backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    @Override
    public String toString() {
        return "ServerProperties{" +
                "port=" + port +
                ", backlog=" + backlog +
                '}';
    }

}
