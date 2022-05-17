import styles from './PricingSection.module.scss'
import {useState} from "react";

const text = {
    text1: (
        <h3>
            This pack is for
            <br/> Free
        </h3>
    ),
    text2: (
        <h3>
            This pack is for
            <br/> 99$
        </h3>
    ),
    text3:(
        <h3>
            This pack is for
            <br/> 999$
        </h3>
    )
};

export const PricingSection = () => {

    const [isFreeActive, setFreeActive] = useState(true);
    const [isPremiumActive, setPremiumActive] = useState(false);
    const [isBusinessActive, setBusinessActive] = useState(false);

    const [description, setDescription] = useState(text.text1);
    const [activeClassName, setActiveClassName] = useState(styles.free);
    const [buttonText, setButtonText] = useState('Try');

    const onClickFree = () => {
        setBusinessActive(false);
        setPremiumActive(false);
        setFreeActive(true);
        setActiveClassName(styles.free);
        setDescription(text.text1);
        setButtonText("Try");
    }

    const onClickPremium = () => {
        setBusinessActive(false);
        setPremiumActive(true);
        setFreeActive(false);
        setActiveClassName(styles.premium);
        setDescription(text.text2);
        setButtonText("Buy");
    }

    const onClickBusiness = () => {
        setBusinessActive(true);
        setPremiumActive(false);
        setFreeActive(false);
        setActiveClassName(styles.business);
        setDescription(text.text3);
        setButtonText("Buy");
    }

    return (
        <div id={'pricing'} className={styles.pricingSection}>
            <div className={styles.contentHeader}>
                <h1>What’s your plan?</h1>
            </div>
            <div className={styles.contentBody}>
                <div className={styles.description}>
                    <h1>
                        Boost your <br/> business now!
                    </h1>
                    <p>
                        Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been
                        the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley
                        of type and scrambled it to make a type.
                    </p>
                </div>
                <div className={styles.board}>
                    <div className={styles.tab} id={'tab'}>
                        <button type={"button"} onClick={() => onClickFree()}
                                className={isFreeActive ? styles.activeFree : ""}>
                            Free
                            <div className={styles.circleR}/>
                        </button>
                        <button type={"button"} onClick={() => onClickPremium()}
                                className={isPremiumActive ? styles.activePremium : ""}>
                            Premium
                            <div className={styles.circleR}/>
                            <div className={styles.circleL}/>
                        </button>
                        <button type={"button"} onClick={() => onClickBusiness()}
                                className={isBusinessActive ? styles.activeBusiness : ""}>
                            Business
                            <div className={styles.circleL}/>
                        </button>
                    </div>
                    <div className={styles.main + " " + activeClassName} id={'pricing-table'}>
                        <div className={styles.content}>
                            {description}
                        </div>
                        <button type={"button"} className={styles.try}>{buttonText}</button>
                    </div>
                </div>
            </div>
        </div>
    )
}