import React, {useEffect, useState} from "react";
import styles from './DashboardPage.module.scss'

import {
    Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend,
} from 'chart.js';

import {Line} from 'react-chartjs-2';
import axios from "axios";
import {baseUrl} from "../../../config/const";
import authHeader from "../../../services/auth-header";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

export const ChartReservation = () => {
    const [data, setData] = useState(undefined);

    const getLastWeeksDate = () => {
        const now = new Date();
        return new Date(now.getFullYear(), now.getMonth(), now.getDate() - 7);
    }

    useEffect(() => {

        const fillDay = () => {
            let labels = [];

            const now = new Date();
            for (let d = getLastWeeksDate(); d <= now; d.setDate(d.getDate() + 1)) {
                const newDay = new Date(d);
                labels.push(newDay.getDate() + " " + Number(newDay.getMonth() + 1));
            }
            return labels;
        }

        const day = (today) => {
            const year = today.getFullYear();
            let month = "";
            let date = "";
            if (today.getMonth() < 10) {
                month = "0" + Number(today.getMonth() + 1);
            } else {
                month = Number(today.getMonth() + 1);
            }
            if (today.getDate() < 10) {
                date = "0" + today.getDate();
            } else {
                date = today.getDate();
            }
            const day = year + "-" + month + "-" + date;
            return day;
        }


        const getNewReservation = async (e) => {
            return new Promise((resolve, reject) => {
                axios.get(`${baseUrl}/systems/reservations/today`, {
                    headers: authHeader(), params: {"fromDate": day(new Date(new Date().setDate(e[0])))}
                })
                    .then(response => {
                        resolve(response.data.length)
                    })
                    .catch(reject);
            })
        }

        const getDataNewReservations = async (data) => {
            let generatedResponse = []
            await Promise.all(data.map(async (e) => {
                try {
                    generatedResponse.push(await getNewReservation(e))
                } catch (error) {
                    console.log('error' + error);
                }
            }))
            return generatedResponse
        }


        const fillData = async (labels) => {
            let data = {
                labels, datasets: [{
                    label: 'Reservations',
                    data: [],
                    borderColor: 'rgb(53, 162, 235)',
                    backgroundColor: 'rgba(53, 162, 235, 0.5)',
                },]
            }

            data.datasets[0].data = await getDataNewReservations(labels);

            return data;
        }


        let days = fillDay();
        Promise.all(days).then(async r => {
            setData((await fillData(r)));
        })

    }, [])

    const options = {
        responsive: true, plugins: {
            legend: {
                position: 'top',
            }, title: {
                display: true, text: 'Rezervace za poslední týden',
            },
        }, layout: {
            padding: 15
        },
    };

    if (data === undefined) {
        return (<div className={styles.chart}>
            <div className={styles.x}>
                <svg xmlns="http://www.w3.org/2000/svg" width="25vh" zoomAndPan="magnify"
                     viewBox="0 0 375 374.999991" height="25vh" preserveAspectRatio="xMidYMid meet"
                     version="1.0">
                    <path fill="rgb(54.899597%, 92.158508%, 76.469421%)"
                          d="M 150.523438 249.144531 C 148.933594 249.144531 147.34375 248.816406 145.753906 248.152344 C 139.523438 245.496094 136.609375 238.328125 139.257812 232.089844 C 146.21875 215.5 162.320312 204.816406 180.277344 204.816406 C 198.238281 204.816406 214.339844 215.5 221.363281 232.089844 C 224.015625 238.328125 221.101562 245.5625 214.871094 248.152344 C 208.640625 250.804688 201.417969 247.886719 198.832031 241.648438 C 195.652344 234.214844 188.363281 229.371094 180.277344 229.371094 C 172.195312 229.371094 164.972656 234.214844 161.789062 241.648438 C 159.867188 246.292969 155.296875 249.144531 150.523438 249.144531 Z M 150.523438 249.144531 "
                          fillOpacity="1" fillRule="nonzero" className={styles.activeUp}
                          id={"logoUpper"}/>
                    <path fill="rgb(52.549744%, 82.749939%, 92.158508%)"
                          d="M 209.867188 268.441406 C 211.457031 268.441406 213.050781 268.773438 214.640625 269.4375 C 220.867188 272.09375 223.785156 279.261719 221.132812 285.496094 C 214.175781 302.089844 198.074219 312.773438 180.113281 312.773438 C 162.15625 312.773438 146.050781 302.089844 139.027344 285.496094 C 136.375 279.261719 139.292969 272.027344 145.523438 269.4375 C 151.75 266.785156 158.972656 269.703125 161.558594 275.941406 C 164.738281 283.375 172.027344 288.21875 180.113281 288.21875 C 188.199219 288.21875 195.421875 283.375 198.601562 275.941406 C 200.523438 271.296875 205.097656 268.441406 209.867188 268.441406 Z M 209.867188 268.441406 "
                          fillOpacity="1" fillRule="nonzero" className={styles.activeBottom}
                          id={"logoBottom"}/>
                </svg>
            </div>
        </div>);
    }

    return (<div className={styles.chart}>
        <Line options={options} data={data} redraw={false} className={styles.chartLine}/>
    </div>)
}