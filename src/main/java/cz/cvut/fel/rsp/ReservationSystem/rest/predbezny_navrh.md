Base url: api/v1/

API sources:
User
- /me
- /{username}"
- /{username}/reservations?from="xx"&to="xx"
- + SECURITY

Reservation system
- /systems
- /systems/{system_id}
- /systems/{system_id}/sources
- /systems/{system_id}/feedback
- /systems/{system_id}/reservations?time="today"

Sources
- /sources/{source_id}
- /sources/{source_id}/events?from="xx"&to="xx"
- /sources/{source_id}/categories

Category
- /categories/{category_id}
- /categories/{category_id}/events?from="xx"&to="xx"

Events
- /events/{event_id}
- /events/{event_id}/timeslots?from="xx"&to="xx"
- /events/{event_id}/reservations?from="xx"&to="xx"&canceled="true/false"

Reservations
- /reservations/{reservation_id}
- /reservations/{reservation_id}/payment

Reservation slots
- /slots/{slot_id}


