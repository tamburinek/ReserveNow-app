import styles from './NavbarCustomer.module.scss'

import logo from '../../../assets/resnow.png'
import profileSVG from '../../../assets/svg/profile.svg'
import settingSVG from '../../../assets/svg/setting.svg'
import notifySVG from '../../../assets/svg/notify.svg'
import {Link, useLocation, useNavigate} from "react-router-dom";
import logoutSVG from "../../../assets/svg/logout.svg";
import AuthService from "../../../services/auth.service";

export const NavbarCustomer = () => {
    const location = useLocation();
    let navigate = useNavigate();

    const logout = () =>{
        AuthService.logout();
        navigate('/');
        window.location.reload()
    }

    const {pathname} = location;
    const splitLocation = pathname.split("app/");

    return (
        <nav className={styles.menu}>
            <div className={styles.leftSideMenu}>
                <Link to={"dashboard"}>
                    <img src={logo} alt={'logo'}/>
                </Link>
                <Link to={'dashboard'}
                   className={splitLocation[1] === "dashboard" ? styles.active : ""}>Dashboard</Link>
                {/*<Link to={'terminy'} className={splitLocation[1] === "terminy" ? styles.active : ""}>Eventy</Link>*/}
                <Link to={'rezervace'}
                   className={splitLocation[1] === "rezervace" ? styles.active : ""}>Rezervace</Link>
            </div>
            <div className={styles.rightSideMenu}>
                <div className={styles.iconContainer}>
                    <Link to={'profil'}><img src={profileSVG} alt={'icon'}/></Link>
                    <Link to={'#'} onClick={()=>logout()}><img src={logoutSVG} alt={'icon'}/></Link>
                </div>
                <div className={styles.searchContainer}>
                    <input className={'input-primary search sh sm'} placeholder={'Find me'}/>
                    <button className={'button-primary-outline'} type={'button'}>Search</button>
                </div>
            </div>
        </nav>
    )
}