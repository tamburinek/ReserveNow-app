import styles from './ContactSection.module.scss'
import logo from '../../../../assets/resnow.png'

export const ContactSection = () => {
    return (
        <div id={'contact'} className={styles.contactSection}>
            <div className={styles.contents}>
                <div className={styles.content}>
                    <img src={logo} alt={'logo'}/>
                </div>
                <div className={styles.content}>
                    <div className={styles.contactHeader}>
                        Contact info
                    </div>
                    <div className={styles.contactsInfo}>
                        <div className={styles.contactInfo}>
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none"
                                 viewBox="0 0 24 24">
                                <path stroke="#006AFF" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                                      d="M12 13a3 3 0 100-6 3 3 0 000 6z"/>
                                <path stroke="#006AFF" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                                      d="M12 2a8 8 0 00-8 8c0 1.892.402 3.13 1.5 4.5L12 22l6.5-7.5c1.098-1.37 1.5-2.608 1.5-4.5a8 8 0 00-8-8v0z"/>
                            </svg>
                            2715 Ash Dr. San Jose,<br/> South Dakota 83475
                        </div>
                        <div className={styles.contactInfo}>
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="19" fill="none"
                                 viewBox="0 0 20 19">
                                <path stroke="#006AFF" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                                      d="M1 1.116c0 11.63 5.76 17.102 18 17.102v-5.473l-5.04-1.368-1.44 2.052c-2.88 0-6.48-3.42-6.48-6.156L8.2 5.905 6.76 1.116H1z"/>
                            </svg>
                            (219) 555-0114
                        </div>
                        <div className={styles.contactInfo}>
                            <svg xmlns="http://www.w3.org/2000/svg" width="22" height="16" fill="none"
                                 viewBox="0 0 22 16">
                                <path fill="#006AFF"
                                      d="M3.575.72h14.85c.913 0 1.792.302 2.456.844.663.541 1.062 1.282 1.113 2.07l.006.174v9.026c0 .788-.35 1.547-.976 2.12-.628.574-1.485.918-2.397.963l-.202.004H3.575c-.913 0-1.792-.301-2.455-.843-.664-.542-1.063-1.282-1.114-2.07L0 12.835V3.808c0-.789.35-1.548.976-2.12.628-.574 1.485-.918 2.397-.963L3.575.72zM20.35 5.825l-8.965 4.076a.926.926 0 01-.663.04l-.106-.04L1.65 5.827v7.008c0 .417.182.819.509 1.126.327.307.776.496 1.258.53l.158.006h14.85c.483 0 .949-.157 1.304-.44.356-.282.575-.67.614-1.086l.007-.136V5.825zm-1.925-3.68H3.575c-.483 0-.949.157-1.304.44-.355.283-.575.67-.614 1.086l-.007.137v.408L11 8.466l9.35-4.251v-.407c0-.417-.182-.82-.51-1.127a2.054 2.054 0 00-1.258-.53l-.157-.006z"/>
                            </svg>
                            info@reservenow.com
                        </div>
                    </div>
                </div>
                <div className={styles.content}>
                    <div className={styles.formHeader}>
                        Get in touch
                    </div>
                    <div className={styles.form} autoComplete="off">
                        <input className={'input-primary'} placeholder={'Your name'} ></input>
                        <input className={'input-primary'} placeholder={'Your email'}></input>
                        <textarea className={'input-primary'} placeholder={'Your message'}></textarea>
                        <button className={'button-primary'}>Send</button>
                    </div>
                </div>
            </div>
        </div>
    )
}