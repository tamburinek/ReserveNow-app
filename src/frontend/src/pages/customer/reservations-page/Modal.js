import styles from './Reservations.module.scss'
import {useState} from "react";
import axios from "axios";
import {baseUrl} from "../../../config/const";
import authHeader from "../../../services/auth-header";

export const Modal = (props) => {

    const [text, setText] = useState('');
    const [error, setError] = useState('');

    const handle = (e) => {
        e.preventDefault();
        if (text.trim().length === 0) {
            setError("Please fill data")
        } else {
            axios.post(
                `${baseUrl}/systems/${props.system}/feedback`,
                {"message": text},
                {headers: authHeader()}
            ).then(() => {
                props.onClose()
                alert("Successful 🤌")
            })
        }
    }

    return (
        <div className={styles.modal} onClick={props.onClose}>
            <div className={styles.modalContainer} onClick={e => e.stopPropagation()}>
                <form>
                    <label>
                        Feedback
                        <input
                            className={'input-primary'}
                            type={"text"}
                            value={text}
                            onChange={e => setText(e.target.value)}
                        />
                    </label>
                    <div className={styles.error}>{error}</div>
                    <button type={'button'} className={'button-primary'} onClick={e => handle(e)}>Add</button>
                </form>
            </div>
        </div>
    )

}