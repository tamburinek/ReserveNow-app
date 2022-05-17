import styles from './WelcomePage.module.scss'

const AboutUs = () => {

    return (
        <div className={styles.presentationContainer}>
            <iframe
                src="https://docs.google.com/presentation/d/e/2PACX-1vRrcRGj9YI9psmWZ4alkZ47gHoq6-nRE47RHO7LpgxLUcfADH-kmPtdyxI0diCmvR0S-9jDH_jK1xCP/embed?start=false&loop=true&delayms=15000"
                allowFullScreen="true" mozallowfullscreen="true"
                webkitallowfullscreen="true"/>
        </div>
    )
}

export default AboutUs