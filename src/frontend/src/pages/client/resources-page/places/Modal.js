import styles from './Places.module.scss'
import {useState} from "react";
import axios from "axios";
import {baseUrl} from "../../../../config/const";
import authHeader from "../../../../services/auth-header";

export const Modal = (props) => {


    const [city, setCity] = useState('');
    const [street, setStreet] = useState('');
    const [houseNum, setHouseNum] = useState('');
    const [postCode, setPostCode] = useState('');

    const [error, setError] = useState('');

    const handle = () => {
        if (
            city.length === 0 ||
            street.length === 0 ||
            houseNum.length === 0 ||
            postCode.length === 0
        ) {
            setError("Please fill data")
        } else {
            axios.post(
                `${baseUrl}/`,
                {},
                {
                    headers: authHeader(),
                    params: {}
                }
            ).then(() => {
                props.onClose()
            })
        }
    }

    return (
        <div className={styles.modal} onClick={props.onClose}>
            <div className={styles.modalContainer} onClick={(e) => e.stopPropagation()}>
                <form>
                    <label>
                        City
                        <input
                            className={'input-primary'}
                            type={'text'}
                            value={city}
                            onChange={(e) => setCity(e.target.value)}
                        />
                    </label>
                    <label>
                        Street
                        <input
                            className={'input-primary'}
                            type={'text'}
                            value={street}
                            onChange={(e) => setStreet(e.target.value)}
                        />
                    </label>
                    <label>
                        House Number
                        <input
                            className={'input-primary'}
                            type={'text'}
                            value={houseNum}
                            onChange={(e) => setHouseNum(e.target.value)}
                        />
                    </label>
                    <label>
                        Post Code
                        <input
                            className={'input-primary'}
                            type={'text'}
                            value={postCode}
                            onChange={(e) => setPostCode(e.target.value)}
                        />
                    </label>
                    <button className={'button-primary'} type={'button'} onClick={handle}>Uložit</button>
                    <div className={styles.error}>{error}</div>
                </form>
            </div>
        </div>
    )
}