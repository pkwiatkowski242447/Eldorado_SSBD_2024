import LoginForm from "../components/forms/LoginForm";
import eldoLogo from "../assets/eldorado.png";

function LoginPage() {
    return (
        <div>
            <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2" />
            <LoginForm/>
        </div>
    )
}

export default LoginPage