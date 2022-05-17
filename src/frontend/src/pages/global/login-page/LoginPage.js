import styles from './LoginPage.module.scss'
import {useEffect, useState} from "react";
import {useNavigate} from 'react-router-dom';
import AuthService from "../../../services/auth.service";
import logo from "../../../assets/resnow.png";
import backgroundImg from "../../../assets/loginPage/login-page-background.png";

const Form = () => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const [error, setError] = useState('');

    let navigate = useNavigate();

    useEffect(()=>{
        AuthService.logout();
    },[])

    const login = (e) => {
        e.preventDefault();
        AuthService.login(username, password).then(
            () => {
                navigate("/app/dashboard");
                window.location.reload();
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
        )
    }

    return (
            <form className={styles.form} onSubmit={(e) => login(e)}>
                <div className={styles.flexRow}>
                    <img src={logo} alt={'logo'}/>
                    <h2>Login</h2>
                </div>
                    <label>
                        Username
                    <input
                        className={'input-primary '.concat(error.trim().length !== 0 ? "error" : "")}
                        type={'text'}
                        value={username}
                        placeholder={'username'}
                        autoComplete={'username'}
                        onChange={(e) => setUsername(e.target.value)}
                    />

                    </label>
                <label>
                    Password
                    <input
                        className={'input-primary '.concat(error.trim().length !== 0 ? "error" : "")}
                        type={'password'}
                        value={password}
                        autoComplete={'current-password'}
                        onChange={(e) => setPassword(e.target.value)}
                    />

                </label>
                <div className={styles.errorMessage}>
                {error}
                </div>
                <button className={'button-primary'}>Login</button>
            </form>
    )
}

export const LoginPage = () => {
    return (
        <div className={styles.container}>
            <div className={styles.background}>
                <img src={backgroundImg} alt={'bg img'}/>
            </div>
            <Form/>
        </div>
    )
}