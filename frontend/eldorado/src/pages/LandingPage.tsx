import {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';

function LandingPage() {
    const navigate = useNavigate();

    useEffect(() => {
        navigate('/home');
    }, [navigate]);

    return null;
}

export default LandingPage;