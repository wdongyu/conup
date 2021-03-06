/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package com.tuscanyscatours.emailgateway.impl;

import java.util.logging.Logger;

import org.oasisopen.sca.annotation.Service;

import cn.edu.nju.moon.conup.spi.datamodel.ConupTransaction;

import com.tuscanyscatours.emailgateway.EmailGateway;

@Service(EmailGateway.class)
public class EmailGatewayImpl implements EmailGateway {
	
	private String COMP_VERSION= "Ver_0";
	
	private Logger LOGGER = Logger.getLogger(EmailGatewayImpl.class.getName());
	@ConupTransaction
    public boolean sendEmail(String sender, String recipient, String subject, String body) {
        LOGGER.fine("From: " + sender);
        LOGGER.fine("To: " + recipient);
        LOGGER.fine("Subject: " + subject);
        LOGGER.fine(body);
        return true;
    }

}
