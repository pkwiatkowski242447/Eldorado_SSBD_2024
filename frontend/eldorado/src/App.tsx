import './App.css';
import {BrowserRouter as Router} from "react-router-dom";
import {RoutesComponents} from "./router/Routes/index";
import {AccountStateContextProvider} from "./context/AccountContext";
import './i18n';

function App() {

    return (
        <AccountStateContextProvider>
            <Router>
                <RoutesComponents/>
            </Router>
        </AccountStateContextProvider>
    )
}

export default App;
