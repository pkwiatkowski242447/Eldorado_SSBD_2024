import React from "react";
import {useNavigate} from "react-router-dom"; // Import useHistory hook
import {Button} from "react-bootstrap";
import {Pathnames} from "../router/pathnames";

function HomePage() {
    const navigate = useNavigate()

    // Function to handle redirection to login page
    const redirectToLoginPage = () => {
        navigate(Pathnames.public.login); // Redirect to login page
    };

    return (
        <div>
            <h1>Nie każdy musi być informatykiem</h1>
            <Button onClick={redirectToLoginPage}>Zaloguj się</Button>
        </div>
    );
}

export default HomePage;
