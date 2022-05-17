import styles from '../modalWindowNew/ModalNew.module.scss'
import {Chart as ChartJS, ArcElement, Tooltip, Legend} from 'chart.js';
import {useState} from "react";

ChartJS.register(ArcElement, Tooltip, Legend);

export const ModalEdit = (props) => {
    const [title, setTitle] = useState('')

    if (!props.show) return (
        <></>
    )

    const handleTitle = (e) => {
        setTitle(e.target.value)
    }

    const handleSubmit = (e) => {
        alert('Title-"' + title
            /*'    SecondName-"' + secondName +
        '    Email-' + email +
        '    Telephone-' + telephone +
        '    Description-' + description +
        '    Active-' + active*/);

        e.preventDefault();
    }

    return (
        <div className={styles.modal} onClick={props.onClose}>
            <div className={styles.content} onClick={(e) => e.stopPropagation()}>
                <div className={styles.header}>
                    <p>Editace zdroju</p>
                </div>
                <form className={styles.form} onSubmit={(e) => {handleSubmit(e)}}>
                    <div>
                        <label htmlFor="title">Název</label>
                        <input id="title" className={'input-primary search sh'} placeholder={'Název'} required
                               value={title} onChange={(e) => {handleTitle(e)}}/>
                        <label htmlFor="place">Místo</label>
                        <select className={styles.selectContainer}>
                            <option disabled>-</option>
                            <option selected value="place1">place1</option>
                            <option value="place2">place2</option>
                            <option value="place3">place3</option>
                        </select >
                        <label htmlFor="service">Služba</label>
                        <select className={styles.selectContainer}>
                            <option disabled>-</option>
                            <option value="service1">service1</option>
                            <option value="service2">service2</option>
                            <option selected value="service3">service3</option>
                        </select>
                        <label htmlFor="employee">Zaměstnanec</label>
                        <select className={styles.selectContainer}>
                            <option disabled>-</option>
                            <option value="employee1">employee1</option>
                            <option selected value="employee2">employee2</option>
                            <option value="employee3">employee3</option>
                        </select>
                        <div className={styles.buttons}>
                            <button className={'button-primary-outline ' .concat(styles.buttonSave)} type="submit">Uložit</button>
                            <button className={'button-primary-outline ' .concat(styles.buttonCancel)} onClick={props.onClose}>Storno</button>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    )
}