package cz.cvut.fel.rsp.ReservationSystem.model.enums;

public enum UserType {
    ROLE_REGULAR_USER("regular_user"),
    ROLE_ADMIN("admin"),
    ROLE_SYSTEM_OWNER("system_owner"),
    ROLE_SYSTEM_EMPLOYEE("system_employee");
    private final String name;

    UserType(String name) {
        this.name = name;
    }
}
