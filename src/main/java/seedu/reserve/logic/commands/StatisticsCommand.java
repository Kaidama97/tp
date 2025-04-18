package seedu.reserve.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.reserve.logic.commands.exceptions.CommandException;
import seedu.reserve.model.Model;

/**
 * Display statistics of reservations in ReserveMate.
 */
public class StatisticsCommand extends Command {

    public static final String COMMAND_WORD = "stats";

    public static final String SHOWING_STATISTICS_MESSAGE = "Displayed statistics of reservations.";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays statistics of all reservations in the reservation book.\n\n"
            + "Example: " + COMMAND_WORD;

    public static final String NO_RESERVATIONS_MESSAGE = "There are no reservations to generate statistics from. "
            + "Use the 'add' command to create a reservation\n";


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.getFilteredReservationList().isEmpty()) {
            throw new CommandException(NO_RESERVATIONS_MESSAGE);
        }

        return new CommandResult(SHOWING_STATISTICS_MESSAGE, false, true, false);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof StatisticsCommand;
    }
}
