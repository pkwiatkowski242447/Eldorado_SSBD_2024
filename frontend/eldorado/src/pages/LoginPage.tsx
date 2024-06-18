import { useEffect, useState } from 'react';
import LoginForm from "../components/forms/LoginForm";
import eldoLogo from "../assets/eldorado.png";
import {api} from "@/api/api.ts";

function LoginPage() {
    const [backgroundImage, setBackgroundImage] = useState('');
    const [imageLoaded, setImageLoaded] = useState(false);

    const fetchImage = async () => {
        const response = await api.getRandomImage();
        const base64Image = `data:image/jpeg;base64,${response.data}`;
        setBackgroundImage(base64Image);
        setImageLoaded(true);
    };

    useEffect(() => {
        fetchImage();
    }, []);

    return (
        <>
            <div style={{
                position: 'fixed',
                top: 0,
                left: 0,
                right: 0,
                bottom: 0,
                backgroundImage: `url(${backgroundImage})`,
                backgroundSize: 'cover',
                backgroundRepeat: 'no-repeat',
                backgroundPosition: 'center',
                transition: 'opacity 1s ease-in-out',
                opacity: imageLoaded ? 0.15 : 0,
            }}/>
            <div style={{position: 'relative'}}>
                <a href="/home" className="flex items-center">
                    <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2"/>
                    <span className="sr-only">Eldorado</span>
                </a>
                <LoginForm/>
            </div>
        </>
    )
}

export default LoginPage