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
package org.apache.tuscany.sca.implementation.java.injection;

import org.apache.tuscany.sca.core.factory.ObjectFactory;

/**
 * Implementation of ObjectFactory that returns a single instance, typically an immutable type.
 *
 * @version $Rev: 567619 $ $Date: 2007-08-20 10:29:57 +0100 (Mon, 20 Aug 2007) $
 */
public class SingletonObjectFactory<T> implements ObjectFactory<T> {
    private final T instance;

    public SingletonObjectFactory(T instance) {
        this.instance = instance;
    }

    public T getInstance() {
        return instance;
    }

}
