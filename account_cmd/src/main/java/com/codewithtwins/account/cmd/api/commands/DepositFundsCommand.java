package com.codewithtwins.account.cmd.api.commands;

import com.codewithtwins.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class DepositFundsCommand extends BaseCommand {
    private double amount;
}
