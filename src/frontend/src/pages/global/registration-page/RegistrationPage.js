import styles from './RegistrationPage.module.scss'
import {useEffect, useState} from "react";
import AuthService from "../../../services/auth.service";
import logo from './../../../assets/resnow.png'
import {useNavigate} from "react-router-dom";
import ReactTooltip from "react-tooltip";
import axios from "axios";
import {baseUrl} from "../../../config/const";
import authService from "../../../services/auth.service";
import authHeader from "../../../services/auth-header";

const Form = () => {

    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [rePassword, setRePassword] = useState('');
    const [system, setSystem] = useState('');
    const [provider, setProvider] = useState(true);
    const [customer, setCustomer] = useState(false);

    const [error, setError] = useState('');

    let navigate = useNavigate();

    useEffect(() => {
        AuthService.logout();
    }, [])

    const valid = (e) => {
        e.preventDefault();

        const userType = provider ? "ROLE_SYSTEM_OWNER" : "ROLE_REGULAR_USER";

        let valid = true;

        if (firstname.trim().length === 0 ||
            lastname.trim().length === 0 ||
            username.trim().length === 0 ||
            email.trim().length === 0 ||
            password.trim().length === 0 ||
            rePassword.trim().length === 0
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
            if (password.trim().length >= 6) {
                if (!password.match(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/)) {
                    setError("Password must contain at least one capital letter and at least one number.")
                    valid = false;
                    e.preventDefault();
                    return;
                }
            } else {
                setError("Password is too short")
                valid = false;
                e.preventDefault();
                return;
            }
            if (rePassword !== password) {
                setError("Passwords do not match")
                valid = false;
                e.preventDefault();
                return;
            }
        }

        if (valid) {
            AuthService.register(firstname, lastname, username, email, password, userType).then(
                () => {
                    if (provider) {
                        authService.login(username, password).then(() => {
                                axios.post(
                                    `${baseUrl}/systems`,
                                    {
                                        "managers": [username],
                                        "name": system
                                    },
                                    {headers: authHeader()}
                                ).then(() => {
                                    navigate("/app/dashboard");
                                    window.location.reload();
                                })
                            }
                        )
                    } else {
                        navigate("/login");
                        window.location.reload();
                    }
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

    return (
        <form className={styles.form} onSubmit={e => valid(e)}>
            <div className={styles.flexRow}>
                <img src={logo} alt={'logo'}/>
                <h2>Registration</h2>
            </div>
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

                    <label>
                        Password
                        <input
                            className={'input-primary '.concat(error.trim().length !== 0 ? "error" : "")}
                            type={'password'}
                            value={password}
                            autoComplete="new-password"
                            onClick={() => setError('')}
                            onChange={(e) => {
                                setPassword(e.target.value)
                            }}
                        />
                    </label>
                    <label
                        data-tip data-for='provider'
                        className={provider ? styles.labelChoose.concat(" ").concat(styles.active) : styles.labelChoose}>
                        Provider
                        <input type={'radio'} value={1} checked={provider ?? "chechked"} onChange={() => {
                            setCustomer(!customer);
                            setProvider(!provider)
                        }}/>
                    </label>
                    <ReactTooltip id='provider' type='dark' effect='solid' place={'left'}>
                        <span>Provider account is used for <br/> people who want to provide<br/> reservations.</span>
                    </ReactTooltip>
                </div>

                <div className={styles.rightInputs}>
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
                        Repeat Password
                        <input
                            className={'input-primary '.concat(error.trim().length !== 0 ? "error" : "")}
                            type={'password'}
                            value={rePassword}
                            autoComplete="new-password"
                            onClick={() => setError('')}
                            onChange={(e) => {
                                setRePassword(e.target.value)
                            }}
                        />
                    </label>
                    <label data-tip data-for='customer'
                           className={customer ? styles.labelChoose.concat(" ").concat(styles.active) : styles.labelChoose}>
                        Customer
                        <input type={'radio'} value={2} checked={customer ?? "checked"} onChange={() => {
                            setCustomer(!customer);
                            setProvider(!provider)
                        }}/>
                    </label>
                    <ReactTooltip id='customer' type='dark' effect='solid' place={'right'}>
                        <span>Customer account is<br/> kind of account that is <br/>used just for making<br/> reservations.</span>
                    </ReactTooltip>
                </div>
            </div>
            {provider ?
                <label>
                    System name
                    <input
                        className={'input-primary fl '.concat(error.trim().length !== 0 ? "error" : "")}
                        type={'text'}
                        value={system}
                        placeholder={'Insacek'}
                        onClick={() => setError('')}
                        onChange={(e) => {
                            setSystem(e.target.value)
                        }}
                    />
                </label>
                : null}
            <div className={styles.errorMessage}>
                {error}
            </div>
            <button
                type={'submit'}
                className={'button-primary bx-sh'}
            >Register
            </button>
        </form>
    )
}

export const RegistrationPage = () => {
    return (
        <div className={styles.container}>
            <Form/>
        </div>
    )
}