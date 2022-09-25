package com.codewithtwins.account.cmd.infrastructure;

import com.codewithtwins.account.cmd.domain.AccountAggregate;
import com.codewithtwins.cqrs.core.domain.AggregateRoot;
import com.codewithtwins.cqrs.core.events.BaseEvent;
import com.codewithtwins.cqrs.core.handlers.EventSourcingHandler;
import com.codewithtwins.cqrs.core.infrastructure.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AccountSourcingHandler implements EventSourcingHandler<AccountAggregate> {
    @Autowired
    private EventStore eventStore;

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncomittedChanges(), aggregate.getVersion());
        aggregate.getUncomittedChanges();
    }

    @Override
    public AccountAggregate getById(String id) {
        AccountAggregate aggregate = new AccountAggregate();
        List<BaseEvent> events = eventStore.getEvents(id);
        if(events != null && !events.isEmpty()) {
            aggregate.replayEvent(events);
            Optional<Integer> latestVersion = events.stream().map(x -> x.getVersion()).max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }
        return aggregate;
    }
}
