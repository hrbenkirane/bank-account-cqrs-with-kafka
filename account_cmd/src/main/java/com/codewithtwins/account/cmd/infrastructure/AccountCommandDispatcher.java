package com.codewithtwins.account.cmd.infrastructure;

import com.codewithtwins.cqrs.core.commands.BaseCommand;
import com.codewithtwins.cqrs.core.commands.CommandHandlerMethod;
import com.codewithtwins.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {
    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();
    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        List<CommandHandlerMethod> handlers = routes.computeIfAbsent(type, c -> new LinkedList<>());
        handlers.add(handler);

    }

    @Override
    public void send(BaseCommand command) {
        List<CommandHandlerMethod> handlers = routes.get(command.getClass());
        if(handlers == null || handlers.isEmpty()) {
            throw new RuntimeException("No command handler was registred!!");
        }
        if(handlers.size() > 1) {
            throw new RuntimeException("Cannot send more than one handlers!");
        }
        handlers.get(0).handle(command);

    }
}
