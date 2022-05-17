import styles from './ModalNewCustomer.module.scss'
import {ArcElement, Chart as ChartJS, Legend, Tooltip} from 'chart.js';
import {getIn, useFormik} from 'formik';
import {useState} from "react";
import logo from "../../../../assets/resnow.png";
import ReactTooltip from "react-tooltip";
import AuthService from "../../../../services/auth.service";
import authService from "../../../../services/auth.service";
import axios from "axios";
import {baseUrl} from "../../../../config/const";
import authHeader from "../../../../services/auth-header";

ChartJS.register(ArcElement, Tooltip, Legend);

const Form = () => {

    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');

    const [error, setError] = useState('');

    const valid = (e) => {
        e.preventDefault();

        let valid = true;

        if (firstname.trim().length === 0 ||
            lastname.trim().length === 0 ||
            username.trim().length === 0 ||
            email.trim().length === 0
        ) {
            setError('Please fill data')
            valid = false;
            e.preventDefault()
        } else {
            const regex = /[^a-zA-ZÀ-Žà-ž]/;
            if (firstname.trim().length >= 2) {
                if (firstname.match(regex)) {
                    setError("Incorrect first name format.")
                    valid = false;
                    e.preventDefault();
                    return;

                }
            } else {
                setError("First name is too short")
                valid = false;
                e.preventDefault();
                return;
            }
            if (lastname.trim().length >= 2) {
                if (lastname.match(regex)) {
                    setError("Incorrect last name format.")
                    valid = false;
                    e.preventDefault();
                    return;
                }
            } else {
                setError("Last name is too short")
                valid = false;
                e.preventDefault();
                return;
            }
            if (username.trim().length >= 4) {
                if (lastname.match(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{4,20}$/)) {
                    setError("Incorrect last name format.")
                    valid = false;
                    e.preventDefault();
                    return;
                }
            } else {
                setError("Username is too short")
                valid = false;
                e.preventDefault();
                return;
            }
            if (!email.match(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/)) {
                setError("Incorrect email format.")
                valid = false;
                e.preventDefault();
                return;
            }
        }

        if (valid) {
            AuthService.register(firstname, lastname, username, email, "123456", "ROLE_REGULAR_USER").then(
                () => {
                    props.onClose()
                },
                (error) => {
                    const resMessage =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();
                    console.log(resMessage);
                    setError(resMessage);
                }
            );
        }
    }

    // const validate = values => {
    //     const errors = {};
    //
    //     if (!values.firstName) {
    //         errors.firstName = 'Toto pole je povinné.';
    //     } else if (values.firstName.length < 2) {
    //         errors.firstName = 'Musí mít minimálně 2 znaky.';
    //     }
    //
    //     if (!values.secondName) {
    //         errors.secondName = 'Toto pole je povinné.';
    //     } else if (values.secondName.length < 2) {
    //         errors.secondName = 'Musí mít minimálně 2 znaky.';
    //     }
    //
    //     if (!values.email) {
    //         errors.email = 'Toto pole je povinné.';
    //     } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.email)) {
    //         errors.email = 'Neplatná emailová adresa.';
    //     }
    //
    //     if (!values.telephone) {
    //         errors.telephone = 'Toto pole je povinné.';
    //     } else if (!/^[0-9\b]+$/i.test(values.telephone)) {
    //         errors.telephone = 'Neplatný telefon.';
    //     }
    //
    //     return errors;
    // };

    // const formik = useFormik({
    //     initialValues: {firstName: '',
    //                     secondName: '',
    //                     email
    //     validate,
    //     onSubmit: values => {
    //         alert(JSON.stringify(values, null, 2));
    //     },
    // });
    //
    // function getStyles(errors, fieldName) {
    //     if (getIn(errors, fieldName)) {
    //         return {
    //             border: '1px solid red'
    //         }
    //     }
    // }

    return (
        <div className={styles.modal} onClick={props.onClose}>
            <div className={styles.content} onClick={(e) => e.stopPropagation()}>
                <form className={styles.form} onSubmit={e => valid(e)}>
                    <div className={styles.flexRow}>
                        <div className={styles.leftInputs}>
                            <label>
                                Firstname
                                <input
                                    className={'input-primary '.concat(error.trim().length !== 0 ? "error" : "")}
                                    type={'text'}
                                    value={firstname}
                                    placeholder={'John'}
                                    onClick={() => setError('')}
                                    onChange={(e) => setFirstname(e.target.value)}
                                />
                            </label>
                            <label>
                                Lastname
                                <input
                                    className={'input-primary '.concat(error.trim().length !== 0 ? "error" : "")}
                                    type={'text'}
                                    value={lastname}
                                    placeholder={'Lemon'}
                                    onClick={() => setError('')}
                                    onChange={(e) => {
                                        setLastname(e.target.value)
                                    }}
                                />
                            </label>
                            <label>
                                Username
                                <input
                                    className={'input-primary '.concat(error.trim().length !== 0 ? "error" : "")}
                                    type={'text'}
                                    value={username}
                                    placeholder={'lemonade'}
                                    autoComplete="username"
                                    onClick={() => setError('')}
                                    onChange={(e) => {
                                        setUsername(e.target.value)
                                    }}
                                />
                            </label>
                            <label>
                                Email
                                <input
                                    className={'input-primary '.concat(error.trim().length !== 0 ? "error" : "")}
                                    type={'email'}
                                    value={email}
                                    placeholder={'john@example.com'}
                                    onClick={() => setError('')}
                                    onChange={(e) => {
                                        setEmail(e.target.value)
                                    }}
                                />
                            </label>
                        </div>
                    </div>
                    <div className={styles.errorMessage}>
                        {error}
                    </div>
                    <button
                        type={'submit'}
                        className={'button-primary bx-sh'}
                    >Register
                    </button>
                </form>
            </div>
        </div>
    )
}

let props;

export const ModalNewCustomer = (prop) => {
    props = prop;
    if (!props.show) return (<></>);
    return <Form/>
}