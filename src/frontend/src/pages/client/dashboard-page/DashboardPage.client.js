import styles from './DashboardPage.module.scss'
import journals from './../../../assets/svg/journals.svg'
import journalMedical from './../../../assets/svg/journal-medical.svg'
import calendar2 from './../../../assets/svg/calendar2-event.svg'
import pin from './../../../assets/svg/pin.svg'
import person from './../../../assets/svg/person.svg'
import React, {useEffect, useState} from "react";
import axios from "axios";
import {baseUrl} from "../../../config/const";
import {Chart as ChartJS, ArcElement, Tooltip, Legend} from 'chart.js';
import {Pie} from 'react-chartjs-2';
import {Modal} from "./modalWindow/Modal";
import {Link} from "react-router-dom";
import {LocalDate, LocalDateTime} from "local-date";
import authHeader from "../../../services/auth-header";
import {ChartReservation} from "./ChartReservation";

ChartJS.register(ArcElement, Tooltip, Legend);

const dataWithoutLabel = {
    datasets: [
        {
            label: '# of Votes',
            data: [12, 19],
            backgroundColor: [
                'rgb(186,202,232)',
                'rgb(134,211,235)',
            ],
            borderColor: [
                'rgb(186,202,232)',
                'rgb(134,211,235)',
            ],
            borderWidth: 1,
        },
    ],
};

const PieChart = () => {
    const [show, setShow] = useState(false);

    const current = new Date();

    const data = {
        labels: ['Tenisový kurt', 'Florbalová hala'],
        datasets: [
            {
                label: '# of Votes',
                data: [12, 19],
                backgroundColor: [
                    'rgb(186,202,232)',
                    'rgb(134,211,235)',
                ],
                borderColor: [
                    'rgb(186,202,232)',
                    'rgb(134,211,235)',
                ],
                borderWidth: 1,
            },
        ],
    };

    return (
        <div className={styles.kolacGraph}>
            <p>
                {current.getDate()}.{current.getMonth() + 1}.{current.getFullYear()}
            </p>
            <div className={styles.pie}>
                <Pie data={data} options={{
                    maintainAspectRatio: false
                }}/>
            </div>
            {/*<button type={'button'} onClick={() => setShow(true)}*/}
            {/*        className={'button-primary '.concat(styles.button)}>Zobrazit více*/}
            {/*</button>*/}
            {/*<Modal onClose={() => setShow(false)} show={show} data={dataWithoutLabel}/>*/}
        </div>
    )
}

export const getDate = (year, month, day) => {
    if (month < 10) {
        month = "0".concat(month.toString());
    }
    if (day < 10) {
        day = "0".concat(day.toString());
    }

    return year + "-" + month + "-" + day;
}

export const DashboardPageClient = () => {

    const [todayReservation, setTodayReservation] = useState(undefined);
    const [allEvents, setAllEvents] = useState(undefined);
    const [show, setShow] = useState(false);
    const [feedbacks, setFeedbacks] = useState(undefined);
    const [customers, setCustomers] = useState(undefined);
    const [resources, setResources] = useState(undefined);
    const [reservations, setReservations] = useState(undefined);
    const [isActiveLogo, setIsLogoActive] = useState(false);


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

    useEffect(async () => {
        const fetchFeedbacks = await Promise.any([
                axios.get(
                    `${baseUrl}/systems/my/feedback`,
                    {headers: authHeader()}
                )
            ]
        )
        setFeedbacks(fetchFeedbacks.data)

        const fetchCustomers = await Promise.any([
                axios.get(`${baseUrl}/systems/my/customers`,
                    {headers: authHeader()})
            ]
        )
        fetchCustomers.data.map(item => item.age)
            .filter((value, index, self) => self.indexOf(value) === index)
        setCustomers(fetchCustomers.data.length)

        const fetchSources = await Promise.any([
                axios.get(
                    `${baseUrl}/systems/my/sources`,
                    {headers: authHeader()})
            ]
        )
        setResources(fetchSources.data.length)

        const fetchReservations = await Promise.any(
            [
                axios.get(
                    `${baseUrl}/systems/reservations`,
                    {headers: authHeader()}
                )
            ]
        )
        setReservations(fetchReservations.data.length)

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

        const fetchToday = await Promise.any(
            [
                axios.get(
                    `${baseUrl}/systems/reservations/today`,
                    {
                        headers: authHeader(),
                        params: {"fromDate": today()}
                    }
                )
            ]
        )
        setTodayReservation(fetchToday.data.length)

        const fetchEvents = await Promise.any([
            axios.get(`${baseUrl}/systems/my/all/events`,
                {headers: authHeader()}
            )
        ])
        setAllEvents(fetchEvents.data.length)

    }, [])

    if (todayReservation === undefined || allEvents === undefined || feedbacks === undefined || customers === undefined || resources === undefined === reservations) {
        return (
                <div className={styles.loading}>
                    <div className={styles.logoContainer} onMouseOver={animateLogo}>
                        <svg xmlns="http://www.w3.org/2000/svg" width="25vh" zoomAndPan="magnify"
                             viewBox="0 0 375 374.999991" height="25vh" preserveAspectRatio="xMidYMid meet"
                             version="1.0">
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
        <div className={styles.container}>
            <div className={styles.left}>
                <div className={styles.topContent}>
                    <div className={styles.cards}>
                        <img src={journals} alt={'icon'}/>
                        <p>{todayReservation}</p>
                        <button className={'button-primary '.concat(styles.button)}><Link
                            to={'/app/rezervace'}>Dnešní rezervace</Link>
                        </button>
                        {/*<Modal onClose={() => setShow(false)} show={show} data={dataWithoutLabel}/>*/}

                    </div>
                    <div className={styles.cards}>
                        <img src={journalMedical} alt={'icon'}/>
                        <p>{reservations}</p>
                        <button className={'button-primary '.concat(styles.button)}><Link
                            to={'/app/rezervace'}>Rezervace</Link>
                        </button>
                    </div>
                    <div className={styles.cards}>
                        <img src={calendar2} alt={'icon'}/>
                        <p>{allEvents}</p>
                        <button className={'button-primary '.concat(styles.button)}><Link
                            to={'/app/terminy'}>Termíny</Link>
                        </button>
                    </div>
                    <div className={styles.cards}>
                        <img src={pin} alt={'icon'}/>
                        <p>{resources}</p>
                        <button className={'button-primary '.concat(styles.button)}><Link
                            to={'/app/zdroje'}>Místa</Link>
                        </button>
                    </div>
                    <div className={styles.cards}>
                        <img src={person} alt={'icon'}/>
                        <p>{customers}</p>
                        <button className={'button-primary '.concat(styles.button)}><Link
                            to={'/app/zakaznici'}>Zákazníci</Link>
                        </button>
                    </div>
                </div>
                <div className={styles.bottomContent}>
                    <div className={styles.mainGraph}>
                        <ChartReservation/>
                    </div>
                </div>
            </div>
            <div className={styles.feedbackContainer}>
                <h2>
                    Feedbacks
                </h2>
                <ul>
                    {feedbacks.map(f => {
                        return (
                            <li>{f.message}<br/></li>
                        )
                    })}
                </ul>
            </div>
        </div>
    )
}