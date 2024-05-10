import eldoLogo from "../assets/eldorado.png";
import RegisterForm from "@/components/forms/RegisterForm.tsx";

function RegisterPage() {
    return (
        <div>
            <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2"/>
            <RegisterForm/>
        </div>
    )
}

export default RegisterPage