/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowant.users.data;

import org.springframework.data.cassandra.core.mapping.event.AbstractCassandraEventListener;
import org.springframework.data.cassandra.core.mapping.event.AfterConvertEvent;
import org.springframework.data.cassandra.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.cassandra.core.mapping.event.AfterLoadEvent;
import org.springframework.data.cassandra.core.mapping.event.AfterSaveEvent;
import org.springframework.data.cassandra.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.cassandra.core.mapping.event.BeforeSaveEvent;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoggingEventListener extends AbstractCassandraEventListener<Object> {

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.cassandra.core.mapping.event.
     * AbstractCassandraEventListener#onBeforeSave(org.springframework.data.
     * cassandra.core.mapping.event.BeforeSaveEvent)
     */
    @Override
    public void onBeforeSave(BeforeSaveEvent<Object> event) {
        log.debug("onBeforeSave: {}, {}", event::getSource, event::getStatement);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.cassandra.core.mapping.event.
     * AbstractCassandraEventListener#onAfterSave(org.springframework.data.cassandra
     * .core.mapping.event.AfterSaveEvent)
     */
    @Override
    public void onAfterSave(AfterSaveEvent<Object> event) {
        log.info("onAfterSave: {}", event.getSource());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.cassandra.core.mapping.event.
     * AbstractCassandraEventListener#onBeforeDelete(org.springframework.data.
     * cassandra.core.mapping.event.BeforeDeleteEvent)
     */
    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Object> event) {
        log.info("onBeforeDelete: {}", event.getSource());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.cassandra.core.mapping.event.
     * AbstractCassandraEventListener#onAfterDelete(org.springframework.data.
     * cassandra.core.mapping.event.AfterDeleteEvent)
     */
    @Override
    public void onAfterDelete(AfterDeleteEvent<Object> event) {
        log.info("onAfterDelete: {}", event.getSource());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.cassandra.core.mapping.event.
     * AbstractCassandraEventListener#onAfterLoad(org.springframework.data.cassandra
     * .core.mapping.event.AfterLoadEvent)
     */
    @Override
    public void onAfterLoad(AfterLoadEvent<Object> event) {
        log.info("onAfterLoad: {}", event.getSource());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.data.cassandra.core.mapping.event.
     * AbstractCassandraEventListener#onAfterConvert(org.springframework.data.
     * cassandra.core.mapping.event.AfterConvertEvent)
     */
    @Override
    public void onAfterConvert(AfterConvertEvent<Object> event) {
        log.info("onAfterConvert: {}", event.getSource());
    }
}
