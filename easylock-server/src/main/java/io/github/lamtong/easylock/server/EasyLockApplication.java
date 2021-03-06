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

package io.github.lamtong.easylock.server;

import io.github.lamtong.easylock.server.handler.BannerHandler;
import io.github.lamtong.easylock.server.handler.PropertiesHandler;
import io.github.lamtong.easylock.server.launcher.ServerLauncher;
import io.github.lamtong.easylock.server.property.ServerProperties;

/**
 * <code>Application</code> of <code>EasyLock</code>.
 *
 * @author Lam Tong
 * @version 1.0.0
 * @since 1.0.0
 */
public class EasyLockApplication {

    public static void main(String[] args) {
        new Thread(() -> {
            new BannerHandler().handleBanner();
            new PropertiesHandler().handleProperties(args);
            new ServerLauncher().launch(ServerProperties.getServerProperties());
        }).start();
    }

}
