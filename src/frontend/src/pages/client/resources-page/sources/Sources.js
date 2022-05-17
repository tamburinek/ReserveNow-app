import styles from './Sources.module.scss';
import {useState, useEffect, useMemo} from "react";
import {useTable, useFilters, usePagination} from "react-table";
import MOCK_DATA from "./MOCK_DATA.json"
import {ModalDeleteConfirm} from "../../customers-page/modalWindowDeleteConfirm/ModalDeleteConfirm";
import {ModalNew} from "./modalWindowNew/ModalNew";
import {ModalEdit} from "./modalWindowEdit/ModalEdit";
import telephoneService from "../../../../services/telephoneService";
import axios from "axios";
import {baseUrl} from "../../../../config/const";
import authHeader from "../../../../services/auth-header";

const Table = ({columns, data}) => {
    const [confirm, setConfirm] = useState(false)
    const [edit, setEdit] = useState(false)

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
        <></>
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

export const Sources = () => {
    const [newRes, setNewRes] = useState(false)
    const data = useMemo(() => MOCK_DATA, [])
    const columns = useMemo(() => [

            {
                Header: "Plný název",
                accessor: "full_name",
                Filter: Filter
            },
            {
                Header: "Název",
                accessor: "title",
                Filter: Filter
            },
            {
                Header: "Služba",
                accessor: "service",
                Filter: Filter
            },
            {
                Header: "Místo",
                accessor: "place",
                Filter: Filter
            },
            {
                Header: "Zaměstnanec",
                accessor: "employee",
                Filter: Filter
            },
        ],
        []
    )

    const [resources, setResources] = useState([]);

    useEffect(async () => {
        const fetchSources = await Promise.any([
                axios.get(
                    `${baseUrl}/systems/my/sources`,
                    {headers: authHeader()})
            ]
        )
        setResources(fetchSources.data)

    }, [])


    return (
        <div className={styles.body}>
            {newRes ? <ModalNew onClose={() => setNewRes(false)} show={newRes}/> : null}
            <div className={styles.buttonContainer}>
                <button className={'button-primary '.concat(styles.button)} onClick={() => {
                    setNewRes(true)
                }}>Nový zdroj
                </button>
            </div>
            <div className={styles.table}>
                <div>
                    {/*<table {...getTableProps()}>*/}
                    {/*    <thead>*/}
                    {/*    {headerGroups.map(headerGroup => (*/}
                    {/*        <tr {...headerGroup.getHeaderGroupProps()}>*/}
                    {/*            <td className={styles.collCheckbox}>*/}
                    {/*                <input type="checkbox"/>*/}
                    {/*            </td>*/}
                    {/*            {headerGroup.headers.map(column => (*/}
                    {/*                <td {...column.getHeaderProps()}>*/}
                    {/*                    <p>{column.render('Header')}</p>*/}
                    {/*                    <div>{column.canFilter ? column.render('Filter') : null}</div>*/}
                    {/*                </td>*/}

                    {/*            ))}*/}
                    {/*        </tr>*/}
                    {/*    ))}*/}
                    {/*    </thead>*/}
                    {/*    <tbody {...getTableBodyProps()}>*/}
                    {/*    {page.map((row) => {*/}
                    {/*        prepareRow(row)*/}
                    {/*        return (*/}
                    {/*            <tr className={styles.tRow} {...row.getRowProps()}>*/}
                    {/*                <td className={styles.collCheckbox}>*/}
                    {/*                    <input type="checkbox"/>*/}
                    {/*                </td>*/}
                    {/*                {row.cells.map(cell => {*/}
                    {/*                    return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>*/}
                    {/*                })}*/}
                    {/*                <td>*/}
                    {/*                    <div className={styles.buttonCell}>*/}
                    {/*                        <button className={'button-primary-outline '.concat(styles.buttonEdit)}*/}
                    {/*                                onClick={() => setEdit(true)}>Upravit*/}
                    {/*                        </button>*/}
                    {/*                        <button className={'button-primary-outline '.concat(styles.buttonDelete)}*/}
                    {/*                                onClick={() => setConfirm(true)}>Odstranit*/}
                    {/*                        </button>*/}
                    {/*                    </div>*/}
                    {/*                </td>*/}
                    {/*            </tr>*/}
                    {/*        )*/}
                    {/*    })}*/}
                    {/*    </tbody>*/}
                    {/*</table>*/}
                    <table>
                        <thead>
                        <tr>
                            <th><strong>Name</strong></th>
                            <th><strong>Description</strong></th>
                        </tr>
                        </thead>
                        <tbody>
                        {resources.map(resource => {
                            return (
                                <tr>
                                    <td>{resource.name}</td>
                                    <td>{resource.description}</td>
                                </tr>
                            )
                        })}
                        </tbody>
                    </table>
                </div>
            </div>
            {/*<div className={styles.selectContainer}>*/}
            {/*    <select className={styles.select}>*/}
            {/*        <option selected>-</option>*/}
            {/*        <option value="Remove">Odstranit</option>*/}
            {/*    </select>*/}
            {/*    <button className={'button-primary sm '}>Aplikovat</button>*/}
            {/*</div>*/}
        </div>
    )
}

