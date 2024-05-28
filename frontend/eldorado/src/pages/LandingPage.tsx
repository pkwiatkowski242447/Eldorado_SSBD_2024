import {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';

function LandingPage() {
    const navigate = useNavigate();

    useEffect(() => {
        navigate('/home');
    }, [navigate]);

    return <div></div>;
}

export default LandingPage;