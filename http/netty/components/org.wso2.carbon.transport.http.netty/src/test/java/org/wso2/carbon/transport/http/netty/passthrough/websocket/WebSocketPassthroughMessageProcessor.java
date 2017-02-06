/*
 *   Copyright (c) ${date}, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.carbon.transport.http.netty.passthrough.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.messaging.CarbonMessageProcessor;
import org.wso2.carbon.messaging.MessageProcessorException;
import org.wso2.carbon.messaging.TextCarbonMessage;
import org.wso2.carbon.messaging.TransportSender;
import org.wso2.carbon.transport.http.netty.common.Constants;
import org.wso2.carbon.transport.http.netty.util.TestUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A Message Processor class to be used for test pass through scenarios
 */
public class WebSocketPassthroughMessageProcessor implements CarbonMessageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketPassthroughMessageProcessor.class);
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private TransportSender transportSender;

    @Override
    public boolean receive(CarbonMessage carbonMessage, CarbonCallback carbonCallback) throws Exception {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (carbonMessage instanceof TextCarbonMessage) {
                        logger.info("Text Frame received for URI : " +
                            carbonMessage.getProperty(Constants.TO));
                        Assert.assertTrue(true);

                    } else {
                        carbonMessage.setProperty(Constants.HOST, TestUtil.TEST_HOST);
                        carbonMessage.setProperty(Constants.PORT, TestUtil.TEST_SERVER_PORT);
                        transportSender.send(carbonMessage, carbonCallback);
                    }
                } catch (MessageProcessorException e) {
                    logger.error("MessageProcessor is not supported ", e);
                }
            }
        });

        return true;
    }

    @Override
    public void setTransportSender(TransportSender transportSender) {
        this.transportSender = transportSender;
    }

    @Override
    public String getId() {
        return "passthrough";
    }
}
