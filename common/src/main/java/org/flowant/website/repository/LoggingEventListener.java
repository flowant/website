package org.flowant.website.repository;

import org.springframework.data.cassandra.core.mapping.event.AbstractCassandraEventListener;
import org.springframework.data.cassandra.core.mapping.event.AfterConvertEvent;
import org.springframework.data.cassandra.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.cassandra.core.mapping.event.AfterLoadEvent;
import org.springframework.data.cassandra.core.mapping.event.AfterSaveEvent;
import org.springframework.data.cassandra.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.cassandra.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class LoggingEventListener extends AbstractCassandraEventListener<Object> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<Object> event) {
        log.debug("onBeforeSave: {}, {}", event::getSource, event::getStatement);
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Object> event) {
        log.debug("onAfterSave: {}", event.getSource());
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Object> event) {
        log.debug("onBeforeDelete: {}", event.getSource());
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Object> event) {
        log.debug("onAfterDelete: {}", event.getSource());
    }

    @Override
    public void onAfterLoad(AfterLoadEvent<Object> event) {
        log.debug("onAfterLoad: {}", event.getSource());
    }

    @Override
    public void onAfterConvert(AfterConvertEvent<Object> event) {
        log.debug("onAfterConvert: {}", event.getSource());
    }
}
