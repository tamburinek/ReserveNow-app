import './App.scss';
import {Route, Routes} from "react-router-dom";
import React, {useEffect, useState} from "react";

import {
    DocumentationPage,
    IndexPage,
    LoginPage,
    RegistrationPage
} from "./pages";
import {ClientRoutes} from "./routes/ClientRoutes";
import {CustomerRoutes} from "./routes/CustomerRoutes";
import AuthService from "./services/auth.service";
import AboutUs from "./pages/global/welcome-page/AboutUs";

const App = () => {

    const [regularUser, setRegularUser] = useState(false);
    const [admin, setAdmin] = useState(false);
    const [systemOwner, setSystemOwner] = useState(false);
    const [systemEmployee, setSystemEmployee] = useState(false);
    const [currentUser, setCurrentUser] = useState(undefined);

    useEffect(() => {
        const user = AuthService.getCurrentUser();
        if (user) {
            setCurrentUser(user);
            setRegularUser(user.roles.includes("ROLE_REGULAR_USER"));
            setAdmin(user.roles.includes("ROLE_ADMIN"));
            setSystemOwner(user.roles.includes("ROLE_SYSTEM_OWNER"));
            setSystemEmployee(user.roles.includes("ROLE_SYSTEM_EMPLOYEE"));
        }
    }, []);

    return (
        <div>
            <Routes>
                <Route exact path="/" element={<IndexPage user={currentUser}/>}/>
                <Route exact path="/login" element={<LoginPage/>}/>
                <Route exact path="/register" element={<RegistrationPage/>}/>
                <Route exact path="/about" element={<AboutUs/>}/>
                <Route exact path="/documentation" element={<DocumentationPage/>}/>

                {regularUser && (
                    <Route path="/app/*" element={<CustomerRoutes/>}/>
                )}

                {systemOwner && (
                    <Route path="/app/*" element={<ClientRoutes/>}/>
                )}

                {systemEmployee && (
                    <Route path="/app/*" element={<ClientRoutes/>}/>
                )}
            </Routes>
        </div>
    )
}

export default App;
