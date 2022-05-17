import styles from './ProfilePage.module.scss'
import telephoneService from "../../../services/telephoneService";
import AuthService from "../../../services/auth.service";
import {useEffect, useState} from "react";
import axios from "axios";
import {baseUrl} from "../../../config/const";
import authHeader from "../../../services/auth-header";


const FormUser = () => {
    const user = AuthService.getCurrentUser();

    const [username, setUsername] = useState(user.username);
    const [email, setEmail] = useState(user.email);
    const [oldPassword, setOldPassword] = useState('');
    const [password, setPassword] = useState('');
    const [rePassword, setRePassword] = useState('');
    const [error, setError] = useState('');


    const handle = (e) => {
        if (username.trim().length === 0 || email.trim().length === 0 || oldPassword.trim().length === 0 || password.trim().length === 0 || rePassword.trim().length === 0) {
            setError('Please fill data')
            e.preventDefault();
        } else {
            if (password === rePassword) {
                axios.post(`${baseUrl}/users/signin`, {
                    "username": user.username, "password": oldPassword
                }).then(r => {
                    console.log(r)
                    axios.post(`${baseUrl}/updateProfile`, {
                        "username": username, "email": email, "password": password
                    }, {
                        headers: authHeader(), params: {
                            "username": username, "email": email, "password": password
                        }
                    }).then(r => {
                        axios.post(`${baseUrl}/users/signin`, {
                            "username": user.username, "password": oldPassword
                        }).then(r => {
                            if (r.data.accessToken) {
                                localStorage.setItem('user', JSON.stringify(r.data));
                            }
                            window.location.reload();
                        })
                    })
                        .catch(e => {
                            setError(e)
                        })
                })
                    .catch(e => {
                        setError(e)
                    })
            } else {
                setError("Passwords does not match")
            }
        }

    }

    return (<form className={styles.userForm}>
        <h2>Profile</h2>
        <label>
            Username
            <input
                type={'text'}
                className={'input-primary'}
                value={username}
                onChange={(e) => {
                    setUsername(e.target.value)
                }}
            />
        </label>
        <label>
            Email
            <input
                type={'email'}
                className={'input-primary'}
                value={email}
                onChange={(e) => {
                    setEmail(e.target.value)
                }}
            />
        </label>
        <label>
            Old Password
            <input
                type={'password'}
                className={'input-primary'}
                value={oldPassword}
                onChange={(e) => {
                    setOldPassword(e.target.value)
                }}
            />
        </label>
        <label>
            New Password
            <input
                type={'password'}
                className={'input-primary'}
                value={password}
                onChange={(e) => {
                    setPassword(e.target.value)
                }}
            />
        </label>
        <label>
            Repeat New Password
            <input
                type={'password'}
                className={'input-primary'}
                value={rePassword}
                onChange={(e) => {
                    setRePassword(e.target.value)
                }}
            />
        </label>
        <div className={styles.error}>
            {error}
        </div>
        <button type={'button'} onClick={(e) => {
            handle(e)
        }} className={'button-primary'}>Save
        </button>
    </form>)
}

export const ProfilePageClient = () => {

    return (<div className={styles.container}>
        <div className={styles.content}>
            <FormUser/>
        </div>
    </div>)
}