import axios from "axios";
import {baseUrl} from '../../../../config/const';
import authHeader from '../../../../services/auth-header';

let current = new Date();
let first = current.getDate() - current.getDay();
let last = first + 6;

let currentDay = new Date(current.setDate(first)).toISOString().substring(0, 10);
let lastDay = new Date(current.setDate(last)).toISOString().substring(0, 10);

class EventUtils {

    //rest/v1/systems/1/events
    getAllEvents() {
        return axios.get(`${baseUrl}/systems/my/events`, {
            headers: authHeader(),
            params: {
                fromDate: currentDay,
                toDate: lastDay
            }
        });
    }

    //patch for update selected event [name, timeFrom, timeTo, ...]
    updateEvent(id, name, timeFrom, timeTo) {
        return axios.put('${baseUrl}/endpoint', {
            "name": "test",
            "fromTime": "12:00:00",
            "toTime": "14:00:00",
            "startDate": "2022-05-14",
            // "repeatUntil": "2022-05-15",
            // "day": 3,
            // "repetition": "NONE",
            // "categoryId": "1",
        }, {
            headers: authHeader()
        })
            .then(response => {
                console.log("Event Updated")
            })
    }

    //put for new event [name, timeFrom, timeTo, ...]
    newCustomTimeEvent(sourceId, name, timeFrom, timeTo, startDate, repeatUntil, day, repetition, minimalReservationTime) {
        return axios.post(`${baseUrl}/categories/${sourceId}/events`, {
            "name": "test eventu",
            "fromTime": "10:00:00",
            "toTime": "11:00:00",
            "startDate": "2022-05-11",
            "repeatUntil": "2022-05-11",
            "day": 3,
            "repetition": "DAILY",
            "categoryId": "0",
            "minimalReservationTime": 3600,
        }, {
            headers: authHeader()
        })
            .then(response => {
                console.log(response)
                console.log("Event created")
            })
    }

    newSeatEvent(sourceId, name, timeFrom, timeTo, startDate, seat) {
        return axios.post(`${baseUrl}/categories/${sourceId}/events`, {
            "name": name,
            "fromTime": timeFrom,
            "toTime": timeTo,
            "startDate": startDate,
            "repeatUntil": startDate,
            "day": 3,
            "repetition": "DAILY",
            "categoryId": "0",
            "seatAmount": seat,
        }, {
            headers: authHeader()
        })
    }

    newIntervalEvent(sourceId, name, timeFrom, timeTo, startDate) {
        return axios.post(`${baseUrl}/categories/${sourceId}/events`, {
            "name": name,
            "fromTime": timeFrom,
            "toTime": timeTo,
            "startDate": startDate,
            "repeatUntil": startDate,
            "day": 6,
            "repetition": "DAILY",
            "categoryId": "0",
            "minimalReservationTime": 0,
        }, {
            headers: authHeader()
        })
    }
}

export default new EventUtils();