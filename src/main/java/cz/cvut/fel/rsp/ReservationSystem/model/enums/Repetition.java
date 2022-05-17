package cz.cvut.fel.rsp.ReservationSystem.model.enums;

import java.time.LocalDate;

public enum Repetition {
    NONE{
        @Override
        public LocalDate add(LocalDate date) {
            return date;
        }
    },
    DAILY{
        @Override
        public LocalDate add(LocalDate date) {
            return date.plusDays(1);
        }
    },
    WEEKLY{
        @Override
        public LocalDate add(LocalDate date) {
            return date.plusWeeks(1);
        }
    },
    MONTHLY{
        @Override
        public LocalDate add(LocalDate date) {
            return date.plusMonths(1);
        }
    },
    YEARLY{
        @Override
        public LocalDate add(LocalDate date) {
            return date.plusYears(1);
        }
    };

    public abstract LocalDate add(LocalDate date);
}
