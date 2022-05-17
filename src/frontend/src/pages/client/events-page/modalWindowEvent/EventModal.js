import React from 'react';
import styles from "../EventsPage.module.scss";


function EventModal({closeModal}) {

    let title = localStorage.getItem("eventTitle");
    let dateFrom = localStorage.getItem("eventStart").substring(0, 24);
    let dateTo = localStorage.getItem("eventEnd").substring(0, 24);

    return (
        <div className={styles.eventModalBackground}>
            <div className={styles.eventContent}>
                <h1>{title}</h1>
                <div>
                    <div className={styles.eventModalTermin}>
                        <p>Termín:</p>
                        <div className={styles.eventModalDates}>
                            <div>od {dateFrom}</div>
                            <div>do {dateTo}</div>
                        </div>
                    </div>
                </div>
                <div className={styles.eventButtons}>
                    <div>
                        <button className={'button-primary-outline'} onClick={() => closeModal(false)}>
                            Close
                        </button>
                    </div>
                    <div>
                        <button className={'button-primary-outline'} onClick={() => closeModal(false)}>
                            Edit
                        </button>
                    </div>
                    <div>
                        <button className={'button-primary-outline'} onClick={() => closeModal(false)}>
                            Delete
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )

}

export default EventModal