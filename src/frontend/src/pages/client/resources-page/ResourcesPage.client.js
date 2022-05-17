import styles from './ResourcesPage.module.scss'
import {useState, useEffect, useMemo} from "react";
// import {useTable} from "react-table"
import {Sources} from "./sources/Sources";
import {Places} from "./places/Places";
import {Employees} from "./employees/Employees";

export const ResourcesPageClient = () => {
    const [source, setSource] = useState(true);
    const [place, setPlace] = useState(false);
    const [employee, setEmployee] = useState(false);

    const activeSource = () => {
        setSource(true)
        setPlace(false)
        setEmployee(false)
    }

    const activePlace = () => {
        setSource(false)
        setPlace(true)
        setEmployee(false)
    }

    const activeEmployee = () => {
        setSource(false)
        setPlace(false)
        setEmployee(true)
    }

    return (
        <div>
            <div className={styles.menu}>
                <button type={'button'} className={source ? styles.active : ""} onClick={activeSource}>Zdroje
                </button>
                <button type={'button'} className={place ? styles.active : ""} onClick={activePlace}>Místa
                </button>
                {/*<button type={'button'} className={employee ? styles.active : ""}*/}
                {/*        onClick={activeEmployee}>Zaměstnanci*/}
                {/*</button>*/}
            </div>
            {source ? <Sources/> : <></>}
            {place ? <Places/> : <></>}
            {employee ? <Employees/> : <></>}
        </div>

    )
}