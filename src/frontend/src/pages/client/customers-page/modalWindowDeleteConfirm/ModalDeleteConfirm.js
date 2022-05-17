import styles from './ModalDeleteConfirm.module.scss'
import {Chart as ChartJS, ArcElement, Tooltip, Legend} from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend);

export const ModalDeleteConfirm = (props) => {
    if (!props.show) return null;

    return (
        <div className={styles.modal} onClick={props.onClose}>
            <div className={styles.content} onClick={(e) => e.stopPropagation()}>
                <div className={styles.header}>
                    <p>Opravdu chcete smazat?</p>
                </div>
                <div className={styles.buttons}>
                    <button className={'button-primary-outline ' .concat(styles.buttonYes)}>Ano</button>
                    <button className={'button-primary-outline ' .concat(styles.buttonNo)} onClick={props.onClose}>Ne</button>
                </div>
            </div>
        </div>
    )
}