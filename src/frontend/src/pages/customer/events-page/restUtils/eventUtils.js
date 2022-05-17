import axios from "axios";
import {baseUrl} from '../../../../config/const';
import authHeader from '../../../../services/auth-header';

let current = new Date();
let first = current.getDate() - current.getDay();
let last = first + 6;

let currentDay = new Date(current.setDate(first)).toISOString().substring(0, 10);
let lastDay = new Date(current.setDate(last)).toISOString().substring(0, 10);

class EventUtils {
    getAllSlots(id) {
        return axios.get(`${baseUrl}/events/${id}/slots`, {
            headers: authHeader(),
            params: {
                fromTimestamp: currentDay,
                toTimestamp: lastDay
            }
        });
    }

    //put for new reservation on customer [name, surname, email, ...]
    newReservation(slotId, name, info) {
        return axios.post(`${baseUrl}/slots/${slotId}`, {name, info}, {
            headers: authHeader()
        })
            .then(response => response.data)
            .then(data => {
                console.log("Reservation created " + data)
            }).catch(e => {
                console.log(e)
            })
    }
}

export default new EventUtils();