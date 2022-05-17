import React, {useEffect, useState} from 'react';
import styles from "../EventsPage.module.scss";
import eventUtils from "../restUtils/eventUtils";

import allIn from "./../../../../assets/allintervaly.png";
import fullSek from "./../../../../assets/fullsekvence 1.png";
import lib from "./../../../../assets/libovolny 1.png";
import axios from "axios";
import {baseUrl} from "../../../../config/const";
import authHeader from "../../../../services/auth-header";


function timestampToDatetimeInputString(timestamp) {
    const date = new Date((timestamp + _getTimeZoneOffsetInMs()));
    return date.toISOString().slice(0, 19);
}

function _getTimeZoneOffsetInMs() {
    return new Date().getTimezoneOffset() * -60 * 1000;
}

//TODO
//getters for activities
//send to rest new event
export const Modal = (props) => {

    const [name, setName] = useState('');
    const [fromTime, setFromTime] = useState('');
    const [toTime, setToTime] = useState('');
    const [startDate, setStartDate] = useState('');
    const [repeatUntil, setRepeatUntil] = useState('');
    const [day, setDay] = useState('');
    const [repetition, setRepetition] = useState('NONE');

    const [minimalReservationTime, setMinimalReservationTime] = useState(0);
    const [seatAmount, setSeatAmount] = useState(1);
    const [timeBetweenIntervals, setTimeBetweenIntervals] = useState(0);
    const [intervalDuration, setIntervalDuration] = useState(0);
    const [sourceId, setSourceId] = useState(0);

    const [eventType, setEventType] = useState(1)

    const [resources, setResources] = useState([]);
    const [data, setData] = useState([]);
    const [error, setError] = useState("");


    useEffect(async () => {
        const today = () => {
            const today = new Date();
            const year = today.getFullYear();
            let month = "";
            let date = "";
            if (today.getMonth() < 10) {
                month = "0" + Number(today.getMonth() + 1);
            } else {
                month = today.getMonth();
            }
            if (today.getDate() < 10) {
                date = "0" + Number(today.getMonth() + 1);
            } else {
                date = today.getDate();
            }
            const day = year + "-" + month + "-" + date;
            return day;
        }
        setStartDate(today)

        const fetchSources = await Promise.any([
                axios.get(
                    `${baseUrl}/systems/my/sources`,
                    {headers: authHeader()})
            ]
        )
        setResources(fetchSources.data)

    }, [])


    const handle = () => {
        if (name.trim().length !== 0) {
            if (JSON.parse(data) === "None") {
                console.log("Please choose a Source")
            } else {
                if (eventType === 1) {
                    eventUtils.newIntervalEvent(JSON.parse(data).address.id, name, fromTime, toTime, startDate).then(() => {
                        props.closeModal()
                        alert("Success 🤌")
                        window.location.reload()
                    })
                } else if (eventType === 2) {
                    eventUtils.newSeatEvent(JSON.parse(data).address.id, name, fromTime, toTime, startDate, seatAmount).then(() => {
                        props.closeModal()
                        alert("Success 🤌")
                        window.location.reload()
                    })
                }
            }
        } else {
            setError("Error")
        }
    }

    const Seat = () => {
        return (
            <div className={styles.flex}>
                <label>
                    <div>
                        Počet míst
                        <input type={"number"}
                               value={seatAmount}
                               onChange={e => setSeatAmount(e.target.value)}/>
                    </div>
                </label>
            </div>
        )
    }

    return (
        <div className={styles.modalBackground} onClick={props.closeModal}>
            <div className={styles.modalContent} onClick={e => e.stopPropagation()}>
                <div className={styles.modalBody}>
                    <form>
                        <fieldset className={styles.eventType}>
                            <legend><h3>Vyberte typ termínu</h3></legend>
                            <div>
                                <label htmlFor="custom">
                                    <img src={lib} alt={"eventType"}/>
                                    <div>
                                        <input type="radio" id="custom" name={"event"} value="custom"
                                               checked={eventType === 1}
                                               onChange={(e) => setEventType(1)}
                                        />Libovolný čas
                                    </div>
                                </label>
                            </div>

                            <div>
                                <label htmlFor="sequence">
                                    <img src={fullSek} alt={"eventType"}/>
                                    <div>
                                        <input type="radio" id="sequence" name={"event"} value="sequence"
                                               checked={eventType === 2}
                                               onChange={(e) => setEventType(2)}
                                        />
                                        Seat
                                    </div>
                                </label>
                            </div>
                        </fieldset>
                        <fieldset>
                            <legend><h3>Název eventu</h3></legend>
                            <label>
                                <div>
                                    <input type={"text"} className={'input-primary'} value={name}
                                           onChange={e => setName(e.target.value)} placeholder={"Hokej"}/>
                                </div>
                            </label>
                        </fieldset>
                        <fieldset className={styles.when}>
                            <legend><h3>Kdy?</h3></legend>
                            <div className={styles.flex}>
                                <label>
                                    <div>
                                        V období:
                                        <input type={"date"}
                                               value={startDate}
                                               onChange={e => setStartDate(e.target.value)}/>
                                    </div>
                                </label>

                                <label>
                                    <div>
                                        od?
                                        <input type={"time"}
                                               value={fromTime}
                                               onChange={e => setFromTime(e.target.value)}/>
                                    </div>
                                </label>

                                <label>
                                    <div>
                                        do?
                                        <input type={"time"}
                                               value={toTime}
                                               onChange={e => setToTime(e.target.value)}/>
                                    </div>
                                </label>
                            </div>
                            {eventType === 2 ? <Seat/> : null}
                        </fieldset>
                        <fieldset>
                            <legend><h3>Co?</h3></legend>
                            <select className={'input-primary'} onChange={(e) => setData(e.target.value)}>
                                <option value={JSON.stringify("None")}>
                                    None
                                </option>
                                {resources.map(resource => {
                                    return (
                                        <option value={JSON.stringify(resource)}>
                                            {resource.name}
                                            {resource.description}
                                        </option>
                                    )
                                })}
                            </select>
                        </fieldset>
                    </form>
                </div>
                <div className={styles.modalFooter}>
                    <button className={'button-primary-outline'} onClick={props.closeModal}>Cancel</button>
                    <button className={'button-primary'} onClick={handle}>Continue</button>
                </div>
                <div className={styles.error}>{error}</div>
            </div>
        </div>
    )
}

export default Modal