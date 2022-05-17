import styles from './Modal.module.scss'
import {Chart as ChartJS, ArcElement, Tooltip, Legend} from 'chart.js';
import {Pie} from 'react-chartjs-2';

ChartJS.register(ArcElement, Tooltip, Legend);

export const Modal = (props) => {
    if (!props.show) return null;

    const current = new Date();

    return (
        <div className={styles.modal} onClick={props.onClose}>
            <div className={styles.content} onClick={(e) => e.stopPropagation()}>
                <h3>{current.getDate()}.{current.getMonth() + 1}.{current.getFullYear()}</h3>
                <div className={styles.topContent}>
                    {/*<div className={styles.pie}>*/}
                    {/*    <Pie data={props.data} options={{*/}
                    {/*        maintainAspectRatio: false*/}
                    {/*    }}/>*/}
                    {/*</div>*/}
                    <div className={styles.timeline}>
                        <table>
                            <tr className={styles.times}>
                                <th></th>
                                <th scope="col">6</th>
                                <th scope="col">7</th>
                                <th scope="col">8</th>
                                <th scope="col">9</th>
                                <th scope="col">10</th>
                                <th scope="col">11</th>
                                <th scope="col">12</th>
                                <th scope="col">13</th>
                                <th scope="col">14</th>
                                <th scope="col">15</th>
                                <th scope="col">16</th>
                                <th scope="col">17</th>
                                <th scope="col">18</th>
                                <th scope="col">19</th>
                                <th scope="col">20</th>
                            </tr>
                            <tr>
                                <th scope="row"></th>
                            </tr>
                            <tr className={styles.place}>
                                <th scope="row">Tenisový kurt</th>
                                <td className={styles.active}></td>
                                <td className={styles.active}></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td className={styles.active}></td>
                                <td className={styles.active}></td>
                                <td className={styles.active}></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <th scope="row"></th>
                            </tr>
                            <tr className={styles.place}>
                                <th scope="row">Florbalová hala</th>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td className={styles.active}></td>
                                <td className={styles.active}></td>
                                <td className={styles.active}></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <th scope="row"></th>
                            </tr>
                            <tr className={styles.place}>
                                <th scope="row">Vnitřní hala</th>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td className={styles.active}></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td className={styles.active}></td>
                                <td></td>
                            </tr>
                            <tr>
                                <th scope="row"></th>
                            </tr>
                            <tr className={styles.place}>
                                <th scope="row">Sauna</th>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td className={styles.active}></td>
                                <td className={styles.active}></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <th scope="row"></th>
                            </tr>
                            <tr className={styles.place}>
                                <th scope="row">Trávník</th>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </tr>
                            <tr>
                                <th scope="row"></th>
                            </tr>
                            <tr className={styles.place}>
                                <th scope="row">Bazén</th>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td className={styles.active}></td>
                                <td className={styles.active}></td>
                                <td className={styles.active}></td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div className={styles.bottomContent}>
                    <table>
                        <tr>
                            <th>Termín</th>
                            <th>Zákazník</th>
                            <th>Datum vytvoření rezervace</th>
                            <th>ID rezervace</th>
                            <th></th>
                        </tr>
                        <tr>
                            <td>10:00 - 14:00</td>
                            <td>Ludmila Spojitá</td>
                            <td>18.03.2022</td>
                            <td>5d43f2</td>
                            <td><a href={'#'}>Zobrazit rezervaci</a></td>
                        </tr>
                        <tr>
                            <td>12:00 - 16:00</td>
                            <td>Martin Novotný</td>
                            <td>18.03.2022</td>
                            <td>45fdg5</td>
                            <td><a href={'#'}>Zobrazit rezervaci</a></td>
                        </tr>
                        <tr>
                            <td>10:00 - 14:00</td>
                            <td>Ludmila Spojitá</td>
                            <td>18.03.2022</td>
                            <td>5d43f2</td>
                            <td><a href={'#'}>Zobrazit rezervaci</a></td>
                        </tr>
                        <tr>
                            <td>10:00 - 14:00</td>
                            <td>Ludmila Spojitá</td>
                            <td>18.03.2022</td>
                            <td>5d43f2</td>
                            <td><a href={'#'}>Zobrazit rezervaci</a></td>
                        </tr>
                        <tr>
                            <td>10:00 - 14:00</td>
                            <td>Ludmila Spojitá</td>
                            <td>18.03.2022</td>
                            <td>5d43f2</td>
                            <td><a href={'#'}>Zobrazit rezervaci</a></td>
                        </tr>
                        <tr>
                            <td>10:00 - 14:00</td>
                            <td>Ludmila Spojitá</td>
                            <td>18.03.2022</td>
                            <td>5d43f2</td>
                            <td><a href={'#'}>Zobrazit rezervaci</a></td>
                        </tr>
                        <tr>
                            <td>10:00 - 14:00</td>
                            <td>Ludmila Spojitá</td>
                            <td>18.03.2022</td>
                            <td>5d43f2</td>
                            <td><a href={'#'}>Zobrazit rezervaci</a></td>
                        </tr>
                        <tr>
                            <td>10:00 - 14:00</td>
                            <td>Ludmila Spojitá</td>
                            <td>18.03.2022</td>
                            <td>5d43f2</td>
                            <td><a href={'#'}>Zobrazit rezervaci</a></td>
                        </tr>
                        <tr>
                            <td>10:00 - 14:00</td>
                            <td>Ludmila Spojitá</td>
                            <td>18.03.2022</td>
                            <td>5d43f2</td>
                            <td><a href={'#'}>Zobrazit rezervaci</a></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    )
}