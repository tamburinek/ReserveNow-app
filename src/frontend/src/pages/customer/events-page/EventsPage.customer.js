import styles from './EventsPage.module.scss'
import React, {useEffect, useState} from "react";
import FullCalendar from '@fullcalendar/react' // must go before plugins
import timeGridPlugin from '@fullcalendar/timegrid' // a plugin!
import NewEvent from './modalWindowNewReservation/newReserveEvent';
import eventUtils from "../events-page/restUtils/eventUtils";

let testEvents = [];


export const EventsPageCustomer = (props) => {
    useEffect(() => {
        eventUtils.getAllSlots(props.system)
            .then(response => response.data)
            .then(data => {
                testEvents = data;
                console.log(testEvents)
                testEvents.map(s => {
                    if (s.hasOwnProperty("price")) {
                        s.title = s.price;
                        delete s.price;
                    }
                    if (s.hasOwnProperty("date") && s.hasOwnProperty("startTime")) {
                        s.start = s.date + " " + s.startTime;
                        delete s.startTime
                    }
                    if (s.hasOwnProperty("date") && s.hasOwnProperty("endTime")) {
                        s.end = s.date + " " + s.endTime;
                        delete s.endTime;
                        delete s.date;
                    }
                })
                setData(testEvents);
                console.log(testEvents)
            });
    }, [])

    const [show, setShow] = useState(false);
    const [data, setData] = useState();

    const handleClose = () => {
        setShow(false);
    };

    return (
        <div>
            {show && <NewEvent closeModal={handleClose}/>}
            <section className={styles.eventBody}>
                <button type={"button"} className={styles.backButton} onClick={props.onClose}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24">
                        <path fill="#fff"
                              d="m4 12-.707-.707-.707.707.707.707L4 12Zm15 1a1 1 0 1 0 0-2v2ZM9.293 5.293l-6 6 1.414 1.414 6-6-1.414-1.414Zm-6 7.414 6 6 1.414-1.414-6-6-1.414 1.414ZM4 13h15v-2H4v2Z"/>
                    </svg>
                </button>
                <div className={styles.eventCalendar}>
                    <FullCalendar
                        plugins={[timeGridPlugin]}
                        initialView="timeGridWeek"
                        locale={'cs'}
                        selectable={true}
                        selectMirror={true}
                        weekends={true}
                        events={data}
                        nowIndicator={true}
                        height={600}
                        headerToolbar={{
                            right: 'today prev,next'
                        }}
                        eventClick={function (info) {
                            localStorage.setItem("eventId", info.event.id);
                            localStorage.setItem("eventFrom", info.event.start.toISOString());
                            localStorage.setItem("eventTo", info.event.end.toISOString());
                            localStorage.setItem("eventPrice", info.event.title);
                            setTimeout(() => setShow(true), 200);
                        }}
                    />
                </div>
            </section>
        </div>
    )
}