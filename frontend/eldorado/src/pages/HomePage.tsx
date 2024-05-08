
import {useNavigate} from "react-router-dom"; // Import useHistory hook
import {Pathnames} from "../router/pathnames";
import {Button} from "@/components/ui/button.tsx";
import eldoLogo from "@/assets/eldorado.png";

function HomePage() {
    const navigate = useNavigate()

    // Function to handle redirection to login page
    const redirectToLoginPage = () => {
        navigate(Pathnames.public.login); // Redirect to login page
    };

    return (
        <div>
            <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-2/3"/>
            <Button onClick={redirectToLoginPage}>Zaloguj się</Button>
        </div>
    );
}

export default HomePage;
