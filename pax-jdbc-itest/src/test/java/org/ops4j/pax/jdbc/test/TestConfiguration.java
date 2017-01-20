/*
 * Copyright 2012 Harald Wellmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.jdbc.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default configuration for native container regression tests, overriding the default test system
 * configuration.
 * <p>
 * We do not need the Remote Bundle Context for Native Container, and we prefer unified logging with
 * logback.
 * <p>
 * To override the standard options, you need to set the configuration property
 * {@code pax.exam.system = default}.
 *
 * @author Harald Wellmann
 */
public class TestConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(TestConfiguration.class);

    static boolean equinoxConsole = true;

    public static boolean isPostgresqlAvailable() {
        ServerConfiguration config = new ServerConfiguration("postgresql");
        config.getUrl();

        String serverName = config.getServerName();
        String portNumber = config.getPortNumber();
        int port = (portNumber == null) ? 5432 : Integer.parseInt(portNumber);

        boolean success = checkSocketConnection(serverName, port);
        if (!success) {
            LOG.warn("cannot connect to PostgreSQL at {}:{}, ignoring test", serverName, port);
        }
        return success;
    }

    public static boolean isMysqlAvailable() {
        ServerConfiguration config = new ServerConfiguration("mysql");
        config.getUrl();

        String serverName = config.getServerName();
        String portNumber = config.getPortNumber();
        int port = (portNumber == null) ? 3306 : Integer.parseInt(portNumber);

        boolean success = checkSocketConnection(serverName, port);
        if (!success) {
            LOG.warn("cannot connect to MySQL at {}:{}, ignoring test", serverName, port);
        }
        return success;
    }

    public static boolean isMariaDbAvailable() {
        ServerConfiguration config = new ServerConfiguration("mariadb");
        config.getUrl();

        String serverName = config.getServerName();
        String portNumber = config.getPortNumber();
        int port = (portNumber == null) ? 3306 : Integer.parseInt(portNumber);

        boolean success = checkSocketConnection(serverName, port);
        if (!success) {
            LOG.warn("cannot connect to MariaDB at {}:{}, ignoring test", serverName, port);
        }
        return success;
    }

    public static boolean checkSocketConnection(String serverName, int port) {
        Socket socket = new Socket();
        try {
            InetSocketAddress endpoint = new InetSocketAddress(serverName, port);
            socket.connect(endpoint, (int) TimeUnit.SECONDS.toMillis(5));
            return true;
        }
        catch (UnknownHostException exc) {
            return false;
        }
        catch (IOException exc) {
            return false;
        }
        finally {
            if (socket != null) {
                try {
                    socket.close();
                }
                catch (IOException e) {

                }
            }
        }
    }
}
