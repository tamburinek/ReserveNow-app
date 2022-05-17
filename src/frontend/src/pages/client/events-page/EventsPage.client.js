import React, {useEffect, useMemo, useState} from "react";
import FullCalendar from '@fullcalendar/react' // must go before plugins
import timeGridPlugin from '@fullcalendar/timegrid' // a plugin!
import NewEventModal from './modalWindowNewEvent/Modal';
import EventModal from './modalWindowEvent/EventModal';

import styles from "./EventsPage.module.scss";
import eventUtils from "./restUtils/eventUtils"
import axios from "axios";
import {baseUrl} from "../../../config/const";
import authHeader from "../../../services/auth-header";

let testEvents = [];

//TODO
//create data and save to db

export const EventsPageClient = () => {

    const [eventShow, setEventShow] = useState(false);
    const [newEventShow, setNewEventShow] = useState(false);
    const [data, setData] = useState();

    useEffect(() => {

        eventUtils.getAllEvents()
            .then(response => response.data)
            .then(data => {
                testEvents = data;
                testEvents.map(s => {
                    if (s.hasOwnProperty("name")) {
                        s.title = s.name;
                        delete s.name;
                    }
                    if (s.hasOwnProperty("startDate") && s.hasOwnProperty("fromTime")) {
                        s.start = s.startDate + " " + s.fromTime;
                        delete s.fromTime
                    }
                    if (s.hasOwnProperty("startDate") && s.hasOwnProperty("toTime")) {
                        s.end = s.startDate + " " + s.toTime;
                        delete s.toTime;
                        delete s.startDate;
                    }
                })
                setData(testEvents);
            });
    }, [])

    return (
        <div>
            {newEventShow && <NewEventModal closeModal={() => setNewEventShow(false)}/>}
            {eventShow && <EventModal closeModal={() => setEventShow(false)}/>}
            <div className={styles.calendarBody}>
                <div className={styles.bodySide}>
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
                        customButtons={{
                            myCustomButton: {
                                text: 'Add event',
                                click: function () {
                                    setNewEventShow(true);
                                },
                            },
                        }}
                        headerToolbar={{
                            right: 'myCustomButton today prev,next'
                        }}
                        eventClick={function (info) {
                            localStorage.setItem("eventTitle", info.event.title);
                            localStorage.setItem("eventStart", info.event.start.toString());
                            localStorage.setItem("eventEnd", info.event.end.toString());
                            setTimeout(() => {
                                setEventShow(true)
                            }, 200);
                            // tpc skarede jak cyp
                        }}
                    />
                </div>
            </div>
        </div>
    );
}