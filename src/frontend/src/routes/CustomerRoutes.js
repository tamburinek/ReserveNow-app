import {Route, Routes} from "react-router-dom";
import React from 'react';

import {NavbarCustomer} from "../parts";
import {
    DashboardPageCustomer,
    EventsPageCustomer,
    ProfilePageCustomer,
    ReservationsPageCustomer,
} from "../pages";

export const CustomerRoutes = () => {
    return (
        <React.Fragment>
            <NavbarCustomer/>
            <Routes>
                <Route exact path="/dashboard" element={<DashboardPageCustomer/>}/>
                <Route exact path="/terminy" element={<EventsPageCustomer/>}/>
                <Route exact path="/rezervace" element={<ReservationsPageCustomer/>}/>
                <Route exact path="/profil" element={<ProfilePageCustomer/>}/>
            </Routes>
        </React.Fragment>
    )
}