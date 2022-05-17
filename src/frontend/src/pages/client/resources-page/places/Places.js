import styles from './Places.module.scss'
import {useState, useMemo, useEffect} from "react";
import {useTable, useFilters, usePagination} from "react-table";
import MOCK_DATA from "./MOCK_DATA.json"
import {ModalNew} from "../sources/modalWindowNew/ModalNew";
import {ModalDeleteConfirm} from "../../customers-page/modalWindowDeleteConfirm/ModalDeleteConfirm";
import {ModalEdit} from "../sources/modalWindowEdit/ModalEdit";
import axios from "axios";
import {baseUrl} from "../../../../config/const";
import authHeader from "../../../../services/auth-header";
import {Modal} from "./Modal";

export const Places = () => {
    const data = useMemo(() => MOCK_DATA, [])
    const columns = useMemo(() => [

            {
                Header: "Název",
                accessor: "title",
                Filter: Filter
            },
            {
                Header: "Popis",
                accessor: "description",
                Filter: Filter
            },
        ],
        []
    )

    const [open, setOpen] = useState(false);
    const [resources, setResources] = useState([]);

    useEffect(async () => {
        const fetchSources = await Promise.any([
                axios.get(
                    `${baseUrl}/systems/my/sources`,
                    {headers: authHeader()})
            ]
        )

        console.log(fetchSources.data)
        setResources(fetchSources.data)

    }, [])

    return (
        <div className={styles.body}>
            <div className={styles.buttonContainer}>
                {/*<button className={'button-primary '.concat(styles.button)} onClick={() => setOpen(true)}>Nové místo*/}
                {/*</button>*/}
            </div>
            {open ? <Modal onClose={() => setOpen(false)}/> : null}
            <div className={styles.table}>
                {/*<Table columns={columns} data={data}/>*/}
                <table>
                    <thead>
                    <tr>
                        <th><strong>City</strong></th>
                        <th><strong>Street</strong></th>
                        <th><strong>House Number</strong></th>
                        <th><strong>Postal Code</strong></th>
                    </tr>
                    </thead>
                    <tbody>
                    {resources.map(resource => {
                        return (
                            <tr>
                                <td>{resource.address.city}</td>
                                <td>{resource.address.street}</td>
                                <td>{resource.address.houseNumber}</td>
                                <td>{resource.address.postalCode}</td>
                            </tr>
                        )
                    })}
                    </tbody>
                </table>
            </div>
        </div>
    )
}

const Table = ({columns, data}) => {

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
        },
        useFilters,
        usePagination
    )

    const {pageIndex} = state

    return (
        <>
            <table {...getTableProps()}>
                <thead>
                {headerGroups.map(headerGroup => (
                    <tr {...headerGroup.getHeaderGroupProps()}>
                        <td className={styles.collCheckbox}>
                            <input type="checkbox"/>
                        </td>
                        {headerGroup.headers.map(column => (
                            <td className={styles.td} {...column.getHeaderProps()}>
                                <p>{column.render('Header')}</p>
                                <div>{column.canFilter ? column.render('Filter') : null}</div>
                            </td>

                        ))}
                    </tr>
                ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                {page.map((row) => {
                    prepareRow(row)
                    return (
                        <tr className={styles.tRow} {...row.getRowProps()}>
                            <td className={styles.collCheckbox}>
                                <input type="checkbox"/>
                            </td>
                            {row.cells.map(cell => {
                                return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                            })}
                            <td>
                                <div className={styles.buttonCell}>
                                    <button className={'button-primary-outline '.concat(styles.buttonEdit)}>Upravit
                                    </button>
                                    <button
                                        className={'button-primary-outline '.concat(styles.buttonDelete)}>Odstranit
                                    </button>
                                </div>
                            </td>
                        </tr>
                    )
                })}
                </tbody>
            </table>
            <div className={styles.buttonsContainer}>
                <div>
                    <span>Page {pageIndex + 1} of {pageOptions.length} </span>
                    <button onClick={() => previousPage()} disabled={!canPreviousPage}
                            className={'button-primary sm'}>Předchozí
                    </button>
                    <button onClick={() => nextPage()} disabled={!canNextPage} className={'button-primary sm'}>Další
                    </button>
                </div>
            </div>
        </>
    )
}

const Filter = ({column}) => {
    const {filterValue, setFilter} = column

    return (
        <span>
            <input value={filterValue} onChange={(e) => setFilter(e.target.value)}
                   className={'input-primary search sh sm'} placeholder={'Hledaný text…'}/>
        </span>
    )
}