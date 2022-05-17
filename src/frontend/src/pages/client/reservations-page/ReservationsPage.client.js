import styles from './ReservationPage.module.scss'
import {Calendar, DateObject} from "react-multi-date-picker"
import React, {useEffect, useState} from "react";
import axios from "axios";
import {baseUrl} from "../../../config/const";
import ModalReservationDetails from "./modalWindowReservationDetails/ModalReservationDetails"
import authHeader from "../../../services/auth-header";
import ModalCreateReservation from "./modalWindowCreateReservation/ModalCreateReservation";

const TopButtons = () => {

    return (
        <div className={styles.topbuttons}>
            <p className={styles.heading}>DAILY RESERVATION OVERVIEW</p>
            <button className={'button-primary '.concat(styles.topbutton)}>Filter</button>
            <button className={'button-primary '.concat(styles.topbutton)}>Statistics</button>
            <button className={'button-primary '.concat(styles.topbutton)}>Export reservations</button>
        </div>
    )
}

const DatePicker = ({getDate}) => {
    const [date, setDate] = useState(new DateObject())

    useEffect(() => {
        getDate(date)
    }, [date])

    return (
        <>
            <Calendar
                numberOfMonths={4}
                disableMonthPicker
                disableYearPicker
                value={date}
                onChange={setDate}
            />
        </>
    )
}

