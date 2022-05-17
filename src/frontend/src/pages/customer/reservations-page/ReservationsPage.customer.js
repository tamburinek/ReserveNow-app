import styles from './Reservations.module.scss'
import {useEffect, useState} from "react";
import {baseUrl} from "../../../config/const";
import axios from "axios";
import authHeader from "../../../services/auth-header";
import {Modal} from "./Modal";


export const ReservationsPageCustomer = () => {
    const [event, setEvents] = useState([]);
    const [slots, setSlots] = useState([]);

    const [open, setOpen] = useState(false);
    const [system, setSystem] = useState([]);
    const [data, setData] = useState('');

    const fetchSlot = async (e) => {
        return new Promise((resolve, reject) => {
            axios.get(`${baseUrl}/slots/${e}`, {headers: authHeader()}).then(response => {
                resolve(response.data)
            }).catch(reject);
        })
    }

    const fetchSlots = async (data) => {

        let response = []
        await Promise.all(data.map(async (e) => {
            try {
                let insertResponse = await fetchSlot(e.reservationSlotId)
                response.push(insertResponse)
            } catch (error) {
                console.log('error' + error);
            }
        }))
        return response
    }

    const fetchEvent = async (id) => {
        return new Promise((resolve, reject) => {
            axios.get(`${baseUrl}/events/${id}`, {headers: authHeader()}).then(response => {
                resolve(response.data)
            }).catch(reject);
        })
    }

    const fetchEvents = async (data) => {

        let response = []
        await Promise.all(data.map(async (e) => {
            try {
                let insertResponse = await fetchEvent(e.eventId)
                response.push(insertResponse)
            } catch (error) {
                console.log('error' + error);
            }
        }))
        return response
    }

    const fetchId = async (id) => {
        return new Promise((resolve, reject) => {
            axios.get(`${baseUrl}/category/${id}/system`, {headers: authHeader()}).then(response => {
                resolve(response.data)
            }).catch(reject);
        })
    }

    const fetchIds = async (data) => {
        let response = []
        await Promise.all(data.map(async (e) => {
            try {
                let insertResponse = await fetchId(e.categoryId)
                response.push(insertResponse)
            } catch (error) {
                console.log('error' + error);
            }
        }))
        return response
    }

    useEffect(async () => {
        const reservations = await Promise.all([axios.get(`${baseUrl}/my/reservations`, {headers: authHeader()})])
        const slots = await Promise.all([fetchSlots(reservations[0].data)])
        setSlots(slots[0])
        const events = await Promise.all([fetchEvents(slots[0])])
        setEvents(events[0]);
        const ids = await Promise.all([fetchIds(events[0])])
        setSystem(ids[0])
    }, [])

    return (<div className={styles.container}>
            {open ? <Modal onClose={() => setOpen(false)} system={data}/> : null}
            <h2>My reservations</h2>
            <table className={styles.table}>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>From Time</th>
                    <th>To Time</th>
                    <th>Date</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>

                {event.map((r) => {
                    return (<tr>
                            <td>
                                {r.name}
                            </td>
                            <td>
                                {r.fromTime}
                            </td>
                            <td>
                                {r.toTime}
                            </td>
                            <td>
                                {r.startDate}
                            </td>
                            <td>
                                {new Date(r.startDate).getDate() + 1 < new Date().getDate() ?
                                    <button className={'button-primary'} onClick={() => {
                                        setOpen(true)
                                        setData(system[0])
                                    }}>Add feedback</button> : null}
                            </td>
                        </tr>)
                })}
                </tbody>
            </table>
        </div>)
}