import eldoLogo from "../assets/eldorado.png";
import RegisterForm from "@/components/forms/RegisterForm.tsx";

function RegisterPage() {
    return (
        <div>
            <a href="/home" className="flex items-center">
                <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2"/>
                <span className="sr-only">Eldorado</span>
            </a>
            <RegisterForm/>
        </div>
    )
}

export default RegisterPage