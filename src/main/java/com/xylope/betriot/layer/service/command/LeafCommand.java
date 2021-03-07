package com.xylope.betriot.layer.service.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LeafCommand extends AbstractCommand {
    private final AbstractCommand command;

    @Override
    public void execute(String... args) {
        command.execute(args);
    }
}
