package cz.cvut.fel.rsp.ReservationSystem.service.interfaces;

import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Address;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;

public interface SourceService{
    void createSource(Source source, ReservationSystem reservationSystem);

    void createSource(Source source, ReservationSystem reservationSystem, String name);

    void removeAddress(Source source);

    void addAddress(Source source, Address address);

    Source find(Integer id);
}
