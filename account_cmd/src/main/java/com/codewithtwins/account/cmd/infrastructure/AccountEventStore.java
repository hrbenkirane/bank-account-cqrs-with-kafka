package com.codewithtwins.account.cmd.infrastructure;

import com.codewithtwins.account.cmd.domain.AccountAggregate;
import com.codewithtwins.account.cmd.domain.EventStoreRepository;
import com.codewithtwins.cqrs.core.events.BaseEvent;
import com.codewithtwins.cqrs.core.events.EventModel;
import com.codewithtwins.cqrs.core.exceptions.AggregateNotFoundException;
import com.codewithtwins.cqrs.core.exceptions.ConcurrencyException;
import com.codewithtwins.cqrs.core.infrastructure.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountEventStore implements EventStore {

    @Autowired
    private EventStoreRepository eventStoreRepository;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        List<EventModel> eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if(expectedVersion != -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }
        int version = expectedVersion;
        for(BaseEvent event : events) {
            version++;
            event.setVersion(version);
            EventModel eventModel = EventModel.builder()
                    .timeStamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();
            EventModel persistedEvent = eventStoreRepository.save(eventModel);
            if(persistedEvent != null) {
                // TODO: produce event to kafka
            }
        }

    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        List<EventModel> eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if(eventStream == null || eventStream.isEmpty()) {
            throw new AggregateNotFoundException("Incorrect account ID provided!");
        }

        return eventStream.stream().map(e -> e.getEventData()).collect(Collectors.toList());
    }
}
