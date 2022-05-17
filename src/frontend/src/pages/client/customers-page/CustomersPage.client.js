import styles from './CustomersPage.module.scss'
import MOCK_DATA from "./MOCK_DATA.json"
import {useState, useMemo, useEffect} from "react";
import {ModalNewCustomer} from "./modalWindowNewCustomer/ModalNewCustomer";
import {ModalDeleteConfirm} from "./modalWindowDeleteConfirm/ModalDeleteConfirm";
import {useTable, useFilters, usePagination} from "react-table";
import telephoneService from "../../../services/telephoneService";
import axios from "axios";
import {baseUrl} from "../../../config/const";
import authHeader from "../../../services/auth-header";

const Filter = ({column}) => {
    const {filterValue, setFilter} = column;

    return (
        <span>
                <input value={filterValue} onChange={(e) => setFilter(e.target.value)}
                       className={'input-primary search sh sm'} placeholder={'Hledaný text…'}/>
            </span>
    )
}

export const CustomersPageClient = () => {

    const [show, setShow] = useState(false);
    const [showUpdate, setShowUpdate] = useState(false);
    const [confirm, setConfirm] = useState(false);

    const data = useMemo(() => MOCK_DATA, []);
    const columns = useMemo(() => [
            {
                Header: "JMÉNO",
                accessor: "name",
                Filter: Filter
            },
            {
                Header: "EMAIL",
                accessor: "email",
                Filter: Filter
            },
            {
                Header: "TELEFON",
                accessor: "tel",
                Filter: Filter
            },
            {
                Header: "N. REZERVACE",
                accessor: "number",
                Filter: Filter
            },
            {
                Header: "REG. DATUM",
                accessor: "date",
                Filter: Filter
            },
        ], []
    );

    const [customers, setCustomers] = useState([]);

    useEffect(async () => {
        const fetchCustomers = await Promise.any([
                axios.get(`${baseUrl}/systems/my/customers`,
                    {headers: authHeader()})
            ]
        )
        fetchCustomers.data.map(item => item.age)
            .filter((value, index, self) => self.indexOf(value) === index)
        setCustomers(fetchCustomers.data)

    }, [])


    let getSelectedRows = () => {
        this.setState({
            Selected: this.state.List.filter((e) => e.selected),
        });
    }

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        page,
        nextPage,
        previousPage,
        canNextPage,
        canPreviousPage,
        pageOptions,
        state,
        prepareRow,
    } = useTable({
            columns,
            data,
            initialState: {
                pageSize: 6
            },
        },
        useFilters,
        usePagination
    );

    const {pageIndex} = state;

    const [checkedState, setCheckedState] = useState([]);
    const handleChange = (e) => {
        const {checked, name} = e.target;
        if (checked) {
            setCheckedState((oldState) => [...oldState, name]);
        } else {
            setCheckedState((oldState) => oldState.filter((row) => row !== name));
        }
    };

    // console.log(checkedState.join(", "));

    return (
        <div className={styles.container}>
            <div className={styles.buttonContainer}>
                <button className={'button-primary '.concat(styles.button)} onClick={() => setShow(true)}>Nový
                    zákazník
                </button>
                <ModalNewCustomer onClose={() => setShow(false)} show={show}/>
                {/*<ModalDeleteConfirm onClose={() => setConfirm(false)} show={confirm}/>*/}
            </div>
            <div className={styles.tableContainer}>
                {/*<table {...getTableProps()}>*/}
                {/*    <thead>*/}
                {/*    {headerGroups.map(headerGroup => (*/}
                {/*        <tr {...headerGroup.getHeaderGroupProps()} >*/}
                {/*            <td className={styles.collCheckbox}>*/}
                {/*                <label htmlFor="checkbox"/>*/}
                {/*                <input id="checkbox"*/}
                {/*                       type="checkbox"*/}
                {/*                />*/}
                {/*            </td>*/}
                {/*            {headerGroup.headers.map(column => (*/}
                {/*                <td {...column.getHeaderProps()}>*/}
                {/*                    <p>{column.render('Header')}</p>*/}
                {/*                    <div>{column.canFilter ? column.render('Filter') : null}</div>*/}
                {/*                </td>*/}
                {/*            ))}*/}
                {/*            <td></td>*/}
                {/*        </tr>*/}
                {/*    ))}*/}
                {/*    </thead>*/}

                {/*    <tbody {...getTableBodyProps()}>*/}
                {/*    {page.map((row) => {*/}
                {/*        prepareRow(row)*/}
                {/*        return (*/}
                {/*            <tr {...row.getRowProps()}*/}
                {/*                className={checkedState.includes(row.id) ? styles.checkedCheckbox : ''}>*/}
                {/*                <td className={styles.collCheckbox}>*/}
                {/*                    <input type="checkbox"*/}
                {/*                           className={'checkbox'}*/}
                {/*                           onChange={handleChange}*/}
                {/*                           checked={checkedState.includes((row.id))}*/}
                {/*                           name={(row.id).toString()}*/}
                {/*                    />*/}
                {/*                </td>*/}
                {/*                {row.cells.map(cell => {*/}
                {/*                    return <td {...cell.getCellProps()}>{telephoneService.generator()}</td>*/}
                {/*                })}*/}
                {/*                <td>*/}
                {/*                    <div className={styles.buttonsTable}>*/}
                {/*                        <button*/}
                {/*                            className={'button-primary-outline '.concat(styles.buttonEdit)}>Upravit*/}
                {/*                        </button>*/}
                {/*                        <button*/}
                {/*                            className={'button-primary-outline '.concat(styles.buttonDelete)}*/}
                {/*                            onClick={() => setConfirm(true)}>Odstranit*/}
                {/*                        </button>*/}
                {/*                    </div>*/}
                {/*                </td>*/}
                {/*            </tr>*/}
                {/*        )*/}
                {/*    })}*/}
                {/*    </tbody>*/}

                {/*</table>*/}
                {/*<div className={styles.buttonsContainer}>*/}
                {/*    <div>*/}
                {/*        <span>Stranka {pageIndex + 1} z {pageOptions.length} </span>*/}
                {/*        <button onClick={() => previousPage()}*/}
                {/*                disabled={!canPreviousPage}*/}
                {/*                className={'button-primary sm'}>Předchozí*/}
                {/*        </button>*/}
                {/*        <button onClick={() => nextPage()}*/}
                {/*                disabled={!canNextPage}*/}
                {/*                className={'button-primary sm'}>Další*/}
                {/*        </button>*/}
                {/*    </div>*/}
                {/*</div>*/}
                <table>
                    <thead>
                    <tr>
                        <th><strong>Username</strong></th>
                        <th><strong>Email</strong></th>
                        <th><strong>Telephone</strong></th>
                        <th><strong>Firstname</strong></th>
                        <th><strong>Lastname</strong></th>
                    </tr>
                    </thead>
                    <tbody>
                    {customers.map(customer => {
                        return (
                            <tr>
                                <td>{customer.username}</td>
                                <td>{customer.email}</td>
                                <td>{telephoneService.generator()}</td>
                                <td>{customer.firstName}</td>
                                <td>{customer.lastName}</td>
                            </tr>
                        )
                    })}
                    </tbody>
                </table>
            </div>
            <div className={styles.containerFunction}>
                <select>
                    <option selected>-</option>
                    <option value="allowReservations">Povolit rezervaci</option>
                    <option value="DisableReservations">Zakázat rezervaci</option>
                    <option value="Remove">Odstranit</option>
                    <option value="writeEmail">Napsat e-mail</option>
                    <option value="writeSms">Napsat sms</option>
                </select>
                <button className={'button-primary sm '} onClick={() => getSelectedRows()}>Aplikovat</button>
            </div>
        </div>
    )
}