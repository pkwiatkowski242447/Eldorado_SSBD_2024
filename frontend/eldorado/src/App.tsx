import './App.css';
import {BrowserRouter as Router} from "react-router-dom";
import {RoutesComponents} from "./router/Routes/index";
import {AccountStateContextProvider} from "./context/AccountContext";
import './i18n';
import SessionHandler from "@/components/SessionHandler.tsx";

function App() {
    return (
        <AccountStateContextProvider>
            <Router>
                <SessionHandler/>
                <RoutesComponents/>
            </Router>
        </AccountStateContextProvider>
    )
}

export default App;
