package com.codewithtwins.account.common.events;

import com.codewithtwins.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FundWithdrawnEvent extends BaseEvent {
    private double amount;
}
