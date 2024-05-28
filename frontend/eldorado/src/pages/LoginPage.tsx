import LoginForm from "../components/forms/LoginForm";
import eldoLogo from "../assets/eldorado.png";

function LoginPage() {
    return (
        <div>
            <a href="/home" className="flex items-center">
                <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2"/>
                <span className="sr-only">Eldorado</span>
            </a>
            <LoginForm/>
        </div>
    )
}

export default LoginPage