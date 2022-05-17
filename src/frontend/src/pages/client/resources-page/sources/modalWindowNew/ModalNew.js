import styles from './ModalNew.module.scss'
import {Chart as ChartJS, ArcElement, Tooltip, Legend} from 'chart.js';
import {useEffect, useMemo, useState} from "react";
import axios from "axios";
import {baseUrl} from "../../../../../config/const";
import authHeader from "../../../../../services/auth-header";
// import MOCK_DATA from "../MOCK_DATA.json"
// import Select from "react-select/base";

ChartJS.register(ArcElement, Tooltip, Legend);

export const ModalNew = (props) => {
    // const data = useMemo(() => MOCK_DATA, [])
    const [title, setTitle] = useState('')

    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [option, setOption] = useState('custom');

    const [error, setError] = useState();

    const handle = () => {
        if (
            name.length === 0 ||
            description.length === 0
        ) {
            setError("Please fill data")
        } else {
            if (option === "custom") {
                axios.post(
                    `${baseUrl}/systems/my/sources`,
                    {
                        "name": name,
                        "description": description,
                        "address": {
                            city: city,
                            street: street,
                            houseNumber: houseNum,
                            postalCode: postCode
                        }
                    },
                    {
                        headers: authHeader(),
                        params: {
                            "name": name,
                            "description": description
                        }
                    }
                ).then((r) => {
                    props.onClose()
                    window.location.reload()
                })
            } else {
                let address = JSON.parse(option);
                console.log(address)
                axios.post(
                    `${baseUrl}/systems/my/sources`,
                    {
                        "name": name,
                        "description": description,
                        "address": {
                            city: address.city,
                            street: address.street,
                            houseNumber: address.houseNumber,
                            postalCode: address.postalCode
                        }
                    },
                    {
                        headers: authHeader(),
                        params: {
                            "name": name,
                            "description": description
                        }
                    }
                ).then((r) => {
                    props.onClose()
                    window.location.reload()
                })
            }

        }
    }

    const [address, setAddress] = useState([]);

    const [city, setCity] = useState('');
    const [street, setStreet] = useState('');
    const [houseNum, setHouseNum] = useState('');
    const [postCode, setPostCode] = useState('');

    useEffect(async () => {
        const fetchAddress = await Promise.any([
                axios.get(
                    `${baseUrl}/systems/my/sources`,
                    {headers: authHeader()})
            ]
        )
        setAddress(fetchAddress.data)

    }, [])

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
                {/*<div className={styles.header}>*/}
                {/*    <p>Nový zdroj</p>*/}
                {/*</div>*/}
                {/*<form className={styles.form} onSubmit={(e) => {handleSubmit(e)}}>*/}
                {/*    <div>*/}
                {/*        <label htmlFor="title">Název</label>*/}
                {/*        <input id="title" className={'input-primary search sh'} placeholder={'Název'} required*/}
                {/*               value={title} onChange={(e) => {handleTitle(e)}}/>*/}
                {/*        <label htmlFor="place">Místo</label>*/}
                {/*        <select className={styles.selectContainer}>*/}
                {/*            <option selected>-</option>*/}
                {/*            <option value="place1">place1</option>*/}
                {/*            <option value="place2">place2</option>*/}
                {/*            <option value="place3">place3</option>*/}
                {/*        </select >*/}
                {/*        <label htmlFor="service">Služba</label>*/}
                {/*        <select className={styles.selectContainer}>*/}
                {/*            <option selected>-</option>*/}
                {/*            <option value="service1">service1</option>*/}
                {/*            <option value="service2">service2</option>*/}
                {/*            <option value="service3">service3</option>*/}
                {/*        </select>*/}
                {/*        <label htmlFor="employee">Zaměstnanec</label>*/}
                {/*        <select className={styles.selectContainer}>*/}
                {/*            <option selected>-</option>*/}
                {/*            <option value="employee1">employee1</option>*/}
                {/*            <option value="employee2">employee2</option>*/}
                {/*            <option value="employee3">employee3</option>*/}
                {/*        </select>*/}
                {/*        <div className={styles.buttons}>*/}
                {/*            <button className={'button-primary-outline ' .concat(styles.buttonSave)} type="submit">Uložit</button>*/}
                {/*            <button className={'button-primary-outline ' .concat(styles.buttonCancel)} onClick={props.onClose}>Storno</button>*/}
                {/*        </div>*/}
                {/*    </div>*/}

                {/*</form>*/}
                <form>
                    <label>
                        Name
                        <input
                            className={'input-primary'}
                            type={'text'}
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />
                    </label>
                    <label>
                        Description
                        <input
                            className={'input-primary'}
                            type={'text'}
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                        />
                    </label>
                    <label>
                        Place
                        <select className={'input-primary'} onChange={(e) => setOption(e.target.value)}>
                            <option value={'custom'}>Custom</option>
                            {address.map(address => {
                                return (
                                    <option value={JSON.stringify(address.address)}>
                                        {address.address.city + " "}
                                        {address.address.street + " "}
                                        {address.address.houseNumber + " "}
                                    </option>
                                )
                            })}
                        </select>
                    </label>
                    {option === "custom" ? <div><label>
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
                        </label></div> : null}

                    <button className={'button-primary'} type={'button'} onClick={handle}>Uložit</button>
                    <div className={styles.error}>{error}</div>
                </form>
            </div>
        </div>
    )
}