const Table = () => {
    const reload = () => window.location.reload();
    const [date, setDate] = useState(new DateObject())

    const [data, setData] = useState(undefined);
    const [detailsShown, setDetailsShown] = useState([]);
    const [openModal, setOpenModal] = useState(false);
    const [openModalCreateReservation, setOpenModalCreateReservation] = useState(false);
    const [isActiveLogo, setIsLogoActive] = useState(false);
    const [client, setClient] = useState(null);
    const [loading, setLoading] = useState(false);


    useEffect(() => {
        sendGetEvents(date.format("YYYY-MM-DD"))
    }, [date])

    const getDate = (date) => {
        setDate(date)
    }

    const sendGetEvents = async (date) => {
        try {
            const resp0 = await axios.get(
                `${baseUrl}/systems/my`,
                {headers: authHeader()})
            let system = resp0.data.id

            const resp = await axios.get(
                `${baseUrl}/systems/${system}/reservations/`,
                {
                    params: {fromDate: date, toDate: date},
                    headers: authHeader()
                })

            for (let i = 0; i < resp.data.length; i++) {
                await concatReservationDetails(resp.data[i])
            }
            setData(resp.data)
            //console.log(resp.data);
        } catch (err) {
            // Handle Error Here
            console.error(err);
        }
    };

    const concatReservationDetails = async (reservation) => {
        try {
            //----------------------------------------
            const respUser = await axios.get(
                `${baseUrl}/users/${reservation.username}`,
                {headers: authHeader()},
            )
            reservation["userEmail"] = respUser.data.email;
            reservation["userFirstName"] = respUser.data.firstName;
            reservation["userLastName"] = respUser.data.lastName;

            const respSlot = await axios.get(
                `${baseUrl}/slots/${reservation.reservationSlotId}`,
                {headers: authHeader()}
            )
            reservation["date"] = respSlot.data.date;
            reservation["pricing"] = respSlot.data.price;
            if (respSlot.data.hasOwnProperty('seatIdentifier')) {
                reservation["seatIdentifier"] = respSlot.data.seatIdentifier
            }

            const respEvent = await axios.get(
                `${baseUrl}/events/${respSlot.data.eventId}`,
                {headers: authHeader()}
            )
            reservation["fromTime"] = respEvent.data.fromTime;
            reservation["toTime"] = respEvent.data.toTime;

            const respCategory = await axios.get(
                `${baseUrl}/categories/${respEvent.data.categoryId}`,
                {headers: authHeader()}
            )

            const sources = []
            for (const sourceId of respCategory.data.sourcesIds) {
                const respSource = await axios.get(
                    `${baseUrl}/sources/${sourceId}`,
                    {headers: authHeader()}
                )
                sources.push(respSource.data.name)
            }
            reservation["sources"] = sources;

            reservation["capacity"] = 1;
            if (reservation.cancelled === false) {
                reservation["state"] = "Active"
            }
            if (reservation.cancelled === true) {
                reservation["state"] = "Cancelled"
            }
            return reservation
        } catch (err) {
            // Handle Error Here
            console.error(err);
        }
    };


    const toggleShown = id => {
        const shownState = detailsShown.slice();
        const index = shownState.indexOf(id)
        if (index >= 0) {
            shownState.splice(index, 1);
            setDetailsShown(shownState)
        } else {
            shownState.push(id);
            setDetailsShown(shownState);
        }
    }

    const animateLogo = () => {
        if (!isActiveLogo) {
            setIsLogoActive(true);
            setTimeout(() => {
                setIsLogoActive(false)
            }, 3000);
        }
    }

    useEffect(() => {
        setIsLogoActive(true);
        setTimeout(() => {
            setIsLogoActive(false)
        }, 2000);
    }, [])

    const afterModalClose = () => {
        window.location.reload();
    }

    if (data === undefined || loading) {
        return (
            <div className={styles.loading}>
                <div className={styles.logoContainer} onMouseOver={animateLogo}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="25vh" zoomAndPan="magnify"
                         viewBox="0 0 375 374.999991" height="25vh" preserveAspectRatio="xMidYMid meet" version="1.0">
                        <defs>
                            <g>
                                <g id="id1"/>
                            </g>
                        </defs>
                        <g fill="rgb(100%, 100%, 100%)" fillOpacity="1">
                            <g transform="translate(86.53581, 180.43281)">
                                <g>
                                    <path
                                        d="M 7.96875 -107.515625 L 37.5625 -107.515625 C 49.332031 -107.515625 58.222656 -105.429688 64.234375 -101.265625 C 70.253906 -97.109375 73.265625 -89.722656 73.265625 -79.109375 C 73.265625 -71.941406 72.332031 -66.234375 70.46875 -61.984375 C 68.613281 -57.734375 65.164062 -54.414062 60.125 -52.03125 L 74.859375 0 L 50.3125 0 L 38.5 -48.1875 L 31.71875 -48.1875 L 31.71875 0 L 7.96875 0 Z M 38.359375 -61.859375 C 43.140625 -61.859375 46.476562 -63.117188 48.375 -65.640625 C 50.28125 -68.160156 51.234375 -71.941406 51.234375 -76.984375 C 51.234375 -81.847656 50.347656 -85.5 48.578125 -87.9375 C 46.804688 -90.375 43.753906 -91.59375 39.421875 -91.59375 L 31.71875 -91.59375 L 31.71875 -61.859375 Z M 38.359375 -61.859375 "/>
                                </g>
                            </g>
                        </g>
                        <g fill="rgb(100%, 100%, 100%)" fillOpacity="1">
                            <g transform="translate(166.174045, 180.43281)">
                                <g>
                                    <path
                                        d="M 7.96875 -107.515625 L 56.28125 -107.515625 L 56.28125 -91.328125 L 31.71875 -91.328125 L 31.71875 -64.640625 L 50.4375 -64.640625 L 50.4375 -48.1875 L 31.71875 -48.1875 L 31.71875 -16.0625 L 56.546875 -16.0625 L 56.546875 0 L 7.96875 0 Z M 7.96875 -107.515625 "/>
                                </g>
                            </g>
                        </g>
                        <g fill="rgb(100%, 100%, 100%)" fillOpacity="1">
                            <g transform="translate(225.504549, 180.43281)">
                                <g>
                                    <path
                                        d="M 36.5 1.59375 C 26.238281 1.59375 18.609375 -1.125 13.609375 -6.5625 C 8.609375 -12.007812 5.929688 -20.660156 5.578125 -32.515625 L 25.890625 -35.578125 C 25.972656 -28.765625 26.785156 -23.875 28.328125 -20.90625 C 29.878906 -17.9375 32.207031 -16.453125 35.3125 -16.453125 C 39.289062 -16.453125 41.28125 -19.066406 41.28125 -24.296875 C 41.28125 -28.453125 40.328125 -32.035156 38.421875 -35.046875 C 36.523438 -38.054688 33.539062 -41.285156 29.46875 -44.734375 L 20.171875 -52.828125 C 15.566406 -56.722656 12.003906 -60.859375 9.484375 -65.234375 C 6.960938 -69.617188 5.703125 -74.816406 5.703125 -80.828125 C 5.703125 -89.859375 8.378906 -96.804688 13.734375 -101.671875 C 19.085938 -106.535156 26.457031 -108.96875 35.84375 -108.96875 C 46.019531 -108.96875 52.941406 -106.003906 56.609375 -100.078125 C 60.285156 -94.148438 62.253906 -86.941406 62.515625 -78.453125 L 42.078125 -75.921875 C 41.898438 -81.492188 41.390625 -85.5 40.546875 -87.9375 C 39.710938 -90.375 37.878906 -91.59375 35.046875 -91.59375 C 33.003906 -91.59375 31.429688 -90.726562 30.328125 -89 C 29.222656 -87.269531 28.671875 -85.34375 28.671875 -83.21875 C 28.671875 -79.59375 29.488281 -76.539062 31.125 -74.0625 C 32.757812 -71.582031 35.394531 -68.753906 39.03125 -65.578125 L 47.921875 -57.734375 C 53.222656 -53.222656 57.332031 -48.445312 60.25 -43.40625 C 63.175781 -38.363281 64.640625 -32.390625 64.640625 -25.484375 C 64.640625 -20.441406 63.46875 -15.863281 61.125 -11.75 C 58.78125 -7.632812 55.484375 -4.378906 51.234375 -1.984375 C 46.984375 0.398438 42.070312 1.59375 36.5 1.59375 Z M 36.5 1.59375 "/>
                                </g>
                            </g>
                        </g>
                        <g fill="rgb(100%, 100%, 100%)" fillOpacity="1">
                            <g transform="translate(55.672116, 312.942406)">
                                <g>
                                    <path
                                        d="M 7.96875 -107.515625 L 24.6875 -107.515625 L 46.71875 -55.75 L 46.71875 -107.515625 L 66.359375 -107.515625 L 66.359375 0 L 50.3125 0 L 28.265625 -55.75 L 28.265625 0 L 7.96875 0 Z M 7.96875 -107.515625 "/>
                                </g>
                            </g>
                        </g>
                        <g fill="rgb(100%, 100%, 100%)" fillOpacity="1">
                            <g transform="translate(130.125945, 312.942406)">
                                <g/>
                            </g>
                        </g>
                        <g fill="rgb(100%, 100%, 100%)" fillOpacity="1">
                            <g transform="translate(164.104931, 312.942406)">
                                <g/>
                            </g>
                        </g>
                        <g fill="rgb(100%, 100%, 100%)" fillOpacity="1">
                            <g transform="translate(198.083916, 312.942406)">
                                <g/>
                            </g>
                        </g>
                        <g fill="rgb(100%, 100%, 100%)" fillOpacity="1">
                            <g transform="translate(232.04791, 312.942406)">
                                <g>
                                    <path
                                        d="M 3.578125 -107.515625 L 23.765625 -107.515625 L 30.125 -44.859375 L 38.234375 -107.375 L 54.015625 -107.375 L 62.515625 -45.265625 L 69.015625 -107.515625 L 88.9375 -107.515625 L 76.0625 0 L 55.34375 0 L 46.1875 -64.90625 L 37.5625 0 L 16.59375 0 Z M 3.578125 -107.515625 "/>
                                </g>
                            </g>
                        </g>
                        <path fill="rgb(54.899597%, 92.158508%, 76.469421%)"
                              d="M 150.523438 249.144531 C 148.933594 249.144531 147.34375 248.816406 145.753906 248.152344 C 139.523438 245.496094 136.609375 238.328125 139.257812 232.089844 C 146.21875 215.5 162.320312 204.816406 180.277344 204.816406 C 198.238281 204.816406 214.339844 215.5 221.363281 232.089844 C 224.015625 238.328125 221.101562 245.5625 214.871094 248.152344 C 208.640625 250.804688 201.417969 247.886719 198.832031 241.648438 C 195.652344 234.214844 188.363281 229.371094 180.277344 229.371094 C 172.195312 229.371094 164.972656 234.214844 161.789062 241.648438 C 159.867188 246.292969 155.296875 249.144531 150.523438 249.144531 Z M 150.523438 249.144531 "
                              fillOpacity="1" fillRule="nonzero" className={isActiveLogo ? styles.activeUp : ""}
                              id={"logoUpper"}/>
                        <path fill="rgb(52.549744%, 82.749939%, 92.158508%)"
                              d="M 209.867188 268.441406 C 211.457031 268.441406 213.050781 268.773438 214.640625 269.4375 C 220.867188 272.09375 223.785156 279.261719 221.132812 285.496094 C 214.175781 302.089844 198.074219 312.773438 180.113281 312.773438 C 162.15625 312.773438 146.050781 302.089844 139.027344 285.496094 C 136.375 279.261719 139.292969 272.027344 145.523438 269.4375 C 151.75 266.785156 158.972656 269.703125 161.558594 275.941406 C 164.738281 283.375 172.027344 288.21875 180.113281 288.21875 C 188.199219 288.21875 195.421875 283.375 198.601562 275.941406 C 200.523438 271.296875 205.097656 268.441406 209.867188 268.441406 Z M 209.867188 268.441406 "
                              fillOpacity="1" fillRule="nonzero" className={isActiveLogo ? styles.activeBottom : ""}
                              id={"logoBottom"}/>
                    </svg>

                </div>
            </div>
        );
    }

    return (
        <div>
            <div className={styles.datePickerPart}>
                <div className={styles.datePickerPartRight}>
                    <DatePicker getDate={getDate}/>
                </div>
            </div>
            <div className={styles.undercalendar}>
                <div className={styles.datedisplay}>
                    <p><strong>
                        {date?.format("dddd MMMM D, YYYY")}
                    </strong>
                    </p>
                </div>
                <div className={'calendar-description '.concat(styles.caldescr)}>

                    <p className={' '.concat(styles.pdescr)}>
                        <strong>Reservations: </strong> {data.length}
                    </p>
                </div>
            </div>
            {openModal && <ModalReservationDetails onClose={() => setOpenModal(false)} data={client}/>}
            {openModalCreateReservation &&
                <ModalCreateReservation closeModal={setOpenModalCreateReservation} onExit={reload}/>}
            <div className={styles.tableContainer}>
                <table>
                    <thead>
                    <tr>
                        <td className={styles.collCheckbox}>
                        </td>
                        <td className={styles.border}>
                            <p>Event</p>
                        </td>
                        <td className={styles.border}>
                            <p>Reservation code</p>
                        </td>
                        <td className={styles.border}>
                            <p>Price</p>
                        </td>
                        <td className={styles.border}>
                            <p>Capacity</p>
                        </td>
                        <td className={styles.border}>
                            <p>Customer</p>
                        </td>
                        <td className={styles.border}>
                            <p>State</p>
                        </td>
                        <td className={styles.border}>
                            <p>Date</p>
                        </td>
                        <td>
                            <button className={'button-primary'}
                                    onClick={() => {
                                        setOpenModalCreateReservation(true)
                                    }}>Create Reservation
                            </button>
                        </td>
                    </tr>
                    </thead>
                    <tbody>
                    {data.map(reservation => (
                        <React.Fragment key={reservation.reservationId}>
                            <tr className={styles.headRow}>
                                <td className={styles.collCheckboxOut}>
                                    <button onClick={() => toggleShown(reservation.reservationId)}>
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="19" fill="none"
                                             viewBox="0 0 16 19">
                                            <path fill="#000"
                                                  d="M.685 6.14 2.05 4.55l5.581 6.524 5.593-6.51 1.362 1.593-6.958 8.1L.685 6.14Z"/>
                                        </svg>
                                    </button>
                                </td>
                                <td className={styles.outline}>from: {reservation.fromTime}<br/>to: {reservation.toTime}
                                </td>
                                <td className={styles.outline}>{reservation.reservationId}</td>
                                <td className={styles.outline}><b>{reservation.pricing} $</b></td>
                                <td className={styles.outline}>{reservation.capacity}</td>
                                <td className={styles.outline}>{reservation.userFirstName} {reservation.userLastName}</td>
                                <td className={styles.outline}>{reservation.state}</td>
                                <td className={styles.outline}>{reservation.date}</td>
                                <td className={styles.outline}>
                                    <div className={styles.buttonsTable}>
                                        <div className={styles.detailsButton}>
                                            <button className={'button-primary-outline'}
                                                    onClick={() => {
                                                        setOpenModal(true)
                                                        setClient(reservation)
                                                    }}>Details
                                            </button>
                                        </div>
                                        {/*<button className={'button-primary-outline '}>Cancel</button>*/}
                                    </div>
                                </td>
                            </tr>
                            {detailsShown.includes(reservation.reservationId) && (
                                <tr>
                                    {reservation.hasOwnProperty('seatIdentifier') ?
                                        <td colSpan="2" className={styles.toggleInfo}>
                                            <strong>Seat: </strong> {reservation.seatIdentifier}</td> :
                                        <td colSpan="2" className={styles.toggleInfo}>
                                        </td>}
                                    <td colSpan="3" className={styles.toggleInfo}>
                                        <strong>Sources: </strong>{reservation.sources}
                                    </td>
                                    <td colSpan="4" className={styles.toggleInfo}><strong>E-mail: </strong><a
                                        className={styles.mailHref}
                                        href={"mailto:" + reservation.userEmail}>{reservation.userEmail}</a></td>
                                </tr>
                            )}
                        </React.Fragment>
                    ))}
                    <div className={styles.containerFunction}>
                    </div>
                    </tbody>
                </table>
            </div>

        </div>
    )
}

export const ReservationsPageClient = () => {
    return (
        <div className={styles.container}>
            <Table/>
        </div>
    )
}