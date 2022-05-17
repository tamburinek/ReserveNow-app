import React, {useEffect, useState} from "react";
import styles from './ModalCreateReservation.module.scss';
import DatePicker, {DateObject} from "react-multi-date-picker"
import axios from "axios";
import {baseUrl} from "../../../../config/const";
import authHeader from "../../../../services/auth-header";


function Modal({closeModal, onExit}) {

    const [date, setDate] = useState(new DateObject());

    const [events, setEvents] = useState([]);
    const [sources, setSources] = useState([]);
    const [currSource, setSource] = useState(sources[0]);
    const [currEvent, setEvent] = useState(events[0]);
    const [currUser, setUser] = useState(null);
    const [timeslots, setTimeslots] = useState([]);
    const [currTS, setCurrTS] = useState(null);
    const [description, setDescription] = useState("no desc");
    const [customers, setCustomers] = useState([]);

    const [resDTO, setResDTO] = useState(null);
    let counter = 1500;

    const [firstUseEffect, setFirstUF] = useState(false)

    const [showError, setShowError] = useState(false);

    useEffect(() => {
        setUser(null)
        setCurrTS(null)
        sendGetRequest(date.format("YYYY-MM-DD"));
        getCustomers();
    }, [date])


    const sendGetRequest = async (date) => {
        setShowError(false)
        try {
            const resp0 = await axios.get(
                `${baseUrl}/systems/my`,
                {headers: authHeader()})
            let system = resp0.data.id;

            const respEvents = await axios.get(
                `${baseUrl}/systems/${system}/events/`,
                {
                    params: {fromDate: date, toDate: date},
                    headers: authHeader()
                })
            setEvents(respEvents.data)
            setEvent(respEvents.data[0])
            console.log(respEvents.data[0].id)
            getTS(respEvents.data[0].id, date)
            console.log(respEvents.data)
        } catch (err) {
            // Handle Error Here
            console.error(err);
            console.log("Didnt find any Events for given date")
            setTimeslots([])
            setShowError(true)
        }
    };

    const getCustomers = async () => {
        const respCustomers = await axios.get(
            `${baseUrl}/systems/my/customers/`,
            {headers: authHeader()})

        let tmp = []
        for (let i = 0; i < respCustomers.data.length; i++) {
            tmp.push(respCustomers.data[i].username)
        }
        setCustomers(tmp)
    }

    const getTS = async (event, date) => {
        try {
            const resp = await axios.get(
                `${baseUrl}/events/${event}/slots`,
                {
                    params: {fromTimestamp: date, toTimestamp: date},
                    headers: authHeader()
                })
            setTimeslots(resp.data)
            setCurrTS(resp.data[0].id)
            //console.log(resp.data)

        } catch (err) {
            // Handle Error Here
            console.error(err);
        }
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        if (currUser === null) {
            alert("NO USER CHOSEN")
            return
        }
        if (currTS === null) {
            alert("NO TIMESLOT CHOSEN")
            return
        }

        try {
            axios.post(
                `${baseUrl}/slots/${currTS}/admin/`,
                {
                    "paymentId": 0,
                    "reservationId": counter,
                    "additionalInfo": e.target.description.value,
                    "reservationSlotId": currTS,
                    "username": currUser
                },
                {
                    headers: authHeader()
                })
            alert("Reservation Created!:)")
            closeModal(false)
            onExit()

        } catch (err) {
            // Handle Error Here
            console.error(err);
            //console.log("No user found")
        }

    }

    function makeButtonCustomer(data) {
        return (
            <button
                key={data}
                className={styles.customerBtn}
                type="button"
                value={data}
                onClick={clickedCustomer}>
                {data}
            </button>
        );
    }

    function clickedCustomer(e) {
        setUser(e.target.value)
        console.log(e.target.value)
    }

    const changeTS = (e) => {
        setCurrTS(e.target.value)
        console.log(e.target.value)
    }

    return (
        <div className={styles.modalWindow}>
            <div className={styles.contentBody}>
                <h1 className={styles.popisDate}>Nová rezervace:</h1>
                <form onSubmit={handleSubmit}>
                    <div className={styles.datePart}>
                        <p>Choose date:</p>
                        <DatePicker
                            value={date}
                            onChange={setDate}
                        />
                    </div>
                    {showError === false &&
                        <select className={'input-primary'}>{
                            events.map((event) =>
                                <option value={event.value} key={event.name}>{event.name}</option>)
                        }</select>}


                    {currEvent === undefined &&
                        <h2>No events for chosen date</h2>
                    }

                    <h3>Choose TimeSlot/Seat</h3>
                    {timeslots !== [] &&
                        <select onChange={changeTS} className={'input-primary'}>{
                            timeslots.map((ts) => {
                                if (ts.hasOwnProperty('seatIdentifier'))
                                    return <option value={ts.id} key={ts.id}>Seat: {ts.seatIdentifier}</option>
                                else return <option value={ts.id} key={ts.id}>EndTime: {ts.endTime}</option>
                            })
                        })
                            }</select>
                    }

                    <label className={styles.chooseCustomer}>
                        <h3>Choose customer:</h3>
                        <select className={'input-primary'} onChange={(e) => setUser(e.target.value)}>
                            <option value={null}>None</option>
                            {customers.map(customer => (
                                <option value={customer}>{customer}</option>
                            ))}
                        </select>
                    </label>
                    {/*{customers.map(makeButtonCustomer, this)}*/}


                    <textarea id="description" name="description" rows="5" placeholder={"Add additional description"}/>

                    <button type="submit" className={'button-primary '.concat(styles.modalButton)}>
                        <p>SUBMIT</p>
                    </button>
                </form>
                <div className={styles.cancelButton}>
                    <button className={'button-primary '} onClick={() => closeModal(false)}>
                        <p>CANCEL</p>
                    </button>
                </div>
            </div>
        </div>
    )
}

export default Modal