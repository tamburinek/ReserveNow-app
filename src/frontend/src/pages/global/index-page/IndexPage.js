import {NabvarIndex} from "../../../parts";
import {HomeSection} from "./home-section/HomeSection";
import {AboutSection} from "./about-section/AboutSection";
import {PricingSection} from "./pricing-section/PricingSection";
import {ContactSection} from "./contact-section/ContactSection";

import styles from './index.module.scss'

export const IndexPage = (props) => {

    return (
        <div>
            <NabvarIndex user={props.user}/>
            <div className={styles.mainContainer}>
                <HomeSection user={props.user}/>
                <AboutSection/>
                <PricingSection/>
                <ContactSection/>
            </div>
        </div>
    )
}
