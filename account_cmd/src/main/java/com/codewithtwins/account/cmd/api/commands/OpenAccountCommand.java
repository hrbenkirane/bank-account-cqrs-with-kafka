package com.codewithtwins.account.cmd.api.commands;

import com.codewithtwins.account.common.dto.AccountType;
import com.codewithtwins.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;
    private AccountType accountType;
    private double openingBalance;

}
